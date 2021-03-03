package com.example.wgpg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerSkillAdapter extends RecyclerView .Adapter<RecyclerSkillAdapter.ViewHolder>{
    private ArrayList<RecyclerSkillItem> lists = new ArrayList<>();
    private SQLiteDatabase sqliteDB;
    private boolean modify;

    RecyclerSkillAdapter(){
        modify = false;
    }

    RecyclerSkillAdapter(SQLiteDatabase db){
        modify = true;
        this.sqliteDB = db;
    }

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

        if(modify) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    sqliteDB.execSQL("DELETE FROM SKILL WHERE SKILL = '" + lists.get(holder.getAdapterPosition()).getSkill() + "'");
                    delItem(holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addItem(String skill, String level, String content){
        RecyclerSkillItem item = new RecyclerSkillItem();

        item.setSkill(skill);
        item.setLevel(level);
        item.setContent(content);

        lists.add(item);
    }

    public void delItem(int position){
        try{
            lists.remove(position);
            notifyItemRemoved(position);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
