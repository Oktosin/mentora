## Project Status

🚧 Active Development

Mentora is currently under active development and continuously evolving.

---

Overview

Mentora is a multi-tenant Learning Management System (LMS) built with Spring Boot and PostgreSQL. The platform enables organizations to create, manage, and deliver learning experiences while providing assessment capabilities, learner analytics, activity auditing, and intelligent recommendation features.

The application follows a Modular Monolith architecture, allowing clear separation of business domains while maintaining the simplicity of a single deployable application.

---

### Current Focus Areas

* User Authentication & Authorization
* Role-Based Access Control (RBAC)
* Course & Content Management
* Assessment Engine
* Learning Analytics
* Recommendation System
* Activity Auditing
* Anonymous Community Discussions

---

## Core Database Tables

| Domain              | Tables                                   |
| ------------------- | ---------------------------------------- |
| Identity & Access   | organizations, users, roles, permissions |
| Learning Management | courses, modules, lessons, enrollments   |
| Assessment          | assessments, questions, submissions      |
| Community           | anonymous_posts                          |
| Intelligence        | recommendations                          |
| Auditing            | activity_logs                            |

---

## API Endpoints

**Base URL**

```http
/api
```

---

### Authentication

```http
POST|GET /api/auth/login
POST|GET /api/auth/logout
POST|GET /api/auth/refresh-token

POST|GET /api/auth/mfa/request-otp
POST|GET /api/auth/mfa/verify
```

---

### Users

```http
POST|GET /api/user/register

POST|GET /api/user/find-users
```

---

### Organizations

```http
POST|GET /api/orgs/create

POST|GET /api/orgs/find-all
```

---

### Learning Management

#### Courses

```http
POST|GET /api/learning/courses

POST|GET /api/learning/find-courses
```

#### Modules

```http
POST|GET /api/learning/courses/{courseId}/modules

POST|GET /api/learning/courses/{courseId}/find-modules
```

#### Lessons

```http
POST|GET /api/learning/modules/{moduleId}/lessons

POST|GET /api/learning/modules/{moduleId}/find-lessons
```

#### Enrollments

```http
POST|GET /api/learning/courses/{courseId}/enroll/{userId}
```

#### Progress Tracking

```http
POST|GET /api/learning/lessons/{lessonId}/progress/{userId}
```

---

#### Assessments

```http
POST|GET /api/assessments/create

POST|GET /api/assessments/find-all
```

#### Questions

```http
POST|GET /api/assessments/{assessmentId}/questions

POST|GET /api/assessments/{assessmentId}/find-questions
```

#### Answers

```http
POST|GET /api/assessments/questions/{questionId}/answers
```

#### Submissions

```http
POST|GET /api/assessments/{assessmentId}/submissions/{userId}
```

---

### Recommendations & Community Feedback

```http
POST|GET /api/feedback

POST|GET /api/find-feedback

POST|GET /api/recommendations
```

---

### Analytics

```http
POST|GET /api/analytics/view
```

---

### Audit Logs

```http
POST|GET /api/audit/activity
```

---

### Content Access

```http
POST|GET /api/content/{assetKey}/access
```


---

## Community & Collaboration

Mentora includes an anonymous discussion platform designed to encourage participation and knowledge sharing among learners.

### Features

* Anonymous posting
* Community engagement
* Peer-to-peer knowledge sharing
* Safe learning discussions
* Learner interaction and collaboration

---

## Security

Mentora implements enterprise-grade security using Spring Security.

### Security Features

* Authentication
* Authorization
* Role-Based Access Control (RBAC)
* Permission-Based Resource Access
* Method-Level Security
* Request Validation
* Secure Password Storage
* Multi-Tenant Access Controls

---

## Future Roadmap

### Phase 1

* JWT Authentication
* Refresh Token Support
* API Documentation with Swagger

### Phase 2

* Email Notifications
* Course Certificates
* User Profiles
* Progress Tracking Dashboard

### Phase 3

* AI-Powered Learning Recommendations
* Learning Path Generation
* Real-Time Analytics

### Phase 4

* Event-Driven Architecture
* Redis Caching
* Search Functionality
* External Integrations

---

## Why Modular Monolith?

Mentora adopts a Modular Monolith architecture to balance simplicity and scalability.

### Benefits

* Single deployment unit
* Clear domain boundaries
* Faster development cycle
* Easier debugging
* Lower operational complexity
* Future microservice extraction capability

Each module owns its business logic, domain entities, services, and APIs while remaining part of a unified application.


---


Author

Tosin Okuwobi

Backend Engineer | Java & Spring Boot Developer

Focused on building scalable backend systems, fintech platforms, and enterprise software solutions.
