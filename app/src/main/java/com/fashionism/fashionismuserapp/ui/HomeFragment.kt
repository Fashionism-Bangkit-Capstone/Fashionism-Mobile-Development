package com.fashionism.fashionismuserapp.ui

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.adapter.FashionItemAdapter
import com.fashionism.fashionismuserapp.adapter.FavProductHomeItemAdapter
import com.fashionism.fashionismuserapp.data.db.Product
import com.fashionism.fashionismuserapp.data.session.UserSession
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModel
import com.fashionism.fashionismuserapp.data.session.UserSessionViewModelFactory
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModel
import com.fashionism.fashionismuserapp.data.viewmodel.MainViewModelFactory
import com.fashionism.fashionismuserapp.databinding.FragmentHomeBinding
import com.fashionism.fashionismuserapp.tools.GridSpacingItemDecoration
import com.fashionism.fashionismuserapp.tools.Helper.reduceFileImage
import com.fashionism.fashionismuserapp.tools.Helper.showLoading
import com.fashionism.fashionismuserapp.tools.shortenText
import com.fashionism.fashionismuserapp.ui.DetailFashionActivity.Companion.EXTRA_FASHION_ITEM
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var token: String
    private lateinit var currentPhotoPath: String
    private lateinit var getFile: File
    private lateinit var loadingDialog: Dialog

    private val FILENAME_FORMAT = "yyyyMMdd_HHmmss"

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        )[MainViewModel::class.java]
    }

    private val userSessionViewModel: UserSessionViewModel by lazy {
        ViewModelProvider(
            this,
            UserSessionViewModelFactory(UserSession.getInstance(requireActivity().dataStore))
        )[UserSessionViewModel::class.java]
    }

    private lateinit var rvLikedProduct: RecyclerView
    private lateinit var rvSuggestionsProduct: RecyclerView

    private val categoryOption = listOf("All Products", "Casual", "Street", "Office", "Formal")

    private val adapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(requireContext(), R.layout.item_dropdown, categoryOption)
    }

    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            userSessionViewModel.getToken().observe(viewLifecycleOwner) { token ->
                this@HomeFragment.token = token
                if (position == 0) {
                    mainViewModel.getAllProduct(token)
                } else {
                    mainViewModel.getProductByCategory(position, token)
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Nothing selected
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            getFile = File(currentPhotoPath)
            if (::getFile.isInitialized) {
                sendImageRecommendation()
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            getFile = uriToFile(data?.data!!, requireContext())!!
            if (::getFile.isInitialized) {
                sendImageRecommendation()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupUI(root)
        setupViewModels()
        setupObservers()
        setupListeners()

        return root
    }

    private fun setupUI(root: View) {
        rvLikedProduct = binding.rvFavorite
        rvLikedProduct.setHasFixedSize(true)
        rvLikedProduct.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        rvSuggestionsProduct = binding.rvFashionItem
        rvSuggestionsProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        rvSuggestionsProduct.addItemDecoration(GridSpacingItemDecoration(spacing, true))
        rvSuggestionsProduct.setHasFixedSize(true)

        binding.appCompatSpinner.adapter = adapter
    }

    private fun setupViewModels() {
        val userSession = UserSession.getInstance(requireActivity().dataStore)
        userSessionViewModel.getAllUserData().observe(viewLifecycleOwner) { dataUser ->
            mainViewModel.getProfile(dataUser.idUser, dataUser.token)
        }
    }

    private fun setupObservers() {
        userSessionViewModel.getToken().observe(viewLifecycleOwner) { token ->
            this.token = token
            mainViewModel.getAllProduct(token)
            mainViewModel.getProductMostYouLike(token)
        }

        mainViewModel.productMostLiked.observe(viewLifecycleOwner) { products ->
            val favProductHomeItemAdapter = FavProductHomeItemAdapter(products)
            rvLikedProduct.adapter = favProductHomeItemAdapter

            favProductHomeItemAdapter.setOnItemClickCallback(object :
                FavProductHomeItemAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Product) {
                    showSelectedFashion(data)
                }
            })
        }

        mainViewModel.productListByCategory.observe(viewLifecycleOwner) { products ->
            val suggestionsProductAdapter = FashionItemAdapter(products, false)
            rvSuggestionsProduct.adapter = suggestionsProductAdapter

            suggestionsProductAdapter.setOnItemClickCallback(object :
                FashionItemAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Product) {
                    showSelectedFashion(data)
                }
            })
        }

        mainViewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            binding.greetingUserName.text = shortenText(userProfile.data.name, 30)
            Glide.with(requireContext())
                .load(userProfile.data.avatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.profileAccountNavigate)
        }

        mainViewModel.fashionRecommendation.observe(viewLifecycleOwner) { fashionRecommendation ->
            if (fashionRecommendation != null) {
                val intent = Intent(requireContext(), ResultSearchActivity::class.java)
                intent.putExtra("fashionOutput", fashionRecommendation)
                startActivity(intent)
                mainViewModel.emptyFashionRecommendation()
            }
        }

        mainViewModel.message.observe(viewLifecycleOwner) { message ->
            when (message) {
                "Berhasil mendapat rekomendasi fashion" -> {}
                "Anda belum memiliki produk" -> {
                    binding.llNodataHome.visibility = View.VISIBLE
                }
                "Anda memiliki produk" -> {
                    binding.llNodataHome.visibility = View.GONE
                }
                else -> Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading, binding.progressBarHome)
        }

        mainViewModel.isLoadingRecommendation.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }
    }

    private fun setupListeners() {
        binding.profileAccountNavigate.setOnClickListener {
            val intent = Intent(requireContext(), ChangeProfileActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slidefromtop_in, R.anim.slidefromtop_out)
        }

        binding.searchFashionItem.setOnClickListener {
            showPopup()
        }

        binding.appCompatSpinner.onItemSelectedListener = onItemSelectedListener
    }

    private fun createCustomTempFile(context: Context): File {
        val timeStamp =
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun uriToFile(uri: Uri, context: Context): File? {
        val filePath = uri.path
        if (filePath != null) {
            try {
                val inputStream: InputStream? = uri.let {
                    context.contentResolver.openInputStream(
                        it
                    )
                }
                if (inputStream != null) {
                    val outputFile = File(context.cacheDir, "temp_file")
                    val outputStream = FileOutputStream(outputFile)
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        outputStream.write(buffer, 0, read)
                    }
                    outputStream.flush()
                    outputStream.close()
                    inputStream.close()
                    return outputFile
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.fashionism.fashionismuserapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        launcherIntentGallery.launch(gallery)
    }

    private fun sendImageRecommendation() {
        val file = reduceFileImage(getFile)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val final = MultipartBody.Part.createFormData("file", file.name, requestImageFile)
        mainViewModel.getFashionRecommendation(final)
    }

    private fun showPopup() {
        val options = arrayOf(
            resources.getString(R.string.cameraOption),
            resources.getString(R.string.fileManagerOption),
            resources.getString(R.string.cancelBtn)
        )
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.chooseOneOption))
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Camera option selected
                        startTakePhoto()
                        dialog.dismiss()
                    }
                    1 -> {
                        // Gallery option selected
                        startGallery()
                        dialog.dismiss()
                    }
                    2 -> {
                        // Cancel option selected
                        dialog.dismiss()
                    }
                }
            }
            .create()
            .show()
    }

    private fun showLoadingDialog() {
        loadingDialog = Dialog(requireContext())
        loadingDialog.setContentView(R.layout.await_data_search)
        loadingDialog.setCancelable(false)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(loadingDialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        loadingDialog.window?.attributes = layoutParams

        loadingDialog.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }

    private fun showSelectedFashion(fashionItem: Product) {
        val intent = Intent(requireContext(), DetailFashionActivity::class.java)
        intent.putExtra(EXTRA_FASHION_ITEM, fashionItem)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


//class HomeFragment : Fragment() {
//
//    private var _binding: FragmentHomeBinding? = null
//    private lateinit var rvLikedProduct: RecyclerView
//    private lateinit var rvSuggestionsProduct: RecyclerView
//    private val binding get() = _binding!!
//    private var getFile: File? = null
//    private lateinit var token: String
//    private var loadingDialog: Dialog? = null
//    private lateinit var currentPhotoPath: String
//    private val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
//
//    private val mainViewModel: MainViewModel by lazy {
//        ViewModelProvider(this, MainViewModelFactory(requireContext()))[MainViewModel::class.java]
//    }
//
//    private val userSessionViewModel by lazy {
//        ViewModelProvider(
//            this,
//            UserSessionViewModelFactory(UserSession.getInstance(requireActivity().dataStore))
//        )[UserSessionViewModel::class.java]
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        activity?.window?.statusBarColor =
//            ContextCompat.getColor(requireContext(), R.color.login_statusBarDark)
//
//        binding.profileAccountNavigate.setOnClickListener {
//            val intent = Intent(requireContext(), ChangeProfileActivity::class.java)
//            startActivity(intent)
//            activity?.overridePendingTransition(R.anim.slidefromtop_in, R.anim.slidefromtop_out)
//        }
//
//        userSessionViewModel.getToken().observe(viewLifecycleOwner) {
//            token = it
//            mainViewModel.getAllProduct(token)
//            mainViewModel.getProductMostYouLike(token)
//        }
//
//        rvLikedProduct = binding.rvFavorite
//        rvLikedProduct.setHasFixedSize(true)
//        rvLikedProduct.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//
//        mainViewModel.productMostLiked.observe(viewLifecycleOwner) { products ->
//            val productLikedItemAdapter = FavProductHomeItemAdapter(products)
//            rvLikedProduct.adapter = productLikedItemAdapter
//
//            productLikedItemAdapter.setOnItemClickCallback(object :
//                FavProductHomeItemAdapter.OnItemClickCallback {
//                override fun onItemClicked(data: Product) {
//                    showSelectedFashion(data)
//                }
//            })
//        }
//
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
//
//        mainViewModel.productListByCategory.observe(viewLifecycleOwner) { products ->
//            val suggestionsProductAdapter = FashionItemAdapter(products, false)
//            rvSuggestionsProduct.adapter = suggestionsProductAdapter
//
//            suggestionsProductAdapter.setOnItemClickCallback(object :
//                FashionItemAdapter.OnItemClickCallback {
//                override fun onItemClicked(data: Product) {
//                    showSelectedFashion(data)
//                }
//            })
//        }
//
//        val userSession = UserSession.getInstance(requireActivity().dataStore)
//        val userSessionViewModel =
//            ViewModelProvider(
//                this,
//                UserSessionViewModelFactory(userSession)
//            )[UserSessionViewModel::class.java]
//
//        userSessionViewModel.getAllUserData().observe(viewLifecycleOwner) { dataUser ->
//            mainViewModel.getProfile(dataUser.idUser, dataUser.token)
//        }
//
//        mainViewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
//            binding.greetingUserName.text = shortenText(userProfile.data.name, 30)
//            Glide.with(requireContext())
//                .load(userProfile.data.avatar)
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .error(R.drawable.ic_launcher_foreground)
//                .into(binding.profileAccountNavigate)
//        }
//
//        binding.searchFashionItem.setOnClickListener {
//            showPopup()
//        }
//
//        val categoryOption = listOf("All Products", "Casual", "Street", "Office", "Formal")
//
//        // Adapter untuk menghubungkan pilihan dengan Spinner
//        val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, categoryOption)
//        binding.appCompatSpinner.adapter = adapter
//        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            showLoading(isLoading, binding.progressBarHome)
//        }
//
//        // Pendengar untuk menangani pemilihan pilihan dalam Spinner
//        binding.appCompatSpinner.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    userSessionViewModel.getToken().observe(viewLifecycleOwner) {
//                        token = it
//                        if (position == 0) {
//                            mainViewModel.getAllProduct(token)
//                        } else {
//                            mainViewModel.getProductByCategory(position, token)
//                        }
//                    }
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>) {
//                    // Tidak ada yang dipilih
//                }
//            }
//
//        mainViewModel.fashionRecommendation.observe(viewLifecycleOwner) { fashionRecommendation ->
//            if (fashionRecommendation != null) {
//                val intent = Intent(requireContext(), ResultSearchActivity::class.java)
//                intent.putExtra("fashionOutput", fashionRecommendation)
//                startActivity(intent)
//                mainViewModel.emptyFashionRecommendation()
//            }
//        }
//
//        mainViewModel.message.observe(viewLifecycleOwner) { message ->
//            when (message) {
//                "Berhasil mendapat rekomendasi fashion" -> {}
//                "Anda belum memiliki produk" -> {
//                    binding.llNodataHome.visibility = View.VISIBLE
//                }
//                "Anda memiliki produk" -> {
//                    binding.llNodataHome.visibility = View.GONE
//                }
//                else -> Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        mainViewModel.isLoadingRecommendation.observe(viewLifecycleOwner) { isLoading ->
//            if (isLoading) {
//                showLoadingDialog()
//            } else {
//                hideLoadingDialog()
//            }
//        }
//
//        return root
//    }
//
//    private fun createCustomTempFile(context: Context): File {
//        val timeStamp: String = SimpleDateFormat(
//            FILENAME_FORMAT,
//            Locale.US
//        ).format(System.currentTimeMillis())
//
//        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(timeStamp, ".jpg", storageDir)
//    }
//
//    private val launcherIntentCamera = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) {
//        if (it.resultCode == RESULT_OK) {
//            getFile = File(currentPhotoPath)
//            if (getFile != null) {
//                sendImageRecommendation()
//            }
//        }
//    }
//
//    private fun startTakePhoto() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        intent.resolveActivity(requireActivity().packageManager)
//        createCustomTempFile(requireActivity().application).also {
//            val photoURI: Uri = FileProvider.getUriForFile(
//                requireContext(),
//                "com.fashionism.fashionismuserapp",
//                it
//            )
//            currentPhotoPath = it.absolutePath
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//            launcherIntentCamera.launch(intent)
//        }
//    }
//
//    private fun uriToFile(uri: Uri, context: Context): File? {
//        val filePath: String? = uri.path
//        if (filePath != null) {
//            try {
//                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
//                if (inputStream != null) {
//                    val outputFile = File(context.cacheDir, "temp_file")
//                    val outputStream = FileOutputStream(outputFile)
//                    val buffer = ByteArray(4 * 1024)
//                    var read: Int
//                    while (inputStream.read(buffer).also { read = it } != -1) {
//                        outputStream.write(buffer, 0, read)
//                    }
//                    outputStream.flush()
//                    outputStream.close()
//                    inputStream.close()
//                    return outputFile
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        return null
//    }
//
//    private val launcherIntentGallery =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val data = result.data
//                getFile = uriToFile(data?.data!!, requireContext())
//                if (getFile != null) {
//                    sendImageRecommendation()
//                }
//            }
//        }
//
//    private fun startGallery() {
//        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        launcherIntentGallery.launch(gallery)
//    }
//
//    private fun sendImageRecommendation() {
//        val file = reduceFileImage(getFile as File)
//        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//        val final = MultipartBody.Part.createFormData(
//            "file",
//            file.name,
//            requestImageFile
//        )
//        mainViewModel.getFashionRecommendation(final)
//    }
//
//    private fun showPopup() {
//        val options = arrayOf(
//            resources.getString(R.string.cameraOption),
//            resources.getString(R.string.fileManagerOption),
//            resources.getString(R.string.cancelBtn)
//        )
//        AlertDialog.Builder(requireContext())
//            .setTitle(resources.getString(R.string.chooseOneOption))
//            .setItems(options) { dialog, which ->
//                when (which) {
//                    0 -> {
//                        // Camera option selected
//                        startTakePhoto()
//                        dialog.dismiss()
//                    }
//                    1 -> {
//                        // Gallery option selected
//                        startGallery()
//                        dialog.dismiss()
//                    }
//                    2 -> {
//                        // Cancel option selected
//                        dialog.dismiss()
//                    }
//                }
//            }
//            .create()
//            .show()
//    }
//
//    private fun showLoadingDialog() {
//        loadingDialog = Dialog(requireContext())
//        loadingDialog?.setContentView(R.layout.await_data_search)
//        loadingDialog?.setCancelable(false)
//
//        // Mengatur ukuran dialog menjadi full screen
//        val layoutParams = WindowManager.LayoutParams()
//        layoutParams.copyFrom(loadingDialog?.window?.attributes)
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
//        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
//        loadingDialog?.window?.attributes = layoutParams
//
//        loadingDialog?.show()
//    }
//
//    private fun hideLoadingDialog() {
//        loadingDialog?.dismiss()
//    }
//
//    private fun showSelectedFashion(fashionItem: Product) {
//        val intent = Intent(requireContext(), DetailFashionActivity::class.java)
//        intent.putExtra(EXTRA_FASHION_ITEM, fashionItem)
//        startActivity(intent)
//        activity?.overridePendingTransition(R.anim.slidefromright_in, R.anim.slidefromright_out)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}