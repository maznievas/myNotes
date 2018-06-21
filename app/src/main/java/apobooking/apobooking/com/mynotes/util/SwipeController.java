package apobooking.apobooking.com.mynotes.util;

// SwipeController.java

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import apobooking.apobooking.com.mynotes.R;

public class SwipeController extends ItemTouchHelper.SimpleCallback  {

    NotesAdapter adapter;
    private static final float buttonWidth = 300;
    private Context context;
    private final int DELTA_DP = 2;
    Bitmap trash;

    public SwipeController(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public SwipeController(NotesAdapter adapter, Context context) {
        super(0, ItemTouchHelper.LEFT);
        this.adapter = adapter;
        this.context = context;
        trash = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_delete);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Задайте флагам значение 0, если они не должны перемещаться
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.LEFT;
        // Используем стандартный метод для создания битмаски флагов движения
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition(); //swiped position

        if (direction == ItemTouchHelper.LEFT) { //swipe left
            adapter.remove(position);
        }
    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        viewHolder.itemView.setTranslationX(dX);
        drawButtons(c, viewHolder);
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding,
                itemView.getTop() + context.getResources().getDimension(R.dimen.small_padding) + DELTA_DP,
                itemView.getRight() - context.getResources().getDimension(R.dimen.small_padding) - DELTA_DP,
                itemView.getBottom() - context.getResources().getDimension(R.dimen.small_padding) - DELTA_DP);
        p.setColor(Color.RED);
        c.drawRect(rightButton, p);
        c.drawRoundRect(rightButton, corners, corners, p);
        drawIcon("DELETE", c, rightButton, p);
    }

    private void drawIcon(String text, Canvas c, RectF button, Paint p) {
        float textSize = 45;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }
}
