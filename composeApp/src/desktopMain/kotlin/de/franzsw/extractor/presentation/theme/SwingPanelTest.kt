package de.franzsw.extractor.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import javax.swing.JPanel


@Composable
fun SwingPanelTest() {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        var isSwingPanelOpen by remember { mutableStateOf(false) }

        Button(
            onClick = { isSwingPanelOpen = !isSwingPanelOpen },
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Text("Toggle Swing Panel")
        }

        if (isSwingPanelOpen) {
            SwingPanel(
                factory = {
                    JPanel().apply {
                        background = java.awt.Color.RED
                    }
                },
                background = Color.Green,
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.Center)
                    .background(Color.Green)
            )
        }
    }
}