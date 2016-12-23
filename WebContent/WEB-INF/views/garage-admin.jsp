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

<%@ page import="edu.asu.garagemonkey.common.Globals" %>

<jsp:include page="header.jsp"></jsp:include>

<% if(request.getAttribute(Globals.ADDED_GARAGE_KEY) != null) { %>

    <div id="garage_add_result" style="color: green; font-weight: bold; margin-bottom: 20px;">
            ${addResult}
    </div>

<% } %>

<div class="main">
     <form class="form" method="post" action="/GarageMonkey/manage-garage" onsubmit="return validateGarageAdd()">
         <h2>Add Parking Spot: </h2>

         <label>Garage Level:</label>
         <input type="text" name="level" id="level" />

         <label>Row number:</label>
         <input type="text" name="row" id="row" />

         <label>Parking spot number:</label>
         <input type="text" name="spot" id="spot" />

         <label>Parking spot size :</label>
         <select name="spotSize" >
             <option value="1"> Small (motorbike) </option>
             <option value="2"> Compact </option>
             <option value="3"> Regular </option>
             <option value="4"> Large (Bus, Truck) </option>
         </select>

         <label>Hourly Rate:</label>
         <input type="text" name="rate" id="rate" />

         <input type="submit" name="add" id="register" value="Add Spot" />
         <input type="hidden" name="action" value="doInsert" />
     </form>

     <div id="search_errors" style="color: red; font-weight: bold;">
        ${errorMessage}
     </div>
 </div>


<jsp:include page="footer.jsp"></jsp:include>