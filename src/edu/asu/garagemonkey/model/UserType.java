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
package edu.asu.garagemonkey.model;

public enum  UserType {
    CUSTOMER(1),
    OPERATOR(2),
    ADMINISTRATOR(3);

    private int type;
    UserType(int type) {
        this.type = type;
    }

    public int getIntValue() {
        return type;
    }

    public static  UserType fromIntValue(int a) {
        if(a == 1)
            return CUSTOMER;
        else if (a == 2)
            return OPERATOR;
        else if(a == 3)
            return ADMINISTRATOR;
        else
            throw new RuntimeException("Invalid user type");
    }
}
