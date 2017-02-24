package com.sss.jjcombs.finalactdials;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class UsableDials extends FragmentActivity {

    String[] labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usable_dials);


        int[] drawableIDs = {
                R.drawable.nob_e,
                R.drawable.nob_n,
                R.drawable.nob_ne,
                R.drawable.nob_nw,
                R.drawable.nob_s,
                R.drawable.nob_se,
                R.drawable.nob_sw,
                R.drawable.nob_w,

                // bottom dial:
                R.drawable.nob_ne,
                R.drawable.nob_n,
                R.drawable.nob_ne,
                R.drawable.nob_nw,
                R.drawable.nob_nw};


        BitmapDrawable[] allTheNobs = new BitmapDrawable[drawableIDs.length];

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int upper = (int) ((float) R.dimen.upper_dial_size * metrics.density);
        int lower = (int) ((float) R.dimen.lower_dial_size * metrics.density);


        int i = 0;
        for ( ; i < 8; i++){//top 8
            allTheNobs[i] = drawableBitmapFromResource(getResources(),
                    drawableIDs[i], upper, upper);
        }
        for ( ; i < drawableIDs.length; i++){
            allTheNobs[i] = drawableBitmapFromResource(getResources(),
                    drawableIDs[i], lower, lower);
        }

        Dial.ALL_NOBS = allTheNobs;


        int upper_background = R.drawable.upper_back;

        int chosen_color = getIntent().getIntExtra("COLOR", ContextCompat.getColor(this, R.color.colorMagenta));
        if (ContextCompat.getColor(this, R.color.colorRed) == chosen_color) {
            upper_background = R.drawable.upper_back_r;
        } else if (ContextCompat.getColor(this, R.color.colorGreen) == chosen_color){
            upper_background = R.drawable.upper_back_g;
        }
        Dial.U_BACKGROUND = drawableBitmapFromResource(getResources(),
                upper_background, lower, lower);
        Dial.L_BACKGROUND = drawableBitmapFromResource(getResources(),
                R.drawable.lower_back, lower, lower);

        labels = new String[]{
                getString(R.string.d2b),
                getString(R.string.d2),
                getString(R.string.d2a),
                getString(R.string.dc),
                getString(R.string.d1b),
                getString(R.string.d1),
                getString(R.string.d1a)
        };

        if (findViewById(R.id.control_panel) != null) {

            if (savedInstanceState != null) {
                return;
            }

            for (String label : labels) {
                Dial aFragment = Dial.newInstance(chosen_color, label);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.control_panel, aFragment, label).commit();
            }

        }

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle_switch);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (String label : labels) {
                    ((Dial)getSupportFragmentManager().findFragmentByTag(label)).setButtons(!isChecked);
                }
            }
        });

    }
    public static BitmapDrawable drawableBitmapFromResource(Resources res, int resId,
                                                                 int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return new BitmapDrawable(res, BitmapFactory.decodeResource(res, resId, options));
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
