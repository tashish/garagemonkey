Steps to install and use GarageMonkey

1. Configure database
   database.sql in the root directory contains SQL script to create garagemonkey database, create a new database user and
   create all the database tables.

   To install the database, use the following command
   a) Go to the directory where database.sql is stored
   b) Make sure mysql server is running. On Unix environment, you can start it using "mysql.server restart"
   c) Install the database using:
      mysql -u root -p < database.sql

2. Download Apache tomcat and extract it

3. Edit tomcat-users.xml under <tomcat-directory>/conf. Add user and password specified in build.properties (in the project resources folder)
   Also add "manager-script" role. Here is an example:
   <role rolename="manager-script"/>
   <user username="garageadmin" password="admin123" roles="manager-script"/>

4. Start tomcat
   a) Go to bin directory in the tomcat folder
   b) run sh catalina.sh start
   c) Make sure tomcat has been successfully started by navigating to http://localhost:8080/

5. Install GarageMonkey web application using the ant script (make sure you have Apache ant installed in the system)
   a) Update build.properties path in edu.asu.garagemonkey.common.Globals to point to your tomcat directory
   b) Update tomcat path in build.xml and in log4j.properties under resources folder
   b) Go to project directory where build.xml is stored (its in the root directory of project)
   c) Run
    ant execute -lib <tomcat-directory>/lib/
   In the above command make sure to specify the path of lib directory of tomcat as part of ant command

6. The above command should open home page of GarageMonkey in Google Chrome. If this doesn't work, nagivate to
  http://localhost:8080/GarageMonkey/
  Application name in the above URL is case sensitive

7. Add some parking spots using
  http://localhost:8080/GarageMonkey/manage-garage
  This will require admin login, the default admin credentials are as follows:
  login: admin@garagemonkey.com
  password: changeme




