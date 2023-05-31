package com.alcorp.fashionism_umkm.ui.transaction

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.ViewModelFactory
import com.alcorp.fashionism_umkm.databinding.FragmentTransactionBinding
import com.alcorp.fashionism_umkm.ui.transaction.chart.ChartTransactionActivity
import com.alcorp.fashionism_umkm.utils.Helper
import com.alcorp.fashionism_umkm.utils.LoadingDialog
import com.alcorp.fashionism_umkm.utils.Status
import kotlinx.coroutines.launch

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var loadingDialog: LoadingDialog
    private val transactionViewModel: TransactionViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
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
        inflater.inflate(R.menu.menu_transaction, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_chart -> {
                startActivity(Intent(requireActivity(), ChartTransactionActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        setupView()
        loadData()
    }

    private fun setupView() {
        loadingDialog = LoadingDialog(requireContext())
    }

    private fun loadData() {
        lifecycleScope.launch {
            transactionViewModel.getTransactionList()
            transactionViewModel.transactionState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.showLoading(true)
                    }

                    Status.SUCCESS -> {
                        loadingDialog.showLoading(false)
                        it.data?.let { data ->
                            val transactionAdapter = TransactionAdapter(data.carts!!)
                            binding.rvTransaction.setHasFixedSize(true)
                            binding.rvTransaction.layoutManager = LinearLayoutManager(requireContext())
                            binding.rvTransaction.adapter = transactionAdapter
                        }
                    }

                    else -> {
                        loadingDialog.showLoading(false)
                        Helper.showToast(requireContext(), it.message.toString())
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