package de.franzsw.extractor.data

import de.franzsw.extractor.SharedRes
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format

actual fun platformString(id: StringResource, args: List<Any>): String {
    return if (args.isEmpty()) {
        StringDesc.Resource(id).localized()
    } else {
        id.format(*args.toTypedArray()).localized()
    }
}

actual fun SharedString.toCommonString(): String {
    return when (this) {
        is SharedString.InvalidDigits -> platformString(SharedRes.strings.field_digit_not_valid, listOf(count))
        SharedString.AddEvent -> platformString(SharedRes.strings.add_event)
        SharedString.AddedEvents -> platformString(SharedRes.strings.added_events)
        SharedString.AutoSelectLastColumn -> platformString(SharedRes.strings.auto_select_last_column)
        SharedString.AutoSelectLastRow -> platformString(SharedRes.strings.auto_select_last_row)
        SharedString.ChooseInputFile -> platformString(SharedRes.strings.choose_input_file)
        SharedString.ChooseOutputFolder -> platformString(SharedRes.strings.choose_output_folder)
        SharedString.EndFourDigits -> platformString(SharedRes.strings.end_four_digits)
        SharedString.ErrorInvalidMonthFormat -> platformString(SharedRes.strings.error_invalid_month_format)
        SharedString.ErrorInvalidTimeForEvents -> platformString(SharedRes.strings.error_invalid_time_for_events)
        SharedString.ErrorInvalidYearFormat -> platformString(SharedRes.strings.error_invalid_year_format)
        SharedString.ErrorNoFolderFile -> platformString(SharedRes.strings.error_no_folder_file)
        SharedString.Event -> platformString(SharedRes.strings.event)
        SharedString.EventConfig -> platformString(SharedRes.strings.event_config)
        SharedString.ExcludedEvents -> platformString(SharedRes.strings.excluded_events)
        SharedString.FailedLoadFile -> platformString(SharedRes.strings.failed_load_file)
        SharedString.FirstColumn -> platformString(SharedRes.strings.first_column)
        SharedString.FirstRow -> platformString(SharedRes.strings.first_row)
        SharedString.FoundWorkers -> platformString(SharedRes.strings.found_workers)
        SharedString.LastColumn -> platformString(SharedRes.strings.last_column)
        SharedString.LastRow -> platformString(SharedRes.strings.last_row)
        SharedString.LoadPreview -> platformString(SharedRes.strings.load_preview)
        SharedString.Month -> platformString(SharedRes.strings.month)
        SharedString.OnlyDigitsAllowed -> platformString(SharedRes.strings.only_digits_allowed)
        SharedString.Overnight -> platformString(SharedRes.strings.overnight)
        SharedString.SaveConfig -> platformString(SharedRes.strings.save_config)
        SharedString.StartExport -> platformString(SharedRes.strings.start_export)
        SharedString.StartFourDigits -> platformString(SharedRes.strings.start_four_digits)
        SharedString.Table -> platformString(SharedRes.strings.table)
        SharedString.TableConfig -> platformString(SharedRes.strings.table_config)
        SharedString.WorkerColumn -> platformString(SharedRes.strings.worker_column)
        SharedString.Year -> platformString(SharedRes.strings.year)
    }
}

