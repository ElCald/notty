package com.elcald.notty;

import java.io.Serializable;

public class Note implements Serializable {
    private final int id;
    private String title;
    private String content;
    private final String dateCreation;
    private String dateModification;
    private String directory;
    private boolean favorite;
    private boolean is_locked;


    // constructeur

    public Note(){
        this.id = -1;
        this.title = "undefined";
        this.content = "content";
        this.dateCreation = "01/01/1990";
        this.dateModification = "01/01/1990";
        this.directory = "undefined";
        this.favorite = false;
        this.is_locked = false;
    }

    public Note(int id, String title, String content, String dateCreation){
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateCreation = dateCreation;
        this.dateModification = dateCreation;
        this.directory = "undefined";
        this.favorite = false;
        this.is_locked = false;
    }

    public Note(int id, String title, String content, String dateCreation, String dateModification, String directory, boolean favorite, boolean is_locked){
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.directory = directory;
        this.favorite = favorite;
        this.is_locked = is_locked;
    }




    // methodes
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getDateCreation() { return dateCreation; }
    public String getDateModification() { return dateModification; }
    public String getDirectory() { return directory; }
    public boolean getFavorite() { return favorite; }
    public boolean getIsLocked() { return is_locked; }


    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setDateModification(String dateModification) { this.dateModification = dateModification; }
    public void setDirectory(String directory) { this.directory = directory; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
    public void lock() { this.is_locked = true; }
    public void unlock() { this.is_locked = false; }
    public void swapLock() { this.is_locked = !this.is_locked; }

}
