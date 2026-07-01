package com.devleonore.dragonicworm

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.devleonore.dragonicworm.databinding.ActivityMainBinding
import com.devleonore.dragonicworm.ui.about.AboutFragment
import com.devleonore.dragonicworm.ui.create.CreateProjectFragment
import com.devleonore.dragonicworm.ui.dashboard.DashboardFragment
import com.devleonore.dragonicworm.ui.export.ExportFragment
import com.devleonore.dragonicworm.ui.preview.PreviewFragment
import com.devleonore.dragonicworm.ui.settings.SettingsFragment
import com.devleonore.dragonicworm.ui.templates.TemplateManagerFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentTag: String = "dashboard"

    private data class NavRow(val container: View, val icon: ImageView, val label: TextView, val tag: String, val titleRes: Int, val iconRes: Int)
    private lateinit var navRows: List<NavRow>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavRows()
        setupTopBar()

        if (savedInstanceState == null) {
            navigateTo("dashboard")
        }
    }

    private fun setupTopBar() {
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(androidx.core.view.GravityCompat.START)
        }
    }

    private fun setupNavRows() {
        navRows = listOf(
            NavRow(binding.navDashboard.root, binding.navDashboard.navIcon, binding.navDashboard.navLabel, "dashboard", R.string.nav_dashboard, R.drawable.ic_nav_dashboard),
            NavRow(binding.navCreate.root, binding.navCreate.navIcon, binding.navCreate.navLabel, "create", R.string.nav_create, R.drawable.ic_nav_create),
            NavRow(binding.navTemplates.root, binding.navTemplates.navIcon, binding.navTemplates.navLabel, "templates", R.string.nav_templates, R.drawable.ic_nav_templates),
            NavRow(binding.navPreview.root, binding.navPreview.navIcon, binding.navPreview.navLabel, "preview", R.string.nav_preview, R.drawable.ic_nav_preview),
            NavRow(binding.navExport.root, binding.navExport.navIcon, binding.navExport.navLabel, "export", R.string.nav_export, R.drawable.ic_nav_export),
            NavRow(binding.navSettings.root, binding.navSettings.navIcon, binding.navSettings.navLabel, "settings", R.string.nav_settings, R.drawable.ic_nav_settings),
            NavRow(binding.navAbout.root, binding.navAbout.navIcon, binding.navAbout.navLabel, "about", R.string.nav_about, R.drawable.ic_nav_about)
        )

        for (row in navRows) {
            row.label.setText(row.titleRes)
            row.icon.setImageResource(row.iconRes)
            row.container.setOnClickListener {
                navigateTo(row.tag)
                binding.drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START)
            }
        }
        updateNavSelection()
    }

    private fun updateNavSelection() {
        for (row in navRows) {
            val selected = row.tag == currentTag
            row.container.isSelected = selected
            row.label.setTextColor(
                ContextCompat.getColor(this, if (selected) R.color.neon_red else R.color.ghost_gray)
            )
            row.icon.setColorFilter(
                ContextCompat.getColor(this, if (selected) R.color.neon_red else R.color.ghost_gray)
            )
        }
    }

    fun navigateTo(tag: String) {
        currentTag = tag
        updateNavSelection()

        val fragment: Fragment = when (tag) {
            "dashboard" -> DashboardFragment()
            "create" -> CreateProjectFragment()
            "templates" -> TemplateManagerFragment()
            "preview" -> PreviewFragment()
            "export" -> ExportFragment()
            "settings" -> SettingsFragment()
            "about" -> AboutFragment()
            else -> DashboardFragment()
        }

        binding.txtTopBarTitle.text = titleFor(tag)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun titleFor(tag: String): String = when (tag) {
        "dashboard" -> getString(R.string.nav_dashboard)
        "create" -> getString(R.string.nav_create)
        "templates" -> getString(R.string.nav_templates)
        "preview" -> getString(R.string.nav_preview)
        "export" -> getString(R.string.nav_export)
        "settings" -> getString(R.string.nav_settings)
        "about" -> getString(R.string.nav_about)
        else -> getString(R.string.nav_dashboard)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(androidx.core.view.GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
