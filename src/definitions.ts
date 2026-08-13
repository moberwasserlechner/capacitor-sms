export interface SmsManagerPlugin {
  /**
   * Opens the platform SMS composer with the supplied recipients and message.
   */
  send(options: SmsSendOptions): Promise<void>;
}

export interface SmsSendOptions {
  numbers: string[];
  text: string;
}
