package com.fashionism.fashionismuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.adapter.FashionItemAdapter
import com.fashionism.fashionismuserapp.adapter.FavProductHomeItemAdapter
import com.fashionism.fashionismuserapp.data.db.ProductDetail
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModel
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModelFactory
import com.fashionism.fashionismuserapp.databinding.FragmentHomeBinding
import com.fashionism.fashionismuserapp.tools.GridSpacingItemDecoration
import com.fashionism.fashionismuserapp.ui.DetailFashionActivity.Companion.EXTRA_FASHION_ITEM

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvLikedProduct: RecyclerView
    private lateinit var rvSuggestionsProduct: RecyclerView
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(requireContext()))[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.login_statusBarDark)

        binding.profileAccountNavigate.setOnClickListener {
            val intent = Intent(requireContext(), ChangeProfileActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        rvLikedProduct = binding.rvFavorite
        rvLikedProduct.setHasFixedSize(true)
        rvLikedProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        rvSuggestionsProduct = binding.rvFashionItem
        val layoutManager = GridLayoutManager(requireContext(), 2)
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        val includeBottom = true

        rvSuggestionsProduct.layoutManager = layoutManager
        rvSuggestionsProduct.addItemDecoration(
            GridSpacingItemDecoration(
                spacing,
                includeBottom
            )
        )
        rvSuggestionsProduct.setHasFixedSize(true)

        mainViewModel.getProducts()
        mainViewModel.product.observe(viewLifecycleOwner) { products ->
            val favProductAdapter = FavProductHomeItemAdapter(products)
            rvLikedProduct.adapter = favProductAdapter

            favProductAdapter.setOnItemClickCallback(object :
                FavProductHomeItemAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ProductDetail) {
                    showSelectedFashion(data)
                }
            })

            val suggestionsProductAdapter = FashionItemAdapter(products)
            rvSuggestionsProduct.adapter = suggestionsProductAdapter

            suggestionsProductAdapter.setOnItemClickCallback(object :
                FashionItemAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ProductDetail) {
                    showSelectedFashion(data)
                }
            })
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        // showRecyclerList()

//        rvSuggestionsProduct = binding.rvFashionItem
//        val layoutManager = GridLayoutManager(requireContext(), 2)
//        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
//        val includeBottom = true
//
//        rvSuggestionsProduct.layoutManager = layoutManager
//        rvSuggestionsProduct.addItemDecoration(
//            GridSpacingItemDecoration(
//                spacing,
//                includeBottom
//            )
//        )
//        rvSuggestionsProduct.setHasFixedSize(true)
//        val suggestionsProductAdapter = FashionItemAdapter(DummyFashion.dummy)
//        rvSuggestionsProduct.adapter = suggestionsProductAdapter

        return root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSelectedFashion(fashionItem: ProductDetail) {
        val intent = Intent(requireContext(), DetailFashionActivity::class.java)

        intent.putExtra(EXTRA_FASHION_ITEM, fashionItem)

        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}