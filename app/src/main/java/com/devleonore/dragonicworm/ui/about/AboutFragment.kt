package com.devleonore.dragonicworm.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.devleonore.dragonicworm.R
import com.devleonore.dragonicworm.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillAboutRow(binding.aboutDev, "DEVELOPER", "Dev Leonore")
        fillAboutRow(binding.aboutProject, "PROJECT", "DRAGONIC WORM BUILDER")
        fillAboutRow(binding.aboutVersion, "VERSION", "2.0.0")
        fillAboutRow(binding.aboutBuild, "BUILD", "2026.06")
        fillAboutRow(binding.aboutLicense, "LICENSE", "MIT")

        binding.txtTechList.text = listOf(
            "Kotlin", "Android SDK 29+", "ViewBinding",
            "java.util.zip", "MediaStore API", "Fragment Navigation"
        ).joinToString("\n") { "• $it" }

        binding.txtNotUsing.text = listOf(
            "❌ AI / Chatbot", "❌ Database", "❌ Login",
            "❌ Cloud / VPS", "❌ External API", "❌ Internet"
        ).joinToString("\n")
    }

    private fun fillAboutRow(rowView: View, label: String, value: String) {
        rowView.findViewById<TextView>(R.id.aboutRowLabel).text = label
        rowView.findViewById<TextView>(R.id.aboutRowValue).text = value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
