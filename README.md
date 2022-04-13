# Capacitor SMS plugin

<a href="#sponsors"><img src="https://img.shields.io/badge/plugin-Sponsors-blue?style=flat-square" /></a>
<a href="https://www.npmjs.com/package/@byteowls/capacitor-sms"><img src="https://img.shields.io/npm/dw/@byteowls/capacitor-sms?style=flat-square" /></a>
<a href="https://www.npmjs.com/package/@byteowls/capacitor-sms"><img src="https://img.shields.io/npm/v/@byteowls/capacitor-sms?style=flat-square" /></a>
<a href="LICENSE"><img src="https://img.shields.io/npm/l/@byteowls/capacitor-sms?style=flat-square" /></a>

Plugin for sending short messages using the device's SMS app.

## Installation

For Capacitor v3
```bash
npm i @byteowls/capacitor-sms
npx cap sync
```

For Capacitor v2 use `2.0.0`
```bash
npm i @byteowls/capacitor-sms@2.0.0
npx cap sync
```

## Versions

| Plugin | For Capacitor         | Docs                                                                               | Notes                          |
|--------|-------------------|------------------------------------------------------------------------------------|--------------------------------|
| 3.x    | 3.x.x             | [README](./README.md)                                                              | Breaking changes see Changelog. XCode 12.0 needs this version  |
| 2.x    | 2.x.x             | [README](https://github.com/moberwasserlechner/capacitor-sms/blob/2.0.0/README.md) | Breaking changes see Changelog. XCode 11.4 needs this version  |
| 1.x    | 1.x.x             | [README](https://github.com/moberwasserlechner/capacitor-sms/blob/1.0.0/README.md) |                                |

## Sponsors

I would like to especially thank some people and companies for supporting my work on this plugin and therefore improving it for everybody.

*

## Maintainers

| Maintainer | GitHub | Consulting                                   |
| -----------| -------|----------------------------------------------|
| Michael Oberwasserlechner | [moberwasserlechner](https://github.com/moberwasserlechner) | [https://byteowls.com](https://byteowls.com) |


## Configuration

Starting with version `3.0.0`, the plugin is registered automatically on all platforms.

This plugin always uses the default sms app.

### Use it

```typescript
import {Component, OnInit} from '@angular/core';
import {SmsManager} from "@byteowls/capacitor-sms";
import {Device, DeviceInfo} from "@capacitor/device";


@Component({
    template: "<button mat-raised-button color='primary' (click)='sendSms()'>Send SMS now!</button>"
})
export class SmsExampleComponent implements OnInit {

    iosOrAndroid: boolean;

    async ngOnInit() {
        const info: DeviceInfo = await Device.getInfo();
        this.iosOrAndroid = (info.platform === "android" || info.platform === "ios");
    }

    sendSms() {
        const numbers: string[] = ["+43 123 123123123", "+43 4564 56456456"];
        SmsManager.send({
            numbers: numbers,
            text: "This is a example SMS",
        }).then(() => {
            // success
        }).catch(error => {
            console.error(error);
        });
    }
}
```

### Error Codes

* SEND_CANCELLED ... User cancelled or closed the SMS app. (ios only)
* ERR_SEND_FAILED ... The SMS app returned that sending the message to the recipients failed. (ios only)
* ERR_SEND_UNKNOWN_STATE ... The SMS app returned a unknown state. There is nothing I can do to clarify the error. (ios only)
* ERR_PLATFORM_NOT_SUPPORTED ... Sending SMS on the web is not supported.
* ERR_NO_NUMBERS ... No recipient numbers were retrieved from options. Make sure to deliver only valid numbers, because the whole sending will fail.
* ERR_NO_TEXT ... No message text was retrieved from options.
* ERR_SERVICE_NOTFOUND ... The used device can not send SMS.

## Platform: Android

Prerequisite: [Capacitor Android Docs](https://capacitor.ionicframework.com/docs/android/configuration)

### Register plugin
On Android the plugin is registered **automatically** by Capacitor.

## Platform: iOS

### Register plugin
On iOS the plugin is registered **automatically** by Capacitor.

## Platform: Web/PWA

- Not supported.

## Platform: Electron

- Not supported.

## Contribute
See [Contribution Guidelines](./.github/CONTRIBUTING.md).

## Changelog
See [CHANGELOG](./CHANGELOG.md).

## License
MIT. Please see [LICENSE](./LICENSE).

## BYTEOWLS Software & Consulting

This plugin is powered by [BYTEOWLS Software & Consulting](https://byteowls.com).

If you need extended support for this project like critical changes or releases ahead of schedule. Feel free to contact us for a consulting offer.

## Disclaimer

We have no business relation to Ionic.
