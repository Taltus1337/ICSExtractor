package de.franzsw.extractor.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.franzsw.extractor.data.SharedString
import de.franzsw.extractor.data.toCommonString
import de.franzsw.extractor.presentation.ExtractionEvent

@Composable
fun TopRow(
    onLaunchInputPicker: () -> Unit,
    inputPath: String,
    onLaunchOutputPicker: () -> Unit,
    outputPath: String
) {
    Row(
//        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Button(
            onClick = onLaunchInputPicker,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(SharedString.ChooseInputFile.toCommonString())
        }
        Text(
            text = inputPath,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.weight(2f))
        Text(
            text = outputPath,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
        )
        Button(
            onClick = onLaunchOutputPicker,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(SharedString.ChooseOutputFolder.toCommonString())
        }

    }
}