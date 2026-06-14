import Foundation

@objc public class NfcHce: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
