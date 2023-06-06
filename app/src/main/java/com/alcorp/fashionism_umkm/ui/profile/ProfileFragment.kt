package com.alcorp.fashionism_umkm.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.data.remote.response.ProfileData
import com.alcorp.fashionism_umkm.databinding.FragmentProfileBinding
import com.alcorp.fashionism_umkm.ui.auth.login.LoginActivity
import com.alcorp.fashionism_umkm.ui.home.add_or_edit_outfit.AddEditOutfitActivity
import com.alcorp.fashionism_umkm.ui.profile.change_password.ChangePasswordActivity
import com.alcorp.fashionism_umkm.ui.profile.edit_profile.EditProfileActivity
import com.alcorp.fashionism_umkm.ui.profile.edit_profile.EditProfileActivity.Companion.EXTRA_EDIT_PROFILE
import com.alcorp.fashionism_umkm.utils.Helper.showToast
import com.alcorp.fashionism_umkm.utils.LoadingDialog
import com.alcorp.fashionism_umkm.utils.PrefData
import com.alcorp.fashionism_umkm.utils.Status
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private lateinit var profileData: ProfileData
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
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
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update_profile -> {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                intent.putExtra(EXTRA_EDIT_PROFILE, profileData)
                startActivity(intent)
                requireActivity().finish()
            }
            R.id.menu_change_password -> {
                val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        setupView()
        loadData()
    }

    private fun setupView() {
        pref = requireActivity().getSharedPreferences("fashionism_umkm", AppCompatActivity.MODE_PRIVATE)
        loadingDialog = LoadingDialog(requireContext())

        binding.btnUpdate.setOnClickListener(this)
    }

    private fun loadData() {
        lifecycleScope.launch {
            profileViewModel.getProfile(PrefData.token, PrefData.idUser)
            profileViewModel.profileState.collect {
                when (it.status) {
                    Status.LOADING -> loadingDialog.showLoading(true)

                    Status.SUCCESS -> {
                        loadingDialog.showLoading(false)
                        it.data?.let { profile ->
                            profileData = ProfileData(profile.data?.id, profile.data?.name, profile.data?.email, profile.data?.phone, profile.data?.address, profile.data?.avatar)
                            Glide.with(requireActivity())
                                .load(profile.data?.avatar)
                                .error(ContextCompat.getDrawable(requireActivity(), R.drawable.default_image))
                                .placeholder(ContextCompat.getDrawable(requireActivity(), R.drawable.default_image))
                                .into(binding.ivProfile)

                            binding.tvShopName.text = profile.data?.name
                            binding.tvEmail.text = profile.data?.email
                            binding.tvPhone.text = profile.data?.phone
                            binding.tvAddress.text = profile.data?.address
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

    override fun onClick(view: View) {
        when (view) {
            binding.btnUpdate -> {
                prefEdit = pref.edit()
                prefEdit.clear().apply()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}