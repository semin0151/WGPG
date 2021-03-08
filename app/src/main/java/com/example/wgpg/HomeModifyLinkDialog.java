package com.example.wgpg;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;

public class HomeModifyLinkDialog extends DialogFragment {
    private View view;

    private Button btn_home_save_link;
    private Button btn_home_cancel_link;
    private EditText et_home_link;

    private SQLiteDatabase sqliteDB;
    private Cursor cursor;

    private int position;

    public HomeModifyLinkDialog(int position){
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_home_modify_link, null);

        init_view();
        sqliteDB = init_DB();
        init_tables();
        btn_clicked();
        set_et();

        builder.setView(view);
        return builder.create();
    }

    private void init_view(){
        btn_home_save_link = (Button)view.findViewById(R.id.btn_home_save_link);
        btn_home_cancel_link = (Button)view.findViewById(R.id.btn_home_cancel_link);
        et_home_link = (EditText)view.findViewById(R.id.et_home_link);
    }

    private void btn_clicked(){
        btn_home_save_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sqlUpdateTbl = "UPDATE LINK SET LINK" + Integer.toString(position) + " = '" + et_home_link.getText().toString()  + "'";
                sqliteDB.execSQL(sqlUpdateTbl);
                dismiss();
            }
        });
        btn_home_cancel_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private SQLiteDatabase init_DB(){
        SQLiteDatabase db = null;

        File file = new File(getContext().getFilesDir(),"wgpg.db");
        try{
            db = SQLiteDatabase.openOrCreateDatabase(file, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return db;
    }

    private void init_tables(){
        if(sqliteDB != null){
            String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS LINK (" +
                    "LINK1 " + "TEXT," +
                    "LINK2 " + "TEXT," +
                    "LINK3 " + "TEXT)";
            sqliteDB.execSQL(sqlCreateTbl);

            cursor = sqliteDB.rawQuery("SELECT * FROM LINK", null);
            if(cursor.getCount()==0){
                String sqlInsert = "INSERT INTO LINK" +
                        "(LINK1, LINK2, LINK3) VALUES ('LINK1','LINK2','LINK3')";
                sqliteDB.execSQL(sqlInsert);
            }
        }
    }

    private void set_et(){
        cursor = sqliteDB.rawQuery("SELECT * FROM LINK", null);
        while(cursor.moveToNext()) {
            et_home_link.setText(cursor.getString(position-1));
        }
    }
}
