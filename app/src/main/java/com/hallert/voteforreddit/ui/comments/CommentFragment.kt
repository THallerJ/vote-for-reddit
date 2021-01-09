package com.hallert.voteforreddit.ui.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hallert.voteforreddit.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import net.dean.jraw.models.NestedIdentifiable

@AndroidEntryPoint
class CommentFragment : Fragment(), CommentClickListener {
    private val commentsViewModel: CommentViewModel by viewModels()

    private lateinit var adapter: CommentAdapter

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_comments, container, false)
        val id = arguments?.getString("submission_id")
        commentsViewModel.submissionId = id.toString()

        adapter = CommentAdapter(this)

        commentsViewModel.comments.observe(viewLifecycleOwner, Observer {
            val data = mutableListOf<NestedIdentifiable>()

            commentsViewModel.comments.value?.forEach { entity ->
                entity.children.forEach { child ->
                    data.add(child)
                }
            }

            adapter.data = data.toList()
        })

        return root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView(commentsViewModel.submissionId)
        super.onViewCreated(view, savedInstanceState)
    }

    @ExperimentalCoroutinesApi
    fun initRecyclerView(id: String) {
        comment_recycler_view.layoutManager = LinearLayoutManager(context)
        comment_recycler_view.adapter = adapter
        commentsViewModel.initComments(id)
    }

    override fun onItemClick() {
        TODO("Not yet implemented")
    }
}