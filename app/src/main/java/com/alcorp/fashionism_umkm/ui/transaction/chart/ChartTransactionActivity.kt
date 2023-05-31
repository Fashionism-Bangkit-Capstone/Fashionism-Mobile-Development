package com.alcorp.fashionism_umkm.ui.transaction.chart

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.fashionism_umkm.R
import com.alcorp.fashionism_umkm.databinding.ActivityChartTransactionBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class ChartTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartTransactionBinding
    private lateinit var barArrayList: ArrayList<BarEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setupToolbar()
        setData()
        setupView()
    }

    private fun setupToolbar() {
        supportActionBar?.title = getString(R.string.title_chart)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupView() {
        val dataSet = BarDataSet(barArrayList, "Transaction")
        val barData = BarData(dataSet)

        dataSet.setColors(Color.CYAN)
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 16f

        binding.chartTransaction.data = barData
        binding.chartTransaction.description.isEnabled = true
    }

    private fun setData() {
        barArrayList = ArrayList()
        for (i in 1..10) {
            val fl = i*10f
            barArrayList.add(BarEntry(i.toFloat(), fl))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}