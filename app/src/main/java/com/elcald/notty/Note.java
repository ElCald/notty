package com.elcald.notty;

public class Note {
    private final int id;
    private String title;
    private String content;
    private final String dateCreation;
    private String dateModification;

    private String directory;
    private boolean favorite;


    // constructeur

    public Note(){
        this.id = -1;
        this.title = "undefined";
        this.content = "content";
        this.dateCreation = "01/01/1990";
        this.dateModification = "01/01/1990";
        this.directory = "undefined";
        this.favorite = false;
    }

    public Note(int id, String title, String content, String dateCreation){
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateCreation = dateCreation;
        this.dateModification = dateCreation;
        this.directory = "undefined";
        this.favorite = false;
    }

    public Note(int id, String title, String content, String dateCreation, String dateModification, String directory, boolean favorite){
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.directory = directory;
        this.favorite = favorite;
    }




    // methodes
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getDateCreation() { return dateCreation; }
    public String getDateModification() { return dateModification; }
    public String getDirectory() { return directory; }
    public boolean getFavorite() { return favorite; }


    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setDateModification(String dateModification) { this.dateModification = dateModification; }
    public void setDirectory(String directory) { this.directory = directory; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

}
