import { registerPlugin } from '@capacitor/core';
import { SmsManagerPluginWeb } from './web';

const SmsManager = registerPlugin<SmsManagerPluginWeb>('SmsManager', {
    web: () => import('./web').then(m => new m.SmsManagerPluginWeb()),
});



export { SmsManager };
export * from './definitions';
