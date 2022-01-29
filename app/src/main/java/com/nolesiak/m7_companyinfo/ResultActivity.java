package com.nolesiak.m7_companyinfo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nolesiak.m7_companyinfo.db.DBHelper;

public class ResultActivity extends AppCompatActivity {

    private TextView regon_tv;
    private TextView np_tv;
    private TextView name_tv;
    private TextView province_tv;
    private TextView city_tv;
    private String result;
    private ResultModel resultModel;
    private Button btn_saveToDb;
    private Button btn_return;

    private DBHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        regon_tv = findViewById(R.id.textView_regon_value);
        np_tv = findViewById(R.id.textView_nip_value);
        name_tv = findViewById(R.id.textView_name_value);
        province_tv = findViewById(R.id.textView_province_value);
        city_tv = findViewById(R.id.textView_city_value);
        btn_saveToDb = findViewById(R.id.btn_saveToDb);
        btn_return = findViewById(R.id.btn_returnToMain);

        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null){

            result = extras.getString("result");
            if(result != null){
                resultModel = ResultModel.fromXmlResult(result);
            }else{
               String nip = extras.getString("nip");
               Cursor res = mydb.getCompanyInfoByNip(nip);
               resultModel =  ResultModel.fromCursor(res);
            }

        }

        regon_tv.setText(resultModel.getRegon());
        np_tv.setText(resultModel.getNip());
        name_tv.setText(resultModel.getName());
        province_tv.setText(resultModel.getProvince());
        city_tv.setText(resultModel.getCity());
        btn_return.setOnClickListener((View v) ->{
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
        btn_saveToDb.setOnClickListener((View v) ->{
            saveToDb(resultModel);
        });
    }


    private void saveToDb(ResultModel resultModel) {
        if(mydb.getCompanyInfoByNip(resultModel.getNip()).getCount() >0){

            new AlertDialog.Builder(ResultActivity.this)
                    .setTitle("Błąd")
                    .setMessage("W bazie jest juz ta firma")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }
        else
        {
            mydb.insertCompanyInfo(resultModel.getName(), resultModel.getNip(), resultModel.getRegon(),resultModel.getCity(), resultModel.getProvince());

            new AlertDialog.Builder(ResultActivity.this)
                    .setTitle("Sukcess")
                    .setMessage("Firma została umieszczona w lokalnym katalogu")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }

    }
}