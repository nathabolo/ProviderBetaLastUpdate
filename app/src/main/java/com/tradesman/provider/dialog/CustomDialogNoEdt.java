package com.tradesman.provider.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jimmiejobscreative.tradesman.provider.R;;

/**
 * Created by Elluminati Mohit on 2/6/2017.
 */

public abstract class CustomDialogNoEdt extends Dialog implements View.OnClickListener {


    private String dialogMessage;
    private String positiveButtontext;
    private String negativeButtontext;
    private String dialogTitle;
    private boolean isTitleAvailbel;


    protected CustomDialogNoEdt(Activity activity, String dialogTitle, String dialogMessage, String positiveButtontext,
                                String negativeButtontext, boolean isTitleAvailbel) {
        super(activity);
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
        this.positiveButtontext = positiveButtontext;
        this.negativeButtontext = negativeButtontext;
        this.isTitleAvailbel = isTitleAvailbel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logout_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        setCancelable(false);
        initRequire();
    }

    public void initRequire() {

        Button btnCancel = (Button) findViewById(R.id.btn_logoutdialog_cancel);
        Button btnOk = (Button) findViewById(R.id.btn_logoutDialog_ok);
        TextView txtTitle = (TextView) findViewById(R.id.txtlogoutTitle);
        TextView txtMessage = (TextView) findViewById(R.id.txt_logoutDialog_message);
        View dialogovShadoLine = findViewById(R.id.dialog_view);
        txtTitle.setText(dialogTitle);
        txtMessage.setText(dialogMessage);
        btnCancel.setText(negativeButtontext);
        btnOk.setText(positiveButtontext);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (!isTitleAvailbel) {
            txtTitle.setVisibility(View.GONE);
            dialogovShadoLine.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_logoutDialog_ok:
                btnOkClickListner();
                break;

            case R.id.btn_logoutdialog_cancel:
                btnCancelClickListner();
                break;
        }
    }

    public abstract void btnOkClickListner();

    public abstract void btnCancelClickListner();

}
