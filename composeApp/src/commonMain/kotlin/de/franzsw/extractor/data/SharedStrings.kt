package de.franzsw.extractor.data

import dev.icerock.moko.resources.StringResource

expect fun platformString(id: StringResource, args: List<Any> = listOf()): String

expect fun SharedString.toCommonString(): String

sealed interface SharedString {
    data class InvalidDigits(val count: Int) : SharedString
    data object ExcludedEvents : SharedString
    data object AddedEvents : SharedString
    data object StartExport : SharedString
    data object SaveConfig : SharedString
    data object Year : SharedString
    data object Month : SharedString
    data object Table : SharedString
    data object WorkerColumn : SharedString
    data object FirstRow : SharedString
    data object FirstColumn : SharedString
    data object LastRow : SharedString
    data object LastColumn : SharedString
    data object AutoSelectLastRow : SharedString
    data object AutoSelectLastColumn : SharedString
    data object AddEvent : SharedString
    data object Event : SharedString
    data object StartFourDigits : SharedString
    data object EndFourDigits : SharedString
    data object Overnight : SharedString
    data object OnlyDigitsAllowed : SharedString
    data object LoadPreview : SharedString
    data object FailedLoadFile : SharedString
    data object FoundWorkers : SharedString
    data object ChooseInputFile : SharedString
    data object ChooseOutputFolder : SharedString
    data object EventConfig : SharedString
    data object TableConfig : SharedString
    data object ErrorNoFolderFile : SharedString
    data object ErrorInvalidTimeForEvents : SharedString
    data object ErrorInvalidMonthFormat : SharedString
    data object ErrorInvalidYearFormat : SharedString
}

