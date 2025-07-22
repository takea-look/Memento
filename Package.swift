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
            url: "https://github.com/takea-look/Memento/releases/download/0.0.5/MementoUi.xcframework.zip",
            checksum:"7903b3688745826b029ef5710926c7e92afd54bfb1a65d06ec1b36442b91cb4f")
    ]
)