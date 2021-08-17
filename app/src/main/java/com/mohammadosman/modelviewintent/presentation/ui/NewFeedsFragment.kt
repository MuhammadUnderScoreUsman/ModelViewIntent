package com.mohammadosman.modelviewintent.presentation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.mohammadosman.modelviewintent.databinding.FragmentNewFeedsBinding
import com.mohammadosman.modelviewintent.presentation.viewmodel.NewFeedsViewModel
import com.mohammadosman.modelviewintent.presentation.util.Resource
import com.mohammadosman.modelviewintent.presentation.util.TAG
import kotlinx.coroutines.flow.collect

class NewFeedsFragment : Fragment() {


    private val viewModel by viewModels<NewFeedsViewModel>()

    private var _binding: FragmentNewFeedsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView:NewFeedsFragment")
        _binding = FragmentNewFeedsBinding.inflate(
            layoutInflater,
            container, false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabNewFeeds.setOnClickListener {
            viewModel.response(
                binding.edtTxtUserINPT.text.toString()
            )
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.handleUiEvents.collect {
                when (it) {
                    is NewFeedsViewModel.UiEvents.ThrowResponseMsg -> {
                        Snackbar.make(
                            view,
                            it.msg,
                            Snackbar.LENGTH_LONG
                        ).show()

                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.newFeeds.collect {
                val data = it ?: return@collect
                binding.txtViewNewFeedsFromNetwork.text = data.data.toString()
                binding.newFeedsSwipeToRefresh.isRefreshing = it is Resource.Loading
                Log.d(TAG, "DATA: ${data.data}")
            }
        }

        binding.newFeedsSwipeToRefresh.setOnRefreshListener {
            viewModel.forceRefresh()
        }

        Log.d(TAG, "onViewCreated:NewFeedsFragment")
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
        Log.d(TAG, "onStart:NewFeedsFragment")

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach:NewFeedsFragment")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach:NewFeedsFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate:NewFeedsFragment")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated:NewFeedsFragment")
    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause:NewFeedsFragment")
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop:NewFeedsFragment")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume:NewFeedsFragment")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState:NewFeedsFragment")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "onDestroyView:NewFeedsFragment")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy:NewFeedsFragment")

    }

}