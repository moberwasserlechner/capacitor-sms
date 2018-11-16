import {WebPlugin} from '@capacitor/core';
import {SmsSendOptions, SmsManagerPlugin} from "./definitions";

export class SmsManagerPluginWeb extends WebPlugin implements SmsManagerPlugin {

    constructor() {
        super({
            name: 'SmsManager',
            platforms: ['web']
        });
    }

    async send(options: SmsSendOptions): Promise<any> {
        return new Promise<any>((resolve, reject) => {
            resolve();
        });
    }

}

const SmsManager = new SmsManagerPluginWeb();

export { SmsManager };
