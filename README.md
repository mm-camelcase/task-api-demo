# Task API Demo

🚀 **Task API Demo** – A fully featured **Task Management API** built with **Spring Boot**, supporting **both REST and GraphQL**.

## 🔹 Features
👉 **REST & GraphQL API** – Flexible data querying options  
👉 **OpenAPI & Swagger UI** – Auto-generated API documentation  
👉 **JWT Authentication** – Secure login with token-based auth  
👉 **Simulated Login** – Test authentication easily in Swagger  
👉 **Validation & Error Handling** – Standardized request validation  
👉 **Pagination & Filtering** – Efficient data retrieval  
👉 **Embedded H2 Database with Spring Data JPA** – Lightweight, in-memory persistence for easy testing

## 🏗️ How It Was Built
This project follows an **API-first** approach, where the API specification was defined first, and the code was auto-generated.

### REST API

#### **1️⃣ Define OpenAPI Specification**
- The API was designed using **OpenAPI 3.0**.
- The spec (`task-api-spec.yaml`) defines endpoints, request/response structures, and validation rules.
- Edited using [Swagger Editor](https://editor.swagger.io/).


#### **2️⃣ Auto-Generate Code Using OpenAPI Generator**

To generate code using OpenAPI:  

```sh
PROJECT_NAME=taskapi
PACKAGE_NAME=com.camelcase

docker run --rm -v ${PWD}:/local -u $(id -u):$(id -g) openapitools/openapi-generator-cli generate \
  -i /local/task-api-spec.yaml \
  -g spring \
  -o /local \
  --additional-properties=library=spring-boot,useSpringBoot3=true,java17=true,dateLibrary=java8,interfaceOnly=true \
  --api-package=${PACKAGE_NAME}.${PROJECT_NAME}.api \
  --model-package=${PACKAGE_NAME}.${PROJECT_NAME}.model \
  --group-id=${PACKAGE_NAME} \
  --artifact-id=${PROJECT_NAME} \
  --package-name=${PACKAGE_NAME}.${PROJECT_NAME}
```

👍 **Uses the OpenAPI specification (`task-api-spec.yaml`) created in the previous step as input.** 
👍 **Keeps generated interfaces separate from business logic** (`interfaceOnly=true`).  

#### **3️⃣ Implement Business Logic & Services**
- A **service layer** was added for clean separation of concerns.
- **H2 database** with **Spring Data JPA** was used for persistence and testing
- **Spring Security & JWT authentication** was implemented manually.


### GraphQL API

#### **4️⃣ Add GraphQL Support**

To generate the **GraphQL schema** from the existing OpenAPI spec, run:

```sh
docker run --rm -v $(pwd):/app -w /app -u $(id -u):$(id -g) node:22 npx openapi-to-graphql-cli ./task-api-spec.yaml \
--save schema.graphql

mv schema.graphql src/main/resources/graphql/task.graphqls
```
👍 **What This Command Does:**
- Converts the **OpenAPI 3.0 specification** into a **GraphQL schema**.
- Saves the schema to `schema.graphql`.
- Moves it to the correct location (`src/main/resources/graphql/task.graphqls`).

🛠️ **Additional Manual Steps Needed:**
- Fine-tune the **GraphQL schema** (e.g., add custom queries/mutations if needed).
- Implement **GraphQL resolvers** using the existing **service layer**.

----------------------------

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

## 📖 API Documentation
This project includes **multiple API documentation tools** to provide flexibility in exploring and testing the API.

| **Documentation Tool**  | **Usage** | **Supports Try It Out?** | **Authentication Support** | **Customization** |
|-------------------------|----------|-----------------|--------------------|----------------|
| **Swagger UI**          | API testing + Docs | ✅ Yes | ✅ OAuth2, Basic, API Key | Moderate |
| **ReDoc**               | Clean API Docs | ❌ No | ❌ No built-in auth | High |
| **Stoplight Elements**  | Interactive Docs + Testing | ✅ Yes | ✅ OAuth2, Basic, API Key | High |
| **RapiDoc**             | Customizable API Docs | ✅ Yes | ✅ OAuth2, Basic, API Key | High |

---

### **1️⃣ Swagger UI**
📌 **Best for:** Developers who want **"Try It Out"** functionality.  
🔗 **Access:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
✅ **Supports API testing, OAuth2, Basic Auth, and API Key authentication**.

**📸 Screenshot:**  
![Swagger UI Screenshot](./assets/images/swaggerui.png)

---

### **2️⃣ ReDoc**
📌 **Best for:** Clean, professional API documentation without interactive testing.  
🔗 **Access:** [http://localhost:8080/redoc](http://localhost:8080/redoc)  
❌ **No "Try It Out" feature** – for **viewing API specs only**.

**📸 Screenshot:**  
![ReDoc Screenshot](./assets/images/redoc.png)

---

### **3️⃣ Stoplight Elements**
📌 **Best for:** Interactive API documentation with testing and authentication.  
🔗 **Access:** [http://localhost:8080/stoplight](http://localhost:8080/stoplight)  
✅ **Supports OAuth2, Basic Auth, and interactive API testing**.

**📸 Screenshot:**  
![Stoplight Elements Screenshot](./assets/images/stoplight-elements.png)

---

### **4️⃣ RapiDoc**
📌 **Best for:** Highly customizable API documentation with interactive testing.  
🔗 **Access:** [http://localhost:8080/rapidoc](http://localhost:8080/rapidoc)  
✅ **Supports API testing and authentication with custom themes**.

**📸 Screenshot:**  
![RapiDoc Screenshot](./assets/images/rapidoc.png)

---

## **How to Choose the Right API Documentation Tool?**
- **Need API testing?** → Use **Swagger UI**, **Stoplight Elements**, or **RapiDoc**.
- **Need professional-looking static docs?** → Use **ReDoc**.
- **Want a customizable solution?** → Use **RapiDoc** or **Stoplight Elements**.


---


## 🔑 Authentication & Token Retrieval

- For this demo, authentication is handled via a **local auth endpoint** that issues tokens using the **OAuth2 Password Flow**.
- In a **production environment**, authentication would typically use the **Authorization Code Flow** with an external **OAuth2 provider** such as **Keycloak**, **Okta**, or **AWS Cognito**, ensuring enhanced security and proper identity federation.

## 🔑 Demo User Credentials

The demo user credentials are configured in the [``application.yml``](https://github.com/mm-camelcase/task-api-demo/blob/service-impl/src/main/resources/application.yml#L21). You can find the username and password there for authentication.

### **Login to Get a Token**



```sh
curl -X POST "http://localhost:8080/auth/login?username=admin&password=password"
```
Example Response:
```json
{
  "access_token": "your-jwt-token",
  "expires_in":"3600",
  "token_type":"Bearer"
}
```


### **Use Token in API Requests**
Include the token in the `Authorization` header:
```sh
curl -X GET "http://localhost:8080/tasks" -H "Authorization: Bearer your-jwt-token"
```

### **Authentication in API Docs**

Most of the API documentation tools used in this project **support authentication**, allowing users to securely test protected endpoints **directly within the documentation interface**.

- **Swagger UI, Stoplight Elements, and RapiDoc** support authentication via **OAuth2, Basic Auth, and API Keys**, enabling users to obtain tokens and include them in API requests.
- **ReDoc**, however, is **view-only** and does not support interactive authentication or API testing.

For this demo, authentication is handled using **OAuth2 Password Flow**, where users enter a **username and password** to receive a token. This token is then **automatically included** in API requests when testing endpoints. In a **production environment**, an **Authorization Code Flow** with an external **OAuth provider** (e.g., Keycloak, Okta) would typically be used for improved security and identity management.


**📸 Screenshot:**  
![RapiDoc Screenshot](./assets/images/docauth.png)

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


### TODO

- GraphQL in swagger etc.
- remove us english
- remove faker refs
