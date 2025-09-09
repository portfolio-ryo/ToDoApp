package com.example.todoapp;

public class Task {
    private final int id;
    private final String title;
    private boolean completed;

    public Task(int id, String title, boolean completed){
        this.id=id;
        this.title=title;
        this.completed=completed;
    }
    public int getId(){
        return  id;
    }
    public String getTitle(){
        return title;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
