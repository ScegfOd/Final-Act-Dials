package com.sss.jjcombs.finalactdials;


import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Represents an individual pair of dials on the Final Act dial board.
 */
public class Dial extends Fragment {
    private static final String ARG_LABEL = "label", ARG_COLOR = "color";

    private String mlabel;
    private int mcolor;

    //8 directions in alphabetical order (e, n, ne, nw, s, se, sw, w), then top 5 for bottom dial.
    private final static int WEST = 7, NORTH = 1;
    private Button[] mAllButtons;
    public static BitmapDrawable[] ALL_NOBS;
    public static BitmapDrawable L_BACKGROUND, U_BACKGROUND;

    public Dial() {
        // Required empty public constructor
    }

    /**
     * Use this method to create a new instance with the given label displayed
     *
     * @param label Text displayed below the dials.
     * @param color Color of upper dial and the label below the dials.
     * @return A new instance of fragment Dial.
     */

    public static Dial newInstance(int color, String label) {
        Dial fragment = new Dial();
        Bundle args = new Bundle();
        args.putString(ARG_LABEL, label);
        args.putInt(ARG_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mlabel = getArguments().getString(ARG_LABEL);
            mcolor = getArguments().getInt(ARG_COLOR);
        } else {
            mlabel = getString(R.string.label_error);
            mcolor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View thisDial = inflater.inflate(R.layout.fragment_dial, container, false);

        // set the label text from input
        final TextView dialLabel = (TextView) thisDial.findViewById(R.id.bottom_label);
        dialLabel.setText(mlabel);
        dialLabel.setBackgroundColor(mcolor);

        // set default orientation of nobs
        final ImageView uDialImage = (ImageView) thisDial.findViewById(R.id.image_u);
        final ImageView lDialImage = (ImageView) thisDial.findViewById(R.id.image_l);
        uDialImage.setImageDrawable(ALL_NOBS[WEST]);
        uDialImage.setBackground(U_BACKGROUND);
        lDialImage.setImageDrawable(ALL_NOBS[NORTH]);
        lDialImage.setBackground(L_BACKGROUND);

        mAllButtons = new Button[]{
                (Button) thisDial.findViewById(R.id.button_e),
                (Button) thisDial.findViewById(R.id.button_n),
                (Button) thisDial.findViewById(R.id.button_ne),
                (Button) thisDial.findViewById(R.id.button_nw),
                (Button) thisDial.findViewById(R.id.button_s),
                (Button) thisDial.findViewById(R.id.button_se),
                (Button) thisDial.findViewById(R.id.button_sw),
                (Button) thisDial.findViewById(R.id.button_w),

                (Button) thisDial.findViewById(R.id.button_e_l),
                (Button) thisDial.findViewById(R.id.button_n_l),
                (Button) thisDial.findViewById(R.id.button_ne_l),
                (Button) thisDial.findViewById(R.id.button_nw_l),
                (Button) thisDial.findViewById(R.id.button_w_l)};
        int x = 0;
        //magic number 8: directions on top dial
        for( ; x < 8; x++) {
            final int finalX = x;
            mAllButtons[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uDialImage.setImageDrawable(ALL_NOBS[finalX]);
                }
            });
        }
        for( ; x < mAllButtons.length; x++) {
            final int finalX = x;
            mAllButtons[x].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    lDialImage.setImageDrawable(ALL_NOBS[finalX]);
                }
            });
        }


        return thisDial;
    }


    public void setButtons(boolean set){
        for(Button button : mAllButtons){
            button.setClickable(set);
        }
    }

}
