package com.findviewbyid.tiwari.radmin;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ShopsStorageClass {

    private final String STORAGE = " com.findViewById.tiwari.myapplication.STORAGE";
    private SharedPreferences preferences;
    private Context context ;

    public ShopsStorageClass(Context context) {
        this.context = context;
    }

    public void shopDetails(List<ShopDetailsModel> arrayList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("shopsArrayList", json);
        editor.apply();
    }

    public void  addNewShop(ShopDetailsModel detailsModel){

        ShopsStorageClass storage = new ShopsStorageClass(DashBoardActivity.activity_dashBoard);
        List<ShopDetailsModel> list =  storage.loadShops();
        if(list!=null)
        list.add(detailsModel);

        else {
            list = new ArrayList<>();
            list.add(detailsModel);
        }
        storage.shopDetails(list);

    }

    public ArrayList<ShopDetailsModel> loadShops() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("shopsArrayList", null);
        Type type = new TypeToken<ArrayList<ShopDetailsModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public ShopDetailsModel getShopByDesc(String desc){

        ArrayList<ShopDetailsModel> s = new ArrayList<>();
        s.addAll(loadShops());


        if(s.size()==0)
        {
            return null;
        }


        for(int i=0;i<s.size();i++){

            if(s.get(i).getmShopName().equals(desc)){
                ShopDetailsModel shopDetailsModel = s.get(i);
                return shopDetailsModel;
            }
        }

        return null;
    }


    public void clearCachedShops() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
