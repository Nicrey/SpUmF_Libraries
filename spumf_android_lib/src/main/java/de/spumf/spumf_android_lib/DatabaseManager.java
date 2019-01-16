package de.spumf.spumf_android_lib;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class DatabaseManager {

    public static final String DATABASE_DATES = "BoardGameDates";
    public static final String DATABASE_CATEGORIES = "Categories";
    public static final String DATABASE_OLD_DATES = "OldBoardGameDates";

    private static DatabaseManager instance;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private DatabaseManager(){
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        auth = FirebaseAuth.getInstance();
    }

    public boolean signedIn(){
        FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser != null;
    }


    public static DatabaseManager getInstance(){
        if(DatabaseManager.instance == null){
            DatabaseManager.instance = new DatabaseManager();
        }
        return DatabaseManager.instance;
    }

    public void publishDate(final Context context, String title, String description, Date date, String lt, String ls, ArrayList<String> categories, String summary){
        Map<String, Object> data = new HashMap<>();
        data.put("Title", title);
        data.put("Date", date);
        data.put("Categories", categories);
        data.put("Address", lt + " " + ls);
        data.put("Description", description);
        db.collection(DATABASE_DATES).add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Erfolgreich hochgeladen.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Fehler beim Hochladen.", Toast.LENGTH_LONG).show();
                        Log.w("DatabaseManager", "Error uploading", e);
                    }
                });
    }

    public void getDates(final RecallActivity recall){
        db.collection(DATABASE_DATES).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                ArrayList<Map<String, Object>> dates = new ArrayList<>();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        dates.add(document.getData());
                        Log.d("DatabaseManager", "yy - Found Date: " + document.getData().get("Title"));
                    }
                }else{
                    Log.w("DatabaseManager", "yy - Error getting Dates", task.getException());
                }
                recall.update(dates, DATABASE_DATES);
            }
        });
    }

    public void getCategories(final RecallActivity recall){
        db.collection(DATABASE_CATEGORIES).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Object> categories = new ArrayList<>();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()){
                        categories.add(document.getData().get("Name"));
                        Log.d("DatabaseManager", "yy - Found Category: " + document.getData().get("Name"));
                    }
                }else{
                    Log.w("DatabaseManager", "yy - Error getting Categories", task.getException());
                }
                recall.update(categories, DATABASE_CATEGORIES);
            }
        });
    }

    public void publishCategory(String text, final RecallActivity recall) {
        Map<String, Object> data = new HashMap<>();
        data.put("Name",text);
        db.collection(DATABASE_CATEGORIES).add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(recall.getApplicationContext(), "Erfolgreich erstellt.", Toast.LENGTH_SHORT).show();
                getCategories(recall);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(recall.getApplicationContext(), "Fehler beim Erstellen.", Toast.LENGTH_LONG).show();
                Log.w("DatabaseManager", "yy - Error uploading Category");
            }
        });
    }

    public void deleteCategory(String category, final RecallActivity recall) {
        Query q = db.collection(DATABASE_CATEGORIES).whereEqualTo("Name", category);
        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot s : task.getResult()){
                        s.getReference().delete();
                    }
                }
                getCategories(recall);
            }
        });
    }

    public void changeCategory(String category, String s, RecallActivity recall) {
        deleteCategory(category, recall);
        publishCategory(s, recall);
    }
}
