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
<%@ page import="edu.asu.garagemonkey.model.*" %>
<%@ page import="edu.asu.garagemonkey.common.Globals" %>

<jsp:include page="header.jsp"></jsp:include>

<div>
<%
    User loggedinUser = (User) session.getAttribute(Globals.USER_DETAILS_KEY);

    if(loggedinUser != null) {
%>
    Welcome <%= loggedinUser.getFirstName()%>, <%= loggedinUser.getLastName()%>
<%
    } else {
%>
    Welcome to GarageMonkey!
<% } %>


</div>

<div style="width:600px; margin-top:40px">
    GarageMonkey is a web application, which aims to solve the inefficacy of parking garage operations and also provide
    new features, which will enable better utilization and management to parking spots to provide a seamless customer
    experience.
</div>

<div style="width:600px; margin-top:20px">
    GarageMonkey automates end-to-end workflow of parking a vehicle in a garage starting with setting up
    a new garage, allocate unique user friendly identifiers to individual spots, handling walk-in customers as well
    as allow online registration. Customers will also be able to extend an existing reservation online if there are
    spots available, without going through any manual step.
</div>

<jsp:include page="footer.jsp"></jsp:include>