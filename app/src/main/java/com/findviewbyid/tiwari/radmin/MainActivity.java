package com.findviewbyid.tiwari.radmin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements   DatePickerDialog.OnDateSetListener{

    private FirebaseDatabase databaseFirebase;


    private ArrayList<BillItem> mAllBillsList;

    private RecyclerView mBillsListRecyclerView;
    private BillItemsRecyclerViewAdapter mBillsRecyclerViewAdapter;
    public RecyclerView.LayoutManager mBillsListlayoutManager;
    public ArrayList<BillItem> mBillItems;




    private BottomSheetBehavior mBottomSheetBehavior;
    private View mFilterBillsLayout;
    private FloatingActionButton mFloatingActionButtonBringUp;

    public static DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;





    private Spinner mFilterItem;
    private TextView mFilterOnDate;
    private TextView mFilterFromDate;
    private TextView mFilterTillDate;
    private Spinner mFilterInMonth;
    private Spinner mFilterShop;
    private Spinner mFilterArea;
    private Spinner mFilterGroup;

    private TextView mBillsCount;


    private ArrayAdapter<String> mFilterItemAdapter;
    private ArrayAdapter<String> mFilterInMonthAdapter;
    private ArrayAdapter<String> mFilterShopAdapter;
    private ArrayAdapter<String> mFilterAreaAdapter;
    private ArrayAdapter<String> mFilterGroupAdapter;


    private List<String> mFilterItemList;
    private List<String> mFilterInMonthList;
    private List<String> mFilterShopList;
    private List<String> mFilterAreaList;
    private List<String> mFilterGroupList;


    public static int dateType;

    public ImageView mClearFilters;

    private FloatingActionButton mExportDataButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseFirebase = FirebaseDatabase.getInstance();
        mAllBillsList = new ArrayList<>();


        mBillItems = new ArrayList<>();
        mBillsListRecyclerView = findViewById(R.id.rv_bill_tem_recyclerView);
        mBillsListlayoutManager = new LinearLayoutManager(this);
        mBillsRecyclerViewAdapter = new BillItemsRecyclerViewAdapter(mBillItems);
        mBillsListRecyclerView.setLayoutManager(mBillsListlayoutManager);
        mBillsListRecyclerView.setAdapter(mBillsRecyclerViewAdapter);


        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();

        mFilterBillsLayout = findViewById(R.id.filter_bills_layout);
        mFilterBillsLayout.setVisibility(View.VISIBLE);

        mFilterItem = mFilterBillsLayout.findViewById(R.id.tv_filter_item);
        mFilterOnDate = mFilterBillsLayout.findViewById(R.id.tv_filter_on_date);
        mFilterFromDate = mFilterBillsLayout.findViewById(R.id.tv_filter_from_date);
        mFilterTillDate = mFilterBillsLayout.findViewById(R.id.tv_filter_till_date);
        mFilterInMonth = mFilterBillsLayout.findViewById(R.id.tv_filter_in_month);
        mFilterShop = mFilterBillsLayout.findViewById(R.id.tv_filter_shop);
        mFilterArea = mFilterBillsLayout.findViewById(R.id.tv_filter_area);
        mFilterGroup = mFilterBillsLayout.findViewById(R.id.tv_filter_group);

        mBillsCount = findViewById(R.id.tv_bills_count);

        mClearFilters=mFilterBillsLayout.findViewById(R.id.iv_clear_filters);


        mFilterItemList = new ArrayList<>();
//        mFilterOnDateList = new ArrayList<>();
//        mFilterFromDateList = new ArrayList<>();
//        mFilterTillDateList = new ArrayList<>();
        mFilterInMonthList = new ArrayList<>();
        mFilterShopList = new ArrayList<>();
        mFilterAreaList = new ArrayList<>();
        mFilterGroupList = new ArrayList<>();


        mFilterOnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterOnDate.setAlpha(1);
                mFilterFromDate.setAlpha(0.2f);
                mFilterTillDate.setAlpha(0.2f);
                mFilterInMonth.setAlpha(0.2f);
                pickOnDate();
            }
        });
        mFilterFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFilterOnDate.setAlpha(0.2f);
                mFilterFromDate.setAlpha(1);
                mFilterTillDate.setAlpha(1);
                mFilterInMonth.setAlpha(0.2f);
               pickFromDate();
            }
        });
        mFilterTillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterOnDate.setAlpha(0.2f);
                mFilterFromDate.setAlpha(1);
                mFilterTillDate.setAlpha(1);
                mFilterInMonth.setAlpha(0.2f);
                pickTillDate();
            }
        });

        mClearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFilters();
            }
        });



        mFilterItemAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mFilterItemList);
        mFilterItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterItem.setAdapter(mFilterItemAdapter);
        mFilterItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilterItem.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFilterItem.setSelection(0);
            }
        });

        mExportDataButton = findViewById(R.id.fb_export_data_excel);
        mExportDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doExportData(MainActivity.this , System.currentTimeMillis()+".xls");
            }
        });






