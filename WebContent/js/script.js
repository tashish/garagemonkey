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
function validateRegistration() {
    var userid = $("#userid").val();
    var password = $("#password").val();
    var cpassword = $("#cpassword").val();
    var fname = $("#fname").val();
    var lname = $("#lname").val();

    if(userid == '' || password == '' || cpassword == '' || fname == '' || lname == '') {
        alert("Please provide all the details")
        return false;
    }

    if(password != cpassword) {
        alert("Password and confirmed password do not match");
        return false;
    }

    return true;
}

function validateLogin() {
    var userid = $("#userid").val();
    var password = $("#password").val();

    if(userid == '' || password == '') {
        alert("Please provide all the details");
        return false;
    }

    return true;
}

function validateSearch() {
    var license = $("#license").val();
    var startDate = $("#startdate").val();
    var startTime = $("#starttime").val();
    var endDate = $("#enddate").val();
    var endTime = $("#endtime").val();

    if(license == '' || startDate == '' || startTime == '' || endDate == '' || endTime == '') {
        alert("Please provide all the details");
        return false;
    }
    return true;
}

function validateGarageAdd() {
    var level = $("#level").val(); 
    var row = $("#row").val(); 
    var spot = $("#spot").val(); 
    var rate = $("#rate").val();  

    if(level == '' || row == '' || spot == '' || rate == '') { 
        alert("Please provide all the details"); 
        return false; 
    } 

    return true; 
}