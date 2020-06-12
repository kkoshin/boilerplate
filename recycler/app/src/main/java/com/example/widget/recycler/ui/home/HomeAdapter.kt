package com.example.widget.recycler.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.widget.recycler.databinding.ItemBlankBinding

class HomeAdapter(
    var data: List<String> = emptyList(),
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<ViewHolderAdapter<ItemBlankBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderAdapter<ItemBlankBinding> {
        return ViewHolderAdapter(
            ItemBlankBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderAdapter<ItemBlankBinding>, position: Int) {
        holder.B.txt.text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class ViewHolderAdapter<T : ViewBinding>(var B: T) : RecyclerView.ViewHolder(B.root)

