[Français](README.md) | [English](README.en.md)

# CartSync - API REST

API backend de synchronisation hors-ligne (Offline-First) de listes de courses et moteur de calcul de budget en temps réel, conçue selon les principes de l'**Architecture Hexagonale (Ports et Adaptateurs)**.

---

## 🎯 Fonctionnalités Clés

- **Gestion collaborative de listes :** Création, partage entre membres, calcul dynamique du total du panier.
- **Moteur de budget & jauges de seuil :**
  - 🟢 **Vert** : Dépenses < 80% du budget alloué
  - 🟠 **Orange** : Dépenses entre 80% et 99% (zone d'alerte)
  - 🔴 **Rouge** : Dépenses >= 100% (dépassement budgétaire)
- **Synchronisation par lots (Offline-First) :** Endpoint `POST /api/sync` traitant des opérations générées hors-ligne avec garantie d'**idempotence** (aucun doublon lors des réémissions réseau).
- **Catalogue externe :** Enrichissement automatique des produits par code-barres (GTIN) via l'API Open Food Facts.
- **Analyse de consommation :** Publication asynchrone des paniers validés pour traitement analytique.

---

## 🏛️ Architecture Hexagonale

Le projet garantit un découplage strict entre la logique métier et les détails d'infrastructure :

- **`domain` (Cœur métier pur, sans framework) :**
  - **Modèles & Value Objects :** `ShoppingList`, `Item`, `Product`, `Budget`, `BudgetStatus`, `SyncOperation`.
  - **Ports In (Use Cases) :** `ManageListUseCase`, `CalculateBudgetUseCase`, `SyncBatchUseCase`, `ValidateCartUseCase`.
  - **Ports Out (Contrats d'infrastructure) :** `ShoppingListRepositoryPort`, `ExternalCatalogPort`, `ConsumptionAnalyticsPort`, `SyncJournalPort`.
  - **Moteurs métier :** `BudgetEngine`, `SyncEngine`.
- **`infrastructure` (Adaptateurs techniques) :**
  - **Inbound Web :** Contrôleurs REST (`ShoppingListController`, `SyncController`, `CatalogController`).
  - **Outbound Persistence :** Adaptateur relationnel JPA / PostgreSQL (`ShoppingListPersistenceAdapter`).
  - **Outbound External :** Adaptateur HTTP Open Food Facts (`OpenFoodFactsAdapter`).
  - **Outbound Analytics :** Adaptateur asynchrone (`AsyncConsumptionAnalyticsAdapter`).

---

## 📡 Principaux Endpoints REST

| Méthode | Route | Description |
|---|---|---|
| `POST` | `/api/sync` | Envoi d'un lot d'opérations hors-ligne (batch sync idempotent) |
| `POST` | `/api/lists` | Créer une nouvelle liste de courses |
| `GET` | `/api/lists` | Lister les listes accessibles par l'utilisateur |
| `GET` | `/api/lists/{id}` | Consulter le détail d'une liste et ses articles |
| `POST` | `/api/lists/{id}/items` | Ajouter un article (avec enrichissement automatique GTIN) |
| `PATCH` | `/api/lists/{id}/items/{itemId}/toggle` | Marquer un article comme acheté / non acheté |
| `GET` | `/api/lists/{id}/budget` | Consulter l'état du budget et sa jauge |
| `POST` | `/api/lists/{id}/validate` | Valider le panier et déclencher l'analyse asynchrone |
| `GET` | `/api/catalog/{gtin}` | Rechercher les informations produit par code-barres |

---

## 🚀 Démarrage

### Prérequis
- Java 17 ou supérieur
- Apache Maven 3.8+

### Lancer les tests unitaires et d'intégration
```bash
mvn test
```

### Lancer l'application en local (profil H2 par défaut)
```bash
mvn spring-boot:run
```

L'application démarre sur [http://localhost:8080](http://localhost:8080).
La console H2 est accessible sur [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:cartsyncdb`).

### Lancer avec PostgreSQL (profil prod)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 📜 Licence

Projet sous licence open-source à vocation pédagogique et portfolio.
