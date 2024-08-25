package com.fit2081.assignment_2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.assignment_2.entities.EventCategory;
import com.fit2081.assignment_2.provider.DAO;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {

    ArrayList<EventCategory> data = new ArrayList<>();

    private final int HEADER_TYPE = 0;
    private final int ITEM_TYPE = 1;

    public void setData(ArrayList<EventCategory> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==HEADER_TYPE){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_header, parent, false); //CardView inflated as RecyclerView list item
        }
        else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false); //CardView inflated as RecyclerView list item

        }
        ViewHolder viewHolder = new ViewHolder(v);
        Log.d("Assignment-AK", "onCreateViewHolder");
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == 0){
            holder.textViewCategoryId.setText("Id");
            holder.textViewName.setText("Name");
            holder.textViewCount.setText("Event Count");
        } else {
            EventCategory category = data.get(position - 1);
            holder.textViewCount.setText(String.valueOf(category.getEventCount()));
            holder.textViewName.setText(String.valueOf(category.getName()));
            holder.textViewCategoryId.setText(category.getCategoryId());
            holder.isActive.setText(category.isActive() ? "Yes" : "No");
        }

        holder.cardview.setOnClickListener(v -> {
            int adjustedPosition = holder.getAdapterPosition() - 1; // Adjust position to account for the header
            if (adjustedPosition >= 0 && adjustedPosition < data.size()) {
                EventCategory selectedCategory = data.get(adjustedPosition);
                String categoryId = selectedCategory.getCategoryId();

                // getch event location for the selected category
                // replace this with your actual logic to fetch the event location
                String eventLocation = selectedCategory.getEventLocation();

                // launch MapsActivity with the location
                Context context = holder.cardview.getContext();
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("eventLocation", eventLocation);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (data == null) {
            return 0; // or return a default count
        }
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_TYPE;
        else
            return ITEM_TYPE;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCategoryId;
        public TextView textViewCount;
        public TextView textViewName;
        public TextView isActive;
        public TextView textEventLocation; // Add this TextView for event location

        public View cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardview = itemView;
            textViewCategoryId = itemView.findViewById(R.id.card_category_id);
            textViewCount = itemView.findViewById(R.id.card_event_count);
            textViewName = itemView.findViewById(R.id.card_name);
            isActive = itemView.findViewById(R.id.card_is_active);
        }
    }
}
