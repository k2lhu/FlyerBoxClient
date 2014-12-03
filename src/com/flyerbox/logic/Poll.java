package com.flyerbox.logic;

/**
 * Created by tmrafael on 02.12.2014.
 */
public class Poll {
    String title;
    String description;
    int id;
    boolean watched;

    public Poll(String title, String description, int id, boolean watched) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.watched = watched;
    }

    public Poll(String title, String description, int id) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.watched = false;
    }

    public Poll() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isWatched() {
        return watched;
    }

    public void getWatched(boolean watched) {
        this.watched = watched;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
