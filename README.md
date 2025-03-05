# Task API Demo

🚀 **Task API Demo** – A fully featured **Task Management API** built with **Spring Boot**, supporting **both REST and GraphQL**.

## 🔹 Features
✅ **REST & GraphQL API** – Flexible data querying options  
✅ **OpenAPI & Swagger UI** – Auto-generated API documentation  
✅ **JWT Authentication** – Secure login with token-based auth  
✅ **Simulated Login** – Test authentication easily in Swagger  
✅ **Validation & Error Handling** – Standardized request validation  
✅ **Mock Data with Faker** – Realistic tasks without a database  
✅ **Pagination & Filtering** – Efficient data retrieval  

## 🏗️ How It Was Built
This project follows an **API-first** approach, where the API specification was defined first, and the code was auto-generated.

### **1️⃣ Define OpenAPI Specification**
- The API was designed using **OpenAPI 3.0**.
- The spec was created in `task-api-spec.yaml`.
- OpenAPI defines endpoints, request/response structures, and validation rules.

### **2️⃣ Auto-Generate Code Using OpenAPI Generator**
The backend was generated using **OpenAPI Generator**:
```sh
docker run --rm -v ${PWD}:/local openapitools/openapi-generator-cli generate \
  -i /local/task-api-spec.yaml \
  -g spring \
  -o /local/generated-task-api \
  --additional-properties=library=spring-boot,useSpringBoot3=true,java17=true,dateLibrary=java8,interfaceOnly=true
```
✅ Generates **Controller interfaces, Models, and DTOs** automatically.  
✅ Reduces manual coding while ensuring **API and implementation stay in sync**.  

### **3️⃣ Implement Business Logic & Services**
- A **service layer** was added for clean separation of concerns.
- **Mock data with Faker** was used to simulate a working API without a database.
- **Spring Security & JWT authentication** was implemented manually.

### **4️⃣ Add GraphQL Support**
- Added GraphQL using `spring-boot-starter-graphql`.
- Created `task.graphqls` schema for flexible queries.
- Integrated with the same **service layer** as REST.

### **5️⃣ Auto-Generated vs. Manual Implementation**
| **Feature**                  | **Auto-Generated?** | **Manual Implementation?** |
|------------------------------|---------------------|----------------------------|
| **Controller interfaces**    | ✅ Yes (OpenAPI Generator) | 🔴 Need to implement methods |
| **GraphQL schema (`.graphqls`)** | ❌ No | ✅ Needs manual definition |
| **DTOs (Models)**            | ✅ Yes (OpenAPI Generator) | 🔴 Need to extend with Lombok |
| **Validation (`@NotBlank`)**  | ✅ Yes (if defined in OpenAPI spec) | ❌ No manual work |
| **Exception Handling**       | ❌ No | ✅ Need `GlobalExceptionHandler.java` |
| **Service Layer (`TaskService`)** | ❌ No | ✅ Need to implement logic |
| **Pagination for REST**      | ✅ Yes (via OpenAPI) | 🔴 Implement in service layer |
| **Pagination for GraphQL**   | ❌ No | ✅ Implement query resolver and service |
| **Faker for Mock Data**      | ❌ No | ✅ Need to add manually |
| **Database (H2, Repository)** | ❌ No | ✅ Need to implement manually |

---

## 🛠️ Installation & Setup

### **1️⃣ Clone the Repository**
```sh
git clone https://github.com/your-username/task-api-demo.git
cd task-api-demo
```

### **2️⃣ Run the Application**
#### **Using Maven:**
```sh
mvn spring-boot:run
```
#### **Using Docker:**
```sh
docker build -t task-api-demo .
docker run -p 8080:8080 task-api-demo
```

## 🔑 Authentication & Token Retrieval
### **Login to Get a Token**
```sh
curl -X POST "http://localhost:8080/auth/login?username=admin&password=password"
```
Example Response:
```json
{
  "token": "your-jwt-token"
}
```

### **Use Token in API Requests**
Include the token in the `Authorization` header:
```sh
curl -X GET "http://localhost:8080/tasks" -H "Authorization: Bearer your-jwt-token"
```

## 📘 API Documentation
### **Swagger UI (REST API Docs)**
After starting the application, visit:
```
http://localhost:8080/swagger-ui.html
```

### **GraphQL Playground**
Query tasks via GraphQL:
```
http://localhost:8080/graphiql
```
Example Query:
```graphql
query {
  getAllTasks(page: 1, size: 5) {
    tasks {
      id
      title
      status
    }
    totalPages
    totalItems
  }
}
```

## 🛠️ Development & Contribution
1️⃣ **Fork the repo & create a new branch**
```sh
git checkout -b feature-branch
```
2️⃣ **Commit changes & push to GitHub**
```sh
git commit -m "Added new feature"
git push origin feature-branch
```
3️⃣ **Create a Pull Request!**

## 📜 License
This project is licensed under the MIT License.

---
🚀 **Happy Coding!** Contributions & feedback are welcome! 😊


