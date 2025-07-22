package my.takealook.memento

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val holder = rememberMementoController()
            val context = LocalContext.current

            Scaffold { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    MementoEditor(
                        modifier = Modifier.fillMaxSize(),
                        controller = holder,
                        mainContent = {
                            Image(
                                painter = painterResource(my.takealook.memento.R.drawable.ic_milk),
                                contentDescription = "milk icon",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onImageCaptured = { bitmap ->
                            bitmap.saveToDisk(context)
                        }
                    )

                    Column {
                        Button(onClick = holder::requestCapture) {
                            Text("Save To Disk")
                        }

                        Button(
                            onClick = {
                                holder.attachImage {
                                    AsyncImage(
                                        model = my.takealook.memento.R.drawable.ic_glasses,
                                        contentDescription = "glasses icon",
                                    )
                                }
                            }
                        ) {
                            Text("Create Image")
                        }
                    }
                }

            }

        }
    }
}


fun ImageBitmap.saveToDisk(
    context: Context,
    filename: String = "memento_captured_${System.currentTimeMillis()}.jpg"
) {
    val bitmap = this.asAndroidBitmap()
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/memento")
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let {
        resolver.openOutputStream(it)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

