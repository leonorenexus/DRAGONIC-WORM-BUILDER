package com.devleonore.dragonicworm.template

object BannerBuilder {

    private const val ART = """
 ██████╗ ██████╗  █████╗  ██████╗  ██████╗ ███╗   ██╗██╗ ██████╗ 
 ██╔══██╗██╔══██╗██╔══██╗██╔════╝ ██╔═══██╗████╗  ██║██║██╔════╝ 
 ██║  ██║██████╔╝███████║██║  ███╗██║   ██║██╔██╗ ██║██║██║      
 ██║  ██║██╔══██╗██╔══██║██║   ██║██║   ██║██║╚██╗██║██║██║      
 ██████╔╝██║  ██║██║  ██║╚██████╔╝╚██████╔╝██║ ╚████║██║╚██████╗ 
 ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═══╝╚═╝ ╚═════╝"""

    fun pythonBanner(projectName: String): String {
        val lines = ART.trim('\n').split("\n").joinToString("\n") { it }
        return buildString {
            append("def banner():\n")
            append("    print(\"\"\"\\033[1;31m\n")
            append(lines)
            append("\\033[0m\n")
            append("\\033[1;37m                    $projectName\\033[0m\n")
            append("\\033[0;31m                    By Dev Leonore\\033[0m\n")
            append("\"\"\")")
        }
    }

    fun bashBanner(projectName: String): String {
        val lines = ART.trim('\n').split("\n")
        return buildString {
            append("banner() {\n")
            append("    echo -e \"\\e[1;31m\"\n")
            for (line in lines) {
                append("    echo \"").append(line.replace("\"", "\\\"")).append("\"\n")
            }
            append("    echo -e \"\\e[0m\"\n")
            append("    echo -e \"\\e[1;37m                    $projectName\\e[0m\"\n")
            append("    echo -e \"\\e[0;31m                    By Dev Leonore\\e[0m\"\n")
            append("}")
        }
    }
}
