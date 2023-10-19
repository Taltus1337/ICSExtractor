package de.franzsw.extractor.data

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

class SettingsRepository {
    private val settings: Settings = PreferencesSettings(Preferences.userRoot())

    fun saveOriginAppointments(session: String) {
        settings.putString(key = "event_meta", value = session)
    }

    fun getOriginAppointments(): String? {
        return settings.getStringOrNull("event_meta")
    }

    fun saveExtractionConfig(config: String) {
        settings.putString(key = "config", value = config)
    }

    fun getExtractionConfig(): String? {
        return settings.getStringOrNull("config")
    }

}