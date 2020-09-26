# FA20 CS242 Assignment 1

Assignment 1 of CS 242 Class Fall 2020. The assignment implements game of Uno. The current version is implemented in Java 14.

## Tool
* [Maven](https://maven.apache.org/download.cgi)  >=3.2

## Play

### Build

The build process requires [Maven](#Tool) because it involves spring boot to run the server. Maven will take care of the dependencies. 

```sh
#Maven packages main class and takes care of the libraries which are specified in pom.xml.
mvn package
```

### Run

By Default, Uno game allows two players. When two instances of client are running, the game automatically starts. 

* `mvn exec:java -Dexec.mainClass="MVC.Controllers.WebApp"`
  * The command runs a single instance of server locally served at [:8080].
* `mvn exec:java -Dexec.mainClass="MVC.Views.Client"`
  * The command runs a single instance of client locally.
  * If multiple instances should run, simple use another terminal. 

## Test 

* `mvn test"`
  * The command runs all tests inside test resource directory.

### Resources:
- [Repo](https://gitlab.engr.illinois.edu/minerl2/fa20-cs242-assignment1)
