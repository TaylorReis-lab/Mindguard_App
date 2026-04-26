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
            "nav.activity" to "Histórico",
            "nav.lista" to "Lista",
            "nav.profile" to "Perfil",
            "nav.settings" to "Configurações",
            "nav.privacy" to "Privacidade",
            "nav.about" to "Sobre",
            "home.blocks" to "Bloqueios",
            "home.attempts" to "Tentativas",
            "home.secure" to "Seguro",
            "home.daysProtected" to "dias protegidos",
            "home.detailActivity" to "Ver Atividade Detalhada",
            "home.resetProgress" to "Zerar meu progresso (Eu falhei)",
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
            "general.inactive" to "Desativado"
        ),
        "en" to mapOf(
            "app.name" to "MindGuard Block",
            "app.slogan" to "Your digital protection",
            "nav.home" to "Home",
            "nav.activity" to "History",
            "nav.lista" to "List",
            "nav.profile" to "Profile",
            "nav.settings" to "Settings",
            "nav.privacy" to "Privacy",
            "nav.about" to "About",
            "home.blocks" to "Blocks",
            "home.attempts" to "Attempts",
            "home.secure" to "Secure",
            "home.daysProtected" to "days protected",
            "home.detailActivity" to "View Detailed Activity",
            "home.resetProgress" to "Reset my progress (I failed)",
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
            "about.taylor" to "Surgical adult blocking engine: BlockPorn.",
            "auth.login" to "Sign in with Google or Email",
            "general.active" to "Active",
            "general.inactive" to "Inactive"
        )
    )

    fun get(lang: String, key: String): String {
        return data[lang]?.get(key) ?: data["en"]?.get(key) ?: key
    }
}

data class LanguageInfo(val code: String, val name: String, val flag: String)
