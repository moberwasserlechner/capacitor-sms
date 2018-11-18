import {WebPlugin} from '@capacitor/core';
import {SmsManagerPlugin, SmsSendOptions} from "./definitions";

export class SmsManagerPluginWeb extends WebPlugin implements SmsManagerPlugin {

    constructor() {
        super({
            name: 'SmsManager',
            platforms: ['web']
        });
    }

    async send(options: SmsSendOptions): Promise<void> {
        return new Promise<void>((resolve, reject) => {
            reject("PLATFORM_NOT_SUPPORTED");
        });
    }

}

const SmsManager = new SmsManagerPluginWeb();

export { SmsManager };
