package de.franzsw.extractor.presentation.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import de.franzsw.extractor.data.SharedString
import de.franzsw.extractor.data.toCommonString

@Composable
fun IntTextFieldVerification(
    modifier: Modifier = Modifier,
    inputText: Int,
    onTextChange: (Int) -> Unit,
    label: String,
    isEnabled: Boolean = true
) {
    var errorMessage: String? by remember { mutableStateOf(null) }



    OutlinedTextField(
        value = inputText.toString().takeIf { it != "0" }.orEmpty(),
        onValueChange = { text ->
            try {
                errorMessage = null
                if (text.isBlank()) {
                    onTextChange(0)
                } else {
                    onTextChange(text.toInt())
                }
            } catch (e: Exception) {
                errorMessage = SharedString.OnlyDigitsAllowed.toCommonString()
            }
        },
        supportingText = {
            errorMessage?.let { text ->
                Text(text)
            }
        },
        label = {
            Text(label)
        },
        isError = errorMessage != null,
        enabled = isEnabled,
        modifier = modifier,
    )
}