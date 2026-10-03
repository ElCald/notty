package com.elcald.notty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.*;


public class MainActivity extends AppCompatActivity {


    private final String FILENAME = "notty_save.txt";
    private Button btn_addNote;
    private Button btn_delete;

    private Bibliotheque biblio;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Création du fichier bdd initiale
        File file = new File(getFilesDir(), FILENAME);
        if(!file.exists())
        {
            try {
                getDir(FILENAME, MODE_PRIVATE);

                JSONObject initFileJSON = new JSONObject("{ \"version\":\"1.0.0\", \"id_total\":0, \"notes\":[] }");

                FileOutputStream initFile = openFileOutput(FILENAME, MODE_PRIVATE);
                initFile.write(initFileJSON.toString().getBytes());

                initFile.close();

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }



        try {
            biblio = new Bibliotheque(FILENAME, this);
        } catch (JSONException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Log.d("nottyprint", biblio.toString());


/*
        // Utilisation du stockage pour des paramètres d'application par exemple
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("homeScore", YOUR_HOME_SCORE);

        // Apply the edits!
        editor.apply();

        // Get from the SharedPreferences
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        int homeScore = settings.getInt("homeScore", 0);
*/


        // Bouton pour créer une note
        this.btn_addNote = (Button)findViewById(R.id.id_btn_addNote);
        btn_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNoteActivity = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivity(createNoteActivity);
                finish();
            }
        });


        // Bouton pour supprimer la bdd une note
        this.btn_delete = (Button)findViewById(R.id.id_btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject initFileJSON = null;
                try {
                    initFileJSON = new JSONObject("{ \"version\":\"1.0.0\", \"id_total\":0, \"notes\":[] }");

                    FileOutputStream initFile = openFileOutput(FILENAME, MODE_PRIVATE);
                    initFile.write(initFileJSON.toString().getBytes());

                    initFile.close();
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }

                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });


        // Récup la gridview et création des item dans le home
        GridView noteGridView = findViewById(R.id.id_gridv_note);
        noteGridView.setAdapter(new NoteItemAdapter(this, biblio.get()));

    }
}