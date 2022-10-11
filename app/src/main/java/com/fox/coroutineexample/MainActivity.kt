package com.fox.coroutineexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.fox.coroutineexample.databinding.ActivityMainBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDownload.setOnClickListener {

            val deferredCity = lifecycleScope.async {
                hideParams()
                binding.progressBar.isVisible = true
                binding.btnDownload.isEnabled = false
                val city = loadCity()
                binding.tvCityName.text = city
                city
            }


            val deferredTemp = lifecycleScope.async {
                val temp = loadTemperature()
                binding.tvTempValue.text = temp.toString()
                temp
            }

            lifecycleScope.launch {
                val city = deferredCity.await()
                val temp = deferredTemp.await()
                Toast.makeText(
                    this@MainActivity,
                    "City $city  temperature $temp",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.isVisible = false
                binding.btnDownload.isEnabled = true
            }
        }
    }

    private suspend fun loadData() {
        Log.d("TAG_MainActivity", "Load started:  $this")
        binding.progressBar.isVisible = true
        binding.btnDownload.isEnabled = false

        val city = loadCity()
        binding.tvCityName.text = city
        hideProgressBar()
        val temp = loadTemperature()
        binding.tvTempValue.text = temp.toString()
        binding.progressBar.isVisible = false
        binding.btnDownload.isEnabled = true
        Log.d("TAG_MainActivity", "Load finished:  $this")
    }

    private suspend fun loadCity(): String {
        delay(5000)
        return "Moscow"
    }

    private suspend fun loadTemperature(): Int {
        delay(5000)
        return 17
    }

    private suspend fun hideProgressBar() {
        binding.progressBar.isVisible = false

        delay(2000)

        binding.progressBar.isVisible = true

    }

    private fun hideParams() {
        if (binding.tvCityName != null) {
            binding.tvCityName.text = ""
        }
        if (binding.tvTempValue != null) {
            binding.tvTempValue.text = ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}