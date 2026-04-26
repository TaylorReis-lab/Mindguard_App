package com.mindguard.app.data

object BlocklistProvider {
    // A sample list of common adult keywords and top domains
    // In a production app, this would be a multi-megabyte list or an API call
    private val adultKeywords = listOf(
        "porn", "xvideos", "pornhub", "redtube", "xnxx", "xhamster", 
        "chaturbate", "onlyfans", "sex", "adult", "hentai", "brazzers",
        "cam4", "livejasmin", "stripchat", "bongacams", "youporn", 
        "spankbang", "eporner", "beeg", "txxx", "yourporn", "vporn",
        "tube8", "thumbzilla", "motherless", "redgifs", "hqporner",
        "tnaflix", "youjizz", "pornmd", "drtuber", "fapvid", "fuq"
    )

    fun isAdultContent(url: String): Boolean {
        val lowerUrl = url.lowercase()
        return adultKeywords.any { keyword -> lowerUrl.contains(keyword) }
    }
    
    // Total count simulation
    const val TOTAL_MASTER_BLOCKLIST_COUNT = "2.4M+"
}
