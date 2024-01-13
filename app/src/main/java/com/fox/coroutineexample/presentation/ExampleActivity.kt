package com.fox.coroutineexample.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fox.coroutineexample.State
import com.fox.coroutineexample.databinding.ActivityAxampleBinding
import kotlinx.coroutines.launch

class ExampleActivity : AppCompatActivity() {

    private val binding by lazy {
       ActivityAxampleBinding.inflate(layoutInflater)
    }

    private val adapter = ItemAdapter()

    private val viewModel by lazy {
        ViewModelProvider(this)[ItemViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()

        observeViewModel()

        binding.btnRefresh.setOnClickListener {
            Log.d("CryptoApp", "ExampleActivity buttonRefresh Click")
            lifecycleScope.launch {
                viewModel.refreshList()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCurrencyPriceList.adapter = adapter
        binding.recyclerViewCurrencyPriceList.itemAnimator = null
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect() {
                    when (it) {
                        is State.UiState.Initial  -> {
                            binding.progressBarLoading.isVisible = false
                            binding.btnRefresh.isEnabled = false
                        }

                        is State.UiState.Loading -> {
                            binding.progressBarLoading.isVisible = true
                            binding.btnRefresh.isEnabled = false
                        }

                        is State.UiState.Content -> {
                            binding.progressBarLoading.isVisible = false
                            binding.btnRefresh.isEnabled = true
                            adapter.submitList(it.itemList)
                        }

                        is State.UiState.Empty -> {
                            binding.progressBarLoading.isVisible = false
                            binding.btnRefresh.isEnabled = true
                        }

                        is State.UiState.Error -> {
                            Log.d(
                                "CryptoApp ", "OOPS Its Exception ExampleActivity!!!", it.throwable
                            )
                        }
                    }
                }

            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    when (it) {
                        is State.UiState.Content -> {
                            Log.d(
                                "CryptoApp", " launch in ExampleActivity"
                            )
                        }

                        else -> {}
                    }

                }
            }
        }
    }

}