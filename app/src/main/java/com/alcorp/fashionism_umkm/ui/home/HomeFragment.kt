package com.alcorp.fashionism_umkm.ui.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.data.remote.response.DetailOutfitData
import com.alcorp.fashionism_umkm.databinding.FragmentHomeBinding
import com.alcorp.fashionism_umkm.ui.auth.login.LoginActivity
import com.alcorp.fashionism_umkm.ui.home.add_or_edit_outfit.AddEditOutfitActivity
import com.alcorp.fashionism_umkm.utils.Helper
import com.alcorp.fashionism_umkm.utils.Helper.showToast
import com.alcorp.fashionism_umkm.utils.LoadingDialog
import com.alcorp.fashionism_umkm.utils.PrefData
import com.alcorp.fashionism_umkm.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var pref: SharedPreferences
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                val intent = Intent(requireContext(), AddEditOutfitActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        setupView()
        checkLogin()
        loadData()
    }

    private fun checkLogin() {
        val token = pref.getString("access_token", "").toString()
        PrefData.token = "Bearer $token"
        PrefData.idUser = pref.getString("id", "").toString()
        if (token == null && token == "") {
            val i = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    private fun setupView() {
        pref = requireActivity().getSharedPreferences("fashionism_umkm", AppCompatActivity.MODE_PRIVATE)
        loadingDialog = LoadingDialog(requireContext())
    }

    private fun loadData() {
        lifecycleScope.launch {
            homeViewModel.getOutfitList(PrefData.token, PrefData.idUser)
            homeViewModel.homeState.collect {
                when (it.status) {
                    Status.LOADING -> loadingDialog.showLoading(true)

                    Status.SUCCESS -> {
                        loadingDialog.showLoading(false)
                        it.data?.let { data ->
                            val homeAdapter = HomeAdapter(data.data!!)
                            binding.rvOutfit.setHasFixedSize(true)
                            binding.rvOutfit.layoutManager = GridLayoutManager(requireContext(), 2)
                            binding.rvOutfit.adapter = homeAdapter
                        }
                    }

                    else -> {
                        loadingDialog.showLoading(false)
                        showToast(requireContext(), it.message.toString())
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}