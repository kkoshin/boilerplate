package com.example.widget.recycler.ui.home

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.widget.recycler.R
import com.example.widget.recycler.databinding.FragmentHomeBinding
import com.example.widget.recycler.ktx.autoUnbind
import com.github.foodiestudio.sugar.dp
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
            val bg = ColorDrawable(Color.RED)
            val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_delete_24)
            val backgroundCornerOffset = 0.dp

            val touchHelper =
                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(UP or DOWN, START) {
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
                        homeAdapter.notifyItemMoved(
                            viewHolder.adapterPosition,
                            target.adapterPosition
                        )
                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        homeAdapter.data.removeAt(viewHolder.adapterPosition)
                        homeAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    }

                    override fun onChildDraw(
                        c: Canvas,
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        dX: Float,
                        dY: Float,
                        actionState: Int,
                        isCurrentlyActive: Boolean
                    ) {
                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        )
                        val itemView = viewHolder.itemView
                        val iconMargin: Int = (itemView.height - icon!!.intrinsicHeight) / 2
                        val iconTop: Int =
                            itemView.getTop() + (itemView.getHeight() - icon.intrinsicHeight) / 2
                        val iconBottom = iconTop + icon.intrinsicHeight
                        when {
                            dX < 0 -> {
                                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                                val iconRight = itemView.right - iconMargin
                                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                                bg.setBounds(
                                    (itemView.right + dX.toInt() - backgroundCornerOffset).toInt(),
                                    itemView.top, itemView.right, itemView.bottom
                                )
                            }
                            else -> {
                                bg.setBounds(0, 0, 0, 0)
                                icon.setBounds(0, 0, 0, 0)
                            }
                        }
                        if (dX != 0f) {
                            bg.draw(c)
                            icon.draw(c)
                        }
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
