package com.fashionism.fashionismuserapp.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.adapter.FashionItemAdapter
import com.fashionism.fashionismuserapp.adapter.FavProductHomeItemAdapter
import com.fashionism.fashionismuserapp.data.model.Product
import com.fashionism.fashionismuserapp.databinding.FragmentHomeBinding
import com.fashionism.fashionismuserapp.tools.GridSpacingItemDecoration
import com.fashionism.fashionismuserapp.tools.Helper.shortenText
import com.fashionism.fashionismuserapp.tools.Helper.showLoading
import com.fashionism.fashionismuserapp.tools.PhotoPicker
import com.fashionism.fashionismuserapp.tools.startActivityWithAnim
import com.fashionism.fashionismuserapp.ui.common.collectWhenStarted
import com.fashionism.fashionismuserapp.ui.common.showError
import com.fashionism.fashionismuserapp.ui.product.DetailFashionActivity
import com.fashionism.fashionismuserapp.ui.product.DetailFashionActivity.Companion.EXTRA_FASHION_ITEM
import com.fashionism.fashionismuserapp.ui.profile.ChangeProfileActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    private var loadingDialog: Dialog? = null

    private val photoPicker = PhotoPicker(this, { requireContext() }) { photo ->
        viewModel.getRecommendation(photo)
    }

    private val categoryOption = listOf("All Products", "Casual", "Street", "Office", "Formal")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        setupObservers()
        setupListeners()
    }

    private fun setupUI() {
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvFashionItem.layoutManager = GridLayoutManager(requireContext(), 2)
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.rvFashionItem.addItemDecoration(GridSpacingItemDecoration(spacing, true))
        binding.rvFashionItem.setHasFixedSize(true)

        binding.appCompatSpinner.adapter =
            ArrayAdapter(requireContext(), R.layout.item_dropdown, categoryOption)
    }

    private fun setupObservers() {
        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            binding.greetingUserName.text = shortenText(profile.name, 30)
            Glide.with(this)
                .load(profile.avatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.profileAccountNavigate)
        }

        viewModel.mostLiked.observe(viewLifecycleOwner) { products ->
            binding.rvFavorite.adapter = FavProductHomeItemAdapter(products).apply {
                setOnItemClickCallback(object : FavProductHomeItemAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Product) = showSelectedFashion(data)
                })
            }
        }

        viewModel.products.observe(viewLifecycleOwner) { products ->
            binding.llNoDataHome.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
            binding.rvFashionItem.adapter = FashionItemAdapter(products, false).apply {
                setOnItemClickCallback(object : FashionItemAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Product) = showSelectedFashion(data)
                })
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading, binding.progressBarHome)
        }

        viewModel.isLoadingRecommendation.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoadingDialog() else hideLoadingDialog()
        }

        viewLifecycleOwner.collectWhenStarted(viewModel.recommendations) { recommendation ->
            val intent = Intent(requireContext(), ResultSearchActivity::class.java)
            intent.putExtra(ResultSearchActivity.EXTRA_RECOMMENDATION, recommendation)
            startActivity(intent)
        }

        viewLifecycleOwner.collectWhenStarted(viewModel.errors) { requireContext().showError(it) }
    }

    private fun setupListeners() {
        binding.profileAccountNavigate.setOnClickListener {
            val intent = Intent(requireContext(), ChangeProfileActivity::class.java)
            requireContext().startActivityWithAnim(intent, R.anim.slidefromtop_in, R.anim.slidefromtop_out)
        }

        binding.searchFashionItem.setOnClickListener {
            photoPicker.show()
        }

        // Also fires once on first layout with position 0, which loads "All Products"
        binding.appCompatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                viewModel.selectCategory(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
    }

    private fun showLoadingDialog() {
        if (loadingDialog?.isShowing == true) return
        loadingDialog = Dialog(requireContext()).apply {
            setContentView(R.layout.await_data_search)
            setCancelable(false)
            window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            show()
        }
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    private fun showSelectedFashion(fashionItem: Product) {
        val intent = Intent(requireContext(), DetailFashionActivity::class.java)
        intent.putExtra(EXTRA_FASHION_ITEM, fashionItem)
        requireContext().startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
    }

    override fun onDestroyView() {
        hideLoadingDialog()
        super.onDestroyView()
        _binding = null
    }
}
