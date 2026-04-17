# fuxx specs

## scope

fuxx is the CLJS-first, opinionated application shell for a future Gecko/SpiderMonkey desktop host.
The public authoring model is data-first and contract-first; renderer and native host internals sit behind capabilities.

## design rules

- `.cljc` for authoring forms, registries, and schemas.
- `uxx` Helix layer is the default renderer contract.
- Cross-boundary traffic uses explicit envelopes.
- Every capability has an args schema and a result schema.
- Plugin execution is explicit, grant-based, and shaped for SCI.

## contract families

| File | Purpose |
|------|---------|
| `contracts/envelopes.edn` | Runtime message envelopes, dispatch payloads, errors |
| `contracts/capabilities.edn` | Host capability request/result shapes + 5 built-in specs |
| `contracts/plugins.edn` | Plugin manifests and policy grants |

## built-in capabilities

| Key | Purpose |
|-----|---------|
| `:fs/read` | Read file from host filesystem |
| `:fs/write` | Write file to host filesystem |
| `:http/request` | Outbound HTTP via host adapter |
| `:window/open` | Open a new shell window or panel |
| `:plugin/load` | Load a SCI plugin with capability grant |

## next files

- `app.cljc` — `defapp`, `defview`, `defcommand`, `defeffect` registries
- `runtime/dispatch.cljs` — normalized message handling
- `adapters/uxx.cljs` — Helix rendering boundary (real uxx wiring)
- `runtime/plugins.cljs` — SCI-backed plugin loading
