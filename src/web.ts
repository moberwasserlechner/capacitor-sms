import {WebPlugin} from '@capacitor/core';
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

const SmsManager = new SmsManagerPluginWeb();

export { SmsManager };

// this does not work for angular. You need to register the plugin in app.component.ts again.
import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(SmsManager);
