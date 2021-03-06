package com.augustg.githubrepobrowser


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.augustg.githubrepobrowser.api.Issue
import com.augustg.githubrepobrowser.adapters.IssuesAdapter
import com.augustg.githubrepobrowser.databinding.FragmentIssuesBinding
import com.intuit.truffleshuffle.CardViewGroup
import kotlinx.android.synthetic.main.fragment_issues.*

class IssuesFragment : Fragment() {

    private lateinit var binding: FragmentIssuesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIssuesBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel by viewModels<GitHubViewModel>()
        binding.viewModel = viewModel
        val args: IssuesFragmentArgs by navArgs()

        // Fetch issues and observe response
        viewModel.fetchIssues(args.name)
        viewModel.issues.observe(this, Observer { setupCardGallery(it) })

        // observe network errors and toast response code
        viewModel.networkErrorMessage.observe(this, Observer { event ->
            event?.getContent()?.let { Toast.makeText(activity?.applicationContext, it, Toast.LENGTH_LONG).show() }
        })
    }

    // This layout will be used when an issue is selected
    private fun getInnerCardLayoutDetail(): Int = R.layout.issue_card_detail

    // This layout will be used when no issues are selected
    private fun getInnerCardLayoutDashboard(): Int = R.layout.issue_card_dashboard

    /**
     * Sets up a Truffle Shuffle gallery of issue cards
     *
     * @param issues List of issues to be displayed
     */
    private fun setupCardGallery(issues: MutableList<Issue>) {
        val cardLayout = truffle_shuffle as CardViewGroup
        IssuesAdapter(
            issues as ArrayList<Issue>,
            truffle_shuffle.context,
            getInnerCardLayoutDetail(),
            getInnerCardLayoutDashboard() // this fourth parameter demos the added feature
        )
            .setupAdapter(cardLayout)
    }
}