//        mFilterOnDateAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mSpinnerList);
//        mFilterOnDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mFilterOnDate.setAdapter(mFilterOnDateAdapter);
//        mFilterOnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mFilterOnDate.setSelection(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                mFilterOnDate.setSelection(0);
//            }
//        });
//
//
//        mFilterFromDateAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mSpinnerList);
//        mFilterFromDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mFilterFromDate.setAdapter(mFilterFromDateAdapter);
//        mFilterFromDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mFilterFromDate.setSelection(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                mFilterFromDate.setSelection(0);
//            }
//        });
//
//
//        mFilterTillDateAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mSpinnerList);
//        mFilterTillDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mFilterTillDate.setAdapter(mFilterTillDateAdapter);
//        mFilterTillDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mFilterTillDate.setSelection(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                mFilterTillDate.setSelection(0);
//            }
//        });




        mFilterInMonthAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mFilterInMonthList);
        mFilterInMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterInMonth.setAdapter(mFilterInMonthAdapter);
        mFilterInMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilterOnDate.setAlpha(0.2f);
                mFilterFromDate.setAlpha(0.2f);
                mFilterTillDate.setAlpha(0.2f);
                mFilterInMonth.setAlpha(1);
                mFilterInMonth.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFilterInMonth.setSelection(0);
            }
        });



        mFilterShopAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mFilterShopList);
        mFilterShopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterShop.setAdapter(mFilterShopAdapter);
        mFilterShop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilterShop.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFilterShop.setSelection(0);
            }
        });


        mFilterAreaAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mFilterAreaList);
        mFilterAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterArea.setAdapter(mFilterAreaAdapter);
        mFilterArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilterArea.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFilterArea.setSelection(0);
            }
        });



        mFilterGroupAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mFilterGroupList);
        mFilterGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterGroup.setAdapter(mFilterGroupAdapter);
        mFilterGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilterGroup.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFilterGroup.setSelection(0);
            }
        });

        mFilterItem.setSelection(0);
        mFilterInMonth.setSelection(0);
        mFilterShop.setSelection(0);
        mFilterArea.setSelection(0);
        mFilterGroup.setSelection(0);

