package de.franzsw.extractor.domain.util

/**
 * @throws error if not condition not matches
 */
fun String.verifyHasFourDigits() {
    if (!(this.length == 4 && this.all { it.isDigit() })) error("Feld muss exakt 4 Zahlen enthalen")
}

/**
 * @throws error if not condition not matches
 */
fun String.verifyHasTwoDigits() {
    if (!(this.length == 2 && this.all { it.isDigit() })) error("Feld muss exakt 2 Zahlen enthalen")
}