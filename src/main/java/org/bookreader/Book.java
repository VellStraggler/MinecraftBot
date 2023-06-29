package org.bookreader;

public class Book {
    private String[] pages;
    private String author;
    private String title;

    public Book() {}

    public void setPages(String[] pages) {
        this.pages = pages;
    }
    public String[] getPages() {
        return pages;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor(String author) {
        return author;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
