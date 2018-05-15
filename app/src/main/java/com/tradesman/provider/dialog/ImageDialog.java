package com.tradesman.provider.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jimmiejobscreative.tradesman.provider.R;;

/**
 * Created by Elluminati Mohit on 2/21/2017.
 */

public class ImageDialog extends Dialog{

    private  String imgUrl;
    private  Context context;


    public ImageDialog(Context context,String imgUrl) {
        super(context);
        this.imgUrl = imgUrl;
        this.context = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.img_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(true);
        initRequire();
    }

    private void initRequire() {
        ImageView img = (ImageView) findViewById(R.id.imgdialog);
        final ImageView imgProgress = (ImageView) findViewById(R.id.imgprogressDialog);
       imgProgress.setAnimation(AnimationUtils.loadAnimation(context , R.anim.rotation_animation));
        Glide.with(context)
                .load(imgUrl)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        imgProgress.clearAnimation();
                        imgProgress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imgProgress.clearAnimation();
                        imgProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img);
    }

}
