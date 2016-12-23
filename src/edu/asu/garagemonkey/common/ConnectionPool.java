/*
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {
    private static final int POOL_SIZE = 1;
    private static final String JDBC_DRIVER_CLASSNAME = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION_URL = "jdbc:mysql://" + Globals.get().getDatabaseHost()
            + ":" + Globals.get().getDatabasePort() + "/" + Globals.get().getDatabaseName();
    private static final String DB_USER = Globals.get().getDatabaseUser();
    private static final String DB_PASSWORD = Globals.get().getDatabasePassword();
    private static final int DB_CONNECTION_CHECKOUT_TIMEOUT = Globals.get().getConnectionCheckoutTimeoutSec();

    private static volatile LinkedBlockingQueue<DatabaseConnection> pool = null;
    private static Logger logger = Logger.getLogger(ConnectionPool.class);

    private ConnectionPool() {
    }

    public static DatabaseConnection getConnection() throws Exception {
        if (pool == null) {
            try {
                initializeConnectionPool();
            } catch (Exception e) {
                logger.error("Unable to initialize connection pool", e);
                throw e;
            }
        }

        return pool.poll(DB_CONNECTION_CHECKOUT_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void returnToPool(DatabaseConnection connection) {
        pool.add(connection);
    }

    private static synchronized void initializeConnectionPool() throws Exception {
        if(pool == null) {
            logger.info("Initializing connection pool");
            pool = new LinkedBlockingQueue<>(POOL_SIZE);
            Class.forName(JDBC_DRIVER_CLASSNAME);

            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = null;
                connection = DriverManager.getConnection(DB_CONNECTION_URL, DB_USER, DB_PASSWORD);

                if (connection != null) {
                    pool.add(new DatabaseConnection(connection));
                } else {
                    logger.error("Unable to create database connection.");
                }
            }
        }
    }
}
