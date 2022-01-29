package com.nolesiak.m7_companyinfo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nolesiak.m7_companyinfo.db.DBHelper;
import com.nolesiak.m7_companyinfo.wsdl.MWLParametryWyszukiwania;
import com.nolesiak.m7_companyinfo.wsdl.MWLe3;

import org.ksoap2.HeaderProperty;


public class MainActivity extends AppCompatActivity {

    private MWLe3 gusService;
    final static String USER_KEY = "abcde12345abcde12345";
    private EditText editText;

    private Button buttonSearchGus;
    private Button buttonSearchLocal;

    private DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase mydatabase = openOrCreateDatabase(DBHelper.DATABASE_NAME,MODE_PRIVATE,null);
        mydb = new DBHelper(this);
        mydb.onCreate(mydatabase);

        gusService = new MWLe3();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        editText= findViewById(R.id.editText_nip);
        buttonSearchGus = findViewById(R.id.btn_search);
        buttonSearchLocal = findViewById(R.id.btn_searchLocal);

        buttonSearchGus.setOnClickListener((View v) -> {
           searchInGus(editText.getText().toString());
        });

        buttonSearchLocal.setOnClickListener((View v) ->{
            searchInLocalDb(editText.getText().toString());
        });
    }

    private void searchInLocalDb(String nip) {
        if(mydb.getCompanyInfoByNip(nip).getCount() == 0){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Błąd")
                    .setMessage("W bazie nie ma podanej firmy")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }else{
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("nip", nip);
            finish();
            startActivity(intent);
        }
        return;

    }

    public void searchInGus(String nip){
        try {
            String sessionId = gusService.Zaloguj(USER_KEY);
            gusService.httpHeaders.add(new HeaderProperty("sid", sessionId));
            MWLParametryWyszukiwania param = new MWLParametryWyszukiwania();
            param.Nip = nip; //6452521870
            String result = gusService.DaneSzukajPodmioty(param);
            gusService.Wyloguj(sessionId);

            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("result", result);
            finish();
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}