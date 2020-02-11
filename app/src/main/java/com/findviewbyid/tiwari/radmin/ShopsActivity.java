package com.findviewbyid.tiwari.radmin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShopsActivity extends AppCompatActivity implements ShopsEditDialogue.DialogueListener{


    private FirebaseDatabase databaseFirebase;

    private RecyclerView mShopsListRecyclerView;
    private ShopsRecyclerViewAdapter mShopsRecyclerViewAdapter;
    public RecyclerView.LayoutManager mShopsListlayoutManager;
    public ArrayList<ShopDetailsModel> mShopsList;

    private FloatingActionButton mSyncShops;
    private FloatingActionButton mExportDataButton;

    public static DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        databaseFirebase = FirebaseDatabase.getInstance();

        mShopsList = new ArrayList<>();

        mShopsListRecyclerView = findViewById(R.id.rv_shopsLise_recyclerView);
        mShopsListlayoutManager = new LinearLayoutManager(this);
        mShopsRecyclerViewAdapter =new ShopsRecyclerViewAdapter(mShopsList);
        mShopsListRecyclerView.setLayoutManager(mShopsListlayoutManager);
        mShopsListRecyclerView.setAdapter(mShopsRecyclerViewAdapter);

        mShopsRecyclerViewAdapter.setOnShopClickedListener(new ShopsRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onShopClicked(int position) {


                doEditShop(position);


            }
        });


        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();


        mSyncShops= findViewById(R.id.fb_add_shopItem);
        mSyncShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSyncShops();
            }
        });
 
        mExportDataButton = findViewById(R.id.fb_export_shop_details);
        mExportDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doExportData(ShopsActivity.this,"shops_data.xls");
            }
        });
        
        
        doGetShops();

    }



    private  boolean doExportData(Context context, String fileName) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.i("", "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row

        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("contacts");

        // Generate column headings

        Row row = sheet1.createRow(0);
        row.setHeight((short)500);

        c = row.createCell(0);
        c.setCellValue("Contact No");
        c.setCellStyle(cs);

//        c = row.createCell(0);
//        c.setCellValue("Shop Name");
//        c.setCellStyle(cs);
//
//        c = row.createCell(1);
//        c.setCellValue("Address");
//        c.setCellStyle(cs);
//
//        c = row.createCell(2);
//        c.setCellValue("Area ");
//        c.setCellStyle(cs);
//
//        c = row.createCell(3);
//        c.setCellValue("Location");
//        c.setCellStyle(cs);
//
//        c = row.createCell(4);
//        c.setCellValue("Sub Location");
//        c.setCellStyle(cs);
//
//        c = row.createCell(5);
//        c.setCellValue("");
//        c.setCellStyle(cs);
//
//        c = row.createCell(6);
//        c.setCellValue("Landmark");
//        c.setCellStyle(cs);
//
//        c = row.createCell(7);
//        c.setCellValue("Contact No");
//        c.setCellStyle(cs);
//
//        c = row.createCell(8);
//        c.setCellValue("Rating");
//        c.setCellStyle(cs);

        CellStyle cse = wb.createCellStyle();
        cse.setFillForegroundColor(HSSFColor.AQUA.index);



        CellStyle cs2;
        CellStyle cs3;
        cs3 = wb.createCellStyle();
        cs3.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        cs3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        ShopsStorageClass storage = new ShopsStorageClass(getApplicationContext());

        double amount= 0;

        for(int i=0;i<mShopsList.size();i++) {


            if(i%2==0){
                cs2 = wb.createCellStyle();
                cs2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                cs2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            }else {
                cs2 = wb.createCellStyle();
                cs2.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
                cs2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            }

            Row row2 = sheet1.createRow(1+i);
            row2.setHeight((short)400);



            c = row2.createCell(0);
            c.setCellValue(mShopsList.get(i).getmContactno());
            c.setCellStyle(cs2);

//            c = row2.createCell(0);
//            c.setCellValue(String.valueOf(i+1));
//            c.setCellStyle(cs2);

//            c = row2.createCell(0);
//            c.setCellValue(String.valueOf(mShopsList.get(i).getmShopName()));
//            c.setCellStyle(cs2);
//
//
//            c = row2.createCell(1);
//            c.setCellValue(mShopsList.get(i).getmAddress());
//            c.setCellStyle(cs2);
//
//
//            c = row2.createCell(2);
//            c.setCellValue(mShopsList.get(i).getmArea());
//            c.setCellStyle(cs2);
//
//
//            c = row2.createCell(3);
//            c.setCellValue(String.valueOf(mShopsList.get(i).getmLocation()));
//            c.setCellStyle(cs2);
//
//            c = row2.createCell(4);
//            c.setCellValue(String.valueOf(mShopsList.get(i).getmSublocation()));
//            c.setCellStyle(cs2);
//
//
//            c = row2.createCell(5);
//            c.setCellStyle(cs3);
//
//            c = row2.createCell(6);
//            c.setCellValue(mShopsList.get(i).getmLandmark());
//            c.setCellStyle(cs2);
//
//            c = row2.createCell(7);
//            c.setCellValue(mShopsList.get(i).getmContactno());
//            c.setCellStyle(cs2);
//
//            c = row2.createCell(8);
//            c.setCellValue(mShopsList.get(i).getmRating());
//            c.setCellStyle(cs2);



        }

        sheet1.setColumnWidth(0, (15 * 400));
//        sheet1.setColumnWidth(1, (15 * 200));
//        sheet1.setColumnWidth(2, (15 * 500));
//
//        sheet1.setColumnWidth(3, (15 * 200));
//        sheet1.setColumnWidth(4, (15 * 200));
//        sheet1.setColumnWidth(5, (15 * 100));
//        sheet1.setColumnWidth(6, (15 * 500));
//        sheet1.setColumnWidth(7, (15 * 500));
//        sheet1.setColumnWidth(8, (15 * 250));
//        sheet1.setColumnWidth(9, (15 * 200));


        // Create a path where we will place our List of objects on external storage

        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        if(success)
            doPreviewReport(fileName);
        return success;
    }


    public void doPreviewReport(String fileName){

        File file = new File(ShopsActivity.this.getExternalFilesDir(null), fileName);



        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = GenericFileProvider.getUriForFile(ShopsActivity.this,getApplicationContext().getPackageName()+".provider",file);
        intent.setDataAndType(uri,"application/xls");
        intent.putExtra(intent.EXTRA_STREAM,uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
    
    public void doEditShop(int position){

        final ShopsEditDialogue shopsEditDialogue = new ShopsEditDialogue(mShopsList.get(position).getmShopName(),mShopsList.get(position).getmAliasName(),mShopsList.get(position).getmAddress(),mShopsList.get(position).getmArea(),mShopsList.get(position).getmLocation(),mShopsList.get(position).getmSublocation(),mShopsList.get(position).getmLandmark(),mShopsList.get(position).getmContactno(),mShopsList.get(position).getmGroup(),mShopsList.get(position).getmRating(),mShopsList.get(position).getmShopId(),position);
        shopsEditDialogue.show(getSupportFragmentManager(),"Edit Shop");

    }


    public void doGetShops(){

        ShopsStorageClass storage = new ShopsStorageClass(DashBoardActivity.activity_dashBoard);


        if(storage.loadShops()!=null){
            if(storage.loadShops().size()!=0){
                mShopsList.addAll(storage.loadShops());
            }
        }

    }

    public void doSyncShops(){

        mShopsList.clear();
        final ShopsStorageClass storage = new ShopsStorageClass(DashBoardActivity.activity_dashBoard);
        storage.clearCachedShops();


        DatabaseReference mDataRef = databaseFirebase.getReference("data");
        mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    DatabaseReference mShopsRef = snapshot.child("shops").getRef();
                    mShopsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot1 : dataSnapshot.getChildren()) {


                                Log.i("----------", snapshot1.getValue().toString());
                                Log.i("----------", snapshot1.child("mContactno").getValue().toString());

                                ShopDetailsModel shopDetailsModel = new ShopDetailsModel(snapshot1.child("mShopName").getValue().toString()
                                        ,snapshot1.child("mAliasName").getValue().toString()
                                        ,snapshot1.child("mAddress").getValue().toString()
                                        ,snapshot1.child("mArea").getValue().toString()
                                        ,snapshot1.child("mLocation").getValue().toString()
                                        ,snapshot1.child("mSublocation").getValue().toString()
                                        ,snapshot1.child("mLandmark").getValue().toString()
                                        ,snapshot1.child("mContactno").getValue().toString()
                                        ,snapshot1.child("mGroup").getValue().toString()
                                        ,Integer.parseInt(snapshot1.child("mRating").getValue().toString())
                                        ,snapshot1.child("mShopId").getValue().toString());

                                storage.addNewShop(shopDetailsModel);
                                mShopsList.add(shopDetailsModel);
                                dataBaseHelper.insertShop(shopDetailsModel.getmShopName(),shopDetailsModel.getmAliasName(),shopDetailsModel.getmAddress(),shopDetailsModel.getmArea(),shopDetailsModel.getmLocation(),shopDetailsModel.getmSublocation(),shopDetailsModel.getmLandmark(),shopDetailsModel.getmContactno(),String.valueOf(shopDetailsModel.getmRating()),shopDetailsModel.getmGroup(),shopDetailsModel.getmShopId(),database);
                                mShopsRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void getModifiedShopsData(String ShopName, String AliasName, String Address, String Area, String Location, String Sublocation, String Landmark, String Contactno, String Group, int Rating,String ShopId, int pos) {
        ShopDetailsModel shopModel= new ShopDetailsModel(ShopName,AliasName,Address,Area,Location,Sublocation,Landmark,Contactno,Group,Rating,ShopId);
        mShopsList.set(pos,shopModel);
        mShopsRecyclerViewAdapter.notifyItemChanged(pos);
        ShopsStorageClass storage = new ShopsStorageClass(getApplicationContext());
        storage.shopDetails(mShopsList);

    }
}
