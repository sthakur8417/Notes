package com.example.sudo_android_notes.model_holder_adapter;

import android.util.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notes implements Serializable,Comparable<Notes> {

    String noteTitle;
    String noteDetail;
    String noteDate;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd, hh:mm aa ");

    public Notes(String noteTitle, String noteDetail, String noteDate) {
        this.noteTitle = noteTitle;
        this.noteDetail = noteDetail;
        this.noteDate = noteDate;
    }

    public String getNotes_title() {
        return noteTitle;
    }

    public void setNotes_title(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNotes_text() {
        return noteDetail;
    }

    public void setNotes_text(String noteDetail) {
        this.noteDetail = noteDetail;
    }

    public String getNotes_date() {
        return noteDate;
    }

    public void setNotes_date(String noteDate) {
        this.noteDate = noteDate;
    }

    @Override
    public String toString() {

        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("noteTitle").value(getNotes_title());
            jsonWriter.name("noteDetail").value(getNotes_text());
            jsonWriter.name("noteDate").value(getNotes_date());
            jsonWriter.endObject();
            jsonWriter.close();
            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int compareTo(Notes notes) {
        Date firstArg = null,secondArg = null;
        try {
            firstArg = simpleDateFormat.parse(notes.getNotes_date());
            secondArg = simpleDateFormat.parse(getNotes_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return firstArg.compareTo(secondArg);
    }
}
