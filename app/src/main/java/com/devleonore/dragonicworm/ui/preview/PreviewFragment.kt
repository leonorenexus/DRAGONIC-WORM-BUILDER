package com.devleonore.dragonicworm.ui.preview

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.devleonore.dragonicworm.DragonicApp
import com.devleonore.dragonicworm.R
import com.devleonore.dragonicworm.databinding.FragmentPreviewBinding
import com.devleonore.dragonicworm.model.Language
import com.devleonore.dragonicworm.model.ProjectConfig
import com.devleonore.dragonicworm.template.ReadmeGenerator

class PreviewFragment : Fragment() {

    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private val tabs = listOf("Banner", "README", "Structure", "Menu", "Deps")
    private var currentTab = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cfg = (requireActivity().application as DragonicApp).currentConfig
        buildTabs()
        populatePanes(cfg)
        showTab(0)
    }

    private fun buildTabs() {
        binding.tabStrip.removeAllViews()
        tabs.forEachIndexed { i, label ->
            val tv = TextView(requireContext()).apply {
                text = label
                textSize = 11f
                setTypeface(null, Typeface.BOLD)
                setPadding(24, 0, 24, 0)
                gravity = android.view.Gravity.CENTER_VERTICAL
                setTextColor(ContextCompat.getColor(requireContext(), R.color.ghost_gray))
                setOnClickListener { showTab(i) }
            }
            binding.tabStrip.addView(tv)
        }
    }

    private fun showTab(index: Int) {
        currentTab = index
        val panes = listOf(
            binding.paneBanner,
            binding.paneReadme,
            binding.paneStructure,
            binding.paneMenu,
            binding.paneDeps
        )
        panes.forEachIndexed { i, pane ->
            pane.visibility = if (i == index) View.VISIBLE else View.GONE
        }
        // Update tab colors
        for (i in 0 until binding.tabStrip.childCount) {
            val tv = binding.tabStrip.getChildAt(i) as? TextView ?: continue
            tv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (i == index) R.color.neon_red else R.color.ghost_gray
                )
            )
        }
    }

    private fun populatePanes(cfg: ProjectConfig) {
        val projectName = cfg.projectName.ifBlank { "PROJECT NAME" }
        val author = cfg.author.ifBlank { "Dev Leonore" }

        // Banner
        binding.txtBannerPreview.text = buildBannerPreview(projectName, author)

        // README
        binding.txtReadmePreview.text = ReadmeGenerator.generate(cfg)

        // Structure
        val mainFile = if (cfg.language == Language.PYTHON) "main.py" else "main.sh"
        val extra = if (cfg.language == Language.PYTHON)
            "├── requirements.txt\n├── modules/\n│   └── __init__.py\n├── assets/\n└── config/\n    └── config.json"
        else
            "├── config/\n│   └── config.sh\n├── modules/\n└── assets/"
        val outputName = cfg.resolvedOutputName()
        binding.txtStructurePreview.text =
            "$outputName/\n├── $mainFile\n├── install.sh\n├── run.sh\n├── README.md\n├── LICENSE\n├── $extra"

        // Menu
        val menuLines = cfg.menuItems.mapIndexed { i, item ->
            "  [${i + 1}] ${item.name.ifBlank { "Option ${i + 1}" }}"
        }.joinToString("\n")
        binding.txtMenuPreview.text =
            "╔══════════════════════════════╗\n║  $projectName\n╠══════════════════════════════╣\n$menuLines\n╠══════════════════════════════╣\n║  [0] Exit\n╚══════════════════════════════╝"

        // Deps
        val deps = buildString {
            if (cfg.language == Language.PYTHON) {
                appendLine("# pip packages:")
                appendLine(cfg.depPip.ifBlank { "# none" })
                appendLine()
            }
            appendLine("# pkg packages:")
            append(cfg.depPkg.ifBlank { "# none" })
        }
        binding.txtDepsPreview.text = deps
    }

    private fun buildBannerPreview(name: String, author: String) = """
 ██████╗ ██████╗  █████╗  ██████╗ 
 ██╔══██╗██╔══██╗██╔══██╗██╔════╝ 
 ██║  ██║██████╔╝███████║██║  ███╗
 ██║  ██║██╔══██╗██╔══██║██║   ██║
 ██████╔╝██║  ██║██║  ██║╚██████╔╝
 ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝
        $name
        By $author
""".trimIndent()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
