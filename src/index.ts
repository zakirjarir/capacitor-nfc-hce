import { registerPlugin } from '@capacitor/core';

import type { NfcHcePlugin } from './definitions';

const NfcHce = registerPlugin<NfcHcePlugin>('NfcHce', {
  web: () => import('./web').then((m) => new m.NfcHceWeb()),
});

export * from './definitions';
export { NfcHce };
