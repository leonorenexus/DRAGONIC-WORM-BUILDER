package com.devleonore.dragonicworm.ui.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devleonore.dragonicworm.DragonicApp
import com.devleonore.dragonicworm.databinding.FragmentSettingsBinding
import com.devleonore.dragonicworm.model.ProjectConfig
import com.devleonore.dragonicworm.util.ToastHelper

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application as DragonicApp
        val cfg = app.currentConfig

        binding.switchAutoReadme.isChecked = cfg.includeReadme
        binding.switchIncludeInstall.isChecked = cfg.includeInstall
        binding.switchIncludeRun.isChecked = cfg.includeRun
        binding.inputDefaultAuthor.setText(cfg.author)

        binding.switchAutoReadme.setOnCheckedChangeListener { _, c -> cfg.includeReadme = c }
        binding.switchIncludeInstall.setOnCheckedChangeListener { _, c -> cfg.includeInstall = c }
        binding.switchIncludeRun.setOnCheckedChangeListener { _, c -> cfg.includeRun = c }

        binding.inputDefaultAuthor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { cfg.author = s?.toString() ?: "Dev Leonore" }
        })

        binding.btnReset.setOnClickListener {
            app.currentConfig = ProjectConfig()
            ToastHelper.show(requireContext(), "Config reset to defaults.", ToastHelper.Type.INFO)
            binding.switchAutoReadme.isChecked = true
            binding.switchIncludeInstall.isChecked = true
            binding.switchIncludeRun.isChecked = true
            binding.inputDefaultAuthor.setText("Dev Leonore")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
