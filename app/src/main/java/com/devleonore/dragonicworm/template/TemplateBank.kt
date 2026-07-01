package com.devleonore.dragonicworm.template

import com.devleonore.dragonicworm.model.Language
import com.devleonore.dragonicworm.model.TemplateType

/**
 * Static template bank. 100% template-based — no AI, no network.
 */
object TemplateBank {

    fun filesFor(template: TemplateType, language: Language): LinkedHashMap<String, String> {
        val key = templateKey(template)
        return when (language) {
            Language.PYTHON -> pythonTemplates[key] ?: pythonTemplates["basic"]!!
            Language.BASH   -> bashTemplates[key]   ?: bashTemplates["basic"]!!
        }
    }

    private fun templateKey(template: TemplateType): String = when (template) {
        TemplateType.BASIC_TOOL    -> "basic"
        TemplateType.MENU_TOOL,
        TemplateType.MULTI_MENU   -> "menu"
        TemplateType.TELEGRAM_BOT  -> "telegram"
        TemplateType.OSINT_TOOL    -> "osint"
        TemplateType.API_TOOL      -> "basic"
        TemplateType.WEB_SCRAPER   -> "basic"
        TemplateType.UTILITY_TOOL  -> "basic"
        TemplateType.INSTALLER_TOOL -> "installer"
        TemplateType.NETWORK_TOOL  -> "network"
    }

    // ─── PYTHON BASIC ────────────────────────────────────────────────────────

