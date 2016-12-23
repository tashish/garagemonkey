/*
 * Copyright 2016 Ashish Tiwari (ashishtiwari@asu.edu)
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package edu.asu.garagemonkey.util;

import edu.asu.garagemonkey.common.DatabaseConnection;
import edu.asu.garagemonkey.model.ParkingSpot;
import edu.asu.garagemonkey.model.User;
import edu.asu.garagemonkey.model.UserType;
import edu.asu.garagemonkey.model.VehicleSize;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods to perform Database operations
 *
 * @author ashishtiwari
 */
public class SQLUtils {
    private static final String CUSTOMER_REGISTRATION_SQL = "INSERT INTO user_details (email, password, f_name, l_name, " +
            "phone, user_type, active) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_USER_SQL = "SELECT * FROM user_details WHERE email=? AND password=? AND active=1";

    private static final String GET_ALL_AVAILABLE_SPOTS = "SELECT * FROM parking_spots where size >= ? AND spot_id NOT IN (\n" +
            "SELECT spot_id FROM reservations " +
            "WHERE " +
            "(start_time >= ? AND start_time <= ?) OR " +
            "(end_time >= ? AND end_time <= ?) OR " +
            "(start_time <= ? AND end_time > ?)) ORDER BY level;";

    private static final String IS_SPOT_STILL_AVAILABLE = "SELECT spot_id FROM reservations WHERE spot_id=? AND (" +
            "(start_time >= ? AND start_time <= ? OR " +
            "(end_time >= ? AND end_time <= ?) OR " +
            "(start_time <= ? AND end_time > ?)))";

    private static final String RESERVE_SPOT_SQL = "INSERT INTO reservations (vehicle_id, spot_id, user_id, start_time, end_time, reserved_by)" +
            "VALUES(?, ?, ?, ?, ?, ?)";

    private static final String GET_RESERVATION_ID_SQL = "SELECT id from reservations WHERE vehicle_id=? AND spot_id=? AND start_time=? AND end_time=?";

    private static final String INSERT_GARAGE_SPOT_SQL = "INSERT INTO parking_spots (level, row, spot_number, size, hourly_rate) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_LAST_INSERTED_SPOT_ID_SQL ="SELECT spot_id FROM parking_spots ORDER BY date_created DESC LIMIT 1 ";

    /**
     * Register a new user to GarageMonkey.user_details table
     *
     * @param user       emailId of the user
     * @param password   password of the user
     * @param connection DatabaseConnection to use to run SQL query
     * @throws SQLException
     */
    public static void registerUser(User user, String password, DatabaseConnection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CUSTOMER_REGISTRATION_SQL);

        statement.setString(1, user.getEmailId());
        statement.setString(2, password);
        statement.setString(3, user.getFirstName());
        statement.setString(4, user.getLastName());
        statement.setString(5, user.getPhone());
        statement.setInt(6, UserType.CUSTOMER.getIntValue());
        statement.setInt(7, 1);

        statement.execute();
    }

    /**
     * Gets a registered user from GarageMonkey.user_details given userid and password
     * Used to validate login
     *
     * @param userId
     * @param pass
     * @param connection
     * @return
     * @throws SQLException
     */
    public static User getUser(String userId, String pass, DatabaseConnection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_USER_SQL);

        statement.setString(1, userId);
        statement.setString(2, pass);

        ResultSet rs = statement.executeQuery();
        User result = null;
        if (rs.next()) {
            String email = rs.getString(1);
            String fName = rs.getString(3);
            String lName = rs.getString(4);
            String phone = rs.getString(5);
            UserType type = UserType.fromIntValue(rs.getInt(6));

            result = new User(fName, lName, email, type, phone);
        }
        return result;
    }

    /**
     * Get a list of all the available parking spots which meet parking requirement (size etc) and which are available
     * during the requested reservation time
     *
     * @param vehicleType
     * @param startTime
     * @param endTime
     * @param connection
     * @return
     * @throws SQLException
     */
    public static List<ParkingSpot> getAvailableParkingSpots(int vehicleType, Timestamp startTime, Timestamp endTime,
                                                             DatabaseConnection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_ALL_AVAILABLE_SPOTS);

        statement.setInt(1, vehicleType);
        statement.setTimestamp(2, startTime);
        statement.setTimestamp(3, endTime);
        statement.setTimestamp(4, startTime);
        statement.setTimestamp(5, endTime);
        statement.setTimestamp(6, startTime);
        statement.setTimestamp(7, endTime);

        ResultSet rs = statement.executeQuery();
        List<ParkingSpot> result = new ArrayList<>();

        while (rs.next()) {
            ParkingSpot spot = new ParkingSpot(
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getString(3),
                    rs.getInt(4),
                    rs.getInt(5),
                    rs.getDouble(6)
            );

            result.add(spot);
        }

        return result;
    }

    /**
     * Method to reserve a parking spot
     * It is statically synchronized to ensure that only one reservation is being made at a time on the webserver
     *
     * @param spotsToChoose Available spots which satisfy reservation criteria
     * @param licensePlate  license plate of the vehicle to park
     * @param reservedBy    emailId of the customer/operator who is requesting this reservation
     * @param connection    Database connection to make this reservation
     * @return ParkingSpot the selected parking spot for this vehicle, null if we could not reserve the spot
     */
    public static synchronized ParkingSpot reserveSpot(List<ParkingSpot> spotsToChoose, String licensePlate, String userId,
                                      Timestamp start, Timestamp end, String reservedBy, DatabaseConnection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_SPOT_STILL_AVAILABLE);
        statement.setTimestamp(2, start);
        statement.setTimestamp(3, end);
        statement.setTimestamp(4, start);
        statement.setTimestamp(5, end);
        statement.setTimestamp(6, start);
        statement.setTimestamp(7, end);

        for(ParkingSpot spot : spotsToChoose) {
            statement.setInt(1, spot.getSpotId());

            ResultSet rs = statement.executeQuery();
            if(!rs.next()) {
                PreparedStatement stmt = connection.prepareStatement(RESERVE_SPOT_SQL);
                stmt.setString(1, licensePlate);
                stmt.setInt(2, spot.getSpotId());
                stmt.setString(3, userId);
                stmt.setTimestamp(4, start);
                stmt.setTimestamp(5, end);
                stmt.setString(6, reservedBy);

                stmt.execute();

                // Get the reservation id for this reservation
                PreparedStatement stmt2 = connection.prepareStatement(GET_RESERVATION_ID_SQL);
                stmt2.setString(1, licensePlate);
                stmt2.setInt(2, spot.getSpotId());
                stmt2.setTimestamp(3, start);
                stmt2.setTimestamp(4, end);

                ResultSet rs2 = stmt2.executeQuery();
                rs2.next();
                spot.setReservationId(rs2.getInt(1));

                return spot;
            }
        }
        return null;
    }

    public static int insertParkingSpot(int level, String row, int spotNumber, int size, double rate, DatabaseConnection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_GARAGE_SPOT_SQL);

        statement.setInt(1, level);
        statement.setString(2, row);
        statement.setInt(3, spotNumber);
        statement.setInt(4, size);
        statement.setDouble(5, rate);
        statement.execute();

        PreparedStatement getStatement = connection.prepareStatement(GET_LAST_INSERTED_SPOT_ID_SQL);
        ResultSet rs = getStatement.executeQuery();
        if(rs == null || !rs.next()) {
            return -1;
        }

        return rs.getInt(1);
    }
}
