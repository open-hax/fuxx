#[cfg(test)]
mod tests {
    use fuxx_host_lib::ipc::{handle, CapabilityCall, CapabilityResult};
    use serde_json::json;

    fn make_call(capability: &str, args: serde_json::Value) -> String {
        json!({
            "capability": capability,
            "args": args
        })
        .to_string()
    }

    #[tokio::test]
    async fn unknown_capability_returns_error() {
        let raw = make_call(":unicorn/fly", json!({ "speed": "ludicrous" }));
        let result = handle(&raw).await.unwrap();
        let parsed: serde_json::Value = serde_json::from_str(&result).unwrap();
        assert_eq!(parsed["ok"], false);
        assert_eq!(parsed["error"]["code"], "capability/not-found");
    }

    #[tokio::test]
    async fn malformed_json_returns_error() {
        let result = handle("not json at all").await;
        assert!(result.is_err());
    }

    #[tokio::test]
    async fn fs_read_missing_path_returns_error() {
        let raw = make_call(":fs/read", json!({}));
        let result = handle(&raw).await.unwrap();
        let parsed: serde_json::Value = serde_json::from_str(&result).unwrap();
        // Missing :path → handler returns ok:false
        assert_eq!(parsed["ok"], false);
    }

    #[tokio::test]
    async fn fs_write_missing_args_returns_error() {
        let raw = make_call(":fs/write", json!({}));
        let result = handle(&raw).await.unwrap();
        let parsed: serde_json::Value = serde_json::from_str(&result).unwrap();
        assert_eq!(parsed["ok"], false);
    }
}
