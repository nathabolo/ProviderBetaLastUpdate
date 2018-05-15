package com.tradesman.provider.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jimmiejobscreative.tradesman.provider.R;;

/**
 * Created by Elluminati Mohit on 2/14/2017.
 */

public abstract class InvoiceDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private String jobAmmount;
    private String adminPrice;
    private String finalAmmount;

    public InvoiceDialog(Context context, String jobAmmount, String adminPrice, String finalAmmount) {
        super(context);
        this.context = context;
        this.jobAmmount = jobAmmount;
        this.adminPrice = adminPrice;
        this.finalAmmount = finalAmmount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.invoice_dialog);
        setCancelable(false);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.color_white_lessOpacity)));
        setCancelable(false);
        initRequire();
    }

    private void initRequire(){

        TextView txtjobAmmount = (TextView) findViewById(R.id.txtjobammount);
        TextView txtAdminPrice = (TextView) findViewById(R.id.txtadminprice);
        TextView txtFinalAmmount = (TextView) findViewById(R.id.txtfinalammount);
        Button btnInvoice = (Button) findViewById(R.id.btninvoice);
        btnInvoice.setOnClickListener(this);
        txtjobAmmount.setText(jobAmmount);
        txtAdminPrice.setText(adminPrice);
        txtFinalAmmount.setText(finalAmmount);
    }

    public abstract void onClickButton(View view);

    @Override
    public void onClick(View view) {
        onClickButton(view);
    }
}
