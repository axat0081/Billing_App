package com.example.billingapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.billingapp.R
import com.example.billingapp.databinding.FragmentRegistrationBinding
import com.example.billingapp.viewModels.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AuthViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegistrationBinding.bind(view)
        binding.apply {
            emailEditText.editText!!.setText(viewModel.registrationEmail)
            passwordEditText.editText!!.setText(viewModel.registrationPassword)
            emailEditText.editText!!.addTextChangedListener { email ->
                viewModel.registrationEmail = email.toString()
            }
            passwordEditText.editText!!.addTextChangedListener { password ->
                viewModel.registrationPassword = password.toString()
            }
            alreadyHaveAnAccountTextView.setOnClickListener {
                findNavController().navigate(
                    R.id.loginFragment
                )
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.registrationFlow.collectLatest { event ->
                    when (event) {
                        is AuthViewModel.AuthEvent.RegistrationSuccess -> {
                            Snackbar.make(
                                requireView(),
                                event.message,
                                Snackbar.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(
                                R.id.listFragment
                            )
                        }
                        is AuthViewModel.AuthEvent.RegistrationFailure -> {
                            Snackbar.make(
                                requireView(),
                                "Error: ${event.message}",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            progressBar.isVisible = false
                        }
                        else -> Unit
                    }
                }
            }
            registerButton.setOnClickListener {
                progressBar.isVisible = true
                hideKeyboard(requireContext())
                viewModel.onRegistrationClick()
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