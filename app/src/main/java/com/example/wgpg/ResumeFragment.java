package com.example.wgpg;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class ResumeFragment extends Fragment {
    private View view;
    private Button btn_modify;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private RecyclerView rv_activity;
    private RecyclerView rv_award;
    private RecyclerView rv_skill;

    private RecyclerActivityAdapter adapter_activity;
    private RecyclerAwardAdapter adapter_award;
    private RecyclerSkillAdapter adapter_skill;

    private SQLiteDatabase sqliteDB;

    public ResumeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resume,container, false);
        btn_modify = (Button)view.findViewById(R.id.btn_modify);
        rv_activity = (RecyclerView)view.findViewById(R.id.rv_activity);
        rv_award = (RecyclerView)view.findViewById(R.id.rv_award);
        rv_skill = (RecyclerView)view.findViewById(R.id.rv_skill);

        sqliteDB = init_DB();

        btn_clicked();
        init_tables();
        init_rv();
        load_values();

        return view;
    }

    public void btn_clicked(){
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ResumeModifyFragment();
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
            String sqlCreateTbl;
            sqlCreateTbl = "CREATE TABLE IF NOT EXISTS ACTIVITY (" +
                    "PERIOD "        +"TEXT," +
                    "CATEGORY "      +"TEXT," +
                    "ORGAN "         +"TEXT," +
                    "CONTENT "       +"TEXT" +
                    ")";
            sqliteDB.execSQL(sqlCreateTbl);

            sqlCreateTbl = "CREATE TABLE IF NOT EXISTS AWARD (" +
                    "DATE "             +"TEXT," +
                    "CATEGORY "         +"TEXT," +
                    "ORGAN "            +"TEXT," +
                    "LANGUAGE "         +"TEXT," +
                    "POINT "            +"TEXT" +
                    ")";
            sqliteDB.execSQL(sqlCreateTbl);

            sqlCreateTbl = "CREATE TABLE IF NOT EXISTS SKILL (" +
                    "SKILL "            +"TEXT," +
                    "LEVEL "            +"TEXT," +
                    "CONTENT "          +"TEXT" +
                    ")";
            sqliteDB.execSQL(sqlCreateTbl);
        }
    }

    private void init_rv(){
        rv_activity.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_award.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_skill.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter_activity = new RecyclerActivityAdapter();
        adapter_award = new RecyclerAwardAdapter();
        adapter_skill = new RecyclerSkillAdapter();

        rv_activity.setAdapter(adapter_activity);
        rv_award.setAdapter(adapter_award);
        rv_skill.setAdapter(adapter_skill);
    }

    private void load_values(){
        if(sqliteDB != null){
            Cursor cursor_activity = null, cursor_award = null, cursor_skill = null;
            cursor_activity = sqliteDB.rawQuery("SELECT * FROM ACTIVITY", null);
            cursor_award = sqliteDB.rawQuery("SELECT * FROM AWARD", null);
            cursor_skill = sqliteDB.rawQuery("SELECT * FROM SKILL", null);

            while(cursor_activity.moveToNext()){

                adapter_activity.addItem(cursor_activity.getString(0),
                        cursor_activity.getString(1),
                        cursor_activity.getString(2),
                        cursor_activity.getString(3));

            }
            while(cursor_award.moveToNext()){

                adapter_award.addItem(cursor_award.getString(0),
                        cursor_award.getString(1),
                        cursor_award.getString(2),
                        cursor_award.getString(3),
                        cursor_award.getString(4));

            }
            while(cursor_skill.moveToNext()){

                adapter_skill.addItem(cursor_skill.getString(0),
                        cursor_skill.getString(1),
                        cursor_skill.getString(2));

            }
        }
    }
}
