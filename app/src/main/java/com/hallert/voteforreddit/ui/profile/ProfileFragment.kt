package com.hallert.voteforreddit.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment: Fragment() {
    lateinit var textView: TextView
    lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        textView = root.findViewById(R.id.textView)
        button = root.findViewById(R.id.logout_button)
        button.setOnClickListener {
            logout()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoginText()
    }

    private fun setLoginText() {
        if (!RedditApp.accountHelper.isAuthenticated() || RedditApp.accountHelper.reddit.authMethod.isUserless) {
            textView.text = "Not signed in"
            button.visibility = View.INVISIBLE
        } else {
            textView.text = "Logged in as: " + RedditApp.accountHelper.reddit.me().username
            button.visibility = View.VISIBLE
        }
    }

    private fun logout() {
        CoroutineScope(IO).launch{
            RedditApp.accountHelper.reddit.authManager.revokeRefreshToken()
            RedditApp.accountHelper.reddit.authManager.revokeAccessToken()
            RedditApp.accountHelper.switchToUserless()

            withContext(Main) {
                setLoginText()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setLoginText()
    }
}
