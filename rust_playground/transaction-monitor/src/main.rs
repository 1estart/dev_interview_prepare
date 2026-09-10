mod api;
mod domain;

use tracing_subscriber::EnvFilter;

#[tokio::main]
async fn main() {
    // Инициализация логирования
    tracing_subscriber::fmt()
        .with_env_filter(
            EnvFilter::try_from_default_env().unwrap_or_else(|_| EnvFilter::new("info")),
        )
        .init();

    // Строим роутер
    let app = api::routes::build_router();

    // Слушаем на порту 3000
    let listener = tokio::net::TcpListener::bind("0.0.0.0:3000")
        .await
        .expect("Failed to bind to port 3000");

    tracing::info!("🚀 Transaction Monitor started on http://0.0.0.0:3000");

    axum::serve(listener, app).await.expect("Server failed");
}
