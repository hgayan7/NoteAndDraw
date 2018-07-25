package com.uvstudio.him.noteproject;

import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by him on 8/26/2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyNotesHolder> {
    private List<Note> noteList;
    RecyclerView recyclerView;
    Note note;
    private ItemClickListener itemClickListener;
   Context context;


    public class MyNotesHolder extends RecyclerView.ViewHolder{
        public TextView title,info;
        public ImageView priority;;
        public MyNotesHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.titleTextView);
            info=(TextView)itemView.findViewById(R.id.infoTextView);
            priority=(ImageView) itemView.findViewById(R.id.priorityTextView);
            recyclerView=(RecyclerView)itemView.findViewById(R.id.recycler_view);
            note=new Note();

        }
    }
    public NotesAdapter(FragmentActivity activity, List<Note> noteList,ItemClickListener itemClickListener)
    {
        this.noteList=noteList;
        this.itemClickListener=itemClickListener;
    }
    @Override
    public MyNotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card,parent,false);
       final MyNotesHolder myNotesHolder=new MyNotesHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(view,myNotesHolder.getPosition(),false);
            }
        });
        return myNotesHolder;
    }

    @Override
    public void onBindViewHolder(MyNotesHolder holder, int position) {
        Note note=noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.info.setText(note.getInfo());
        String priority=note.getPriority();
        if(priority.equals("High"))
            {
                holder.priority.setBackgroundColor(Color.RED);
            }
        else if(priority.equals("Medium"))
            {
                holder.priority.setBackgroundColor(Color.YELLOW);
            }
        else {
                holder.priority.setBackgroundColor(Color.GREEN);
            }

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

}
