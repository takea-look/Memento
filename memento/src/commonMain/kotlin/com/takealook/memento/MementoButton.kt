package com.takealook.memento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.takealook.memento.resources.Res
import com.takealook.memento.resources.ic_sticker
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun MementoButton(
    onClick: () -> Unit,
    icon: DrawableResource,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.Black)
            .padding(5.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            modifier = modifier.align(Alignment.Center),
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

@Preview
@Composable
fun MementoButtonPreview() {
    MementoButton(
        onClick = {},
        icon = Res.drawable.ic_sticker,
        contentDescription = "Sticker",
    )
}