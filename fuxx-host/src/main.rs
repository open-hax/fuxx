use anyhow::Result;
use fuxx_host_lib::ipc;
use tao::{
    event::{Event, WindowEvent},
    event_loop::{ControlFlow, EventLoop},
    window::WindowBuilder,
};
use wry::WebViewBuilder;

fn main() -> Result<()> {
    let event_loop = EventLoop::new();

    let window = WindowBuilder::new()
        .with_title("fuxx")
        .with_inner_size(tao::dpi::LogicalSize::new(1280.0, 800.0))
        .build(&event_loop)?;

    let index_url = {
        let mut p = std::env::current_dir()?;
        p.push("public/index.html");
        format!("file://{}", p.display())
    };

    let webview = WebViewBuilder::new()
        .with_url(&index_url)
        .with_ipc_handler(|request| {
            let body = request.body().to_string();
            tokio::runtime::Builder::new_current_thread()
                .enable_all()
                .build()
                .unwrap()
                .block_on(async move {
                    match ipc::handle(&body).await {
                        Ok(result) => result,
                        Err(e) => format!(
                            r#"{{"ok":false,"error":{{"code":"ipc/error","message":"{}"}}}}""",
                            e
                        ),
                    }
                })
        })
        .with_devtools(cfg!(debug_assertions))
        .build(&window)?;

    event_loop.run(move |event, _, control_flow| {
        *control_flow = ControlFlow::Wait;
        match event {
            Event::WindowEvent {
                event: WindowEvent::CloseRequested,
                ..
            } => *control_flow = ControlFlow::Exit,
            _ => {
                let _ = webview.window();
            }
        }
    });
}
