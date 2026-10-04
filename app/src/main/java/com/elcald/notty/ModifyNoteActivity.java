package com.elcald.notty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

public class ModifyNoteActivity extends AppCompatActivity {

    private Note note;
    private int id_note;

    private EditText edit_title = null;
    private EditText edit_body = null;

    private Button btn_backHome = null;

    private Bibliotheque biblio = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modify_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // récup champs xml
        edit_title = findViewById(R.id.id_modify_edit_title);
        edit_body = findViewById(R.id.id_modify_edit_body);

        btn_backHome = findViewById(R.id.id_modify_btn_backHome);


        try {
            biblio = new Bibliotheque(this);
        } catch (JSONException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        // Récup des données put en extra
        Intent intent = getIntent();

        if(intent.getExtras() != null){
            id_note = (int) intent.getSerializableExtra("id_note");
            note = biblio.getNote(id_note);

            edit_title.setText(note.getTitle());
            edit_body.setText(note.getContent());
        }

        // Bouton pour sauvegarder et retourner en mode lecture
        btn_backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intent.getExtras() != null) {
                    if(id_note != -1){

                        biblio.updateNote(id_note, edit_title.getText().toString(), edit_body.getText().toString());
                        biblio.save_notty_file();
                        Intent readNoteActivity = new Intent(getApplicationContext(), ReadNoteActivity.class);
                        startActivity(readNoteActivity.putExtra("id_note", id_note));
                        finish();
                    }

                }
            }
        });
    }
}