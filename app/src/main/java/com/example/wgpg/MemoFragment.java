package com.example.wgpg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MemoFragment extends Fragment {
    private View view;
    private String str;
    private EditText et_memo;
    private SharedPreferences sharedPreferences;
    public MemoFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_memo, container,false);
        et_memo = (EditText)view.findViewById(R.id.et_memo);

        sharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        str = sharedPreferences.getString("str", "");
        et_memo.setText(str);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        str = et_memo.getText().toString();
        editor.putString("str",str);
        editor.commit();
    }
}
