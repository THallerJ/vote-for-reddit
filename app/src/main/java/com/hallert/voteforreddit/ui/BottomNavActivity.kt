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
import com.hallert.voteforreddit.ui.inbox.InboxFragment
import com.hallert.voteforreddit.ui.submission.sort.SubmissionSortFragment
import com.hallert.voteforreddit.ui.profile.ProfileFragment
import com.hallert.voteforreddit.ui.submission.SubmissionsFragment
import com.hallert.voteforreddit.ui.subreddits.SubredditsFragment
import com.hallert.voteforreddit.user.UserManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod
import javax.inject.Inject

private const val ROOT_FRAGMENT: String = "root_fragment"
private const val PROFILE_FRAGMENT_TAG: String = "profile_fragment"
private const val INBOX_FRAGMENT_TAG: String = "inbox_fragment"

private const val CURRENT_FRAGMENT_TAG: String = "current_fragment_tag"

private const val CURRENT_USER_TAG: String = "current_user"

private const val TITLE_TEXT: String = "title_text"
private const val SUBREDDIT_TITLE_TEXT: String = "subreddit_text"
private const val LOGIN_REQUEST_CODE = 0

@AndroidEntryPoint
class BottomNavActivity :
    AppCompatActivity(),
    SubredditsFragment.SubredditFragmentObserver,
    SubmissionsFragment.SubmissionFragmentObserver,
    SubmissionSortFragment.SortingFragmentObserver {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbarTitleTextView: TextView
    private lateinit var sortText: TextView

    private lateinit var subredditTitle: String

    private var doLoadFrontpage = true

    private lateinit var currentUser: String

    @Inject
    lateinit var userManager: UserManager

    private lateinit var currentFragmentTag: String

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbarTitleTextView = findViewById(R.id.bottom_nav_title)

        if (savedInstanceState == null) {
            currentUser = userManager.currentUser()
            subredditTitle = getString(R.string.frontpage)
            toolbarTitleTextView.text = subredditTitle
            currentFragmentTag = ROOT_FRAGMENT
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, SubmissionsFragment(), ROOT_FRAGMENT).commit()
        } else {
            currentUser = savedInstanceState.getString(CURRENT_USER_TAG)!!
            currentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG)!!
            toolbarTitleTextView.text = savedInstanceState.getString(TITLE_TEXT)
            subredditTitle = savedInstanceState.getString(SUBREDDIT_TITLE_TEXT)!!
        }

        bottomNav = findViewById(R.id.bottom_navigation_bar)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

        sortText = findViewById(R.id.sort_text)
        sort.setOnClickListener { sort() }
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

    @ExperimentalCoroutinesApi
    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_posts -> {
                if ((doLoadFrontpage && (subredditTitle != getString(R.string.frontpage)))
                    || (currentUser != userManager.currentUser())
                ) {
                    val fragment: SubmissionsFragment = getSubmissionFragment()
                    sortText.text = "Hot"
                    subredditTitle = getString(R.string.frontpage)
                    currentUser = userManager.currentUser()
                    fragment.openFrontpage()
                }

                toolbarTitleTextView.text = subredditTitle
                switchFragments(SubmissionsFragment(), ROOT_FRAGMENT)
                doLoadFrontpage = true
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
                sheet.show(supportFragmentManager, "subreddit_bottom_sheet")

                return@OnNavigationItemSelectedListener false
            }
            R.id.nav_inbox -> {
                if (!userManager.isUserless()) {
                    toolbarTitleTextView.text = getString(R.string.inbox)
                    switchFragments(InboxFragment(), INBOX_FRAGMENT_TAG)
                    doLoadFrontpage = false
                } else {
                    loginNewUser()
                }
            }
            R.id.nav_profile -> {
                if (!userManager.isUserless()) {
                    toolbarTitleTextView.text = getString(R.string.profile)
                    switchFragments(ProfileFragment(), PROFILE_FRAGMENT_TAG)
                    doLoadFrontpage = false
                } else {
                    loginNewUser()
                }
            }
        }
        true
    }

    fun loginNewUser() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, LOGIN_REQUEST_CODE)
    }

    @ExperimentalCoroutinesApi
    override fun onSubredditSelected(selection: String) {
        subredditTitle = selection
        toolbarTitleTextView.text = subredditTitle
        getSubmissionFragment().openSubreddit(selection)
        sortText.text = "Hot"
        switchFragments(SubmissionsFragment(), ROOT_FRAGMENT)
        doLoadFrontpage = false
        bottomNav.selectedItemId = R.id.nav_posts
    }

    override fun onFrontPageSelected() {
        doLoadFrontpage = true
        sortText.text = "Hot"
        bottomNav.selectedItemId = R.id.nav_posts
    }

    private fun getSubmissionFragment(): SubmissionsFragment {
        return supportFragmentManager.findFragmentByTag(ROOT_FRAGMENT) as SubmissionsFragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST_CODE) {
            switchFragments(SubmissionsFragment(), ROOT_FRAGMENT)
            bottomNav.selectedItemId = R.id.nav_posts
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CURRENT_FRAGMENT_TAG, currentFragmentTag)
        outState.putString(TITLE_TEXT, toolbarTitleTextView.text.toString())
        outState.putString(SUBREDDIT_TITLE_TEXT, subredditTitle)
        outState.putString(CURRENT_USER_TAG, currentUser)

        super.onSaveInstanceState(outState)
    }

    override fun loginUser() {
        loginNewUser()
    }

    override fun sort() {
        val sheet = SubmissionSortFragment()
        sheet.show(supportFragmentManager, "sorting_bottom_sheet")
    }

    @ExperimentalCoroutinesApi
    override fun sortSelected(sort: SubredditSort, timePeriod: TimePeriod?) {
        if (timePeriod == null) {
            sortText.text = sort.toString()
        } else {
            sortText.text = "$sort $timePeriod"
        }

        getSubmissionFragment().changeSort(sort, timePeriod)
    }
}