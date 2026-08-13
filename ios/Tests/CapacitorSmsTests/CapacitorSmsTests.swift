import XCTest
@testable import CapacitorSms

final class CapacitorSmsTests: XCTestCase {
    func testPluginMetadataMatchesJavaScriptRegistration() {
        let plugin = SmsManagerPlugin()

        XCTAssertEqual(plugin.identifier, "SmsManagerPlugin")
        XCTAssertEqual(plugin.jsName, "SmsManager")
        XCTAssertEqual(plugin.pluginMethods.count, 1)
        XCTAssertEqual(plugin.pluginMethods.first?.name, "send")
    }

    func testErrorCodesRemainCompatible() {
        XCTAssertEqual(SmsError.noNumbers.rawValue, "ERR_NO_NUMBERS")
        XCTAssertEqual(SmsError.noText.rawValue, "ERR_NO_TEXT")
        XCTAssertEqual(SmsError.serviceNotFound.rawValue, "ERR_SERVICE_NOTFOUND")
        XCTAssertEqual(SmsError.sendCancelled.rawValue, "SEND_CANCELLED")
        XCTAssertEqual(SmsError.sendFailed.rawValue, "ERR_SEND_FAILED")
        XCTAssertEqual(SmsError.unknownState.rawValue, "ERR_SEND_UNKNOWN_STATE")
    }
}
