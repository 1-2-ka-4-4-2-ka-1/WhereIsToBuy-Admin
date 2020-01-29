package com.findviewbyid.tiwari.radmin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BillsActivity extends AppCompatActivity {



    private RecyclerView mBillsListRecyclerView;
    private BillItemsRecyclerViewAdapter mBillsRecyclerViewAdapter;
    public RecyclerView.LayoutManager mBillsListlayoutManager;
    public ArrayList<BillItem> mBillItems;

    public static DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;
    private FirebaseDatabase databaseFirebase;

    private FloatingActionButton mSharePdfButton;


    private String date;
    private int id;


    public static final int EDIT_NOTE_REQUEST = 2;
    private int pos=0;



    private Paint paint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();
        databaseFirebase = FirebaseDatabase.getInstance();








        mSharePdfButton = findViewById(R.id.fb_share_pdf_button);
        mSharePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           proceedToBillPreview();

            }
        });


        mBillItems = new ArrayList<>();
        mBillsListRecyclerView = findViewById(R.id.rv_bills_recyclerView);
        mBillsListlayoutManager = new LinearLayoutManager(this);
        mBillsRecyclerViewAdapter = new BillItemsRecyclerViewAdapter(mBillItems);
        mBillsListRecyclerView.setLayoutManager(mBillsListlayoutManager);
        mBillsListRecyclerView.setAdapter(mBillsRecyclerViewAdapter);



        date = getIntent().getStringExtra("date");
        id = getIntent().getIntExtra("id",-1);



