┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│   Client    │────▶│  API Gateway │────▶│  Validator  │
│ (REST/gRPC) │     │   (Axum)     │     │  (Tokio)    │
└─────────────┘     └──────────────┘     └──────┬──────┘
                                                 │
                    ┌────────────────────────────┼──────────────────┐
                    │                            │                  │
             ┌──────▼──────┐           ┌─────────▼───────┐  ┌──────▼──────┐
             │ PostgreSQL  │           │     Redis       │  │    Kafka    │
             │ (транзакции)│           │ (кэш/лимиты)    │  │ (события)   │
             └─────────────┘           └─────────────────┘  └─────────────┘

Real-time transaction monitor

// POST /api/v1/transactions
{
  "user_id": "user_123",
  "amount": 1000.50,
  "currency": "RUB",
  "merchant_id": "merchant_456",
  "timestamp": "2026-01-15T10:30:00Z"
}

# Запуск
cargo run

# В другом терминале проверяем:
# 1. Health check
curl http://localhost:3000/health

# 2. Отправляем транзакцию
curl -X POST http://localhost:3000/api/v1/transactions `
  -H "Content-Type: application/json" `
  -d '{
    "user_id": "user_123",
    "amount": 1500.50,
    "currency": "RUB",
    "merchant_id": "merchant_456"
  }'