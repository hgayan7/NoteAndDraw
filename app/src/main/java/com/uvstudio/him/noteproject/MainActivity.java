package com.uvstudio.him.noteproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements NoteEditFragment.SendData,NotesFragment.UpdateData,UpdateFragment.UpdateDATABASE{
    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  toolbar=(Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.buttonNavigation);
        bottomNavigationView.getBackground().setAlpha(150);
        bottomNavigationView.inflateMenu(R.menu.bottom_menu);
        fragmentManager=getSupportFragmentManager();
        fragment=new NotesFragment();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment,"One").commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                decide(item);
                return true;
            }
        });


    }

    public void decide(MenuItem item){

        int id=item.getItemId();
        switch (id){
            case R.id.first:
                fragment=new NotesFragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment,"One").addToBackStack(null).commit();
                break;

            case R.id.three:
                StoreDrawImages_frag frag=new StoreDrawImages_frag();
               // DrawFragment frag=new DrawFragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,frag,"Three").setCustomAnimations(R.anim.moveup,R.anim.movedown).addToBackStack(null).commit();
                break;
        }


    }


    @Override
    public void setNote(String title, String info, String priority) {
        try {
            NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager().findFragmentByTag("One");
            notesFragment.update(title, info, priority);

        } catch (Exception e) {
            Toast.makeText(this, "Heck", Toast.LENGTH_SHORT).show();
            Log.d("DRAW", e.getMessage());
        }
    }
    @Override
    public void uNote() {
        try{
            UpdateFragment updateFragment=(UpdateFragment)getSupportFragmentManager().findFragmentByTag("Update");
            updateFragment.uNote();
            Log.d(MainActivity.class.getSimpleName(), "uNote: "+"Main working");
        }catch (Exception e){
            Toast.makeText(this, "Main", Toast.LENGTH_SHORT).show();
            Log.d("Error", e.toString());
        }
    }


    @Override
    public void updateDatabase(int id,String a,String b,String c) {
        try {
            NotesFragment notesFragment=(NotesFragment)getSupportFragmentManager().findFragmentByTag("One");
            notesFragment.updateDatabase(id,a,b,c);

        }catch (Exception e){
            Toast.makeText(this, "Heck", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StoreDrawImages_frag frag= (StoreDrawImages_frag) getSupportFragmentManager().findFragmentByTag("Three");
        frag.onActivityResult(requestCode,resultCode,data);
    }


}
