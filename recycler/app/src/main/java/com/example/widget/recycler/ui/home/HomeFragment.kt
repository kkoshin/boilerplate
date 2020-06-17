package com.example.widget.recycler.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.widget.recycler.databinding.FragmentHomeBinding
import com.example.widget.recycler.ktx.autoUnbind
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var binding: FragmentHomeBinding by autoUnbind()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.content) {
            val homeAdapter = HomeAdapter(
                mutableListOf(
                    "Hello!!!",
                    "Hahaha",
                    "Hello!!!",
                    "Hahaha",
                    "Hello!!!",
                    "Hahaha"
                )
            ) {

            }
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(UP or DOWN, START) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    Collections.swap(
                        homeAdapter.data,
                        viewHolder.adapterPosition,
                        target.adapterPosition
                    )
                    homeAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    homeAdapter.data.removeAt(viewHolder.adapterPosition)
                    homeAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                }

                override fun isLongPressDragEnabled(): Boolean {
                    return false
                }
            }).also {
                it.attachToRecyclerView(this)
            }

            homeAdapter.touchHelper = touchHelper
        }
    }
}
