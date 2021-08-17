package com.mohammadosman.modelviewintent.presentation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mohammadosman.modelviewintent.R
import com.mohammadosman.modelviewintent.presentation.viewmodel.AccountViewModel
import com.mohammadosman.modelviewintent.presentation.util.TAG
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {

    private lateinit var swipeRef: SwipeRefreshLayout
    private lateinit var txtViewAccount: TextView

    private val viewmodel by viewModels<AccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView:AccountFragment")
        val root = inflater.inflate(R.layout.fragment_account, container, false)
        swipeRef = root.findViewById(R.id.swipeToRef)
        txtViewAccount = root.findViewById(R.id.txtView_account)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.result.collect {
                txtViewAccount.text = it ?: ""
            }

            viewmodel.receiveEvent.collect {
                Toast.makeText(this@AccountFragment.context, it, Toast.LENGTH_SHORT).show()
            }
        }


        /*  swipeRef.setOnRefreshListener {

          }*/
        Log.d(TAG, "onViewCreated:AccountFragment")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach:AccountFragment")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach:AccountFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate:AccountFragment")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated:AccountFragment")
    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause:AccountFragment")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart:AccountFragment")

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop:AccountFragment")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume:AccountFragment")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState:AccountFragment")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView:AccountFragment")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy:AccountFragment")

    }

}