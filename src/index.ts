import { registerPlugin } from '@capacitor/core';

import type { SmsManagerPlugin } from './definitions';

const SmsManager = registerPlugin<SmsManagerPlugin>('SmsManager', {
  web: () => import('./web').then(({ SmsManagerPluginWeb }) => new SmsManagerPluginWeb()),
});

export * from './definitions';
export { SmsManager };
