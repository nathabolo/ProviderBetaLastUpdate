package com.tradesman.provider.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jimmiejobscreative.tradesman.provider.R;;

/**
 * Created by Elluminati Mohit on 2/4/2017.
 */

public abstract class CustomDialog extends Dialog implements View.OnClickListener {


    private String dialogTitle;
    private String poitiveButtontext;
    private String negativeButtontext;

    private EditText edtDailog;
    private String edtHint;

    protected CustomDialog(Activity activity,String dialogTitle, String poitiveButtontext , String negativeButtontext, String edtHint) {

        super(activity);
        this.dialogTitle =dialogTitle;
        this.poitiveButtontext = poitiveButtontext;
        this.negativeButtontext = negativeButtontext;
        this.edtHint = edtHint;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);

        setCancelable(false);
        initRequire();
    }

    public void initRequire(){

        Button btnCancel= (Button) findViewById(R.id.btn_dialog_cancel);
        Button btnOk=(Button) findViewById(R.id.btn_dialog_ok);
        TextView txtTitle =(TextView) findViewById(R.id.txt_dialog_title);
        edtDailog = (EditText) findViewById(R.id.edt_dialog_pass);
        txtTitle.setText(dialogTitle);
        btnCancel.setText(negativeButtontext);
        edtDailog.setHint(edtHint);
        btnOk.setText(poitiveButtontext);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btn_dialog_ok){
            OnpositiveButtonClick(edtDailog.getText().toString());
        }else{
            OnnegativeButtonClick();
        }
    }

    public abstract void OnpositiveButtonClick(String edtData);

    public abstract void OnnegativeButtonClick();
}
