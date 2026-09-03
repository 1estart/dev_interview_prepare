use axum::{
    Router,
    routing::{get, post},
};

use super::handlers;

pub fn build_router() -> Router {
    Router::new()
        // Health check
        .route("/health", get(handlers::health_check))
        // Транзакции
        .route("/api/v1/transactions", post(handlers::create_transaction))
}
