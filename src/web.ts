import { WebPlugin } from '@capacitor/core';
import {SmsManagerPlugin, SmsSendOptions} from "./definitions";

export class SmsManagerPluginWeb extends WebPlugin implements SmsManagerPlugin {

    async send(): Promise<void> {
        throw this.unimplemented('Not implemented on web.');
    }

}
