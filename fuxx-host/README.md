# fuxx-host

Native host shell for `fuxx`. Spawns a WebKitGTK (Linux) / WKWebView (macOS) window,
loads the compiled CLJS shell, and handles capability calls over an IPC channel.

## architecture

```
CLJS runtime
  └─ window.__fuxx_ipc(json)   ────────────────────── IPC channel
Rust host
  └─ ipc::handle(raw)          deserialize CapabilityCall
  └─ capabilities::*           fs / http / window / plugin
  └─ CapabilityResult          serialize + return to JS
```

## built-in capabilities

| Key | Handler | Notes |
|-----|---------|-------|
| `:fs/read` | `capabilities::fs::read` | tokio async file read |
| `:fs/write` | `capabilities::fs::write` | tokio async file write, optional mkdir |
| `:http/request` | `capabilities::http::request` | reqwest async, all methods |
| `:window/open` | `capabilities::window::open` | stub — EventLoopProxy wiring TODO |
| `:plugin/load` | `capabilities::plugin::load` | stub — SCI eval TODO |

## build

```bash
cd fuxx-host
cargo build
cargo run
```

## renderer swap path

WRY abstracts the webview. To swap WebKitGTK for Servo:
1. Replace the `wry` dep with the Servo embedding crate.
2. Implement the same `with_ipc_handler` surface.
3. CLJS runtime never changes — it only sees capability envelopes.

## linux deps (Ubuntu/Debian)

```bash
sudo apt install libwebkit2gtk-4.1-dev libgtk-3-dev libayatana-appindicator3-dev librsvg2-dev
```
