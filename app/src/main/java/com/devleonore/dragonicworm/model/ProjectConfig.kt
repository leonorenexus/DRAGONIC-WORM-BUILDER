package com.devleonore.dragonicworm.model

import java.io.Serializable

/**
 * Holds all configuration entered by the user when creating a project.
 * Mirrors the form fields in Create Project screen.
 */
data class ProjectConfig(
    var projectName: String = "",
    var author: String = "Dev Leonore",
    var version: String = "1.0.0",
    var description: String = "",
    var license: String = "MIT",
    var bannerColor: String = "#FF0033",
    var outputName: String = "",
    var outputFolder: String = "",
    var language: Language = Language.PYTHON,
    var template: TemplateType = TemplateType.BASIC_TOOL,
    var menuItems: MutableList<MenuItem> = mutableListOf(
        MenuItem("Option One", "echo 'Running option 1'"),
        MenuItem("Option Two", "echo 'Running option 2'"),
        MenuItem("Option Three", "echo 'Running option 3'")
    ),
    var depPkg: String = "",
    var depPip: String = "",
    var envVars: String = "",
    var includeReadme: Boolean = true,
    var includeRequirements: Boolean = true,
    var includeInstall: Boolean = true,
    var includeRun: Boolean = true
) : Serializable {

    fun resolvedOutputName(): String =
        outputName.trim().ifEmpty {
            projectName.trim().replace(Regex("\\s+"), "_").lowercase()
        }.ifEmpty { "dragonic_project" }
}

data class MenuItem(
    var name: String = "",
    var command: String = ""
) : Serializable

enum class Language(val label: String) {
    PYTHON("Python"),
    BASH("Bash")
}

enum class License(val label: String) {
    MIT("MIT"),
    APACHE2("Apache2"),
    GPL3("GPL3"),
    CUSTOM("Custom")
}

enum class TemplateType(
    val displayName: String,
    val icon: String,
    val description: String,
    val supportedLanguages: List<Language>
) {
    BASIC_TOOL("Basic Tool", "🔧", "Simple CLI tool structure", listOf(Language.PYTHON, Language.BASH)),
    MENU_TOOL("Menu Tool", "📋", "Interactive numbered menu", listOf(Language.PYTHON)),
    MULTI_MENU("Multi Menu", "🗂️", "Multi-level menu system", listOf(Language.PYTHON)),
    API_TOOL("API Tool", "🌐", "REST API integration tool", listOf(Language.PYTHON)),
    TELEGRAM_BOT("Telegram Bot", "🤖", "Telegram bot framework", listOf(Language.PYTHON)),
    WEB_SCRAPER("Web Scraper", "🕷️", "Web scraping tool", listOf(Language.PYTHON)),
    UTILITY_TOOL("Utility Tool", "⚙️", "General utility collection", listOf(Language.PYTHON, Language.BASH)),
    OSINT_TOOL("OSINT Tool", "🔍", "Open-source intelligence", listOf(Language.PYTHON)),
    INSTALLER_TOOL("Installer Tool", "📦", "Package installer script", listOf(Language.BASH)),
    NETWORK_TOOL("Network Tool", "🌐", "Network scanning & analysis", listOf(Language.BASH));

    val usesMenu: Boolean
        get() = this == MENU_TOOL || this == MULTI_MENU

    val isTelegram: Boolean
        get() = this == TELEGRAM_BOT

    companion object {
        fun forLanguage(lang: Language): List<TemplateType> =
            entries.filter { lang in it.supportedLanguages }
    }
}

/** A lightweight record of a generated project, kept in memory for "Recent Generate" list. */
data class RecentProject(
    val name: String,
    val template: String,
    val language: String,
    val time: String
) : Serializable
