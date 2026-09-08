# Kestachet - API REST

API backend de gestion de listes de courses partagées, moteur de calcul de budget et synchronisation hors-ligne (Offline-First), conçue selon l'**Architecture Hexagonale (Ports et Adaptateurs)**.

---

## 🎯 Fonctionnalités

- **Gestion des listes de courses :** Création, partage entre membres, calcul en temps réel du total du panier.
- **Moteur de budget & seuils :**
  - 🟢 **Vert** : Dépenses < 80% du budget
  - 🟠 **Orange** : Dépenses entre 80% et 99%
  - 🔴 **Rouge** : Dépenses >= 100% (dépassement)
- **Synchronisation par lots (Offline-First) :** Endpoint `POST /api/sync` recevant les opérations effectuées hors-ligne avec traitement **idempotent** (aucun doublon en cas de réémission réseau).
- **Catalogue externe :** Enrichissement automatique des produits par code-barres (GTIN) via l'API Open Food Facts.
- **Analyse de consommation :** Publication asynchrone des paniers validés pour traitement statistique.

---

## 🏛️ Architecture Hexagonale

Le projet sépare strictement la logique métier des détails techniques :

- **`domain` (Cœur métier pur) :**
  - Modèles : `ShoppingList`, `Item`, `Product`, `Budget`, `BudgetStatus`, `SyncOperation`.
  - Ports In (Use Cases) : `ManageListUseCase`, `CalculateBudgetUseCase`, `SyncBatchUseCase`, `ValidateCartUseCase`.
  - Ports Out (Contrats) : `ShoppingListRepositoryPort`, `ExternalCatalogPort`, `ConsumptionAnalyticsPort`, `SyncJournalPort`.
  - Moteurs : `BudgetEngine`, `SyncEngine`.
- **`infrastructure` (Adaptateurs) :**
  - **Inbound Web :** Contrôleurs REST (`ShoppingListController`, `SyncController`, `CatalogController`).
  - **Outbound Persistence :** Adaptateur PostgreSQL / JPA (`ShoppingListPersistenceAdapter`).
  - **Outbound External :** Adaptateur HTTP Open Food Facts (`OpenFoodFactsAdapter`).
  - **Outbound Analytics :** Adaptateur asynchrone (`AsyncConsumptionAnalyticsAdapter`).

---

## 🚀 Démarrage

### Prérequis
- Java 17 ou 21
- Maven

### Lancer les tests
```bash
mvn test
```

### Lancer l'application en local (avec base H2 en mémoire)
```bash
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`.
La console H2 est accessible sur `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:kestachetdb`).

### Principaux Endpoints REST

| Méthode | Route | Description |
|---|---|---|
| `POST` | `/api/sync` | Envoi d'un lot d'opérations hors-ligne (batch) |
| `POST` | `/api/lists` | Créer une liste de courses |
| `GET` | `/api/lists` | Récupérer les listes accessibles par l'utilisateur |
| `GET` | `/api/lists/{id}` | Détail d'une liste et de ses articles |
| `POST` | `/api/lists/{id}/items` | Ajouter un article (avec enrichissement GTIN) |
| `PATCH` | `/api/lists/{id}/items/{itemId}/toggle` | Cocher / décocher un article acheté |
| `GET` | `/api/lists/{id}/budget` | Consulter l'état du budget et sa jauge |
| `POST` | `/api/lists/{id}/validate` | Valider le panier et déclencher l'analyse |
| `GET` | `/api/catalog/{gtin}` | Rechercher un produit par son code-barres |
