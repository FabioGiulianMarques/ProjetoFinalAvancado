package com.fabiomarques.firebaselogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.fabiomarques.firebaselogin.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {
    private lateinit var _viewModel:AuthenticationViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        binding = FragmentLoginBinding.inflate(inflater,container,false).apply{
            viewModel = _viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        _viewModel.loginState.observe(viewLifecycleOwner, Observer { state ->
            if (state is AuthenticationViewModel.AuthState.Success
                && state.user != null) {
               // Toast.makeText(requireContext(), "Login with success", Toast.LENGTH_LONG).show()
                startActivity(Intent(requireContext(), AccountActivity::class.java))


            }
        })

            binding.subscribeButton.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.toSubscribe())
            }
        return binding.root
    }
 }










