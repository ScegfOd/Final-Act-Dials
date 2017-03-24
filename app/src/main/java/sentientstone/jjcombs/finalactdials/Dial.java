package sentientstone.jjcombs.finalactdials;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Represents an individual dial on the Final Act control panel.
 */
public class Dial extends Fragment {
    private static final String ARG_ANGLE = "angle";
    private static boolean mDraggable = true;
    private static float mSize;
    private static int mPadding;
    private float mAngle;
    private ImageView mKnob;
    private static Bitmap mDrawable;
    private View mThisDial;

    public Dial() {/* Required empty public constructor*/}

    /**
     * Use this method to create a new instance with the given starting angle
     *
     * @param angle starting angle of the knob, 0 degrees points up, 270 degrees points left
     * @return A new instance of fragment Dial.
     */
    public static Dial newInstance(float angle){
        Dial fragment = new Dial();
        Bundle args = new Bundle();
        args.putFloat(ARG_ANGLE, angle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mAngle = getArguments().getFloat(ARG_ANGLE);
        } else {
            mAngle = 0;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mThisDial = inflater.inflate(R.layout.fragment_dial, container, false);
        final View thisDial = mThisDial;
        // create knob
        mKnob = (ImageView) (thisDial.findViewById(R.id.knob));
        mKnob.setImageBitmap(mDrawable);
        mKnob.setPadding(mPadding,0,0,0);
        rotateKnob(mAngle);

        //steal the ACTION_MOVE from the horizontal scroller!
        thisDial.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {hideStatusBar();}
                if (mDraggable){
                    if (event.getAction() == MotionEvent.ACTION_MOVE){
                        thisDial.getParent().requestDisallowInterceptTouchEvent(true);
                        float x = event.getX() / mSize;
                        float y = event.getY() / mSize;
                        mAngle = cartesianToDegrees(x, y);
                        mAngle += 180f;
                        mAngle *= -1;
                        rotateKnob(mAngle);
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP){
                        rotateKnob(snapToCorner(mAngle));
                    }
                }
                else {
                    thisDial.getParent().requestDisallowInterceptTouchEvent(false);

                }
                return mDraggable;
            }
        });



        return mThisDial;
    }

    public void rotateKnob(float deg) {
        Matrix matrix=new Matrix();
        mKnob.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.postRotate(deg, mSize/2, mSize/2);
        mKnob.setImageMatrix(matrix);
    }

    /**hiding the status bar is taken care of with immersive starting with KITKAT
     * see {@link sentientstone.jjcombs.finalactdials.UsableDials#onPostResume()}
     * for the other part of why this is unnecessary after API 19
     */
    private void hideStatusBar(){
        mThisDial.getRootView().setSystemUiVisibility(
                  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    public static float snapToCorner(float deg){
        int steps = 0;
        if (deg < 0){
            deg += 360;
        }
        while (deg > 22.5f){
            deg -= 45;
            steps++;
        }
        return steps * 45;
    }
    public static void setLock(boolean set){mDraggable = set;}
    public static void setKnob(Bitmap x){mDrawable = x;}
    public static void setSize(float x){mSize = x;}
    public static void setPadding(float x){mPadding = Math.round(x);}

    private float cartesianToDegrees(float x, float y) {
        return (float) Math.toDegrees(Math.atan2(x - 0.5f, y - 0.5f));
    }

}
