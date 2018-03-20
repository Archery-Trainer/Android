package com.archery.tessa.homescreen.tasks;


/**
 * An interface to fight the fact that Java has no function pointers.
 * To be used to make AsyncTasks update Activities' UI
 */
public interface OnTaskCompleted {
    /**
     * Do something with the result of an AsyncTask
     * @param o Type of the result
     */
    void onTaskCompleted(Object o);
}
