#!/usr/bin/env node

import { execFileSync } from 'node:child_process';
import { existsSync, mkdirSync, mkdtempSync, readFileSync, realpathSync, rmSync, symlinkSync, writeFileSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join, resolve } from 'node:path';

const root = resolve(import.meta.dirname, '..');
const workspace = mkdtempSync(join(tmpdir(), 'capacitor-sms-verify-'));
const expected = [
  'package/LICENSE',
  'package/Package.swift',
  'package/README.md',
  'package/android/build.gradle',
  'package/android/src/main/AndroidManifest.xml',
  'package/android/src/main/kotlin/com/byteowls/capacitor/sms/SmsComposer.kt',
  'package/android/src/main/kotlin/com/byteowls/capacitor/sms/SmsManagerPlugin.kt',
  'package/android/src/main/kotlin/com/byteowls/capacitor/sms/SmsOptions.kt',
  'package/dist/index.cjs',
  'package/dist/index.cjs.map',
  'package/dist/index.d.ts',
  'package/dist/index.js',
  'package/dist/index.js.map',
  'package/dist/plugin.js',
  'package/dist/plugin.js.map',
  'package/ios/Sources/CapacitorSms/SmsManagerPlugin.swift',
  'package/package.json',
].sort();

try {
  const output = execFileSync('npm', ['pack', '--json', '--pack-destination', workspace], {
    cwd: root,
    encoding: 'utf8',
  });
  const tarball = join(workspace, JSON.parse(output)[0].filename);
  const actual = execFileSync('tar', ['-tzf', tarball], { encoding: 'utf8' })
    .split('\n')
    .filter((entry) => entry.startsWith('package/') && !entry.endsWith('/'))
    .sort();
  const missing = expected.filter((entry) => !actual.includes(entry));
  const unexpected = actual.filter((entry) => !expected.includes(entry));
  if (missing.length || unexpected.length) {
    throw new Error(`package contents differ\nmissing: ${missing.join(', ')}\nunexpected: ${unexpected.join(', ')}`);
  }

  const consumer = join(workspace, 'consumer');
  const packageDir = join(consumer, 'node_modules', '@byteowls', 'capacitor-sms');
  mkdirSync(packageDir, { recursive: true });
  execFileSync('tar', ['-xzf', tarball, '-C', packageDir, '--strip-components=1']);
  const manifest = JSON.parse(readFileSync(join(packageDir, 'package.json'), 'utf8'));
  if (Object.keys(manifest.dependencies ?? {}).length) throw new Error('production dependencies are not allowed');

  const swiftManifest = readFileSync(join(packageDir, 'Package.swift'), 'utf8');
  const swiftTargetPaths = [...swiftManifest.matchAll(/path:\s*"([^"]+)"/g)].map((match) => match[1]);
  for (const targetPath of swiftTargetPaths) {
    if (!existsSync(join(packageDir, targetPath))) {
      throw new Error(`Swift target path is missing from the package: ${targetPath}`);
    }
  }

  const capacitorScope = join(consumer, 'node_modules', '@capacitor');
  mkdirSync(capacitorScope, { recursive: true });
  symlinkSync(realpathSync(join(root, 'node_modules', '@capacitor', 'core')), join(capacitorScope, 'core'));
  writeFileSync(join(consumer, 'package.json'), '{"name":"consumer","private":true,"type":"module"}\n');
  writeFileSync(
    join(consumer, 'app.ts'),
    "import { SmsManager } from '@byteowls/capacitor-sms';\nimport type { SmsSendOptions } from '@byteowls/capacitor-sms';\nconst options: SmsSendOptions = { numbers: ['+43123'], text: 'Hello' };\nvoid SmsManager.send(options);\n",
  );
  writeFileSync(
    join(consumer, 'tsconfig.json'),
    JSON.stringify({ compilerOptions: { module: 'NodeNext', moduleResolution: 'NodeNext', target: 'ES2022', strict: true, noEmit: true, skipLibCheck: false }, include: ['app.ts'] }),
  );

  execFileSync('node', ['--input-type=module', '-e', "import { SmsManager } from '@byteowls/capacitor-sms'; if (!SmsManager) throw new Error('ESM export missing')"], { cwd: consumer });
  execFileSync('node', ['--input-type=commonjs', '-e', "if (!require('@byteowls/capacitor-sms').SmsManager) throw new Error('CommonJS export missing')"], { cwd: consumer });
  execFileSync(join(root, 'node_modules', '.bin', 'tsc'), ['-p', 'tsconfig.json'], { cwd: consumer });
  console.log('package contents, Swift targets, runtime exports, and declarations verified');
} finally {
  rmSync(workspace, { recursive: true, force: true });
}
