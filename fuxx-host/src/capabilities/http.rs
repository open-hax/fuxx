use anyhow::Result;
use serde_json::{json, Value};

use crate::ipc::CapabilityResult;

pub async fn request(args: Value) -> Result<CapabilityResult> {
    let url = args["url"]
        .as_str()
        .ok_or_else(|| anyhow::anyhow!("http/request: missing :url"))?;

    let method = args["method"]
        .as_str()
        .unwrap_or("get")
        .to_uppercase();

    let client = reqwest::Client::new();

    let mut req = match method.as_str() {
        "GET"    => client.get(url),
        "POST"   => client.post(url),
        "PUT"    => client.put(url),
        "PATCH"  => client.patch(url),
        "DELETE" => client.delete(url),
        m        => return Ok(CapabilityResult::err(
            "http/unknown-method",
            &format!("Unknown method: {}", m),
            None,
        )),
    };

    if let Some(headers) = args["headers"].as_object() {
        for (k, v) in headers {
            if let Some(v) = v.as_str() {
                req = req.header(k.as_str(), v);
            }
        }
    }

    if !args["body"].is_null() {
        req = req.json(&args["body"]);
    }

    match req.send().await {
        Ok(resp) => {
            let status = resp.status().as_u16();
            let body: Value = resp.json().await.unwrap_or(Value::Null);
            Ok(CapabilityResult::ok(json!({ "status": status, "body": body }), None))
        }
        Err(e) => Ok(CapabilityResult::err("http/request-error", &e.to_string(), None)),
    }
}
