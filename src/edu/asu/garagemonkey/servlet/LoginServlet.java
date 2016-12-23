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
 * Servlet for Login page to facilitate user (customer, operator, administrator) login
 *
 * @author ashishtiwari
 */
public class LoginServlet extends HttpServlet {
    private static final String LOGIN_PAGE = "/WEB-INF/views/userLogin.jsp";
    private static Logger logger = Logger.getLogger(LoginServlet.class);

    /**
     * Get request on LoginServlet will show the login page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(LOGIN_PAGE);
        dispatcher.forward(request, response);
    }

    /**
     * Post request on LoginServlet will process the user credentials to see if user is valid
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter(Globals.USER_ID_PARAM);
        String password = request.getParameter(Globals.PASSWORD_PARAM);

        if(userId == null || password == null) {
            request.setAttribute("errorMessage", "Invalid input");
            doGet(request, response);
            return;
        }

        if(userId.isEmpty() || password.isEmpty()) {
            request.setAttribute("errorMessage", "Invalid input");
            doGet(request, response);
            return;
        }
        User user = null;
        DatabaseConnection connection = null;

        try {
            connection = ConnectionPool.getConnection();
            user = SQLUtils.getUser(userId, password, connection);
        } catch (Exception ex) {
            logger.error("Error in checking user in database", ex);
        } finally {
            if(connection != null) {
                connection.returnToPool();
            }
        }

        if(user == null) {
            logger.info("Invalid login attempt for user: '" + userId + "'");
            request.setAttribute("errorMessage", "Invalid login. Please try again!");
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(LOGIN_PAGE);
            dispatcher.forward(request, response);
        } else {
            logger.info("User '" + user.getEmailId() + "' successfully logged in.");

            HttpSession session = request.getSession();
            session.setAttribute(Globals.USER_DETAILS_KEY, user);

            response.sendRedirect(request.getContextPath());
        }
    }
}
