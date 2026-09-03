use axum::{extract::Json, http::StatusCode, response::IntoResponse};
use chrono::Utc;
use uuid::Uuid;

use crate::domain::transaction::{TransactionRequest, TransactionResponse, TransactionStatus};

pub async fn create_transaction(Json(payload): Json<TransactionRequest>) -> impl IntoResponse {
    tracing::info!(
        user_id = %payload.user_id,
        amount = %payload.amount,
        currency = %payload.currency,
        "Received transaction"
    );

    let response = TransactionResponse {
        id: Uuid::new_v4(),
        user_id: payload.user_id,
        amount: payload.amount,
        currency: payload.currency,
        merchant_id: payload.merchant_id,
        status: TransactionStatus::Approved,
        created_at: Utc::now(),
    };

    (StatusCode::CREATED, Json(response))
}

pub async fn health_check() -> impl IntoResponse {
    Json(serde_json::json!({
        "status": "ok",
        "service": "transaction-monitor",
        "version": env!("CARGO_PKG_VERSION"),
    }))
}
