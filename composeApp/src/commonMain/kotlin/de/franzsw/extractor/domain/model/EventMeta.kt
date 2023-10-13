package de.franzsw.extractor.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EventMeta(
    val abbreviation: String,
    val start: String,
    val end: String,
    val isOvernight: Boolean
)

fun getDefaultInitialAppointments(): List<EventMeta> {
    return listOf(
        EventMeta("DBT","0600","1800",false),
        EventMeta("DBN","1800","0600",true),
        EventMeta("DÜN","0700","1500",false),
        EventMeta("DUN","0700","1500",false),
        EventMeta("MEB","1500","1630",false),
    )
}