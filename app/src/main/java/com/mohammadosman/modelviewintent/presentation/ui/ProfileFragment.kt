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
import com.mohammadosman.modelviewintent.databinding.FragmentProfileBinding
import com.mohammadosman.modelviewintent.presentation.viewmodel.mvi.ProfileViewModelMvi
import com.mohammadosman.modelviewintent.presentation.viewmodel.mvi.ProfileIntent
import com.mohammadosman.modelviewintent.presentation.util.TAG

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModels<ProfileViewModelMvi>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView:ProfileFragment")
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)


        viewModel.uiState.observe(viewLifecycleOwner, { feed ->
            feed?.let {
                binding.txtViewNewFeedsFromNetworkProfile.text = it.data
                binding.profileSwipeToRefresh.isRefreshing =
                    it.loadingState && it.pullToRefreshToFalse

            }
        })

        viewModel.uiState.observe(viewLifecycleOwner, { feed ->
            feed?.let {
                it.pullToRefLimitationError?.let { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.profileSwipeToRefresh.setOnRefreshListener {
            viewModel.sendIntent(intent = ProfileIntent.ForcePullToRefresh)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach:ProfileFragment")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach:ProfileFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.sendIntent(intent = ProfileIntent.InitialData)
        Log.d(TAG, "onCreate:ProfileFragment")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated:ProfileFragment")
    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause:ProfileFragment")
    }

    override fun onStart() {
        super.onStart()
        viewModel.sendIntent(intent = ProfileIntent.AutoPullToRefresh)
        Log.d(TAG, "onStart:ProfileFragment")

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop:ProfileFragment")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume:ProfileFragment")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState:ProfileFragment")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //viewModel.pullToRefreshInitialization = 1
        _binding = null
        Log.d(TAG, "onDestroyView:ProfileFragment")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy:ProfileFragment")

    }

}