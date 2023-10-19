package de.franzsw.extractor.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.franzsw.extractor.data.SharedString
import de.franzsw.extractor.data.toCommonString
import de.franzsw.extractor.domain.model.ExtractionConfig

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConfigAssembly(
    config: ExtractionConfig,
    onChangeConfig: (ExtractionConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        TextFieldWithVerification(
            digits = 4,
            inputText = config.year,
            onTextChange = {
                onChangeConfig(config.copy(year = it))
            },
            label = SharedString.Year.toCommonString(),
        )

        TextFieldWithVerification(
            digits = 2,
            inputText = config.month,
            onTextChange = {
                onChangeConfig(config.copy(month = it))
            },
            label = SharedString.Month.toCommonString(),
        )

        IntTextFieldVerification(
            inputText = config.sheet,
            onTextChange = {
                onChangeConfig(config.copy(sheet = it))
            },
            label = SharedString.Table.toCommonString(),
        )

        IntTextFieldVerification(
            inputText = config.workerColumn,
            onTextChange = {
                onChangeConfig(config.copy(workerColumn = it))
            },
            label = SharedString.WorkerColumn.toCommonString(),
        )

        IntTextFieldVerification(
            inputText = config.eventsStartRow,
            onTextChange = {
                onChangeConfig(config.copy(eventsStartRow = it))
            },
            label = SharedString.FirstRow.toCommonString(),
        )

        IntTextFieldVerification(
            inputText = config.eventsStartColumn,
            onTextChange = {
                onChangeConfig(config.copy(eventsStartColumn = it))
            },
            label = SharedString.FirstColumn.toCommonString(),
        )

        IntTextFieldVerification(
            inputText = config.eventsEndRow,
            onTextChange = {
                onChangeConfig(config.copy(eventsEndRow = it))
            },
            label = SharedString.LastRow.toCommonString(),
            isEnabled = !config.isAutoSelectLastRow
        )

        IntTextFieldVerification(
            inputText = config.eventsEndColumn,
            onTextChange = {
                onChangeConfig(config.copy(eventsEndColumn = it))
            },
            label = SharedString.LastColumn.toCommonString(),
            isEnabled = !config.isAutoSelectLastColumn
        )

        CheckBoxWithDescription(
            description = SharedString.AutoSelectLastRow.toCommonString(),
            onCheckedChange = { onChangeConfig(config.copy(isAutoSelectLastRow = it)) },
            isChecked = config.isAutoSelectLastRow,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        CheckBoxWithDescription(
            description = SharedString.AutoSelectLastColumn.toCommonString(),
            onCheckedChange = { onChangeConfig(config.copy(isAutoSelectLastColumn = it)) },
            isChecked = config.isAutoSelectLastColumn,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

    }
}

@Composable
private fun CheckBoxWithDescription(
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(2.dp))
            .padding(4.dp)
    ) {
        Text(description)
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}