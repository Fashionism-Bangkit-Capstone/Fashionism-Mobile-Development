package com.fashionism.fashionismuserapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.databinding.FragmentProfileBinding
import com.fashionism.fashionismuserapp.tools.Helper.showLoading
import com.fashionism.fashionismuserapp.tools.LanguageUtil.applyLanguage
import com.fashionism.fashionismuserapp.tools.LanguageUtil.getLanguage
import com.fashionism.fashionismuserapp.tools.LanguageUtil.setLanguage
import com.fashionism.fashionismuserapp.tools.startActivityWithAnim
import com.fashionism.fashionismuserapp.ui.auth.LoginActivity
import com.fashionism.fashionismuserapp.ui.common.collectWhenStarted
import com.fashionism.fashionismuserapp.ui.common.showError
import com.fashionism.fashionismuserapp.ui.common.showToast
import com.fashionism.fashionismuserapp.ui.product.FavoriteActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModel()

    private val categoryOption = listOf("English", "Indonesia")
    private val adapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(requireContext(), R.layout.item_language, categoryOption)
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.profile.observe(viewLifecycleOwner) {
            binding.nameUserProfile.text = it.name
            binding.emailUserProfile.text = it.email
            Glide.with(this)
                .load(it.avatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.profileImage)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading, binding.progressBarProfile)
        }

        viewLifecycleOwner.collectWhenStarted(viewModel.errors) { requireContext().showError(it) }

        viewLifecycleOwner.collectWhenStarted(viewModel.events) { event ->
            if (event == ProfileViewModel.Event.LoggedOut) openLogin()
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
            requireContext().startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.favoriteProfileBtn.setOnClickListener {
            val intent = Intent(requireContext(), FavoriteActivity::class.java)
            requireContext().startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.changePasswordAccountBtn.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            requireContext().startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
        }

        binding.logoutProfileBtn.setOnClickListener {
            logout()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        viewModel.logout()
    }

    private fun openLogin() {
        requireContext().showToast(getString(R.string.logoutSuccess))
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireContext().startActivityWithAnim(intent, R.anim.slidefromright_in, R.anim.slidefromright_out)
    }
}
