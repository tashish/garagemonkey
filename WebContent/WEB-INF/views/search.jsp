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
<%@ page import="java.util.ArrayList" %>

<jsp:include page="header.jsp"></jsp:include>

<% if(request.getAttribute(Globals.FIND_SPOTS_KEY) == null) { %>

<div class="main">
     <form class="form" method="post" action="/GarageMonkey/search" onsubmit="return validateSearch()">
         <h2>Search Parking Spot: </h2>

         <% User loggedinUser = (User) session.getAttribute(Globals.USER_DETAILS_KEY);
            if(loggedinUser != null && loggedinUser.getUserType().equals(UserType.CUSTOMER)) { %>
            <input type="hidden" name="userid" id="userid" value="<%= loggedinUser.getEmailId()%>" />
         <% } else { %>
         <label>Customer Email:</label>
         <input type="text" name="userid" id="userid" />
         <% } %>

         <label>Vehicle type :</label>
         <select name="vehicleType" >
             <option value="1"> Small (motorbike) </option>
             <option value="2"> Compact </option>
             <option value="3"> Regular </option>
             <option value="4"> Large (Bus, Truck) </option>
         </select>

         <label>License Plate:</label>
         <input type="text" name="license" id="license" />

         <label>Start Date (MM/DD/YYYY):</label>
         <input type="text" name="startdate" id="startdate" />
         <label>Start Time (00:00):</label>
         <input type="text" name="starttime" id="starttime" />

         <label>End Date (MM/DD/YYYY):</label>
         <input type="text" name="enddate" id="enddate" />
         <label>End Time (00:00):</label>
         <input type="text" name="endtime" id="endtime" />

         <input type="hidden" name="action" value="doSearch" />
         <input type="submit" name="search" id="register" value="Reserve" />
     </form>

     <div id="search_errors" style="color: red; font-weight: bold;">
        ${errorMessage}
     </div>
 </div>

<% } else {
    ParkingSpot spot = (ParkingSpot) request.getAttribute(Globals.SPOT_RESERVED);
    if(spot == null) {
%>
    <div> Sorry! We could not reserve a parking spot matching your criteria, Please <a href="/GarageMonkey/search"> Search </a> again! </div>
 <% } else { %>

    <div id="reservation-success">
        <p> Congratulations, you have successfully reserved a parking spot. </p>
         <p> Here are the parking details: </p>

        <p id="reservation-details">
            Reservation Id: <%= spot.getReservationId() %> <br/>
            Spot Id: <%= spot.getSpotId() %> <br/>
            Level: <%= spot.getLevel() %>
            Row: <%= spot.getRow() %>
        </p>

        <p>
            <button id="print-receipt"> Print Receipt </button>
        </p>
    </div>
<% } } %>

<jsp:include page="footer.jsp"></jsp:include>