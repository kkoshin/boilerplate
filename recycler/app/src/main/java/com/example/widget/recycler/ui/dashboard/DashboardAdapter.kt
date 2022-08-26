package com.example.widget.recycler.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.widget.recycler.HolderBinding
import com.example.widget.recycler.databinding.ItemPicBinding

class DashboardAdapter(
    val data: MutableList<String>
): RecyclerView.Adapter<HolderBinding<ItemPicBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderBinding<ItemPicBinding> {
        return HolderBinding(
            ItemPicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HolderBinding<ItemPicBinding>, position: Int) {
        with(holder.delegate) {
            if (position == 0) {
                title.text = "new"
            } else {
                title.text = data[position]
            }
        }
    }
}