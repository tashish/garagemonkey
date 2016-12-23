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
package edu.asu.garagemonkey.common;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Globals {
    private static Globals instance = null;
    private static final String PROPERTY_FILE = "/Users/ramayan.tiwari/dev/tools/tomcat/webapps/GarageMonkey/WEB-INF/classes/default.properties";

    private static Properties prop = new Properties();
    private static Logger logger = Logger.getLogger(Globals.class);

    // Constants
    public static final String USER_DETAILS_KEY = "user_details_key";
    public static final String SPOT_RESERVED = "reserved_spot";
    public static final String FIND_SPOTS_KEY = "find_spots_key";
    public static final String ADDED_GARAGE_KEY = "add_garage_key";

    public static final String USER_ID_PARAM = "userid";
    public static final String PASSWORD_PARAM = "password";
    public static final String FIRST_NAME_PARAM = "fname";
    public static final String LAST_NAME_PARAM = "lname";
    public static final String PHONE_PARAM = "phone";
    public static final String LICENSE_PLATE_PARAM = "license";

    private Globals() {

    }

    public static Globals get() {
        if(instance == null) {
            initializeGlobals();
        }
        return instance;
    }

    private static synchronized void initializeGlobals() {
        if(instance == null) {
            instance = new Globals();
            try {
                InputStream input = new FileInputStream(PROPERTY_FILE);
                prop.load(input);
            } catch (Exception e) {
                logger.error("Unable to read properties", e);
            }
        }
    }

    public String getDatabaseHost() {
        return prop.getProperty("dbhost");
    }

    public String getDatabasePort() {
        return prop.getProperty("dbport");
    }

    public String getDatabaseName() {
        return prop.getProperty("dbname");
    }

    public String getDatabaseUser() {
        return prop.getProperty("dbuser");
    }

    public String getDatabasePassword() {
        return prop.getProperty("dbpass");
    }

    public int getConnectionCheckoutTimeoutSec() {
        return Integer.parseInt(prop.getProperty("connection_checkout_timeout_sec"));
    }
}
