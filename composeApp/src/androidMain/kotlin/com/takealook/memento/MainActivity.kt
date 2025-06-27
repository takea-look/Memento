package com.takealook.memento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.takealook.memento.sticker.sticker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MementoEditor(
                stickerBuilder = {
                    (0..10).forEach { _ ->
                        sticker {
                            Image(
                                modifier = Modifier,
                                painter = painterResource(com.takealook.memento.R.drawable.ic_glasses),
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}