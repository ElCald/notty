package com.elcald.notty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Bibliotheque {

    // attributs

    private final String FILENAME = "notty_save.txt";
    private String jsonBib = ""; // Nom du fichier bdd
    private List<Note> noteItemListRaw; // liste brute du json qui est modifié
    private List<Note> noteItemList; // liste d'affichage
    private String version;
    private int id_total;
    private Context context;
    private String hashPassword;
    private int sortType;
    private List<String> directories;

    public final int SORT_TITLE = 0;
    public final int SORT_DATECREATION = 1;
    public final int SORT_DATECREATION_REVERSE = 2;
    public final int SORT_DATEMODIFICATION = 3;
    public final int SORT_DATEMODIFICATION_REVERSE = 4;
    public final int SORT_FAV = 5;


    // constructeur

    /**
     *
     * @param context
     * @throws JSONException
     * @throws FileNotFoundException
     */
    public Bibliotheque(Context context) throws JSONException, FileNotFoundException {
        FileInputStream file_notes = null;
        String temp_parse = " ";

        this.context = context;

        this.noteItemListRaw = new ArrayList<>();

        this.directories  = new ArrayList<>();



        // Création du fichier bdd initiale s'il n'existe pas
        File file = new File(context.getFilesDir(), FILENAME);
        if(!file.exists())
        {
            delete_notty_file();
        }
        


        try {

            file_notes = this.context.openFileInput(FILENAME);
            InputStreamReader inputStream = new InputStreamReader(file_notes);
            BufferedReader buffer = new BufferedReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();

            while( ( temp_parse = buffer.readLine() ) != null ){
                stringBuilder.append(temp_parse).append("\n");
            }

            this.jsonBib = stringBuilder.toString();


            if(file_notes!=null) // fermeture du fichier
                file_notes.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }





        Log.d("nottyprint", this.jsonBib);

        // parsing du json pour créer la liste et obtenir toutes les info

        JSONObject buJSON = new JSONObject(this.jsonBib);
        JSONArray notesJSON = null;
        JSONArray directoriesJSON = null;



        try {
            notesJSON = buJSON.getJSONArray("notes");
        }catch(Exception e){
            notesJSON = new JSONArray();
        }

        try {
            directoriesJSON = buJSON.getJSONArray("directories");
        }catch(Exception e){
            directoriesJSON = new JSONArray();
        }

        try {
            this.version = buJSON.getString("version");
        }catch(Exception e){
            this.version = "1.0.0";
        }

        try {
            this.hashPassword = buJSON.getString("hashPassword");
        }catch(Exception e){
            this.hashPassword = "";
        }

        try {
            this.sortType = buJSON.getInt("sortType");
        }catch(Exception e){
            this.sortType = 0;
        }

        try {
            this.id_total = buJSON.getInt("id_total");
        }catch(Exception e){
            this.id_total = 0;
        }


        for (int i = 0; i < notesJSON.length(); i++) {

            JSONObject noteJSON = notesJSON.getJSONObject(i);

            int id = noteJSON.getInt("id");
            String title = noteJSON.getString("title");
            String content = noteJSON.getString("body");
            String dateCreation = noteJSON.getString("dateCreation");
            String dateModification = noteJSON.getString("dateModification");
            String directory = noteJSON.getString("directory");
            boolean favorite = noteJSON.getBoolean("favorite");
            boolean is_locked = noteJSON.getBoolean("is_locked");

            try {
                @SuppressLint("SimpleDateFormat")
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                formatter.parse(dateCreation);
            } catch (Exception e) {
                dateCreation += " 00:00:00";
            }

            try {
                @SuppressLint("SimpleDateFormat")
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                formatter.parse(dateModification);
            } catch (Exception e) {
                dateModification += " 00:00:00";
            }

            noteItemListRaw.add(new Note(id, title, content, dateCreation, dateModification, directory, favorite, is_locked));
        }

        if(directoriesJSON.length() == 0){
            directories.add("");
        }
        else{
            for(int i =0; i<directoriesJSON.length(); i++){
                String dirName = directoriesJSON.getString(i);
                this.directories.add(dirName);
            }
        }


        noteItemList = new ArrayList<>(noteItemListRaw);

        sortBy();

    }




    // methodes

    @NonNull
    @Override
    public String toString(){
        StringBuilder contenuBibli = new StringBuilder();

        contenuBibli.append("Version:").append(this.version).append("\n").append("id size:").append(this.id_total).append("\nNotes:\n");

        for(int i=0; i< noteItemListRaw.size(); i++){
            contenuBibli.append(noteItemListRaw.get(i).getTitle()).append(" | Date modif : ").append(noteItemListRaw.get(i).getDateModification()).append("\n");
        }

        contenuBibli.append("hash:").append(this.hashPassword).append("\n");

        return contenuBibli.toString();
    }

    /**
     * Ajout d'une note à la liste et incrémente la nombre d'id_total+ sauvegarde
     * @param note
     */
    public void addNote(Note note){
        noteItemListRaw.add(note);
        this.id_total++;
        save_notty_file();
    }

    /**
     * Creation de la note et ajout dans la liste avec sauvegarde
     * @param title
     * @param content
     */
    public void createNote(String title, String content){

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);

        Note note = new Note(id_total, title, content, formattedDate);

        addNote(note);
    }

    /**
     *
     * @param id Id de la note
     * @return position de la note dans la liste de la bdd, sinon -1
     */
    public int getNotePos(int id){
        for(int i=0; i<noteItemListRaw.size(); i++){
            if(noteItemListRaw.get(i).getId() == id)
                return i;
        }

        return -1;
    }

    /**
     *
     * @param id Id de la note
     * @return Note
     */
    public Note getNote(int id){
        int i = getNotePos(id);

        if(i != -1){
            return noteItemListRaw.get(i);
        }
        else {
            return null;
        }
    }

    /**
     * Mise à jour de la note
     * @param id id de la note
     * @param title nouveau titre
     * @param content nouveau contenu
     */
    public void updateNote(int id, String title, String content){
        int i = getNotePos(id);

        noteItemListRaw.get(i).setTitle(title);
        noteItemListRaw.get(i).setContent(content);

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = myDateObj.format(myFormatObj);

        noteItemListRaw.get(i).setDateModification(formattedDate);

        save_notty_file();
    }

    /**
     * Inverse le verrou de la note
     * @param id
     */
    public void swapLock(int id){
        int i = getNotePos(id);
        noteItemListRaw.get(i).swapLock();
        save_notty_file();
    }

    /**
     * Suppression d'une note selon son id
      * @param id Id note
     */
    public void deleteNote(int id){
        int i = getNotePos(id);
        noteItemListRaw.remove(i);
    }

    public void addDirectory(String dir){
        if(!directories.contains(dir))
            directories.add(dir);
    }

    public void deleteDirectory(String dir){
        directories.remove(dir);
    }

    /**
     *
     * @return Liste de notes
     */
    public List<Note> get(){
        return noteItemList;
    }

    public List<Note> getRaw(){
        return noteItemListRaw;
    }

    public List<String> getDirectories(){
        return directories;
    }

    /**
     *
      * @return Nombre total d'id crée
     */
    public int getId_total(){ return this.id_total; }

    /**
     *
     * @return Nom du fichier Notty
     */
    public String getNomFichierNotty(){ return this.FILENAME; }


    public void setSortType(int s){
        this.sortType = s;
        save_notty_file();
    }

    public void sortBy(){

        noteItemList = new ArrayList<>(noteItemListRaw);

        switch (this.sortType){
            case 0: // titre
                noteItemList.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note n1, Note n2) {
                        return CharSequence.compare(n1.getTitle(), n2.getTitle());
                    }
                });
                break;

            case 1: // date creation
                noteItemList.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note n1, Note n2) {
                        try {
                            return n2.getDateFormated(n2.getDateCreation()).compareTo(n1.getDateFormated(n1.getDateCreation()));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                break;

            case 2: // date creation reverse
                noteItemList.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note n1, Note n2) {
                        try {
                            return n1.getDateFormated(n1.getDateCreation()).compareTo(n2.getDateFormated(n2.getDateCreation()));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                break;

            case 3: // date modification
                noteItemList.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note n1, Note n2) {
                        try {
                            return n2.getDateFormated(n2.getDateModification()).compareTo(n1.getDateFormated(n1.getDateModification()));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                break;

            case 4: // date modification reverse
                noteItemList.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note n1, Note n2) {
                        try {
                            return n1.getDateFormated(n1.getDateModification()).compareTo(n2.getDateFormated(n2.getDateModification()));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                break;

            case 5: // favoris
                noteItemList.clear();
                for(Note n : noteItemListRaw){
                    if(n.getFavorite()){
                        noteItemList.add(n);
                    }
                }
                break;

            default:
                break;
        }
    }

    // json
    public JSONObject toJSON() throws JSONException {
        JSONObject buJSON = new JSONObject();
        JSONArray notesJSON = new JSONArray();
        JSONArray directoriesJSON = new JSONArray();

        buJSON.put("version",this.version);
        buJSON.put("id_total",this.id_total);
        buJSON.put("hashPassword",this.hashPassword);
        buJSON.put("sortType",this.sortType);


        // creation de la liste JSON des notes

        for(int i=0; i<noteItemListRaw.size(); i++){
            JSONObject noteJSON = new JSONObject();

            noteJSON.put("id",noteItemListRaw.get(i).getId());
            noteJSON.put("title",noteItemListRaw.get(i).getTitle());
            noteJSON.put("body",noteItemListRaw.get(i).getContent());
            noteJSON.put("dateCreation",noteItemListRaw.get(i).getDateCreation());
            noteJSON.put("dateModification",noteItemListRaw.get(i).getDateModification());
            noteJSON.put("directory",noteItemListRaw.get(i).getDirectory());
            noteJSON.put("favorite",noteItemListRaw.get(i).getFavorite());
            noteJSON.put("is_locked",noteItemListRaw.get(i).getIsLocked());

            notesJSON.put(noteJSON);
        }

        // creation de la liste des répertoires

        for(int i=0; i<directories.size(); i++){
            directoriesJSON.put(directories.get(i));
        }

        buJSON.put("notes",notesJSON);
        buJSON.put("directories",directoriesJSON);

        return buJSON;
    }

    /**
     * Enregistre les notes dans le fichier bdd
     */
    public void save_notty_file(){
        try {

            FileOutputStream nottyFile = this.context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            nottyFile.write(toJSON().toString().getBytes());

            nottyFile.close();

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete_notty_file(){

        this.context.getDir(FILENAME, Context.MODE_PRIVATE);

        JSONObject initFileJSON = null;
        try {
            initFileJSON = new JSONObject("{ \"version\":\"1.0.0\", \"id_total\":0, \"hashPassword\":\"default\", \"sortType\":0, \"notes\":[], \"directories\":[] }");

            FileOutputStream initFile = this.context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            initFile.write(initFileJSON.toString().getBytes());

            initFile.close();
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
