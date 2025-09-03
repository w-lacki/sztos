# SZTOS ![MIT License](https://img.shields.io/badge/license-MIT-green)

System for testing and evaluating student code assignments.

https://github.com/user-attachments/assets/eda95904-331d-49a5-8db3-25039b809783
## Table of Contents

- [Description](#description)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Usage](#usage)
- [License](#license)

## Description

The project was developed to replicate the functionality of the existing system at Gdańsk University of Technology, STOS (https://stos.eti.pg.gda.pl/), and to better understand its inner workings.
The system enables the creation of contests, the assignment of students to these contests, the development of problems for students to solve, and the automated evaluation of their code submissions through various testing mechanisms.

## Features

- **Registration and Login** with email address verification.
- **Contest Creation** and management of students assigned to each contest.
- **Problem Creation** and management within each contest.
- **Automatic Compilation and Execution** of code in Docker containers.
- **Role-based System** for user access and permissions.

## Tech Stack

- **Frontend** (JavaScript):
  - **React**: Used for building the frontend of the application
  - **Axios**: Used to make http requests to the backend.

- **Backend** (Kotlin): 
  - **Spring Boot**: Backend built on top of the Spring Boot framework.
  - **Spring OAuth2**: Used for implementing the JWT based authentication.
  - **Spring Redis**: Used for storing OTP with eviction policy.
  - **PostgreSQL**: SQL database for storing user and contest data.
  - **Docker**: Containerization tool used for deploying and managing application services.

## Requirements:
- **NPM** 11.2.0+

## Installation:
- Clone the repository
- Navigate to backend and run it with gradle `./gradlew bootRun`, the backend will be running on port `localhost:8080`.
- Navigate to frontend and run it with npm `npm run`, the frontend will be running on `localhost:3000`

## Usage:
- Default `admin` user password is `secret`
- Default `test` user password is `secret`
- There is no option to create new users yet.
- Navigate using navigation bar at the top.

## License:
- MIT
