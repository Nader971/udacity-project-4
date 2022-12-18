package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity
import org.koin.android.ext.android.bind

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    companion object {
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAuthenticationBinding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)

        binding.lifecycleOwner = this

        binding.btnLogin.setOnClickListener {
            launchSignInFlow()
        }

        observeAuthenticationState()

    }
    
    //Here we check if the users logged in or not, to navigate to RemindersActivity.
    private fun observeAuthenticationState() {
        viewModel.authenticationState.observe(this, Observer { authenticationState ->
          if (authenticationState == AuthenticationViewModel.AuthenticationState.AUTHENTICATED) {
              startActivity(Intent(this, RemindersActivity::class.java))
              finish()
          }

        })


    }

    private fun launchSignInFlow() {
        //Give the users the option to register and sign in with email or google accounts.
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build())

        //Create and launch sign-in intent
        startActivityForResult(

            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                providers
            ).build(), SIGN_IN_RESULT_CODE
        )

    }
}
