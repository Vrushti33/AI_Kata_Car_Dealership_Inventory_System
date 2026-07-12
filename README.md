# 🏁 Radiator Springs Showroom — Car Dealership Inventory System

Welcome to **Radiator Springs Showroom**, a full-stack car dealership inventory and transaction management system inspired by the rustic desert vibes of Route 66 and the *Cars* movie universe. 

The application is built with a secure, high-performance **Spring Boot 3** Java backend and a responsive, beautifully animated **React** single-page application frontend.

---

## 🚀 Deployed Application
* [Radiator Springs Showroom](https://radiator-springs-showroom.vercel.app/)

---

## 🛠️ Technology Stack

### Backend
* **Core Framework**: Spring Boot 3.3.4 (configured with Java 17/25 compatibility)
* **Security & Auth**: Spring Security with stateless JSON Web Tokens (JWT) stored in secure, tamper-proof `httpOnly` cookies.
* **Database**: PostgreSQL (Development/Production) & H2 (In-memory testing)
* **Migration & Seeding**: Flyway Migrations (Schema initialization, seeds 18 classic themed cars and default accounts)
* **Data Layer**: Spring Data JPA & Hibernate
* **Tests**: JUnit 5, Mockito, MockMvc, and Spring `@SpringBootTest` Integration Tests

### Frontend
* **Core Framework**: React 18 (bootstrapped with Vite)
* **Routing**: React Router DOM v6 (with fully protected paths and role guards)
* **HTTP Client**: Axios (configured with `withCredentials: true` for cookie-based auth)
* **Styling**: Vanilla CSS custom variables (designed with a dark desert sky theme, neon glows, glassmorphism panels, and a custom hover-shake "engine-revving" micro-animation)

---

## ✨ Features Implemented

### 1. Secure Authentication & Authorization
* **Cookie-Based Sessions**: Stateless JWT authentication stored in HTTP-Only cookies. No tokens are saved in vulnerable `localStorage`.
* **State Persistence**: The client automatically checks session health (`/api/auth/me`) on page load to restore user context.
* **Role Guards**: 
  * `USER` role: Access showroom, run searches, and execute purchases.
  * `ADMIN` role: Access the layout editor, adjust specs (add/edit/delete models), and restock inventory.

### 2. Interactive Vehicle Showroom
* **Unified Keyword Search**: Debounced search bar (400ms delay to prevent server overload) matching either `make` OR `model` names case-insensitively.
* **Advanced Filters**: Filter inventory dynamically by category (Sedan, SUV, Coupe, Truck, Electric, etc.) and price ranges.
* **Theme Styling**: 
  * Adaptive gradient cards based on vehicle category (e.g. electric is cyber-cyan, coupe is McQueen red).
  * Category badges styled as stamped **embossed license plates**.
  * Stock count status tags (`✔ IN STOCK: X`, `⚠ LOW STOCK: X LEFT` if <= 2, or `✖ SOLD OUT` blocking sales).

### 3. Transactional Inventory Controls
* **Purchase Flows**: Transactional purchases that decrement car inventory stock and record purchase timestamps, total pricing, and buyer profile metadata to the database.
* **Admin Management**: Direct restock tools (adding quantity units to existing cars) and complete CRUD panel to import/edit catalog specifications.
* **Price Boundaries**: Frontend HTML5 inputs and backend `@DecimalMin` validation strictly block zero-dollar ($0.00) or negative price entries.

### 4. Historic Transaction Logs
* **Personal Receipts**: Logged-in customers can track their own purchases (Receipt Log) showing quantities, model specs, pricing, and dates.
* **Master Ledger**: Admins can inspect the system-wide catalog purchase records (System Logs) detailing buyer names, email addresses, order details, and precise transaction timestamps.

### 5. Code Quality & Test Suite
* Over **72+ passing test cases** validating:
  * Database entity parsing and custom repository JPQL queries.
  * JWT generation, expiration boundaries, and filter validation.
  * AuthService registry conflicts and login matches.
  * Vehicle, Inventory CRUD, and Purchase History logs service transactions.
  * Role authorization guards and error translations for purchase logs.
  * Full end-to-end integration flows.

---

## 📂 Project Structure

```text
AI_Kata_Car_Dealership_Inventory_System/
├── backend/                       # Spring Boot Java application
│   ├── src/main/java/             # Backend source classes
│   │   └── com/cardealership/
│   │       ├── config/            # Security, CORS, and startup configs
│   │       ├── controller/        # REST Controllers (Auth, Vehicle, Inventory)
│   │       ├── dto/               # DTO Request/Response models
│   │       ├── entity/            # JPA Entities (User, Vehicle, Purchase)
│   │       ├── exception/         # Exception classes and Global Handler
│   │       ├── repository/        # Spring Data JPA repositories
│   │       └── service/           # Service layer implementations
│   ├── src/main/resources/
│   │   ├── db/migration/          # Flyway schema and seed SQL scripts
│   │   └── application.properties  # Database and Spring environment configs
│   └── pom.xml                    # Maven dependency manager
│
├── frontend/                      # Vite + React single-page application
│   ├── src/
│   │   ├── api/                   # Axios client instance and API definitions
│   │   ├── components/            # Reusable components (Navbar, Footer, VehicleCard)
│   │   ├── context/               # AuthContext managing user sessions
│   │   ├── hooks/                 # Custom hooks (useDebounce)
│   │   ├── pages/                 # SPA Pages (Dashboard, Login, Register, Add/Edit, PurchasesPage)
│   │   ├── routes/                # ProtectedRoute and AdminRoute guards
│   │   ├── index.css              # Global styles and design system variables
│   │   └── App.jsx                # Router switch mapping
│   ├── package.json               # Frontend dependencies and npm scripts
│   └── vite.config.js             # Vite development configurations
│
└── README.md                      # Documentation
```

---

## 🛠️ Setup and Installation

### Prerequisites
* Java JDK 17 or higher
* Node.js (v18+)
* PostgreSQL 17 database

### Database Setup
1. Create a local PostgreSQL database named `car_dealership`.
2. Configure database connection parameters (host, port, username, password) inside your system or in a `.env` file at the root of `backend/`:
   ```env
   DB_HOST=localhost
   DB_PORT=5433
   DB_NAME=car_dealership
   DB_USERNAME=postgres
   DB_PASSWORD=your_password
   ```

---

### Running the Backend

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Build the project and run the server using Maven:
   ```bash
   mvn spring-boot:run
   ```
   *Flyway will automatically run database migrations and seed default records.*
   *During startup, a CommandLineRunner checks and heals password hashes to ensure seeded accounts are fully functional.*

---

### Running the Frontend

1. Navigate to the frontend directory:
   ```bash
   cd ../frontend
   ```
2. Install npm dependencies:
   ```bash
   npm install
   ```
3. Boot the local Vite development server:
   ```bash
   npm run dev
   ```
4. Open your browser and navigate to `http://localhost:5173`.

---

## 👤 Default Accounts (Seeded)
You can log in immediately using these pre-configured profiles:
* **Showroom Customer**:
  * **Email**: `sally_carrera@route66.com`
  * **Password**: `User@123`
* **Dealership Admin**:
  * **Email**: `admin@cardealership.com`
  * **Password**: `Admin@123`

---

## 🤖 My AI Usage

### AI Tools Utilized
This project was developed with assistance from **Gemini** acting as an advanced agentic AI coding assistant and **Claude** acting as planning assistant.

### How it was Used
* **Architecture & Scaffolding**: Brainstormed the API endpoint hierarchy, relational database schemas (Flyway migrations), and state mappings.
* **Test-Driven Development (TDD)**: Partnered to write complete testing suites (using Junit 5, Mockito, MockMvc, and Spring `@SpringBootTest`) in lockstep with repository, security, service, and controller implementations.
* **Security & Authentication**: Configured JWT token creation, signature validation, custom filter parsing from HTTP cookies, and CORS configurations.
* **Troubleshooting & Bug Fixes**:
  * Resolved byte-buddy class formatting conflicts on JDK 25.
  * Troubleshot PostgreSQL type inference problems (`lower(bytea) does not exist`) by writing SQL-standard casts.
  * Implemented an automated self-healing `CommandLineRunner` in Java to verify and repair seeded database user password hashes on startup.
* **Frontend Design System**: Styled custom dark desert gradients, glow text effects, license-plate styles, and CSS micro-animations.

### Reflection
Integrating AI into this coding cycle fundamentally redefined the development speed, code quality and UI:
1. **Parallel Testing**: Writing tests and features concurrently is a bit slow; delegating boilerplate setup (such as MockMvc request builders and assertions) enabled complete test coverage without slowing down execution.
2. **Instant Debugging**: Instead of spending hours investigating obscure environment warnings (like the PostgreSQL parameter type inference crash or Java compiler major version compilation errors on JDK 25), I was able to get detailed root-cause analyses and code fix recommendations instantly.
3. **Focus on Quality**: AI handled structural layouts, repetitive properties mappings, and custom CSS styling rules, allowing me to focus heavily on the data transactional safety, clean coding principles, and end-to-end integration safety.

---

## Application Screenshots
<img width="1920" height="1140" alt="Screenshot 2026-07-12 154503" src="https://github.com/user-attachments/assets/61e0f5f3-a5c2-412a-9054-43997470b08f" />
<img width="1920" height="1140" alt="Screenshot 2026-07-12 154603" src="https://github.com/user-attachments/assets/104b16de-493a-4f18-8689-5134fab7a66d" />
<img width="1920" height="1140" alt="Screenshot 2026-07-12 154747" src="https://github.com/user-attachments/assets/17ed48ac-6dc5-401c-839d-bf40a1299fca" />
<img width="1920" height="1140" alt="Screenshot 2026-07-12 154853" src="https://github.com/user-attachments/assets/96247390-ae5b-450b-991e-6666ecca92b2" />
<img width="1920" height="1140" alt="Screenshot 2026-07-12 154951" src="https://github.com/user-attachments/assets/ca9dc370-5d07-4c26-8869-c5974e04e159" />
<img width="1920" height="1140" alt="Screenshot 2026-07-12 155000" src="https://github.com/user-attachments/assets/85719809-df5f-4a2d-a640-2e7ab2c3c7fe" />
<img width="1920" height="1140" alt="Screenshot 2026-07-12 155018" src="https://github.com/user-attachments/assets/18ad3fed-7b17-40a0-a394-a642a297ac7e" />
<img width="1920" height="1140" alt="Screenshot 2026-07-12 154841" src="https://github.com/user-attachments/assets/d639dad7-8fbf-491c-a901-3a86bec986e1" />
<img width="1920" height="1140" alt="Screenshot 2026-07-12 155023" src="https://github.com/user-attachments/assets/fa9a7c7c-212a-488a-a241-4d3c9a265c63" />
