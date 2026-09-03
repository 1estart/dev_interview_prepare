use chrono::{DateTime, Utc};
use serde::{Deserialize, Serialize};
use uuid::Uuid;

#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct TransactionRequest {
    pub user_id: String,
    pub amount: f64,
    pub currency: String,
    pub merchant_id: String,
}

#[derive(Debug, Clone, Serialize)]
pub struct TransactionResponse {
    pub id: Uuid,
    pub user_id: String,
    pub amount: f64,
    pub currency: String,
    pub merchant_id: String,
    pub status: TransactionStatus,
    pub created_at: DateTime<Utc>,
}

#[derive(Debug, Clone, Serialize, PartialEq)]
pub enum TransactionStatus {
    Approved,
    Rejected,
    FraudDetected,
}
