package com.takealook.memento

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MementoEditor(
                mainContent = {
                    Image(
                        painter = painterResource(com.takealook.memento.R.drawable.ic_milk),
                        contentDescription = "milk icon",
                        modifier = Modifier.fillMaxWidth()
                    )
                },
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

