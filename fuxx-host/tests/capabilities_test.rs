#[cfg(test)]
mod tests {
    use serde_json::json;
    use fuxx_host_lib::capabilities::{fs, http};

    #[tokio::test]
    async fn fs_write_and_read_round_trip() {
        let path = "/tmp/fuxx_test_rw.txt";
        let text = "fuxx integration test";

        let write_result = fs::write(json!({ "path": path, "text": text })).await;
        assert!(write_result.is_ok());
        let wr = write_result.unwrap();
        assert!(wr.ok);

        let read_result = fs::read(json!({ "path": path })).await;
        assert!(read_result.is_ok());
        let rr = read_result.unwrap();
        assert!(rr.ok);
        let val = rr.value.unwrap();
        assert_eq!(val["text"], text);
    }

    #[tokio::test]
    async fn fs_read_nonexistent_file() {
        let result = fs::read(json!({ "path": "/tmp/fuxx_does_not_exist_xyz.txt" })).await;
        // Handler returns ok:false, not an Err
        assert!(result.is_ok());
        assert!(!result.unwrap().ok);
    }

    // HTTP test is gated behind a feature flag to avoid hitting network in CI
    #[cfg(feature = "network-tests")]
    #[tokio::test]
    async fn http_get_returns_ok() {
        let result = http::request(json!({
            "url": "https://httpbin.org/get",
            "method": "get"
        }))
        .await;
        assert!(result.is_ok());
        assert!(result.unwrap().ok);
    }
}