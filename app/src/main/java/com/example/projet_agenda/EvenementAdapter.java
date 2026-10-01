package com.example.projet_agenda;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_agenda.bdd.AccesLocal;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/* C'est la liste des évennements*/
public class EvenementAdapter extends BaseAdapter implements Serializable {

    //attribut
    private Context contexte;
    private List<Evenement> listeEvent;
    private LayoutInflater inflater;
    private TextView dateTextView;


    //const
    public EvenementAdapter(Context contexte, List<Evenement> listeEvent){
        this.listeEvent = listeEvent;
        this.contexte = contexte;
        this.inflater = LayoutInflater.from(contexte);
    }

    @Override
    public int getCount() {
        return listeEvent.size();
    }

    @Override
    public Evenement getItem(int position) {
        return listeEvent.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.adapter_item, null);


        Evenement currentItem = getItem(i);

        String itemTitre = currentItem.getTitre();
        String itemDate = currentItem.getDate();

        TextView itemTitreView = (TextView) view.findViewById(R.id.titre);
        TextView itemDateView = (TextView) view.findViewById(R.id.date);

        itemTitreView.setText(itemTitre);
        itemDateView.setText(itemDate);

        ImageView btn_supp = (ImageView) view.findViewById(R.id.imageSupp);
        btn_supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder myPopup = new AlertDialog.Builder(contexte);

                myPopup.setMessage("Voulez-vous supprimer cet évènement ?");

                myPopup.setTitle("Attention");
                final boolean[] checked = new boolean[] {false};
                myPopup.setMultiChoiceItems(new String[]{"Ne plus demander"}, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        checked[i] = b;
                    }
                });

                myPopup.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AccesLocal accesLocal = new AccesLocal(contexte);
                        accesLocal.supprimer(itemTitre);
                        Toast.makeText(contexte, "Vous avez supprimé "+itemTitre, Toast.LENGTH_SHORT).show();
                    }
                });//fin positiveButton

                myPopup.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(contexte, "Annulé", Toast.LENGTH_SHORT).show();
                    }
                });//fin negative

                myPopup.show();

            }
        });

        return view;
    }//fin getView

}//fin class
