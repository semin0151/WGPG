package com.example.wgpg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerSkillAdapter extends RecyclerView .Adapter<RecyclerSkillAdapter.ViewHolder>{
    private ArrayList<RecyclerSkillItem> lists = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_skill;
        private TextView tv_skill_level;
        private TextView tv_skill_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_skill = (TextView)itemView.findViewById(R.id.tv_skill);
            tv_skill_level = (TextView)itemView.findViewById(R.id.tv_skill_level);
            tv_skill_content = (TextView)itemView.findViewById(R.id.tv_skill_content);
        }
    }

    @NonNull
    @Override
    public RecyclerSkillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_skill, parent, false);
        RecyclerSkillAdapter.ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerSkillAdapter.ViewHolder holder, int position) {
        RecyclerSkillItem item = lists.get(position);

        holder.tv_skill.setText(item.getSkill());
        holder.tv_skill_level.setText(item.getLevel());
        holder.tv_skill_content.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addItem(String skill, String level, String content){
        RecyclerSkillItem item = new RecyclerSkillItem();

        item.setSkill(skill);
        item.setLevel(level);
        item.setContent(level);

        lists.add(item);
    }
}
