package com.example.projet_agenda;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public abstract class Serializer {

    public static void serialize(String nomFichier, long val, Context context){
        try{
            FileOutputStream file = context.openFileOutput(nomFichier, Context.MODE_PRIVATE);
            ObjectOutputStream oss;
            try{
                oss = new ObjectOutputStream(file);
                oss.write((int) val);
                oss.flush();
                oss.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }//fin serialize


    public static long deSerialize(String nomFichier, Context context){
        try{
            FileInputStream file = context.openFileInput(nomFichier);
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(file);
                long objet = ois.read();
                ois.close();
                return objet;
            }catch (StreamCorruptedException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return -1;

    }//fin deSerialize


}//fin class
