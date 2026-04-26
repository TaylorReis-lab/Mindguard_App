package com.mindguard.app.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import java.util.concurrent.TimeUnit

class MindguardRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("mindguard_prefs", Context.MODE_PRIVATE)
    
    private val _allEvents = MutableStateFlow<List<BlockEvent>>(emptyList())
    val allEvents: StateFlow<List<BlockEvent>> = _allEvents

    private val _userStats = MutableStateFlow(loadStats())
    val userStats: StateFlow<UserStats> = _userStats

    private val _customBlocklist = MutableStateFlow(loadCustomBlocklist())
    val customBlocklist: StateFlow<List<String>> = _customBlocklist

    private val _language = MutableStateFlow(prefs.getString("language", "pt-BR") ?: "pt-BR")
    val language: StateFlow<String> = _language

    private val _notificationsEnabled = MutableStateFlow(prefs.getBoolean("notifications", true))
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    fun toggleNotifications() {
        val newVal = !_notificationsEnabled.value
        prefs.edit().putBoolean("notifications", newVal).apply()
        _notificationsEnabled.value = newVal
    }

    private fun loadStats(): UserStats {
        return UserStats(
            daysClean = prefs.getInt("daysClean", 0),
            totalFailedAttempts = prefs.getInt("totalFailedAttempts", 0),
            totalBlockedAttempts = prefs.getInt("totalBlockedAttempts", 0),
            lastFailTimestamp = prefs.getLong("lastFailTimestamp", 0)
        )
    }

    private fun loadCustomBlocklist(): List<String> {
        return prefs.getStringSet("custom_blocklist", emptySet())?.toList() ?: emptyList()
    }

    fun addCustomSite(url: String) {
        val current = _customBlocklist.value.toMutableSet()
        if (current.add(url)) {
            prefs.edit().putStringSet("custom_blocklist", current).apply()
            _customBlocklist.value = current.toList()
        }
    }

    fun removeCustomSite(url: String) {
        val current = _customBlocklist.value.toMutableSet()
        if (current.remove(url)) {
            prefs.edit().putStringSet("custom_blocklist", current).apply()
            _customBlocklist.value = current.toList()
        }
    }

    fun setLanguage(lang: String) {
        prefs.edit().putString("language", lang).apply()
        _language.value = lang
    }

    fun isUrlBlocked(url: String): Boolean {
        // Check Custom Blocklist
        if (_customBlocklist.value.any { url.contains(it, ignoreCase = true) }) return true
        
        // Check Master Adult Blocklist
        return BlocklistProvider.isAdultContent(url)
    }

    private fun saveStats(stats: UserStats) {
        prefs.edit().apply {
            putInt("daysClean", stats.daysClean)
            putInt("totalFailedAttempts", stats.totalFailedAttempts)
            putInt("totalBlockedAttempts", stats.totalBlockedAttempts)
            putLong("lastFailTimestamp", stats.lastFailTimestamp)
            apply()
        }
        _userStats.value = stats
    }

    fun recordBlock(url: String, type: String) {
        val newEvent = BlockEvent(url = url, type = type)
        _allEvents.value = listOf(newEvent) + _allEvents.value
        
        if (type == "ADULT") {
            val current = _userStats.value
            saveStats(current.copy(totalBlockedAttempts = current.totalBlockedAttempts + 1))
        }
    }

    fun recordFail() {
        val current = _userStats.value
        saveStats(current.copy(
            daysClean = 0,
            totalFailedAttempts = current.totalFailedAttempts + 1,
            lastFailTimestamp = System.currentTimeMillis()
        ))
    }

    fun updateDaysClean() {
        val current = _userStats.value
        if (current.lastFailTimestamp > 0) {
            val diff = System.currentTimeMillis() - current.lastFailTimestamp
            val days = TimeUnit.MILLISECONDS.toDays(diff).toInt()
            if (days != current.daysClean) {
                saveStats(current.copy(daysClean = days))
            }
        }
    }

    fun getMotivationalMessage(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour in 22..23 || hour in 0..4 -> "A noite é longa, mas sua força é maior. Não desista agora!"
            hour in 5..8 -> "Comece o dia com foco total. Um novo dia, uma nova vitória!"
            hour in 12..14 -> "Pausa para o descanso, mas mantenha a guarda alta."
            else -> "Você é dono do seu destino. Cada segundo conta!"
        }
    }
}
