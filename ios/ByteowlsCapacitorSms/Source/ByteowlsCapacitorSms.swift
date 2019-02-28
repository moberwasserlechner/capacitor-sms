import Foundation
import Capacitor
import MessageUI

typealias JSObject = [String:Any]

@objc(SmsManagerPlugin)
public class SmsManagerPlugin: CAPPlugin, MFMessageComposeViewControllerDelegate {
    
    let PARAM_NUMBERS = "numbers"
    let PARAM_TEXT = "text"
    
    var pluginCall: CAPPluginCall?
    
    public func messageComposeViewController(_ controller: MFMessageComposeViewController, didFinishWith result: MessageComposeResult) {
       
        switch (result.rawValue) {
        case MessageComposeResult.cancelled.rawValue:
            self.pluginCall!.reject("SEND_CANCELLED")
        case MessageComposeResult.failed.rawValue:
            self.pluginCall!.reject("ERR_SEND_FAILED")
        case MessageComposeResult.sent.rawValue:
            self.pluginCall!.success()
        default:
            self.pluginCall!.reject("ERR_SEND_UNKOWN_STATE")
        }
        controller.dismiss(animated: true, completion: nil)
    }

    @objc func send(_ call: CAPPluginCall) {
        self.pluginCall = call
        guard let numbers = call.getArray(PARAM_NUMBERS, String.self) else {
            call.reject("ERR_NO_NUMBERS")
            return
        }
        guard let text = call.getString(PARAM_TEXT) else {
            call.reject("ERR_NO_TEXT")
            return
        }
        
        if !MFMessageComposeViewController.canSendText() {
            call.reject("ERR_SMS_SERVICE_NOTFOUND")
            return
        }
        
        let composeVC = MFMessageComposeViewController()
        composeVC.messageComposeDelegate = self
        // Configure the fields of the interface.
        composeVC.recipients = numbers
        composeVC.body = text
        // Present the view controller modally.
        DispatchQueue.main.async {
            // Update UI
            self.bridge.viewController.present(composeVC, animated: true, completion: nil);
        }
    }
}
