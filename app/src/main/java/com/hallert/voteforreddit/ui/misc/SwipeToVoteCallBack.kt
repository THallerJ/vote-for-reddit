import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeToVoteCallBack constructor(
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

        var newDx: Float = dX;
        if (newDx >= 200f) {
            newDx = 200f
        } else if (newDx <= -200) {
            newDx = -200f
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