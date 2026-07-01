package com.devleonore.dragonicworm.template

import com.devleonore.dragonicworm.model.MenuItem

object MenuBuilder {

    fun functions(items: List<MenuItem>): String =
        items.mapIndexed { i, item ->
            val n = i + 1
            val cmd = item.command.ifBlank { "echo 'Running ${item.name}...'" }
            val callLine = if (cmd.startsWith("os.") || cmd.startsWith("print"))
                "    $cmd"
            else
                "    os.system(\"${cmd.replace("\"", "\\\"")}\")"
            "def option_$n():\n    print(\"\\033[1;32m[*] ${item.name}\\033[0m\")\n$callLine\n"
        }.joinToString("\n")

    fun menuItems(items: List<MenuItem>): String =
        items.mapIndexed { i, item ->
            "    print(\"\\033[1;31m║\\033[0m  \\033[1;31m[${i + 1}]\\033[0m \\033[0;37m${item.name}\\033[0m\")"
        }.joinToString("\n")

    fun menuChoices(items: List<MenuItem>): String =
        items.mapIndexed { i, _ ->
            val n = i + 1
            val prefix = if (i == 0) "        if" else "        elif"
            "$prefix choice == \"$n\":\n            option_$n()\n            input(\"\\n\\033[0;37m[Press Enter]\\033[0m\")"
        }.joinToString("\n")

    fun telegramCommands(items: List<MenuItem>): String =
        items.mapIndexed { i, item ->
            "    help_text += \"/cmd_${i + 1} - ${item.name}\\n\""
        }.joinToString("\n")
}
