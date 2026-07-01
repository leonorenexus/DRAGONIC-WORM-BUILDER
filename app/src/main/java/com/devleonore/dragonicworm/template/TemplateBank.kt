package com.devleonore.dragonicworm.template

import com.devleonore.dragonicworm.model.Language
import com.devleonore.dragonicworm.model.TemplateType

/**
 * Static template bank. 100% template-based — no AI, no network.
 * Each entry maps a relative file path inside the generated project
 * to its raw content containing {{PLACEHOLDER}} tokens.
 */
object TemplateBank {

    fun filesFor(template: TemplateType, language: Language): LinkedHashMap<String, String> {
        val key = templateKey(template)
        return when (language) {
            Language.PYTHON -> pythonTemplates[key] ?: pythonTemplates["basic"]!!
            Language.BASH -> bashTemplates[key] ?: bashTemplates["basic"]!!
        }
    }

    private fun templateKey(template: TemplateType): String = when (template) {
        TemplateType.BASIC_TOOL -> "basic"
        TemplateType.MENU_TOOL, TemplateType.MULTI_MENU -> "menu"
        TemplateType.TELEGRAM_BOT -> "telegram"
        TemplateType.OSINT_TOOL -> "osint"
        TemplateType.API_TOOL -> "basic"
        TemplateType.WEB_SCRAPER -> "basic"
        TemplateType.UTILITY_TOOL -> "basic"
        TemplateType.INSTALLER_TOOL -> "installer"
        TemplateType.NETWORK_TOOL -> "network"
    }

    // ───────────────────────── PYTHON ─────────────────────────

