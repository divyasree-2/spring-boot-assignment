# Spring Boot Assignment - Grid07

## Tech Stack
- Java 17
- Spring Boot 3.x
- PostgreSQL
- Redis
- Docker

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

For the Atomic Locks in Phase 2, I used Redis `INCR` command which is a single atomic operation on the Redis server. Even when 200 concurrent requests hit the API at the same millisecond, each request gets a unique incremented value back from Redis. This means only the first 100 requests will get values 1-100 and proceed, while request 101 onwards will get blocked with a 429 Too Many Requests response. No two threads can ever get the same count value, which guarantees the bot cap stops at exactly 100.

## Redis Keys Used

| Key | Purpose |
|-----|---------|
| post:{id}:virality_score | Tracks virality score |
| post:{id}:bot_count | Tracks bot reply count |
| cooldown:bot_{id}:human_{id} | 10 min cooldown |
| notif_cooldown:user_{id} | 15 min notification cooldown |
| user:{id}:pending_notifs | Pending notifications list |