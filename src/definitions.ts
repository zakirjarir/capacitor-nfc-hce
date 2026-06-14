import type { PluginListenerHandle } from '@capacitor/core';

export interface NfcHcePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  startHce(): Promise<void>;
  stopHce(): Promise<void>;
  sendResponse(options: { response: string }): Promise<void>;
  setResponseCache(options: { cache: Record<string, string> }): Promise<void>;
  clearResponseCache(): Promise<void>;
  addListener(
    eventName: 'onApduCommand',
    listenerFunc: (data: { command: string }) => void,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
  addListener(
    eventName: 'onHceDeactivated',
    listenerFunc: (data: { reason: number }) => void,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
}
