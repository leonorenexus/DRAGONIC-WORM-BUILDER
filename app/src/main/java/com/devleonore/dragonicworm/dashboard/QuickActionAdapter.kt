package com.devleonore.dragonicworm.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devleonore.dragonicworm.databinding.ItemQuickActionBinding

data class QuickAction(val icon: String, val label: String, val desc: String, val targetTag: String)

class QuickActionAdapter(
    private val items: List<QuickAction>,
    private val onClick: (QuickAction) -> Unit
) : RecyclerView.Adapter<QuickActionAdapter.VH>() {

    inner class VH(val binding: ItemQuickActionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemQuickActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.quickIcon.text = item.icon
        holder.binding.quickLabel.text = item.label
        holder.binding.quickDesc.text = item.desc
        holder.binding.root.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
