use anyhow::{anyhow, Result};
use serde::{Deserialize, Serialize};
use serde_json::Value;

use crate::capabilities::{fs, http, plugin, window};

// ── Envelope types ────────────────────────────────────────────────────────────

#[derive(Debug, Deserialize)]
pub struct CapabilityCall {
    pub capability: String,
    pub args: Value,
    #[serde(rename = "requestId")]
    pub request_id: Option<String>,
}

#[derive(Debug, Serialize)]
pub struct CapabilityResult {
    pub ok: bool,
    #[serde(skip_serializing_if = "Option::is_none")]
    pub value: Option<Value>,
    #[serde(skip_serializing_if = "Option::is_none")]
    pub error: Option<IpcError>,
    #[serde(rename = "requestId", skip_serializing_if = "Option::is_none")]
    pub request_id: Option<String>,
}

#[derive(Debug, Serialize)]
pub struct IpcError {
    pub code: String,
    pub message: String,
}

impl CapabilityResult {
    pub fn ok(value: Value, request_id: Option<String>) -> Self {
        Self { ok: true, value: Some(value), error: None, request_id }
    }

    pub fn err(code: &str, message: &str, request_id: Option<String>) -> Self {
        Self {
            ok: false,
            value: None,
            error: Some(IpcError { code: code.into(), message: message.into() }),
            request_id,
        }
    }
}

// ── Dispatch ──────────────────────────────────────────────────────────────────

pub async fn handle(raw: &str) -> Result<String> {
    let call: CapabilityCall = serde_json::from_str(raw)
        .map_err(|e| anyhow!("ipc/parse-error: {}", e))?;

    let request_id = call.request_id.clone();

    let result = match call.capability.as_str() {
        ":fs/read"       => fs::read(call.args).await,
        ":fs/write"      => fs::write(call.args).await,
        ":http/request"  => http::request(call.args).await,
        ":window/open"   => window::open(call.args).await,
        ":plugin/load"   => plugin::load(call.args).await,
        unknown          => Ok(CapabilityResult::err(
            "capability/not-found",
            &format!("Unknown capability: {}", unknown),
            request_id.clone(),
        )),
    };

    let result = result.unwrap_or_else(|e| CapabilityResult::err(
        "capability/handler-error",
        &e.to_string(),
        request_id,
    ));

    Ok(serde_json::to_string(&result)?)
}
