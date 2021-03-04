package com.example.wgpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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

    private EditText et_name;
    private EditText et_date;
    private EditText et_address1;
    private EditText et_address2;
    private EditText et_phone;
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
    private SharedPreferences sharedPreferences;

    private String name, date, address1, address2, phone;

    public ResumeModifyFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resume_modify,container, false);

        init_view();
        sqliteDB = init_DB();

        btn_clicked();
        init_tables();
        init_rv();
        save_values();
        load_values();
        pop_shared();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        push_shared();
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
        btn_save                = (Button)view.findViewById(R.id.btn_save);
        btn_add_activity        = (Button)view.findViewById(R.id.btn_add_activity);
        btn_add_award           = (Button)view.findViewById(R.id.btn_add_award);
        btn_add_skill           = (Button)view.findViewById(R.id.btn_add_skill);

        et_activity_period      = (EditText)view.findViewById(R.id.et_activity_period);
        et_activity_category    = (EditText)view.findViewById(R.id.et_activity_category);
        et_activity_organ       = (EditText)view.findViewById(R.id.et_activity_organ);
        et_activity_content     = (EditText)view.findViewById(R.id.et_activity_content);
        et_award_date           = (EditText)view.findViewById(R.id.et_award_date);
        et_award_category       = (EditText)view.findViewById(R.id.et_award_category);
        et_award_organ          = (EditText)view.findViewById(R.id.et_award_organ);
        et_award_language       = (EditText)view.findViewById(R.id.et_award_language);
        et_award_point          = (EditText)view.findViewById(R.id.et_award_point);
        et_skill                = (EditText)view.findViewById(R.id.et_skill);
        et_skill_level          = (EditText)view.findViewById(R.id.et_skill_level);
        et_skill_content        = (EditText)view.findViewById(R.id.et_skill_content);
        et_name                 = (EditText)view.findViewById(R.id.et_name);
        et_date                 = (EditText)view.findViewById(R.id.et_date);
        et_address1             = (EditText)view.findViewById(R.id.et_address1);
        et_address2             = (EditText)view.findViewById(R.id.et_address2);
        et_phone                = (EditText)view.findViewById(R.id.et_phone);

        rv_activity = (RecyclerView)view.findViewById(R.id.rv_activity);
        rv_award = (RecyclerView)view.findViewById(R.id.rv_award);
        rv_skill = (RecyclerView)view.findViewById(R.id.rv_skill);
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

    private void init_rv(){
        rv_activity.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_award.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_skill.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter_activity = new RecyclerActivityAdapter(sqliteDB);
        adapter_award = new RecyclerAwardAdapter(sqliteDB);
        adapter_skill = new RecyclerSkillAdapter(sqliteDB);

        rv_activity.setAdapter(adapter_activity);
        rv_award.setAdapter(adapter_award);
        rv_skill.setAdapter(adapter_skill);
    }

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
                    init_rv();
                    load_values();
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
                    init_rv();
                    load_values();
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
                    init_rv();
                    load_values();
                }
            });
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

    private void push_shared(){
        sharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        name = et_name.getText().toString();
        date = et_date.getText().toString();
        address1 = et_address1.getText().toString();
        address2 = et_address2.getText().toString();
        phone = et_phone.getText().toString();

        editor.putString("resume_name",name);
        editor.putString("resume_date",date);
        editor.putString("resume_address1",address1);
        editor.putString("resume_address2",address2);
        editor.putString("resume_phone",phone);
        editor.commit();
    }

    private void pop_shared(){
        sharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("resume_name",name);
        date = sharedPreferences.getString("resume_date",date);
        address1 = sharedPreferences.getString("resume_address1",address1);
        address2 = sharedPreferences.getString("resume_address2",address2);
        phone = sharedPreferences.getString("resume_phone",phone);

        et_name.setText(name);
        et_date.setText(date);
        et_address1.setText(address1);
        et_address2.setText(address2);
        et_phone.setText(phone);
    }
}
