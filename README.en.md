[Français](README.md) | [English](README.en.md)

# CartSync - REST API

Backend API for collaborative grocery list synchronization (Offline-First) and dynamic budget calculation, built following **Hexagonal Architecture (Ports and Adapters)** principles.

---

## 🎯 Key Features

- **Collaborative List Management:** Create and share lists among household members with dynamic cart total calculation.
- **Budget Engine & Alert Thresholds:**
  - 🟢 **Green**: Spending < 80% of allocated budget
  - 🟠 **Orange**: Spending between 80% and 99% (warning zone)
  - 🔴 **Red**: Spending >= 100% (budget overrun)
- **Batch Synchronization (Offline-First):** Dedicated `POST /api/sync` endpoint processing offline operations with **idempotency** guarantees (no duplicate items on network retries).
- **External Product Catalog:** Automated product metadata enrichment via barcode (GTIN) through the Open Food Facts API.
- **Consumption Analytics:** Asynchronous event publishing on cart validation for analytics processing.

---

## 🏛️ Hexagonal Architecture

The codebase enforces strict decoupling between domain business rules and technical infrastructure:

- **`domain` (Pure core domain, zero framework dependencies):**
  - **Models & Value Objects:** `ShoppingList`, `Item`, `Product`, `Budget`, `BudgetStatus`, `SyncOperation`.
  - **Inbound Ports (Use Cases):** `ManageListUseCase`, `CalculateBudgetUseCase`, `SyncBatchUseCase`, `ValidateCartUseCase`.
  - **Outbound Ports (Infrastructure Contracts):** `ShoppingListRepositoryPort`, `ExternalCatalogPort`, `ConsumptionAnalyticsPort`, `SyncJournalPort`.
  - **Domain Engines:** `BudgetEngine`, `SyncEngine`.
- **`infrastructure` (Technical Adapters):**
  - **Inbound Web:** REST Controllers (`ShoppingListController`, `SyncController`, `CatalogController`).
  - **Outbound Persistence:** JPA / PostgreSQL relational adapter (`ShoppingListPersistenceAdapter`).
  - **Outbound External:** HTTP client adapter for Open Food Facts (`OpenFoodFactsAdapter`).
  - **Outbound Analytics:** Asynchronous event publisher (`AsyncConsumptionAnalyticsAdapter`).

---

## 📡 Key REST Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/sync` | Submit a batch of offline operations (idempotent sync) |
| `POST` | `/api/lists` | Create a new shopping list |
| `GET` | `/api/lists` | List shopping lists accessible by the user |
| `GET` | `/api/lists/{id}` | Retrieve details and items of a specific list |
| `POST` | `/api/lists/{id}/items` | Add item (with automatic GTIN barcode lookup) |
| `PATCH` | `/api/lists/{id}/items/{itemId}/toggle` | Toggle item purchased state |
| `GET` | `/api/lists/{id}/budget` | Inspect budget balance and threshold gauge |
| `POST` | `/api/lists/{id}/validate` | Validate cart and trigger async analytics |
| `GET` | `/api/catalog/{gtin}` | Query product information by barcode |

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Apache Maven 3.8+

### Run Unit and Integration Tests
```bash
mvn test
```

### Run Locally (H2 in-memory default profile)
```bash
mvn spring-boot:run
```

The application runs on [http://localhost:8080](http://localhost:8080).
H2 Web Console is available at [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:cartsyncdb`).

### Run with PostgreSQL (prod profile)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 📜 License

Open-source project built for educational and portfolio demonstration purposes.
