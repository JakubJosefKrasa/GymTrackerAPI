# GymTracker

GymTracker is a Spring Boot application designed to help users track their workout sessions. It allows users to manage exercises, create training plans, and log their workout progress effectively.
For the backend was also created frontend application written in react that can be found here [GymTrackerClient](https://github.com/JakubJosefKrasa/GymTrackerClient and Running Frontentend connected to backend [here](http://localhost:5173/) first request can take up to 1 min to start the server

---

## Table of Contents

1. [Features](#features)
2. [Technical Details](#technical-details)
3. [Installation](#installation)
4. [Authors](#authors)

## Features
- **User**:
    - Sign up
    - Sign in
- **Exercises**:
    - Create exercise
    - Change exercise's name
    - Delete exercise
    - Fetch exercises with pagination
    - Fetch exercises that are not in particular training plan
- **Training Plans**:
    - Create training plan
    - Change training plan's name
    - Delete training plan
    - Fetch training plan
    - Fetch training plan with pagination
    - Fetch exercises that are not in particular training plan
    - Add exercise in training plan
    - Remove exercise from training plan
- **Workout Sessions**:
    - Create workout session (choosing training plan and date)
    - Delete workout session
    - Fetch workout sessions
    - Fetch workout session
    - Create set for exercise in training plan for current workout session (set describes repetitions and weight for exercise)
    - Delete set for exercise in training plan for current workout session
    - Update set for exercise in training plan for current workout session
- **API DOCS**: Can be found in [api-docs.yaml](https://github.com/JakubJosefKrasa/GymTrackerAPI/blob/master/src/main/resources/api-docs.yaml) and can be [viewed](https://editor.swagger.io/) by importing api-docs.yaml file

## Technical Details

- **Java**: 17
- **Spring Boot Version**: 3.2.7
- **Dependencies**:
    - Spring Data JPA
    - Spring Security
    - Spring Boot Validation
    - Lombok
    - MapStruct
    - Swagger UI
- **Database**: PostgreSQL
- **Authorization**: JWT stored in access_token cookie

## Installation

This application is dockerized for easy setup and deployment. The properties for the application for Docker usage are configured in the `docker-application.yml`

### Prerequisites
- Java 17
- [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) installed on your system.

### Steps to Run the Application with docker
1. Clone the repository:
   ```bash
   git clone https://github.com/JakubJosefKrasa/GymTrackerAPI.git
2. Start the application:
    ```bash
    docker compose up --build
3. Access the application at http://localhost:8080.

### Authors
- Jakub Josef Krása [Github](https://github.com/JakubJosefKrasa)