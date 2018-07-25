package com.uvstudio.him.noteproject;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    SeekBar seekBar;
    TextView low,medium,high;
    EditText titleW,infoW;
    String priority="Medium";
    Button save,savepdf,savedata;
    SendData sendData;
    NoteDbHelper noteDbHelper;
    Context context;
    RelativeLayout layhide;
    ImgAnimator anim=new ImgAnimator();
    String tString,iString;
    public NoteEditFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_note_edit, container, false);
        save=(Button)view.findViewById(R.id.saveButton);
        noteDbHelper=new NoteDbHelper(getActivity());
        low=(TextView)view.findViewById(R.id.lowText);
        medium=(TextView)view.findViewById(R.id.mediumText);
        high=(TextView)view.findViewById(R.id.highText);
        titleW=(EditText)view.findViewById(R.id.titleEditText);
        infoW=(EditText)view.findViewById(R.id.infoEditText);
        layhide= (RelativeLayout) view.findViewById(R.id.hideview);
        savepdf= (Button) view.findViewById(R.id.savepdf);
        savedata= (Button) view.findViewById(R.id.savedatabase);
        layhide.setVisibility(View.INVISIBLE);

        low.setVisibility(View.VISIBLE);
        high.setVisibility(View.VISIBLE);
        medium.setVisibility(View.VISIBLE);
        medium.setTextColor(Color.YELLOW);
        seekBar=(SeekBar)view.findViewById(R.id.seekBar);
        seekBar.setProgress(0);
        seekBar.incrementProgressBy(1);
        seekBar.setMax(2);
        final RippleBackground rippleBackground= (RippleBackground) view.findViewById(R.id.ripple_effect);
        final RippleBackground rippleBackground2= (RippleBackground) view.findViewById(R.id.ripple_effect_2);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (i){
                    case 0:
                        low.setTextColor(Color.GREEN);
                        medium.setTextColor(Color.LTGRAY);
                        high.setTextColor(Color.LTGRAY);
                        priority="Low";
                    break;
                    case 1:
                        medium.setTextColor(Color.YELLOW);
                        low.setTextColor(Color.LTGRAY);
                        high.setTextColor(Color.LTGRAY);
                        priority="Medium";
                    break;
                    case 2:
                        high.setTextColor(Color.RED);
                        medium.setTextColor(Color.LTGRAY);
                        low.setTextColor(Color.LTGRAY);
                        priority="High";
                    break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
       save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleBackground.startRippleAnimation();
                rippleBackground2.startRippleAnimation();
             anim.enterReveal(layhide,(int)save.getX(),(int)save.getY());
            }
        });

       savepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim.exitReveal(layhide,(int)v.getX(),(int)v.getY());
                rippleBackground2.stopRippleAnimation();
                rippleBackground.stopRippleAnimation();
                Toast.makeText(context,"Saved as PDF",Toast.LENGTH_SHORT).show();
            }
        });

       savedata.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              try {
                  tString = titleW.getText().toString();
                  iString = infoW.getText().toString();
                  sendData.setNote(tString, iString, priority);
                  FragmentManager fragmentManager = getFragmentManager();
                  fragmentManager.popBackStackImmediate();

                  Toast.makeText(context, "Saved in Database", Toast.LENGTH_SHORT).show();
                  rippleBackground.stopRippleAnimation();
                  rippleBackground2.stopRippleAnimation();
                  anim.exitReveal(layhide, (int) v.getX(), (int) v.getY());
              }catch (Exception e){}
           }
       });
        touchlistener(layhide);
        return view;
    }


    public void touchlistener(View v)
    {
        v.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getActionMasked()==MotionEvent.ACTION_UP)
                {
                    anim.exitReveal(layhide,(int)v.getX(),(int)v.getY());
                }
                return true;
            }
        });
    }



    public interface SendData{
        public void setNote(String t,String i,String p);

    }

    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);
        try{
            sendData=(SendData)context;

        }catch (Exception e){

        }
    }
}
