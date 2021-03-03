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

public class RecyclerAwardAdapter extends RecyclerView.Adapter<RecyclerAwardAdapter.ViewHolder>{
    private ArrayList<RecyclerAwardItem> lists = new ArrayList<>();
    private SQLiteDatabase sqliteDB;
    private boolean modify;

    RecyclerAwardAdapter(){
        modify = false;
    }

    RecyclerAwardAdapter(SQLiteDatabase db){
        modify = true;
        this.sqliteDB = db;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_award_date;
        private TextView tv_award_category;
        private TextView tv_award_organ;
        private TextView tv_award_language;
        private TextView tv_award_point;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_award_date = (TextView)itemView.findViewById(R.id.tv_award_date);
            tv_award_category = (TextView)itemView.findViewById(R.id.tv_award_category);
            tv_award_organ = (TextView)itemView.findViewById(R.id.tv_award_organ);
            tv_award_language = (TextView)itemView.findViewById(R.id.tv_award_language);
            tv_award_point = (TextView)itemView.findViewById(R.id.tv_award_point);
        }
    }

    @NonNull
    @Override
    public RecyclerAwardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_award,parent,false);
        RecyclerAwardAdapter.ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAwardAdapter.ViewHolder holder, int position) {
        RecyclerAwardItem item = lists.get(position);

        holder.tv_award_date.setText(item.getDate());
        holder.tv_award_category.setText(item.getCategory());
        holder.tv_award_organ.setText(item.getOrgan());
        holder.tv_award_language.setText(item.getLanguage());
        holder.tv_award_point.setText(item.getPoint());

        if(modify) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    sqliteDB.execSQL("DELETE FROM AWARD WHERE LANGUAGE = '" + lists.get(holder.getAdapterPosition()).getLanguage() + "'");
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

    public void addItem(String date, String category, String organ, String language, String point){
        RecyclerAwardItem item = new RecyclerAwardItem();

        item.setDate(date);
        item.setCategory(category);
        item.setOrgan(organ);
        item.setLanguage(language);
        item.setPoint(point);

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