    private val pythonBasic = linkedMapOf(
        "main.py" to (
            "#!/usr/bin/env python3\n" +
            "# -*- coding: utf-8 -*-\n" +
            "# {{PROJECT_NAME}}\n" +
            "# Author  : {{AUTHOR}}\n" +
            "# Version : {{VERSION}}\n" +
            "# License : {{LICENSE}}\n\n" +
            "import os\n" +
            "import sys\n\n" +
            "{{BANNER_CODE}}\n\n" +
            "def main():\n" +
            "    banner()\n" +
            "    print('\\033[1;32m[+] {{PROJECT_NAME}} v{{VERSION}}\\033[0m')\n" +
            "    print('\\033[0;37m{{DESCRIPTION}}\\033[0m\\n')\n" +
            "    run()\n\n" +
            "def run():\n" +
            "    pass\n\n" +
            "if __name__ == '__main__':\n" +
            "    try:\n" +
            "        main()\n" +
            "    except KeyboardInterrupt:\n" +
            "        print('\\n\\033[1;31m[!] Interrupted by user.\\033[0m')\n" +
            "        sys.exit(0)\n"
        ),
        "requirements.txt" to "{{DEPENDENCIES_PIP}}\n",
        "install.sh" to (
            "#!/bin/bash\n" +
            "# Install Script for {{PROJECT_NAME}}\n" +
            "# Author: {{AUTHOR}}\n\n" +
            "echo '[*] Installing {{PROJECT_NAME}}...'\n" +
            "pkg update -y && pkg upgrade -y\n" +
            "pkg install -y python {{DEPENDENCIES_PKG}}\n" +
            "pip install {{DEPENDENCIES_PIP}}\n" +
            "echo '[+] Done! Run: bash run.sh'\n"
        ),
        "run.sh" to "#!/bin/bash\npython3 main.py\n",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.json" to (
            "{\n" +
            "  \"name\": \"{{PROJECT_NAME}}\",\n" +
            "  \"author\": \"{{AUTHOR}}\",\n" +
            "  \"version\": \"{{VERSION}}\",\n" +
            "  \"description\": \"{{DESCRIPTION}}\"\n" +
            "}\n"
        ),
        "modules/__init__.py" to "# Modules for {{PROJECT_NAME}}\n",
        "assets/.gitkeep" to ""
    )

    // ─── PYTHON MENU ─────────────────────────────────────────────────────────

    private val pythonMenu = linkedMapOf(
        "main.py" to (
            "#!/usr/bin/env python3\n" +
            "# -*- coding: utf-8 -*-\n" +
            "# {{PROJECT_NAME}}\n" +
            "# Author  : {{AUTHOR}}\n" +
            "# Version : {{VERSION}}\n\n" +
            "import os\n" +
            "import sys\n" +
            "import time\n\n" +
            "{{BANNER_CODE}}\n\n" +
            "def clear():\n" +
            "    os.system('clear' if os.name == 'posix' else 'cls')\n\n" +
            "{{MENU_FUNCTIONS}}\n\n" +
            "def menu():\n" +
            "    banner()\n" +
            "    print('\\033[1;31m\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557\\033[0m')\n" +
            "    print('\\033[1;31m\u2551\\033[0m  \\033[1;37m{{PROJECT_NAME}}\\033[0m')\n" +
            "    print('\\033[1;31m\u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563\\033[0m')\n" +
            "{{MENU_ITEMS}}\n" +
            "    print('\\033[1;31m\u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563\\033[0m')\n" +
            "    print('\\033[1;31m\u2551\\033[0m  \\033[1;31m[0]\\033[0m \\033[0;37mExit\\033[0m')\n" +
            "    print('\\033[1;31m\u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d\\033[0m')\n\n" +
            "def main():\n" +
            "    while True:\n" +
            "        clear()\n" +
            "        menu()\n" +
            "        choice = input('\\n\\033[1;31m[?] Select option: \\033[0m').strip()\n" +
            "{{MENU_CHOICES}}\n" +
            "        elif choice == '0':\n" +
            "            print('\\n\\033[1;31m[!] Exiting...\\033[0m')\n" +
            "            sys.exit(0)\n" +
            "        else:\n" +
            "            print('\\033[1;33m[!] Invalid option.\\033[0m')\n" +
            "            time.sleep(1)\n\n" +
            "if __name__ == '__main__':\n" +
            "    try:\n" +
            "        main()\n" +
            "    except KeyboardInterrupt:\n" +
            "        print('\\n\\033[1;31m[!] Interrupted.\\033[0m')\n" +
            "        sys.exit(0)\n"
        ),
        "requirements.txt" to "{{DEPENDENCIES_PIP}}\n",
        "install.sh" to (
            "#!/bin/bash\n" +
            "echo '[*] Installing {{PROJECT_NAME}}...'\n" +
            "pkg update -y && pkg install -y python {{DEPENDENCIES_PKG}}\n" +
            "pip install {{DEPENDENCIES_PIP}}\n" +
            "echo '[+] Done! Run: bash run.sh'\n"
        ),
        "run.sh" to "#!/bin/bash\npython3 main.py\n",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.json" to "{\"name\":\"{{PROJECT_NAME}}\",\"author\":\"{{AUTHOR}}\",\"version\":\"{{VERSION}}\"}\n",
        "modules/__init__.py" to "# {{PROJECT_NAME}} modules\n",
        "assets/.gitkeep" to ""
    )

    // ─── PYTHON TELEGRAM ─────────────────────────────────────────────────────

    private val pythonTelegram = linkedMapOf(
        "main.py" to (
            "#!/usr/bin/env python3\n" +
            "# {{PROJECT_NAME}} - Telegram Bot\n" +
            "# Author  : {{AUTHOR}}\n" +
            "# Version : {{VERSION}}\n\n" +
            "import os\n" +
            "import logging\n" +
            "from telegram import Update\n" +
            "from telegram.ext import (\n" +
            "    Application, CommandHandler, MessageHandler,\n" +
            "    filters, ContextTypes\n" +
            ")\n\n" +
            "BOT_TOKEN = os.getenv('BOT_TOKEN', 'YOUR_BOT_TOKEN_HERE')\n\n" +
            "logging.basicConfig(\n" +
            "    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',\n" +
            "    level=logging.INFO\n" +
            ")\n" +
            "logger = logging.getLogger(__name__)\n\n" +
            "async def start(update: Update, context: ContextTypes.DEFAULT_TYPE):\n" +
            "    await update.message.reply_text(\n" +
            "        '{{PROJECT_NAME}}\\nv{{VERSION}} by {{AUTHOR}}\\n\\n{{DESCRIPTION}}'\n" +
            "    )\n\n" +
            "async def help_cmd(update: Update, context: ContextTypes.DEFAULT_TYPE):\n" +
            "    help_text = 'Available commands:\\n'\n" +
            "{{TELEGRAM_COMMANDS}}\n" +
            "    await update.message.reply_text(help_text)\n\n" +
            "async def unknown(update: Update, context: ContextTypes.DEFAULT_TYPE):\n" +
            "    await update.message.reply_text('Unknown command. Use /help')\n\n" +
            "def main():\n" +
            "    app = Application.builder().token(BOT_TOKEN).build()\n" +
            "    app.add_handler(CommandHandler('start', start))\n" +
            "    app.add_handler(CommandHandler('help', help_cmd))\n" +
            "    app.add_handler(MessageHandler(filters.COMMAND, unknown))\n" +
            "    logger.info('{{PROJECT_NAME}} Bot running...')\n" +
            "    app.run_polling(allowed_updates=Update.ALL_TYPES)\n\n" +
            "if __name__ == '__main__':\n" +
            "    main()\n"
        ),
        "requirements.txt" to "python-telegram-bot>=20.0\n{{DEPENDENCIES_PIP}}\n",
        "install.sh" to (
            "#!/bin/bash\n" +
            "echo '[*] Installing {{PROJECT_NAME}} Bot...'\n" +
            "pkg update -y && pkg install -y python {{DEPENDENCIES_PKG}}\n" +
            "pip install python-telegram-bot {{DEPENDENCIES_PIP}}\n" +
            "echo '[+] Set your BOT_TOKEN in .env'\n" +
            "echo '[+] Run: bash run.sh'\n"
        ),
        "run.sh" to "#!/bin/bash\nexport BOT_TOKEN=\"YOUR_TOKEN_HERE\"\npython3 main.py\n",
        ".env.example" to "BOT_TOKEN=your_telegram_bot_token_here\n",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.json" to "{\"name\":\"{{PROJECT_NAME}}\",\"author\":\"{{AUTHOR}}\",\"version\":\"{{VERSION}}\"}\n",
        "modules/__init__.py" to "# {{PROJECT_NAME}} modules\n",
        "assets/.gitkeep" to ""
    )

    // ─── PYTHON OSINT ─────────────────────────────────────────────────────────

    private val pythonOsint = linkedMapOf(
        "main.py" to (
            "#!/usr/bin/env python3\n" +
            "# {{PROJECT_NAME}} - OSINT Tool\n" +
            "# Author  : {{AUTHOR}}\n" +
            "# Version : {{VERSION}}\n\n" +
            "import sys\n" +
            "import json\n\n" +
            "{{BANNER_CODE}}\n\n" +
            "def osint_lookup(target):\n" +
            "    print(f'[*] Looking up: {target}')\n" +
            "    results = {'target': target, 'status': 'analyzed'}\n" +
            "    return results\n\n" +
            "def main():\n" +
            "    banner()\n" +
            "    if len(sys.argv) < 2:\n" +
            "        print('[!] Usage: python3 main.py <target>')\n" +
            "        sys.exit(1)\n" +
            "    target = sys.argv[1]\n" +
            "    results = osint_lookup(target)\n" +
            "    print(json.dumps(results, indent=2))\n\n" +
            "if __name__ == '__main__':\n" +
            "    main()\n"
        ),
        "requirements.txt" to "requests\n{{DEPENDENCIES_PIP}}\n",
        "install.sh" to (
            "#!/bin/bash\n" +
            "pkg update -y && pkg install -y python {{DEPENDENCIES_PKG}}\n" +
            "pip install requests {{DEPENDENCIES_PIP}}\n" +
            "echo '[+] Done! Run: python3 main.py <target>'\n"
        ),
        "run.sh" to "#!/bin/bash\npython3 main.py \$1\n",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "modules/__init__.py" to "# OSINT modules\n",
        "assets/.gitkeep" to "",
        "config/config.json" to "{\"name\":\"{{PROJECT_NAME}}\",\"author\":\"{{AUTHOR}}\",\"version\":\"{{VERSION}}\"}\n"
    )

    private val pythonTemplates = mapOf(
        "basic"    to pythonBasic,
        "menu"     to pythonMenu,
        "telegram" to pythonTelegram,
        "osint"    to pythonOsint
    )

    // ─── BASH BASIC ──────────────────────────────────────────────────────────

    private val bashBasic = linkedMapOf(
        "main.sh" to (
            "#!/bin/bash\n" +
            "# {{PROJECT_NAME}}\n" +
            "# Author  : {{AUTHOR}}\n" +
            "# Version : {{VERSION}}\n" +
            "# License : {{LICENSE}}\n" +
            "# Desc    : {{DESCRIPTION}}\n\n" +
            "{{BASH_BANNER}}\n\n" +
            "function main() {\n" +
            "    banner\n" +
            "    echo -e \"\\e[1;32m[+] {{PROJECT_NAME}} v{{VERSION}}\\e[0m\"\n" +
            "    echo -e \"\\e[0;37m{{DESCRIPTION}}\\e[0m\"\n" +
            "}\n\n" +
            "main \"\$@\"\n"
        ),
        "install.sh" to (
            "#!/bin/bash\n" +
            "echo '[*] Installing {{PROJECT_NAME}}...'\n" +
            "pkg update -y && pkg upgrade -y\n" +
            "pkg install -y {{DEPENDENCIES_PKG}}\n" +
            "chmod +x main.sh run.sh\n" +
            "echo '[+] Done! Run: bash run.sh'\n"
        ),
        "run.sh" to "#!/bin/bash\nbash main.sh \"\$@\"\n",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.sh" to (
            "#!/bin/bash\n" +
            "PROJECT_NAME=\"{{PROJECT_NAME}}\"\n" +
            "AUTHOR=\"{{AUTHOR}}\"\n" +
            "VERSION=\"{{VERSION}}\"\n"
        ),
        "modules/.gitkeep" to "",
        "assets/.gitkeep" to ""
    )

    // ─── BASH INSTALLER ──────────────────────────────────────────────────────

    private val bashInstaller = linkedMapOf(
        "main.sh" to (
            "#!/bin/bash\n" +
            "# {{PROJECT_NAME}} - Installer Tool\n" +
            "# Author: {{AUTHOR}} | Version: {{VERSION}}\n\n" +
            "RED='\\e[1;31m' GRN='\\e[1;32m' YLW='\\e[1;33m' NC='\\e[0m'\n\n" +
            "{{BASH_BANNER}}\n\n" +
            "check_root() {\n" +
            "    if [ \"\$EUID\" -ne 0 ]; then\n" +
            "        echo -e \"\${YLW}[!] Running without root\${NC}\"\n" +
            "    fi\n" +
            "}\n\n" +
            "install_deps() {\n" +
            "    echo -e \"\${GRN}[*] Installing dependencies...\${NC}\"\n" +
            "    pkg update -y && pkg upgrade -y\n" +
            "    pkg install -y {{DEPENDENCIES_PKG}}\n" +
            "    echo -e \"\${GRN}[+] Dependencies installed!\${NC}\"\n" +
            "}\n\n" +
            "main() {\n" +
            "    banner\n" +
            "    check_root\n" +
            "    install_deps\n" +
            "    echo -e \"\${GRN}[+] {{PROJECT_NAME}} setup complete!\${NC}\"\n" +
            "}\n\n" +
            "main \"\$@\"\n"
        ),
        "install.sh" to "#!/bin/bash\nbash main.sh\n",
        "run.sh" to "#!/bin/bash\nbash main.sh \"\$@\"\n",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.sh" to (
            "#!/bin/bash\n" +
            "PROJECT_NAME=\"{{PROJECT_NAME}}\"\n" +
            "AUTHOR=\"{{AUTHOR}}\"\n" +
            "VERSION=\"{{VERSION}}\"\n"
        ),
        "assets/.gitkeep" to ""
    )

    // ─── BASH NETWORK ────────────────────────────────────────────────────────

    private val bashNetwork = linkedMapOf(
        "main.sh" to (
            "#!/bin/bash\n" +
            "# {{PROJECT_NAME}} - Network Tool\n" +
            "# Author: {{AUTHOR}} | Version: {{VERSION}}\n\n" +
            "RED='\\e[1;31m' GRN='\\e[1;32m' CYN='\\e[1;36m' NC='\\e[0m'\n\n" +
            "{{BASH_BANNER}}\n\n" +
            "scan_host() {\n" +
            "    local host=\$1\n" +
            "    echo -e \"\${CYN}[*] Scanning: \$host\${NC}\"\n" +
            "    nmap -sV --open \"\$host\" 2>/dev/null || echo '[!] Install nmap: pkg install nmap'\n" +
            "}\n\n" +
            "ping_test() {\n" +
            "    local host=\$1\n" +
            "    echo -e \"\${CYN}[*] Pinging: \$host\${NC}\"\n" +
            "    ping -c 4 \"\$host\"\n" +
            "}\n\n" +
            "main() {\n" +
            "    banner\n" +
            "    echo -e \"\${GRN}{{PROJECT_NAME}} v{{VERSION}}\${NC}\"\n" +
            "    if [ -z \"\$1\" ]; then\n" +
            "        echo 'Usage: bash main.sh <host>'\n" +
            "        exit 1\n" +
            "    fi\n" +
            "    scan_host \"\$1\"\n" +
            "}\n\n" +
            "main \"\$@\"\n"
        ),
        "install.sh" to (
            "#!/bin/bash\n" +
            "pkg update -y && pkg install -y nmap {{DEPENDENCIES_PKG}}\n" +
            "chmod +x main.sh run.sh\n" +
            "echo '[+] Done!'\n"
        ),
        "run.sh" to "#!/bin/bash\nbash main.sh \"\$@\"\n",
        "README.md" to "{{README_CONTENT}}",
        "LICENSE" to "{{LICENSE_CONTENT}}",
        "config/config.sh" to (
            "#!/bin/bash\n" +
            "PROJECT_NAME=\"{{PROJECT_NAME}}\"\n" +
            "AUTHOR=\"{{AUTHOR}}\"\n" +
            "VERSION=\"{{VERSION}}\"\n"
        ),
        "assets/.gitkeep" to ""
    )

    private val bashTemplates = mapOf(
        "basic"     to bashBasic,
        "installer" to bashInstaller,
        "network"   to bashNetwork
    )
}
