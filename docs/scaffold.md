# fuxx scaffold

Initial `#μ` scaffold: CLJS-first runtime loop with contract-first boundaries.

## namespace map

| Namespace | Kind | Purpose |
|-----------|------|---------|
| `fuxx.app` | `.cljc` | Authoring macros: defapp/view/route/command/query/effect/capability/plugin |
| `fuxx.schema` | `.cljc` | Malli-backed contract registry and validation helpers |
| `fuxx.contracts` | `.cljc` | EDN contract bootstrap |
| `fuxx.core` | `.cljs` | boot! entrypoint |
| `fuxx.runtime.state` | `.cljs` | Runtime atom |
| `fuxx.runtime.dispatch` | `.cljs` | Envelope dispatcher (defmulti on :kind) |
| `fuxx.runtime.router` | `.cljs` | Route mutation |
| `fuxx.runtime.capabilities` | `.cljs` | Capability registry and invocation |
| `fuxx.runtime.render` | `.cljs` | Renderer frame handoff |
| `fuxx.runtime.plugins` | `.cljs` | SCI plugin manifest loading stub |
| `fuxx.adapters.uxx` | `.cljs` | Opinionated uxx Helix adapter boundary |
| `fuxx.ui.root` | `.cljs` | Skeleton root view |

## next pass

- Wire real `uxx` imports into the adapter.
- Add route/view registries generated from macros.
- Add command/query/effect execution semantics.
- Add SCI namespace whitelist and capability grants.
- Add `deps.edn`, `package.json`, `shadow-cljs.edn` so the scaffold boots.
- Dogfood a tiny Knoxx/OpenPlanner shell.
