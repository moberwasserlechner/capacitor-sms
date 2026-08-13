import { afterEach, describe, expect, it, vi } from 'vitest';

import type { SmsComposeOptions } from '../src/definitions';
import { SmsManagerPluginWeb } from '../src/web';

afterEach(() => {
  vi.unstubAllGlobals();
});

describe('SmsManagerPluginWeb', () => {
  it('opens an encoded SMS URL for multiple recipients', async () => {
    const assign = vi.fn();
    vi.stubGlobal('window', { location: { assign } });
    const plugin = new SmsManagerPluginWeb();

    await plugin.compose({ numbers: ['+43 123', '', '+43&456'], text: 'Hello & goodbye' });

    expect(assign).toHaveBeenCalledWith('sms:+43%20123,+43%26456?body=Hello%20%26%20goodbye');
  });

  it.each([
    undefined,
    null,
    { numbers: [], text: 'Hello' },
    { numbers: [''], text: 'Hello' },
    { numbers: undefined, text: 'Hello' },
  ])('rejects missing or invalid recipients', async (options) => {
    const plugin = new SmsManagerPluginWeb();

    await expect(plugin.compose(options as unknown as SmsComposeOptions)).rejects.toMatchObject({
      code: 'ERR_NO_NUMBERS',
    });
  });

  it.each([
    { numbers: ['+43123'], text: '' },
    { numbers: ['+43123'], text: undefined },
  ])('rejects invalid message text', async (options) => {
    const plugin = new SmsManagerPluginWeb();

    await expect(plugin.compose(options as unknown as SmsComposeOptions)).rejects.toMatchObject({
      code: 'ERR_NO_TEXT',
    });
  });
});
