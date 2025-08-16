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
            url: "https://github.com/takea-look/Memento/releases/download/0.2.1/MementoUi.xcframework.zip",
            checksum:"a4a699247b7dee4c5d545751a0e8218913862260d51f50565024be89b5d85146")
    ]
)