package de.franzsw.extractor.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.franzsw.extractor.data.SharedString
import de.franzsw.extractor.data.toCommonString
import de.franzsw.extractor.domain.model.EventMeta

@Composable
fun EventsAssembly(
    events: List<EventMeta>,
    onChangeEvent: (index: Int, event: EventMeta) -> Unit,
    onDeleteEvent: (index: Int) -> Unit,
    onAddEvent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .animateContentSize()
    ) {
        events.forEachIndexed { index, item ->
            MetaItem(
                meta = item,
                onChangeMeta = { onChangeEvent(index, it) },
                onDelete = { onDeleteEvent(index) }
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
//                .padding(8.dp)
        ) {
            FilledTonalButton(onClick = onAddEvent) {
                Text(SharedString.AddEvent.toCommonString())
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MetaItem(
    meta: EventMeta,
    onChangeMeta: (EventMeta) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(10.dp))
            .padding(4.dp)
    ) {
        OutlinedTextField(
            value = meta.abbreviation,
            onValueChange = {
                onChangeMeta(meta.copy(abbreviation = it))
            },
            label = {
                Text(SharedString.Event.toCommonString())
            },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        )

        TextFieldWithVerification(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            digits = 4,
            inputText = meta.start,
            onTextChange = { onChangeMeta(meta.copy(start = it)) },
            label = SharedString.StartFourDigits.toCommonString()
        )

        TextFieldWithVerification(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            digits = 4,
            inputText = meta.end,
            onTextChange = { onChangeMeta(meta.copy(end = it)) },
            label = SharedString.EndFourDigits.toCommonString()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(SharedString.Overnight.toCommonString())
                Checkbox(
                    checked = meta.isOvernight,
                    onCheckedChange = { onChangeMeta(meta.copy(isOvernight = it)) }
                )
            }

            IconButton(
                onClick = onDelete,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                )
            }
        }

    }

}