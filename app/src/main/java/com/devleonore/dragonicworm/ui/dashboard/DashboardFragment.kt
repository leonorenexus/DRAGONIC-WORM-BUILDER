package com.devleonore.dragonicworm.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devleonore.dragonicworm.DragonicApp
import com.devleonore.dragonicworm.MainActivity
import com.devleonore.dragonicworm.R
import com.devleonore.dragonicworm.databinding.FragmentDashboardBinding
import com.devleonore.dragonicworm.model.TemplateType

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as DragonicApp

        // Stats
        binding.statTemplates.root.findViewById<TextView>(R.id.statNumber).text =
            TemplateType.entries.size.toString()
        binding.statTemplates.root.findViewById<TextView>(R.id.statLabel).text = "Templates"

        binding.statGenerated.root.findViewById<TextView>(R.id.statNumber).text =
            app.totalGenerated.toString()
        binding.statGenerated.root.findViewById<TextView>(R.id.statLabel).text = "Generated"

        binding.statLanguages.root.findViewById<TextView>(R.id.statNumber).text = "2"
        binding.statLanguages.root.findViewById<TextView>(R.id.statLabel).text = "Languages"

        // Quick actions
        val actions = listOf(
            QuickAction("✦", "New Project", "Start from template", "create"),
            QuickAction("◈", "Templates", "Browse all templates", "templates"),
            QuickAction("◉", "Preview", "Preview output files", "preview"),
            QuickAction("⬇", "Export", "Download ZIP archive", "export")
        )
        binding.rvQuickActions.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvQuickActions.adapter = QuickActionAdapter(actions) { action ->
            (activity as? MainActivity)?.navigateTo(action.targetTag)
        }

        // Hero buttons
        binding.btnHeroCreate.setOnClickListener {
            (activity as? MainActivity)?.navigateTo("create")
        }
        binding.btnHeroTemplates.setOnClickListener {
            (activity as? MainActivity)?.navigateTo("templates")
        }

        // Recent
        if (app.recentProjects.isEmpty()) {
            binding.txtNoRecent.visibility = View.VISIBLE
            binding.rvRecent.visibility = View.GONE
        } else {
            binding.txtNoRecent.visibility = View.GONE
            binding.rvRecent.visibility = View.VISIBLE
            binding.rvRecent.layoutManager = LinearLayoutManager(requireContext())
            binding.rvRecent.adapter = RecentProjectAdapter(app.recentProjects.take(5))
        }

        // System info grid
        populateSystemInfo()
    }

    private fun populateSystemInfo() {
        val grid = binding.gridSystemInfo
        grid.columnCount = 2
        val info = listOf(
            "ENGINE" to "Template v2.0",
            "AI" to "None (Offline)",
            "DATABASE" to "None",
            "CLOUD" to "None",
            "DEVELOPER" to "Dev Leonore",
            "BUILD" to "2026.06"
        )
        val ctx = requireContext()
        for ((key, value) in info) {
            val container = android.widget.LinearLayout(ctx).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                val lp = GridLayout.LayoutParams()
                lp.width = 0
                lp.height = GridLayout.LayoutParams.WRAP_CONTENT
                lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                lp.setMargins(0, 0, 16, 16)
                layoutParams = lp
            }
            val keyView = TextView(ctx).apply {
                text = key
                setTextColor(resources.getColor(R.color.neon_red_dark, null))
                textSize = 9f
                letterSpacing = 0.15f
            }
            val valView = TextView(ctx).apply {
                text = value
                setTextColor(resources.getColor(R.color.text_primary, null))
                textSize = 11f
                setPadding(0, 4, 0, 0)
            }
            container.addView(keyView)
            container.addView(valView)
            grid.addView(container)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
