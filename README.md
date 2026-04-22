# Spring Boot Assignment - Grid07

## Tech Stack
- Java 17
- Spring Boot 3.x
- PostgreSQL (Neon.tech)
- Redis (Upstash)

## How to Run

### Step 1: Start PostgreSQL and Redis
docker-compose up -d

### Step 2: Run the Application
mvn spring-boot:run

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/posts | Create a new post |
| POST | /api/posts/{postId}/comments | Add a comment |
| POST | /api/posts/{postId}/like | Like a post |

## How I Guaranteed Thread Safety

I used Redis INCR command for atomic operations. When 200 concurrent requests hit the API, each gets a unique incremented value. Only first 100 succeed, rest get 429 Too Many Requests. This guarantees bot cap stops at exactly 100.

## Redis Keys

| Key | Purpose |
|-----|---------|
| post:{id}:virality_score | Virality score |
| post:{id}:bot_count | Bot reply count |
| cooldown:bot_{id}:human_{id} | 10 min cooldown |
| notif_cooldown:user_{id} | 15 min notification cooldown |
| user:{id}:pending_notifs | Pending notifications |
