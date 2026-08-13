import { WebPlugin } from '@capacitor/core';

import type { SmsComposeOptions, SmsManagerPlugin } from './definitions';

export class SmsManagerPluginWeb extends WebPlugin implements SmsManagerPlugin {
  async compose(_options: SmsComposeOptions): Promise<void> {
    throw this.unimplemented('Composing SMS on the web is not supported.');
  }
}
