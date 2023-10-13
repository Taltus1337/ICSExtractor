package de.franzsw.extractor.domain

import de.franzsw.extractor.data.SettingsRepository
import de.franzsw.extractor.domain.model.ExtractionConfig
import de.franzsw.extractor.domain.model.EventMeta
import de.franzsw.extractor.domain.model.getDefaultExtractionConfig
import de.franzsw.extractor.domain.model.getDefaultInitialAppointments
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SessionConverter {
    private var settingsRepository: SettingsRepository? = try { SettingsRepository() } catch(e: Exception) {null}

    fun saveInitialAppointments(appointments: List<EventMeta>) {
        val json = Json.encodeToString(appointments)
        settingsRepository?.saveOriginAppointments(json) ?: println("preferences can't be accessed")
    }

    fun getInitialAppointments(): List<EventMeta> {
        return try {
            val appointmentsJson = settingsRepository?.getOriginAppointments() ?: throw IllegalAccessException()
            Json.decodeFromString<List<EventMeta>>(appointmentsJson)
        } catch (e: Exception) {
            getDefaultInitialAppointments()
        }
    }

    fun saveExtractionConfig(config: ExtractionConfig) {
        val json = Json.encodeToString(config)
        settingsRepository?.saveExtractionConfig(json) ?: println("preferences can't be accessed")
    }

    fun getExtractionConfig(): ExtractionConfig {
        return try {
            val config = settingsRepository?.getExtractionConfig() ?: throw IllegalAccessException()
            Json.decodeFromString<ExtractionConfig>(config)
        } catch (e: Exception) {
            getDefaultExtractionConfig()
        }
    }
}