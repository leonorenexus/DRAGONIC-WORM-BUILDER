package com.devleonore.dragonicworm.ui.templates

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.devleonore.dragonicworm.R
import com.devleonore.dragonicworm.databinding.ItemTemplateCardBinding
import com.devleonore.dragonicworm.model.TemplateType

class TemplateAdapter(
    private val items: List<TemplateType>,
    private var selected: TemplateType,
    private val onClick: (TemplateType) -> Unit
) : RecyclerView.Adapter<TemplateAdapter.VH>() {

    inner class VH(val binding: ItemTemplateCardBinding) : RecyclerView.ViewHolder(binding.root)

    fun setSelected(t: TemplateType) {
        val old = items.indexOf(selected)
        selected = t
        val new = items.indexOf(t)
        if (old >= 0) notifyItemChanged(old)
        if (new >= 0) notifyItemChanged(new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTemplateCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val tpl = items[position]
        val ctx = holder.binding.root.context
        holder.binding.tplIcon.text = tpl.icon
        holder.binding.tplName.text = tpl.displayName
        holder.binding.tplDesc.text = tpl.description
        holder.binding.root.isSelected = tpl == selected

        // Lang badges
        holder.binding.layoutTplLangs.removeAllViews()
        for (lang in tpl.supportedLanguages) {
            val tv = TextView(ctx).apply {
                text = lang.label
                setTextColor(ContextCompat.getColor(ctx, R.color.neon_red_dark))
                textSize = 8f
                setPadding(10, 3, 10, 3)
                setBackgroundResource(R.drawable.bg_badge)
                val lp = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(0, 0, 6, 0)
                layoutParams = lp
            }
            holder.binding.layoutTplLangs.addView(tv)
        }

        holder.binding.root.setOnClickListener { onClick(tpl) }
    }

    override fun getItemCount() = items.size
}
