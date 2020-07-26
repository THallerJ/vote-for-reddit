package com.hallert.voteforreddit.user

import net.dean.jraw.oauth.AccountHelper
import javax.inject.Inject

class UserManager @Inject constructor(private val accountHelper: AccountHelper) {
    fun currentUser(): String {
        if (isUserless()) {
            return "Userless"
        } else {
            return accountHelper.reddit.me().username
        }
    }

    fun isUserless(): Boolean {
        return accountHelper.reddit.authMethod.isUserless
    }
}
