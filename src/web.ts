import { WebPlugin } from '@capacitor/core';

import type { NfcHcePlugin } from './definitions';

export class NfcHceWeb extends WebPlugin implements NfcHcePlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
