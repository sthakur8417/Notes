package com.example.sudo_android_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sudo_android_notes.model_holder_adapter.Notes;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Edit_activity extends AppCompatActivity {

    private EditText title_notes, detail_notes;
    private ArrayList<Notes> notesArrayList;
    int x = -1;
    String tempDetail,tempTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setTitle("Android Notes");

        title_notes=findViewById(R.id.give_title_notes);
        detail_notes=findViewById(R.id.give_detail_notes);
        x = -1;

        Bundle bundle = getIntent().getBundleExtra("NotesList");
        notesArrayList= (ArrayList<Notes>) bundle.getSerializable("NotesList");

        Bundle bundleTemp = getIntent().getExtras();
        tempTitle = bundleTemp.getString("noteTitle");
        if(tempTitle != null) {
            tempDetail = bundleTemp.getString("noteDetail");
            x = bundleTemp.getInt("notePosition");
            title_notes.setText(tempTitle);
            detail_notes.setText(tempDetail);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_save:
                Intent intent=new Intent(this,MainActivity.class);
                if(!jsonNotesAdd())
                {
                    Toast.makeText(this,"Enter title",Toast.LENGTH_SHORT).show();
                    alertDialogeBox();
                    return true;
                }

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void jsonNotesSave()
    {
        try {
            FileOutputStream fOS = getApplicationContext().openFileOutput("MyAppNotes.json", Context.MODE_PRIVATE);
            PrintWriter pW = new PrintWriter(fOS);
            pW.print(notesArrayList);
            Log.d("jsonNotesSave", pW.toString()+notesArrayList.toString());
            pW.close();
            fOS.close();
        }catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    private boolean jsonNotesAdd()
    {
        if(!title_notes.getText().toString().isEmpty())
        {
            String strDate = new SimpleDateFormat("E MMM dd, hh:mm aa ").format(new Date());
            Notes notesInfo = new Notes(title_notes.getText().toString(),detail_notes.getText().toString(),strDate);
            if(x>=0)
            {
                if (!tempTitle.equals(title_notes.getText().toString()) || !tempDetail.equals(detail_notes.getText().toString())) {
                    notesArrayList.remove(x);
                }else
                {
                    return true;
                }
            }
            notesArrayList.add(notesInfo);
            Log.d("jsonNotesAdd",notesInfo.getNotes_text()+notesArrayList.toString());
            jsonNotesSave();
            return true;
        }
        return false;
    }

    public void alertDialogeBox()
    {
        Intent intent = new Intent(this,MainActivity.class);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Title field is necessary")
                .setMessage("Note will not save without Title.\n"+"Continue?\n")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                startActivity(intent);
                            }
                        }
                )
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,MainActivity.class);
        String temp = title_notes.getText().toString();
        if(x>-1 && tempTitle.equals(title_notes.getText().toString()) && tempDetail.equals(detail_notes.getText().toString()))
        {
            jsonNotesAdd();
            startActivity(intent);
        }else if(temp.isEmpty() && detail_notes.getText().toString().isEmpty())
        {
            startActivity(intent);
        }else
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Do you want to save note '"+temp+"'?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            if(temp.isEmpty())
                            {
                                Toast.makeText(getApplicationContext(),"Title field is necessary",Toast.LENGTH_LONG).show();
                            }else
                            {
                                jsonNotesAdd();
                                startActivity(intent);
                            }
                        }

                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    startActivity(intent);
                                }
                            }
                    )
                    .show();
        }
    }
}