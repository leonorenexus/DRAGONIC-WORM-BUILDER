package com.devleonore.dragonicworm.template

import com.devleonore.dragonicworm.model.Language
import com.devleonore.dragonicworm.model.ProjectConfig

/**
 * Core engine step: copy template → replace placeholders → build structure.
 * Pure string substitution, no AI, fully offline/local.
 */
object TemplateProcessor {

    fun process(rawContent: String, cfg: ProjectConfig, readmeContent: String): String {
        var out = rawContent

        out = out.replace("{{PROJECT_NAME}}", cfg.projectName.ifBlank { "MyProject" })
        out = out.replace("{{AUTHOR}}", cfg.author.ifBlank { "Dev Leonore" })
        out = out.replace("{{VERSION}}", cfg.version.ifBlank { "1.0.0" })
        out = out.replace("{{DESCRIPTION}}", cfg.description.ifBlank { "A Termux tool." })
        out = out.replace("{{LICENSE}}", cfg.license)
        out = out.replace(
            "{{DEPENDENCIES_PIP}}",
            cfg.depPip.replace(",", "\n").ifBlank { "# no pip deps" }
        )
        out = out.replace(
            "{{DEPENDENCIES_PKG}}",
            cfg.depPkg.replace(",", " ")
        )

        out = out.replace("{{BANNER_CODE}}", BannerBuilder.pythonBanner(cfg.projectName.ifBlank { "PROJECT" }))
        out = out.replace("{{BASH_BANNER}}", BannerBuilder.bashBanner(cfg.projectName.ifBlank { "PROJECT" }))

        if (cfg.template.usesMenu) {
            out = out.replace("{{MENU_FUNCTIONS}}", MenuBuilder.functions(cfg.menuItems))
            out = out.replace("{{MENU_ITEMS}}", MenuBuilder.menuItems(cfg.menuItems))
            out = out.replace("{{MENU_CHOICES}}", MenuBuilder.menuChoices(cfg.menuItems))
        }

        if (cfg.template.isTelegram) {
            out = out.replace("{{TELEGRAM_COMMANDS}}", MenuBuilder.telegramCommands(cfg.menuItems))
        }

        out = out.replace("{{README_CONTENT}}", readmeContent)
        out = out.replace("{{LICENSE_CONTENT}}", LicenseGenerator.generate(cfg.license, cfg.author))

        return out
    }

    /**
     * Builds the full set of output files (relative path -> final content)
     * for the given config, applying include-file toggles.
     */
    fun buildProjectFiles(cfg: ProjectConfig): LinkedHashMap<String, String> {
        val templateFiles = TemplateBank.filesFor(cfg.template, cfg.language)
        val readme = ReadmeGenerator.generate(cfg)
        val result = LinkedHashMap<String, String>()

        for ((path, raw) in templateFiles) {
            if (!cfg.includeReadme && path == "README.md") continue
            if (!cfg.includeRequirements && path == "requirements.txt") continue
            if (!cfg.includeInstall && path == "install.sh") continue
            if (!cfg.includeRun && path == "run.sh") continue
            result[path] = process(raw, cfg, readme)
        }
        return result
    }
}
