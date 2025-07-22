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
        ZStack(alignment: .topTrailing) {
            ComposeView(controller)
                    .ignoresSafeArea(.all) // Compose has own keyboard handler
            
            HStack(spacing: 8) {
                Button(action: {
                    controller.attachImage(image: UIImage(named: "ic_glasses")!, contentDescription: "")
                }) {
                    Image(systemName: "plus")
                        .frame(width: 24, height: 24)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.blue)
                        .clipShape(Circle())
                        .shadow(radius: 4)
                }
                
                Button(action: {
                    controller.removeCurrent()
                }) {
                    Image(systemName: "minus")
                        .frame(width: 24, height: 24)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.red)
                        .clipShape(Circle())
                        .shadow(radius: 4)
                }
                
                Button(action: {
                    controller.requestCapture()
                }) {
                    Image(systemName: "square.and.arrow.down")
                        .frame(width: 24, height: 24)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.orange)
                        .clipShape(Circle())
                        .shadow(radius: 4)
                }
            }
        }
    }
}

func saveImageToGallery(_ image: UIImage) {
    UIImageWriteToSavedPhotosAlbum(image, nil, nil, nil)
}


#Preview {
    ContentView()
}
