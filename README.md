# 🛒 E-commerce Platform

Plateforme e-commerce professionnelle multi-produits construite avec **Angular**, **Spring Boot**, **Java 21**, **PostgreSQL**, **Docker** et **Stripe**.

L'objectif est de construire une application e-commerce moderne, maintenable, sécurisée et déployable sur un VPS avec une chaîne CI/CD complète.

---

## 📌 État du projet

**Statut :** 🟡 En développement

**Version :** `0.1.0-SNAPSHOT`

**Phase actuelle :** Initialisation et configuration technique

### Progression globale

| Domaine                | Statut |
| ---------------------- | ------ |
| Architecture du projet | 🟢     |
| Backend Spring Boot    | 🟢     |
| Frontend Angular       | 🟢     |
| PostgreSQL             | 🟢     |
| Flyway                 | 🟢     |
| Docker                 | 🟢     |
| Authentification       | 🟡     |
| Catalogue produits     | 🟢     |
| Panier                 | 🟡     |
| Commandes              | ⚪      |
| Paiement Stripe        | ⚪      |
| Administration         | ⚪      |
| Tests automatisés      | 🟡     |
| CI/CD                  | 🟡      |
| Déploiement VPS        | ⚪      |
| Monitoring             | ⚪      |

### Légende

* 🟢 Terminé
* 🟡 En cours / à corriger
* 🔵 Planifié
* ⚪ Pas encore commencé
* 🔴 Bloqué

---

# 📖 Sommaire

