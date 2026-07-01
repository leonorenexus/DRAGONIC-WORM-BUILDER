package com.devleonore.dragonicworm.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devleonore.dragonicworm.databinding.ItemRecentProjectBinding
import com.devleonore.dragonicworm.model.RecentProject

class RecentProjectAdapter(private val items: List<RecentProject>) :
    RecyclerView.Adapter<RecentProjectAdapter.VH>() {

    inner class VH(val binding: ItemRecentProjectBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemRecentProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.recentName.text = item.name
        holder.binding.recentBadge.text = item.template
        holder.binding.recentTime.text = item.time
    }

    override fun getItemCount() = items.size
}
