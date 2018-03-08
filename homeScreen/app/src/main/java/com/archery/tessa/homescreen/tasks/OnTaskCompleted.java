package com.archery.tessa.homescreen.tasks;


/**
 * An interface to fight the fact that Java has no function pointers.
 * To be used to make AsyncTasks update Activities' UI
 */
public interface OnTaskCompleted {
    void onTaskCompleted(Object o);
}
