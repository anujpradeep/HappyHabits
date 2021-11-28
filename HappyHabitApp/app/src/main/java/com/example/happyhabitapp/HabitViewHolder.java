package com.example.happyhabitapp;


import android.content.ClipData;
import android.content.res.ColorStateList;
import android.media.Image;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Represents the view model for each {@link Habit}.
 * Used with {@link HabitsAdapter}.
 * @author Jonathan, Anthony
 */

public class HabitViewHolder extends RecyclerView.ViewHolder implements
    View.OnTouchListener, GestureDetector.OnGestureListener, FirestoreCallback
{
    GestureDetector habitGestureDetector;

    private View view;

    private TextView titleTextView;
    private TextView reasonTextView;
    private TextView frequencyTextView;

    private ItemTouchHelper touchHelper;
    private HabitListener viewListener;

    private ProgressBar progressBar;
    private TextView progressBarText;

    /**
     * Gets all relevant data fields and initializes a {@link GestureDetector} to set to the View.
     * @param habitView a {@link View}
     * @param helper an {@link ItemTouchHelper}
     * @param habitListener an instance of {@link HabitListener}
     */
    public HabitViewHolder(View habitView, ItemTouchHelper helper, HabitListener habitListener) {
        super(habitView);
        view = habitView;
        titleTextView = (TextView) habitView.findViewById(R.id.habit_title);
        reasonTextView = (TextView) habitView.findViewById(R.id.reason_text);
        frequencyTextView = (TextView) habitView.findViewById(R.id.selected_dates);

        habitGestureDetector = new GestureDetector(habitView.getContext(), this);
        touchHelper = helper;
        viewListener = habitListener;

        habitView.setOnTouchListener(this);
    }

    /**
     * Sets the data in the view model according to {@link Habit} instance.
     * @param habit
     */
    public void attachData(Habit habit) {
        titleTextView.setText(habit.getTitle());
        reasonTextView.setText(habit.getReason());
        frequencyTextView.setText(habit.getWeekAsStr());
        setProgressOnBar(habit);
    }

    private void setProgressOnBar(Habit habit) {
        int percentage = getProgressOnBar(habit);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBarText = view.findViewById(R.id.progress_text);
        progressBar.setProgress(percentage);
        fillProgressBar(percentage);
    }

    /**
     * Colors the progress bar using its percentage
     * @param percentage an int representing the percentage of the progress bar
     */
    private void fillProgressBar(int percentage) {
        if (percentage < 33) {
            progressBarText.setTextColor(view.getContext().getResources().getColor(R.color.progress_indicator_low));
            progressBar.setProgressTintList(ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.progress_indicator_low)));
        }
        else if (percentage < 66) {
            progressBarText.setTextColor(view.getContext().getResources().getColor(R.color.progress_indicator_mid));
            progressBar.setProgressTintList(ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.progress_indicator_mid)));
        }
        else {
            progressBarText.setTextColor(view.getContext().getResources().getColor(R.color.progress_indicator_high));
            progressBar.setProgressTintList(ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.progress_indicator_high)));
        }
    }

    /**
     * Determines what portion of the bar is filled, and what color it is to be set to.
     */
    private int getProgressOnBar(Habit habit) {
        int progressCap = habit.getEvents().size();
        float totalProgress = 0;

        if (progressCap == 0) {
            progressBar.setVisibility(View.INVISIBLE);  //If there are no respective events, progress bar will not show up.
        }
        else {
            progressBar.setMax(progressCap);
        }

        //Gets the total progress of the event
        for (int i = 0; i < progressCap - 1; i++) {
            int status = habit.getEvents().get(i).getStatus();  //Get the status code of the habit event

            switch (status) {
                case 0: totalProgress += 0;     //Event is incomplete
                        break;
                case 1: totalProgress += 1;     //Event is completed
                        break;
                case 2: totalProgress += 0.5;   //Event is in progress
                        break;
            }
        }

        return Math.round(totalProgress/progressCap) * 100;  //Gets a rounded percentage out of 100
    }


    //All the below methods are required by the interfaces implemented

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        //Does nothing
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        viewListener.onHabitClick(getAdapterPosition());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        touchHelper.startDrag(this);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        habitGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    @Override
    public void callHabitList(ArrayList<Habit> habits) {

    }

    @Override
    public void callUserList(ArrayList<User> requesters) {

    }

    @Override
    public void checkUser(boolean[] has) {

    }

    @Override
    public void callEventList(ArrayList<HabitEvent> events) {
        
    }
}
