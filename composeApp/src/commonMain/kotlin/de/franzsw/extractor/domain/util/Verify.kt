package de.franzsw.extractor.domain.util

import de.franzsw.extractor.data.SharedString
import de.franzsw.extractor.data.toCommonString

/**
 * @throws error if condition not matches
 */
fun String.verifyDigits(digits: Int): String {
    if (!(this.length <= digits && this.all { it.isDigit() })) {
        error(SharedString.InvalidDigits(digits).toCommonString())
    } else {
        return this
    }
}

/**
 * @return input string if valid else null
 */
fun String.verifyDigitsAndLength(digits: Int): String? {
    return if (!(this.length == digits && this.all { it.isDigit() })) null else  this
}



