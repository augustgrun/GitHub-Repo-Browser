package com.augustg.githubrepobrowser.adapters

import android.content.Context
import android.view.View
import android.widget.TextView
import com.augustg.githubrepobrowser.R
import com.augustg.githubrepobrowser.adapters.truffleShuffle.TruffleShuffleAdapter
import com.augustg.githubrepobrowser.api.Issue

class IssuesAdapter(
    cardDetails: ArrayList<Issue>,
    mContext: Context,
    layout: Int,
    dashboardLayout: Int
) :
    TruffleShuffleAdapter<Issue>(cardDetails, mContext, layout, dashboardLayout) {

    override fun getViewContent(view: View, cardContent: Issue) {
        (view.findViewById<View>(R.id.title_field) as TextView?)?.text = cardContent.title
        (view.findViewById<View>(R.id.description_field) as TextView?)?.text = "#${cardContent.number} opened by ${cardContent.user.login}"
        (view.findViewById<View>(R.id.number_field) as TextView?)?.text = "#${cardContent.number}"
        (view.findViewById<View>(R.id.comment_field) as TextView?)?.text = cardContent.commentCount.toString()
        (view.findViewById<View>(R.id.body_field) as TextView?)?.text = cardContent.body
    }
}