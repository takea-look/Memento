import UIKit
import SwiftUI
import Memento

struct ComposeView: UIViewControllerRepresentable {
    var holder : MementoStateHolder!
    init(_ holder: MementoStateHolder) {
        self.holder = holder
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
         return MementoEditorViewControllerKt.MementoEditorViewController(
            mainContent: UIImage(named: "ic_milk")!,
            stateHolder: holder,
            onImageCaptured: { image in saveImageToGallery(image) }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @State var holder: MementoStateHolder = MementoStateHolder()
    var body: some View {
        ComposeView(holder)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
        
        Button("Attach Sticker") {
            holder.attachImage(image: UIImage(named: "ic_glasses")!, contentDescription: nil)
        }
        Button("SaveToDisk") {
            holder.requestCapture()
        }
    }
}

func saveImageToGallery(_ image: UIImage) {
    UIImageWriteToSavedPhotosAlbum(image, nil, nil, nil)
}


#Preview {
    ContentView()
}
