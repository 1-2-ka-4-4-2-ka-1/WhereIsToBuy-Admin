package com.findviewbyid.tiwari.radmin;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsersStorageClass {

    private final String STORAGE = "com.findViewById.tiwari.myapplication.STORAGE";
    private SharedPreferences preferences;
    private Context context ;

    public UsersStorageClass(Context context) {
        this.context = context;
    }

    public void storeUsers(List<UsersModel> arrayList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("usersArrayList", json);
        editor.apply();
    }


    public void  addNewUser(UsersModel detailsModel){

        UsersStorageClass storage = new UsersStorageClass(DashBoardActivity.activity_dashBoard);
        List<UsersModel> list =  storage.loadUsers();
        if(list!=null)
            list.add(detailsModel);

        else {
            list = new ArrayList<>();
            list.add(detailsModel);
        }
        storage.storeUsers(list);
    }


    public ArrayList<UsersModel> loadUsers() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("usersArrayList", null);
        Type type = new TypeToken<ArrayList<UsersModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }



    public void clearCachedItems() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
