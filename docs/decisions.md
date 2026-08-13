# Decisions

Architectural decisions for `@byteowls/capacitor-sms`.

## Production compatibility

The documented public API, plugin registration name, and error codes remain compatible within a major version. Version 8 renames `SmsManager.send()` to `SmsManager.compose()` because the plugin opens the platform SMS composer rather than sending a message itself.

## Capacitor 8 is the only supported major in version 8

Version 8 targets Capacitor 8 and its platform floors. Older plugin majors remain available for older Capacitor applications.

## pnpm is the development package manager

The repository uses pnpm with a committed frozen lockfile. npm is used only for registry tarball verification.

## iOS is distributed through Swift Package Manager only

Capacitor 8 is the migration boundary for removing CocoaPods. The package ships a production-only `Package.swift` and sources under `ios/Sources`; the podspec, Podfile, CocoaPods Xcode project, and tests are not published. XCTest uses a separate repository-only package under `ios/Tests`.

## Android is implemented in Kotlin

The Capacitor bridge is thin. Input parsing and recipient formatting are plain Kotlin with unit tests. Samsung's comma separator remains supported; other manufacturers use a semicolon. Verification builds Android both standalone and from the packed npm artifact as a subproject of a Capacitor-style host, covering the publish allowlist, plugin classpath, and property integration.

## Web remains unsupported

A browser cannot provide reliable sent/cancelled semantics. The plugin therefore rejects with Capacitor's `UNIMPLEMENTED` code instead of using `sms:` links or a gateway.

## Published JavaScript and declarations are bundled

Rollup creates ESM, CommonJS, IIFE, and bundled declaration artifacts. Capacitor remains external because the consuming application supplies the peer dependency.
