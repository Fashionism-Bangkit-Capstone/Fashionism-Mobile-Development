package com.alcorp.fashionism_umkm.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.databinding.FragmentProfileBinding
import com.alcorp.fashionism_umkm.ui.auth.login.LoginActivity
import com.alcorp.fashionism_umkm.ui.profile.edit_profile.EditProfileActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
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
            R.id.menu_logout -> {
                prefEdit = pref.edit()
                prefEdit.clear().apply()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        setupView()
    }

    private fun setupView() {
        pref = requireActivity().getSharedPreferences("fashionism_umkm", AppCompatActivity.MODE_PRIVATE)

        Glide.with(requireActivity())
            .load(pref.getString("image", ""))
            .error(ContextCompat.getDrawable(requireActivity(), R.drawable.default_image))
            .apply(
                RequestOptions
                    .centerCropTransform()
                    .placeholder(ContextCompat.getDrawable(requireActivity(), R.drawable.default_image))
                    .error(ContextCompat.getDrawable(requireActivity(), R.drawable.default_image))
            )
            .into(binding.ivProfile)

        binding.tvShopName.text = pref.getString("name", "")
        binding.tvEmail.text = pref.getString("email", "")

        binding.btnUpdate.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnUpdate -> {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}