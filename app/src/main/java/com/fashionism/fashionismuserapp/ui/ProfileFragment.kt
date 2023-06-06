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
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.session.UserSession
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModel
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModelFactory
import com.fashionism.fashionismuserapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private val pref by lazy {
        UserSession.getInstance(dataStore = requireContext().dataStore)
    }

    private var _binding: FragmentProfileBinding? = null
    private lateinit var token : String
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)

        val dataStoreViewModel =
            ViewModelProvider(this, UserSessionViewModelFactory(pref))[UserSessionViewModel::class.java]
        dataStoreViewModel.getToken().observe(viewLifecycleOwner) {
            token = it
        }

        val userSessionViewModel =
            ViewModelProvider(this, UserSessionViewModelFactory(pref))[UserSessionViewModel::class.java]

        userSessionViewModel.getName().observe(viewLifecycleOwner) {
            binding.nameUserProfile.text = it
        }
        userSessionViewModel.getEmail().observe(viewLifecycleOwner) {
            binding.emailUserProfile.text = it
        }

        binding.informationAccountBtn.setOnClickListener {
            val intent = Intent(requireContext(), ChangeProfileActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.favoriteProfileBtn.setOnClickListener {
            val intent = Intent(requireContext(), FavoriteActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.changePasswordAccountBtn.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.logoutProfileBtn.setOnClickListener {
            logout()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        val loginViewModel =
            ViewModelProvider(this, UserSessionViewModelFactory(pref))[UserSessionViewModel::class.java]
        loginViewModel.clearDataLogin()
        Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}