1. [Vision](#-vision)
2. [Objectifs](#-objectifs)
3. [Stack technique](#-stack-technique)
4. [Architecture](#-architecture)
5. [Structure du projet](#-structure-du-projet)
6. [Environnement de développement](#-environnement-de-développement)
7. [Installation](#-installation)
8. [Lancement](#-lancement)
9. [Base de données](#-base-de-données)
10. [Backend](#-backend)
11. [Frontend](#-frontend)
12. [Tests](#-tests)
13. [Sécurité](#-sécurité)
14. [Paiement](#-paiement)
15. [Docker](#-docker)
16. [CI/CD](#-cicd)
17. [Déploiement](#-déploiement)
18. [Roadmap](#-roadmap)
19. [Journal d'évolution](#-journal-dévolution)
20. [Règles de développement](#-règles-de-développement)

---

# 🎯 Vision

Construire une plateforme e-commerce professionnelle capable de gérer :

* plusieurs produits ;
* catégories ;
* comptes clients ;
* authentification ;
* panier ;
* commandes ;
* paiement en ligne ;
* gestion du stock ;
* espace client ;
* administration ;
* notifications ;
* déploiement automatisé.

L'application doit être conçue pour pouvoir évoluer sans devoir réécrire toute l'architecture.

---

# 🎯 Objectifs

## Fonctionnels

* [x] Consultation du catalogue
* [x] Recherche de produits
* [x] Filtrage et catégories initial
* [x] Fiche produit initiale
* [x] Gestion du panier initiale
* [x] Création de compte
* [x] Connexion / déconnexion
* [x] Gestion du profil
* [ ] Passage de commande
* [ ] Paiement Stripe
* [ ] Historique des commandes
* [ ] Gestion des stocks
* [ ] Administration des produits
* [ ] Administration des commandes
* [ ] Gestion des utilisateurs

## Techniques

* [ ] Architecture modulaire
* [ ] API REST
* [ ] Validation backend
* [ ] Gestion centralisée des erreurs
* [x] Authentification sécurisée initiale
* [x] Gestion des rôles initiale
* [x] Migrations Flyway
* [x] Tests unitaires de base
* [ ] Tests d'intégration
* [ ] Tests E2E
* [x] Dockerisation initiale
* [x] CI/CD initiale
* [ ] HTTPS
* [ ] Monitoring
* [ ] Logs structurés
* [ ] Déploiement VPS

---

# 🧰 Stack technique

## Backend

| Technologie     | Version / choix                 |
| --------------- | ------------------------------- |
| Java            | **21 LTS**                      |
| Spring Boot     | Version définie dans `pom.xml`  |
| Spring Web      | REST API                        |
| Spring Data JPA | ORM                             |
| Spring Security | Authentification / autorisation |
| Hibernate       | ORM                             |
| Flyway          | Migrations DB                   |
| Maven           | Build                           |
| JUnit           | Tests                           |
| Testcontainers  | Tests d'intégration             |

## Frontend

| Technologie      | Choix                       |
| ---------------- | --------------------------- |
| Angular          | Standalone                  |
| TypeScript       | Strict                      |
| SCSS             | Styling                     |
| Routing          | Angular Router              |
| State management | Signals / NgRx selon besoin |

## Base de données

* PostgreSQL 17
* Flyway
* Base de développement : `ecommerce`

## Infrastructure

* Docker
* Docker Compose
* Nginx ou Caddy
* GitHub Actions
* VPS Linux
* HTTPS

## Paiement

* Stripe Checkout
* Stripe Webhooks

---

# 🏗️ Architecture

Le projet utilise un **monolithe modulaire** avec une architecture inspirée de l'architecture hexagonale.

Les principaux modules backend sont :

```text
Catalog
Identity
Customer
Cart
Order
Payment
Inventory
```

Chaque module doit limiter ses dépendances vers les autres modules.

## Principes

* séparation des responsabilités ;
* faible couplage ;
* forte cohésion ;
* logique métier indépendante de l'infrastructure ;
* validation côté backend ;
* aucune confiance dans les données envoyées par le frontend ;
* transactions maîtrisées ;
* migrations de base de données versionnées.

---

# 📁 Structure du projet

```text
ecommerce/
│
├── backend/
│   └── ecommerce-api/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   └── resources/
│       │   │       └── db/
│       │   │           └── migration/
│       │   └── test/
│       ├── pom.xml
│       └── mvnw
│
├── frontend/
│   └── ecommerce-web/
│       ├── src/
│       ├── angular.json
│       ├── package.json
│       └── ...
│
├── infra/
│   ├── docker/
│   └── nginx/
│
├── .github/
│   └── workflows/
│
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

---

# 💻 Environnement de développement

## Prérequis

Installer :

* Git
* Java 21
* Maven Wrapper
* Node.js LTS
* npm
* Angular CLI
* Docker Desktop
* PostgreSQL client optionnel

Vérification :

```bash
java -version
node -v
npm -v
docker --version
git --version
```

Le projet doit utiliser **Java 21**.

---

# 🚀 Installation

Cloner le projet :

```bash
git clone <repository-url>
cd ecommerce
```

Créer le fichier d'environnement :

```bash
cp .env.example .env
```

Installer les dépendances frontend :

```bash
cd frontend/ecommerce-web
npm install
```

---

# ▶️ Lancement du projet

## 1. Démarrer PostgreSQL

Depuis la racine :

```bash
docker compose up -d
```

Vérifier :

```bash
docker compose ps
```

PostgreSQL doit apparaître comme :

```text
healthy
```

---

## 2. Démarrer le backend

```bash
cd backend/ecommerce-api
./mvnw spring-boot:run
```

API :

```text
http://localhost:8080
```

Health check :

```text
http://localhost:8080/api/v1/health
```

Réponse attendue :

```json
{
   "status": "UP",
   "service": "ecommerce-api"
}
```

---

## 3. Démarrer Angular

Dans un autre terminal :

```bash
cd frontend/ecommerce-web
npm start
```

Application :

```text
http://localhost:4200
```

---

# 🗄️ Base de données

Configuration de développement actuelle :

```text
Host: localhost
Port: 5432
Database: ecommerce
User: ecommerce
```

Le mot de passe est fourni par la configuration d'environnement.

La valeur locale par défaut est `ecommerce_dev`. Si le volume PostgreSQL a été
initialisé avec un autre mot de passe, il faut synchroniser le mot de passe du
rôle existant ou recréer le volume uniquement si aucune donnée ne doit être
conservée.

## Docker

Le conteneur PostgreSQL actuel :

```text
ecommerce-postgres
```

Vérification :

```bash
docker ps
```

Connexion :

```bash
docker exec -it ecommerce-postgres psql -U ecommerce -d ecommerce
```

---

# 🔄 Flyway

Les migrations sont stockées dans :

```text
backend/ecommerce-api/src/main/resources/db/migration/
```

Format :

```text
V1__create_products.sql
V2__create_categories.sql
V3__create_user_accounts.sql
V4__create_carts.sql
V5__add_profile_fields.sql
V6__seed_catalog.sql
```

### Règles

Une migration déjà exécutée ne doit pas être modifiée.

Pour modifier le schéma :

```text
Créer une nouvelle migration
        ↓
V2__...
        ↓
Commit Git
        ↓
Application
        ↓
Flyway
```

---

# ☕ Backend

Le backend expose une API REST versionnée.

Préfixe :

```text
/api/v1
```

Exemples futurs :

```text
GET    /api/v1/products
GET    /api/v1/products/{id}

POST   /api/v1/auth/register
Connexion HTTP Basic sur les endpoints protégés

GET    /api/v1/cart
POST   /api/v1/cart/items

POST   /api/v1/orders
GET    /api/v1/orders
GET    /api/v1/orders/{id}

GET    /api/v1/cart
POST   /api/v1/cart/items
PUT    /api/v1/cart/items/{productId}
DELETE /api/v1/cart/items/{productId}

POST   /api/v1/payments/checkout
```

---

# 🖥️ Frontend

Angular utilise :

* standalone components ;
* lazy loading ;
* routing ;
* services ;
* Signals lorsque suffisants ;
* NgRx uniquement lorsque la complexité du state le justifie.

Le frontend ne doit jamais être considéré comme une source de vérité pour :

* prix ;
* stock ;
* droits ;
* montant des commandes ;
* état du paiement.

Ces informations doivent être validées côté backend.

En développement, Angular utilise `proxy.conf.json` pour rediriger les appels
`/api` vers Spring Boot sur `http://localhost:8080`. L'application appelle
donc l'API via `/api/v1/health`.

---

# 🧪 Tests

## Backend

Tests unitaires :

```bash
./mvnw test
```

Tests d'intégration :

```text
JUnit
+
Testcontainers
+
PostgreSQL
```

## Frontend

```bash
npm test
```

Build frontend :

```bash
npm run build
```

## E2E

L'outil E2E sera choisi et configuré pendant la phase dédiée.

Scénario principal :

```text
Accueil
   ↓
Catalogue
   ↓
Produit
   ↓
Panier
   ↓
Connexion
   ↓
Checkout
   ↓
Stripe
   ↓
Commande
```

---

# 🔐 Sécurité

Principes obligatoires :

* mots de passe hashés ;
* aucune donnée sensible dans Git ;
* secrets dans les variables d'environnement ;
* validation backend ;
* contrôle d'accès côté serveur ;
* protection des endpoints ;
* CORS configuré explicitement ;
* HTTPS en production ;
* webhooks Stripe vérifiés ;
* protection contre les doubles traitements ;
* logs sans données sensibles.

L'inscription est disponible via `POST /api/v1/auth/register`. Les nouveaux
comptes reçoivent le rôle `CUSTOMER` et les mots de passe sont stockés avec
BCrypt. Les routes `/api/v1/admin/**` nécessitent le rôle `ADMIN`; la
provision d'un compte administrateur sera ajoutée avec la gestion complète des
utilisateurs.

Le frontend propose la page `/auth`. Un visiteur peut utiliser un panier local;
après connexion, les opérations du panier sont synchronisées avec l'API
persistante.

---

# 💳 Paiement Stripe

Le paiement sera réalisé avec **Stripe Checkout**.

Architecture prévue :

```text
Angular
   │
   │ demande checkout
   ▼
Spring Boot
   │
   │ crée Checkout Session
   ▼
Stripe
   │
   │ paiement
   ▼
Stripe Webhook
   │
   ▼
Spring Boot
   │
   ├── vérification signature
   ├── idempotence
   ├── confirmation commande
   └── mise à jour paiement
```

Le frontend ne doit jamais déterminer lui-même le montant final d'une commande.

---

# 🐳 Docker

Les Dockerfiles backend et frontend sont disponibles dans leurs projets
respectifs. Le Compose racine peut lancer PostgreSQL, Spring Boot et Angular
derrière Nginx :

Services prévus :

```text
┌───────────────────────┐
│       Frontend        │
│       Angular         │
└───────────┬───────────┘
            │
            ▼
┌───────────────────────┐
│       Backend         │
│      Spring Boot      │
└───────────┬───────────┘
            │
            ▼
┌───────────────────────┐
│      PostgreSQL       │
└───────────────────────┘
```

Commandes :

```bash
docker compose up -d
```

Le frontend est exposé sur `http://localhost` lorsque le port 80 est disponible.
Le frontend et le backend peuvent aussi être lancés localement avec les
commandes de la section [Lancement](#-lancement).

Arrêt :

```bash
docker compose down
```

Ne jamais utiliser :

```bash
docker compose down -v
```

sans vérifier au préalable si des données doivent être conservées.

---

# 🔁 CI/CD

Un workflow initial est disponible dans `.github/workflows/ci.yml`. Il exécute
la vérification Maven du backend et le build Angular du frontend sur les pushes
vers `main` et `develop`, ainsi que sur les Pull Requests.

Pipeline prévu :

```text
Git Push
   ↓
GitHub Actions
   ↓
Build
   ↓
Tests
   ↓
Analyse
   ↓
Docker Build
   ↓
Docker Registry
   ↓
Déploiement VPS
   ↓
Health Check
```

## Pull Request

Chaque Pull Request doit au minimum :

* compiler ;
* passer les tests ;
* respecter les conventions du projet.

---

# 🌍 Déploiement

Environnement cible :

```text
VPS Linux
      │
      ├── Reverse Proxy
      │       └── HTTPS
      │
      ├── Frontend
      │
      ├── Backend
      │
      └── PostgreSQL
```

Les secrets de production ne doivent jamais être stockés dans Git.

---

# 🗺️ Roadmap

## Phase 0 — Initialisation

* [x] Création du repository
* [x] Structure backend
* [x] Structure frontend
* [x] PostgreSQL Docker
* [x] Spring Boot
* [x] Flyway
* [x] Endpoint de santé backend
* [x] Service de santé frontend
* [x] Proxy Angular de développement
* [x] Dockerfiles backend et frontend
* [x] Compose full-stack initial
* [x] Workflow CI initial
* [x] Configurer Java 21 comme cible Maven
* [ ] Installer Java 21 dans l'environnement local

## Phase 1 — Architecture backend

* [ ] Architecture modulaire
* [ ] Modules métier
* [ ] Gestion des exceptions
* [ ] DTO
* [ ] Validation
* [ ] API versionnée
* [ ] Documentation API

## Phase 2 — Catalogue

* [x] Produit
* [x] Catégorie
* [ ] Prix
* [ ] Stock
* [ ] Images
* [x] CRUD administration initial des produits et catégories
* [x] API catalogue de lecture
* [x] Interface catalogue initiale

## Phase 3 — Identité

* [x] Inscription
* [x] Connexion HTTP Basic initiale
* [x] Authentification persistée
* [x] Rôles CUSTOMER et ADMIN
* [x] Profil client initial
* [x] Protection initiale de l'administration catalogue

## Phase 4 — Panier

* [x] Création panier frontend
* [x] Ajout produit
* [x] Modification quantité
* [x] Suppression
* [x] Calcul total
* [x] Panier persistant côté backend
* [ ] Validation stock

## Phase 5 — Commandes

* [ ] Création commande
* [ ] Statuts
* [ ] Historique
* [ ] Adresse
* [ ] Calcul montant
* [ ] Gestion transactionnelle

## Phase 6 — Paiement

* [ ] Stripe Checkout
* [ ] Webhook
* [ ] Vérification signature
* [ ] Idempotence
* [ ] Confirmation paiement
* [ ] Gestion des erreurs

## Phase 7 — Tests

* [ ] Unit tests
* [ ] Integration tests
* [ ] Testcontainers
* [ ] E2E
* [ ] Tests sécurité

## Phase 8 — Production

* [x] Docker images
* [x] GitHub Actions initiales
* [ ] VPS
* [ ] Reverse proxy
* [ ] HTTPS
* [ ] Monitoring
* [ ] Backup PostgreSQL
* [ ] Logs
* [ ] Health checks

---

# 📈 Journal d'évolution

Cette section doit être mise à jour à chaque étape importante.

## 2026-09-21 — Initialisation et socle technique

### Réalisé

* Création du projet e-commerce.
* Backend Spring Boot créé.
* Frontend Angular créé.
* PostgreSQL 17 configuré avec Docker.
* Flyway configuré.
* Endpoint de santé ajouté.
* Service de santé Angular ajouté.
* Proxy Angular configuré pour le développement local.
* Dockerfiles backend et frontend ajoutés.
* Compose full-stack configuré avec PostgreSQL, backend et frontend.
* Nginx configuré pour servir l'application Angular et proxyfier `/api`.
* Workflow GitHub Actions initial ajouté.
* Tests backend et frontend exécutés avec succès.
* Images Docker backend et frontend construites avec succès.

### Problème résolu

**PostgreSQL**

Lors de la première initialisation, Spring Boot obtenait :

```text
FATAL: password authentication failed for user "ecommerce"
```

Le mot de passe du rôle `ecommerce` a été synchronisé avec `ecommerce_dev`,
sans supprimer le volume ni les données existantes.

### Prochaines étapes

1. Installer Java 21 dans l'environnement local.
2. Ajouter la création de commandes à partir du panier.
3. Ajouter l’historique des commandes côté client.

---

# 📝 Règles de développement

## Avant de coder

Toujours :

```bash
git status
```

Puis comprendre l'impact de la modification.

## Avant une modification importante

Créer un commit propre :

```bash
git add .
git commit -m "description"
```

## Après modification

Exécuter les tests :

```bash
./mvnw test
```

et/ou :

```bash
npm test
```

## Règles générales

* Ne pas modifier une migration Flyway déjà exécutée.
* Ne pas mettre de secrets dans Git.
* Ne pas faire confiance aux montants envoyés par Angular.
* Ne pas supprimer de volume Docker sans confirmation.
* Ne pas introduire de dépendance inutile.
* Préférer une solution simple avant une abstraction complexe.
* Ajouter des tests pour la logique métier importante.
* Garder les modules faiblement couplés.

---

# 🤖 Utilisation avec Codex

Codex peut être utilisé pour développer ce projet, mais les modifications doivent rester contrôlées.

Avant chaque tâche importante, Codex doit :

1. analyser le code existant ;
2. identifier les fichiers concernés ;
3. expliquer brièvement son plan ;
4. effectuer les modifications ;
5. lancer les tests appropriés ;
6. signaler les éventuels problèmes ;
7. résumer les fichiers modifiés.

## Règle importante

Codex ne doit pas :

* supprimer des données de production ;
* supprimer un volume Docker contenant des données ;
* exposer des secrets ;
* modifier les secrets GitHub ;
* modifier une migration Flyway déjà appliquée ;
* effectuer une opération destructive sans confirmation.

---

# 📌 Definition of Done

Une fonctionnalité est considérée comme terminée lorsqu'elle respecte les critères suivants :

* [ ] Code implémenté
* [ ] Architecture respectée
* [ ] Validation ajoutée
* [ ] Gestion des erreurs ajoutée
* [ ] Tests ajoutés
* [ ] Tests réussis
* [ ] Documentation mise à jour
* [ ] Aucun secret dans le repository
* [ ] Git diff vérifié
* [ ] Commit réalisé

---

# 📊 Versioning

Le projet utilise le versioning sémantique :

```text
MAJOR.MINOR.PATCH
```

Exemple :

```text
0.1.0
0.2.0
1.0.0
```

Avant la version `1.0.0`, le projet est considéré comme en développement.

---

# 📚 Documentation complémentaire

La documentation détaillée pourra progressivement être organisée ainsi :

```text
docs/
├── architecture/
├── backend/
├── frontend/
├── database/
├── security/
├── payment/
├── docker/
├── deployment/
├── testing/
└── decisions/
```

Les décisions techniques importantes pourront être enregistrées sous forme d'ADR :

```text
docs/decisions/
├── ADR-001-architecture.md
├── ADR-002-postgresql.md
├── ADR-003-authentication.md
└── ADR-004-stripe.md
```

---

# 🚀 Objectif final

Obtenir une plateforme e-commerce professionnelle :

```text
Angular
   │
   ▼
Spring Boot API
   │
   ├── Identity
   ├── Catalog
   ├── Cart
   ├── Order
   ├── Payment
   ├── Inventory
   └── Customer
   │
   ▼
PostgreSQL

        +

Docker
        +
GitHub Actions
        +
VPS
        +
HTTPS
        +
Monitoring
```

Le README est le document de référence permettant de suivre l'évolution technique et fonctionnelle du projet.
