package com.example.wgpg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeShowProfileFragment extends Fragment {
    private View view;

    private Button btn_home_back_profile;

    private ImageView iv_home_large_profile;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public HomeShowProfileFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_show_profile, container,false);
        init_view();
        set_imageview();
        btn_clicked();

        return view;
    }

    private void init_view(){
        btn_home_back_profile = (Button)view.findViewById(R.id.btn_home_back_profile);
        iv_home_large_profile = (ImageView)view.findViewById(R.id.iv_home_large_profile);
    }

    private void btn_clicked(){
        btn_home_back_profile.setOnClickListener(new View.OnClickListener() {
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

    private void set_imageview(){
        Bitmap bitmap = BitmapFactory.decodeFile(getActivity().getFilesDir().toString() + "/home_profile.jpg");
        iv_home_large_profile.setImageBitmap(bitmap);
    }
}
