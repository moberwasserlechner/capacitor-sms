<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">SMS</h3>
<p align="center"><strong><code>@byteowls/capacitor-sms</code></strong></p>
<p align="center">
    Capacitor SMS plugin
</p>

<p align="center">
    <img src="https://img.shields.io/maintenance/yes/2024?style=flat-square" />
    <a href="LICENSE"><img src="https://img.shields.io/npm/l/@byteowls/capacitor-sms?style=flat-square" /></a>
<br>
    <a href="https://www.npmjs.com/package/@byteowls/capacitor-sms"><img src="https://img.shields.io/npm/dw/@byteowls/capacitor-sms?style=flat-square" /></a>
    <a href="https://www.npmjs.com/package/@byteowls/capacitor-sms"><img src="https://img.shields.io/npm/v/@byteowls/capacitor-sms?style=flat-square" /></a>
</p>

# Capacitor SMS plugin


Plugin for sending short messages using the device's SMS app.

## Installation

```bash
npm i @byteowls/capacitor-sms
npx cap sync
```

## Versions

| Plugin | For Capacitor | Docs                                                                               | Notes                                                         |
|--------|---------------|------------------------------------------------------------------------------------|---------------------------------------------------------------|
| 6.x    | 6.x.x         | [README](./README.md)                                                              | Breaking changes see Changelog.                               |
| 5.x    | 5.x.x         | [README](https://github.com/moberwasserlechner/capacitor-sms/blob/5.0.0/README.md) | Breaking changes see Changelog.                               |
| 4.x    | 4.x.x         | [README](https://github.com/moberwasserlechner/capacitor-sms/blob/4.0.0/README.md) | Breaking changes see Changelog.                               |


## Configuration

```typescript
import {Component, OnInit} from '@angular/core';
import {SmsManager} from "@byteowls/capacitor-sms";


@Component({
    template: "<button mat-raised-button color='primary' (click)='sendSms()'>Send SMS now!</button>"
})
export class SmsExampleComponent implements OnInit {

    async ngOnInit() {
        const info: DeviceInfo = await Device.getInfo();
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

* SEND_CANCELLED ... User cancelled or closed the SMS app.
* ERR_SEND_FAILED ... The SMS app returned that sending the message to the recipients failed. (ios only)
* ERR_SEND_UNKNOWN_STATE ... The SMS app returned a unknown state. There is nothing I can do to clarify the error. (ios only)
* UNIMPLEMENTED ... Sending SMS on the web is not supported.
* ERR_NO_NUMBERS ... No recipient numbers were retrieved from options. Make sure to deliver only valid numbers, because the whole sending will fail.
* ERR_NO_TEXT ... No message text was retrieved from options.
* ERR_SERVICE_NOTFOUND ... The used device can not send SMS.

## Contribute
See [Contribution Guidelines](./.github/CONTRIBUTING.md).

## Changelog
See [CHANGELOG](./CHANGELOG.md).

## License
MIT. Please see [LICENSE](./LICENSE).

## Disclaimer

We have no business relation to Ionic.
