package com.fox.coroutineexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.fox.coroutineexample.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDownload.setOnClickListener {
            loadData()
        }
    }

    private fun loadData() {
        binding.progressBar.isVisible = true
        binding.btnDownload.isEnabled = false
        loadCity {
            binding.tvCityName.text = it
            loadTemperature(it) {
                binding.tvTempValue.text = it.toString()
                binding.progressBar.isVisible = false
                binding.btnDownload.isEnabled = true
            }
        }

    }

    private fun loadCity(callback: (String) -> Unit) {
        thread {
            Thread.sleep(5000)

            callback.invoke("Moscow")
        }

    }

    private fun loadTemperature(city: String, callback: (Int) -> Unit) {
        thread {
            Toast.makeText(
                this,
                getString(R.string.loading_temperature_toast, city),
                Toast.LENGTH_SHORT
            ).show()

            Thread.sleep(5000)

            callback.invoke(17)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}