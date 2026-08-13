import { WebPlugin } from '@capacitor/core';

import type { SmsComposeOptions, SmsManagerPlugin } from './definitions';

type SmsValidationErrorCode = 'ERR_NO_NUMBERS' | 'ERR_NO_TEXT';

const encodeRecipient = (number: string): string => encodeURIComponent(number).replaceAll('%2B', '+');

class SmsValidationError extends Error {
  readonly code: SmsValidationErrorCode;

  constructor(code: SmsValidationErrorCode) {
    super(code);
    this.name = 'SmsValidationError';
    this.code = code;
  }
}

export class SmsManagerPluginWeb extends WebPlugin implements SmsManagerPlugin {
  async compose(options: SmsComposeOptions): Promise<void> {
    const numbers = Array.isArray(options?.numbers)
      ? options.numbers.filter((number) => typeof number === 'string' && number.length > 0)
      : [];
    if (numbers.length === 0) {
      throw new SmsValidationError('ERR_NO_NUMBERS');
    }
    if (typeof options?.text !== 'string' || options.text.length === 0) {
      throw new SmsValidationError('ERR_NO_TEXT');
    }

    const recipients = numbers.map(encodeRecipient).join(',');
    const body = encodeURIComponent(options.text);
    window.location.assign(`sms:${recipients}?body=${body}`);
  }
}
