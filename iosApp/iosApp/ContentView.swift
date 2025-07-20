import UIKit
import SwiftUI
import MementoUi

struct ComposeView: UIViewControllerRepresentable {
    var controller : MementoController!
    init(_ controller: MementoController) {
        self.controller = controller
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
         return MementoEditorViewControllerKt.MementoEditorViewController(
            mainContent: UIImage(named: "ic_milk")!,
            controller: controller,
            onImageCaptured: { image in saveImageToGallery(image) }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @State var controller: MementoController = MementoController()
    var body: some View {
        ComposeView(controller)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
        
        Button("Attach Sticker") {
            controller.attachImage(image: UIImage(named: "ic_glasses")!, contentDescription: nil)
        }
        Button("SaveToDisk") {
            controller.requestCapture()
        }
    }
}

func saveImageToGallery(_ image: UIImage) {
    UIImageWriteToSavedPhotosAlbum(image, nil, nil, nil)
}


#Preview {
    ContentView()
}
