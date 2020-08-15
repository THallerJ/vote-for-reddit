package com.hallert.voteforreddit.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.ui.authentication.LoginActivity
import com.hallert.voteforreddit.ui.profile.ProfileFragment
import com.hallert.voteforreddit.ui.submission.SubmissionsFragment
import com.hallert.voteforreddit.ui.subreddits.SubredditsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.dean.jraw.RedditClient
import net.dean.jraw.oauth.AccountHelper
import javax.inject.Inject

private const val ROOT_FRAGMENT: String = "root_fragment"
private const val PROFILE_FRAGMENT_TAG: String = "profile_fragment"

private const val CURRENT_FRAGMENT_TAG: String = "current_fragment_tag"


private const val TITLE_TEXT: String = "title_text"
private const val SUBREDDIT_TITLE_TEXT: String = "subreddit_text"
private const val LOGIN_REQUEST_CODE = 0

@AndroidEntryPoint
class BottomNavActivity : AppCompatActivity(), SubredditsFragment.SubredditFragmentListener {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbarTitleTextView: TextView

    private lateinit var subreddit_title: String

    @Inject
    lateinit var client: RedditClient

    private lateinit var currentFragmentTag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbarTitleTextView = findViewById(R.id.bottom_nav_title)

        if (savedInstanceState == null) {
            subreddit_title = getString(R.string.frontpage)
            toolbarTitleTextView.text = subreddit_title
            currentFragmentTag = ROOT_FRAGMENT
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, SubmissionsFragment(), ROOT_FRAGMENT).commit()
        } else {
            currentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG)!!
            toolbarTitleTextView.text = savedInstanceState.getString(TITLE_TEXT)
            subreddit_title = savedInstanceState.getString(SUBREDDIT_TITLE_TEXT)!!
        }

        bottomNav = findViewById(R.id.bottom_navigation_bar)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
    }

    private fun switchFragments(
        newFragment: Fragment,
        newFragmentTag: String
    ) {
        if (currentFragmentTag != newFragmentTag) {
            if (supportFragmentManager.findFragmentByTag(newFragmentTag) != null) {
                supportFragmentManager.beginTransaction()
                    .show(supportFragmentManager.findFragmentByTag(newFragmentTag)!!)
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, newFragment, newFragmentTag)
                    .commit()
            }

            if (supportFragmentManager.findFragmentByTag(currentFragmentTag) != null) {
                supportFragmentManager.beginTransaction()
                    .hide(supportFragmentManager.findFragmentByTag(currentFragmentTag)!!).commit()
            }

            currentFragmentTag = newFragmentTag
        }
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_posts -> {
                toolbarTitleTextView.text = subreddit_title
                switchFragments(SubmissionsFragment(), ROOT_FRAGMENT)
            }
            R.id.nav_search -> {
                Toast.makeText(
                    this@BottomNavActivity,
                    "TODO: Launch search Fragment",
                    Toast.LENGTH_SHORT
                )
                    .show()

                return@OnNavigationItemSelectedListener false
            }
            R.id.nav_subs -> {
                val sheet = SubredditsFragment()
                sheet.show(supportFragmentManager, "subredditBottomSheet")

                return@OnNavigationItemSelectedListener false
            }
            R.id.nav_inbox -> {
                // TODO: Replace check with Authentication.isUserless()
                if (!client.authMethod.isUserless) {
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
                if (!client.authMethod.isUserless) {
                    toolbarTitleTextView.text = getString(R.string.profile)
                    switchFragments(ProfileFragment(), PROFILE_FRAGMENT_TAG)
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

    @ExperimentalCoroutinesApi
    override fun onSubredditSelected(selection: String) {
        subreddit_title = selection
        toolbarTitleTextView.text = subreddit_title
        val fragment: SubmissionsFragment =
            supportFragmentManager.findFragmentByTag(ROOT_FRAGMENT) as SubmissionsFragment
        fragment.openSubreddit(selection)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST_CODE) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    SubmissionsFragment()
                ).commit()

            bottomNav.selectedItemId = R.id.nav_posts
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CURRENT_FRAGMENT_TAG, currentFragmentTag)
        outState.putString(TITLE_TEXT, toolbarTitleTextView.text.toString())
        outState.putString(SUBREDDIT_TITLE_TEXT, subreddit_title)

        super.onSaveInstanceState(outState)
    }
}