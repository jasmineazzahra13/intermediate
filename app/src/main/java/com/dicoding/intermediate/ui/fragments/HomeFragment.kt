package com.dicoding.intermediate.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.intermediate.R
import com.dicoding.intermediate.databinding.FragmentHomeBinding
import com.dicoding.intermediate.ui.adapters.LoadingStateAdapt
import com.dicoding.intermediate.ui.adapters.StoryPagingAdapt
import com.dicoding.intermediate.ui.vm.HomeVM
import com.dicoding.intermediate.utils.helper.closeApp
import com.dicoding.intermediate.utils.helper.gone
import com.dicoding.intermediate.utils.helper.hide
import com.dicoding.intermediate.utils.helper.hideAndStop
import com.dicoding.intermediate.utils.helper.popClick
import com.dicoding.intermediate.utils.helper.show
import com.dicoding.intermediate.utils.helper.showAndStart
import com.dicoding.intermediate.utils.helper.showYesNoDialog
import timber.log.Timber
import org.koin.android.ext.android.inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeVM by inject()

    private val storyPagingAdapter: StoryPagingAdapt by lazy {
        StoryPagingAdapt {
            navigateToDetail(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showYesNoDialog(
                        title = getString(R.string.title_close_app),
                        message = getString(R.string.message_close_app),
                        onYes = {
                            closeApp()
                        }
                    )
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        binding.apply {
            rvStory.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = storyPagingAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapt {
                        storyPagingAdapter.retry()
                    }
                )
                isNestedScrollingEnabled = false
            }

            storyPagingAdapter.addLoadStateListener { loadState ->
                if (loadState.append.endOfPaginationReached) {
                    if (storyPagingAdapter.itemCount < 1) {
                        layoutEmpty.root.show()
                        layoutError.root.gone()
                    }
                }
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        showLoading(true)
                    }
                    is LoadState.NotLoading -> {
                        showLoading(false)
                        binding.rvStory.scheduleLayoutAnimation()
                    }
                    is LoadState.Error -> {
                        showLoading(false)
                        layoutEmpty.root.gone()
                        rvStory.gone()
                        layoutError.root.show()
                    }
                    else -> showLoading(false)
                }
            }
        }
    }

    override fun initActions() {
        binding.apply {
            toolBar.apply {
                btnSetting.popClick {
                    findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                }
            }

            fabAddStory.popClick {
                findNavController().navigate(R.id.action_homeFragment_to_addStoryFragment)
            }

            layoutRefresh.setOnRefreshListener {
                storyPagingAdapter.refresh()
                layoutRefresh.isRefreshing = false
            }

            layoutError.btnRefresh.popClick {
                storyPagingAdapter.refresh()
                layoutError.root.gone()
            }
        }

        initResultData()
    }

    override fun initProcess() {}

    override fun initObservers() {
        viewModel.getStory().observe(this) {
            storyPagingAdapter.submitData(lifecycle, it)
        }
    }

    private fun initResultData(){
        setFragmentResultListener(AddStoryFragment.IS_UPLOAD_SUCCESS_KEY) { _, bundle ->
            val result = bundle.getBoolean(AddStoryFragment.IS_UPLOAD_SUCCESS_BUNDLE)
            Timber.d("Data is $result")

            /// Force scroll top after success
            Handler(Looper.getMainLooper()).postDelayed({
                binding.rvStory.scrollToPosition(0)
            }, 300)
        }
    }

    private fun navigateToDetail(id: String) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailStoryFragment(
                id
            )
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                shimmerLoading.showAndStart()
                rvStory.hide()
            } else {
                shimmerLoading.hideAndStop()
                rvStory.show()
            }
        }
    }
}