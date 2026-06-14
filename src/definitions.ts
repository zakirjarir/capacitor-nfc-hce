export interface NfcHcePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
