package com.mindguard.app.data

data class BlockEvent(
    val id: Long = System.currentTimeMillis(),
    val url: String,
    val type: String, // "AD", "TRACKER", "ADULT"
    val timestamp: Long = System.currentTimeMillis()
)

data class UserStats(
    val daysClean: Int = 0,
    val totalFailedAttempts: Int = 0,
    val totalBlockedAttempts: Int = 0, // Specifically for Adult
    val adsBlocked: Int = 0,
    val trackersBlocked: Int = 0,
    val lastFailTimestamp: Long = 0
)
