package com.elcald.notty;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Serializable {
    private int id;
    private String title;
    private String content;
    private final String dateCreation;
    private String dateModification;
    private String directory;
    private boolean favorite;
    private boolean is_locked;
    private boolean is_selected;


    // constructeur

    public Note(){
        this.id = -1;
        this.title = "undefined";
        this.content = "content";
        this.dateCreation = "01/01/1990 00:00:00";
        this.dateModification = "01/01/1990 00:00:00";
        this.directory = "";
        this.favorite = false;
        this.is_locked = false;
        this.is_selected = false;
    }

    public Note(int id, String title, String content, String dateCreation){
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateCreation = dateCreation;
        this.dateModification = dateCreation;
        this.directory = "";
        this.favorite = false;
        this.is_locked = false;
        this.is_selected = false;
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
        this.is_selected = is_locked;
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
    public boolean getIsSelected() { return is_selected; }

    public Date getDateFormated(String dateString) throws ParseException {
        try {
            @SuppressLint("SimpleDateFormat")
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return formatter.parse(dateString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String getDateCreationShort(){
        return dateCreation.substring(0, 10);
    }

    public String getDateModificationShort(){
        return dateModification.substring(0, 10);
    }


    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setDateModification(String dateModification) { this.dateModification = dateModification; }
    public void setDirectory(String directory) { this.directory = directory; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
    public void lock() { this.is_locked = true; }
    public void unlock() { this.is_locked = false; }
    public void swapLock() { this.is_locked = !this.is_locked; }
    public void select() { this.is_selected = true; }
    public void unselect() { this.is_selected = false; }
    public void delete() { this.id = -1; }

}
