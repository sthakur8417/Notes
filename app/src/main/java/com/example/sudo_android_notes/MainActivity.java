package com.example.sudo_android_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.sudo_android_notes.model_holder_adapter.Adapter;
import com.example.sudo_android_notes.model_holder_adapter.Notes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    RecyclerView recyclerNotesView;
    Adapter recyclerAdapter;
    List<Notes> notesArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("onCreate",notesArrayList.toString());

        recyclerNotesView=findViewById(R.id.recyclerView);
        recyclerAdapter=new Adapter(notesArrayList,this);
        recyclerNotesView.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerNotesView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart()
    {
        notesArrayList.clear();
        notesArrayList.addAll(jsonNotesLoad());
        Log.d("onStart",notesArrayList.toString());
        setTitle("Android Notes ("+notesArrayList.size()+")");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Collections.sort(notesArrayList);
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        finish();
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_add:
                Intent intent = new Intent(this,Edit_activity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NotesList",(Serializable) notesArrayList);
                intent.putExtra("NotesList",bundle);
                startActivity(intent);
                return true;
            case R.id.menu_item_about:
                Intent info = new Intent(this,About_Activity.class);
                startActivity(info);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public ArrayList<Notes> jsonNotesLoad()
    {
        ArrayList<Notes> jsonNoteslist = new ArrayList<>();
        try
        {
            InputStream iS = getApplicationContext().openFileInput("MyAppNotes.json");
            BufferedReader bR = new BufferedReader(new InputStreamReader(iS, StandardCharsets.UTF_8));
            StringBuffer sB = new StringBuffer();
            String line;
            while((line = bR.readLine())!= null)
            {
                sB.append(line);
            }

            Log.d("jsonNotesLoad",sB.toString());

            JSONArray jsonArray = new JSONArray(sB.toString());
            Log.d("jsonNotesLoad - jsArr",jsonArray.toString());
            for(int i=0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("noteTitle");
                String detail = jsonObject.getString("noteDetail");
                String date = jsonObject.getString("noteDate");
                Notes notes = new Notes(title,detail,date);
                jsonNoteslist.add(notes);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d("retrive",jsonNoteslist.toString());
        Collections.sort(jsonNoteslist);
        return jsonNoteslist;
    }

    private void jsonNotesSave()
    {
        try
        {
            FileOutputStream fOS = getApplicationContext().openFileOutput("MyAppNotes.json", Context.MODE_PRIVATE);
            PrintWriter pW = new PrintWriter(fOS);
            pW.print(notesArrayList);
            pW.close();
            fOS.close();
        }catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    @Override
    public void onClick(View view)
    {
        int position = recyclerNotesView.getChildAdapterPosition(view);
        Notes notes = notesArrayList.get(position);
        Intent intent = new Intent(this,Edit_activity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("noteTitle",notes.getNotes_title());
        intent.putExtra("noteDetail",notes.getNotes_text());
        bundle.putSerializable("NotesList",(Serializable) notesArrayList);
        intent.putExtra("NotesList",bundle);
        intent.putExtra("notePosition",position);
        startActivity(intent);

    }

    @Override
    public boolean onLongClick(View view)
    {
        int position = recyclerNotesView.getChildAdapterPosition(view);
        new AlertDialog.Builder(this)
                .setTitle("Do you want to Delete Note '"+notesArrayList.get(position).getNotes_title()+"'?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notesArrayList.remove(position);
                                jsonNotesSave();
                                setTitle("Android Notes ("+notesArrayList.size()+")");
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }
                )
                .setNegativeButton("No",null)
                .show();
        return true;
    }
}