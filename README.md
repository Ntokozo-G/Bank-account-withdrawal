# Bank-account-withdrawal

This is a SpringBoot REST API application that processes bank account witdrawals using SNS, but for this technical assessment I used an in memory H2 database this runs locally.
I added this purely for testing since i wanted to see how the app would work. I've added the config for AWS SNS as the initial code used it for publishing events.

**Tech Stack**

* Java 21 + SpringBoot
* H2 in memeory database(for local testing)
* Spring JDBC(JdbcTemplate) for the database access
* AWS SNS SDK (This is optional since i do not have a personal account with AWS credentials to test this)
* Maven for build and dependency management

**How to Run**
Required

* Java 21+
* Maven 3.8+

Run Locally (default profile since I do not have personal AWS profile)

bash (or you can run the class on intelij)

`mvn spring-boot:run`

This will start the app on http://localhost:8080 using an in-memory H2 database(the database is prepopulated with some dummy data). No external services or AWS credentials needed.

**To simulate someone sending a request, you can run:**
1. **A successful withdrawal scenario**

curl -X POST http://localhost:8080/bank/withdraw  -H "Content-Type: application/json" -d '{"accountId": 1, "amount": "100.00"}'
![img.png](img.png)

2. **Insufficient funds scenario**

curl -X POST http://localhost:8080/bank/withdraw -H "Content-Type: application/json" -d '{"accountId": 3, "amount": "50.00"}'

3. **Account not found scenario**

curl -X POST http://localhost:8080/bank/withdraw -H "Content-Type: application/json" -d '{"accountId": 999, "amount": "10.00"}'

4. **Bad request scenario**

curl -X POST http://localhost:8080/bank/withdraw -H "Content-Type: application/json" -d '{"accountId": 1, "amount": "-50.00"}'

We can also view the database from the browser, open http://localhost:8080/h2-console in the browser.Use JDBC URL jdbc:h2:mem:bankdb, username sanlamFinTech, no password.
