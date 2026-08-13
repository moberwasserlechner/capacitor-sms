import { describe, expect, it } from 'vitest';

import { SmsManagerPluginWeb } from '../src/web';

describe('SmsManagerPluginWeb', () => {
  it('keeps SMS composition unsupported on web', async () => {
    const plugin = new SmsManagerPluginWeb();

    await expect(plugin.compose({ numbers: ['+43123'], text: 'Hello' })).rejects.toMatchObject({
      code: 'UNIMPLEMENTED',
    });
  });
});
