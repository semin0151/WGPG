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

public class RecyclerActivityAdapter extends RecyclerView.Adapter<RecyclerActivityAdapter.ViewHolder>{
    private ArrayList<RecyclerActivityItem> lists = new ArrayList<>();
    private SQLiteDatabase sqliteDB;
    private boolean modify;

    RecyclerActivityAdapter(){
        modify = false;
    }

    RecyclerActivityAdapter(SQLiteDatabase db){
        modify = true;
        this.sqliteDB = db;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_activity_period;
        private TextView tv_activity_category;
        private TextView tv_activity_organ;
        private TextView tv_activity_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_activity_period = (TextView)itemView.findViewById(R.id.tv_activity_period);
            tv_activity_category = (TextView)itemView.findViewById(R.id.tv_activity_category);
            tv_activity_organ = (TextView)itemView.findViewById(R.id.tv_activity_organ);
            tv_activity_content = (TextView)itemView.findViewById(R.id.tv_activity_content);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_activity,parent,false);
        RecyclerActivityAdapter.ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerActivityItem item = lists.get(position);

        holder.tv_activity_period.setText(item.getPeriod());
        holder.tv_activity_category.setText(item.getCategory());
        holder.tv_activity_organ.setText(item.getOrgan());
        holder.tv_activity_content.setText(item.getContent());

        if(modify) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    sqliteDB.execSQL("DELETE FROM ACTIVITY WHERE CONTENT = '" + lists.get(holder.getAdapterPosition()).getContent() + "'");
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

    public void addItem(String period, String category, String organ, String content){
        RecyclerActivityItem item = new RecyclerActivityItem();

        item.setPeriod(period);
        item.setCategory(category);
        item.setOrgan(organ);
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
