package com.example.projet_agenda.bdd;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.projet_agenda.Evenement;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;



public class AccesLocal {

    //attributs
    private String nomBDD = "bdAgenda.sqlite";
    private Integer versionBase = 1;
    private MySQLiteOpenHelper accesBD;
    private SQLiteDatabase bdd;
    private Timestamp timestamp;

    //const
    public AccesLocal(Context contexte){
        accesBD = new MySQLiteOpenHelper(contexte, nomBDD, null, versionBase);
    }

    //methodes
    public void ajout(@NonNull Evenement event){
        bdd = accesBD.getWritableDatabase();

        String titre = event.getTitre();
        String contenu = event.getContenu();
        String tag = event.getTag();
        String date = event.getDate();
        String image = event.getImage();


        String req = "INSERT INTO agenda (titre, contenu, tag, date, image) VALUES (\""+titre+"\",\""+contenu+"\",\""+tag+"\",\""+date+"\",\""+image+"\")";
        bdd.execSQL(req);

    }


    public void supprimer(String i){
        bdd = accesBD.getWritableDatabase();

        String req = "DELETE FROM agenda WHERE titre = \""+i+"\"";
        bdd.execSQL(req);
    }

    public void supprimerAll(){
        bdd = accesBD.getWritableDatabase();

        String req = "DELETE FROM agenda";
        bdd.execSQL(req);
    }

    public void supprimerDate(String d){
        bdd = accesBD.getWritableDatabase();

        String req = "DELETE FROM agenda WHERE date < \""+d+"\" ";
        bdd.execSQL(req);
    }


    public void modifier(@NonNull Evenement event, String t){
        bdd = accesBD.getWritableDatabase();

        String titre = event.getTitre();
        String contenu = event.getContenu();
        String tag = event.getTag();
        String date = event.getDate();
        String image = event.getImage();

        String req = "UPDATE agenda SET titre = \""+titre+"\", contenu = \""+contenu+"\", tag = \""+tag+"\", date = \""+date+"\", image = \""+image+"\" WHERE titre = \""+t+"\" ";

        bdd.execSQL(req);

    }


    public Evenement recupN(int n){
        bdd = accesBD.getReadableDatabase();
        Evenement event = null;
        String req = "SELECT * FROM agenda WHERE num = \""+n+"\" ";
        Cursor curseur = bdd.rawQuery(req, null);
        curseur.moveToPosition(0);

        if(!curseur.isAfterLast()){
            String titre = curseur.getString(1);
            String contenu = curseur.getString(2);
            String tag = curseur.getString(3);
            String date = curseur.getString(4);
            String image = curseur.getString(5);

            event = new Evenement(titre, contenu, tag, date, image);
        }

        curseur.close();
        return event;

    }//fin recupN


    public List<Evenement> recupAll(){
        bdd = accesBD.getReadableDatabase();
        String req = "SELECT * FROM agenda";
        Cursor curseur = bdd.rawQuery(req, null);
        List<Evenement> listeEvent = new ArrayList<>();

        int i = 0;
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {

            String titre = curseur.getString(1);
            String contenu = curseur.getString(2);
            String tag = curseur.getString(3);
            String date = curseur.getString(4);
            String image = curseur.getString(5);

            Evenement event = new Evenement(titre, contenu, tag, date, image);

            listeEvent.add(event);
            i++;
            curseur.moveToNext();
        }

        return listeEvent;
    }//fin recupAll


    public List<Evenement> recupAllDate(String d){
        bdd = accesBD.getReadableDatabase();
        String req = "SELECT * FROM agenda WHERE date= \""+d+"\" ";
        Cursor curseur = bdd.rawQuery(req, null);
        List<Evenement> listeEvent = new ArrayList<>();

        int i = 0;
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {

            String titre = curseur.getString(1);
            String contenu = curseur.getString(2);
            String tag = curseur.getString(3);
            String date = curseur.getString(4);
            String image = curseur.getString(5);

            Evenement event = new Evenement(titre, contenu, tag, date, image);

            listeEvent.add(event);
            i++;
            curseur.moveToNext();
        }

        return listeEvent;
    }//fin recupAllDate

    public List<Evenement> recupAllTag(String t){
        bdd = accesBD.getReadableDatabase();
        String req = "SELECT * FROM agenda WHERE tag= \""+t+"\" ";
        Cursor curseur = bdd.rawQuery(req, null);
        List<Evenement> listeEvent = new ArrayList<>();

        int i = 0;
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {

            String titre = curseur.getString(1);
            String contenu = curseur.getString(2);
            String tag = curseur.getString(3);
            String date = curseur.getString(4);
            String image = curseur.getString(5);

            Evenement event = new Evenement(titre, contenu, tag, date, image);

            listeEvent.add(event);
            i++;
            curseur.moveToNext();
        }

        return listeEvent;
    }//fin recupAllDate


    public int NEvenement(){
        bdd = accesBD.getReadableDatabase();

        final String DATABASE_COMPARE = "select count(*) from agenda ";

        int sometotal = (int) DatabaseUtils.longForQuery(bdd, DATABASE_COMPARE, null);

        return sometotal;
    }//Fin NEvenement



    public Cursor getNum(String t){
        bdd = accesBD.getReadableDatabase();
        String req = "SELECT num FROM agenda WHERE titre=\""+t+"\" ";
        Cursor curseur = bdd.rawQuery(req, null);
        curseur.moveToPosition(0);
        curseur.getInt(0);
        return curseur;
    }


}//fin class

