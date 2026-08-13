<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">SMS</h3>
<p align="center"><strong><code>@byteowls/capacitor-sms</code></strong></p>
<p align="center">Capacitor plugin for composing SMS messages</p>

<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2026?style=flat-square" alt="Maintained in 2026" />
  <a href="https://github.com/moberwasserlechner/capacitor-sms/actions?query=workflow%3ACI"><img src="https://img.shields.io/github/actions/workflow/status/moberwasserlechner/capacitor-sms/ci.yml?style=flat-square" alt="CI status" /></a>
  <a href="https://github.com/moberwasserlechner/capacitor-sms/actions/workflows/ios.yml"><img src="https://img.shields.io/github/actions/workflow/status/moberwasserlechner/capacitor-sms/ios.yml?style=flat-square&label=iOS" alt="iOS build status" /></a>
  <a href="https://www.npmjs.com/package/@byteowls/capacitor-sms"><img src="https://img.shields.io/npm/l/@byteowls/capacitor-sms?style=flat-square" alt="npm license" /></a>
  <br>
  <a href="https://www.npmjs.com/package/@byteowls/capacitor-sms"><img src="https://img.shields.io/npm/dw/@byteowls/capacitor-sms?style=flat-square" alt="npm weekly downloads" /></a>
  <a href="https://www.npmjs.com/package/@byteowls/capacitor-sms"><img src="https://img.shields.io/npm/v/@byteowls/capacitor-sms?style=flat-square" alt="npm version" /></a>
</p>

# Capacitor SMS plugin

Opens the device's native SMS composer with recipients and message text. The user remains responsible for sending the message.

## Direct sending is not supported

This plugin only opens the platform SMS composer. The user must review the message and tap **Send**; the plugin cannot send an SMS automatically.

- **iOS:** Apple provides no public API for third-party apps to send SMS messages without user interaction.
- **Android:** Direct sending requires sensitive SMS permissions. Google Play restricts these permissions primarily to default SMS handlers and narrowly approved use cases; sideloaded apps are outside the normal Play distribution model.

The plugin will **not** provide Android-only direct sending.

## Installation

### pnpm (recommended)

```bash
pnpm add @byteowls/capacitor-sms
pnpm exec cap sync
```

### npm

```bash
npm install @byteowls/capacitor-sms
npx cap sync
```

Version 8 supports Capacitor 8 and uses Swift Package Manager for iOS. Applications upgrading from plugin 7 must migrate their Capacitor iOS project from CocoaPods to Swift Package Manager before installing plugin 8.

## Versions

| Plugin | Capacitor | iOS packaging |
| --- | --- | --- |
| 8.x | 8.x | Swift Package Manager |
| 7.x | 7.x | CocoaPods |
| 6.x | 6.x | CocoaPods |

## Usage

```typescript
import { SmsManager } from '@byteowls/capacitor-sms';

await SmsManager.compose({
  numbers: ['+43 123 123123123', '+43 4564 56456456'],
  text: 'Hello from Capacitor',
});
```

### Web behavior

On web, `compose()` makes a best-effort attempt to open the device's SMS handler using an `sms:` URL. The promise resolves when navigation is initiated, not when the message is sent. Browser and operating-system support varies, especially for multiple recipients and desktop protocol handlers; sent and cancelled states cannot be reported.

## Error codes

- `SEND_CANCELLED` — the user cancelled or closed the SMS composer.
- `ERR_SEND_FAILED` — iOS reported that sending failed.
- `ERR_SEND_UNKNOWN_STATE` — iOS returned an unknown result.
- `ERR_NO_NUMBERS` — no valid recipient numbers were supplied.
- `ERR_NO_TEXT` — no message text was supplied.
- `ERR_SERVICE_NOTFOUND` — the device cannot compose SMS messages.

## Contributing

See [Contribution Guidelines](./.github/CONTRIBUTING.md).

## License

[MIT](./LICENSE). This project has no business relationship with Ionic.
