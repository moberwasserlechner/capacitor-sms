require 'json'

package = JSON.parse(File.read(File.join(File.dirname(__FILE__), 'package.json')))

Pod::Spec.new do |s|
    s.name = 'ByteowlsCapacitorSms'
    s.version = package['version']
    s.summary = package['description']
    s.license = package['license']
    s.homepage = package['repository']['url']
    s.author = package['author']
    s.source = { :git => package['repository']['url'], :tag => s.version.to_s }
    s.source_files = 'ios/ByteowlsCapacitorSms/Source/*.{swift,h,m}'
    s.ios.deployment_target  = '13.0'
    s.dependency 'Capacitor'
    s.swift_version = '5.1'
end
