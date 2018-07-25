package com.uvstudio.him.noteproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;



public class RecycleItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mlistener;
    GestureDetector mGesture;

    public interface OnItemClickListener{
        public void OnItemClick(View view, int position);
    }



    public RecycleItemClickListener(Context context,OnItemClickListener listener)
    {
        mlistener=listener;
        mGesture=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childview=rv.findChildViewUnder(e.getX(),e.getY());
        if(childview!=null && mlistener!=null && mGesture.onTouchEvent(e))
        {
            mlistener.OnItemClick(childview,rv.getChildAdapterPosition(childview));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