//        mFilterItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doGetItemFilterQuery();
//            }
//        });
//        mFilterOnDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doGetOnDateFilterQuery();
//            }
//        });
//        mFilterFromDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doGetFromDateFilterQuery();
//            }
//        });
//        mFilterTillDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doGetTillDateFilterQuery();
//            }
//        });
//        mFilterInMonth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doGetInMonthFilterQuery();
//            }
//        });
//        mFilterShop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doGetShopFilterQuery();
//            }
//        });
//        mFilterArea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doGetAreaFilterQuery();
//            }
//        });
//        mFilterGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doGetGroupFilterQuery();
//            }
//        });





        View bottomSheet = findViewById(R.id.filters_bottom_sheet);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(150);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){
                                                        @Override
                                                        public void onStateChanged(@NonNull View view, int i) {
                                                            switch (i) {
                                                                case BottomSheetBehavior.STATE_EXPANDED: {
                                                                    mFloatingActionButtonBringUp.setScaleY(-1);
                                                                    break;
                                                                }
                                                                case BottomSheetBehavior.STATE_COLLAPSED: {
                                                                    mFloatingActionButtonBringUp.setScaleY(1);
                                                                    doFilterList();

                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onSlide(@NonNull View view, float v) {
                                                            mFloatingActionButtonBringUp.setRotationX(v);
                                                        }
            });
        mFloatingActionButtonBringUp=findViewById(R.id.fb_muve_up_filters);
        mFloatingActionButtonBringUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        doSetupAdapters();




       // parseDateToddMMyyyy("17/12/2019");




////
//        LoadMappingData lodeItemsDataAsyncTask = new LoadMappingData();
//        lodeItemsDataAsyncTask.execute();

//        Cursor cursor = database.rawQuery("SELECT * FROM _mapping_tb ", new String[]{});
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//                     Log.i("song Name",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4));
//
//
//
//
//                } while (cursor.moveToNext());
//            }
//
//            cursor.close();
//
//        }
//
//        String query = "SELECT B._desc ,M._date FROM  _all_bills_tb B,_mapping_tb M WHERE Date(M._date) = date(?)";
//        Cursor cursor = database.rawQuery(query, new String[]{parseDateToddMMyyyy("17/12/19")});


//        String query = "SELECT * FROM  _all_bills_tb WHERE  _desc IS NOT NULL";
//        Cursor cursor = database.rawQuery(query, new String[]{});

//        String query = "SELECT * FROM  _all_bills_tb B,_mapping_tb M,_all_shops_tb S WHERE  _desc LIKE (?) AND B._id_label LIKE M._mbill_id AND M._shopname LIKE (?)";
//        Cursor cursor = database.rawQuery(query, new String[]{"%%","ytecj"});
//
//        String query = "SELECT B._bill_id ,B._amount  , B._desc , B._id_label  , B._qty , B._rate , B._unit ,S._shop_name ,M._date FROM   _all_bills_tb B,  _mapping_tb M ,_all_shops_tb S  WHERE   M._mbill_id = B._id_label AND S._shop_name = M._shopname GROUP  BY S._shop_name ";



        //Working

//        String query = "SELECT B._bill_id ,B._amount  , B._desc , B._id_label  , B._qty , B._rate , B._unit  , M._mbill_id , M._count , M._date , M._shopname , M._shop_id , S._shop_name ,S._alias  ,S._address , S._area  , S._location , S._sublocation  , S._landmark  , S._contact  , S._ratiing  ,S._group  FROM   _all_bills_tb B   INNER JOIN _mapping_tb M ON B._id_label = M._mbill_id INNER JOIN _all_shops_tb S ON S._shop_name = M._shopname ";
//        Cursor cursor = database.rawQuery(query, new String[]{});


       // String query = "SELECT *   FROM   _all_bills_tb  WHERE _shop_id = (SELECT  _shop_id FROM  _all_shops_tb WHERE _shop_name = (?) ) ";
        //String query = "SELECT *   FROM   _all_bills_tb ";
        String query = "SELECT *   FROM   _all_bills_tb WHERE Date(_date) = Date((?)) ";

        Cursor cursor = database.rawQuery(query, new String[]{parseDateToddMMyyyy("01/01/70")});


//
//-: 1--- 511---- 7 ---2020-01-02 ---asm--- 98
//    2--- 1---- 3 ---2020-01-02 ---new--- 1234
//
//        String query = "SELECT B._desc ,M._date FROM  _all_bills_tb B,_mapping_tb M WHERE Date(M._date) = date(?)";
//        Cursor cursor = database.rawQuery(query, new String[]{parseDateToddMMyyyy("17/12/19")});

//
//        String query = "SELECT * FROM  _mapping_tb WHERE  Date(_date)>date(?)";
//        Cursor cursor = database.rawQuery(query, new String[]{parseDateToddMMyyyy("17/12/19")});
//
//        String query = "SELECT * FROM  _all_bills_tb WHERE _unit=?  AND _amount=?";
//        Cursor cursor = database.rawQuery(query, new String[]{"piece","25.0"});
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//
//                   // mBillItems.add(new BillItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),Double.parseDouble(cursor.getString(3)),Double.parseDouble(cursor.getString(4)),Double.parseDouble(cursor.getString(5)) , cursor.getLong(6) , cursor.getString(7),cursor.getInt(8)));
//                    //Log.i("------------",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" -"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+" ---"+cursor.getString(6)+" ---"+cursor.getString(8)+cursor.getString(9)+" ---"+cursor.getString(10)+" ---"+cursor.getString(11)+" ---");//+cursor.getString(12)+" ---"+cursor.getString(13));
//
//                    Log.i("------------",cursor.getString(0)+" & "+cursor.getString(1)+" & "+cursor.getString(2)+" & "+cursor.getString(3)+" & "+cursor.getString(4)+" & "+cursor.getString(5)+" & "+cursor.getString(6)+" & "+cursor.getString(8)+" & "+cursor.getString(10));
//                } while (cursor.moveToNext());
//                mBillsRecyclerViewAdapter.notifyDataSetChanged();
//            }
//            else {
//                Toast.makeText(MainActivity.this, "Not Found" ,Toast.LENGTH_LONG).show();
//            }
//
//            cursor.close();
//
//        }else {
//            Toast.makeText(MainActivity.this, "Not Found null" ,Toast.LENGTH_LONG).show();
//        }



        query = "SELECT B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label FROM  _all_bills_tb B WHERE B._desc LIKE (?)  AND B._shop_id LIKE (SELECT  _shop_id FROM  _all_shops_tb WHERE _shop_name LIKE (?)  AND _area LIKE (?) AND  _group LIKE (?) )";
        cursor = database.rawQuery(query, new String[]{"%%","%%","%%","%%"});
        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {
//                    String mitem_id_label, String mitem_desc, String mitem_unit, double mitem_rate, double mitem_qty, double mitem_amount , Long mshop_Id, String mdate, int mcount
                    //Log.i("------------",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+cursor.getString(7)+cursor.getString(8)+"---------------"+cursor.getString(9));
                    mBillItems.add(new BillItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),Double.parseDouble(cursor.getString(3)),Double.parseDouble(cursor.getString(4)),Double.parseDouble(cursor.getString(5)) , cursor.getLong(6) , cursor.getString(7),cursor.getInt(8)));
                } while (cursor.moveToNext());
                mBillsRecyclerViewAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(MainActivity.this, "Not Found" ,Toast.LENGTH_LONG).show();
            }

            cursor.close();

        }else {
            Toast.makeText(MainActivity.this, "Not Found null" ,Toast.LENGTH_LONG).show();
        }
        mBillsCount.setText(String.valueOf(mBillItems.size()));
