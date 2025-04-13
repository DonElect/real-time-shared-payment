# Shared Payment Processing System

## 1. Overview

This application implements a real-time shared payment processing system that allows administrators to manage payments between parents and students with different association types. It supports both unique and shared student payments with atomic transaction guarantees.

---

## 2. System Requirements

- Java 17+
- Maven 3.8+
- Spring Boot 3.1+

---

## 3. Installation & Setup

### 3.1 Clone the Repository

```bash
git clone https://github.com/your-repo/shared-payment-processing.git
cd shared-payment-processing
```

### 3.2 Build the application
```bash
mvn clean install
```

### 3.3 Run the Application
```bash
mvn spring-boot:run
```

## 4. Database Configuration

### 4.1 H2 Database Console Details

- **Console URL**: [http://localhost:2025/h2-console](http://localhost:2025/h2-console)
- **JDBC URL**: `jdbc:h2:mem:payment_db`
- **Username**: `sa`
- **Password**: *(blank)*

## 5. Initial Data Setup

The system automatically initializes with the following test data:

### 5.1 Parents

| ID | Name     | Balance   |
|----|----------|-----------|
| 1  | Parent A | 100000.00 |
| 2  | Parent B | 150000.00 |

### 5.2 Students

| ID | Name      | Balance | Associated Parents           |
|----|-----------|---------|------------------------------|
| 1  | Student 1 | 0.00    | Parent A, Parent B (Shared)  |
| 2  | Student 2 | 0.00    | Parent A only                |
| 3  | Student 3 | 0.00    | Parent B only                |

### 5.3 Admin Credentials

- **Username**: `admin@testemail.com`
- **Password**: `1234`

## 6. API Documentation

### 6.1 Authentication

- **Endpoint**: `POST /api/v1/users/login`

#### Request Body

```json
{
  "username": "admin@testemail.com",
  "password": "1234"
}
```

## 7. Business Rules

### 7.1 Unique Child Payment

- Only affects the associated parent's balance.
- **Example**: Payment for **Student 2** only updates **Parent A**'s balance.

### 7.2 Shared Child Payment

- Updates balances for **both parents**.
- Amount is **divided equally** between associated parents.
- **Example**: Payment for **Student 1** updates both **Parent A** and **Parent B**.

### 7.3 Dynamic Rate Calculation

```text
adjustedAmount = paymentAmount * (1 + dynamicRate)
```
#### Rate Breakdown:

- Base rate: 5%
- Additional +2% for shared students
- -1% discount for parents with a positive balance

## 8. Testing the Application

### 8.1 Authenticate to Get JWT Token

```bash
curl -X POST http://localhost:2025/api/v1/users/login \
-H "Content-Type: application/json" \
-d '{"username":"admin@testemail.com","password":"1234"}'
```

### 8.2 Process Payment (Using Returned JWT Token)
```bash
curl -X POST http://localhost:2025/api/v1/payments \
-H "Content-Type: application/json" \
-H "Authorization: Bearer YOUR_JWT_TOKEN" \
-d '{"parentId":1,"studentId":1,"amount":200.00}'
```

## 9. Error Handling

### 9.1 Standard Error Response Format

```json
{
  "code": "40400",
  "message": "Parent not found with id: 99",
  "errorDetail": {
    "message": "Parent not found with id: 99",
    "status": "NOT_FOUND"
  }
}
```
### Common Error Codes

| Code  | HTTP Status    | Description                  |
|-------|----------------|------------------------------|
| 40000 | BAD_REQUEST    | Malformed JSON request       |
| 40400 | NOT_FOUND      | Parent not found             |
| 40401 | NOT_FOUND      | Student not found            |
| 40600 | NOT_ACCEPTABLE | Payment authorization failed |

## 10. Security

- JWT authentication
- BCrypt password encoding
- Role-based access control
- CSRF protection
- Input validation

---

## 11. Troubleshooting

| Issue                   | Solution                                                                    |
|-------------------------|-----------------------------------------------------------------------------|
| Can't access H2 console | Verify the application is running and the JDBC URL matches exactly          |
| Authentication fails    | Double-check credentials and ensure the JWT token hasn't expired            |
| Payment rejected        | Confirm that parent/student IDs exist and the parent has sufficient balance |
# real-time-shared-payment
