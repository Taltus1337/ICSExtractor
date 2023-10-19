package de.franzsw.extractor.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.franzsw.extractor.data.SharedString
import de.franzsw.extractor.data.toCommonString
import de.franzsw.extractor.domain.model.WorkbookPreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreviewAssembly(
    preview: WorkbookPreview?,
    onLoadPreview: () -> Unit,
    isFailedOpenError: Boolean,
    isLoading: Boolean
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedContent(isLoading) {
                    if (it) {
                        CircularProgressIndicator()
                    } else {
                        FilledTonalButton(
                            onClick = onLoadPreview,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(SharedString.LoadPreview.toCommonString())
                        }
                    }
                }


                if (isFailedOpenError) {
                    Text(
                        text = SharedString.FailedLoadFile.toCommonString(),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }


            Text(
                buildString {
                    preview?.eventsPreview?.forEachIndexed { index, rows ->
                        rows.forEach { field ->
                            append(" | ${field.ifBlank { "N/A" }} | ")
                        }
                        if (index == 1) append("\n\n < ... > \n\n") else append("\n")
                    }
                },
                modifier = Modifier.padding(8.dp)
            )
        }

        if (preview != null) {
            Text(
                text = "${SharedString.FoundWorkers.toCommonString()}: ${preview.foundWorkers.joinToString().ifBlank { "N/A" }}",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }

}

