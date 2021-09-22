import { WebPlugin } from '@capacitor/core';
import {SmsManagerPlugin, SmsSendOptions} from "./definitions";

export class SmsManagerPluginWeb extends WebPlugin implements SmsManagerPlugin {

    NOT_SUPPORTED: string = "ERR_PLATFORM_NOT_SUPPORTED";

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

}
