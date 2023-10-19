package de.franzsw.extractor.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.franzsw.extractor.data.SharedString
import de.franzsw.extractor.data.toCommonString

@Composable
fun ActionInfoAssembly(
    onClickExport: () -> Unit,
    onSaveConfigEvents: () -> Unit,
    errorMessage: String?,
    isError: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .animateContentSize()
            .padding(8.dp)
    ) {

        errorMessage?.let {
            Text(
                text = it,
                color = if (isError) MaterialTheme.colorScheme.error else LocalTextStyle.current.color,
                modifier = Modifier.padding(8.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedContent(isLoading) {
                if (it) {
                    CircularProgressIndicator()
                } else {
                    Button(onClickExport) {
                        Text(SharedString.StartExport.toCommonString())
                    }
                }
            }

            Button(onSaveConfigEvents) {
                Text(SharedString.SaveConfig.toCommonString())
            }
        }
    }
}