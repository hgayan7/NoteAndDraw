package com.uvstudio.him.noteproject;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static android.content.ContentValues.TAG;



public class UpdateFragment extends Fragment implements TextToSpeech.OnInitListener{

    NoteDbHelper noteDbHelper;
    TextView low,medium,high;
    EditText titleW,infoW;
    SeekBar seekBar;
    String priority;
    int id;
    UpdateDATABASE updateDATABASE;
    Button editButton,updateButton;
    int initial;
    KeyListener keyListener,keyListener1;
    Button playButton;
    TextToSpeech textToSpeech;
    int flag=0;
    public UpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_update, container, false);
        noteDbHelper=new NoteDbHelper(getActivity());
        editButton=(Button)view.findViewById(R.id.editButton);
        updateButton=(Button)view.findViewById(R.id.updateButton);

        updateButton.setEnabled(false);
        titleW=(EditText)view.findViewById(R.id.titleEditText);
        infoW=(EditText)view.findViewById(R.id.infoEditText);
        low=(TextView)view.findViewById(R.id.lowText);
        medium=(TextView)view.findViewById(R.id.mediumText);
        high=(TextView)view.findViewById(R.id.highText);
        uNote();
        seekBar=(SeekBar)view.findViewById(R.id.seekBar);
        seekBar.setEnabled(false);
        seekBar.incrementProgressBy(1);
        seekBar.setMax(2);
        seekBar.setProgress(initial);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButton.setEnabled(true);
                infoW.setKeyListener(keyListener1);
                titleW.setKeyListener(keyListener);
                seekBar.setEnabled(true);
            }
        });
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
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDATABASE.updateDatabase(id,titleW.getText().toString(),infoW.getText().toString(),priority);
                FragmentManager fragmentManager = getFragmentManager();
                 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                  Fragment fragment = new NotesFragment();
                 fragmentTransaction.replace(R.id.fragment_container, fragment);
                 fragmentTransaction.commit();
            }
        });
        playButton=(Button)view.findViewById(R.id.speak);
        playButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                play();
            }
        });
        textToSpeech=new TextToSpeech(getActivity(),this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void uNote(){
        Log.d(TAG, "uNote: "+"Working");
        Bundle bundle=getArguments();
        if(bundle!=null){
            String title=bundle.getString("title");
            id=bundle.getInt("id");
            String info=bundle.getString("info");
            priority=bundle.getString("priority");
            if(priority.equals("Low")){
                initial=0;
            }
            if (priority.equals("Medium"))
            {
                initial=1;
            }
            if (priority.equals("High")){
                initial=2;
            }
            titleW.setText(title);
            infoW.setText(info);
            keyListener=titleW.getKeyListener();
            titleW.setKeyListener(null);
            keyListener1=infoW.getKeyListener();
            infoW.setKeyListener(null);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onInit(int i) {

        if (i == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                playButton.setEnabled(true);
                play();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }


    public interface UpdateDATABASE{
        public void updateDatabase(int i,String j,String k,String l);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            updateDATABASE=(UpdateDATABASE)context;

        }catch (Exception e){
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void play(){

            textToSpeech.setSpeechRate(0.6f);
            textToSpeech.speak("Title is,,,"+titleW.getText().toString() +"and"+",,,Note is,,,"+infoW.getText().toString(),
                    TextToSpeech.QUEUE_FLUSH,null);


    }

    @Override
    public void onDestroy() {
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
