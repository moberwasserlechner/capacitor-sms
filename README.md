<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">SMS</h3>
<p align="center"><strong><code>@byteowls/capacitor-sms</code></strong></p>
<p align="center">Capacitor plugin for composing SMS messages</p>

# Capacitor SMS plugin

Opens the device's native SMS composer with recipients and message text. The user remains responsible for sending the message.

## Installation

```bash
pnpm add @byteowls/capacitor-sms
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

await SmsManager.send({
  numbers: ['+43 123 123123123', '+43 4564 56456456'],
  text: 'Hello from Capacitor',
});
```

Web is intentionally unsupported and rejects with Capacitor's `UNIMPLEMENTED` code.

## Error codes

- `SEND_CANCELLED` — the user cancelled or closed the SMS composer.
- `ERR_SEND_FAILED` — iOS reported that sending failed.
- `ERR_SEND_UNKNOWN_STATE` — iOS returned an unknown result.
- `UNIMPLEMENTED` — SMS composition is unsupported on web.
- `ERR_NO_NUMBERS` — no valid recipient numbers were supplied.
- `ERR_NO_TEXT` — no message text was supplied.
- `ERR_SERVICE_NOTFOUND` — the device cannot compose SMS messages.

## Contributing

See [Contribution Guidelines](./.github/CONTRIBUTING.md).

## License

[MIT](./LICENSE). This project has no business relationship with Ionic.
