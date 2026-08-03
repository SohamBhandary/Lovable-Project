# 🤖 Lovable — AI-Powered Code Generation SaaS

> **Build complete React applications from natural-language prompts.**

Lovable is a distributed AI SaaS platform that allows users to create and modify React applications using natural-language instructions.

For example:

```text
Build a Snake game in React
```

The platform uses **Spring Boot, Spring AI, GPT-4o-mini, Kafka, PostgreSQL + pgvector, MinIO, Docker, and Kubernetes** to manage AI-powered code generation, project files, conversations, and isolated application execution.

---

## 🚀 What Lovable Does

The platform allows users to:

* 🔐 Create accounts and authenticate
* 📁 Create and manage projects
* 🤖 Generate React applications using AI
* 💬 Continue conversations with the AI within a project
* 🧠 Maintain conversational context
* 📖 Allow the AI to read existing project files
* ✏️ Create and edit project files based on user instructions
* 🌳 Explore the generated project file structure
* 📄 View individual file contents
* 📦 Download the complete project as a ZIP
* ☸️ Run generated applications inside Kubernetes Runner Pods
* 🌐 Access the generated application's preview

---

# 🏗️ Architecture

Lovable is built around a distributed architecture combining AI, asynchronous communication, persistent storage, object storage, and container orchestration.

```text
                         👤 User
                           │
                           ▼
                    React Frontend
                           │
                           ▼
                    Spring Boot API
                           │
            ┌──────────────┼──────────────┐
            │              │              │
            ▼              ▼              ▼
       Spring AI        Kafka       PostgreSQL
            │                           │
            ▼                           │
       GPT-4o-mini                  pgvector
            │
            ▼
     AI Code Generation
            │
            ▼
    Read / Create / Edit
        Project Files
            │
            ▼
          MinIO
            │
            ▼
     Kubernetes Runner
           Pods
            │
            ▼
      React Application
            │
            ▼
      🌐 Live Preview
```

---

# 🤖 AI Code Generation

The core functionality of Lovable is AI-powered application development.

The user provides instructions in natural language, and the AI works with the project's existing codebase.

### Example

```text
User:
Build a Snake game in React.

        ↓

AI understands the request

        ↓

AI creates the required files

        ↓

React application generated
```

The AI can also work iteratively with an existing project.

```text
User:
Add a score counter to the game.

        ↓

AI reads the existing project files

        ↓

AI understands the current implementation

        ↓

AI edits the required files
```

This allows users to progressively build and modify their applications through conversation.

---

# 🧠 Context-Aware AI Coding

Lovable maintains chat sessions associated with projects.

The AI can work with the existing project context rather than treating every request as an isolated generation task.

### Chat functionality

```text
✓ List Chat Sessions
✓ Create New Chat Session
✓ Load Full Chat History
✓ Stream AI Responses
```

Users can continue conversations and ask the AI to modify their existing application.

---

# 📁 Project Management

Lovable provides project-level management for generated applications.

### Project functionality

```text
✓ Create Project
✓ Manage Project
✓ List Projects
```

Each project represents an individual application that can be generated and modified through the AI coding workflow.

---

# 🌳 File Management

Generated applications are maintained as complete project file structures.

Lovable provides functionality to inspect and retrieve those files.

### File functionality

```text
✓ Get File Tree + Metadata
✓ Get File Content
```

### Example

```text
project/
├── src/
│   ├── components/
│   ├── App.jsx
│   └── main.jsx
├── public/
├── package.json
├── vite.config.js
└── index.html
```

The AI can read the existing files and make targeted changes based on subsequent user instructions.

---

# ☸️ Kubernetes Runner Pods

Generated React applications are executed inside **Kubernetes Runner Pods**.

This allows the generated application to run in an isolated environment rather than simply returning the generated source code to the user.

### Execution Flow

```text
Generated React Project
          │
          ▼
     Project Files
          │
          ▼
     MinIO Storage
          │
          ▼
   Kubernetes Runner
          │
          ▼
    React Application
          │
          ▼
     🌐 Preview
```

The Runner environment provides the execution layer for generated applications.

---

# 🌐 Project Preview

Once the generated React application is running, users can access its preview through the platform.

```text
✓ Get Project Preview
```

This creates the final step in the workflow:

```text
Natural Language
      ↓
AI Code Generation
      ↓
Create / Edit Files
      ↓
Project Storage
      ↓
Kubernetes Runner Pod
      ↓
Running React Application
      ↓
🌐 Live Preview
```

---

# 🔐 Authentication

Lovable includes user authentication and profile management.

### Authentication functionality

```text
✓ Signup
✓ Login
✓ Get My Profile
```

Authentication allows projects and AI coding sessions to be associated with individual users.

---

# 🧩 Technology Stack

| Category         | Technology                  |
| ---------------- | --------------------------- |
| Backend          | Java, Spring Boot           |
| AI Integration   | Spring AI                   |
| LLM              | GPT-4o-mini                 |
| Frontend         | React                       |
| Build Tool       | Vite                        |
| Database         | PostgreSQL                  |
| Vector Database  | pgvector                    |
| Messaging        | Apache Kafka                |
| Object Storage   | MinIO                       |
| Containerization | Docker                      |
| Orchestration    | Kubernetes                  |
| Architecture     | Distributed / Microservices |

---

# 🔄 End-to-End Workflow

A typical Lovable workflow looks like this:

```text
1. User signs up / logs in
              ↓
2. User creates a project
              ↓
3. User provides a natural-language prompt
              ↓
4. Spring AI communicates with GPT-4o-mini
              ↓
5. AI understands the requested application
              ↓
6. AI reads existing project files when required
              ↓
7. AI creates or edits project files
              ↓
8. Project files are stored and synchronized
              ↓
9. Kubernetes Runner Pod executes the application
              ↓
10. User accesses the project preview
```

---

# 🧱 Core Components

### Spring Boot

Acts as the backend foundation of the platform and handles the application's core APIs and business logic.

### Spring AI

Provides the AI integration layer used to communicate with the LLM and implement the coding workflow.

### GPT-4o-mini

Used as the underlying language model for natural-language understanding and code generation/editing.

### Apache Kafka

Used for asynchronous communication between services within the distributed architecture.

### PostgreSQL + pgvector

PostgreSQL handles persistent application data, while **pgvector** provides vector storage capabilities for AI-related contextual information.

### MinIO

Used for storing and synchronizing generated project files and application assets.

### Kubernetes

Used to orchestrate isolated Runner Pods where generated React applications can be executed.

---

# 💡 Project Workflow at a Glance

```text
              👤 USER
                 │
                 ▼
       Natural Language Prompt
                 │
                 ▼
        🤖 Spring AI + GPT-4o-mini
                 │
                 ▼
       🧠 Understand Project Context
                 │
                 ▼
          📖 Read Existing Files
                 │
                 ▼
          ✏️ Create / Edit Files
                 │
                 ▼
              📦 MinIO
                 │
                 ▼
          ☸️ Kubernetes
                 │
                 ▼
           Runner Pod
                 │
                 ▼
        ⚛️ React Application
                 │
                 ▼
           🌐 Live Preview
```

---

# 🎯 Project Objective

Lovable aims to reduce the gap between **describing an application** and **having a working application**.

Instead of manually creating the project structure, writing components, managing files, and setting up an execution environment, users can interact with the platform through natural language while the system handles the underlying application generation and execution workflow.

> **Describe your idea. Let AI build and modify the application. 🚀**
