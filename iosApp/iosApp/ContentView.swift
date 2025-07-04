import UIKit
import SwiftUI
import Memento

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MementoEditorViewControllerKt.MementoEditorViewController(
            mainContent: UIImage(named: "ic_milk")!
        ) { builder in
            let sticker = MementoSticker(
                key: "ic_glasses",
                image: UIImage(named: "ic_glasses")!,
                contentDescription: "glasses icon"
            )
            
            builder.sticker(content: sticker)
        }
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}



#Preview {
    ContentView()
}
