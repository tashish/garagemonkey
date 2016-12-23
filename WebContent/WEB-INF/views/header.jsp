<%
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
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="edu.asu.garagemonkey.model.*" %>
<%@ page import="edu.asu.garagemonkey.common.Globals" %>

<!DOCTYPE html>

<html>
    <head>
        <title> ${pageTitle} </title>
        <link rel="stylesheet" href="css/style.css"/>
        <script type="text/javascript" src="js/jquery-3.1.1.min.js"> </script>
        <script type="text/javascript" src="js/script.js"> </script>
    </head>

    <body>
        <div id="header-container">
            <div id="header-inner-wrapper">
                <div id="header-title">
                    <span> GarageMonkey </span>
                </div>

                <div id="main-nav">
                    <ul>
                        <li><a class="active" href="/GarageMonkey/">Home</a></li>
                        <%
                            User loggedinUser = (User) session.getAttribute(Globals.USER_DETAILS_KEY);
                            if(loggedinUser == null) {
                        %>
                            <li><a href="/GarageMonkey/login">Login</a></li>
                            <li><a href="/GarageMonkey/register">Register</a></li>
                            <li><a href="/GarageMonkey/search">Reserve spot</a></li>
                        <%
                            } else {
                                UserType type = loggedinUser.getUserType();
                                if(type.equals(UserType.ADMINISTRATOR)) {
                        %>
                                    <li><a href="/GarageMonkey/manage-garage">Manage Garage</a></li>
                                    <li><a href="/GarageMonkey/add-operator">Add Operator</a></li>
                        <%
                                } else {
                        %>
                                    <li><a href="/GarageMonkey/search">Reserve spot</a></li>
                        <%      }  %>
                            <li><a href="/GarageMonkey/profile">Profile</a></li>
                            <li><a href="/GarageMonkey/logout">Logout</a></li>
                        <%
                            }
                        %>
                        <li><a href="/GarageMonkey/about">About</a></li>
                        <li><a href="/GarageMonkey/contact">Contact</a></li>
                    </ul>
                </div>
            </div>
        </div>

        <div class="clr"></div>

        <div id="content-container">
