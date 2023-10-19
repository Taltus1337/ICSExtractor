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
        EventMeta("E1","0600","1800",false),
        EventMeta("E2","1800","0600",true),
    )
}