package com.hallert.voteforreddit.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.ui.authentication.LoginActivity
import com.hallert.voteforreddit.ui.profile.ProfileFragment
import com.hallert.voteforreddit.ui.submission.SubmissionsFragment

class BottomNavActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    private val LOGIN_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    SubmissionsFragment()).commit()
        }

        bottomNav = findViewById(R.id.bottom_navigation_bar)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_posts -> {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        SubmissionsFragment()
                    ).commit()
            }
            R.id.nav_search -> {
                Toast.makeText(
                    this@BottomNavActivity,
                    "TODO: Launch search Fragment",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            R.id.nav_subs -> {
                // TODO: Replace check with Authentication.isUserless()
                if (!RedditApp.accountHelper.reddit.authMethod.isUserless) {
                    Toast.makeText(
                        this@BottomNavActivity,
                        "TODO: Launch submit BottomSheet",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    loginNewUser()
                }
            }
            R.id.nav_inbox -> {
                // TODO: Replace check with Authentication.isUserless()
                if (!RedditApp.accountHelper.reddit.authMethod.isUserless) {
                    Toast.makeText(
                        this@BottomNavActivity,
                        "TODO: Launch messages Fragment",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    loginNewUser()
                }
            }
            R.id.nav_profile -> {
                // TODO: Replace check with Authentication.isUserless()
                if (!RedditApp.accountHelper.reddit.authMethod.isUserless) {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            ProfileFragment()
                        ).commit()
                } else {
                    loginNewUser()
                }
            }
        }
        true
    }

    private fun loginNewUser() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, LOGIN_REQUEST_CODE)
    }
}