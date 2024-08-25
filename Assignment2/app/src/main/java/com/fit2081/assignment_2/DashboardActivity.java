package com.fit2081.assignment_2;

import static com.fit2081.assignment_2.Utils.TAG_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.assignment_2.entities.Event;
import com.fit2081.assignment_2.entities.EventCategory;
import com.fit2081.assignment_2.provider.ViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DashboardActivity extends AppCompatActivity implements IMessage{
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    Toolbar toolbar;

    EditText editTextEventId;
    EditText editTextEventName;
    EditText editTextCategoryId;
    Switch isActive;

    EditText editTextTicketsAvailable;

    SharedPreferences sP;

    ArrayList<Event> events;
    Gson gson = new Gson();


    ArrayList<EventCategory> eventCategories;
    String eventCategoriesString, eventString;

    ViewModel viewModel;

    private GestureDetector gestureDetector;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_main);

        editTextEventId = findViewById(R.id.editTextEventId);
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextCategoryId = findViewById(R.id.editTextCategoryId);
        isActive = findViewById(R.id.isActive);
        editTextTicketsAvailable = findViewById(R.id.editTextTicketsAvailable);
        sP = getSharedPreferences(Keys.FILE_NAME, MODE_PRIVATE);

        eventCategoriesString = sP.getString(Keys.CATEGORY_LIST, "[]");
        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        eventCategories = gson.fromJson(eventCategoriesString, type);

        eventString = sP.getString(Keys.EVENT_LIST, "[]");
        Type type2 = new TypeToken<ArrayList<Event>>() {}.getType();
        events = gson.fromJson(eventString, type2);

        ActivityCompat.requestPermissions(this, new String[]{"android.permission.SEND_SMS",
                "android.permission.RECEIVE_SMS", "android.permission.READ_SMS"}, 0);
        SMSReceiver.bindListener(this);

        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            saveRecordSave();
        });

        // Initialize the GestureDetector
        gestureDetector = new GestureDetector(this, new GestureListener());

        // Find the touchpad view
        View touchpadView = findViewById(R.id.touchpad);

        // Set a touch listener to the touchpad view
        touchpadView.setOnTouchListener((v, event) -> {
            // Pass the touch event to the GestureDetector
            gestureDetector.onTouchEvent(event);
            return true;
        });

        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getAllEventCategoriesLiveData().observe(this, new Observer<List<EventCategory>>() {
            @Override
            public void onChanged(List<EventCategory> eventCategories) {
                Log.d(TAG_KEY, "Event categories list changed: " + eventCategories);
            }
        });

        loadData();
    }

    // Custom GestureListener class to handle gestures
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // Handle double tap gesture
            saveRecordSave(); // Call your method to save new event record
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // Handle long press gesture
            clearFields(); // Call your method to clear all fields
        }
    }

    private void loadData(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, FragmentListCategory.newInstance("", ""))
                .commit();
    }

    private void clearFields(){
        updateFields("", "", "", false);
        editTextEventId.setText("");
    }

    private void deleteEvents(){
        viewModel.deleteAllEvents();
        loadData();
    }

    private void deleteCategories(){
        viewModel.deleteAllEventCategories();
        loadData();
    }



    @Override
    public void messageReceived(String message) {
        Log.d("Assignment_1_TAG", "MessageReceived: " + message);

        try {
            int commandIndex = message.indexOf(':');
            String command = message.substring(0, commandIndex);
            String params = message.substring(commandIndex + 1);
            StringTokenizer sT = new StringTokenizer(params, ";");
            if (command.equalsIgnoreCase("event")) {
                String eventName = sT.nextToken();
                String categoryId = sT.nextToken();
                String ticketsAvailable = sT.nextToken();
                String isActiveSt = sT.nextToken();
                if (isActiveSt.equalsIgnoreCase("TRUE") || isActiveSt.equalsIgnoreCase("FALSE")) {
                    boolean isActive = isActiveSt.equalsIgnoreCase("TRUE");
                    updateFields(eventName, categoryId, ticketsAvailable, isActive);
                } else {
                    throw new Exception("Invalid Format");
                }
            } else {
                Toast.makeText(this, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
        }

    }

    private EventCategory findCategoryById(String categoryId) {
        Log.d(TAG_KEY, "Searching for: " + categoryId);

        // retrieve the list of EventCategory objects from the ViewModel
        List<EventCategory> eventCategories = viewModel.getAllEventCategoriesLiveData().getValue();

        if (eventCategories != null) {
            Log.d(TAG_KEY, "eventCategories size: " + eventCategories.size());

            // search for the category by ID within the list
            for (EventCategory e : eventCategories) {
                Log.d(TAG_KEY, "CategoryID: " + e.getCategoryId());
                if (e.getCategoryId().equalsIgnoreCase(categoryId)) {
                    return e;
                }
            }
        } else {
            Log.d(TAG_KEY, "Event categories list is null");
        }

        return null;
    }

    private void saveRecordSave() {

        String eventName = editTextEventName.getText().toString();
        String categoryId = editTextCategoryId.getText().toString();
        if (Utils.isNumeric(eventName) || !Utils.isAlphaNumeric(eventName)){
            Toast.makeText(this, "Invalid event name", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Integer.parseInt(editTextTicketsAvailable.getText().toString());
        } catch (Exception e){
            Toast.makeText(this, "Tickets available, valid integer value expected", Toast.LENGTH_SHORT).show();
            return;
        }

        // check category Id exists
        EventCategory categoryById = findCategoryById(categoryId);
        if (categoryById == null){
            Toast.makeText(this, "Category does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        // increment the event count for the category
        categoryById.setEventCount(categoryById.getEventCount() + 1);
        viewModel.updateEventCategory(categoryById);

        // create the new event
        Event newEvent = new Event(
                eventName,
                categoryId,
                Integer.parseInt(editTextTicketsAvailable.getText().toString()),
                isActive.isChecked());


        // save the new event in the database
        viewModel.insertEvents(newEvent);


        //String categoryId = editTextCategoryId.getText().toString();
        String eventIdId = Utils.generateEventId();

        editTextEventId.setText(eventIdId);

        Toast.makeText(this, "Event saved: " + eventIdId + " to "+ categoryId, Toast.LENGTH_SHORT).show();

        // show the Snackbar with UNDO action
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, "Event saved", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", v -> {
            // Perform the undo action
            viewModel.deleteRecentEvent(); // Delete the event that was just added
            // Show a message indicating that the undo action was successful
            Toast.makeText(DashboardActivity.this, "Event undone", Toast.LENGTH_SHORT).show();
        });
        snackbar.show();


        loadData();
    }

    private void updateFields(String _eventName, String _categoryId, String _ticketsAvailable, boolean _isActive) {
        editTextEventName.setText(_eventName);
        editTextCategoryId.setText(_categoryId);
        editTextTicketsAvailable.setText(_ticketsAvailable);
        isActive.setChecked(_isActive);
    }

    public void onEventBtnClick(View view){
        Intent intent=new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    public void onCategoryBtnClick(View view){
        Intent intent=new Intent(this, EventCategoryActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.option_menu_refresh) {
            loadData();
        }
        else if (id == R.id.option_menu_clear) {
            clearFields();
        } else if (id == R.id.option_menu_del_cat){
            deleteCategories();
        } else if (id == R.id.option_menu_del_events){
            deleteEvents();
        }
        return true;
    }

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();
            if (id == R.id.nav_menu_add_category) {
                onCategoryBtnClick(null);
            } else if (id == R.id.nav_menu_all_categories) {
                Intent intent = new Intent(getApplicationContext(), ListCategoryActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_menu_all_events) {
                Intent intent = new Intent(getApplicationContext(), ListEventActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_menu_logout) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }

            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }
}