package com.amsoft.RNProgressHUB;


import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageView;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.kaopiz.kprogresshud.*;

/**
 *
 * Created by arnoma2015 on 16/9/13.
 */
public class RNProgressHUBModule  extends ReactContextBaseJavaModule {
    
    private KProgressHUD currentHud = null;

    public RNProgressHUBModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNProgressHUB";
    }


    @ReactMethod
    public void showSimpleText(String message, int duration) {

        //关闭上一个实例
        dismissCurrentHud();

        Activity activity = getCurrentActivity();

        TextView textView = new TextView(activity);
        textView.setText(message);
        textView.setTextColor(0xffffffff);
        textView.setTextSize(18.f);
        currentHud = KProgressHUD.create(activity)
                .setCustomView(textView)
                .show();
        scheduleDismiss(duration);
    }

    @ReactMethod
    public void show(String message, String imageName, int duration) {
        dismissCurrentHud();

        Activity activity = getCurrentActivity();

        LinearLayout customView = new LinearLayout(activity);
        customView.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = new ImageView(activity);
        int id = getCurrentActivity().getResources().getIdentifier(imageName, "drawable", getCurrentActivity().getPackageName());
        imageView.setImageResource(id);
        imageView.setColorFilter(Color.parseColor("#FFFFFF"));
        customView.addView(imageView);

        if (message != null) {
            TextView textView = new TextView(activity);
            textView.setText(message);
            textView.setTextColor(0xffffffff);
            textView.setTextSize(18.f);
            textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            customView.addView(textView);
        }

        currentHud = KProgressHUD.create(activity)
                .setCustomView(customView)
                .show();
        scheduleDismiss(duration);
    }
    
    @ReactMethod
    public void showSpinIndeterminate(){
        dismissCurrentHud();


        currentHud = KProgressHUD.create(getCurrentActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .show();

    }


    @ReactMethod
    public void showSpinIndeterminateWithTitle(String label){
        dismissCurrentHud();
        currentHud = KProgressHUD.create(getCurrentActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(label)
                .show();
    }

    @ReactMethod
    public void showSpinIndeterminateWithTitleAndDetails(String label, String details){
        dismissCurrentHud();
        currentHud = KProgressHUD.create(getCurrentActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(label)
                .setDetailsLabel(details)
                .show();
    }

    @ReactMethod
    public void showDeterminate(int mode, String title, String details){
        dismissCurrentHud();

        currentHud = KProgressHUD.create(getCurrentActivity());
        currentHud.setMaxProgress(100);
        currentHud.setStyle(mode == 0 ? KProgressHUD.Style.ANNULAR_DETERMINATE : KProgressHUD.Style.BAR_DETERMINATE);
        if (title != null){
            currentHud.setLabel(title);
        }
        if (details != null){
            currentHud.setDetailsLabel(details);
        }
        currentHud.show();
    }

    @ReactMethod
    public void setProgress(float progress){
        if (currentHud != null){
            progress = progress * 100;
            currentHud.setProgress((int)progress);
        }
    }

    @ReactMethod
    public void dismiss(){
        dismissCurrentHud();
    }

    //定时关闭
    private void scheduleDismiss(int duration) {
        if(currentHud != null){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissCurrentHud();
                }
            }, duration);
        }
    }

    private void dismissCurrentHud() {
        if(currentHud != null){
            try {
                currentHud.dismiss();
            } catch (Exception e) {
                //
            } finally {
                currentHud = null;
            }
        }
    }


}
