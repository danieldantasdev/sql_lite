package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.criarBancoAutenticacao();

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(v->{
            String login = ((EditText)findViewById(R.id.login)).getText().toString();
            String senha = ((EditText)findViewById(R.id.senha)).getText().toString();
            SQLiteDatabase myDB = openOrCreateDatabase("login.db", MODE_PRIVATE, null);
            myDB.execSQL("CREATE TABLE IF NOT EXISTS usuario (login VARCHAR(20), senha VARCHAR(20))");
            Cursor myCursor = myDB.rawQuery("select login, senha from usuario", null);
            myCursor.moveToNext();
            String loginString = myCursor.getString(0);
            String senhaString = myCursor.getString(1);

            //SharedPreferences
            this.criarSharedPreferencesAutenLcacao();

            if(loginString.equalsIgnoreCase(login) && senhaString.equalsIgnoreCase(senha)){
                criarArquivoAutenNcacao();
                criarSharedPreferencesAutenLcacao();
                Toast.makeText(this, "SQL OK", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(this, "SQL Error", Toast.LENGTH_LONG).show();
            myDB.close();
        });

        //Banco
        this.criarBancoAutenticacao();
    }

    public void criarBancoAutenticacao(){
        SQLiteDatabase myDB = openOrCreateDatabase("login.db", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS usuario (login VARCHAR(20), senha VARCHAR(20))");
        ContentValues registro = new ContentValues();
        registro.put("login", "admin");
        registro.put("senha", "12345678");
        myDB.insert("usuario", null, registro);
        myDB.close();
    }

    public void criarArquivoAutenNcacao()  {
        ObjectMapper objectMapper = new ObjectMapper();
        Login login = new Login("admin", "12345678");
                File internalStorageDir = getFilesDir();
        File arquivo = new File(internalStorageDir, "login.json");
        try {
            objectMapper.writeValue(arquivo, login);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     void criarSharedPreferencesAutenLcacao(){
        SharedPreferences minhasPreferencias =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor myEditor = minhasPreferencias.edit();
        myEditor.putString("LOGIN", "admin");
        myEditor.putString("SENHA", "12345678");
        myEditor.commit();
    }

    public void autenLcarSharedPreferences(View v) {
        String login = ((EditText) findViewById(R.id.login)).getText().toString();
        String senha = ((EditText) findViewById(R.id.senha)).getText().toString();
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String loginString = myPreferences.getString("LOGIN", "unknown");
        String senhaString = myPreferences.getString("SENHA", "unknown");
        if (loginString.equalsIgnoreCase(login) && senhaString.equalsIgnoreCase(senha))
            Toast.makeText(this, "SP OK", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "SP Error", Toast.LENGTH_LONG).show();
    }

    public void autenticarArquivo(View v){
        try {
            String login = ((EditText)findViewById(R.id.login)).getText().toString();
            String senha = ((EditText)findViewById(R.id.senha)).getText().toString();
            final ObjectMapper mapper = new ObjectMapper();
            File internalStorageDir = getFilesDir();
            File jsonFile = new File(internalStorageDir, "login.json");
            Login Login = mapper.readValue(jsonFile, Login.class);
            String loginString = Login.getLogin();
            String senhaString = Login.getSenha();
            if(loginString.equalsIgnoreCase(login) && senhaString.equalsIgnoreCase(senha))
                Toast.makeText(this, "Arq OK", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Arq Error", Toast.LENGTH_LONG).show();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void criarBancoAutenLcacao(){
        SQLiteDatabase myDB = openOrCreateDatabase("login.db", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS usuario (login VARCHAR(20), senha VARCHAR(20))");
        ContentValues registro = new ContentValues();
        registro.put("login", "admin");
        registro.put("senha", "12345678");
        myDB.insert("usuario", null, registro);
        myDB.close();
    }

    public void autenLcarBanco(View v){
        String login = ((EditText)findViewById(R.id.login)).getText().toString();
        String senha = ((EditText)findViewById(R.id.senha)).getText().toString();
        SQLiteDatabase myDB = openOrCreateDatabase("login.db", MODE_PRIVATE, null);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS usuario (login VARCHAR(20), senha VARCHAR(20))");
        Cursor myCursor = myDB.rawQuery("select login, senha from usuario", null);
        myCursor.moveToNext();
        String loginString = myCursor.getString(0);
        String senhaString = myCursor.getString(1);
        if(loginString.equalsIgnoreCase(login) && senhaString.equalsIgnoreCase(senha))
            Toast.makeText(this, "SQL OK", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "SQL Error", Toast.LENGTH_LONG).show();
        myDB.close();
    }

}