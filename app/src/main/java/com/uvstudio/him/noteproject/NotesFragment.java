package com.uvstudio.him.noteproject;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {
    Context context;
    NoteDbHelper noteDbHelper;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    Note note;
    UpdateData updateData;
    ImageButton imageButton;
    List<Note> noteList=new ArrayList<>();


    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notes, container, false);
        noteDbHelper=new NoteDbHelper(getActivity());
        note=new Note();

        Log.d(TAG, "Inserting: ");
     //   noteDbHelper.addNote(new Note("Second note","This is a sample info","Low"));
     //   noteDbHelper.addNote(new Note("Third note","This is a sample info","Medium"));
        Log.d(TAG, "Deleting: ");

        Log.d(TAG, "Reading notes: ");
        Log.d(TAG, "Updating notes: ");
       // noteDbHelper.updateNote(new Note(3,"Third note","New updated note","Low"));
      //  noteDbHelper.getOneNote(3);
        final List<Note> noteList=noteDbHelper.getNOtesList();
        for(Note n:noteList){
            String log ="Id="+n.getId()+" TITLE="+n.getTitle()+" INFO="+n.getInfo()+" PRIORITY="+n.getPriority();
            Log.d(TAG, "NOTE: "+log);
        }
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        imageButton=(ImageButton)view.findViewById(R.id.imagebutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new NoteEditFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        notesAdapter=new NotesAdapter(getActivity(),noteList, new ItemClickListener() {
            @Override
            public void onClick(View view, int position,boolean isLongCLick) {
                int id=noteList.get(position).getId();
                note=noteDbHelper.getOneNote(id);
                String t,in,p;
                int i=note.getId();
                t=note.getTitle();
                in=note.getInfo();
                p=note.getPriority();
                Log.d(TAG, "T,I,In,P  "+i+t+in+p);
               try{
                   Log.d(TAG, "Sending data for editing: ");
                   FragmentManager fragmentManager = getFragmentManager();
                   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                   Fragment fragment = new UpdateFragment();
                   Bundle bundle=new Bundle();
                   bundle.putInt("id",i);
                   bundle.putString("title",t);
                   bundle.putString("info",in);
                   bundle.putString("priority",p);
                   fragment.setArguments(bundle);
                   fragmentTransaction.replace(R.id.fragment_container, fragment,"Update");
                   fragmentTransaction.addToBackStack(null);
                   fragmentTransaction.commit();
                   updateData.uNote();

               }
               catch (Exception e){
                   Toast.makeText(getActivity(), note.getInfo(), Toast.LENGTH_SHORT).show();
               }

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(notesAdapter);
        Log.d(TAG, "count:"+notesAdapter.getItemCount());
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback( ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                note=noteDbHelper.getOneNote(noteList.get(viewHolder.getAdapterPosition()).getId());
                                noteDbHelper.deleteNote(note);
                                recyclerView.invalidate();
                            //    recyclerView.removeViewAt(viewHolder.getAdapterPosition());
                                notesAdapter.notifyDataSetChanged();
                              //  notesAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                notesAdapter.notifyItemRangeChanged(viewHolder.getAdapterPosition(),noteList.size());
                                Fragment frg = null;
                                frg = getFragmentManager().findFragmentByTag("One");
                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commit();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                notesAdapter.notifyDataSetChanged();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return view;
    }
    public void update(String T,String I,String P){
        noteDbHelper.addNote(new Note(T,I,P));
    }

    public interface UpdateData{
        public void uNote();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            updateData=(UpdateData)context;

        }catch (Exception e){
            Toast.makeText(context, "NOTES Frag", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateDatabase(int id,String title,String info,String priority){
        noteDbHelper.updateNote(new Note(id,title,info,priority));
    }

}
