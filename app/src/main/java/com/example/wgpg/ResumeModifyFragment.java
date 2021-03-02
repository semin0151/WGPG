package com.example.wgpg;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class ResumeModifyFragment extends Fragment {
    private View view;
    private Button btn_save;
    private Button btn_add_activity;
    private Button btn_add_award;
    private Button btn_add_skill;

    private EditText et_activity_period;
    private EditText et_activity_category;
    private EditText et_activity_organ;
    private EditText et_activity_content;
    private EditText et_award_date;
    private EditText et_award_category;
    private EditText et_award_organ;
    private EditText et_award_language;
    private EditText et_award_point;
    private EditText et_skill;
    private EditText et_skill_level;
    private EditText et_skill_content;

    private RecyclerView rv_activity;
    private RecyclerView rv_award;
    private RecyclerView rv_skill;

    private RecyclerActivityAdapter adapter_activity;
    private RecyclerAwardAdapter adapter_award;
    private RecyclerSkillAdapter adapter_skill;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SQLiteDatabase sqliteDB;

    public ResumeModifyFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resume_modify,container, false);
        /*
        rv_activity = (RecyclerView)view.findViewById(R.id.rv_activity);
        rv_award = (RecyclerView)view.findViewById(R.id.rv_award);
        rv_skill = (RecyclerView)view.findViewById(R.id.rv_skill);
*/
        init_view();
        sqliteDB = init_DB();
        btn_clicked();

        sqliteDB.execSQL("DELETE FROM ACTIVITY");
        sqliteDB.execSQL("DELETE FROM AWARD");
        sqliteDB.execSQL("DELETE FROM SKILL");

        init_tables();
        //init_rv();
        save_values();

        return view;
    }

    public void btn_clicked(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ResumeFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void init_view(){
        btn_save = (Button)view.findViewById(R.id.btn_save);
        btn_add_activity = (Button)view.findViewById(R.id.btn_add_activity);
        btn_add_award = (Button)view.findViewById(R.id.btn_add_award);
        btn_add_skill = (Button)view.findViewById(R.id.btn_add_skill);
        et_activity_period = (EditText)view.findViewById(R.id.et_activity_period);
        et_activity_category = (EditText)view.findViewById(R.id.et_activity_category);
        et_activity_organ = (EditText)view.findViewById(R.id.et_activity_organ);
        et_activity_content = (EditText)view.findViewById(R.id.et_activity_content);
        et_award_date = (EditText)view.findViewById(R.id.et_award_date);
        et_award_category = (EditText)view.findViewById(R.id.et_award_category);
        et_award_organ = (EditText)view.findViewById(R.id.et_award_organ);
        et_award_language = (EditText)view.findViewById(R.id.et_award_language);
        et_award_point = (EditText)view.findViewById(R.id.et_award_point);
        et_skill = (EditText)view.findViewById(R.id.et_skill);
        et_skill_level = (EditText)view.findViewById(R.id.et_skill_level);
        et_skill_content = (EditText)view.findViewById(R.id.et_skill_content);
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
            String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS ACTIVITY (" +
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
/*
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
*/
    private void save_values(){
            btn_add_activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sqlInsert = "INSERT INTO ACTIVITY" +
                            "(PERIOD, CATEGORY, ORGAN, CONTENT) VALUES (" +
                            "'" + et_activity_period.getText().toString() + "'" + "," +
                            "'" + et_activity_category.getText().toString() + "'" + "," +
                            "'"+ et_activity_organ.getText().toString() + "'" + "," +
                            "'"+ et_activity_content.getText().toString() +"'" + ")";
                    sqliteDB.execSQL(sqlInsert);

                    et_activity_period.setText("");
                    et_activity_category.setText("");
                    et_activity_organ.setText("");
                    et_activity_content.setText("");
                }
            });

            btn_add_award.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sqlInsert = "INSERT INTO AWARD" +
                            "(DATE, CATEGORY, ORGAN, LANGUAGE, POINT) VALUES (" +
                            "'" + et_award_date.getText().toString() + "'" + "," +
                            "'" + et_award_category.getText().toString() + "'" + "," +
                            "'"+ et_award_organ.getText().toString() + "'" + "," +
                            "'"+ et_award_language.getText().toString() + "'" + "," +
                            "'"+ et_award_point.getText().toString() +"'" + ")";
                    sqliteDB.execSQL(sqlInsert);

                    et_award_date.setText("");
                    et_award_category.setText("");
                    et_award_organ.setText("");
                    et_award_language.setText("");
                    et_award_point.setText("");
                }
            });

            btn_add_skill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sqlInsert = "INSERT INTO SKILL" +
                            "(SKILL, LEVEL, CONTENT) VALUES (" +
                            "'" + et_skill.getText().toString() + "'" + "," +
                            "'" + et_skill_level.getText().toString() + "'" + "," +
                            "'" + et_skill_content.getText().toString() +"'" + ")";
                    sqliteDB.execSQL(sqlInsert);

                    et_skill.setText("");
                    et_skill_level.setText("");
                    et_skill_content.setText("");
                }
            });
    }
}
