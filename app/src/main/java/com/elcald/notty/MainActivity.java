package com.elcald.notty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.LambdaConversionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.*;


public class MainActivity extends AppCompatActivity {



    private ImageView btn_addNote;
    private ImageView btn_directories;
    private ImageView btn_options;
    private TextView btn_options_sort;
    private TextView btn_options_sort_title;
    private TextView btn_options_sort_dateCreation;
    private TextView btn_options_sort_dateModification;
    private TextView btn_options_sort_fav;
    private Button btn_delete;
    private Toolbar home_toolbare;
    private LinearLayout home_footbar;

    private LinearLayout options_menu;
    private LinearLayout options_menu_sortList;


    private List<String> listSettings = new ArrayList<>();

    private Bibliotheque biblio;

    private View showedView = null;


    @SuppressLint({"MissingInflatedId", "CutPasteId"})
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



        // Gestion toolbar
        home_toolbare = (Toolbar) findViewById(R.id.id_home_toolbar);
        home_toolbare.setTitle("");
        setSupportActionBar(home_toolbare);

        home_footbar = findViewById(R.id.id_home_footbar);

        // Menus
        options_menu = (LinearLayout) findViewById(R.id.id_home_options);
        options_menu_sortList = (LinearLayout) findViewById(R.id.id_home_options_sortlist);


        // Création de la bibliothèque de notes
        try {
            biblio = new Bibliotheque(this);
        } catch (JSONException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }



//        Log.d("nottyprint", biblio.toString());


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


        // click dans la fenêtre pour tout fermer
//        findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                options_menu.setVisibility(View.INVISIBLE);
//                options_menu_sortList.setVisibility(View.INVISIBLE);
//            }
//        });




        // Bouton pour aller dans le menu des répertoires
        this.btn_directories = (ImageView)findViewById(R.id.id_home_toolbar_btn_directory);
        btn_directories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent directoryActivity = new Intent(getApplicationContext(), DirectoryActivity.class);
                startActivity(directoryActivity);
                finish();
            }
        });


        // Bouton pour créer une note
        this.btn_addNote = (ImageView)findViewById(R.id.id_home_toolbar_btn_addNote);
        btn_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNoteActivity = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivity(createNoteActivity);
                finish();
            }
        });



        // Bouton pour afficher les options
        this.btn_options = (ImageView)findViewById(R.id.id_home_toolbar_btn_options);
        btn_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options_menu.setVisibility((options_menu.getVisibility()-1)%2);
                options_menu_sortList.setVisibility(View.INVISIBLE);
                showedView = options_menu;
            }
        });



        // Bouton pour afficher les options de tri
        this.btn_options_sort = (TextView) findViewById(R.id.id_options_home_btn_sort);
        btn_options_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options_menu.setVisibility(View.INVISIBLE);
                options_menu_sortList.setVisibility(View.VISIBLE);
                showedView = options_menu_sortList;
            }
        });

        // -------------- TRI --------------

        this.btn_options_sort_title = (TextView) findViewById(R.id.id_options_home_btn_sort_title);
        btn_options_sort_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options_menu_sortList.setVisibility(View.INVISIBLE);
                biblio.setSortType(biblio.SORT_TITLE);
                showedView = null;
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();

            }
        });

        this.btn_options_sort_dateCreation = (TextView) findViewById(R.id.id_options_home_btn_sort_dateCreation);
        btn_options_sort_dateCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options_menu_sortList.setVisibility(View.INVISIBLE);
                biblio.setSortType(biblio.SORT_DATECREATION);
                showedView = null;
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });

        this.btn_options_sort_dateModification = (TextView) findViewById(R.id.id_options_home_btn_sort_dateModification);
        btn_options_sort_dateModification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options_menu_sortList.setVisibility(View.INVISIBLE);
                showedView = null;
                biblio.setSortType(biblio.SORT_DATEMODIFICATION);
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });

        this.btn_options_sort_fav = (TextView) findViewById(R.id.id_options_home_btn_sort_fav);
        btn_options_sort_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options_menu_sortList.setVisibility(View.INVISIBLE);
                biblio.setSortType(biblio.SORT_FAV);
                showedView = null;
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });








        // Bouton pour supprimer la bdd une note
//        this.btn_delete = (Button)findViewById(R.id.id_home_btn_delete);
//        btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                biblio.delete_notty_file();
//
//                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(mainActivity);
//                finish();
//            }
//        });


        NoteItemAdapter noteItemAdapter = new NoteItemAdapter(this, biblio.get());()

        // Récup la gridview et création des item dans le home
        GridView noteGridView = findViewById(R.id.id_home_gridv_note);
        noteGridView.setAdapter(noteItemAdapter);


        noteGridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);


        noteGridView.setOnItemClickListener((parent, view, position, id) -> {
            Note item = (Note)parent.getItemAtPosition(position);

            Intent readNoteActivity = new Intent(this, ReadNoteActivity.class);
            startActivity(readNoteActivity.putExtra("id_note", item.getId()));
            finish();
        });


        noteGridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Appelée à chaque (dé)sélection : on met à jour le titre
//                mode.setTitle(noteGridView.getCheckedItemCount() + "");
                noteItemAdapter.notifyDataSetChanged(); // getView() relit l'état coché
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getMenuInflater().inflate(R.menu.menu_selection_note_tile, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // au clique sur la corbeille
                if (item.getItemId() == R.id.id_menu_note_tile_action_supprimer || item.getItemId() == R.id.id_menu_note_tile_action_directory) {
                    SparseBooleanArray checked = noteGridView.getCheckedItemPositions();
                    List<Note> selectedNotes = new ArrayList<>();


                    // on récupère les notes séléctionnées
                    for (int i = 0; i < checked.size(); i++) {
                        if (checked.valueAt(i)) {
                            selectedNotes.add(noteItemAdapter.getItem(checked.keyAt(i)));
                        }
                    }


                    // Appliquez votre action à selectedNotes
                    if(item.getItemId() == R.id.id_menu_note_tile_action_supprimer){
                        biblio.deletesNotes(selectedNotes);
                    }
                    else if(item.getItemId() == R.id.id_menu_note_tile_action_directory){
                        Log.d("nottyprint", "dossier");
                    }

                    mode.finish(); // quitte le mode sélection
                    return true;
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Les items reprennent automatiquement leur couleur normale
                noteItemAdapter.notifyDataSetChanged();
            }
        });




    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            if (showedView != null && !isPointInsideView(showedView, ev.getRawX(), ev.getRawY()) && !isPointInsideView(home_footbar, ev.getRawX(), ev.getRawY())) {
                options_menu.setVisibility(View.INVISIBLE);
                options_menu_sortList.setVisibility(View.INVISIBLE);
                biblio.unselectAll();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isPointInsideView(View view, float rawX, float rawY) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return rawX >= location[0] && rawX <= location[0] + view.getWidth()
                && rawY >= location[1] && rawY <= location[1] + view.getHeight();
    }

}