//
        LoadeBillsDataAsyncTask lodeItemsDataAsyncTask = new LoadeBillsDataAsyncTask();
        lodeItemsDataAsyncTask.execute();

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
        cs.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        cs.setFillPattern(HSSFCellStyle.ALIGN_CENTER);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("myOrder");

        // Generate column headings

            Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("S.No");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Desc ");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Qty");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Rate");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Unit");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("Amount");
        c.setCellStyle(cs);


        CellStyle cse = wb.createCellStyle();
        cse.setFillForegroundColor(HSSFColor.AQUA.index);


        CellStyle cs0 = wb.createCellStyle();
        cs0.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);


        for(int i=0;i<mBillItems.size();i++) {

            Row row2 = sheet1.createRow(1+i);


            c = row2.createCell(0);
            c.setCellValue(i);
            c.setCellStyle(cs0);

            c = row2.createCell(1);
            c.setCellValue(mBillItems.get(i).getMitem_desc());
            c.setCellStyle(cs0);


            c = row2.createCell(2);
            c.setCellValue(mBillItems.get(i).getMitem_rate());
            c.setCellStyle(cs0);



            c = row2.createCell(3);
            c.setCellValue(mBillItems.get(i).getMitem_qty());
            c.setCellStyle(cs0);


            c = row2.createCell(4);
            c.setCellValue(mBillItems.get(i).getMitem_unit());
            c.setCellStyle(cs0);

            c = row2.createCell(5);
            c.setCellValue(mBillItems.get(i).getMitem_amount());
            c.setCellStyle(cs0);


        }

        sheet1.setColumnWidth(0, (15 * 100));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 200));
        sheet1.setColumnWidth(3, (15 * 200));
        sheet1.setColumnWidth(4, (15 * 200));
        sheet1.setColumnWidth(5, (15 * 200));

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

        return success;
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


//   /song Name: 1--- 90---- 2 ---2019-12-10 ---new shop
//    2--- 68---- 3 ---2019-12-17 ---new shop
//    3--- 640---- 38 ---2020-01-02 ---new shop
//10-12-2019
//        17-12-2019
//        02-01-2020
//        10-12-2019
//        17-12-2019
//        02-01-2020

//    public void doSyncShops(){
//        mAllBillsList.clear();
////        final ShopsStorageClass storage = new ShopsStorageClass(DashBoardActivity.activity_dashBoard);
////        storage.clearCachedShops();
//        DatabaseReference mDataRef = databaseFirebase.getReference("data");
//        mDataRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//                    DatabaseReference mShopsRef = snapshot.child("bills").getRef();
//                    mShopsRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            int i=0;
//                            for(DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
//
//
//                                Log.i("----------", snapshot1.getValue().toString());
//                                //Log.i("----------", snapshot1.child("mitem_desc").getValue().toString());
//
//                                BillItem billItem = new BillItem(snapshot1.child("bill_id").getValue().toString()
//
//                                        ,snapshot1.child("mitem_desc").getValue().toString()
//                                        ,snapshot1.child("mitem_unit").getValue().toString()
//                                        ,Double.parseDouble(snapshot1.child("mitem_qty").getValue().toString())
//                                        ,Double.parseDouble(snapshot1.child("mitem_rate").getValue().toString())
//                                        ,Double.parseDouble(snapshot1.child("mitem_amount").getValue().toString())
//                                      );
//                              //  storage.addNewShop(billItem);
//                                mBillItems.add(billItem);
//                                mBillsRecyclerViewAdapter.notifyItemInserted(i);
//                                i++;
//
//                               // mShopsRecyclerViewAdapter.notifyDataSetChanged();
//                            }
//
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                    mBillsRecyclerViewAdapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//    }

    //        String query = "SELECT _bill_id ,_amount  , _desc , _id_label  , _qty , _rate , _unit  FROM  _all_bills_tb ";
//        Cursor cursor = database.rawQuery(query, new String[]{});
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//                    Log.i("------------",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6));
//                    //mFilterItemList
//                } while (cursor.moveToNext());
//            }
//            else {
//                Toast.makeText(MainActivity.this, "Not Found" ,Toast.LENGTH_LONG).show();
//            }
//
//            cursor.close();
//
//        }else {
//            Toast.makeText(MainActivity.this, "Not Found null" ,Toast.LENGTH_LONG).show();
//        }