//        BillsActivity.LoadeBillsDataAsyncTask lodeItemsDataAsyncTask = new BillsActivity.LoadeBillsDataAsyncTask();
//        lodeItemsDataAsyncTask.execute();



        Log.i("date is ---",id+"");
        String query = "SELECT B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label FROM  _all_bills_tb B WHERE Date(B._date) = Date(?) AND B._id_label = (?)";
        Cursor cursor = database.rawQuery(query, new String[]{(date),String.valueOf(id)});
        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {

                    Log.i("------------",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+cursor.getString(7)+cursor.getString(8)+"---------------"+cursor.getString(9));
                    mBillItems.add(new BillItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),Double.parseDouble(cursor.getString(3)),Double.parseDouble(cursor.getString(4)),Double.parseDouble(cursor.getString(5)) , cursor.getLong(6) , cursor.getString(7),cursor.getInt(8)));

                } while (cursor.moveToNext());
                mBillsRecyclerViewAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(BillsActivity.this, "Not Found" ,Toast.LENGTH_LONG).show();
            }

            cursor.close();

        }else {
            Toast.makeText(BillsActivity.this, "Not Found null" ,Toast.LENGTH_LONG).show();
        }


        mBillsRecyclerViewAdapter.setOnItemClickedListener(new BillItemsRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent intent = new Intent(BillsActivity.this, EditBillActivity.class);

                pos = position;
                intent.putExtra(EditBillActivity.EXTRA_ID_LABLE,mBillItems.get(position).getMitem_id_label());
                intent.putExtra(EditBillActivity.EXTRA_ID,id);
                intent.putExtra(EditBillActivity.EXTRA_DESC,mBillItems.get(position).getMitem_desc());
                intent.putExtra(EditBillActivity.EXTRA_UNIT,mBillItems.get(position).getMitem_unit());
                intent.putExtra(EditBillActivity.EXTRA_RATE,mBillItems.get(position).getMitem_rate());
                intent.putExtra(EditBillActivity.EXTRA_QTY,mBillItems.get(position).getMitem_qty());
                intent.putExtra(EditBillActivity.EXTRA_AMOUNT,mBillItems.get(position).getMitem_amount());

                startActivityForResult(intent,EDIT_NOTE_REQUEST);

            }
        });


    }



    public void drawOncancvas(Canvas canvas){
        // canvas.drawColor(Color.RED);
        paint = new Paint();
        paint.setColor(Color.BLACK);


        canvas.drawText("Shop:", 100-50, 90-30, paint);

        ShopsStorageClass shopsStorageClass = new ShopsStorageClass(BillsActivity.this);
        ShopDetailsModel shop= shopsStorageClass.getShopById(Long.toString(mBillItems.get(0).getMshop_Id()));
        paint.setTextSize(8);
        String shopDetail = shop.getmShopName()+"  "+shop.getmAddress()+"  "+shop.getmArea()+"  "+shop.getmLandmark()+"  "+shop.getmContactno()+"123456456123123123121123456456123123123121212345645612312312312122123456456123123123121123456456123123123121212345645612312312312122123456456123123123121123456456123123123121212345645612312312312122123456456123123123121123456456123123123121212345645612312312312122123456456123123123121123456456123123123121212345645612312312312122123456456123123123121123456456123123123121212345645612312312312122";
        Log.i("length is ---",shopDetail.length()+"");


        String l1="";
        String l2="";

        int len = shopDetail.length();
        if(len>64)
        {
            l1= shopDetail.substring(0,63);
            l2= shopDetail.substring(l1.length(),len);

        }else {
            l1= shopDetail;
            l2="";
        }

        if(l2.length()>65){
            l2 = l2.substring(0,65);
            l2+="....";
        }

        canvas.drawText(l1, 100-10, 90-30, paint);
        canvas.drawText(l2, 100-40, 130-45, paint);


        paint.setStrokeWidth(0.4f);
        paint.setTextSize(10);
        canvas.drawLine(100-50, 100-30, 450-50, 100-30, paint);
        canvas.drawLine(100-50, 130-40, 450-50, 130-40, paint);
        canvas.drawText(mBillItems.get(0).mdate, 490-140, 70-50, paint);

        paint.setColor(Color.rgb(12, 108, 138));

        paint.setTextSize(8);
        paint.setTypeface(Typeface.SANS_SERIF);
        canvas.drawText("Rough Estimate", 170, 15, paint);
        paint.setTextSize(25);
        canvas.drawText("REENA", 165, 35, paint);


        paint.setStrokeWidth(1);
        paint.setTextSize(10);
        paint.setColor(Color.BLACK);

        canvas.drawText("Qty", 90 + 3-50, 170-50, paint);
        canvas.drawText("Unit", 120 + 10-50, 170-50, paint);
        canvas.drawText("Desc of good", 155 + 40-50, 170-50, paint);
        canvas.drawText("Rate", 335 + 5-50, 170-50, paint);
        canvas.drawText("Amount", 400-50, 170-50, paint);


        //canvas.drawLine(90,160,90,750,paint);
//        canvas.drawLine(90-50, 190-50, 90-50, 595-80, paint);
//        canvas.drawLine(120-50, 190-50, 120-50, 595-80, paint);
//        canvas.drawLine(165-50, 190-50, 165-50, 595-80, paint);
//        canvas.drawLine(325-50, 190-50, 325-50-10, 595-80, paint);
//        canvas.drawLine(390-50-10, 190-50, 390-50-10, 595-80, paint);
//        canvas.drawLine(460-50-10, 190-50, 460-50-10, 595-80, paint);


        canvas.drawText("Note:", 80-50, 595-60, paint);
        canvas.drawText("Total", 390-50, 595-50, paint);
    }



    public void proceedToBillPreview() {
// create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(421, 595, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        drawOncancvas(canvas);


        paint.setTextSize(8);
        int j = 25;

        int total = 0;



        int max = 17;
        Paint p = new Paint();
        p.setColor(Color.WHITE);

        int o=25;
        int count =  mBillItems.size();

        if(count>17)
            count = 17;

        for (int i = 0; i < count; i++) {

            BillItem item = mBillItems.get(i);

            if(i%2==0)
            {
                p.setColor(Color.parseColor("#f0fcfc"));
            }else p.setColor(Color.parseColor("#e8e8e8"));

            canvas.drawRect(30,170 + o-50,405,170 +o-50+25,p);



            o+=22;

            String desc = item.getMitem_desc();
            j = j + 10;
            canvas.drawText((Integer.toString((int)item.getMitem_qty())), 90 + 10-50, 170 + j-50, paint);
            canvas.drawText(item.getMitem_unit(), 120 + 15-50, 170 + j-50,paint);
            canvas.drawText(String.valueOf((int)item.getMitem_rate()), 325 + 5-50, 170 + j-50, paint);
            canvas.drawText(String.valueOf(item.getMitem_amount()), 400-50, 170 + j-50, paint);

            total += item.getMitem_amount();

            int m = 0;
            int n = 0;
            if (desc.length() - m < 25) {
                n = desc.length();
                // j+=10;
            } else {
                n = 25;
                max = 15;
            }
            for (int k = 0; k <= desc.length() / 25; k++) {
                canvas.drawText(desc.substring(m, n), 135 + 40-50, 170 + j-50, paint);
                m = n;
                if (desc.length() - m < 25) {
                    n = desc.length();
                } else {

                    n = 25;
                    j += 12;

                }

                j += 12;
            }

        }

        paint.setTextSize(12);
        canvas.drawText(Double.toString(total), 390-50, 595-20, paint);


        canvas.drawLine(90-50, 190-50, 90-50, 595-80+5, paint);
        canvas.drawLine(120-50, 190-50, 120-50, 595-80+5, paint);
        canvas.drawLine(165-50, 190-50, 165-50, 595-80+5, paint);
        canvas.drawLine(325-50-10, 190-50, 325-50-10, 595-80+5, paint);
        canvas.drawLine(390-50-10, 190-50, 390-50-10, 595-80+5, paint);
        canvas.drawLine(460-50-10, 190-50, 460-50-10, 595-80+5, paint);



        paint.setStrokeWidth(0.4f);
        //canvas.drawLine(10, 595-2, 411, 595-2, paint);


        // finish the page
        document.finishPage(page);



        if(mBillItems.size()>17)
        {
            pageInfo = new PdfDocument.PageInfo.Builder(421, 595, 1).create();
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();
            drawOncancvas(canvas);
            paint.setTextSize(8);
             j = 25;

             total = 0;



             max = 17;
             p = new Paint();
            p.setColor(Color.WHITE);

            o=25;
            for (int i = 17; i < mBillItems.size(); i++) {

                BillItem item = mBillItems.get(i);

                if(i%2==0)
                {
                    p.setColor(Color.parseColor("#f0fcfc"));
                }else p.setColor(Color.parseColor("#e8e8e8"));

                canvas.drawRect(30,170 + o-50,405,170 +o-50+25,p);



                o+=22;

                String desc = item.getMitem_desc();
                j = j + 10;
                canvas.drawText((Integer.toString((int)item.getMitem_qty())), 90 + 10-50, 170 + j-50, paint);
                canvas.drawText(item.getMitem_unit(), 120 + 15-50, 170 + j-50,paint);
                canvas.drawText(String.valueOf((int)item.getMitem_rate()), 325 + 5-50, 170 + j-50, paint);
                canvas.drawText(String.valueOf(item.getMitem_amount()), 400-50, 170 + j-50, paint);

                total += item.getMitem_amount();

                int m = 0;
                int n = 0;
                if (desc.length() - m < 25) {
                    n = desc.length();
                    // j+=10;
                } else {
                    n = 25;
                    max = 15;
                }
                for (int k = 0; k <= desc.length() / 25; k++) {
                    canvas.drawText(desc.substring(m, n), 135 + 40-50, 170 + j-50, paint);
                    m = n;
                    if (desc.length() - m < 25) {
                        n = desc.length();
                    } else {

                        n = 25;
                        j += 12;

                    }

                    j += 12;
                }

            }

            paint.setTextSize(12);
            canvas.drawText(Double.toString(total), 390-50, 595-20, paint);


            canvas.drawLine(90-50, 190-50, 90-50, 595-80+5, paint);
            canvas.drawLine(120-50, 190-50, 120-50, 595-80+5, paint);
            canvas.drawLine(165-50, 190-50, 165-50, 595-80+5, paint);
            canvas.drawLine(325-50-10, 190-50, 325-50-10, 595-80+5, paint);
            canvas.drawLine(390-50-10, 190-50, 390-50-10, 595-80+5, paint);
            canvas.drawLine(460-50-10, 190-50, 460-50-10, 595-80+5, paint);



            paint.setStrokeWidth(0.4f);
           // canvas.drawLine(10, 595-2, 411, 595-2, paint);


            // finish the page
            document.finishPage(page);
        }

        // Create Page


        // write the document content
        //String targetPdf = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fileName.pdf";
       // String targetPdf =this.getFilesDir() + "/fileName.pdf";
        String fileName = id +"_"+date+"_file.pdf";
        File filePath = new File(BillsActivity.this.getExternalFilesDir(null), fileName);

        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Saved_"+mBillItems.get(0).getBill_id() + fileName, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }


        // close the document
        document.close();
        showPdf();

    }



    public void showPdf(){

        String fileName = id +"_"+date+"_file.pdf";


        Intent display = new Intent(BillsActivity.this,PdfPreviewer.class);
        display.putExtra("filename",BillsActivity.this.getExternalFilesDir(null)+ "/"+fileName);
        startActivity(display);


        File file = new File(BillsActivity.this.getExternalFilesDir(null), fileName);



        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = GenericFileProvider.getUriForFile(BillsActivity.this,getApplicationContext().getPackageName()+".provider",file);
        intent.setDataAndType(uri,"application/pdf");
        intent.putExtra(intent.EXTRA_STREAM,uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);

    }







    public class LoadeBillsDataAsyncTask extends AsyncTask<ArrayList<BillItem>, Void, ArrayList<BillItem>> {
        @Override
        protected void onPostExecute(ArrayList<BillItem> billItems) {
            super.onPostExecute(billItems);

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
                                                    BillItem billItem = new BillItem(bill.child("mitem_id_label").getValue().toString()
                                                            , bill.child("mitem_desc").getValue().toString()
                                                            , bill.child("mitem_unit").getValue().toString()
                                                            , Double.parseDouble(bill.child("mitem_rate").getValue().toString())
                                                            , Double.parseDouble(bill.child("mitem_qty").getValue().toString())
                                                            , Double.parseDouble(bill.child("mitem_amount").getValue().toString())
                                                            , Long.parseLong(bill.child("mShopId").getValue().toString())
                                                            , bill.child("mDate").getValue().toString()
                                                            , Integer.parseInt(bill.child("mCount").getValue().toString())
                                                    );

                                                   if(bill.child("mitem_id_label").getValue().toString().equals(String.valueOf(id))){

                                                       mBillItems.add(billItem);
                                                       mBillsRecyclerViewAdapter.notifyItemInserted(mBillItems.size());
                                                   }

                                                    dataBaseHelper.insertBill(String.valueOf(billItem.getBill_id()), String.valueOf(billItem.getMitem_amount()),
                                                            billItem.getMitem_desc(), billItem.getMitem_id_label(), String.valueOf(billItem.getMitem_qty()),
                                                            String.valueOf(billItem.getMitem_rate()), billItem.getMitem_unit(), billItem.mshop_Id.toString(), billItem.getMdate(), String.valueOf(billItem.getMcount()), database);


                                                    bill.child("bill_id").getRef().setValue("--");

                                                }
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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return null;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){


            int id = data.getIntExtra(EditBillActivity.EXTRA_ID,-1);

            if(id == -1){
                Toast.makeText(getApplicationContext(),"Can't be updated",Toast.LENGTH_LONG).show();
                return;
            }

            String item_id_label = data.getStringExtra(EditBillActivity.EXTRA_ID_LABLE);
            String item_desc = data.getStringExtra(EditBillActivity.EXTRA_DESC);
            String item_unit = data.getStringExtra(EditBillActivity.EXTRA_UNIT);
            double item_rate = data.getDoubleExtra(EditBillActivity.EXTRA_RATE,0);
            double item_qty = data.getDoubleExtra(EditBillActivity.EXTRA_QTY,0);
            double item_amount = data.getDoubleExtra(EditBillActivity.EXTRA_AMOUNT,0);


            BillItem billItem = new BillItem(item_id_label,item_desc,item_unit,item_rate,item_qty,item_amount,mBillItems.get(pos).getMshop_Id(),mBillItems.get(pos).getMdate(),mBillItems.get(pos).getBill_id());
            BillItem prevBill = mBillItems.get(pos);

            dataBaseHelper.updateBill(billItem,prevBill,id,database);


            mBillItems.remove(pos);
            mBillItems.add(pos,billItem);

            mBillsRecyclerViewAdapter.notifyItemChanged(pos);


            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Not Saved",Toast.LENGTH_LONG).show();
        }
    }


}
