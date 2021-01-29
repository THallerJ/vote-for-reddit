package com.hallert.voteforreddit.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import com.hallert.voteforreddit.ui.authentication.LoginActivity
import com.hallert.voteforreddit.ui.inbox.InboxFragment
import com.hallert.voteforreddit.ui.profile.ProfileFragment
import com.hallert.voteforreddit.ui.search.SearchFragment
import com.hallert.voteforreddit.ui.submission.SubmissionsFragment
import com.hallert.voteforreddit.ui.submission.actionbottomsheet.ActionsFragment
import com.hallert.voteforreddit.ui.submission.sort.SubmissionSortFragment
import com.hallert.voteforreddit.ui.subreddits.SubredditFragment
import com.hallert.voteforreddit.user.UserManager
import com.hallert.voteforreddit.util.StringFormatUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.dean.jraw.models.SearchSort
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod
import javax.inject.Inject

private const val ROOT_FRAGMENT: String = "root_fragment"
private const val PROFILE_FRAGMENT_TAG: String = "profile_fragment"
private const val INBOX_FRAGMENT_TAG: String = "inbox_fragment"
private const val SUBREDDIT_SHEET_TAG: String = "subreddit_sheet_tag"
private const val SEARCH_SHEET_TAG: String = "search_sheet_tag"
private const val SUBMISSION_SORT_TAG: String = "submission_sort"
private const val ACTION_MENU_TAG: String = "action_menu_tag"

private const val CURRENT_FRAGMENT_TAG: String = "current_fragment_tag"

private const val CURRENT_USER_TAG: String = "current_user"

private const val TITLE_TEXT: String = "title_text"
private const val SUBREDDIT_TITLE_TEXT: String = "subreddit_text"
private const val LOGIN_REQUEST_CODE = 0

private const val SEARCH_BUNDLE = "search_bundle"
private const val LAYERED_ACTIVITY_FRAGMENT_INTENT = "layered_activity_intent"
private const val LAYERED_ACTIVITY_SUBMISSION_INTENT = "layered_activity_submission_intent"

private const val COMMENTS_FRAGMENT_TAG = "comments_fragment"