//
//    mFilterItem = mFilterBillsLayout.findViewById(R.id.tv_filter_item);
//    mFilterOnDate = mFilterBillsLayout.findViewById(R.id.tv_filter_on_date);
//    mFilterFromDate = mFilterBillsLayout.findViewById(R.id.tv_filter_from_date);
//    mFilterTillDate = mFilterBillsLayout.findViewById(R.id.tv_filter_till_date);
//    mFilterInMonth = mFilterBillsLayout.findViewById(R.id.tv_filter_in_month);
//    mFilterShop = mFilterBillsLayout.findViewById(R.id.tv_filter_shop);
//    mFilterArea = mFilterBillsLayout.findViewById(R.id.tv_filter_area);
//    mFilterGroup = mFilterBillsLayout.findViewById(R.id.tv_filter_group);
//
    public void doFilterList(){


//        if(!mFilterShop.getSelectedItem().equals("----")){
//            queries.add(" AND B._id_label=S._mbill_id AND S._shop_name = "+"'"+mFilterShop.getSelectedItem()+"' ");
//        }
//
//
//        if(!mFilterArea.getSelectedItem().equals("----")){
//            queries.add(" AND S._area = "+"'"+mFilterArea.getSelectedItem()+"' ");
//        }
//
//
//        if(!mFilterGroup.getSelectedItem().equals("----")){
//            queries.add(" AND S._group = "+"'"+mFilterGroup.getSelectedItem()+"' ");
//        }
//
        String desc="%%";
        String shop="%%";
        String area="%%";
        String group="%%";



        if(!mFilterItem.getSelectedItem().toString().equals("----")){
           desc=mFilterItem.getSelectedItem().toString();
        }

        if(!mFilterShop.getSelectedItem().toString().equals("----")){
            shop=mFilterShop.getSelectedItem().toString();
        }


        if(!mFilterArea.getSelectedItem().toString().equals("----")){
            area=mFilterArea.getSelectedItem().toString();
        }


        if(!mFilterGroup.getSelectedItem().toString().equals("----")){
            group=mFilterGroup.getSelectedItem().toString();
        }

        boolean date_selectd = false;

        String query = "SELECT B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label FROM  _all_bills_tb B WHERE B._desc LIKE (?)  AND B._shop_id LIKE (SELECT  _shop_id FROM  _all_shops_tb WHERE _shop_name LIKE (?)  AND _area LIKE (?) AND  _group LIKE (?) )";
        Cursor cursor = database.rawQuery(query, new String[]{desc,shop,area,group});

        if(!mFilterOnDate.getText().toString().equals("00/00/00")){
            Log.i("now executed-------",mFilterOnDate.getText().toString());
            query = "SELECT B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label FROM  _all_bills_tb B WHERE Date(B._date) = Date(?) AND B._desc LIKE (?)  AND B._shop_id LIKE (SELECT  _shop_id FROM  _all_shops_tb WHERE _shop_name LIKE (?)  AND _area LIKE (?) AND  _group LIKE (?) )";
            cursor = database.rawQuery(query, new String[]{parseDateToddMMyyyy(mFilterOnDate.getText().toString()),desc,shop,area,group});
            date_selectd = true;
        }
        else if(!mFilterFromDate.getText().toString().equals( "00/00/00")){
            query = "SELECT B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label FROM  _all_bills_tb B WHERE  Date(B._date) BETWEEN Date(?) AND  date(?)    AND B._desc LIKE (?)  AND B._shop_id LIKE (SELECT  _shop_id FROM  _all_shops_tb WHERE _shop_name LIKE (?)  AND _area LIKE (?) AND  _group LIKE (?) )";
            cursor = database.rawQuery(query, new String[]{parseDateToddMMyyyy(mFilterFromDate.getText().toString()),parseDateToddMMyyyy(mFilterTillDate.getText().toString()),desc,shop,area,group});
            date_selectd = true;

        }else if(!mFilterInMonth.getSelectedItem().toString().equals("----")){
             query = "SELECT  B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label FROM  _all_bills_tb B WHERE B._desc LIKE (?) AND B._date <(?) AND B._shop_id LIKE (SELECT  _shop_id FROM  _all_shops_tb WHERE _shop_name LIKE (?)  AND _area LIKE (?) AND  _group LIKE (?) )";
             cursor = database.rawQuery(query, new String[]{desc,"%%-08-20",shop,area,group});
            date_selectd = true;
        }


        if( shop.equals("%%" ) && area.equals("%%") && group.equals("%%") && date_selectd){
            if(!mFilterOnDate.getText().toString().equals("00/00/00")){

                Log.i("now executed--------",mFilterOnDate.getText().toString());
                query = "SELECT  B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label FROM  _all_bills_tb B WHERE Date(B._date) = Date(?) AND B._desc LIKE (?) ";
                cursor = database.rawQuery(query, new String[]{parseDateToddMMyyyy(mFilterOnDate.getText().toString()),desc});

            }
            else if(!mFilterFromDate.getText().toString().equals( "00/00/00")){
                query = "SELECT  B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label FROM  _all_bills_tb B WHERE  Date(B._date) BETWEEN Date(?) AND  date(?)    AND B._desc LIKE (?) ";
                cursor = database.rawQuery(query, new String[]{parseDateToddMMyyyy(mFilterFromDate.getText().toString()),parseDateToddMMyyyy(mFilterTillDate.getText().toString()),desc});


            }else if(!mFilterInMonth.getSelectedItem().toString().equals("----")){
                query = "SELECT  B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label FROM  _all_bills_tb B WHERE B._desc LIKE (?) AND B._date LIKE (?) ";
                cursor = database.rawQuery(query, new String[]{desc,"%%-02-20"});
            }
        }

      // date_selectd = false;

        if(desc.equals("%%") && !date_selectd ){
             query = "SELECT B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label  FROM  _all_bills_tb B WHERE  B._shop_id LIKE (SELECT  _shop_id FROM  _all_shops_tb WHERE _shop_name LIKE (?)  AND _area LIKE (?) AND  _group LIKE (?) )";
             cursor = database.rawQuery(query, new String[]{shop,area,group});
        }


//        String query = "SELECT B._bill_id ,B._amount  , B._desc , B._id_label  , B._qty , B._rate , B._unit ,_count , B._date , B._shop_id FROM  _all_bills_tb B WHERE B._desc LIKE (?) AND B._date <(?) AND B._shop_id LIKE (SELECT  _shop_id FROM  _all_shops_tb WHERE _shop_name LIKE (?)  AND _area LIKE (?) AND  _group LIKE (?) )";
//        Cursor cursor = database.rawQuery(query, new String[]{desc,parseDateToddMMyyyy("02/01/20"),shop,area,group});
        mBillItems.clear();
       // mBillsRecyclerViewAdapter.notifyDataSetChanged();

        Log.i("data-----",query);
        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {
//                    String mitem_id_label, String mitem_desc, String mitem_unit, double mitem_rate, double mitem_qty, double mitem_amount , Long mshop_Id, String mdate, int mcount
                    Log.i("------------",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+cursor.getString(7)+cursor.getString(8)+"---------------"+cursor.getString(9));
                    mBillItems.add(new BillItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),Double.parseDouble(cursor.getString(3)),Double.parseDouble(cursor.getString(4)),Double.parseDouble(cursor.getString(5)) , cursor.getLong(6) , cursor.getString(7),cursor.getInt(8)));
                } while (cursor.moveToNext());
                mBillsRecyclerViewAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(MainActivity.this, "Not Found" ,Toast.LENGTH_LONG).show();
            }

            cursor.close();

        }else {
            Toast.makeText(MainActivity.this, "Not Found null" ,Toast.LENGTH_LONG).show();
        }
        mBillsCount.setText(String.valueOf(mBillItems.size()));

    }

    public void clearFilters(){

        mFilterItem.setSelection(0);
        mFilterOnDate.setText("00/00/00");
        mFilterFromDate.setText("00/00/00");
        mFilterTillDate.setText("00/00/00");
        mFilterInMonth.setSelection(0);
        mFilterShop.setSelection(0);
        mFilterArea.setSelection(0);
        mFilterGroup.setSelection(0);
        mFilterOnDate.setAlpha(0.2f);
        mFilterFromDate.setAlpha(0.2f);
        mFilterTillDate.setAlpha(0.2f);
        mFilterInMonth.setAlpha(0.2f);
//        LoadeBillsDataAsyncTask lodeItemsDataAsyncTask = new LoadeBillsDataAsyncTask();
//        lodeItemsDataAsyncTask.execute();

    }

    public void doSetupAdapters(){
        ShopItemsStorageClass storage = new ShopItemsStorageClass(getApplicationContext());
        ArrayList<ShopItemModel> shop_items = storage.loadItems();
        mFilterItemList.add("----");
        if (shop_items != null) {
            if (shop_items.size() != 0) {

                for(int i=0;i<shop_items.size();i++){
                    mFilterItemList.add(shop_items.get(i).getMitem_desc());
                }

                mFilterItem.setSelection(0);
            }
        }
        mFilterItemAdapter.notifyDataSetChanged();

        mFilterInMonthList.add("----");
        mFilterInMonthList.add(" jan ");
        mFilterInMonthList.add(" feb ");
        mFilterInMonthList.add(" mar ");
        mFilterInMonthList.add(" april ");
        mFilterInMonthList.add(" may ");
        mFilterInMonthList.add(" june ");
        mFilterInMonthList.add(" july ");
        mFilterInMonthList.add(" aug ");
        mFilterInMonthList.add(" sept ");
        mFilterInMonthList.add(" oct ");
        mFilterInMonthList.add(" nov ");
        mFilterInMonthList.add(" dec ");
        mFilterInMonthAdapter.notifyDataSetChanged();


        ShopsStorageClass storage2 = new ShopsStorageClass(getApplicationContext());
        ArrayList<ShopDetailsModel> shops_list = storage2.loadShops();

        mFilterShopList.add("----");
        mFilterAreaList.add("----");
        mFilterGroupList.add("----");

        if (shops_list != null) {
            if (shops_list.size() != 0) {
                for(int i=0;i<shops_list.size();i++){
                    mFilterShopList.add(shops_list.get(i).getmShopName());
                    mFilterAreaList.add(shops_list.get(i).getmArea());
                    mFilterGroupList.add(shops_list.get(i).getmGroup());
                }

               mFilterShop.setSelection(0);
               mFilterArea.setSelection(0);
               mFilterGroup.setSelection(0);
            }
        }
        mFilterShopAdapter.notifyDataSetChanged();
        mFilterAreaAdapter.notifyDataSetChanged();
        mFilterGroupAdapter.notifyDataSetChanged();

    }


    private void pickOnDate() {
        dateType=0;
        DialogFragment date_picker = new DatePickerFragment(0);
        date_picker.show(getSupportFragmentManager(),"date picker");

    }
    private void pickFromDate() {
        dateType=1;
        DialogFragment date_picker = new DatePickerFragment(1);
        date_picker.show(getSupportFragmentManager(),"date picker");
    }
    private void pickTillDate() {
        dateType=2;
        DialogFragment date_picker = new DatePickerFragment(2);
        date_picker.show(getSupportFragmentManager(),"date picker");
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd/MM/yy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i(time,str);
        return str;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c =Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy ");
        String currentDate = dateFormat.format(c.getTime());


       if(dateType==0){
           mFilterOnDate.setText(currentDate);
       }
       else if(dateType==1){
           mFilterFromDate.setText(currentDate);
       }
       else if(dateType==2 ){
           mFilterTillDate.setText(currentDate);
       }
    }




    public class LoadeBillsDataAsyncTask extends AsyncTask<ArrayList<BillItem>, Void, ArrayList<BillItem>> {
        @Override
        protected void onPostExecute(ArrayList<BillItem> billItems) {
            super.onPostExecute(billItems);
            if (billItems != null) {
                Toast.makeText(MainActivity.this, "Loading....", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "some thing went wrong", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected ArrayList<BillItem> doInBackground(ArrayList<BillItem>... arrayLists) {


            DatabaseReference mDataRef = databaseFirebase.getReference("data");
            mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot salesmen : dataSnapshot.getChildren()){

                        DatabaseReference mBillsref = salesmen.child("bills").getRef();
                        mBillsref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot bills : dataSnapshot.getChildren()){

                                    DatabaseReference mBill = bills.getRef();
                                    mBill.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                         for(DataSnapshot bill : dataSnapshot.getChildren()) {

                                             if (bill.child("bill_id").getValue().toString().equals("--")) {


                                             } else {
                                                 Log.i("changed", bill.getValue().toString());
                                                 BillItem billItem = new BillItem(bill.child("bill_id").getValue().toString()
                                                         , bill.child("mitem_desc").getValue().toString()
                                                         , bill.child("mitem_unit").getValue().toString()
                                                         , Double.parseDouble(bill.child("mitem_qty").getValue().toString())
                                                         , Double.parseDouble(bill.child("mitem_rate").getValue().toString())
                                                         , Double.parseDouble(bill.child("mitem_amount").getValue().toString())
                                                         , Long.parseLong(bill.child("mShopId").getValue().toString())
                                                         , bill.child("mDate").getValue().toString()
                                                         , Integer.parseInt(bill.child("mCount").getValue().toString())
                                                 );


                                                 mBillItems.add(billItem);
                                                      dataBaseHelper.insertBill(String.valueOf(billItem.getBill_id()), String.valueOf(billItem.getMitem_amount()),
                                                                billItem.getMitem_desc(), billItem.getMitem_id_label(), String.valueOf(billItem.getMitem_qty()),
                                                                String.valueOf(billItem.getMitem_rate()), billItem.getMitem_unit(), billItem.mshop_Id.toString(), billItem.getMdate(), String.valueOf(billItem.getMcount()), database);

                                                 bill.child("bill_id").getRef().setValue("--");
                                             }
                                         }

                                         mBillsRecyclerViewAdapter.notifyDataSetChanged();
                                         mBillsCount.setText(mBillItems.size()+"");
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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return mBillItems;
        }


    }



//    public class LoadeBillsDataAsyncTask extends AsyncTask<ArrayList<BillItem>, Void, ArrayList<BillItem>> {
//        @Override
//        protected void onPostExecute(ArrayList<BillItem> billItems) {
//            super.onPostExecute(billItems);
//            if (billItems != null) {
//                Toast.makeText(MainActivity.this, "Loading....", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(MainActivity.this, "some thing went wrong", Toast.LENGTH_LONG).show();
//            }
//
////            clearBillsDataAsyncTask lodeItemsDataAsyncTask = new clearBillsDataAsyncTask();
////            lodeItemsDataAsyncTask.execute();
//
//
//        }
//
//




//        @Override
//        protected ArrayList<BillItem> doInBackground(ArrayList<BillItem>... arrayLists) {
//
//           //dataBaseHelper.clearBills(database);
//          //  mAllBillsList.clear();
//            DatabaseReference mDataRef = databaseFirebase.getReference("data");
//            mDataRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                        DatabaseReference mShopsRef = snapshot.child("bills").getRef();
//                        mShopsRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
//
//                                      Log.i("MSG----",snapshot1.getKey());
//
//                                      DatabaseReference billsref = snapshot1.getRef();
//                                      billsref.addValueEventListener(new ValueEventListener() {
//                                          @Override
//                                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                              Log.i("Key 1",dataSnapshot.getKey()+"");
//
//                                              int i = 0;
//                                             DataSnapshot d2 = dataSnapshot.getChildren().iterator().next();
//                                              {
//                                                  //Log.i("final--------",d2.child("mitem_desc").getValu
//                                                  // e().toString());
//
//                                                  Log.i("Key 1",d2.getKey()+"");
//
////                                                    if(d2.child("bill_id").getValue().toString().equals("--")){
////                                                       Log.i("diacarted-------","this ");
////                                                    }
////                                                    else {
//
//
//                                                        Log.i("desc",d2.child("mitem_desc").getValue().toString());
//
//
////                                                        BillItem billItem = new BillItem(d2.child("bill_id").getValue().toString()
////                                                                , d2.child("mitem_desc").getValue().toString()
////                                                                , d2.child("mitem_unit").getValue().toString()
////                                                                , Double.parseDouble(d2.child("mitem_qty").getValue().toString())
////                                                                , Double.parseDouble(d2.child("mitem_rate").getValue().toString())
////                                                                , Double.parseDouble(d2.child("mitem_amount").getValue().toString())
////                                                                , Long.parseLong(d2.child("mShopId").getValue().toString())
////                                                                , d2.child("mDate").getValue().toString()
////                                                                , Integer.parseInt(d2.child("mCount").getValue().toString())
////                                                        );
////
////
////                                                        mBillItems.add(billItem);
////                                                        dataBaseHelper.insertBill(String.valueOf(billItem.getBill_id()), String.valueOf(billItem.getMitem_amount()),
////                                                                billItem.getMitem_desc(), billItem.getMitem_id_label(), String.valueOf(billItem.getMitem_qty()),
////                                                                String.valueOf(billItem.getMitem_rate()), billItem.getMitem_unit(), billItem.mshop_Id.toString(), billItem.getMdate(), String.valueOf(billItem.getMcount()), database);
////
//////                                                        mBillsRecyclerViewAdapter.notifyItemInserted(i);
////////                                                      i++;
////                                                        d2.child("bill_id").getRef().setValue("--");
//
//                                                    }
//
//
//                                              //}
//
//                                              mBillsRecyclerViewAdapter.notifyDataSetChanged();
//                                              mBillsCount.setText(mBillItems.size()+"");
//
//                                          }
//
//                                          @Override
//                                          public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                          }
//                                      });
//
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//            return mBillItems;
//        }
//    }


    public class LoadMappingData extends AsyncTask<ArrayList<BillItem>, Void ,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(ArrayList... arrayLists) {


        DatabaseReference mDataRef = databaseFirebase.getReference("data");
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){

                    DatabaseReference mShopsRef = snapshot.child("mapping").getRef();
                    mShopsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                               Log.i("----------",snapshot1.getValue().toString());

                                dataBaseHelper.insertMapping(snapshot1.child("mBillId").getValue().toString(),
                                        snapshot1.child("mCount").getValue().toString(),
                                        snapshot1.child("mDate").getValue().toString(),
                                        snapshot1.child("mName").getValue().toString(),
                                        snapshot1.child("mShopId").getValue().toString(),
                                        database
                                        );
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


            return null;
        }
    }


}
