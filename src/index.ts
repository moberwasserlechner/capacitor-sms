import { registerPlugin } from '@capacitor/core';
import { SmsManagerPlugin } from './definitions';

const SmsManager = registerPlugin<SmsManagerPlugin>('SmsManager', {
    web: () => import('./web').then(m => new m.SmsManagerPluginWeb()),
});

export * from './definitions';
export { SmsManager };