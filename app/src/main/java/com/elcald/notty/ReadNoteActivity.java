package com.elcald.notty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.io.FileNotFoundException;

public class ReadNoteActivity extends AppCompatActivity {

    private Note note = null;
    private int id_note;
    private TextView title_field = null;
    private TextView body_field = null;
    private TextView dateModif_field = null;
    private Button btn_backHome = null;
    private Button btn_modify = null;

    private Bibliotheque biblio = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_read_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // création de la bibliothèque
        try {
            biblio = new Bibliotheque(this);
        } catch (JSONException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Récup des champs XML
        this.title_field = findViewById(R.id.id_read_text_title);
        this.body_field = findViewById(R.id.id_read_text_body);
        this.dateModif_field = findViewById(R.id.id_read_text_dateModif);

        this.btn_backHome = findViewById(R.id.id_read_btn_backHome);
        this.btn_modify = findViewById(R.id.id_read_btn_modify);

//        this.body_field.setMovementMethod(new ScrollingMovementMethod());


        // Récup des données put en extra
        Intent intent = getIntent();

        if(intent.getExtras() != null){
            id_note = (int) intent.getSerializableExtra("id_note");

            note = biblio.getNote(id_note);
            title_field.setText(note.getTitle());
            body_field.setText(note.getContent());
            dateModif_field.setText(note.getDateModification());
        }
        else{
            id_note = -1;
        }



        // Bouton pour retourner au home
        btn_backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });


        // Bouton pour modifier la note
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intent.getExtras() != null) {
                    if(id_note != -1){
                        Intent modifyNoteActivity = new Intent(getApplicationContext(), ModifyNoteActivity.class);
                        startActivity(modifyNoteActivity.putExtra("id_note", id_note));
                        finish(); // on le finish pour le réouvrir proprement après
                    }

                }
            }
        });


    }
}