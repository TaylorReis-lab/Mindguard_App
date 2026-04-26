package com.mindguard.app.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.mindguard.app.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MindguardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MindguardRepository(application)
    val allEvents: StateFlow<List<BlockEvent>> = repository.allEvents
    val userStats: StateFlow<UserStats> = repository.userStats
    val motivationalMessage: String = repository.getMotivationalMessage()
    val customBlocklist: StateFlow<List<String>> = repository.customBlocklist
    val language: StateFlow<String> = repository.language
    val notificationsEnabled: StateFlow<Boolean> = repository.notificationsEnabled

    private val _isVpnActive = MutableStateFlow(false)
    val isVpnActive: StateFlow<Boolean> = _isVpnActive

    init {
        repository.updateDaysClean()
    }

    fun toggleNotifications() {
        repository.toggleNotifications()
    }

    fun toggleVpn(active: Boolean) {
        _isVpnActive.value = active
    }

    fun addCustomSite(url: String) {
        repository.addCustomSite(url)
    }

    fun removeCustomSite(url: String) {
        repository.removeCustomSite(url)
    }

    fun setLanguage(lang: String) {
        repository.setLanguage(lang)
    }

    fun recordFail() {
        repository.recordFail()
    }

    fun simulateBlock() {
        val urls = listOf("exemplo-adulto.com", "ads-tracker.net", "social-tracking.com")
        val types = listOf("ADULT", "AD", "TRACKER")
        val index = (0..2).random()
        repository.recordBlock(urls[index], types[index])
    }
}
