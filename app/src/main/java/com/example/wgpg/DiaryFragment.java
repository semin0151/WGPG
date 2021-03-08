package com.example.wgpg;

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

public class DiaryFragment extends Fragment {
    private View view;
    private Button btn_diary_new;

    private RecyclerView rv_diary;

    private RecyclerDiaryAdapter adapter_diary;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SQLiteDatabase sqliteDB;
    public DiaryFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary, container, false);
        init_view();
        sqliteDB = init_DB();

        btn_clicked();
        init_tables();
        init_rv();
        load_values();
        return view;
    }

    private void init_view(){
        rv_diary = (RecyclerView)view.findViewById(R.id.rv_diary);
        btn_diary_new = (Button)view.findViewById(R.id.btn_diary_new);
    }

    private void btn_clicked(){
        btn_diary_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new DiaryAddFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.addToBackStack(null);
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

    private void init_rv(){
        rv_diary.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter_diary = new RecyclerDiaryAdapter(sqliteDB,getActivity());

        rv_diary.setAdapter(adapter_diary);
    }

    private void load_values(){
        if(sqliteDB != null){
            Cursor cursor_diary = null;
            cursor_diary = sqliteDB.rawQuery("SELECT * FROM DIARY", null);

            while(cursor_diary.moveToNext()){
                adapter_diary.addItem("[" + cursor_diary.getString(0) +"]",
                        cursor_diary.getString(1),
                        cursor_diary.getString(2),
                        cursor_diary.getString(3),
                        cursor_diary.getString(4));
            }
        }
    }
}
