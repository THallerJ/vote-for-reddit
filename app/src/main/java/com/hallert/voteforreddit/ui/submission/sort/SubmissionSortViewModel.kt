package com.hallert.voteforreddit.ui.submission.sort

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import net.dean.jraw.models.SubredditSort

class SubmissionSortViewModel @ViewModelInject constructor() : ViewModel() {
    var hideSort = false
    lateinit var sort: SubredditSort
}