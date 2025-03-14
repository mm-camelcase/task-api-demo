# Task API Demo

🚀 **Task API Demo** – A fully featured **Task Management API** built with **Spring Boot**, supporting **both REST and GraphQL**.

## **🔹 Features**
👉 **REST & GraphQL API** – Flexible data querying options  
👉 **OpenAPI & Swagger UI** – Auto-generated API documentation  
👉 **JWT Authentication** – Secure login with token-based auth  
👉 **Simulated Login** – Test authentication easily in Swagger  
👉 **Validation & Error Handling** – Standardized request validation  
👉 **Pagination & Filtering** – Efficient data retrieval  

---

## **🏠 How It Was Built**
This project follows an **API-first** approach, where the API specification was defined first, and the code was **auto-generated**.

### **1️⃣ Define OpenAPI Specification**
- The API was designed using **OpenAPI 3.0**.
- The spec (`task-api-spec.yaml`) defines endpoints, request/response structures, and validation rules.
- Edited using [Swagger Editor](https://editor.swagger.io/).

### **2️⃣ Generate Code Using OpenAPI Generator**
To generate code using OpenAPI:
```sh
docker run --rm -v ${PWD}:/local -u $(id -u):$(id -g) openapitools/openapi-generator-cli generate \
  -i /local/task-api-spec.yaml \
  -g spring \
  -o /local \
  --additional-properties=library=spring-boot,useSpringBoot3=true,java17=true,dateLibrary=java8,interfaceOnly=true
```
👍 **Prevents `README.md` from being overwritten** using `.openapi-generator-ignore`.  
👍 **Keeps generated interfaces separate from business logic** (`interfaceOnly=true`).  

---

### **3️⃣ Add GraphQL Support**
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

---

## **📚 What is GraphQL?**
GraphQL is a **query language** for APIs that provides:
- **Flexible data retrieval** (fetch only the fields you need).
- **Efficient performance** (reduces over-fetching of data).
- **Self-documenting schema** (introspective API structure).
- **Single endpoint** (`/graphql`) instead of multiple REST endpoints.

👍 **Reuses the existing service layer** for task management.

---

## **🛠️ How to Use GraphQL**
### **1️⃣ Access GraphQL Playground**
- **URL:** `http://localhost:8080/graphiql`
- **Tool:** GraphiQL (a built-in UI for testing GraphQL queries).

---

### **2️⃣ Query: Get All Tasks**
```graphql
query {
  taskPage(page: 1, size: 5) {
    tasks {
      id
      title
      taskStatus
    }
    totalPages
    totalItems
  }
}
```
👍 **Returns paginated task data.**

---

### **3️⃣ Query: Get a Single Task**
```graphql
query {
  task(id: "1") {
    id
    title
    taskStatus
  }
}
```
👍 **Returns a specific task by ID.**

---

### **4️⃣ Mutation: Create a Task**
```graphql
mutation {
  create(taskCreateRequestInput: {
    title: "New Task",
    description: "A task for testing",
    taskStatus: PENDING,
    dueDate: "2025-04-01"
  }) {
    id
    title
    taskStatus
  }
}
```
👍 **Creates a new task.**

---

### **5️⃣ Mutation: Update a Task**
```graphql
mutation {
  update(id: "1", taskUpdateRequestInput: {
    title: "Updated Task",
    taskStatus: COMPLETED
  }) {
    id
    title
    taskStatus
  }
}
```
👍 **Updates an existing task.**

---

### **6️⃣ Mutation: Delete a Task**
```graphql
mutation {
  deleteTask(id: "1")
}
```
👍 **Deletes a task.**

---

## **📚 License**
This project is licensed under the **MIT License**.

---

## **🚀 Summary of Improvements**
1. **Added a dedicated GraphQL section** explaining what it is and how it works.
2. **Included commands for generating GraphQL schema from OpenAPI.**
3. **Expanded GraphQL examples for queries and mutations.**
4. **Restructured documentation for clarity and ease of use.**

🚀 **Now your README is clean, clear, and structured for both REST & GraphQL!** Let me know if you need modifications. 🔥

