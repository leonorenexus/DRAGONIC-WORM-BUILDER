package com.devleonore.dragonicworm.ui.create

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devleonore.dragonicworm.databinding.ItemMenuBuilderRowBinding
import com.devleonore.dragonicworm.model.MenuItem

class MenuBuilderAdapter(
    private val items: MutableList<MenuItem>,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<MenuBuilderAdapter.VH>() {

    inner class VH(val binding: ItemMenuBuilderRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var nameWatcher: TextWatcher? = null
        var cmdWatcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMenuBuilderRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val b = holder.binding

        b.menuIndex.text = "[${position + 1}]"

        // Remove old watchers before setting text to avoid feedback loops
        holder.nameWatcher?.let { b.inputMenuName.removeTextChangedListener(it) }
        holder.cmdWatcher?.let { b.inputMenuCommand.removeTextChangedListener(it) }

        if (b.inputMenuName.text.toString() != item.name) b.inputMenuName.setText(item.name)
        if (b.inputMenuCommand.text.toString() != item.command) b.inputMenuCommand.setText(item.command)

        val nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) items[pos].name = s?.toString() ?: ""
            }
        }
        val cmdWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) items[pos].command = s?.toString() ?: ""
            }
        }
        b.inputMenuName.addTextChangedListener(nameWatcher)
        b.inputMenuCommand.addTextChangedListener(cmdWatcher)
        holder.nameWatcher = nameWatcher
        holder.cmdWatcher = cmdWatcher

        b.btnRemoveMenuItem.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) onRemove(pos)
        }
    }

    override fun getItemCount() = items.size
}
