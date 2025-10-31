# App README

## Group elements:
- 66174 Rúben Rocha  
- 106090 Tiago Alves  
- 113239 Pedro Veloso  
- 122123 Rodrigo Delaunay  

### [Demo of the app on youtube](https://www.youtube.com/watch?v=g93oCkNMEKA)
[![App demo video thumbnail](https://img.youtube.com/vi/g93oCkNMEKA/0.jpg)](https://www.youtube.com/watch?v=g93oCkNMEKA)

## Project Structure

The sources of your App have the following structure:

```
src
├── main/frontend
│   ├── index.html
│   └── themes
│       └── default
│           ├── styles.css
│           └── theme.json
├── main/java
│   └── com
│       └── example
│           ├── Application.java
│           ├── base
│           │   └── ui
│           │       ├── component
│           │       │   └── ViewToolbar.java
│           │       ├── MainErrorHandler.java
│           │       └── MainLayout.java
│           ├── charts113239
│           │   ├── Graphs.java
│           │   ├── GraphsRepository.java
│           │   ├── GraphsService.java
│           │   ├── package-info.java
│           │   └── ui
│           │       └── GraphsView.java
│           ├── examplefeature
│           │   ├── package-info.java
│           │   ├── Task.java
│           │   ├── TaskRepository.java
│           │   ├── TaskService.java
│           │   └── ui
│           │       └── TaskListView.java
│           ├── IFindAll.java
│           ├── jmoney
│           │   ├── JMoney.java
│           │   ├── JMoneyRepository.java
│           │   ├── JMoneyService.java
│           │   ├── package-info.java
│           │   └── ui
│           │       └── JMoneyListView.java
│           ├── pdfexporter
│           │   ├── package-info.java
│           │   ├── PdfExporter.java
│           │   └── ui
│           │       └── PdfExporterView.java
│           └── user
│               ├── package-info.java
│               ├── ui
│               │   └── UserView.java
│               ├── User.java
│               ├── UserRepository.java
│               └── UserService.java
├── main/resources
│   ├── application-ci.properties
│   └── application.properties
└── test
    └── java
        └── com
            └── example
                └── examplefeature
                    └── TaskServiceTest.java
```

The main entry point into the application is `Application.java`. This class contains the `main()` method that start up 
the Spring Boot application.

The skeleton follows a *feature-based package structure*, organizing code by *functional units* rather than traditional 
architectural layers. It includes two feature packages: `base` and `examplefeature`.

* The `base` package contains classes meant for reuse across different features, either through composition or 
  inheritance. You can use them as-is, tweak them to your needs, or remove them.
* The `examplefeature` package is an example feature package that demonstrates the structure. It represents a 
  *self-contained unit of functionality*, including UI components, business logic, data access, and an integration test.
  Once you create your own features, *you'll remove this package*.

The `src/main/frontend` directory contains an empty theme called `default`, based on the Lumo theme. It is activated in
the `Application` class, using the `@Theme` annotation.

## Starting in Development Mode

To start the application in development mode, import it into your IDE and run the `Application` class. 
You can also start the application from the command line by running: 

```bash
./mvnw
```

## Building for Production

To build the application in production mode, run:

```bash
./mvnw -Pproduction package
```

To build a Docker image, run:

```bash
docker build -t my-application:latest .
```

If you use commercial components, pass the license key as a build secret:

```bash
docker build --secret id=proKey,src=$HOME/.vaadin/proKey .
```

## Getting Started

The [Getting Started](https://vaadin.com/docs/latest/getting-started) guide will quickly familiarize you with your new
App implementation. You'll learn how to set up your development environment, understand the project 
structure, and find resources to help you add muscles to your skeleton — transforming it into a fully-featured 
application.

## Pipeline

This pipeline is meant to enable the automation of the .jar generation everytime the "main" branch is updated.

When we push to main the following actions are triggered:

- Grabs the codebase from the repository
- Sets up a java environment with maven build system
- Downloads the required dependencies
- Executes the build with maven in test mode, using H2 Database:  
`run: mvn -B clean package -Dspring.profiles.active=ci`
- After passing the tests, builds and packages jar file with MariaDB, skipping tests  
`run: mvn -B package -Pproduction -DskipTests`
- Publishes the .jar file as an artefact.

```yaml
steps:
      - name: Check out repository
        uses: actions/checkout@v4

      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
          cache: maven

      - name: Build with Maven
        run: mvn -B clean package

      - name: Upload JAR artifact
        uses: actions/upload-artifact@v4
        with:
          name: app-jar
          path: |
            **/target/*.jar
          if-no-files-found: error
          retention-days: 14
```

## Initialize the MariaDB database

- Download and install [MariaDB Server](https://mariadb.org/download/)
  - Uncheck the "change root password" feature
- Create a new database named "semana3db"
  - `CREATE DATABASE semana3db;`

### Useful MariaDB queries/commands:  

#### - Base commands:
```mysql 
SELECT User, Host FROM mysql.user;  # Check existing users on mariadb
SELECT USER();      # Check who you authenticated as
SHOW DATABASES      # Check existing databases
USE <database>;     # Select which database you want to perform queries on
SHOW TABLES;        # Show existing tables on the current database you selected
DESCRIBE <table>;   # Show the data structure of a specified table 
```

#### - Clear and reset an existing table:
```mysql
TRUNCATE TABLE <table_name>;
ALTER TABLE <table_name> AUTO_INCREMENT = 1;
```

#### - Change an user password:
```mysql
ALTER USER 'root'@'localhost' IDENTIFIED VIA mysql_native_password USING PASSWORD('new_password');
FLUSH PRIVILEGES;
```
