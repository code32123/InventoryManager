package com.example.inventorymanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private ArrayList<String> mCardNames;
    private ArrayList<String> mCardMods;
    private ArrayList<String> mCardQuantities;
    private ArrayList<String> mCardLocations;
    private ArrayList<TextView> ViewsToMatch;
    private int position;
    
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    public RecyclerViewAdapter(Context mContext, ArrayList<String> mCardNames, ArrayList<String> mCardMods, ArrayList<String> mCardQuantities, ArrayList<String> mCardLocations, ArrayList<TextView> ViewsToMatch) {
        this.mContext = mContext;
        this.mCardNames = mCardNames;
        this.mCardMods = mCardMods;
        this.mCardQuantities = mCardQuantities;
        this.mCardLocations = mCardLocations;
        this.ViewsToMatch = ViewsToMatch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
		Log.d(TAG, "onBindViewHolder: called: " + mCardNames.get(position));
    
        final String mCardName = mCardNames.get(position);
        final String mCardMod = mCardMods.get(position);
        final String mCardLocation = mCardLocations.get(position);
        final String mCardQuantity = mCardQuantities.get(position);
//        holder.mConstLayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Log.d(TAG, "onLongClick: LongClicked");
//                setPosition(holder.getPosition());
////                Toast.makeText(mContext, (String) "LONG", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//        holder.mConstLayout.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(TAG, "onTouch: touched on mCardName: " + mCardName);
//               if (ViewsToMatch.size() > 0) {ViewsToMatch.get(0).setText(mCardName);}
//               if (ViewsToMatch.size() > 1) {ViewsToMatch.get(1).setText(mCardMod);}
//               if (ViewsToMatch.size() > 2) {ViewsToMatch.get(2).setText(mCardLocation);}
//               if (ViewsToMatch.size() > 3) {ViewsToMatch.get(3).setText(mCardQuantity);}
//               Toast.makeText(mContext, (String) mCardName + ", " + mCardLocation, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
        holder.mConstLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.d(TAG, "onClick: clicked on mCardName: " + mCardNames.get(position));
               if (ViewsToMatch.size() > 0) {ViewsToMatch.get(0).setText(mCardName);}
               if (ViewsToMatch.size() > 1) {ViewsToMatch.get(1).setText(mCardMod);}
               if (ViewsToMatch.size() > 2) {ViewsToMatch.get(2).setText(mCardLocation);}
               if (ViewsToMatch.size() > 3) {ViewsToMatch.get(3).setText(mCardQuantity);}
               Toast.makeText(mContext, (String) mCardName + ", " + mCardLocation, Toast.LENGTH_SHORT).show();
           }
        });
        holder.mCardName.setText(mCardNames.get(position));
        holder.mCardMod.setText(mCardMods.get(position));
        holder.mCardQuantity.setText(mCardQuantities.get(position));
        holder.mCardLocation.setText(mCardLocations.get(position));
    }

    @Override
    public int getItemCount() {
        return mCardNames.size();
    }
    
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    
        CardView mCard;
        ConstraintLayout mConstLayout;
        TextView mCardName;
        TextView mCardMod;
        TextView mCardQuantity;
        TextView mCardLocation;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
    
    
            mCard = itemView.findViewById(R.id.cardView);
            mCardName = itemView.findViewById(R.id.CardName);
            mCardMod = itemView.findViewById(R.id.CardMod);
            mCardQuantity = itemView.findViewById(R.id.CardQuantity);
            mCardLocation = itemView.findViewById(R.id.CardLocation);
            mConstLayout = itemView.findViewById(R.id.ConstLayout);
    
            mConstLayout.setOnCreateContextMenuListener(this);
        }
        
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Log.d(TAG, "onCreateContextMenu: called");
            menu.setHeaderTitle("Select The Action");
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Delete");//groupId, itemId, order, title
            menu.add(this.getAdapterPosition(), v.getId(), 0, "Edit (Don't press)");
        }
    }
}
