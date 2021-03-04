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

    private RecyclerView rv_activity;
    private RecyclerView rv_award;
    private RecyclerView rv_skill;

    private RecyclerActivityAdapter adapter_activity;
    private RecyclerAwardAdapter adapter_award;
    private RecyclerSkillAdapter adapter_skill;

    private TextView tv_name_resume;
    private TextView tv_date_resume;
    private TextView tv_address1_resume;
    private TextView tv_address2_resume;
    private TextView tv_phone_resume;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SQLiteDatabase sqliteDB;
    private SharedPreferences sharedPreferences;

    private String name, date, address1, address2, phone;

    public ResumeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resume,container, false);

        init_view();
        sqliteDB = init_DB();

        btn_clicked();
        init_tables();
        init_rv();
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

    private void init_view(){
        btn_modify      = (Button)view.findViewById(R.id.btn_modify);

        rv_activity     = (RecyclerView)view.findViewById(R.id.rv_activity);
        rv_award        = (RecyclerView)view.findViewById(R.id.rv_award);
        rv_skill        = (RecyclerView)view.findViewById(R.id.rv_skill);

        tv_name_resume  = (TextView)view.findViewById(R.id.tv_name_resume);
        tv_date_resume  = (TextView)view.findViewById(R.id.tv_date_resume);
        tv_address1_resume = (TextView)view.findViewById(R.id.tv_address1_resume);
        tv_address2_resume = (TextView)view.findViewById(R.id.tv_address2_resume);
        tv_phone_resume = (TextView)view.findViewById(R.id.tv_phone_resume);
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

    private void push_shared(){
        sharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        name = tv_name_resume.getText().toString();
        date = tv_date_resume.getText().toString();
        address1 = tv_address1_resume.getText().toString();
        address2 = tv_address2_resume.getText().toString();
        phone = tv_phone_resume.getText().toString();

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

        tv_name_resume.setText(name);
        tv_date_resume.setText(date);
        tv_address1_resume.setText(address1);
        tv_address2_resume.setText(address2);
        tv_phone_resume.setText(phone);
    }
}
