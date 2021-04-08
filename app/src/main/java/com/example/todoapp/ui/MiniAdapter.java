package com.example.todoapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Random;

public class MiniAdapter extends RecyclerView.Adapter<MiniAdapter.FirstViewHolder> {
    private ArrayList<String> content;
    private TextView show;
    private String selectedItem;
    private Object HomeFragment;

    // Daten werden von der Activity hineingereicht
    public MiniAdapter(ArrayList<String> content) {
        this.content = content;
    }

    @NonNull
    @Override
    public FirstViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Datendarstellung holen
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.text_item,parent,false);
        FirstViewHolder viewHolder = new FirstViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FirstViewHolder holder, int position) {
        final String s = content.get(position);
        holder.textView.setText(s);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity act = (AppCompatActivity)view.getContext();
                TextView show = act.findViewById(R.id.show);
                show.setText("To-Do Punkt \"" + s + "\" ausgewählt.");
                selectedItem = s;
            }
        });



    }
    @Override
    public int getItemCount() {
        return content.size();
    }

    public class FirstViewHolder extends RecyclerView.ViewHolder {
        private View layout;
        private TextView textView;

        public FirstViewHolder(View v) {
            super(v);
            layout = v;
            textView = v.findViewById(R.id.tv_list);
        }

        public void add(int position, String item) {
            content.add(position,item);
            notifyItemInserted(position);
        }

        public void remove(int position, String item) {
            content.remove(position);
            notifyItemRemoved(position);
        }

        public void selected(int randomInt) {
            AppCompatActivity act;
            act = (AppCompatActivity) HomeFragment;
            TextView show = act.findViewById(R.id.show);
            show.setText(content.get(randomInt).toString());
        }

    }



        public String getSelectedItem() {
        return selectedItem;
    }
}

