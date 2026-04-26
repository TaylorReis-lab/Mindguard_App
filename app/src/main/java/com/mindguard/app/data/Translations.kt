package com.mindguard.app.data

object Translations {
    val LANGUAGES = listOf(
        LanguageInfo("pt-BR", "Português (Brasil)", "🇧🇷"),
        LanguageInfo("en", "English (US)", "🇺🇸"),
        LanguageInfo("ru", "Русский", "🇷🇺"),
        LanguageInfo("es", "Español", "🇪🇸")
    )

    private val data = mapOf(
        "pt-BR" to mapOf(
            "app.name" to "MindGuard Block",
            "app.slogan" to "Sua proteção digital",
            "nav.home" to "Início",
            "nav.activity" to "Atividade",
            "nav.profile" to "Perfil",
            "nav.settings" to "Config",
            "profile.editProfile" to "Editar Perfil",
            "profile.language" to "Idioma",
            "profile.security" to "Segurança",
            "profile.privacy" to "Privacidade",
            "profile.notifications" to "Notificações",
            "profile.connected" to "Contas Conectadas",
            "profile.logout" to "Sair da Conta",
            "profile.version" to "Versão",
            "profile.developed" to "Desenvolvido por Taylor Reis",
            "about.adguard" to "Bloqueio de anúncios provido por AdGuard DNS.",
            "about.taylor" to "Motor de bloqueio adulto cirúrgico: BlockPorn.",
            "auth.login" to "Entrar com Google ou Email",
            "general.active" to "Ativado",
            "general.inactive" to "Desativado",
            "home.sitesBlocked" to "Sites Bloqueados",
            "home.adsBlocked" to "Anúncios",
            "home.trackersBlocked" to "Rastreadores"
        ),
        "en" to mapOf(
            "app.name" to "MindGuard Block",
            "app.slogan" to "Your digital protection",
            "nav.home" to "Home",
            "nav.activity" to "Activity",
            "nav.profile" to "Profile",
            "nav.settings" to "Settings",
            "profile.editProfile" to "Edit Profile",
            "profile.language" to "Language",
            "profile.security" to "Security",
            "profile.privacy" to "Privacy",
            "profile.notifications" to "Notifications",
            "profile.connected" to "Connected Accounts",
            "profile.logout" to "Sign Out",
            "profile.version" to "Version",
            "profile.developed" to "Developed by Taylor Reis",
            "about.adguard" to "Ad blocking powered by AdGuard DNS.",
            "about.taylor" to "Surgical adult blocking engine developed by Taylor Reis.",
            "auth.login" to "Sign in with Google or Email",
            "general.active" to "Active",
            "general.inactive" to "Inactive",
            "home.sitesBlocked" to "Sites Blocked",
            "home.adsBlocked" to "Ads",
            "home.trackersBlocked" to "Trackers"
        ),
        "ru" to mapOf(
            "nav.home" to "Главная",
            "nav.activity" to "Активность",
            "nav.profile" to "Профиль",
            "profile.logout" to "Выйти"
            // Simplified for brevity, in real app add all keys
        ),
        "es" to mapOf(
            "nav.home" to "Inicio",
            "nav.activity" to "Actividad",
            "nav.profile" to "Perfil",
            "profile.logout" to "Cerrar Sesión"
        )
    )

    fun get(lang: String, key: String): String {
        return data[lang]?.get(key) ?: data["en"]?.get(key) ?: key
    }
}

data class LanguageInfo(val code: String, val name: String, val flag: String)
