# Recurring Money

### Setup Development Environment

* Install JDK 1.8
* Install Maven 3.x
* Install latest eclipse (or other IDE)
* Install MySQL 5.x
* Install Memcached with default port

### Prepare UI

* Install Node.js 0.12.x with npm.

```
$ cd api
$ cd submodules/uikit
$ git pull
$ npm install -g gulp
$ npm install
```

### Build

```
$ cd project
$ mvn clean package
$ cd ../api
$ mvn package
```

### Init database

* Run App.java in api project
* Run target/init.sql and target/ddl.sql

### Run

```
$ cd api/target
$ java -jar jetty-runner.jar api-0.1-SNAPSHOT-jar-with-dependencies.war
```
