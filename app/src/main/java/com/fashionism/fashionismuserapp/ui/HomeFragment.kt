package com.fashionism.fashionismuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.adapter.FashionItemAdapter
import com.fashionism.fashionismuserapp.adapter.FavProductHomeItemAdapter
import com.fashionism.fashionismuserapp.data.db.ProductDetail
import com.fashionism.fashionismuserapp.data.session.UserSession
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModel
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModelFactory
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModel
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModelFactory
import com.fashionism.fashionismuserapp.databinding.FragmentHomeBinding
import com.fashionism.fashionismuserapp.tools.GridSpacingItemDecoration
import com.fashionism.fashionismuserapp.tools.Helper.showLoading
import com.fashionism.fashionismuserapp.tools.shortenText
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
            activity?.overridePendingTransition(R.anim.slidefromtop_in, R.anim.slidefromtop_out)
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

            val suggestionsProductAdapter = FashionItemAdapter(products, false)
            rvSuggestionsProduct.adapter = suggestionsProductAdapter

            suggestionsProductAdapter.setOnItemClickCallback(object :
                FashionItemAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ProductDetail) {
                    showSelectedFashion(data)
                }
            })
        }

        val userSession = UserSession.getInstance(requireActivity().dataStore)
        val userSessionViewModel =
            ViewModelProvider(
                this,
                UserSessionViewModelFactory(userSession)
            )[UserSessionViewModel::class.java]

        userSessionViewModel.getAllUserData().observe(viewLifecycleOwner) { dataUser ->
            mainViewModel.getProfile(dataUser.idUser, dataUser.token)
        }

        mainViewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            binding.greetingUserName.text = shortenText(userProfile.data.name, 30)
            Glide.with(requireContext())
                .load(userProfile.data.avatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.profileAccountNavigate)
        }

//        binding.searchFashionItem.setOnClickListener {
//            val intent = Intent(requireContext(), SearchActivity::class.java)
//            startActivity(intent)
//        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading, binding.progressBarHome)
        }

        mainViewModel.message.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        return root
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