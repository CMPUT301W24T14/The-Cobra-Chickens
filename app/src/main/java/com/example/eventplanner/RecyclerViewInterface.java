package com.example.eventplanner;
/**
 * An interface for handling item click events in a RecyclerView.
 */
public interface RecyclerViewInterface {
    /**
     * Called when an item in the RecyclerView is clicked.
     *
     * @param position The position of the clicked item in the RecyclerView.
     */
    void onItemClick(int position);
}
