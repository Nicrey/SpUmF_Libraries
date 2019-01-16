package de.spumf.spumf_lib;



import com.google.cloud.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

/*
    Modelliert einen Termin
 */
public class BoardGameDate {
    private String id;
    private Date date;
    private String title;
    private String description;
    private HashSet<String> categories;

    public BoardGameDate(String id, Date date, String title, String description, HashSet<String> categories) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.categories = categories;
    }

    /**
     * Konstruktor für die Erstellung eines BoardGameDates aus einem Datenbankeintrag
     * @param databaseEntry Der zugehörige Datenbankeintrag
     */
    public BoardGameDate(Map<String, Object> databaseEntry){
        this.categories = new HashSet<>();
        for(Map.Entry<String, Object> entry : databaseEntry.entrySet()){
            switch(entry.getKey()){
                case "ID":
                    this.id = (String) entry.getValue();
                    break;
                case "Title":
                    this.title = (String) entry.getValue();
                    break;
                case "Description":
                    this.description = (String) entry.getValue();
                    break;
                case "Date":
                    this.date = ((Timestamp) entry.getValue()).toDate();
                    break;
                case "Categories":
                    this.categories.addAll((ArrayList<String>) entry.getValue());
                default:
                    break;
            }
        }
    }

    public String toString(){
        return "{ID=" + this.id
                + ", Title=" + this.title
                + ", Description="+ this.description
                + ", Date=" + this.date.toString()
                + ", Categories=" + this.categories.toString()
                + "}";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashSet<String> getCategories() {
        return categories;
    }

    public void setCategories(HashSet<String> categories) {
        this.categories = categories;
    }
}
