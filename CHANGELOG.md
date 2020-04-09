# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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

[Unreleased]: https://github.com/moberwasserlechner/capacitor-sms/compare/2.0.0..master
[2.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/1.0.0..2.0.0
[1.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/1.0.0-beta.2..1.0.0
[1.0.0-beta.2]: https://github.com/moberwasserlechner/capacitor-sms/compare/1.0.0-beta.1..1.0.0-beta.2
[1.0.0-beta.1]: https://github.com/moberwasserlechner/capacitor-sms/releases/tag/1.0.0-beta.1
