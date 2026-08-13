import { WebPlugin } from '@capacitor/core';

import type { SmsManagerPlugin, SmsSendOptions } from './definitions';

export class SmsManagerPluginWeb extends WebPlugin implements SmsManagerPlugin {
  async send(_options: SmsSendOptions): Promise<void> {
    throw this.unimplemented('Sending SMS on the web is not supported.');
  }
}
