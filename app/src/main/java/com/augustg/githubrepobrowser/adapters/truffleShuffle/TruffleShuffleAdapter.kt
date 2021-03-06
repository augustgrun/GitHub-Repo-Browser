package com.augustg.githubrepobrowser.adapters.truffleShuffle

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.intuit.truffleshuffle.CardViewGroup
import java.util.ArrayList

// I forked Truffle Shuffle and added a feature :)
// I'm using it here to display issues of the selected repo
// https://github.com/intuit/truffle-shuffle/pull/13

/**
 * The adapter for mapping the array of card contents to the specific views in the card gallery
 * Note that the array of contents is generic.
 * If a separate dashboard layout is provided, it will be used when no cards are selected
 * Created by Katie Levy
 */
abstract class TruffleShuffleAdapter<T : Any?>(
    private val cardContentArray: ArrayList<T>,
    mContext: Context,
    private val layout: Int,
    private val dashboardLayout: Int = layout // added feature
) : BaseAdapter() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val separateLayoutProvided = (dashboardLayout != layout)
    private var galleryState = CardViewGroup.GalleryState.DETAIL

    /**
     * Get the count of items in the adapter
     * @return number of items in the adapter
     */
    override fun getCount(): Int {
        return cardContentArray.size
    }

    /**
     * Get a specific item in the adapter's array
     * @param i index of the item
     * @return the item in the array or null
     */
    override fun getItem(i: Int): T? {
        return if (i < count) {
            cardContentArray[i]
        } else {
            null
        }
    }

    /**
     * Get the id of the item
     * @param i index of the item
     * @return the id for that item
     */
    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    /**
     * Inflate the card view with the specific content in the adapter's array
     * This can be customized for the view in @see cardGallery.CustomizeAdapter
     * @param position the index of the card
     * @param cardView the card's view
     * @param parent the parent view of the card's view
     * @return the view with the content filled into the view
     */
    override fun getView(position: Int, cardView: View?, parent: ViewGroup?): View {
        val view = when (galleryState) {
            CardViewGroup.GalleryState.DETAIL -> cardView ?: layoutInflater.inflate(layout, parent, false)
            CardViewGroup.GalleryState.DASHBOARD -> cardView ?: layoutInflater.inflate(dashboardLayout, parent, false)
        }
        getViewContent(view, cardContentArray[position])
        return view
    }

    /**
     * Add the content to the card's view
     * @param view the card's view
     * @param cardContent the data object of card content
     */
    abstract fun getViewContent(view: View, cardContent: T)

    /**
     * Toggle the layout on click if a dashboard layout was specified
     * @param cardViewGroup the custom view group of all the cards
     */
    private fun toggleLayout(cardViewGroup: CardViewGroup) {
        galleryState = when (cardViewGroup.galleryState) {
            CardViewGroup.GalleryState.DETAIL -> CardViewGroup.GalleryState.DASHBOARD
            CardViewGroup.GalleryState.DASHBOARD -> CardViewGroup.GalleryState.DETAIL
        }
        cardViewGroup.removeAllViews()
        setupAdapter(cardViewGroup)
    }

    /**
     * Setup the views inside the card view group with the data in the adapter
     * @param cardViewGroup the custom view group of all the cards
     */
    fun setupAdapter(cardViewGroup: CardViewGroup) {
        for (index in cardContentArray.indices) {
            val cardView = getView(index, null, cardViewGroup)
                .apply { tag = index }
            cardViewGroup.addView(cardView)
            cardView.setOnTouchListener(View.OnTouchListener { view, event ->
                return@OnTouchListener when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> true
                    MotionEvent.ACTION_UP -> {
                        if (separateLayoutProvided) toggleLayout(cardViewGroup)
                        cardViewGroup.click(view.tag as Int)
                        true
                    }
                    else -> false
                }
            })
        }
    }
}