# Recruitment Management System

Backend API for recruitment management with JWT authentication, resume parsing, and job application tracking.

## Tech Stack

- Java 17
- Spring Boot 3.2.0
- Spring Security + JWT
- MySQL 8.0
- Maven

## Features

- User authentication (Admin/Applicant roles)
- Resume upload with auto-parsing (PDF/DOCX)
- Job posting and management
- Application tracking

## Quick Setup

### 1. Prerequisites
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. Database Setup 
```sql
recruitment_db;
```

### 3. Configure `application.properties`
```properties
spring.datasource.username=root
spring.datasource.password=your_password

jwt.secret=YourSecretKeyMustBeAtLeast256BitsLong
resume.parser.api.key=ghhg....
```

### 4. Run
```bash
mvn clean install
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

## API Endpoints

### Auth (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/signup` | Register user |
| POST | `/login` | Login & get JWT |

### Applicant (Auth Required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/uploadResume` | Upload resume |
| GET | `/jobs` | View all jobs |
| GET | `/jobs/apply?job_id={id}` | Apply to job |

### Admin (Auth Required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/admin/job` | Create job |
| GET | `/admin/job/{id}` | Get job + applicants |
| GET | `/admin/applicants` | List all applicants |
| GET | `/admin/applicant/{id}` | Get applicant profile |

## Quick Test

### 1. Signup (Admin)
```bash
POST /signup
{
  "name": "Admin",
  "email": "admin@test.com",
  "password": "admin123",
  "userType": "ADMIN",
  "profileHeadline": "HR Manager",
  "address": "NYC"
}
```

### 2. Login
```bash
POST /login
{
  "email": "admin@test.com",
  "password": "admin123"
}
```
**Save the token from response!**

### 3. Create Job (Admin)
```bash
POST /admin/job
Headers: Authorization: Bearer {token}
{
  "title": "Java Developer",
  "description": "5+ years exp",
  "companyName": "TechCorp"
}
```

### 4. Signup (Applicant)
```bash
POST /signup
{
  "name": "John",
  "email": "john@test.com",
  "password": "john123",
  "userType": "APPLICANT",
  "profileHeadline": "Developer",
  "address": "LA"
}
```

### 5. Upload Resume (Applicant)
```bash
POST /uploadResume
Headers: Authorization: Bearer {applicant_token}
Body: form-data
file: resume.pdf
```

### 6. Apply to Job
```bash
GET /jobs/apply?job_id=1
Headers: Authorization: Bearer {applicant_token}
```

## Project Structure
```
src/main/java/com/recruitment/system/
├── config/          # Security config
├── controller/      # REST endpoints
├── dto/             # Request/Response objects
├── exception/       # Error handlers
├── model/           # Entities (User, Job, Profile)
├── repository/      # JPA repositories
├── security/        # JWT components
└── service/         # Business logic
```

## Database Tables

- **users**: User accounts (Admin/Applicant)
- **profiles**: Resume data (auto-parsed)
- **jobs**: Job postings
- **job_applications**: Application tracking

## Authentication

Add JWT token to all protected endpoints:
```
Authorization: Bearer eyJhbGc....
```

## Error Response Format
```json
{
  "success": false,
  "message": "Error description"
}
```

## Common Issues

**MySQL Connection Failed**
- Check MySQL is running
- Verify credentials in `application.properties`

**Unauthorized Error**
- Ensure token is in `Authorization` header
- Check token hasn't expired (24h validity)

**File Upload**
- Only PDF/DOCX allowed


