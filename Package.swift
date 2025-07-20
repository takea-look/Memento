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
            url: "https://github.com/takea-look/Memento/releases/download/0.0.0/MementoUi.xcframework.zip",
            checksum:"bd4378ab60f9ec4fc72f681c5877ccdac2f029d5df5edb8d4945884de0f6c7d8")
    ]
)