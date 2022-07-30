package com.fabiomarques.firebaselogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fabiomarques.firebaselogin.databinding.FragmentSubscribeBinding



class SubscribeFragment:Fragment() {
    private lateinit var  _viewModel:AuthenticationViewModel
    private lateinit var binding: FragmentSubscribeBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
                _viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
                binding = FragmentSubscribeBinding.inflate(inflater, container, false).apply {
                viewModel = _viewModel
                lifecycleOwner = viewLifecycleOwner
        }
        _viewModel.subscribeState.observe(viewLifecycleOwner, Observer {
                state -> if (state is AuthenticationViewModel.AuthState.Success
                         && state.user != null) {
            startActivity(Intent(requireContext(), AccountActivity::class.java))


        }
        })
        return binding.root
    }
}