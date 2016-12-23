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
package edu.asu.garagemonkey.servlet;

import edu.asu.garagemonkey.common.ConnectionPool;
import edu.asu.garagemonkey.common.DatabaseConnection;
import edu.asu.garagemonkey.common.Globals;
import edu.asu.garagemonkey.model.ParkingSpot;
import edu.asu.garagemonkey.model.User;
import edu.asu.garagemonkey.util.SQLUtils;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Servlet for facilitating garage spot search and registration
 *
 * @author ashishtiwari
 */
public class SearchParkingSpotServlet extends HttpServlet {
    private static final String SEARCH_PAGE = "/WEB-INF/views/search.jsp";
    private static Logger logger = Logger.getLogger(SearchParkingSpotServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if(action != null && !action.isEmpty() && action.equals("doSearch")) {
            request.setAttribute(Globals.FIND_SPOTS_KEY, "findspot");
            findSpot(request);
        }

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(SEARCH_PAGE);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void findSpot(HttpServletRequest request) {
        String type = request.getParameter("vehicleType");
        String startDate = request.getParameter("startdate");
        String startTime = request.getParameter("starttime");
        String endDate = request.getParameter("enddate");
        String endTime = request.getParameter("endtime");

        if(type == null || startDate == null || startTime == null || endDate == null || endTime == null) {
            request.setAttribute("errorMessage", "Invalid input");
            return;
        }

        if(type.isEmpty() || startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endTime.isEmpty()) {
            request.setAttribute("errorMessage", "Invalid input");
            return;
        }

        Timestamp start = getDateFromUserInput(startDate, startTime);
        Timestamp end = getDateFromUserInput(endDate, endTime);
        int vehicleType = Integer.parseInt(type);
        String userId = request.getParameter(Globals.USER_ID_PARAM);
        String licensePlate = request.getParameter(Globals.LICENSE_PLATE_PARAM);
        String reservedBy = userId;

        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Globals.USER_DETAILS_KEY);
        if(user != null) {
            reservedBy = user.getEmailId();
        }

        DatabaseConnection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            List<ParkingSpot> availableSpots = SQLUtils.getAvailableParkingSpots(vehicleType, start, end, connection);

            if(availableSpots != null || !availableSpots.isEmpty()) {
                ParkingSpot reservedSpot = SQLUtils.reserveSpot(availableSpots, licensePlate, userId, start, end, reservedBy, connection);
                request.setAttribute(Globals.SPOT_RESERVED, reservedSpot);
            }
        } catch (Exception ex) {
            logger.error("Error in finding parking spot", ex);
        } finally {
            if(connection != null) {
                connection.returnToPool();
            }
        }
    }

    private Timestamp getDateFromUserInput(String date, String time) {
        String[] dateParts = date.split("/"); // MM/DD/YYYY
        String month = dateParts[0];
        String day = dateParts[1];
        String year = dateParts[2];

        String[] timeParts = time.split(":"); //00:00
        String hour = timeParts[0];
        String min = timeParts[1];
        String s = year + "-" + month + "-" + day + " " + hour + ":" + min + ":00";
        return Timestamp.valueOf(s);
    }
}
