use anyhow::Result;
use serde_json::{json, Value};

use crate::ipc::CapabilityResult;

// SCI plugin loading stub.
// TODO: spawn a SCI process or thread, apply capability whitelist,
// evaluate plugin source, register exported commands/views.

pub async fn load(args: Value) -> Result<CapabilityResult> {
    let plugin_id = args["plugin/id"]
        .as_str()
        .ok_or_else(|| anyhow::anyhow!("plugin/load: missing :plugin/id"))?;

    eprintln!("[fuxx-host] plugin/load: {} (stub — SCI eval not yet wired)", plugin_id);

    Ok(CapabilityResult::ok(
        json!({ "plugin/id": plugin_id, "status": "stub" }),
        None,
    ))
}
