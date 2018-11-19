import {WebPlugin} from '@capacitor/core';
import {SmsManagerPlugin, SmsPermissionOptions, SmsSendOptions} from "./definitions";

export class SmsManagerPluginWeb extends WebPlugin implements SmsManagerPlugin {

    NOT_SUPPORTED: string = "PLATFORM_NOT_SUPPORTED";

    constructor() {
        super({
            name: 'SmsManager',
            platforms: ['web']
        });
    }

    async send(options: SmsSendOptions): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            reject(this.NOT_SUPPORTED);
        });
    }

    async hasPermission(options: SmsPermissionOptions): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            reject(this.NOT_SUPPORTED);
        });
    }

}

const SmsManager = new SmsManagerPluginWeb();

export { SmsManager };
