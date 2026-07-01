package com.devleonore.dragonicworm.ui.templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.devleonore.dragonicworm.DragonicApp
import com.devleonore.dragonicworm.MainActivity
import com.devleonore.dragonicworm.R
import com.devleonore.dragonicworm.databinding.FragmentTemplateManagerBinding
import com.devleonore.dragonicworm.model.TemplateType
import com.devleonore.dragonicworm.template.TemplateBank
import com.devleonore.dragonicworm.util.ToastHelper

class TemplateManagerFragment : Fragment() {

    private var _binding: FragmentTemplateManagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TemplateAdapter
    private var selectedTemplate: TemplateType = TemplateType.BASIC_TOOL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTemplateManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as DragonicApp
        selectedTemplate = app.currentConfig.template

        val allTemplates = TemplateType.entries
        adapter = TemplateAdapter(allTemplates, selectedTemplate) { tpl ->
            selectedTemplate = tpl
            adapter.setSelected(tpl)
            updateDetail(tpl)
        }
        binding.rvTemplates.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvTemplates.adapter = adapter

        updateDetail(selectedTemplate)

        binding.btnUseTemplate.setOnClickListener {
            app.currentConfig.template = selectedTemplate
            // Also set language to first supported if current language not supported
            if (selectedTemplate.supportedLanguages.none { it == app.currentConfig.language }) {
                app.currentConfig.language = selectedTemplate.supportedLanguages.first()
            }
            ToastHelper.show(requireContext(), "Template set: ${selectedTemplate.displayName}", ToastHelper.Type.SUCCESS)
            (activity as? MainActivity)?.navigateTo("create")
        }
    }

    private fun updateDetail(tpl: TemplateType) {
        binding.txtDetailIcon.text = tpl.icon
        binding.txtDetailName.text = tpl.displayName
        binding.txtDetailDesc.text = tpl.description

        // File list - get from the template bank for first supported language
        val lang = tpl.supportedLanguages.first()
        val files = TemplateBank.filesFor(tpl, lang)
        binding.txtDetailFiles.text = files.keys.joinToString("\n") { "├─ $it" }

        // Lang badges
        binding.layoutDetailLangs.removeAllViews()
        for (l in tpl.supportedLanguages) {
            val tv = TextView(requireContext()).apply {
                text = l.label
                setTextColor(resources.getColor(R.color.neon_red_dark, null))
                textSize = 9f
                setPadding(10, 3, 10, 3)
                setBackgroundResource(R.drawable.bg_badge)
                val lp = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(0, 0, 8, 0)
                layoutParams = lp
            }
            binding.layoutDetailLangs.addView(tv)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
