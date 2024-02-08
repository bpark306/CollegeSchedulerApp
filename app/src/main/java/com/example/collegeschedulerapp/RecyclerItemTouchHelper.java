package com.example.collegeschedulerapp;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.Adapter.AssignmentAdapter;
import com.example.collegeschedulerapp.Adapter.CourseAdapter;
import com.example.collegeschedulerapp.Adapter.ExamAdapter;
import com.example.collegeschedulerapp.Adapter.TaskAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private AssignmentAdapter adapter;

    private ExamAdapter examAdapter;
    private TaskAdapter taskAdapter;
    CourseAdapter courseAdapter;

    public RecyclerItemTouchHelper(AssignmentAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    public RecyclerItemTouchHelper(ExamAdapter examAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.examAdapter = examAdapter;
    }

    public RecyclerItemTouchHelper(TaskAdapter taskAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.taskAdapter = taskAdapter;
    }

    public RecyclerItemTouchHelper(CourseAdapter courseAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.courseAdapter = courseAdapter;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT) {

            AlertDialog.Builder builder;
            if (adapter != null) {
                builder = new AlertDialog.Builder(adapter.getContext());
            } else if (taskAdapter != null) {
                builder = new AlertDialog.Builder(taskAdapter.getContext());
            } else if (courseAdapter != null) {
                builder = new AlertDialog.Builder(courseAdapter.getContext());
            } else {
                builder = new AlertDialog.Builder(examAdapter.getContext());
            }
            builder.setTitle("Delete Item");
            builder.setMessage("Are you sure you want to delete this item?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (adapter != null) {
                                adapter.deleteItem(position);
                            } else if (taskAdapter != null) {
                                taskAdapter.deleteItem(position);
                            } else if (courseAdapter != null) {
                                courseAdapter.deleteItem(position);
                            } else {
                                examAdapter.deleteItem(position);
                            }
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (adapter != null) {
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    } else if (taskAdapter != null) {
                        taskAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    } else if (courseAdapter != null) {
                        courseAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    } else {
                        examAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                    //adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            //Swipe Right Action
            if (adapter != null) {
                adapter.sendToDo(position);
            } else if (taskAdapter != null) {
                //taskAdapter.sendToDo(position);
            } else if (courseAdapter != null) {
                //taskAdapter.sendToDo(position);
            } else {
                examAdapter.sendToDo(position);
            }

            //adapter.sendToDo(position);

        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX > 0) {
            if (adapter != null) {
                icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.delete_sweep);
            } else if (taskAdapter != null) {
                icon = ContextCompat.getDrawable(taskAdapter.getContext(), R.drawable.delete_sweep);
            } else if (courseAdapter != null) {
                icon = ContextCompat.getDrawable(courseAdapter.getContext(), R.drawable.delete_sweep);
            } else {
                icon = ContextCompat.getDrawable(examAdapter.getContext(), R.drawable.delete_sweep);
            }
            background = new ColorDrawable(Color.RED);
        } else {

            if (adapter != null) {
                icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.add_task_swipe);
            } else if (taskAdapter != null) {
                icon = ContextCompat.getDrawable(taskAdapter.getContext(), R.drawable.add_task_swipe);
            } else if (courseAdapter != null) {
                icon = ContextCompat.getDrawable(courseAdapter.getContext(), R.drawable.delete_sweep);
            }else {
                icon = ContextCompat.getDrawable(examAdapter.getContext(), R.drawable.add_task_swipe);
            }
            background = new ColorDrawable(Color.BLUE);
        }

        assert icon != null;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}