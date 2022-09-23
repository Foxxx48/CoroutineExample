package com.fox.coroutineexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.view.isVisible
import com.fox.coroutineexample.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDownload.setOnClickListener {
            hideParams()
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
            handler.post {
                callback.invoke("Moscow")
               binding.progressBar.isVisible = false
            }

        }

    }

    private fun loadTemperature(city: String, callback: (Int) -> Unit) {
        thread {
            Thread.sleep(1500)
            handler.post {
                binding.progressBar.isVisible = true
                Toast.makeText(
                    this,
                    getString(R.string.loading_temperature_toast, city),
                    Toast.LENGTH_SHORT
                ).show()
            }
            Thread.sleep(5000)
            handler.post {
                callback.invoke(17)
            }
        }
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