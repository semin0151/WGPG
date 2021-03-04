package com.example.wgpg;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryAddFragment extends Fragment {
    private View view;
    private Button btn_diary_save;
    private EditText et_diary_category;
    private EditText et_diary_title;
    private EditText et_diary_content;

    private TextView tv_test;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String strDate, strTime;

    private SQLiteDatabase sqliteDB;
    DiaryAddFragment(){
        strDate = new SimpleDateFormat("yyyy.MM.dd").format(new Date(System.currentTimeMillis()));
        strTime = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary_add, container, false);
        btn_diary_save = (Button)view.findViewById(R.id.btn_diary_save);
        et_diary_category = (EditText)view.findViewById(R.id.et_diary_category);
        et_diary_title = (EditText)view.findViewById(R.id.et_diary_title);
        et_diary_content = (EditText)view.findViewById(R.id.et_diary_content);
        tv_test = view.findViewById(R.id.tv_test);

        sqliteDB = init_DB();
        init_tables();
        btn_clicked();


        return view;
    }

    public void btn_clicked(){
        btn_diary_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_values();

                fragment = new DiaryFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.commit();
            }
        });
    }

    private SQLiteDatabase init_DB(){
        SQLiteDatabase db = null;

        File file= new File(getActivity().getFilesDir(),"wgpg.db");
        try {
            db = SQLiteDatabase.openOrCreateDatabase(file, null);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return db;
    }

    private void init_tables(){
        if(sqliteDB != null){
            String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS DIARY (" +
                    "CATEGORY "         +"TEXT," +
                    "TITLE "            +"TEXT," +
                    "CONTENT "          +"TEXT," +
                    "DATE "             +"TEXT," +
                    "TIME "             +"TEXT" +
                    ")";
            sqliteDB.execSQL(sqlCreateTbl);
        }
    }

    private void save_values(){
        try {
            String sqlInsert = "INSERT INTO DIARY" +
                    "(CATEGORY, TITLE, CONTENT, DATE, TIME) VALUES (" +
                    "'" + et_diary_category.getText().toString() + "'" + "," +
                    "'" + et_diary_title.getText().toString() + "'" + "," +
                    "'" + et_diary_content.getText().toString() + "'" + "," +
                    "'" + strDate + "'" + "," +
                    "'" + strTime + "'" + ")";
            sqliteDB.execSQL(sqlInsert);
        }catch (Exception e){
            tv_test.setText(e.getMessage());
        }

    }
}
