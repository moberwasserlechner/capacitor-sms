import Foundation
import Capacitor

typealias JSObject = [String:Any]

@objc(SmsManagerPlugin)
public class SmsManagerPlugin: CAPPlugin {

    let PARAM_NUMBERS = "numbers"
    let PARAM_TEXT = "text"


    @objc func send(_ call: CAPPluginCall) {
        guard let numbers = getString(call, PARAM_NUMBERS) else {
            call.reject("ERR_INVALID_SMS")
            return
        }
        guard let text = getString(call, PARAM_TEXT) else {
            call.reject("ERR_INVALID_SMS")
            return
        }
    }

    private func getConfigObjectDeepest(_ options: [AnyHashable: Any?]!, key: String) -> [AnyHashable:Any?]? {
        let parts = key.split(separator: ".")

        var o = options
        for (_, k) in parts[0..<parts.count-1].enumerated() {
            if (o != nil) {
                o = o?[String(k)] as? [String:Any?] ?? nil
            }
        }
        return o
    }

    private func getConfigKey(_ key: String) -> String {
        let parts = key.split(separator: ".")
        if parts.last != nil {
            return String(parts.last!)
        }
        return ""
    }
    
    private func getOverwritableString(_ call: CAPPluginCall, _ key: String) -> String? {
        var base = getString(call, key)
        let ios = getString(call, "ios." + key)
        if ios != nil {
            base = ios
        }
        return base;
    }

    private func getValue(_ call: CAPPluginCall, _ key: String) -> Any? {
        let k = getConfigKey(key)
        let o = getConfigObjectDeepest(call.options, key: key)
        return o?[k] ?? nil
    }

    private func getString(_ call: CAPPluginCall, _ key: String) -> String? {
        let value = getValue(call, key)
        if value == nil {
            return nil
        }
        return value as? String
    }

}
