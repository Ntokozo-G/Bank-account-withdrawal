# Bank-account-withdrawal

This is a SpringBoot REST API application that processes bank account witdrawals using SNS, but for this technical assessment I used an in memory H2 database this runs locally. 
I added this purely for testing since i wanted to see how the app would work. I've added the config for AWS SNS as the initial code used it for publishing events.

**Tech Stack**

-Java 21 + SpringBoot
-H2 in memeory database(for local testing)
-Spring JDBC(JdbcTemplate) for the database access
-AWS SNS SDK (This is optional since i do not have a personl account with AWS credetials to test this)
-Maven for build and dependency management 

**How to Run**
Required

-Java 21+
-Maven 3.8+

Run Locally (default profile)
bashmvn spring-boot:run
