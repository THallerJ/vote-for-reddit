package com.hallert.voteforreddit.ui.authentication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.RedditApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import net.dean.jraw.oauth.OAuthException
import net.dean.jraw.oauth.StatefulAuthHelper
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Timber.plant()

        val webView = findViewById<WebView>(R.id.webView)

        webView.clearCache(true)
        webView.clearHistory()

        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()

        val helper = RedditApp.accountHelper.switchToNewUser()

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (helper.isFinalRedirectUrl(url!!)) {
                    webView.stopLoading()
                    webView.visibility = View.GONE

                    CoroutineScope(IO).launch {
                        authenticateUser(url, helper)
                    }
                }
            }
        }

        val requestRefreshToken = true
        val useMobileSite = true

        // TODO: Update scope to appropriate values
        val scope = arrayOf("read", "identity", "subscribe", "mysubreddits")
        val authUrl = helper.getAuthorizationUrl(requestRefreshToken, useMobileSite, *scope)

        webView.loadUrl(authUrl)
    }

    private suspend fun authenticateUser(url: String, helper: StatefulAuthHelper) {
        val host = this@LoginActivity

        var success = false

        try {
            helper.onUserChallenge(url)
            success = true
        } catch (e: OAuthException) {
            Timber.tag("User not authenticated:").e(e)
        }

        host.setResult(if (success) Activity.RESULT_OK else Activity.RESULT_CANCELED, Intent())
        host.finish()
    }
}