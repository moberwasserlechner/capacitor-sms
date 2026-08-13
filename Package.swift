// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorSms",
    platforms: [.iOS(.v15)],
    products: [
        .library(name: "CapacitorSms", targets: ["CapacitorSms"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", .upToNextMajor(from: "8.0.0"))
    ],
    targets: [
        .target(
            name: "CapacitorSms",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/CapacitorSms"
        )
    ]
)
