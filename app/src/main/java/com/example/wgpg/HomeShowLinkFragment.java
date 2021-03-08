package com.example.wgpg;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;

public class HomeShowLinkFragment extends Fragment {
    private View view;

    private Button btn_home_back_link;

    private WebView wv_home_large_link;

    private SQLiteDatabase sqliteDB;
    private Cursor cursor;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int position;
    private String url;

    public HomeShowLinkFragment(int position){
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_show_link,container,false);
        init_view();

        sqliteDB = init_DB();
        init_tables();
        load_values();
        set_webview();
        btn_clicked();

        return view;
    }

    private void init_view(){
        wv_home_large_link = (WebView)view.findViewById(R.id.wv_home_large_link);
        btn_home_back_link = (Button)view.findViewById(R.id.btn_home_back_link) ;
    }

    private void btn_clicked(){
        btn_home_back_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HomeFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.commit();
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

    private void set_webview(){
        wv_home_large_link.getSettings().setJavaScriptEnabled(true);
        wv_home_large_link.loadUrl(url);
        wv_home_large_link.setWebChromeClient(new WebChromeClient());
        wv_home_large_link.setWebViewClient(new WebViewClient());
    }

    private void load_values(){
        if(sqliteDB != null){
            Cursor cursor_link = null;
            cursor_link = sqliteDB.rawQuery("SELECT * FROM LINK", null);

            while(cursor_link.moveToNext()){
                url = cursor_link.getString(position);
            }
        }
    }
}
