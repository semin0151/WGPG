package com.example.wgpg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class DiaryShowFragment extends Fragment {
    private View view;
    private RecyclerDiaryItem item;

    private TextView tv_diary_category;
    private TextView tv_diary_title;
    private TextView tv_diary_content;
    private TextView tv_diary_date;
    private TextView tv_diary_time;

    DiaryShowFragment(RecyclerDiaryItem item){
        this.item = item;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary_show,container,false);
        init_view();

        return view;
    }
    private void init_view(){
        tv_diary_category = (TextView)view.findViewById(R.id.tv_diary_category);
        tv_diary_title = (TextView)view.findViewById(R.id.tv_diary_title);
        tv_diary_content = (TextView)view.findViewById(R.id.tv_diary_content);
        tv_diary_date = (TextView)view.findViewById(R.id.tv_diary_date);
        tv_diary_time = (TextView)view.findViewById(R.id.tv_diary_time);

        tv_diary_category.setText(item.getCategory());
        tv_diary_title.setText(item.getTitle());
        tv_diary_content.setText(item.getContent());
        tv_diary_date.setText(item.getDate());
        tv_diary_time.setText(item.getTime());
    }
}
