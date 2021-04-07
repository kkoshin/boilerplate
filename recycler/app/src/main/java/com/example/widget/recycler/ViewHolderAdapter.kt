package com.example.widget.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * 当 ViewHolder 遇上 ViewBinding
 */
class HolderBinding<T : ViewBinding>(var delegate: T) : RecyclerView.ViewHolder(delegate.root)
