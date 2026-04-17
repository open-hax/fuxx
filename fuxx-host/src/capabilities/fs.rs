use anyhow::Result;
use serde_json::{json, Value};
use tokio::fs as tfs;

use crate::ipc::CapabilityResult;

pub async fn read(args: Value) -> Result<CapabilityResult> {
    let path = args["path"]
        .as_str()
        .ok_or_else(|| anyhow::anyhow!("fs/read: missing :path"))?;

    match tfs::read_to_string(path).await {
        Ok(text) => Ok(CapabilityResult::ok(json!({ "text": text }), None)),
        Err(e)   => Ok(CapabilityResult::err("fs/read-error", &e.to_string(), None)),
    }
}

pub async fn write(args: Value) -> Result<CapabilityResult> {
    let path = args["path"]
        .as_str()
        .ok_or_else(|| anyhow::anyhow!("fs/write: missing :path"))?;
    let text = args["text"]
        .as_str()
        .ok_or_else(|| anyhow::anyhow!("fs/write: missing :text"))?;

    if let Some(true) = args["mkdir?"].as_bool() {
        if let Some(parent) = std::path::Path::new(path).parent() {
            tfs::create_dir_all(parent).await?;
        }
    }

    match tfs::write(path, text).await {
        Ok(_)  => Ok(CapabilityResult::ok(json!({ "bytes-written": text.len() }), None)),
        Err(e) => Ok(CapabilityResult::err("fs/write-error", &e.to_string(), None)),
    }
}
