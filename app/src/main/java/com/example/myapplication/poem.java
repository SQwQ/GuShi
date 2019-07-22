package com.example.myapplication;

public class poem {
    private String id;
    private String author;
    private String content;

    public poem(String ID, String Author, String Content) {
        this.id = ID;
        this.author = Author;
        this.content = Content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return id;
    }

}