    private val pythonBasic = linkedMapOf(
        "main.py" to """
            |#!/usr/bin/env python3
            |# -*- coding: utf-8 -*-
            |"""
            |{{PROJECT_NAME}}
            |Author  : {{AUTHOR}}
            |Version : {{VERSION}}
            |License : {{LICENSE}}
            |"""
            |
            |import os
            |import sys
            |
            |{{BANNER_CODE}}
            |
            |def main():
            |    banner()
            |    print("\033[1;32m[+] {{PROJECT_NAME}} v{{VERSION}}\033[0m")
            |    print("\033[0;37m{{DESCRIPTION}}\033[0m\n")
            |    run()
            |
            |def run():
            |    pass
            |
            |if __name__ == "__main__":
            |    try:
            |        main()
            |    except KeyboardInterrupt:
            |        print("\n\033[1;31m[!] Interrupted by user.\033[0m")
            |        sys.exit(0)
        """.trimMargin(),
        "requirements.txt" to "{{DEPENDENCIES_PIP}}",
        "install.sh" to """
            |#!/bin/bash
            |# Install Script for {{PROJECT_NAME}}
            |# Author: {{AUTHOR}}
            |
            |echo "[*] Installing {{PROJECT_NAME}}..."
            |pkg update -y && pkg upgrade -y
            |pkg install -y python {{DEPENDENCIES_PKG}}
            |pip install {{DEPENDENCIES_PIP}}
            |echo "[+] Done! Run: bash run.sh"
        """.trimMargin(),
        "run.sh" to "#!/bin/bash\npython3 main.py\n",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.json" to """
            |{
            |  "name": "{{PROJECT_NAME}}",
            |  "author": "{{AUTHOR}}",
            |  "version": "{{VERSION}}",
            |  "description": "{{DESCRIPTION}}"
            |}
        """.trimMargin(),
        "modules/__init__.py" to "# Modules for {{PROJECT_NAME}}",
        "assets/.gitkeep" to ""
    )

    private val pythonMenu = linkedMapOf(
        "main.py" to """
            |#!/usr/bin/env python3
            |# -*- coding: utf-8 -*-
            |"""
            |{{PROJECT_NAME}}
            |Author  : {{AUTHOR}}
            |Version : {{VERSION}}
            |License : {{LICENSE}}
            |"""
            |
            |import os
            |import sys
            |import time
            |
            |{{BANNER_CODE}}
            |
            |def clear():
            |    os.system("clear" if os.name == "posix" else "cls")
            |
            |{{MENU_FUNCTIONS}}
            |
            |def menu():
            |    banner()
            |    print("\033[1;31m╔══════════════════════════════╗\033[0m")
            |    print("\033[1;31m║\033[0m  \033[1;37m{{PROJECT_NAME}}\033[0m")
            |    print("\033[1;31m╠══════════════════════════════╣\033[0m")
            |{{MENU_ITEMS}}
            |    print("\033[1;31m╠══════════════════════════════╣\033[0m")
            |    print("\033[1;31m║\033[0m  \033[1;31m[0]\033[0m \033[0;37mExit\033[0m")
            |    print("\033[1;31m╚══════════════════════════════╝\033[0m")
            |
            |def main():
            |    while True:
            |        clear()
            |        menu()
            |        choice = input("\n\033[1;31m[?] Select option: \033[0m").strip()
            |{{MENU_CHOICES}}
            |        elif choice == "0":
            |            print("\n\033[1;31m[!] Exiting...\033[0m")
            |            sys.exit(0)
            |        else:
            |            print("\033[1;33m[!] Invalid option.\033[0m")
            |            time.sleep(1)
            |
            |if __name__ == "__main__":
            |    try:
            |        main()
            |    except KeyboardInterrupt:
            |        print("\n\033[1;31m[!] Interrupted.\033[0m")
            |        sys.exit(0)
        """.trimMargin(),
        "requirements.txt" to "{{DEPENDENCIES_PIP}}",
        "install.sh" to """
            |#!/bin/bash
            |echo "[*] Installing {{PROJECT_NAME}}..."
            |pkg update -y && pkg install -y python {{DEPENDENCIES_PKG}}
            |pip install {{DEPENDENCIES_PIP}}
            |echo "[+] Done! Run: bash run.sh"
        """.trimMargin(),
        "run.sh" to "#!/bin/bash\npython3 main.py",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.json" to """{"name":"{{PROJECT_NAME}}","author":"{{AUTHOR}}","version":"{{VERSION}}"}""",
        "modules/__init__.py" to "# {{PROJECT_NAME}} modules",
        "assets/.gitkeep" to ""
    )

    private val pythonTelegram = linkedMapOf(
        "main.py" to """
            |#!/usr/bin/env python3
            |"""
            |{{PROJECT_NAME}} - Telegram Bot
            |Author  : {{AUTHOR}}
            |Version : {{VERSION}}
            |"""
            |
            |import os
            |import logging
            |from telegram import Update
            |from telegram.ext import (
            |    Application, CommandHandler, MessageHandler,
            |    filters, ContextTypes
            |)
            |
            |BOT_TOKEN = os.getenv("BOT_TOKEN", "YOUR_BOT_TOKEN_HERE")
            |
            |logging.basicConfig(
            |    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
            |    level=logging.INFO
            |)
            |logger = logging.getLogger(__name__)
            |
            |async def start(update: Update, context: ContextTypes.DEFAULT_TYPE):
            |    await update.message.reply_text(
            |        "{{PROJECT_NAME}}\nv{{VERSION}} by {{AUTHOR}}\n\n{{DESCRIPTION}}"
            |    )
            |
            |async def help_cmd(update: Update, context: ContextTypes.DEFAULT_TYPE):
            |    help_text = "Available commands:\n"
            |{{TELEGRAM_COMMANDS}}
            |    await update.message.reply_text(help_text)
            |
            |async def unknown(update: Update, context: ContextTypes.DEFAULT_TYPE):
            |    await update.message.reply_text("Unknown command. Use /help")
            |
            |def main():
            |    app = Application.builder().token(BOT_TOKEN).build()
            |    app.add_handler(CommandHandler("start", start))
            |    app.add_handler(CommandHandler("help", help_cmd))
            |    app.add_handler(MessageHandler(filters.COMMAND, unknown))
            |    logger.info("{{PROJECT_NAME}} Bot running...")
            |    app.run_polling(allowed_updates=Update.ALL_TYPES)
            |
            |if __name__ == "__main__":
            |    main()
        """.trimMargin(),
        "requirements.txt" to "python-telegram-bot>=20.0\n{{DEPENDENCIES_PIP}}",
        "install.sh" to """
            |#!/bin/bash
            |echo "[*] Installing {{PROJECT_NAME}} Bot..."
            |pkg update -y && pkg install -y python {{DEPENDENCIES_PKG}}
            |pip install python-telegram-bot {{DEPENDENCIES_PIP}}
            |echo "[+] Set your BOT_TOKEN in .env"
            |echo "[+] Run: bash run.sh"
        """.trimMargin(),
        "run.sh" to "#!/bin/bash\nexport BOT_TOKEN=\"YOUR_TOKEN_HERE\"\npython3 main.py",
        ".env.example" to "BOT_TOKEN=your_telegram_bot_token_here",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.json" to """{"name":"{{PROJECT_NAME}}","author":"{{AUTHOR}}","version":"{{VERSION}}"}""",
        "modules/__init__.py" to "# {{PROJECT_NAME}} modules",
        "assets/.gitkeep" to ""
    )

    private val pythonOsint = linkedMapOf(
        "main.py" to """
            |#!/usr/bin/env python3
            |"""
            |{{PROJECT_NAME}} - OSINT Tool
            |Author  : {{AUTHOR}}
            |Version : {{VERSION}}
            |"""
            |
            |import sys
            |import json
            |
            |{{BANNER_CODE}}
            |
            |def osint_lookup(target):
            |    print(f"[*] Looking up: {target}")
            |    results = {"target": target, "status": "analyzed"}
            |    return results
            |
            |def main():
            |    banner()
            |    if len(sys.argv) < 2:
            |        print("[!] Usage: python3 main.py <target>")
            |        sys.exit(1)
            |    target = sys.argv[1]
            |    results = osint_lookup(target)
            |    print(json.dumps(results, indent=2))
            |
            |if __name__ == "__main__":
            |    main()
        """.trimMargin(),
        "requirements.txt" to "requests\n{{DEPENDENCIES_PIP}}",
        "install.sh" to """
            |#!/bin/bash
            |pkg update -y && pkg install -y python {{DEPENDENCIES_PKG}}
            |pip install requests {{DEPENDENCIES_PIP}}
            |echo "[+] Done! Run: python3 main.py <target>"
        """.trimMargin(),
        "run.sh" to "#!/bin/bash\npython3 main.py \$1",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "modules/__init__.py" to "# OSINT modules",
        "assets/.gitkeep" to "",
        "config/config.json" to """{"name":"{{PROJECT_NAME}}","author":"{{AUTHOR}}","version":"{{VERSION}}"}"""
    )

    private val pythonTemplates = mapOf(
        "basic" to pythonBasic,
        "menu" to pythonMenu,
        "telegram" to pythonTelegram,
        "osint" to pythonOsint
    )

    // ───────────────────────── BASH ─────────────────────────

    private val bashBasic = linkedMapOf(
        "main.sh" to """
            |#!/bin/bash
            |# {{PROJECT_NAME}}
            |# Author  : {{AUTHOR}}
            |# Version : {{VERSION}}
            |# License : {{LICENSE}}
            |# Desc    : {{DESCRIPTION}}
            |
            |{{BASH_BANNER}}
            |
            |function main() {
            |    banner
            |    echo -e "\e[1;32m[+] {{PROJECT_NAME}} v{{VERSION}}\e[0m"
            |    echo -e "\e[0;37m{{DESCRIPTION}}\e[0m"
            |}
            |
            |main "${'$'}@"
        """.trimMargin(),
        "install.sh" to """
            |#!/bin/bash
            |echo "[*] Installing {{PROJECT_NAME}}..."
            |pkg update -y && pkg upgrade -y
            |pkg install -y {{DEPENDENCIES_PKG}}
            |chmod +x main.sh run.sh
            |echo "[+] Done! Run: bash run.sh"
        """.trimMargin(),
        "run.sh" to "#!/bin/bash\nbash main.sh \"\$@\"",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.sh" to "#!/bin/bash\nPROJECT_NAME=\"{{PROJECT_NAME}}\"\nAUTHOR=\"{{AUTHOR}}\"\nVERSION=\"{{VERSION}}\"\n",
        "modules/.gitkeep" to "",
        "assets/.gitkeep" to ""
    )

    private val bashInstaller = linkedMapOf(
        "main.sh" to """
            |#!/bin/bash
            |# {{PROJECT_NAME}} - Installer Tool
            |# Author: {{AUTHOR}} | Version: {{VERSION}}
            |
            |RED='\e[1;31m' GRN='\e[1;32m' YLW='\e[1;33m' NC='\e[0m'
            |
            |{{BASH_BANNER}}
            |
            |check_root() {
            |    if [ "${'$'}EUID" -ne 0 ]; then
            |        echo -e "${'$'}{YLW}[!] Running without root${'$'}{NC}"
            |    fi
            |}
            |
            |install_deps() {
            |    echo -e "${'$'}{GRN}[*] Installing dependencies...${'$'}{NC}"
            |    pkg update -y && pkg upgrade -y
            |    pkg install -y {{DEPENDENCIES_PKG}}
            |    echo -e "${'$'}{GRN}[+] Dependencies installed!${'$'}{NC}"
            |}
            |
            |main() {
            |    banner
            |    check_root
            |    install_deps
            |    echo -e "${'$'}{GRN}[+] {{PROJECT_NAME}} setup complete!${'$'}{NC}"
            |}
            |
            |main "${'$'}@"
        """.trimMargin(),
        "install.sh" to "#!/bin/bash\nbash main.sh",
        "run.sh" to "#!/bin/bash\nbash main.sh \"\$@\"",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.sh" to "#!/bin/bash\nPROJECT_NAME=\"{{PROJECT_NAME}}\"\nAUTHOR=\"{{AUTHOR}}\"\nVERSION=\"{{VERSION}}\"\n",
        "assets/.gitkeep" to ""
    )

    private val bashNetwork = linkedMapOf(
        "main.sh" to """
            |#!/bin/bash
            |# {{PROJECT_NAME}} - Network Tool
            |# Author: {{AUTHOR}} | Version: {{VERSION}}
            |
            |RED='\e[1;31m' GRN='\e[1;32m' CYN='\e[1;36m' NC='\e[0m'
            |
            |{{BASH_BANNER}}
            |
            |scan_host() {
            |    local host=${'$'}1
            |    echo -e "${'$'}{CYN}[*] Scanning: ${'$'}host${'$'}{NC}"
            |    nmap -sV --open "${'$'}host" 2>/dev/null || echo "[!] nmap not found, install with: pkg install nmap"
            |}
            |
            |ping_test() {
            |    local host=${'$'}1
            |    echo -e "${'$'}{CYN}[*] Pinging: ${'$'}host${'$'}{NC}"
            |    ping -c 4 "${'$'}host"
            |}
            |
            |main() {
            |    banner
            |    echo -e "${'$'}{GRN}{{PROJECT_NAME}} v{{VERSION}}${'$'}{NC}"
            |    if [ -z "${'$'}1" ]; then
            |        echo "Usage: bash main.sh <host>"
            |        exit 1
            |    fi
            |    scan_host "${'$'}1"
            |}
            |
            |main "${'$'}@"
        """.trimMargin(),
        "install.sh" to """
            |#!/bin/bash
            |pkg update -y && pkg install -y nmap {{DEPENDENCIES_PKG}}
            |chmod +x main.sh run.sh
            |echo "[+] Done!"
        """.trimMargin(),
        "run.sh" to "#!/bin/bash\nbash main.sh \"\$@\"",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.sh" to "#!/bin/bash\nPROJECT_NAME=\"{{PROJECT_NAME}}\"\nAUTHOR=\"{{AUTHOR}}\"\nVERSION=\"{{VERSION}}\"",
        "assets/.gitkeep" to ""
    )

    private val bashTemplates = mapOf(
        "basic" to bashBasic,
        "installer" to bashInstaller,
        "network" to bashNetwork
    )
}
