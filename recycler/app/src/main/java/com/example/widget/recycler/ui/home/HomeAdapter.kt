package com.example.widget.recycler.ui.home

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import com.example.widget.recycler.HolderBinding
import com.example.widget.recycler.databinding.ItemTextBinding
import com.example.widget.recycler.ktx.showSnackbar

class HomeAdapter(
    var data: MutableList<String> = mutableListOf(),
    private val listener: (String) -> Unit
) : ListAdapter<String, HolderBinding<ItemTextBinding>>(object : DiffUtil.ItemCallback<String?>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}) {

    var touchHelper: ItemTouchHelper? = null

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
            ivDrag.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    touchHelper?.startDrag(holder)
                }
                false
            }
            container.setOnClickListener {
                it.showSnackbar(data[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}


