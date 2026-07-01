package com.devleonore.dragonicworm.ui.create

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devleonore.dragonicworm.DragonicApp
import com.devleonore.dragonicworm.R
import com.devleonore.dragonicworm.databinding.FragmentCreateProjectBinding
import com.devleonore.dragonicworm.model.Language
import com.devleonore.dragonicworm.model.License
import com.devleonore.dragonicworm.model.MenuItem
import com.devleonore.dragonicworm.model.ProjectConfig
import com.devleonore.dragonicworm.model.RecentProject
import com.devleonore.dragonicworm.model.TemplateType
import com.devleonore.dragonicworm.template.TemplateProcessor
import com.devleonore.dragonicworm.util.FileExporter
import com.devleonore.dragonicworm.util.ToastHelper
import com.devleonore.dragonicworm.util.ZipBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateProjectFragment : Fragment() {

    private var _binding: FragmentCreateProjectBinding? = null
    private val binding get() = _binding!!

    private lateinit var cfg: ProjectConfig
    private lateinit var menuAdapter: MenuBuilderAdapter
    private var isGenerating = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateProjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application as DragonicApp
        cfg = app.currentConfig

        bindProjectInfoFields()
        setupLicenseSpinner()
        setupLanguageSpinner()
        setupTemplateSpinner() // depends on language
        bindDependencyFields()
        setupMenuBuilder()
        bindIncludeSwitches()
        setupGenerateButton()
        setupPreviewButton()

        refreshMenuCardVisibility()
    }

    private fun bindProjectInfoFields() {
        binding.inputProjectName.setText(cfg.projectName)
        binding.inputAuthor.setText(cfg.author)
        binding.inputVersion.setText(cfg.version)
        binding.inputDescription.setText(cfg.description)
        binding.inputOutputName.setText(cfg.outputName)
        binding.txtBannerColor.text = cfg.bannerColor
        binding.colorSwatch.setBackgroundColor(safeParseColor(cfg.bannerColor))

        binding.inputProjectName.doAfterTextChangedSafe {
            cfg.projectName = it
            if (cfg.outputName.isBlank()) {
                val auto = it.trim().replace(Regex("\\s+"), "_").lowercase()
                binding.inputOutputName.setText(auto)
            }
        }
        binding.inputAuthor.doAfterTextChangedSafe { cfg.author = it }
        binding.inputVersion.doAfterTextChangedSafe { cfg.version = it }
        binding.inputDescription.doAfterTextChangedSafe { cfg.description = it }
        binding.inputOutputName.doAfterTextChangedSafe { cfg.outputName = it }

        binding.txtBannerColor.setOnClickListener {
            // Simple color cycle picker (no external picker lib needed)
            showColorPicker()
        }
        binding.colorSwatch.setOnClickListener { showColorPicker() }
    }

    private fun showColorPicker() {
        val colors = listOf("#FF0033", "#FF3300", "#FF0066", "#CC0022", "#FF1A47", "#990022", "#FF4466")
        val labels = colors.toTypedArray()
        android.app.AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_DragonicWorm_Dialog)
            .setTitle("Banner Color")
            .setItems(labels) { _, which ->
                val picked = colors[which]
                cfg.bannerColor = picked
                binding.txtBannerColor.text = picked
                binding.colorSwatch.setBackgroundColor(safeParseColor(picked))
            }
            .show()
    }

    private fun safeParseColor(hex: String): Int = try {
        Color.parseColor(hex)
    } catch (e: Exception) {
        Color.parseColor("#FF0033")
    }

    private fun setupLicenseSpinner() {
        val licenses = License.entries.map { it.label }
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, licenses)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.spinnerLicense.adapter = adapter
        val idx = licenses.indexOf(cfg.license).coerceAtLeast(0)
        binding.spinnerLicense.setSelection(idx)
        binding.spinnerLicense.onItemSelectedListener = simpleSelectionListener { position ->
            cfg.license = licenses[position]
        }
    }

    private fun setupLanguageSpinner() {
        val languages = Language.entries.map { it.label }
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, languages)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.spinnerLanguage.adapter = adapter
        val idx = Language.entries.indexOf(cfg.language).coerceAtLeast(0)
        binding.spinnerLanguage.setSelection(idx)
        binding.spinnerLanguage.onItemSelectedListener = simpleSelectionListener { position ->
            cfg.language = Language.entries[position]
            setupTemplateSpinner()
            refreshDependencyFieldsVisibility()
        }
        refreshDependencyFieldsVisibility()
    }

    private fun setupTemplateSpinner() {
        val templates = TemplateType.forLanguage(cfg.language)
        if (templates.none { it == cfg.template }) {
            cfg.template = templates.firstOrNull() ?: TemplateType.BASIC_TOOL
        }
        val names = templates.map { "${it.icon} ${it.displayName}" }
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, names)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        binding.spinnerTemplate.adapter = adapter
        val idx = templates.indexOf(cfg.template).coerceAtLeast(0)
        binding.spinnerTemplate.setSelection(idx)
        binding.spinnerTemplate.onItemSelectedListener = simpleSelectionListener { position ->
            cfg.template = templates[position]
            refreshMenuCardVisibility()
        }
    }

    private fun refreshDependencyFieldsVisibility() {
        binding.layoutDepPip.visibility =
            if (cfg.language == Language.PYTHON) View.VISIBLE else View.GONE
    }

    private fun bindDependencyFields() {
        binding.inputDepPip.setText(cfg.depPip)
        binding.inputDepPkg.setText(cfg.depPkg)
        binding.inputEnvVars.setText(cfg.envVars)
        binding.inputDepPip.doAfterTextChangedSafe { cfg.depPip = it }
        binding.inputDepPkg.doAfterTextChangedSafe { cfg.depPkg = it }
        binding.inputEnvVars.doAfterTextChangedSafe { cfg.envVars = it }
    }

    private fun setupMenuBuilder() {
        menuAdapter = MenuBuilderAdapter(cfg.menuItems) { position ->
            if (cfg.menuItems.size > 1) {
                cfg.menuItems.removeAt(position)
                menuAdapter.notifyDataSetChanged()
            } else {
                ToastHelper.show(requireContext(), "At least 1 menu item is required.", ToastHelper.Type.ERROR)
            }
        }
        binding.rvMenuItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMenuItems.adapter = menuAdapter

        binding.btnAddMenuItem.setOnClickListener {
            if (cfg.menuItems.size >= 10) {
                ToastHelper.show(requireContext(), "Maximum 10 menu items.", ToastHelper.Type.INFO)
                return@setOnClickListener
            }
            cfg.menuItems.add(MenuItem("Option ${cfg.menuItems.size + 1}", "echo 'option ${cfg.menuItems.size + 1}'"))
            menuAdapter.notifyItemInserted(cfg.menuItems.size - 1)
        }
    }

    private fun refreshMenuCardVisibility() {
        binding.cardMenuBuilder.visibility = if (cfg.template.usesMenu) View.VISIBLE else View.GONE
    }

    private fun bindIncludeSwitches() {
        binding.switchReadme.isChecked = cfg.includeReadme
        binding.switchRequirements.isChecked = cfg.includeRequirements
        binding.switchInstall.isChecked = cfg.includeInstall
        binding.switchRun.isChecked = cfg.includeRun

        binding.switchReadme.setOnCheckedChangeListener { _, checked -> cfg.includeReadme = checked }
        binding.switchRequirements.setOnCheckedChangeListener { _, checked -> cfg.includeRequirements = checked }
        binding.switchInstall.setOnCheckedChangeListener { _, checked -> cfg.includeInstall = checked }
        binding.switchRun.setOnCheckedChangeListener { _, checked -> cfg.includeRun = checked }
    }

    private fun setupPreviewButton() {
        binding.btnPreviewOutput.setOnClickListener {
            (activity as? com.devleonore.dragonicworm.MainActivity)?.navigateTo("preview")
        }
    }

    private fun setupGenerateButton() {
        binding.btnGenerate.setOnClickListener { attemptGenerate() }
    }

    private fun attemptGenerate() {
        if (isGenerating) return
        if (cfg.projectName.isBlank()) {
            ToastHelper.show(requireContext(), "Project Name is required.", ToastHelper.Type.ERROR)
            return
        }
        if (cfg.author.isBlank()) {
            ToastHelper.show(requireContext(), "Author is required.", ToastHelper.Type.ERROR)
            return
        }
        runGenerateSequence()
    }

    private fun runGenerateSequence() {
        isGenerating = true
        binding.btnGenerate.isEnabled = false
        binding.btnGenerate.text = getString(R.string.generating)
        binding.progressContainer.visibility = View.VISIBLE

        val steps = listOf(
            10 to "Loading template...",
            25 to "Processing placeholders...",
            45 to "Building file structure...",
            65 to "Generating support files...",
            80 to "Compressing to ZIP...",
            95 to "Finalizing...",
            100 to "Complete!"
        )

        val handler = Handler(Looper.getMainLooper())
        var stepIndex = 0

        fun runStep() {
            if (stepIndex >= steps.size) {
                finishGenerate()
                return
            }
            val (pct, msg) = steps[stepIndex]
            binding.progressBar.progress = pct
            binding.txtProgressMsg.text = msg
            binding.txtProgressPct.text = "$pct%"
            stepIndex++
            handler.postDelayed({ runStep() }, 260)
        }
        runStep()
    }

    private fun finishGenerate() {
        try {
            val files = TemplateProcessor.buildProjectFiles(cfg)
            val folderName = cfg.resolvedOutputName()
            val zipFileName = "$folderName.zip"

            val cacheDir = FileExporter.cacheDir(requireContext())
            val zipFile = File(cacheDir, zipFileName)
            if (zipFile.exists()) zipFile.delete()

            ZipBuilder.build(zipFile, folderName, files)

            val savedUri = FileExporter.saveToDownloads(requireContext(), zipFile, zipFileName)

            val app = requireActivity().application as DragonicApp
            val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            app.recentProjects.add(0, RecentProject(
                name = cfg.projectName,
                template = cfg.template.displayName,
                language = cfg.language.label,
                time = timeFmt.format(Date())
            ))
            if (app.recentProjects.size > 5) {
                app.recentProjects.removeAt(app.recentProjects.size - 1)
            }
            app.totalGenerated += 1

            if (savedUri != null) {
                ToastHelper.show(requireContext(), "✓ $zipFileName saved to Downloads!", ToastHelper.Type.SUCCESS)
            } else {
                ToastHelper.show(requireContext(), "✓ $zipFileName generated (Downloads save failed, use Share).", ToastHelper.Type.INFO)
            }

            // Offer share sheet too
            offerShare(zipFile)

        } catch (e: Exception) {
            e.printStackTrace()
            ToastHelper.show(requireContext(), "Failed to generate ZIP: ${e.message}", ToastHelper.Type.ERROR)
        }

        isGenerating = false
        binding.btnGenerate.isEnabled = true
        binding.btnGenerate.text = getString(R.string.generate_project)
        binding.progressContainer.visibility = View.GONE
        binding.progressBar.progress = 0
    }

    private fun offerShare(zipFile: File) {
        android.app.AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_DragonicWorm_Dialog)
            .setTitle("Project Generated")
            .setMessage("${zipFile.name} has been saved to Downloads.\n\nShare it now?")
            .setPositiveButton("Share") { _, _ ->
                startActivity(FileExporter.buildShareIntent(requireContext(), zipFile))
            }
            .setNegativeButton("Close", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// ─── small extension helpers local to this file ───────────────────────────

private fun com.google.android.material.textfield.TextInputEditText.doAfterTextChangedSafe(action: (String) -> Unit) {
    this.addTextChangedListener(object : android.text.TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: android.text.Editable?) {
            action(s?.toString() ?: "")
        }
    })
}

private fun simpleSelectionListener(onSelected: (Int) -> Unit) =
    object : android.widget.AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
            onSelected(position)
        }
        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
    }
