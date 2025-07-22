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
            url: "https://github.com/takea-look/Memento/releases/download/0.1.0/MementoUi.xcframework.zip",
            checksum:"c1a9b6cc263a6a28634a58b660e188ce36f7fec8596ff009ced62222a0147f4d")
    ]
)