@AndroidEntryPoint
class BottomNavActivity :
    AppCompatActivity(),
    SubredditFragment.SubredditFragmentObserver,
    SubmissionsFragment.SubmissionFragmentObserver,
    SubmissionSortFragment.SortingFragmentObserver,
    SearchFragment.SearchFragmentObserver {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbarTitleTextView: TextView
    private lateinit var sortText: TextView
    private lateinit var sortLayout: LinearLayout

    private lateinit var subredditTitle: String

    private var doLoadFrontpage = true
    private var searchFlag = false

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

        sidebar.setOnClickListener {
            Toast.makeText(this, "TODO: Open sidemenu", Toast.LENGTH_SHORT).show()
        }

        sortLayout = findViewById(R.id.sort)
    }

    private fun switchFragments(
        newFragment: Fragment,
        newFragmentTag: String,
        titleText: String?,
        sortVisible: Boolean
    ) {
        if (titleText != null)
            toolbarTitleTextView.text = titleText

        if (sortVisible) {
            sortLayout.visibility = View.VISIBLE
            sidebar.visibility = View.VISIBLE
        }
        else {
            sortLayout.visibility = View.GONE
            sidebar.visibility = View.GONE
        }

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
                    searchFlag = false
                    sortText.text = RedditApp.appContext.getString(R.string.hot)
                    subredditTitle = getString(R.string.frontpage)
                    currentUser = userManager.currentUser()
                    fragment.openMultireddit(RedditApp.appContext.getString(R.string.frontpage))
                }

                switchFragments(SubmissionsFragment(), ROOT_FRAGMENT, subredditTitle, !searchFlag)
                doLoadFrontpage = true
            }
            R.id.nav_search -> {
                val sheet = SearchFragment()
                val bundle = Bundle()
                bundle.putString(SEARCH_BUNDLE, subredditTitle)
                sheet.arguments = bundle
                sheet.show(supportFragmentManager, SEARCH_SHEET_TAG)
                return@OnNavigationItemSelectedListener false
            }
            R.id.nav_subs -> {
                val sheet = SubredditFragment()
                sheet.show(supportFragmentManager, SUBREDDIT_SHEET_TAG)

                return@OnNavigationItemSelectedListener false
            }
            R.id.nav_inbox -> {
                if (!userManager.isUserless()) {
                    switchFragments(
                        InboxFragment(),
                        INBOX_FRAGMENT_TAG,
                        getString(R.string.inbox),
                        false
                    )
                    doLoadFrontpage = false
                } else {
                    loginNewUser()
                }
            }
            R.id.nav_profile -> {
                if (!userManager.isUserless()) {
                    sortLayout.visibility = View.GONE
                    switchFragments(
                        ProfileFragment(),
                        PROFILE_FRAGMENT_TAG,
                        getString(R.string.profile),
                        false
                    )
                    doLoadFrontpage = false
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

    private fun openSubmissionFragment(title: String, sortVisibile: Boolean, searchFlag: Boolean) {
        subredditTitle = title
        switchFragments(SubmissionsFragment(), ROOT_FRAGMENT, title, sortVisibile)
        this.searchFlag = searchFlag
        doLoadFrontpage = false
        bottomNav.selectedItemId = R.id.nav_posts
    }

    @ExperimentalCoroutinesApi
    private fun openSubreddit(selection: String) {
        openSubmissionFragment(selection, true, false)
        getSubmissionFragment().openSubreddit(selection)
        sortText.text = RedditApp.appContext.getString(R.string.hot)
    }

    @ExperimentalCoroutinesApi
    override fun onSubredditSelected(selection: String) {
        openSubreddit(selection)
    }

    @ExperimentalCoroutinesApi
    override fun onSubredditSearchSelected(selection: String) {
        openSubreddit(selection)
    }

    @ExperimentalCoroutinesApi
    override fun onSearch(
        query: String,
        timePeriod: TimePeriod,
        sort: SearchSort,
        subreddit: String?
    ) {
        openSubmissionFragment(query, false, true)
        getSubmissionFragment().searchReddit(query, timePeriod, sort, subreddit)
    }

    @ExperimentalCoroutinesApi
    override fun onMultiredditSelected(selection: String) {
        if (selection == RedditApp.appContext.getString(R.string.frontpage)) {
            doLoadFrontpage = true
            sortText.text = RedditApp.appContext.getString(R.string.hot)
            bottomNav.selectedItemId = R.id.nav_posts
        } else {
            subredditTitle = selection
            getSubmissionFragment().openMultireddit(selection)
            sortText.text = RedditApp.appContext.getString(R.string.hot)
            switchFragments(SubmissionsFragment(), ROOT_FRAGMENT, subredditTitle, true)
            doLoadFrontpage = false
            bottomNav.selectedItemId = R.id.nav_posts
        }
    }

    private fun getSubmissionFragment(): SubmissionsFragment {
        return supportFragmentManager.findFragmentByTag(ROOT_FRAGMENT) as SubmissionsFragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOGIN_REQUEST_CODE) {
            switchFragments(SubmissionsFragment(), ROOT_FRAGMENT, null, true)
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

    fun sort() {
        val sheet = SubmissionSortFragment()
        sheet.show(supportFragmentManager, SUBMISSION_SORT_TAG)
    }

    override fun openComments(id: String) {
        val intent = Intent(this, LayeredActivity::class.java)
        intent.putExtra(LAYERED_ACTIVITY_FRAGMENT_INTENT, COMMENTS_FRAGMENT_TAG)
        intent.putExtra(LAYERED_ACTIVITY_SUBMISSION_INTENT, id)
        startActivity(intent)
    }

    override fun openMenu() {
        val sheet = ActionsFragment()
        sheet.show(supportFragmentManager, ACTION_MENU_TAG)
    }

    @ExperimentalCoroutinesApi
    override fun sortSelected(sort: SubredditSort, timePeriod: TimePeriod?) {
        if (timePeriod == null) {
            sortText.text = StringFormatUtil.capitalizeWords("$sort")
        } else {
            sortText.text = StringFormatUtil.capitalizeWords("$sort $timePeriod")
        }

        getSubmissionFragment().changeSort(sort, timePeriod)
    }
}