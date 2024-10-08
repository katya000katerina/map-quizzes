# Map Quizzes

## Project Description

Map Quizzes is an interactive web application that offers geographical quizzes focused on physical features of the world. Users can test their knowledge of world mountains and volcanoes through engaging, map-based quizzes.

## Current Status

The project has implemented core functionality including user authentication, interactive map quizzes, and personalized quiz features. Some additional pages and comprehensive testing are still in development.

## Features

### Implemented
- RESTful API using Spring Boot with frontend integration
- Interactive physical map quizzes (mountains of the world and volcanoes of the world)
- User account creation and management (change username, change password, delete account)
- JWT authentication for secure access
- User ranking system based on quiz completion time
- Personalized quizzes for registered users focusing on previously incorrect answers
- PDF ranking certificate download for registered users
- Caching for improved performance
- API testing

## Features Showcase

### Taking a Quiz
![Taking a Quiz](./assets/quiz-demo.gif)

### User Profile
![User Profile](./assets/user-profile-demo.gif)

### Ranking System
![Ranking System](./assets/ranking-demo.gif)

## Technology Stack

- Backend: Java 17, Spring Framework (Core, Boot, Web, Security, Data JPA)
- Frontend: HTML, CSS, JavaScript
- Database: PostgreSQL, Redis
- Build Tool: Maven
- Testing: JUnit 5
- Additional Libraries: Lombok, JJWT, MapStruct, Apache PDFBox