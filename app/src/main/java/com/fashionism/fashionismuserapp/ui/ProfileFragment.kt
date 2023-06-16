package com.fashionism.fashionismuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.session.UserSession
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModel
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModelFactory
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModel
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModelFactory
import com.fashionism.fashionismuserapp.databinding.FragmentProfileBinding
import com.fashionism.fashionismuserapp.tools.Helper.showLoading
import com.fashionism.fashionismuserapp.tools.LanguageUtil.applyLanguage
import com.fashionism.fashionismuserapp.tools.LanguageUtil.getLanguage
import com.fashionism.fashionismuserapp.tools.LanguageUtil.setLanguage

class ProfileFragment : Fragment() {


    private val mainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(requireContext()))[MainViewModel::class.java]
    }

    private val userSessionViewModel by lazy {
        ViewModelProvider(
            this,
            UserSessionViewModelFactory(UserSession.getInstance(requireActivity().dataStore))
        )[UserSessionViewModel::class.java]
    }

    private val categoryOption = listOf("English", "Indonesia")
    private val adapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(requireContext(), R.layout.item_language, categoryOption)
    }

    private var _binding: FragmentProfileBinding? = null
    private lateinit var token: String
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getLanguage(requireContext())
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)

        userSessionViewModel.getToken().observe(viewLifecycleOwner) {
            token = it
        }
        userSessionViewModel.getAllUserData().observe(viewLifecycleOwner) { dataUser ->
            mainViewModel.getProfile(dataUser.idUser, dataUser.token)
        }

        mainViewModel.userProfile.observe(viewLifecycleOwner) {
            binding.nameUserProfile.text = it.data.name
            binding.emailUserProfile.text = it.data.email
            Glide.with(requireContext())
                .load(it.data.avatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.profileImage)
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading, binding.progressBarProfile)
        }

        binding.languageChangeSpinner.adapter = adapter
        val currentLanguage = if (getLanguage(requireContext()) == "en") {
            "English"
        } else {
            "Indonesia"
        }
        binding.languageChangeSpinner.setSelection(adapter.getPosition(currentLanguage))

        // Atur listener pada spinner
        binding.languageChangeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Mendapatkan nilai action dari item yang dipilih
                val selectedAction = parent?.getItemAtPosition(position).toString()

                // Lakukan sesuatu dengan nilai action yang didapatkan
                if (selectedAction == "English") {
                    setLanguage(requireContext(), "en")
                    applyLanguage(requireContext())
                    // Lakukan tindakan untuk Action 1
                } else if (selectedAction == "Indonesia") {
                    setLanguage(requireContext(), "in")
                    applyLanguage(requireContext())
                    // Lakukan tindakan untuk Action 2
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Implementasi jika tidak ada item yang dipilih
            }
        }

        binding.informationAccountBtn.setOnClickListener {
            val intent = Intent(requireContext(), ChangeProfileActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.favoriteProfileBtn.setOnClickListener {
            val intent = Intent(requireContext(), FavoriteActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.changePasswordAccountBtn.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
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
        userSessionViewModel.clearDataLogin()
        Toast.makeText(
            requireContext(),
            resources.getString(R.string.logoutSuccess),
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
    }
}