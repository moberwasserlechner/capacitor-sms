# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [3.0.1] - 2022-04-13

### Fixes
* Readme and Changelog links

## [3.0.0] - 2022-04-13

### Changed

- Upgrade to support Capacitor 3.x - [#12](https://github.com/moberwasserlechner/capacitor-sms/pull/12), [#11](https://github.com/moberwasserlechner/capacitor-sms/issues/11) ... thx [@chrum](https://github.com/chrum)
- Fix thread access - [#13](https://github.com/moberwasserlechner/capacitor-sms/pull/13), [#9](https://github.com/moberwasserlechner/capacitor-sms/issues/9)... thx [@chrum](https://github.com/chrum)

### Breaking
- Capacitor 3.x is new minimum peer dependency!

## [2.0.0] - 2020-04-09

### Breaking
- Capacitor 2.x is new minimum peer dependency!

## [1.0.0] - 2019-06-23

### Breaking
-  Capacitor 1.x is new minimum peer dependency

## [1.0.0-beta.2] - 2019-03-07

### Fixed
- Capacitor version was pinned to beta.16 in podspec file. That prevented the capacitor upgrade to beta.18 by using npx cap sync ios

## [1.0.0-beta.1] - 2019-03-01

### Added
- Send SMS to more than one recipient by opening the native SMS App on Android or iOS

### Breaking
- This plugin will not support Android's background sending features as Google recently change their policy on sending SMS limiting the permission to only a few usecases. See https://play.google.com/about/privacy-security-deception/permissions/ for details.

[Unreleased]: https://github.com/moberwasserlechner/capacitor-sms/compare/3.0.1..main
[3.0.1]: https://github.com/moberwasserlechner/capacitor-sms/compare/3.0.0..3.0.1
[3.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/2.0.0..3.0.0
[2.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/1.0.0..2.0.0
[1.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/1.0.0-beta.2..1.0.0
[1.0.0-beta.2]: https://github.com/moberwasserlechner/capacitor-sms/compare/1.0.0-beta.1..1.0.0-beta.2
[1.0.0-beta.1]: https://github.com/moberwasserlechner/capacitor-sms/releases/tag/1.0.0-beta.1
