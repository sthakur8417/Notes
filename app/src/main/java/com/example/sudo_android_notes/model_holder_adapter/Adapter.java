package com.example.sudo_android_notes.model_holder_adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudo_android_notes.MainActivity;
import com.example.sudo_android_notes.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    List<Notes> notesList;
    MainActivity mainActivity;

    public Adapter(List<Notes> notesList, MainActivity mainActivity){
        this.notesList = notesList;
        this.mainActivity = mainActivity;
        Log.d("Construtor Called",notesList.toString());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d("onCreateViewHolder",notesList.toString());


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_view, parent, false);

        view.setOnClickListener((View.OnClickListener) mainActivity);
        view.setOnLongClickListener((View.OnLongClickListener) mainActivity);

        Log.d("onCreateViewHolder",notesList.toString());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.note_title_field.setText(notesList.get(position).getNotes_title());
        String detail = notesList.get(position).getNotes_text();
        detail=detail.replaceAll("\n"," ");

        if(detail.length()>80){
            holder.note_text_field.setText(detail.substring(0,80)+"...");
        }else{
            holder.note_text_field.setText(detail);
        }
        holder.note_date_field.setText(notesList.get(position).getNotes_date());

        Log.d("onBindViewHolder",notesList.toString());
    }


    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView note_title_field;
        TextView note_text_field;
        TextView note_date_field;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            note_title_field = itemView.findViewById(R.id.note_title_field);
            note_text_field = itemView.findViewById(R.id.note_text_field);
            note_date_field = itemView.findViewById(R.id.note_date_field);
        }
    }
}
