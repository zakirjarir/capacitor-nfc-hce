import { WebPlugin } from '@capacitor/core';
import type { PluginListenerHandle } from '@capacitor/core';

import type { NfcHcePlugin } from './definitions';

export class NfcHceWeb extends WebPlugin implements NfcHcePlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async startHce(): Promise<void> {
    console.warn('NFC HCE is not available on the web.');
    throw this.unimplemented('NFC HCE is not available on the web.');
  }

  async stopHce(): Promise<void> {
    console.warn('NFC HCE is not available on the web.');
    throw this.unimplemented('NFC HCE is not available on the web.');
  }

  async sendResponse(options: { response: string }): Promise<void> {
    console.warn('NFC HCE is not available on the web.', options);
    throw this.unimplemented('NFC HCE is not available on the web.');
  }

  async setResponseCache(options: { cache: Record<string, string> }): Promise<void> {
    console.warn('NFC HCE is not available on the web.', options);
    throw this.unimplemented('NFC HCE is not available on the web.');
  }

  async clearResponseCache(): Promise<void> {
    console.warn('NFC HCE is not available on the web.');
    throw this.unimplemented('NFC HCE is not available on the web.');
  }

  // @ts-ignore
  async addListener(
    eventName: string,
    listenerFunc: (...args: any[]) => void,
  ): Promise<PluginListenerHandle> {
    console.warn(`NFC HCE is not available on the web. Event: ${eventName}`, listenerFunc);
    throw this.unimplemented('NFC HCE is not available on the web.');
  }
}
