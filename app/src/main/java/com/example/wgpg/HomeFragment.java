package com.example.wgpg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.InputStream;

public class HomeFragment extends Fragment {
    private View view;

    private Button btn_home_modify;

    private ImageView iv_profile;
    private ImageView iv_link1;
    private ImageView iv_link2;
    private ImageView iv_link3;

    private TextView tv_home_name;
    private TextView tv_home_infs;
    private TextView tv_home_content;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SharedPreferences sharedPreferences;

    private String name = "", infs = "", content = "";

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        init_view();

        btn_clicked();
        pop_shared();
        setImageview();
        set_profile();
        set_link();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        push_shared();
    }

    private void init_view(){
        btn_home_modify = (Button)view.findViewById(R.id.btn_home_modify);

        iv_profile = (ImageView)view.findViewById(R.id.iv_profile);
        iv_link1 = (ImageView)view.findViewById(R.id.iv_link1);
        iv_link2 = (ImageView)view.findViewById(R.id.iv_link2);
        iv_link3 = (ImageView)view.findViewById(R.id.iv_link3);

        tv_home_name = (TextView)view.findViewById(R.id.tv_home_name);
        tv_home_infs = (TextView)view.findViewById(R.id.tv_home_infs);
        tv_home_content = (TextView)view.findViewById(R.id.tv_home_content);
    }

    private void set_profile(){
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HomeShowProfileFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void set_link(){
        iv_link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HomeShowLinkFragment(0);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        iv_link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HomeShowLinkFragment(1);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        iv_link3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HomeShowLinkFragment(2);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void btn_clicked(){
        btn_home_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HomeModifyFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void setImageview(){
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(getActivity().getFilesDir().toString() + "/home_profile.jpg");
            Bitmap link = BitmapFactory.decodeResource(getResources(),R.drawable.home_link);
            bitmap = getBitmapSquareCrop(bitmap, bitmap.getWidth(), bitmap.getHeight());
            bitmap = getBitmapCircleCrop(bitmap, bitmap.getWidth(), bitmap.getHeight());
            link = getBitmapSquareCrop(link, link.getWidth(), link.getHeight());
            link = getBitmapCircleCrop(link, link.getWidth(), link.getHeight());
            iv_profile.setImageBitmap(bitmap);
            iv_link1.setImageBitmap(link);
            iv_link2.setImageBitmap(link);
            iv_link3.setImageBitmap(link);
        }
        catch (Exception e){

        }
    }

    private static Bitmap getBitmapCircleCrop(Bitmap bitmap, int Width, int Height) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        Bitmap CroppedBitmap = output;

        return CroppedBitmap;
    }

    private static Bitmap getBitmapSquareCrop(Bitmap bitmap, int Width, int Height){
        Bitmap CroppedBitmap;
        if(bitmap.getWidth()<bitmap.getHeight())
            CroppedBitmap = Bitmap.createBitmap(bitmap,0,(Height-Width)/2,bitmap.getWidth(),bitmap.getWidth());
        else if(bitmap.getWidth()>bitmap.getHeight())
            CroppedBitmap = Bitmap.createBitmap(bitmap,(Width-Height)/2,0,bitmap.getHeight(),bitmap.getHeight());
        else
            CroppedBitmap = bitmap;

        return CroppedBitmap;
    }

    private void push_shared(){
        sharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        name = tv_home_name.getText().toString();
        infs = tv_home_infs.getText().toString();
        content = tv_home_content.getText().toString();

        editor.putString("home_name",name);
        editor.putString("home_infs",infs);
        editor.putString("home_content",content);
        editor.commit();
    }

    private void pop_shared(){
        try {

            sharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);

            name = sharedPreferences.getString("home_name", name);
            infs = sharedPreferences.getString("home_infs", infs);
            content = sharedPreferences.getString("home_content", content);

            tv_home_name.setText(name);
            tv_home_infs.setText(infs);
            tv_home_content.setText(content);
        }catch (Exception e){
            tv_home_name.setText(e.getMessage());
        }
    }
}
