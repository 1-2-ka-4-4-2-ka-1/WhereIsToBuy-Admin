package com.findviewbyid.tiwari.radmin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditBillActivity extends AppCompatActivity {


    public TextView mitem_id_label;
    public EditText mitem_desc;
    public TextView mitem_unit;
    public EditText mitem_rate;
    public EditText mitem_qty;
    public TextView mitem_amount;



    private FloatingActionButton msave_edit;

    public static final String EXTRA_ID_LABLE = "com.findViewById.tiwari.myapplication.EXTRA_ID_LABLE";
    public static final String EXTRA_ID = "com.findViewById.tiwari.myapplication.EXTRA_ID";
    public static final String EXTRA_RATE = "com.findViewById.tiwari.myapplication.EXTRA_RATE";
    public static final String EXTRA_DESC = "com.findViewById.tiwari.myapplication.EXTRA_DESC";
    public static final String EXTRA_AMOUNT = "com.findViewById.tiwari.myapplication.EXTRA_AMOUNT";
    public static final String EXTRA_QTY = "com.findViewById.tiwari.myapplication.EXTRA_QTY";
    public static final String EXTRA_UNIT = "com.findViewById.tiwari.myapplication.EXTRA_UNIT";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill);

        mitem_id_label = findViewById(R.id.tv_item_id);
        mitem_desc = findViewById(R.id.ed_description);
        mitem_unit = findViewById(R.id.tv_unit);
        mitem_rate = findViewById(R.id.ed_rate);
        mitem_qty = findViewById(R.id.ed_quantity);
        mitem_amount = findViewById(R.id.tv_subAmount);

        msave_edit = findViewById(R.id.fb_save_edit_bill);


        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");

            mitem_id_label.setText(String.valueOf(intent.getIntExtra(EXTRA_ID,-1)));
            mitem_rate.setText(Double.toString(intent.getDoubleExtra(EXTRA_RATE, 0)));
            mitem_desc.setText(intent.getStringExtra(EXTRA_DESC));
            mitem_amount.setText(Double.toString(intent.getDoubleExtra(EXTRA_AMOUNT, 0)));
            mitem_qty.setText(Double.toString(intent.getDoubleExtra(EXTRA_QTY, 0)));
            mitem_unit.setText(intent.getStringExtra(EXTRA_UNIT));

        }



        msave_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    doSaveCorrection();

            }
        });


    }



    public void doSaveCorrection(){

        String item_desc = mitem_desc.getText().toString();
        String item_unit = mitem_unit.getText().toString();


        if (mitem_qty.getText().toString().trim().equals("") || mitem_rate.getText().toString().trim().equals("")) {

            Toast toast = Toast.makeText(EditBillActivity.this, "Cannot be Empty", Toast.LENGTH_LONG);
            View v = toast.getView();
            v.getBackground().setColorFilter(Color.rgb(10, 190, 207), PorterDuff.Mode.SRC_IN);
            toast.show();
            return;
        }


        double item_rate = Double.parseDouble(mitem_rate.getText().toString());
        double item_qty = Double.parseDouble(mitem_qty.getText().toString());
        double item_amount = Double.parseDouble(mitem_amount.getText().toString());

        try {

            if (item_desc.trim().isEmpty() || item_rate == 0 || item_qty == 0) {
                Toast toast = Toast.makeText(EditBillActivity.this, "Cannot be 0", Toast.LENGTH_LONG);
                View v = toast.getView();
                v.getBackground().setColorFilter(Color.rgb(10, 190, 207), PorterDuff.Mode.SRC_IN);
                toast.show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(EditBillActivity.this, "Make sure fields are Filled", Toast.LENGTH_SHORT).show();
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_DESC, item_desc);
        data.putExtra(EXTRA_UNIT, item_unit);
        data.putExtra(EXTRA_RATE, item_rate);
        data.putExtra(EXTRA_QTY, item_qty);
        data.putExtra(EXTRA_AMOUNT, item_rate*item_qty);

        data.putExtra(EXTRA_ID,getIntent().getIntExtra(EXTRA_ID,-1) );
        setResult(RESULT_OK, data);
        finish();
    }

}
