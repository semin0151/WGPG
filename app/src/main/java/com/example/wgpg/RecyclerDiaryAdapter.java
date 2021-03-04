package com.example.wgpg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerDiaryAdapter extends RecyclerView.Adapter<RecyclerDiaryAdapter.ViewHolder>{
    private ArrayList<RecyclerDiaryItem> lists = new ArrayList<>();
    private SQLiteDatabase sqliteDB;
    private boolean modify;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    RecyclerDiaryAdapter(){
        modify = false;
    }

    RecyclerDiaryAdapter(SQLiteDatabase db, FragmentActivity activity){
        modify = true;
        this.sqliteDB = db;
        fragmentManager = activity.getSupportFragmentManager();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_diary_category;
        private TextView tv_diary_title;
        private TextView tv_diary_date;
        private TextView tv_diary_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_diary_category = (TextView)itemView.findViewById(R.id.tv_diary_category);
            tv_diary_title = (TextView)itemView.findViewById(R.id.tv_diary_title);
            tv_diary_date = (TextView)itemView.findViewById(R.id.tv_diary_date);
            tv_diary_time = (TextView)itemView.findViewById(R.id.tv_diary_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_diary,parent,false);

        RecyclerDiaryAdapter.ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerDiaryItem item = lists.get(position);

        holder.tv_diary_category.setText(item.getCategory());
        holder.tv_diary_title.setText(item.getTitle());
        holder.tv_diary_date.setText(item.getDate());
        holder.tv_diary_time.setText(item.getTime());

        delItem(holder);
        selItem(holder);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addItem(String category, String title, String content, String date, String time){
        RecyclerDiaryItem item = new RecyclerDiaryItem();

        item.setCategory(category);
        item.setTitle(title);
        item.setContent(content);
        item.setDate(date);
        item.setTime(time);

        lists.add(item);
    }

    private void delItem(ViewHolder holder){
        if(modify){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    sqliteDB.execSQL("DELETE FROM DIARY WHERE TITLE = '" + lists.get(holder.getAdapterPosition()).getTitle() + "' " +
                            "AND CONTENT = '" + lists.get(holder.getAdapterPosition()).getContent() + "' " +
                            "AND DATE = '" + lists.get(holder.getAdapterPosition()).getDate() + "' " +
                            "AND TIME ='" + lists.get(holder.getAdapterPosition()).getTime() + "'");
                    try{
                        lists.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }
    }

    private void selItem(ViewHolder holder){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new DiaryShowFragment(lists.get(holder.getAdapterPosition()));
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
