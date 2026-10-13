# Gym Membership Management

## Run with MySQL persistence

The application saves members, trainers, and fitness schedules in MySQL. Records
are loaded into the existing collections when the application starts; closing or
restarting the application does not clear the database. Processing a schedule
removes it from the current in-memory queue, but does not delete its database row.

Requirements:

- Java 8 or newer
- MySQL Server running on `localhost:3306`
- MySQL Connector/J JAR

1. Download the MySQL Connector/J ZIP from
   [the official MySQL Connector/J downloads page](https://dev.mysql.com/downloads/connector/j/).
2. Extract the `mysql-connector-j-*.jar` file into a `lib` folder in this project.
3. Open PowerShell in the project folder and run:

```powershell
powershell -ExecutionPolicy Bypass -File .\run.ps1
```

If your downloaded JAR has a different version, use its exact filename for
`-MySqlConnectorJar`. If you use a different MySQL username, pass it with
`-DatabaseUser your_username`. The script securely prompts for the password in
the local PowerShell window, compiles the Java source into `build\classes`, and
launches the app with the connector on its classpath; it does not use Maven.

On first startup, the application creates the `gym_management` database and its
tables. The MySQL user must have permission to create databases and tables.

To add a schedule, first add a member and a trainer. Then provide their IDs,
the activity, a date in `YYYY-MM-DD` format, and a time in 24-hour `HH:MM`
format when prompted.
