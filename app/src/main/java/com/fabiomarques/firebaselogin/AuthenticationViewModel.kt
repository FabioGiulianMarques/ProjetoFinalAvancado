package com.fabiomarques.firebaselogin
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.internal.ConnectionErrorMessages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthenticationViewModel: ViewModel() {

    companion object {
        const val MIN_PASS_SIZE = 8
    }
    val subscribeEmail = MutableLiveData<String>()
    val subscribePassword = MutableLiveData<String>()
    val loginEmail = MutableLiveData<String>()
    val loginPassword = MutableLiveData<String>()
    // read firebase
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    sealed class AuthState(){
        var errorMessage:String? = null
        constructor(errorMessages: String): this(){
            this.errorMessage = errorMessage
        }

        object Loading: AuthState()
        class Success(val user:FirebaseUser?): AuthState()
        class Error(error:String): AuthState(error)
        fun isError() = this is Error
    }

    private val _subscribeState = MutableLiveData<AuthState>()
    var subscribeState = _subscribeState

    private val _loginState = MutableLiveData<AuthState>()
    var loginState = _loginState

    val subscribeFormIsValid = MediatorLiveData<Boolean>()
    val loginFormIsValid = MediatorLiveData<Boolean>()

    private fun isSubscribeValid(): Boolean {
        return subscribeEmail.value != null
                && subscribeEmail.value!!.isNotEmpty()
                && subscribePassword.value != null
                && subscribePassword.value!!.isNotEmpty()
                && subscribePassword.value!!.length > MIN_PASS_SIZE
    }
    private fun isLoginValid(): Boolean {
        return loginEmail.value != null
                && loginEmail.value!!.isNotEmpty()
                && loginPassword.value != null
                && loginPassword.value!!.isNotEmpty()
                && loginPassword.value!!.length > MIN_PASS_SIZE
    }
    private fun resetSubscribeState() {
        _subscribeState.value = null
    }
    private fun resetLoginState() {
        _loginState.value = null
    }
    init {
    //Subscribe validation
        subscribeFormIsValid.addSource(subscribeEmail) {
            resetSubscribeState()
            subscribeFormIsValid.value = isSubscribeValid()
        }
        subscribeFormIsValid.addSource(subscribePassword) {
            resetSubscribeState()
            subscribeFormIsValid.value = isSubscribeValid()
        }


    //Login validation
        loginFormIsValid.addSource(loginEmail) {
            resetLoginState()
            loginFormIsValid.value = isLoginValid()
        }

        loginFormIsValid.addSource(loginPassword) {
            resetLoginState()
            loginFormIsValid.value = isLoginValid()
        }
    //Check if user is already logged in
        val user = auth.currentUser
        if (user != null) {
            _loginState.value = AuthState.Success(user)
        }
    }
    fun login() {
        _loginState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(loginEmail.value!!, loginPassword.value!!)
            .addOnCompleteListener {
                    task -> if (task.isSuccessful) {
                        _loginState.value = AuthState.Success( auth.currentUser )
                    } else {
                        _loginState.value = AuthState.Error( task.exception?.message!! )
                    }
            }
    }
    fun subscribe() {
        _subscribeState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(subscribeEmail.value!!, subscribePassword.value!!)
            .addOnCompleteListener {
                    task -> if (task.isSuccessful) {
                        _subscribeState.value = AuthState.Success(auth.currentUser)
            } else {
                _subscribeState.value = AuthState.Error( task.exception?.message!! )
            }
            }
    }
}






