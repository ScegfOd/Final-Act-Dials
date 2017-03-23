package com.sss.jjcombs.finalactdials;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class UsableDials extends FragmentActivity {
    private static final String[] labels = {"2B", "2", "2A", "C", "1B", "1", "1A"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usable_dials);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        this.onPostResume();

        //set bottom toggle function
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle_switch);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Dial.setLock(!isChecked);
            }
        });

        //resize numbers from display
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float screen_height = Math.min(metrics.widthPixels, metrics.heightPixels);
        float text_size = getResources().getDimension(R.dimen.menu_height);
        float available_height = screen_height - text_size;
        float scaling_factor = available_height / getResources().getDimension(R.dimen.background_height);


        //get the scale for images
        float size = getResources().getDimension(R.dimen.dial_size) * scaling_factor,
              background_width = getResources().getDimension(R.dimen.background_width) * scaling_factor,
              background_height = getResources().getDimension(R.dimen.background_height) * scaling_factor;

        //get alignment for images
        float top_buffer = getResources().getDimension(R.dimen.top_buffer) * scaling_factor,
              middle_buffer = getResources().getDimension(R.dimen.middle_buffer) * scaling_factor,
              left_buffer = getResources().getDimension(R.dimen.left_buffer) * scaling_factor,
              knob_buffer = getResources().getDimension(R.dimen.knob_buffer) * scaling_factor;
        findViewById(R.id.both_panels).setPadding(Math.round(left_buffer),0,0,0);
        findViewById(R.id.control_panel_top).setPadding(
                0, Math.round(top_buffer), 0, Math.round(middle_buffer));

        //get background color
        int background_pointer = R.drawable.orange_back;
        int chosen_color = getIntent().getIntExtra("COLOR", ContextCompat.getColor(this, R.color.colorOrange));
        if (ContextCompat.getColor(this, R.color.colorGreen) == chosen_color) {
            background_pointer = R.drawable.green_back;
        }

        //resize and set background image
        ImageView panel_back = (ImageView) findViewById(R.id.background);
        Bitmap background = bitmapFromResource(getResources(), background_pointer,
                background_width / metrics.density, background_height / metrics.density);
        Bitmap final_background = Bitmap.createScaledBitmap(background,
                Math.round(background_width), Math.round(background_height), false);
        panel_back.setImageBitmap(final_background);

        //resize and set knob image
        Bitmap knob = bitmapFromResource(getResources(), R.drawable.knob,
                size / metrics.density, size / metrics.density);
        Bitmap final_knob = Bitmap.createScaledBitmap(knob, Math.round(size), Math.round(size), false);
        Dial.setKnob(final_knob);
        Dial.setSize(size);
        Dial.setPadding(knob_buffer);


        //add knobs
        float point_knob_up = 0, point_knob_left = 270;
        for (String label : labels) {
            Dial aFragment = Dial.newInstance(point_knob_left);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.control_panel_top, aFragment, label+"_t").commit();
        }

        for (String label : labels) {
            Dial aFragment = Dial.newInstance(point_knob_up);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.control_panel_bottom, aFragment, label+"_b").commit();
        }
    }

    @Override
    public void onPostResume(){
        super.onPostResume();
        // Hide the things!
        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            uiOptions = uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }


    public static Bitmap bitmapFromResource(Resources res, int resId,
                                            float reqWidth, float reqHeight) {
        return bitmapFromResource(res, resId, Math.round(reqWidth), Math.round(reqHeight));
    }

    public static Bitmap bitmapFromResource(Resources res, int resId,
                                            int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.outHeight = reqHeight;
        options.outWidth = reqWidth;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
