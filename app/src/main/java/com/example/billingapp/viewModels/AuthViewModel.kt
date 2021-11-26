package com.example.billingapp.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val state: SavedStateHandle
) : ViewModel() {
    var loginEmail = state.get<String>("loginEmail") ?: ""
        set(value) {
            field = value
            state.set("loginEmail", loginEmail)
        }
    var loginPassword = state.get<String>("loginPassword") ?: ""
        set(value) {
            field = value
            state.set("loginPassword", loginPassword)
        }
    var registrationEmail = state.get<String>("registrationEmail") ?: ""
        set(value) {
            field = value
            state.set("registrationEmail", registrationEmail)
        }
    var registrationPassword = state.get<String>("registrationPassword") ?: ""
        set(value) {
            field = value
            state.set("registrationPassword", registrationPassword)
        }
    private val authChannel = Channel<AuthEvent>()
    val loginFlow = authChannel.receiveAsFlow()
    val registrationFlow = authChannel.receiveAsFlow()
    fun onLoginClick() {
        if (loginPassword.isBlank()) {
            viewModelScope.launch {
                authChannel.send(AuthEvent.LoginFailure("Password cannot be empty"))
            }
            return
        }
        if (loginEmail.isBlank()) {
            if (mAuth.currentUser == null) {
                viewModelScope.launch {
                    authChannel.send(AuthEvent.LoginFailure("Email cannot be empty"))
                }
            }
            return
        }
        mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                viewModelScope.launch {
                    authChannel.send(
                        AuthEvent.LoginFailure(
                            task.exception?.localizedMessage ?: "An error occurred"
                        )
                    )
                }
            } else {
                viewModelScope.launch {
                    authChannel.send(AuthEvent.LoginSuccess("Login Successful"))
                }
            }
        }
    }

    fun onRegistrationClick() {
        if (registrationEmail.isBlank()) {
            viewModelScope.launch {
                authChannel.send(AuthEvent.RegistrationFailure("Email cannot be empty"))
            }
            return
        }
        if (registrationPassword.isBlank()) {
            viewModelScope.launch {
                authChannel.send(AuthEvent.RegistrationFailure("Password cannot be empty"))
            }
            return
        }
        mAuth.createUserWithEmailAndPassword(registrationEmail, registrationPassword)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    viewModelScope.launch {
                        authChannel.send(
                            AuthEvent.RegistrationFailure(
                                task.exception?.localizedMessage ?: "An error occurred"
                            )
                        )
                    }
                } else {
                    viewModelScope.launch {
                        authChannel.send(AuthEvent.RegistrationSuccess("Account created"))
                    }
                }
            }
    }

    sealed class AuthEvent {
        data class LoginSuccess(val message: String) : AuthEvent()
        data class LoginFailure(val message: String) : AuthEvent()
        data class RegistrationSuccess(val message: String) : AuthEvent()
        data class RegistrationFailure(val message: String) : AuthEvent()
    }
}