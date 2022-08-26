package com.example.widget.recycler.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.widget.recycler.databinding.FragmentDashboardBinding
import com.example.widget.recycler.ktx.autoUnbind
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var binding: FragmentDashboardBinding by autoUnbind()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.content) {
            val dashboardAdapter = DashboardAdapter(
                mutableListOf(
                    "Hello", "Zzzz", "Hello", "Zzzz", "Hello", "Zzzz",
                    "Hello", "Zzzz", "Hello", "Zzzz", "Hello", "Zzzz",
                    "Hello", "Zzzz", "Hello", "Zzzz", "Hello", "Zzzz"
                )
            )
            adapter = dashboardAdapter
            layoutManager = GridLayoutManager(context, 5)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    (layoutManager as GridLayoutManager).orientation
                )
            )

            val touchHelper =
                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                    ItemTouchHelper.START
                ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        if (viewHolder.bindingAdapterPosition == 0) {
                            return false
                        }
                        if (target.bindingAdapterPosition == 0) {
                            return false
                        }
                        Collections.swap(
                            dashboardAdapter.data,
                            viewHolder.bindingAdapterPosition,
                            target.bindingAdapterPosition
                        )
                        dashboardAdapter.notifyItemMoved(
                            viewHolder.bindingAdapterPosition,
                            target.bindingAdapterPosition
                        )
                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    }

                    override fun isLongPressDragEnabled(): Boolean {
                        return true
                    }
                }).also {
                    it.attachToRecyclerView(this)
                }

//            adapter.touchHelper = touchHelper
        }
    }
}
