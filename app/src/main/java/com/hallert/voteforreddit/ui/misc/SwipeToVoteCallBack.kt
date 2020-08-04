import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hallert.voteforreddit.R

abstract class SwipeToVoteCallBack(
    private val context: Context?,
    private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val swipeDistance = 200f
        var newDx: Float = dX;
        if (newDx >= swipeDistance) {
            newDx = swipeDistance
        } else if (newDx <= -swipeDistance) {
            newDx = -swipeDistance
        }

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            newDx,
            dY,
            actionState,
            isCurrentlyActive
        );

        val itemView = viewHolder.itemView

        val upvoteBackground = context?.resources?.getColor(R.color.upvoteColor, null)?.let {
            ColorDrawable(
                it
            )
        }
        val downvoteBackground = context?.resources?.getColor(R.color.downvoteColor, null)?.let {
            ColorDrawable(
                it
            )
        }

        // val upvoteIcon = context?.resources?.getDrawable(R.drawable.ic_upvote, null)

        when {
            // swipe to the right
            dX > 0 -> {
                upvoteBackground?.setBounds(
                    itemView.left,
                    itemView.top,
                    if (dX <= swipeDistance) itemView.left + dX.toInt() else swipeDistance.toInt(),
                    itemView.bottom
                )
            }
            // swipe to the left
            dX < 0 -> {
                downvoteBackground?.setBounds(
                    if (dX <= -swipeDistance) itemView.right - swipeDistance.toInt() else itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom

                    //itemView.right - swipeDistance.toInt()
                )
            }
            else -> {
                upvoteBackground?.setBounds(0, 0, 0, 0)
            }
        }

        downvoteBackground?.draw(c)
        upvoteBackground?.draw(c)

        //upvoteIcon.draw(c)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.RIGHT) {
            onSwipeRight()
        } else if (direction == ItemTouchHelper.LEFT) {
            onSwipeLeft()
        }

        adapter.notifyItemChanged(viewHolder.adapterPosition)
    }


    abstract fun onSwipeLeft()
    abstract fun onSwipeRight()
}