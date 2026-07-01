package com.devleonore.dragonicworm.ui.export

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devleonore.dragonicworm.DragonicApp
import com.devleonore.dragonicworm.MainActivity
import com.devleonore.dragonicworm.R
import com.devleonore.dragonicworm.databinding.FragmentExportBinding
import com.devleonore.dragonicworm.model.Language
import com.devleonore.dragonicworm.model.RecentProject
import com.devleonore.dragonicworm.template.TemplateProcessor
import com.devleonore.dragonicworm.util.FileExporter
import com.devleonore.dragonicworm.util.ToastHelper
import com.devleonore.dragonicworm.util.ZipBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExportFragment : Fragment() {

    private var _binding: FragmentExportBinding? = null
    private val binding get() = _binding!!
    private var isGenerating = false
    private var lastZipFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application as DragonicApp
        val cfg = app.currentConfig

        val outputName = cfg.resolvedOutputName()
        val mainFile = if (cfg.language == Language.PYTHON) "main.py" else "main.sh"
        val extra = if (cfg.language == Language.PYTHON)
            "├── requirements.txt\n├── modules/\n└── config/"
        else "├── config/\n└── assets/"

        binding.txtExportInfo.text =
            "Project : ${cfg.projectName.ifBlank { "(not set)" }}\n" +
            "Template: ${cfg.template.displayName}\n" +
            "Language: ${cfg.language.label}\n" +
            "Output  : $outputName.zip\n" +
            "Author  : ${cfg.author}"

        binding.txtOutputStructure.text =
            "$outputName/\n├── $mainFile\n├── install.sh\n├── run.sh\n├── README.md\n├── LICENSE\n├── $extra"

        binding.btnDownloadZip.setOnClickListener { runGenerate(saveToDownloads = true) }
        binding.btnShareZip.setOnClickListener {
            if (lastZipFile != null) {
                startActivity(FileExporter.buildShareIntent(requireContext(), lastZipFile!!))
            } else {
                runGenerate(saveToDownloads = false)
            }
        }
        binding.btnBackToConfig.setOnClickListener {
            (activity as? MainActivity)?.navigateTo("create")
        }
    }

    private fun runGenerate(saveToDownloads: Boolean) {
        if (isGenerating) return
        val app = requireActivity().application as DragonicApp
        val cfg = app.currentConfig
        if (cfg.projectName.isBlank()) {
            ToastHelper.show(requireContext(), "Project Name is required.", ToastHelper.Type.ERROR)
            (activity as? MainActivity)?.navigateTo("create")
            return
        }

        isGenerating = true
        binding.progressContainer.visibility = View.VISIBLE
        binding.btnDownloadZip.isEnabled = false
        binding.btnShareZip.isEnabled = false

        val steps = listOf(
            10 to "Loading template...",
            30 to "Processing placeholders...",
            55 to "Building ZIP...",
            80 to "Saving...",
            100 to "Done!"
        )
        val handler = Handler(Looper.getMainLooper())
        var i = 0

        fun runStep() {
            if (i >= steps.size) { finishGenerate(saveToDownloads); return }
            binding.progressBar.progress = steps[i].first
            binding.txtProgressMsg.text = steps[i].second
            binding.txtProgressPct.text = "${steps[i].first}%"
            i++
            handler.postDelayed({ runStep() }, 250)
        }
        runStep()
    }

    private fun finishGenerate(saveToDownloads: Boolean) {
        val app = requireActivity().application as DragonicApp
        val cfg = app.currentConfig
        try {
            val files = TemplateProcessor.buildProjectFiles(cfg)
            val folderName = cfg.resolvedOutputName()
            val zipFile = File(FileExporter.cacheDir(requireContext()), "$folderName.zip")
            if (zipFile.exists()) zipFile.delete()
            ZipBuilder.build(zipFile, folderName, files)
            lastZipFile = zipFile

            if (saveToDownloads) {
                FileExporter.saveToDownloads(requireContext(), zipFile, "$folderName.zip")
                ToastHelper.show(requireContext(), "✓ $folderName.zip saved to Downloads!", ToastHelper.Type.SUCCESS)
            }

            val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            app.recentProjects.add(0, RecentProject(cfg.projectName, cfg.template.displayName, cfg.language.label, timeFmt.format(Date())))
            if (app.recentProjects.size > 5) app.recentProjects.removeAt(app.recentProjects.size - 1)
            app.totalGenerated++

            if (!saveToDownloads) {
                startActivity(FileExporter.buildShareIntent(requireContext(), zipFile))
            }
        } catch (e: Exception) {
            ToastHelper.show(requireContext(), "Error: ${e.message}", ToastHelper.Type.ERROR)
        }
        isGenerating = false
        binding.progressContainer.visibility = View.GONE
        binding.btnDownloadZip.isEnabled = true
        binding.btnShareZip.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
