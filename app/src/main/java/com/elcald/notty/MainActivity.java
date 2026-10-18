package com.elcald.notty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.LambdaConversionException;
import java.nio.charset.StandardCharsets;
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
    private TextView btn_options_export;
    private TextView btn_options_import;
    private TextView btn_options_sort_fav;
    private TextView btn_options_deleteAll;
    private TextView btn_options_credits;
    private Button btn_delete;
    private Toolbar home_toolbare;

    private TextView home_toolbar_title;
    private LinearLayout home_footbar;

    private LinearLayout options_menu;
    private LinearLayout options_menu_sortList;


    private List<String> listSettings = new ArrayList<>();

    private Bibliotheque biblio;

    private View showedView = null;

    private String currentDirectory = "All";

    private String contentToExport = "";

    private final ActivityResultLauncher<String> creerFichier = registerForActivityResult( new ActivityResultContracts.CreateDocument("text/plain"), uri -> {
        if (uri == null) return; // l'utilisateur a annulé
        ecrireDansUri(uri, contentToExport);
    });

    private void ecrireDansUri(Uri uri, String texte) {
        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
            if (out == null) throw new IOException(String.valueOf(R.string.error_file_not_found));
            out.write(texte.getBytes(StandardCharsets.UTF_8));
            Toast.makeText(this, R.string.file_saved, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, R.string.error + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private final ActivityResultLauncher<String[]> choisirFichier = registerForActivityResult(new ActivityResultContracts.OpenDocument(),uri -> {
        if (uri == null) return; // l'utilisateur a annulé
        String texte = lireDepuisUri(uri);
        if (texte != null) { // action après avoir récup le fichier
            try {
                biblio.parseImport(texte);
                finish();
                startActivity(getIntent());
            } catch (JSONException e) {
                Toast.makeText(this, R.string.error_import, Toast.LENGTH_LONG).show();
            }
        }
    });

    private String lireDepuisUri(Uri uri) {
        try (InputStream in = getContentResolver().openInputStream(uri)) {
            if (in == null) throw new IOException(String.valueOf(R.string.error_file_not_found));

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int n;
            while ((n = in.read(data)) != -1) {
                buffer.write(data, 0, n);
            }
            return buffer.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            Toast.makeText(this, R.string.error + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }



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

        MainActivity activity = this;

        // Gestion toolbar
        home_toolbare = (Toolbar) findViewById(R.id.id_home_toolbar);
        home_toolbare.setTitle("");
        setSupportActionBar(home_toolbare);

        home_toolbar_title = findViewById(R.id.textView_TBar_titre);
        home_toolbar_title.setText(R.string.app_name);

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


        this.btn_options_export = (TextView)findViewById(R.id.id_options_home_btn_export);
        btn_options_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    contentToExport = biblio.getNottyFile();
                } catch (JSONException e) {
                    contentToExport = "Erreur d'export";
                }

                creerFichier.launch("nottybdd");          // nom proposé
            }
        });


        this.btn_options_import = (TextView)findViewById(R.id.id_options_home_btn_import);
        btn_options_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choisirFichier.launch(new String[]{"text/plain"});
            }
        });



        // Bouton pour supprimer la bdd
        this.btn_options_deleteAll = (TextView)findViewById(R.id.id_options_home_btn_deleteAll);
        btn_options_deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder popup_delete_nottyfile = new AlertDialog.Builder(activity);
                popup_delete_nottyfile.setTitle(R.string.delete_bdd_warning);

                popup_delete_nottyfile.setMessage(R.string.delete_bdd_message);

                popup_delete_nottyfile.setPositiveButton(R.string.optn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        biblio.delete_notty_file();

                        finish();
                        startActivity(getIntent());

                        Toast.makeText(getApplicationContext(), R.string.delete_bdd_confirm, Toast.LENGTH_SHORT).show();
                    }
                });//fin positiveButton

                popup_delete_nottyfile.setNegativeButton(R.string.optn_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), R.string.canceled, Toast.LENGTH_SHORT).show();
                    }
                });//fin negative

                popup_delete_nottyfile.show();


            }
        });

        this.btn_options_credits = (TextView)findViewById(R.id.id_options_home_btn_credits);
        btn_options_credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popup_credits popup_credits = new Popup_credits(activity);

                popup_credits.getBtn_close().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup_credits.dismiss();
                    }
                });

                popup_credits.build();
            }
        });


        NoteItemAdapter noteItemAdapter = new NoteItemAdapter(this, biblio.get());

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
                    if(item.getItemId() == R.id.id_menu_note_tile_action_supprimer){ // Suppression des notes

                        AlertDialog.Builder popup_delete_nottyfile = new AlertDialog.Builder(activity);
                        popup_delete_nottyfile.setTitle(R.string.delete_bdd_warning);

                        popup_delete_nottyfile.setMessage(R.string.delete_bdd_message);

                        popup_delete_nottyfile.setPositiveButton(R.string.optn_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                biblio.deletesNotes(selectedNotes);

                                finish();
                                startActivity(getIntent());

                                Toast.makeText(getApplicationContext(), R.string.delete_bdd_confirm, Toast.LENGTH_SHORT).show();
                            }
                        });//fin positiveButton

                        popup_delete_nottyfile.setNegativeButton(R.string.optn_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), R.string.canceled, Toast.LENGTH_SHORT).show();
                            }
                        });//fin negative

                        popup_delete_nottyfile.show();
                    }
                    else if(item.getItemId() == R.id.id_menu_note_tile_action_directory){ // ajout dans un dossier
                        Popup_selectDirectory popup_selectDirectory = new Popup_selectDirectory(activity, selectedNotes);

                        popup_selectDirectory.getBtn_close().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popup_selectDirectory.dismiss();
                            }
                        });

                        popup_selectDirectory.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                                startActivity(getIntent());
                            }
                        });

                        popup_selectDirectory.build();
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