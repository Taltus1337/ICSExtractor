package de.franzsw.extractor.presentation.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import de.franzsw.extractor.domain.util.verifyDigits

@Composable
fun TextFieldWithVerification(
    modifier: Modifier = Modifier,
    digits: Int,
    inputText: String,
    onTextChange: (String) -> Unit,
    label: String
) {
    var errorMessage: String? by remember { mutableStateOf(null) }

    OutlinedTextField(
        value = inputText,
        onValueChange = { text ->
            try {
                errorMessage = null
                onTextChange(text.verifyDigits(digits))
            } catch (e: Exception) {
                errorMessage = e.message
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
        modifier = modifier
    )
}