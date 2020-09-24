package com.hallert.voteforreddit.ui.submission

class SubmissionDomainParser {
    fun parse(domain: String): SubmissionDomainType {
        return if ("youtube.com" in domain || "youtu.be" in domain) {
            SubmissionDomainType.YOUTUBE
        } else if ("i.redd.it" in domain || "i.imgur.com" in domain) {
            SubmissionDomainType.IMAGE
        } else if ("v.redd.it" in domain) {
            SubmissionDomainType.VIDEO
        } else if ("twitter.com" in domain) {
            SubmissionDomainType.TWITTER
        } else {
            SubmissionDomainType.INTERNET
        }
    }
}

enum class SubmissionDomainType {
    YOUTUBE, TWITTER, INTERNET, VIDEO, IMAGE
}
