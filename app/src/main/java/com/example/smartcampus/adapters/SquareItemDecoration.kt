package com.example.smartcampus.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SquareItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spanCount = (parent.layoutManager as? androidx.recyclerview.widget.GridLayoutManager)?.spanCount ?: 4
        val spacing = 8 // dp
        val column = position % spanCount

        // Convert dp to pixels
        val spacingInPixels = (spacing * view.context.resources.displayMetrics.density).toInt()

        // Set equal height to width for square items
        if (parent.width > 0) {
            val itemSize = (parent.width - (spanCount + 1) * spacingInPixels) / spanCount
            val params = view.layoutParams
            params.width = itemSize
            params.height = itemSize
            view.layoutParams = params
        } else {
            // If parent width is not yet measured, post a delayed update
            parent.post {
                parent.adapter?.notifyDataSetChanged()
            }
        }

        // Add spacing between items
        outRect.left = if (column == 0) spacingInPixels else spacingInPixels / 2
        outRect.right = if (column == spanCount - 1) spacingInPixels else spacingInPixels / 2
        outRect.top = spacingInPixels
        outRect.bottom = spacingInPixels
    }
} 