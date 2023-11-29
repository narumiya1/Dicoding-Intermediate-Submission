package com.ackerman.intermediatesubmission.data.view_ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ackerman.intermediatesubmission.MainActivity
import com.ackerman.intermediatesubmission.data.local.UserModel
import com.ackerman.intermediatesubmission.data.utils.ViewModelFactory
import com.ackerman.intermediatesubmission.databinding.ActivityLognBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private val loginBinding: ActivityLognBinding by lazy {
        ActivityLognBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(loginBinding.root)

        viewModelSetUp()
        playAnimation()

        loginBinding.btnLogin.setOnClickListener {
            actionLogin()
        }

        loginBinding.reg.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

    }

    private fun playAnimation() {

        // Title
        val titleLogin =
            ObjectAnimator.ofFloat(loginBinding.loginTitle, View.ALPHA, 1f).setDuration(500)
        val subTitleLogin =
            ObjectAnimator.ofFloat(loginBinding.subTitle, View.ALPHA, 1f).setDuration(500)

        // Email
        val textEmail =
            ObjectAnimator.ofFloat(loginBinding.tvEmailUser, View.ALPHA, 1f).setDuration(500)
        val inputEmail =
            ObjectAnimator.ofFloat(loginBinding.tlEmailUser, View.ALPHA, 1f).setDuration(500)

        // Password
        val textPassword =
            ObjectAnimator.ofFloat(loginBinding.tvPasswordUser, View.ALPHA, 1f).setDuration(500)
        val inputPassword =
            ObjectAnimator.ofFloat(loginBinding.tlPasswordUser, View.ALPHA, 1f).setDuration(500)

        // Button
        val buttonLogin =
            ObjectAnimator.ofFloat(loginBinding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val reg =
            ObjectAnimator.ofFloat(loginBinding.reg, View.ALPHA, 1f).setDuration(500)

        val title = AnimatorSet().apply {
            playTogether(titleLogin, subTitleLogin)
        }

        val email = AnimatorSet().apply {
            playTogether(textEmail, inputEmail)
        }

        val password = AnimatorSet().apply {
            playTogether(textPassword, inputPassword)
        }

        val button = AnimatorSet().apply {
            playTogether(buttonLogin,reg)
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                email,
                password,
                button
            )
            start()
        }
    }

    private fun actionLogin() {
        val email = loginBinding.edtEmailUser.text.toString()
        val password = loginBinding.edtPasswordUser.text.toString()

        loginViewModel.loginUser(email, password).observe(this) {
            when (it) {
                is com.ackerman.intermediatesubmission.data.utils.Result.Loading -> showLoading(true)

                is com.ackerman.intermediatesubmission.data.utils.Result.Success->{
                    showLoading(false)
                    val response = it.data

                    saveDataUser(UserModel
                        (response.result.name, response.result.token, true))
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }


                is com.ackerman.intermediatesubmission.data.utils.Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "User Gagal Login", Toast.LENGTH_LONG).show()
                }

                else -> {}
            }

        }
    }

    private fun saveDataUser(userModel: UserModel) {
        loginViewModel.saveUser(userModel)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            loginBinding.progressCircular.visibility = View.VISIBLE
        } else {
            loginBinding.progressCircular.visibility = View.GONE
        }
    }
    private fun viewModelSetUp() {
        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }
}