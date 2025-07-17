import UIKit
import SwiftUI
import Memento

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MementoEditorViewControllerKt.MementoEditorViewController(
            mainContent: UIImage(named: "ic_milk")!
        )
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
