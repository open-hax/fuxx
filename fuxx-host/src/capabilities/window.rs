use anyhow::Result;
use serde_json::{json, Value};
use uuid::Uuid;

use crate::ipc::CapabilityResult;

// NOTE: Multi-window management requires passing an EventLoopProxy or
// a channel from main.rs. This stub returns a window-id for now.
// Wire up real window spawning in the next pass.

pub async fn open(args: Value) -> Result<CapabilityResult> {
    let route = args["route"]
        .as_str()
        .ok_or_else(|| anyhow::anyhow!("window/open: missing :route"))?;

    let window_id = Uuid::new_v4().to_string();

    // TODO: send OpenWindow event through EventLoopProxy
    eprintln!("[fuxx-host] window/open: route={} id={}", route, window_id);

    Ok(CapabilityResult::ok(json!({ "window-id": window_id, "route": route }), None))
}
