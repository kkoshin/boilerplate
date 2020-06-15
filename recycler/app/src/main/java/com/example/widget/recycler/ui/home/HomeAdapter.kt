package com.example.widget.recycler.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.widget.recycler.HolderBinding
import com.example.widget.recycler.databinding.ItemTextBinding

class HomeAdapter(
    var data: List<String> = emptyList(),
    private val listener: (String) -> Unit
) : ListAdapter<String, HolderBinding<ItemTextBinding>>(object : DiffUtil.ItemCallback<String?>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderBinding<ItemTextBinding> {
        return HolderBinding(
            ItemTextBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HolderBinding<ItemTextBinding>, position: Int) {
        with(holder.delegate) {
            txt.text = data[position]
            container.setOnClickListener {
                it.isEnabled = true
                notifyItemChanged(holder.adapterPosition)
            }
            ivDrag.setOnClickListener {
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}


