package com.example.billingapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.billingapp.R
import com.example.billingapp.databinding.FragmentLoginBinding
import com.example.billingapp.viewModels.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("Login","Logging in")
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)
        binding.apply {
            emailEditText.editText!!.setText(viewModel.loginEmail)
            passwordEditText.editText!!.setText(viewModel.loginPassword)
            emailEditText.editText!!.addTextChangedListener {
                viewModel.loginEmail = it.toString()
            }
            passwordEditText.editText!!.addTextChangedListener {
                viewModel.loginPassword = it.toString()
            }
            loginButton.setOnClickListener {
                viewModel.onLoginClick()
                hideKeyboard(requireContext())
            }
            needNewAccountTextView.setOnClickListener {
                findNavController().navigate(
                    R.id.registrationFragment
                )
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.loginFlow.collectLatest { event->
                    when (event) {
                        is AuthViewModel.AuthEvent.LoginSuccess -> {
                            loginProgressBar.isVisible = false
                            Snackbar.make(
                                requireView(),
                                event.message,
                                Snackbar.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(
                                R.id.listFragment
                            )
                        }
                        is AuthViewModel.AuthEvent.LoginFailure -> {
                            loginProgressBar.isVisible = false
                            Snackbar.make(
                                requireView(),
                                "Error: ${event.message}",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun hideKeyboard(mContext: Context) {
        val imm = mContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            requireActivity().window
                .currentFocus!!.windowToken, 0
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}