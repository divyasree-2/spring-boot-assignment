# Social Media Backend Engine

A scalable backend microservice built using Java 17 and Spring Boot that simulates social media interactions with Redis-powered concurrency guardrails, real-time virality scoring, cooldown enforcement, and distributed notification throttling using NeonDB and Upstash Redis.

## Features

- Create posts and comments using REST APIs
- Real-time virality scoring using Redis
- Atomic concurrency protection with Redis counters
- Bot interaction guardrails and cooldown enforcement
- Thread-safe handling of concurrent requests
- Notification throttling using Redis TTL
- Scheduled notification batching using Spring Scheduler
- PostgreSQL persistence with NeonDB
- Distributed state management using Upstash Redis


## Tech Stack

- Java 17
- Spring Boot 3.x
- PostgreSQL (NeonDB)
- Upstash Redis
- Spring Data JPA
- Spring Data Redis
- Maven
- REST APIs

## Engineering Challenges Solved

### Concurrency Protection
Implemented Redis atomic operations to prevent race conditions during high concurrent bot interactions.

### Horizontal Scaling Guardrails
Used Redis counters to strictly enforce maximum bot reply limits on posts.

### Cooldown Enforcement
Implemented TTL-based Redis cooldown keys to prevent repetitive bot-human interactions.

### Real-Time Virality Scoring
Designed a Redis-based scoring engine for instant interaction score updates without database bottlenecks.
## Cloud Services Used

### NeonDB
Used Neon serverless PostgreSQL for scalable cloud-hosted relational database management.

### Upstash Redis
Used Upstash Redis for atomic counters, cooldown locks, virality scoring, and notification throttling.

