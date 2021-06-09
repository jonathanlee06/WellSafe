package com.jonathanlee.wellsafe.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jonathanlee.wellsafe.*
import com.jonathanlee.wellsafe.data.model.Stats
import com.jonathanlee.wellsafe.databinding.ActivitySplashBinding
import com.jonathanlee.wellsafe.utils.Injection

class SplashActivity : BaseActivity(), SplashContract.View {

    private lateinit var spinner: ProgressBar
    override lateinit var presenter: SplashContract.Presenter
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPresenter()
        initView()
    }

    override fun onGetMalaysiaDataFailure(errorMsg: String?) {
        Toast.makeText(this, "API Failed!", Toast.LENGTH_SHORT).show()
    }

    override fun onGetMalaysiaDataSuccess(stats: Stats) {
        if (checkLoginStatus()) {
            navigateTo(Location.REGISTER)
        } else {
            navigateTo(Location.HOME)
        }
    }

    private fun checkLoginStatus(): Boolean {
        // Initialise Firebase
        val authFirebase = FirebaseAuth.getInstance()
        val currentUser = authFirebase.currentUser
        return currentUser == null
    }

    private fun initPresenter() {
        SplashPresenter(
            Injection.provideStatsRepository(),
            this
        )
        presenter = SplashPresenter(
            Injection.provideStatsRepository(),
            this
        )
        presenter.getMalaysiaData()
    }

    private fun initView() {
        spinner = binding.loading
    }

    private fun navigateTo(where: Location) {
        val intent: Intent? = when (where) {
            Location.HOME -> {
                Intent(this, HomeActivity::class.java)
            }
            Location.LOGIN -> {
                Intent(this, LogInActivity::class.java)
            }
            Location.REGISTER -> {
                Intent(this, SignUpActivity::class.java)
            }
        }
        intent?.let {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }

    private enum class Location {
        LOGIN,
        REGISTER,
        HOME
    }
}