package com.elcald.notty;

import android.content.Context;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {

    // attributs

    private final String FILENAME = "notty_save.txt";
    private String jsonBib = ""; // Nom du fichier bdd
    private List<Note> noteItemList;
    private String version;
    private int id_total;
    private Context context;


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



        // Création du fichier bdd initiale s'il n'existe pas
        File file = new File(context.getFilesDir(), FILENAME);
        if(!file.exists())
        {
            try {
                this.context.getDir(FILENAME, Context.MODE_PRIVATE);

                JSONObject initFileJSON = new JSONObject("{ \"version\":\"1.0.0\", \"id_total\":0, \"notes\":[] }");

                FileOutputStream initFile = this.context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                initFile.write(initFileJSON.toString().getBytes());

                initFile.close();

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
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



        this.noteItemList = new ArrayList<>();


        // parsing du json pour créer la liste et obtenir toutes les info

        JSONObject buJSON = new JSONObject(this.jsonBib);
        JSONArray notesJSON = buJSON.getJSONArray("notes");


        this.version = buJSON.getString("version");
        this.id_total = buJSON.getInt("id_total");


        for (int i = 0; i < notesJSON.length(); i++) {

            JSONObject noteJSON = notesJSON.getJSONObject(i);

            int id = noteJSON.getInt("id");
            String title = noteJSON.getString("title");
            String content = noteJSON.getString("body");
            String dateCreation = noteJSON.getString("dateCreation");
            String dateModification = noteJSON.getString("dateModification");
            String directory = noteJSON.getString("directory");
            boolean favorite = noteJSON.getBoolean("favorite");

            noteItemList.add(new Note(id, title, content, dateCreation, dateModification, directory, favorite));
        }

    }

    // methodes

    @NonNull
    @Override
    public String toString(){
        StringBuilder contenuBibli = new StringBuilder();

        contenuBibli.append("Version:").append(this.version).append("\n").append("id size:").append(this.id_total).append("\nNotes:\n");

        for(int i=0; i< noteItemList.size(); i++){
            contenuBibli.append(noteItemList.get(i).getTitle()).append(" | Date modif : ").append(noteItemList.get(i).getDateModification()).append("\n");
        }

        return contenuBibli.toString();
    }

    /**
     * Ajout d'une note à la liste et incrémente la nombre d'id_total
     * @param note
     */
    public void addNote(Note note){
        noteItemList.add(note);
        this.id_total++;
        save_notty_file();
    }


    /**
     *
     * @param id Id de la note
     * @return position de la note dans la liste de la bdd, sinon -1
     */
    public int getNotePos(int id){
        for(int i=0; i<noteItemList.size(); i++){
            if(noteItemList.get(i).getId() == id)
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
            return noteItemList.get(i);
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

        noteItemList.get(i).setTitle(title);
        noteItemList.get(i).setContent(content);

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = myDateObj.format(myFormatObj);

        noteItemList.get(i).setDateModification(formattedDate);

        save_notty_file();
    }

    /**
     *
     * @return Liste de notes
     */
    public List<Note> get(){
        return noteItemList;
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

    // json
    public JSONObject toJSON() throws JSONException {
        JSONObject buJSON = new JSONObject();
        JSONArray notesJSON = new JSONArray();

        buJSON.put("version",this.version);
        buJSON.put("id_total",this.id_total);


        // creation de la liste JSON des notes

        for(int i=0; i<noteItemList.size(); i++){
            JSONObject noteJSON = new JSONObject();

            noteJSON.put("id",noteItemList.get(i).getId());
            noteJSON.put("title",noteItemList.get(i).getTitle());
            noteJSON.put("body",noteItemList.get(i).getContent());
            noteJSON.put("dateCreation",noteItemList.get(i).getDateCreation());
            noteJSON.put("dateModification",noteItemList.get(i).getDateModification());
            noteJSON.put("directory",noteItemList.get(i).getDirectory());
            noteJSON.put("favorite",noteItemList.get(i).getFavorite());

            notesJSON.put(noteJSON);
        }


        buJSON.put("notes",notesJSON);

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

        JSONObject initFileJSON = null;
        try {
            initFileJSON = new JSONObject("{ \"version\":\"1.0.0\", \"id_total\":0, \"notes\":[] }");

            FileOutputStream initFile = this.context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            initFile.write(initFileJSON.toString().getBytes());

            initFile.close();
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
