# User Management RestFul API

Test task for Clear Solutions

This project is a simple User Management Restul API developed using Spring Boot. It provides functionalities for creating, updating, and searching for users.

Features:

* Create User: Allows registering new users with validation for age.
* Update User: Supports updating single or multiple user fields.
* Delete User: Enables user deletion.
* Search Users: Facilitates searching for users within a specified birth date range.
* Validation: Includes email pattern validation, not blank and birth date validations.
*Error Handling: Implements error handling for RESTful responses.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
### Prerequisites - Required software
* Java 17
* PostgreSQL
* Spring Boot
* Spring Data JPA
* Spring Web
* Spring Boot Test
* Swagger

### Installing

Clone this repository to your local machine using:

```shell
git clone https://github.com/vasilpetrus/ClearSolutionsTestApplication
```
Create a database and set values in environment variables:
* DATABASE_URL
* DATABASE_USERNAME
* DATABASE_PASSWORD

Run application and open [Swagger](http://localhost:8080/swagger-ui/index.html#/)
