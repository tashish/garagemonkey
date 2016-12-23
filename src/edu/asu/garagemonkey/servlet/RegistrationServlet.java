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
import java.io.IOException;

/**
 * Servlet for User registration
 *
 * @author ashishtiwari
 */
public class RegistrationServlet extends HttpServlet {
    private static final String CUSTOMER_REGISTRATION_PAGE = "/WEB-INF/views/customerRegistration.jsp";
    private static Logger logger = Logger.getLogger(RegistrationServlet.class);
    private static final String PAGE_TITLE = "GarageMonkey - User Registration";

    /**
     * Get request on RegistrationServlet will show the user registration page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", PAGE_TITLE);

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(CUSTOMER_REGISTRATION_PAGE);
        dispatcher.forward(request, response);
    }

    /**
     * Post request on RegistrationServlet will process the user registration request and if request has all the valid
     * details, create a new user account
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter(Globals.USER_ID_PARAM);
        String password = request.getParameter(Globals.PASSWORD_PARAM);
        String firstName = request.getParameter(Globals.FIRST_NAME_PARAM);
        String lastName = request.getParameter(Globals.LAST_NAME_PARAM);
        String phone = request.getParameter(Globals.PHONE_PARAM);

        if(userId == null || password == null || firstName == null || lastName == null || phone == null) {
            request.setAttribute("errorMessage", "Invalid input");
            doGet(request, response);
            return;
        }

        if(userId.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
            request.setAttribute("errorMessage", "Invalid input");
            doGet(request, response);
            return;
        }

        User newUser = new User(firstName, lastName, userId, UserType.CUSTOMER, phone);
        boolean registrationResult = false;
        DatabaseConnection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            SQLUtils.registerUser(newUser, password, connection);
            registrationResult = true;
        } catch (Exception ex) {
            logger.error("Error in registering user", ex);
        } finally {
            if(connection != null) {
                connection.returnToPool();
            }
        }

        if(registrationResult) {
            request.setAttribute("registered", "Registration successful");
        } else {
            request.setAttribute("errorMessage", "Could not complete registration. Please try again later.");
        }

        doGet(request, response);
    }
}
