package com.elcald.notty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DirectoryActivity extends AppCompatActivity {

    private ImageView btn_addDirectory;
    private Bibliotheque biblio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_directory);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DirectoryActivity activity = this;

        btn_addDirectory = findViewById(R.id.id_directory_toolbar_btn_addDirectory);

        // Création de la bibliothèque de notes
        try {
            biblio = new Bibliotheque(this);
        } catch (JSONException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        DirectoryItemAdapter directoryAdapter = new DirectoryItemAdapter(this, biblio.getDirectories());

        @SuppressLint({"MissingInflatedId", "LocalSuppress", "CutPasteId"})
        ListView directoriesListView = findViewById(R.id.id_directory_listview);
        directoriesListView.setAdapter(directoryAdapter);



        // bouton retour du téléphone
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();

            }
        });


        btn_addDirectory.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Popup_addDirectory popup = new Popup_addDirectory(activity);

                popup.getBtn_cancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                    }
                });


                popup.getBtn_add().setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(!popup.getEnterText().getText().toString().trim().isEmpty())
                            biblio.addDirectory(popup.getEnterText().getText().toString());
                        popup.dismiss();

                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);

                    }
                });

                popup.build();
            }
        });



        // Récup la gridview et création des item dans le home

        directoriesListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);


        directoriesListView.setOnItemClickListener((parent, view, position, id) -> {
            String item = (String)parent.getItemAtPosition(position);

            biblio.showOnlyDirectory(item);

            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        });


        directoriesListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Appelée à chaque (dé)sélection : on met à jour le titre
//                mode.setTitle(noteGridView.getCheckedItemCount() + "");
                directoryAdapter.notifyDataSetChanged(); // getView() relit l'état coché
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_selection_directory_tile, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // au clique sur la corbeille
                if (item.getItemId() == R.id.id_menu_directory_tile_action_supprimer) {
                    SparseBooleanArray checked = directoriesListView.getCheckedItemPositions();
                    List<String> selectedDirectory = new ArrayList<>();


                    // on récupère les notes séléctionnées
                    for (int i = 0; i < checked.size(); i++) {
                        if (checked.valueAt(i)) {
                            selectedDirectory.add(directoryAdapter.getItem(checked.keyAt(i)));
                        }
                    }


                    // Appliquez votre action à selectedNotes
                    if(item.getItemId() == R.id.id_menu_directory_tile_action_supprimer){
                        biblio.deletesDirectories(selectedDirectory);
                    }


                    mode.finish(); // quitte le mode sélection
                    return true;
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Les items reprennent automatiquement leur couleur normale
                directoryAdapter.notifyDataSetChanged();
            }
        });


    }
}