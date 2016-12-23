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
import edu.asu.garagemonkey.model.User;
import edu.asu.garagemonkey.model.UserType;
import edu.asu.garagemonkey.util.SQLUtils;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for Managing parking spots
 *
 * @author ashishtiwari
 */
public class GarageAdminServlet extends HttpServlet {
    private static final String GARAGE_ADMIN_PAGE = "/WEB-INF/views/garage-admin.jsp";
    private static Logger logger = Logger.getLogger(GarageAdminServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User adminUser = (User)session.getAttribute(Globals.USER_DETAILS_KEY);
        if(adminUser == null || !adminUser.getUserType().equals(UserType.ADMINISTRATOR)) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");

        if(action != null && !action.isEmpty() && action.equals("doInsert")) {
            insertSpot(request);
            request.setAttribute(Globals.ADDED_GARAGE_KEY, "addedSpot");
        }

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(GARAGE_ADMIN_PAGE);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void insertSpot(HttpServletRequest request) {
        String level = request.getParameter("level");
        String row = request.getParameter("row");
        String spot = request.getParameter("spot");
        String spotSize = request.getParameter("spotSize");
        String rate = request.getParameter("rate");

        if(level == null || row == null || spot == null || spotSize == null || rate == null) {
            request.setAttribute("errorMessage", "Invalid request, please provide all the parameters");
        }

        if(level.isEmpty() || row.isEmpty() || spot.isEmpty() || spotSize.isEmpty() || rate.isEmpty()) {
            request.setAttribute("errorMessage", "Invalid request, please provide all the parameters");
        }

        DatabaseConnection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            int spotId = SQLUtils.insertParkingSpot(Integer.parseInt(level), row, Integer.parseInt(spot), Integer.parseInt(spotSize), Double.valueOf(rate), connection);

            if(spotId == -1) {
                request.setAttribute("errorMessage", "Could not insert a parking spot. Please refer to logs for more details.");
            } else {
                request.setAttribute("addResult", "Successfully inserted a new parking spot with id: " + spotId);
            }

        } catch (Exception ex) {
            String message = "Error in creating a new parking spot.";
            request.setAttribute("errorMessage", message + "<br/>" +  ex.getMessage());
            logger.error(message, ex);
        } finally {
            if(connection != null) {
                connection.returnToPool();
            }
        }
    }
}
