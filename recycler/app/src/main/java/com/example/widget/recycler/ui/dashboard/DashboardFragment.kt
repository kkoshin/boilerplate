package com.example.widget.recycler.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.widget.recycler.databinding.FragmentDashboardBinding
import com.example.widget.recycler.ktx.autoClear

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var binding: FragmentDashboardBinding by autoClear()

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
            adapter = DashboardAdapter(listOf(
                "Hello", "Zzzz", "Hello", "Zzzz", "Hello", "Zzzz",
                "Hello", "Zzzz", "Hello", "Zzzz", "Hello", "Zzzz",
                "Hello", "Zzzz", "Hello", "Zzzz", "Hello", "Zzzz"
            ))
            layoutManager = GridLayoutManager(context, 5)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    (layoutManager as GridLayoutManager).orientation
                )
            )
        }
    }
}
