# fuxx

Firefox/uxx — opinionated ClojureScript shell runtime.

CLJS-first desktop app framework backed by Gecko, rendering via the [uxx](https://github.com/open-hax/uxx) Helix layer.

## what it is

- **Data-first** — apps, views, routes, commands, queries, effects, and capabilities are all plain EDN-shaped data.
- **CLJS-first** — the authoring surface is `.cljc` macros; the runtime is ClojureScript.
- **Opinionated renderer** — uxx Helix is the one renderer. No bring-your-own-renderer nonsense on day one.
- **Capability membrane** — host/native integration happens through named, schema-validated capability calls. Rust/C++/Gecko internals live behind that line.
- **Contract-first** — every cross-boundary message uses a Malli-validated envelope.

## structure

```
contracts/          — EDN capability/envelope/plugin contracts
docs/               — specs and architecture notes
src/fuxx/
  app.cljc          — defapp, defview, defroute, defcommand, defquery, defeffect, defcapability, defplugin
  schema.cljc       — malli registry and validation helpers
  contracts.cljc    — EDN contract bootstrap
  core.cljs         — boot! entrypoint
  runtime/          — state, dispatch, router, capabilities, render, plugins
  adapters/uxx.cljs — uxx Helix render boundary
  ui/root.cljs      — skeleton root view
```

## license

GPLv3+
