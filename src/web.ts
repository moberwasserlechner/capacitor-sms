import {WebPlugin} from '@capacitor/core';
import {SmsOptions, SmsPlugin} from "./definitions";

export class SmsPluginWeb extends WebPlugin implements SmsPlugin {

    constructor() {
        super({
            name: 'Sms',
            platforms: ['web']
        });
    }

    async send(options: SmsOptions): Promise<any> {
        return new Promise<any>((resolve, reject) => {
            resolve();
        });
    }

}

const Sms = new SmsPluginWeb();

export { Sms };
