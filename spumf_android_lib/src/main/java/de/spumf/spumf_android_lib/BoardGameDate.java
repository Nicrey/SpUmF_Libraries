package de.spumf.spumf_android_lib;


import android.content.Intent;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

/*
    Modelliert einen Termin
 */
public class BoardGameDate {
    public static final String DATE_FIELD_ID = "ID";
    public static final String DATE_FIELD_TITLE = "Title";
    public static final String DATE_FIELD_DESCRIPTION = "Description";
    public static final String DATE_FIELD_ADDRESS = "Address";
    public static final String DATE_FIELD_DATE = "Date";
    public static final String DATE_FIELD_CATEGORIES = "Categories";

    private String id = "";
    private Date date;
    private String title = "";
    private String description ="";
    private String address ="";
    private HashSet<String> categories;

    public BoardGameDate(String id, Date date, String title, String description,String address, HashSet<String> categories) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.categories = categories;
        this.address = address;
    }

    /**
     * Konstruktor für die Erstellung eines BoardGameDates aus einem Datenbankeintrag
     * @param databaseEntry Der zugehörige Datenbankeintrag
     */
    public BoardGameDate(Map<String, Object> databaseEntry){
        this.categories = new HashSet<>();
        for(Map.Entry<String, Object> entry : databaseEntry.entrySet()){
            switch(entry.getKey()){
                case DATE_FIELD_ID:
                    this.id = (String) entry.getValue();
                    break;
                case DATE_FIELD_TITLE:
                    this.title = (String) entry.getValue();
                    break;
                case DATE_FIELD_DESCRIPTION:
                    this.description = (String) entry.getValue();
                    break;
                case DATE_FIELD_DATE:
                    this.date = ((Timestamp) entry.getValue()).toDate();
                    break;
                case DATE_FIELD_CATEGORIES:
                    this.categories.addAll((ArrayList<String>) entry.getValue());
                    break;
                case DATE_FIELD_ADDRESS:
                    this.address = (String) entry.getValue();
                default:
                    break;
            }
        }
    }


    /**
     * Constructs a BoardGameDate using a given Intent with the Extras of this Intent
     * @param i intent
     */
    public BoardGameDate(Intent i) {
        this.categories = new HashSet<>();
        this.id = i.getStringExtra(DATE_FIELD_ID);
        this.title = i.getStringExtra(DATE_FIELD_TITLE);
        this.description = i.getStringExtra(DATE_FIELD_DESCRIPTION);
        this.address = i.getStringExtra(DATE_FIELD_ADDRESS);
        this.date = new Date(i.getLongExtra(DATE_FIELD_DATE,0));
        this.categories.addAll(i.getStringArrayListExtra(DATE_FIELD_CATEGORIES));

    }


    /**
     * More Formal String Output (Not Beautiful but readable)
     * For a more beautiful version use toSummaryString()
     * @return the string
     */
    @Override
    public String toString(){
        return "{ID=" + this.id
                + ", Title=" + this.title
                + ", Description="+ this.description
                + ", Date=" + this.date.toString()
                + ", Categories=" + this.categories.toString()
                + ", Address=" + this.address
                + "}";
    }

    /**
     * Transforms the BoardGameDate to a good looking String
     * @return the String
     */
    public String toSummaryString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd.MM.yyyy, HH:mm", Locale.GERMANY);
        return title + "\n\n"
                + "Wo? \n" + address + "\n\n"
                + "Wann? \n" + dateFormat.format(date) + "\n\n"
                + "Was? \n" + description + "\n\n"
                + "Tags: \n" + categories.toString() + "\n\n";
    }
    /**
     * Enhances a given Intent with the data of this class
     * @param i
     * @return the enhanced intent
     */
    public Intent putExtras(Intent i) {
        i.putExtra(DATE_FIELD_ID, id);
        i.putExtra(DATE_FIELD_TITLE, title);
        i.putExtra(DATE_FIELD_DESCRIPTION, description);
        i.putExtra(DATE_FIELD_ADDRESS, address);
        i.putExtra(DATE_FIELD_DATE, date.getTime());
        i.putStringArrayListExtra(DATE_FIELD_CATEGORIES,new ArrayList<>(categories));
        return i;
    }

    /**
     * Transforms this date into a database representation of this date
     * Basically reverse function to the constructor
     * @return a Map of Fields to their values => Databaseentry for Firestore
     */
    public Map<String, Object> toData() {
        Map<String,Object> map = new HashMap<>();
        map.put(DATE_FIELD_ID, id);
        map.put(DATE_FIELD_TITLE, title);
        map.put(DATE_FIELD_DESCRIPTION,description);
        map.put(DATE_FIELD_DATE, date);
        map.put(DATE_FIELD_ADDRESS, address);
        map.put(DATE_FIELD_CATEGORIES,new ArrayList<>(categories));
        return map;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
