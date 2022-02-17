package com.example.widget.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class HolderBinding<T : ViewBinding>(var delegate: T) : RecyclerView.ViewHolder(delegate.root)
