# Gym Membership Management

## Run with MySQL persistence

The application saves members, trainers, and fitness schedules in MySQL. Records
are loaded into the existing collections when the application starts; closing or
restarting the application does not clear the database. Processing a schedule
removes it from the current in-memory queue, but does not delete its database row.
The Delete Member and Delete Trainer buttons permanently remove the selected
record and its linked schedules after confirmation.

Requirements:

- Java 8 or newer
- MySQL Server running on `localhost:3306`
- MySQL Workbench (to create the database once)
- MySQL Connector/J JAR

1. In MySQL Workbench, connect to your local server and run this once:

```sql
CREATE DATABASE gym_management;
```

2. Download the MySQL Connector/J ZIP from
   [the official MySQL Connector/J downloads page](https://dev.mysql.com/downloads/connector/j/).
3. Extract the `mysql-connector-j-*.jar` file into a `lib` folder in this project.
4. Open PowerShell in the project folder and run:

```powershell
powershell -ExecutionPolicy Bypass -File .\run.ps1
```

If your downloaded JAR has a different version, use its exact filename for
`-MySqlConnectorJar`. The JDBC URL, username, and password are set directly in
`database/DatabaseConnection.java`; update those values if your MySQL settings
change. The script compiles the Java source into `build\classes` and launches
the app with the connector on its classpath; it does not use Maven.

On startup, the application creates its tables if they do not already exist.
The MySQL user must have permission to create tables.

To add a schedule, first add a member and a trainer. Then provide their IDs,
the activity, a date in `DD-MM-YYYY` format, and a time in 24-hour `HH:MM`
format when prompted.
