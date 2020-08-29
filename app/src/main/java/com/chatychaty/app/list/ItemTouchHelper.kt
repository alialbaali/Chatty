package com.chatychaty.app.list

import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chatychaty.app.R
import kotlin.math.roundToInt


class ChatItemTouchHelper(private val listener: ChatItemTouchHelperListener, private val context: Context) : ItemTouchHelper.Callback() {

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        listener.onMoveViewHolder(viewHolder as ListRVAdapter.ChatItemViewHolder, target as ListRVAdapter.ChatItemViewHolder)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = listener.onSwipeViewHolder(viewHolder as ListRVAdapter.ChatItemViewHolder)

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        listener.onClearViewHolder(viewHolder as ListRVAdapter.ChatItemViewHolder)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val paint = Paint()


        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX > 0) {

//                val icon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_delete_black_24dp)
                val drawable = context.getDrawable(R.drawable.ic_delete_black_24dp)


                val icon = Bitmap.createBitmap(drawable!!.intrinsicWidth , drawable!!.intrinsicHeight, Bitmap.Config.ARGB_8888)

                val canvas = Canvas(icon)




                drawable.setBounds(0, 0, canvas.width, canvas.height)

                drawable.draw(canvas)


                paint.color = Color.parseColor("#9C27B0")

                val itemView = viewHolder.itemView

                c.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat(), paint)

                c.drawBitmap(icon, itemView.left.toFloat(), itemView.top.toFloat(), paint)

            } else {


            }


        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private val alphaFull = 1.0f
//    override fun onChildDraw(
//        c: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            // Get RecyclerView item from the ViewHolder
//            val itemView: View = viewHolder.itemView
//            val paint = Paint()
//
//
//            val icon: Bitmap
//
//
//            if (dX > 0) {
//                /* Note, ApplicationManager is a helper class I created
//               myself to get a context outside an Activity class -
//               feel free to use your own method */
////                icon = BitmapFactory.decodeResource(context.resources, R.drawable.archive_arrow_down_outline)
//
//                /* Set your color for positive displacement */
////                paint.color = Color.parseColor("#9C27B0")
//
//                // Draw Rect with varying right side, equal to displacement dX
////                c.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat(), paint)
//
//                // Set the image icon for Right swipe
////                c.drawBitmap(
////                    icon,
////                    itemView.left.toFloat() + convertDpToPx(16),
////                    itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
////                    paint
////                )
//            } else {
////                icon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_delete_black_24dp)
//
//                /* Set your color for negative displacement */
//                paint.color = Color.RED
//
//                // Draw Rect with varying left side, equal to the item's right side
//                // plus negative displacement dX
////                c.drawRect(
////                    itemView.right.toFloat() + dX, itemView.top.toFloat(),
////                    itemView.right.toFloat(), itemView.bottom.toFloat(), paint
////                )
//
//                //Set the image icon for Left swipe
////                c.drawBitmap(
////                    icon,
////                    itemView.right.toFloat() - convertDpToPx(16) - icon.width,
////                    itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
////                    paint
////                )
//            }
//
//            // Fade out the view as it is swiped out of the parent's bounds
////            val alpha = alphaFull - abs(dX) / viewHolder.itemView.width.toFloat()
////            viewHolder.itemView.alpha = alpha
////            viewHolder.itemView.translationX = dX
//        } else {
//            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//        }
//    }

    private fun convertDpToPx(dp: Int): Int {
        return (dp * (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}


interface ChatItemTouchHelperListener {

    fun onMoveViewHolder(fromViewHolder: ListRVAdapter.ChatItemViewHolder, toViewHolder: ListRVAdapter.ChatItemViewHolder)

    fun onSwipeViewHolder(viewHolder: ListRVAdapter.ChatItemViewHolder)

    fun onClearViewHolder(viewHolder: ListRVAdapter.ChatItemViewHolder)

}
