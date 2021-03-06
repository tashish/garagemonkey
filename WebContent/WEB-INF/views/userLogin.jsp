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

<jsp:include page="header.jsp"></jsp:include>

     <div class="main">
         <form class="form" method="post" action="/GarageMonkey/login" onsubmit="return validateLogin()">
             <h2>Login to GarageMonkey</h2>

             <label>Email :</label>
             <input type="text" name="userid" id="userid">

             <label>Password :</label>
             <input type="password" name="password" id="password">

             <input type="submit" name="login" id="register" value="Login">
         </form>

         <div id="login_errors" style="color: red; font-weight: bold;">
            ${errorMessage}
         </div>
     </div>

<jsp:include page="footer.jsp"></jsp:include>