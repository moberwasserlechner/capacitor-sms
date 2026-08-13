export interface SmsManagerPlugin {
  /**
   * Opens the platform SMS composer with the supplied recipients and message.
   */
  compose(options: SmsComposeOptions): Promise<void>;
}

export interface SmsComposeOptions {
  numbers: string[];
  text: string;
}
