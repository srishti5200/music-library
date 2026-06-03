# 🎵 Rhythmix — Music Library (Microservices)

A full-stack Music Library application built with Spring Boot microservices, Spring Security (JWT), H2 database, and a vanilla JS frontend.

---

## Architecture

```
┌──────────────────────────────────────────────────────┐
│                    Frontend (HTML/JS)                 │
│                  frontend/index.html                  │
└─────────────────────────┬────────────────────────────┘
                          │  HTTP (port 8080)
┌─────────────────────────▼────────────────────────────┐
│                   API Gateway :8080                   │
│             (Spring Cloud Gateway)                    │
└──────┬────────────────┬───────────────────┬──────────┘
       │                │                   │
┌──────▼──────┐  ┌──────▼──────┐  ┌────────▼───────┐
│User Service │  │Admin Service│  │Notification    │
│   :8081     │  │   :8082     │  │Service :8083   │
│  H2: userdb │  │  H2: admindb│  │H2: notifydb    │
└─────────────┘  └─────────────┘  └────────────────┘
       │                │                   │
       └────────────────┴───────────────────┘
                        │
            ┌───────────▼──────────┐
            │   Eureka Server      │
            │      :8761           │
            └──────────────────────┘
```

---

## Services & Ports

| Service              | Port | H2 Console                        |
|----------------------|------|-----------------------------------|
| Eureka Server        | 8761 | —                                 |
| API Gateway          | 8080 | —                                 |
| User Service         | 8081 | http://localhost:8081/h2-console  |
| Admin Service        | 8082 | http://localhost:8082/h2-console  |
| Notification Service | 8083 | http://localhost:8083/h2-console  |

---

## Swagger UI (per service)

| Service              | URL                                         |
|----------------------|---------------------------------------------|
| User Service         | http://localhost:8081/swagger-ui.html       |
| Admin Service        | http://localhost:8082/swagger-ui.html       |
| Notification Service | http://localhost:8083/swagger-ui.html       |
| Eureka Dashboard     | http://localhost:8761                       |

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Step 1 — Start Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```
Wait until you see `Started EurekaServerApplication`.

### Step 2 — Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

### Step 3 — Start User Service
```bash
cd user-service
mvn spring-boot:run
```

### Step 4 — Start Admin Service
```bash
cd admin-service
mvn spring-boot:run
```
A default admin is seeded automatically:
- **Email:** `admin@musiclibrary.com`
- **Password:** `admin@123`

### Step 5 — Start Notification Service
```bash
# Set Gmail credentials (for actual email sending)
export MAIL_USERNAME=your@gmail.com
export MAIL_PASSWORD=your-app-password
export SUBSCRIBER_EMAILS=user1@example.com,user2@example.com

cd notification-service
mvn spring-boot:run
```

### Step 6 — Open the Frontend
Open `frontend/index.html` in a browser (or serve with Live Server).

---

## API Reference

### Auth (User Service via Gateway)
```
POST /api/auth/register     — Register new user
POST /api/auth/login        — Login, get JWT token
```

### Songs (User Service)
```
GET  /api/songs                         — All visible songs
GET  /api/songs/{id}                    — Song details
GET  /api/songs/search?keyword=         — Search all fields
GET  /api/songs/search/singer?singer=   — Filter by singer
GET  /api/songs/search/album?album=     — Filter by album
GET  /api/songs/search/director?director= — Filter by director
```

### Playlists (User Service)
```
POST   /api/playlists                            — Create playlist
GET    /api/playlists                            — My playlists
GET    /api/playlists/{id}                       — Get playlist
PUT    /api/playlists/{id}                       — Update playlist
DELETE /api/playlists/{id}                       — Delete playlist
POST   /api/playlists/{id}/songs/{songId}        — Add song
DELETE /api/playlists/{id}/songs/{songId}        — Remove song
GET    /api/playlists/search?songName=           — Search by song name
```

### Admin (Admin Service)
```
POST  /api/admin/auth/login                      — Admin login
POST  /api/admin/songs                           — Add song
GET   /api/admin/songs                           — All songs (incl. hidden)
GET   /api/admin/songs/{id}                      — Song by ID
PUT   /api/admin/songs/{id}                      — Update song
DELETE /api/admin/songs/{id}                     — Delete song
PATCH /api/admin/songs/{id}/toggle-visibility    — Restrict/allow visibility
```

### Notifications (Notification Service)
```
POST /api/notifications/new-song    — Triggered by admin when song is added
POST /api/notifications/custom      — Custom email
GET  /api/notifications/logs        — View all notification logs
```

---
1. Replace H2 with MySQL/PostgreSQL per service.
2. Replace hard-coded JWT secret with Spring Cloud Config Server or Vault.
3. Add Resilience4j circuit breaker on Feign calls.
4. Use Kafka instead of Feign for notification events (true async).
5. Add refresh token support.
6. Admin panel UI can be built separately (React recommended).
