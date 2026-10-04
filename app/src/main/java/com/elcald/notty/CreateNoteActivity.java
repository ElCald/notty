package com.elcald.notty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateNoteActivity extends AppCompatActivity {

    private Button btn_backHome;
    private Bibliotheque biblio;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        try {
            biblio = new Bibliotheque(this);
        } catch (JSONException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }



        // Bouton pour retourner au home et sauvegarder
        this.btn_backHome = (Button)findViewById(R.id.id_create_btn_backHome);
        btn_backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edit_title = findViewById(R.id.id_create_text_title);
                EditText edit_content = findViewById(R.id.id_create_edit_body);
                String title = String.valueOf(edit_title.getText());
                String content = String.valueOf(edit_content.getText());


                // Enregistrement dans le fichier json
                // On vérifie que le titre ou le corps de la note n'est pas vide avant d'enregistrer
                if(!title.trim().isEmpty() || !content.trim().isEmpty()){
                    biblio.createNote(title, content);
                }



                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });
    }
}