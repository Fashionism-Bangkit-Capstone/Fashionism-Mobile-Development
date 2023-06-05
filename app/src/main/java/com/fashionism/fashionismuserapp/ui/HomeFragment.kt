package com.fashionism.fashionismuserapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.adapter.AdsItemAdapter
import com.fashionism.fashionismuserapp.adapter.FashionItemAdapter
import com.fashionism.fashionismuserapp.data.dummy.DummyAds
import com.fashionism.fashionismuserapp.data.dummy.DummyFashion
import com.fashionism.fashionismuserapp.databinding.FragmentHomeBinding
import com.fashionism.fashionismuserapp.tools.GridSpacingItemDecoration

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerView2: RecyclerView? = null
    private var adapter2: AdsItemAdapter? = null
    private var adapter1: FashionItemAdapter? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.login_statusBarDark)

        recyclerView2 = binding.rvAdsTop
        recyclerView2?.setHasFixedSize(true)
        recyclerView2?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView2?.adapter = adapter2
        recyclerView2?.adapter = AdsItemAdapter(DummyAds.dummy, requireActivity())

        recyclerView = binding.rvFashionData
        val layoutManager = GridLayoutManager(requireContext(), 2)
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        val includeBottom = true

        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(
            GridSpacingItemDecoration(
                spacing,
                includeBottom
            )
        )
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = adapter1
        recyclerView?.adapter = FashionItemAdapter(DummyFashion.dummy, requireActivity())

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}