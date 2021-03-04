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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.InputStream;

public class HomeModifyFragment extends Fragment {
    private View view;

    private Button btn_home_save;

    private EditText et_home_name;
    private EditText et_home_infs;
    private EditText et_home_content;

    private ImageView iv_profile;
    private ImageView iv_link1;
    private ImageView iv_link2;
    private ImageView iv_link3;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SharedPreferences sharedPreferences;

    private String name, infs, content;
    private boolean profile = false, link1 = false, link2 = false, link3 = false;

    public HomeModifyFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_modify, container, false);

        init_view();

        btn_clicked();
        setImage();
        pop_shared();

        return view;
    }

    private void btn_clicked(){
        btn_home_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HomeFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.commit();
                push_shared();
            }
        });
    }

    private void init_view(){
        btn_home_save = (Button)view.findViewById(R.id.btn_home_save);

        et_home_name = (EditText)view.findViewById(R.id.et_home_name);
        et_home_infs = (EditText)view.findViewById(R.id.et_home_infs);
        et_home_content = (EditText)view.findViewById(R.id.et_home_content);

        iv_profile = (ImageView)view.findViewById(R.id.iv_profile);
        iv_link1 = (ImageView)view.findViewById(R.id.iv_link1);
        iv_link2 = (ImageView)view.findViewById(R.id.iv_link2);
        iv_link3 = (ImageView)view.findViewById(R.id.iv_link3);
    }

    private void setImage(){
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile = true;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,0);
            }
        });
        iv_link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link1 = true;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,0);
            }
        });
        iv_link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link2 = true;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,0);
            }
        });
        iv_link3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link3 = true;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == getActivity().RESULT_CANCELED) {
            try {
                InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(in);

                bitmap = getBitmapSquareCrop(bitmap, bitmap.getWidth(), bitmap.getHeight());
                bitmap = getBitmapCircleCrop(bitmap, bitmap.getWidth(), bitmap.getHeight());
                in.close();

                if(profile) {
                    iv_profile.setImageBitmap(bitmap);
                    profile = false;
                }else if(link1){
                    iv_link1.setImageBitmap(bitmap);
                    link1 = false;
                }else if(link2){
                    iv_link2.setImageBitmap(bitmap);
                    link2 = false;
                }else if(link3){
                    iv_link3.setImageBitmap(bitmap);
                    link3 = false;
                }
            } catch (Exception e) {

            }
        }
    }

    public static Bitmap getBitmapCircleCrop(Bitmap bitmap, int Width, int Height) {

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

    public static Bitmap getBitmapSquareCrop(Bitmap bitmap, int Width, int Height){
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
        name = et_home_name.getText().toString();
        infs = et_home_infs.getText().toString();
        content = et_home_content.getText().toString();

        editor.putString("home_name",name);
        editor.putString("home_infs",infs);
        editor.putString("home_content",content);
        editor.commit();
    }

    private void pop_shared(){
        sharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("home_name",name);
        infs = sharedPreferences.getString("home_infs",infs);
        content = sharedPreferences.getString("home_content",content);

        et_home_name.setText(name);
        et_home_infs.setText(infs);
        et_home_content.setText(content);
    }
}