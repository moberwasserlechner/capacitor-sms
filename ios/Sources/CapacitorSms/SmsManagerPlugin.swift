import Capacitor
import Foundation
import MessageUI

public enum SmsError: String {
    case noNumbers = "ERR_NO_NUMBERS"
    case noText = "ERR_NO_TEXT"
    case serviceNotFound = "ERR_SERVICE_NOTFOUND"
    case sendCancelled = "SEND_CANCELLED"
    case sendFailed = "ERR_SEND_FAILED"
    case unknownState = "ERR_SEND_UNKNOWN_STATE"
}

@objc(SmsManagerPlugin)
public class SmsManagerPlugin: CAPPlugin, CAPBridgedPlugin, MFMessageComposeViewControllerDelegate {
    public let identifier = "SmsManagerPlugin"
    public let jsName = "SmsManager"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "send", returnType: CAPPluginReturnPromise)
    ]

    private var pendingCall: CAPPluginCall?

    @objc func send(_ call: CAPPluginCall) {
        guard let numbers = call.getArray("numbers", String.self), !numbers.isEmpty else {
            call.reject(SmsError.noNumbers.rawValue)
            return
        }
        guard let text = call.getString("text"), !text.isEmpty else {
            call.reject(SmsError.noText.rawValue)
            return
        }
        guard MFMessageComposeViewController.canSendText() else {
            call.reject(SmsError.serviceNotFound.rawValue)
            return
        }

        pendingCall = call
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            let composer = MFMessageComposeViewController()
            composer.messageComposeDelegate = self
            composer.recipients = numbers
            composer.body = text
            self.bridge?.viewController?.present(composer, animated: true)
        }
    }

    public func messageComposeViewController(
        _ controller: MFMessageComposeViewController,
        didFinishWith result: MessageComposeResult
    ) {
        defer {
            pendingCall = nil
            controller.dismiss(animated: true)
        }
        switch result {
        case .cancelled:
            pendingCall?.reject(SmsError.sendCancelled.rawValue)
        case .failed:
            pendingCall?.reject(SmsError.sendFailed.rawValue)
        case .sent:
            pendingCall?.resolve()
        @unknown default:
            pendingCall?.reject(SmsError.unknownState.rawValue)
        }
    }
}
