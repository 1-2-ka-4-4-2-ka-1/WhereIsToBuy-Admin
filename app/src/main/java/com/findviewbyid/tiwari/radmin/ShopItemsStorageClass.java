package com.findviewbyid.tiwari.radmin;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ShopItemsStorageClass {


    private final String STORAGE = "com.findViewById.tiwari.myapplication.STORAGE";
    private SharedPreferences preferences;
    private Context context ;

    public ShopItemsStorageClass(Context context) {
        this.context = context;
    }

    public void storeItems(List<ShopItemModel> arrayList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("itemsArrayList", json);
        editor.apply();
    }


    public void  addNewShop(ShopItemModel detailsModel){

        ShopItemsStorageClass storage = new ShopItemsStorageClass(DashBoardActivity.activity_dashBoard);
        List<ShopItemModel> list =  storage.loadItems();
        if(list!=null)
            list.add(detailsModel);

        else {
            list = new ArrayList<>();
            list.add(detailsModel);
        }
        storage.storeItems(list);
    }


    public ArrayList<ShopItemModel> loadItems() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("itemsArrayList", null);
        Type type = new TypeToken<ArrayList<ShopItemModel>>() {
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
