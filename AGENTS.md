# AGENTS.md

Instructions for coding agents working on this project.

## Rules

1. This is a released production plugin. Preserve documented behaviour and public API unless a breaking change is explicitly approved for a major release.
2. Before planning or implementing architectural changes, read `docs/decisions.md`.
3. GitHub issues are the source of truth for planned work. Do not create a local task backlog.
4. Use pnpm for dependency installation and package scripts. Commit `pnpm-lock.yaml`; do not add an npm lockfile.
5. Dependencies use exact versions. Only peer dependencies may use a compatibility range.
6. TypeScript stays in strict mode. Avoid `any`; use `unknown` at untrusted boundaries.
7. Keep `src/index.ts` and `src/definitions.ts` as the public entry point and API. Tests live under `test/`.
8. Every user-facing change gets an entry under `CHANGELOG.md`'s Unreleased section.
9. Run relevant web, package, Android, and iOS checks before proposing a change for review.
10. Packaging changes require `pnpm verify:package`.
11. Ask the user for review and propose a sensible commit before committing.
12. Never add AI attribution, generated-by footers, or AI `Co-Authored-By` trailers.
13. Prefer explicit `if` blocks over ternary expressions or dense one-line conditions when they improve readability.

## Stack

- TypeScript in strict mode
- pnpm
- Capacitor 8
- Web (best-effort `sms:` URL composition), Android (Kotlin), and iOS (Swift)
- iOS packaging through Swift Package Manager only
- Vitest, JUnit, and XCTest

## Commands

```bash
pnpm install
pnpm lint
pnpm test
pnpm build
pnpm verify:package
pnpm verify:android
pnpm verify:ios
pnpm verify
```
