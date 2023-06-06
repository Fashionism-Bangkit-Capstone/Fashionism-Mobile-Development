package com.fashionism.fashionismuserapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.adapter.FashionItemAdapter
import com.fashionism.fashionismuserapp.data.dummy.DummyFashion
import com.fashionism.fashionismuserapp.data.session.UserSession
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModel
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModelFactory
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModel
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModelFactory
import com.fashionism.fashionismuserapp.databinding.ActivityFavoriteBinding
import com.fashionism.fashionismuserapp.tools.GridSpacingItemDecoration

class FavoriteActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFavoriteBinding.inflate(layoutInflater)
    }

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    private var recyclerView: RecyclerView? = null
    private var adapter1: FashionItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBackFavorite.setOnClickListener {
            finish()
        }

        val userSession = UserSession.getInstance(dataStore)
        val userSessionViewModel =
            ViewModelProvider(
                this,
                UserSessionViewModelFactory(userSession)
            )[UserSessionViewModel::class.java]

        recyclerView = binding.rvFavoriteItem
        val layoutManager = GridLayoutManager(this, 2)
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
        recyclerView?.adapter = FashionItemAdapter(DummyFashion.dummy, this)
    }
}