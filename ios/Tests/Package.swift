// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorSmsTests",
    platforms: [.iOS(.v15)],
    dependencies: [
        .package(path: "../..")
    ],
    targets: [
        .testTarget(
            name: "CapacitorSmsTests",
            dependencies: [
                .product(name: "CapacitorSms", package: "capacitor-sms")
            ],
            path: "CapacitorSmsTests"
        )
    ]
)
