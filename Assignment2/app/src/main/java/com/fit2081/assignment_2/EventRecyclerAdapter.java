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

import com.fit2081.assignment_2.entities.Event;
import com.fit2081.assignment_2.entities.EventCategory;
import com.fit2081.assignment_2.provider.ViewModel;

import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    ArrayList<Event> data = new ArrayList<>();
    private ViewModel viewModel;
    public EventRecyclerAdapter(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_event, parent, false); //CardView inflated as RecyclerView list item


        ViewHolder viewHolder = new ViewHolder(v);

        Log.d("Assignment-AK", "onCreateViewHolder");
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the views
        Event event = data.get(position);
        holder.textViewId.setText(event.getEventId());
        holder.textViewTickets.setText(String.valueOf(event.getTicketsAvailable()));
        holder.textViewName.setText(event.getName());
        holder.textViewCategoryId.setText(event.getCategoryId());
        holder.isActive.setText(event.isActive() ? "Active" : "InActive");

        // Set click listener for the card
        holder.cardview.setOnClickListener(v -> {
            // Get the clicked event
            Event clickedEvent = data.get(holder.getAdapterPosition());

            // Check if the event is not null
            if (clickedEvent != null) {
                // Retrieve the event name
                String eventName = clickedEvent.getName();

                // Launch EventGoogleResult activity with the event name
                Context context = holder.cardview.getContext();
                Intent intent = new Intent(context, EventGoogleResult.class);
                intent.putExtra("eventName", eventName);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewId;
        public TextView textViewCategoryId;
        public TextView textViewTickets;
        public TextView textViewName;
        public TextView isActive;
        public View cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardview = itemView;
            textViewId = itemView.findViewById(R.id.card_event_id);
            textViewCategoryId = itemView.findViewById(R.id.card_category_id);
            textViewTickets = itemView.findViewById(R.id.card_event_tickets);
            textViewName = itemView.findViewById(R.id.card_event_name);
            isActive = itemView.findViewById(R.id.card_is_active);
        }
    }
}
