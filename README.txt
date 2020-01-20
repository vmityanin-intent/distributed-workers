This is a distributed workers task
The application grabs URIs from table performs HTTP requests and updates HTTP response code accordingly.
Current implementation for demonstration purposes fills in in-memory DB with dummy data on startup (though full-fledged DB
can be easily configured in application.properties)

Two main configuration properties are:
rows-per-fetch = 1000
threads-quantity = 5

rows-per-fetch - number of table rows each worker thread will fetch and process
threads-quantity - number of worker threads that process rows in DB simultaneously

Prerequisites:
Java 11, Apache Maven 3.x+
This is a maven project, to build it run: mvn clean install
it builds distributed-workers-1.0-SNAPSHOT.jar


Built artifact that can be run from console as simple as
java -jar distributed-workers-1.0-SNAPSHOT.jar
or as this is SpringBoot implementation you can run this project
from your favourite IDE via com.mityanin.workers.ApplicationRunner class

Launching this application start application server on 8080 port (can be changed in application.properties)

Hitting http://localhost:8080 takes you to the launcher page that allows to start the process and observe its status
For debugging purposes you may request records by pages.
Example: http://localhost:8080/api/v1/content?page=500&size=20
will return you 500th page of 20 records

NOTE: this is rather conceptual/prototype implementation with the following simplification/TODOs:
1)Embedded DB usage instead of ful-fledged one
2)DB scripts for tables + schema migration tools
3)Rather naive AAA implementation (utilizing request parameter for user identification)
4)Test coverage
5)Basic exception handling/translation

This is rather to give you shallow impression of my coding style.

