package com.example.atv.note;

import java.io.Serializable;

/**
 * Created by luatnguyen88 on 8/11/2016.
 */
public class Note implements Serializable {
    private int noteId;
    private String noteTitle;
    private String noteContent;
    private String noteAlarmTime;
    private String noteCreatTime;
    private String noteColor;
    public Note(int noteId,String noteTitle,String noteContent,String noteColor,String noteAlarmTime,String noteCreatTime){
//        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteColor = noteColor;
        this.noteAlarmTime = noteAlarmTime;
        this.noteCreatTime = noteCreatTime;
        this.noteId = noteId;
    }
    public Note(){}
    public Note(String noteTitle,String noteContent,String noteColor,String noteCreatTime){
//        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteCreatTime = noteCreatTime;
        this.noteColor = noteColor;
    }
    public int getNoteId(){
        return this.noteId;
    }
    public String getNoteTitle(){
        return this.noteTitle;
    }
    public String getNoteContent(){
        return this.noteContent;
    }
    public String getNoteAlarmTime(){
        return  this.noteAlarmTime;
    }
    public String getNoteCreatTime(){
        return  this.noteCreatTime;
    }
    public String getNoteColor(){
        return  this.noteColor;
    }
    public void setNoteId(int noteId){
        this.noteId = noteId;
    }
    public void setNoteTitle(String noteTitle){
        this.noteTitle = noteTitle;
    }
    public void setNoteContent(String noteContent){
        this.noteContent = noteContent;
    }
    public void setNoteAlarmTime(String noteAlarmTime){
        this.noteAlarmTime = noteAlarmTime;
    }
    public void setNoteCreatTime(String noteCreatTime){
        this.noteCreatTime = noteCreatTime;
    }
    public void setNoteColor(String noteColor){
        this.noteColor = noteColor;
    }
}
