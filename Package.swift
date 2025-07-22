// swift-tools-version:6.1
import PackageDescription

let package = Package(
    name: "MementoUi",
    platforms: [
        .iOS(.v14),
    ],
    products: [
        .library(name: "MementoUi", targets: ["MementoUi"])
    ],
    targets: [
        .binaryTarget(
            name: "MementoUi",
            url: "https://github.com/takea-look/Memento/releases/download/0.0.2/MementoUi.xcframework.zip",
            checksum:"4ec38d9ed85029514757757c483bbf737b2e6cc15b005cb59bf0f0b419ed6b0b")
    ]
)