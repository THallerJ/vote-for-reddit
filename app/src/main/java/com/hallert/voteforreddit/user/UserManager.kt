package com.hallert.voteforreddit.user

import com.hallert.voteforreddit.RedditApp

class UserManager {
    companion object {
        fun currentUser(): String {
            if (isUserless()) {
                return "Userless"
            } else {
                return RedditApp.accountHelper.reddit.me().username
            }
        }

        fun isUserless(): Boolean {
            return RedditApp.accountHelper.reddit.authMethod.isUserless
        }
    }
}