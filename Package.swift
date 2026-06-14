// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorNfcHce",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "CapacitorNfcHce",
            targets: ["NfcHcePlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "8.0.0")
    ],
    targets: [
        .target(
            name: "NfcHcePlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/NfcHcePlugin"),
        .testTarget(
            name: "NfcHcePluginTests",
            dependencies: ["NfcHcePlugin"],
            path: "ios/Tests/NfcHcePluginTests")
    ]
)