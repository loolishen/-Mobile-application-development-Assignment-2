package com.fit2081.assignment_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.fit2081.assignment_2.entities.Event;
import com.fit2081.assignment_2.provider.ViewModel;

import java.util.StringTokenizer;


public class EventActivity extends AppCompatActivity implements IMessage {

    EditText editTextEventId;
    EditText editTextEventName;
    EditText editTextCategoryId;
    Switch isActive;

    EditText editTextTicketsAvailable;

    SharedPreferences sP;

    ViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        editTextEventId = findViewById(R.id.editTextEventId);
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextCategoryId = findViewById(R.id.editTextCategoryId);
        isActive = findViewById(R.id.isActive);
        editTextTicketsAvailable = findViewById(R.id.editTextTicketsAvailable);
        sP = getSharedPreferences(Keys.FILE_NAME, MODE_PRIVATE);

        ActivityCompat.requestPermissions(this, new String[]{"android.permission.SEND_SMS",
                "android.permission.RECEIVE_SMS", "android.permission.READ_SMS"}, 0);
        SMSReceiver.bindListener(this);
    }

    private void updateFields(String _eventName, String _categoryId, String _ticketsAvailable, boolean _isActive) {
        editTextEventName.setText(_eventName);
        editTextCategoryId.setText(_categoryId);
        editTextTicketsAvailable.setText(_ticketsAvailable);
        isActive.setChecked(_isActive);
    }

    @Override
    public void messageReceived(String message) {
        Log.d("Assignment_1_TAG", "MessageReceived: " + message);

        try {
            int commandIndex = message.indexOf(':');
            String command = message.substring(0, commandIndex);
            String params = message.substring(commandIndex + 1);
            StringTokenizer sT = new StringTokenizer(params, ";");
            if (command.equals("event")) {
                String eventName = sT.nextToken();
                String categoryId = sT.nextToken();
                String ticketsAvailable = sT.nextToken();
                String isActiveSt = sT.nextToken();
                if (isActiveSt.equals("TRUE") || isActiveSt.equals("FALSE")) {
                    boolean isActive = isActiveSt.equals("TRUE");
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

    public void onSaveClick(View view) {
        saveRecordSave();
    }

    private void saveRecordSave() {
        String categoryId = editTextCategoryId.getText().toString();
        String eventIdId = Utils.generateEventId();

        // validate category ID against the list of categories saved in the database
        viewModel.getEventCategoryById(categoryId).observe(this, category -> {
            if (category != null) {
                // ategory exists, proceed with saving the event record
                SharedPreferences.Editor editor = sP.edit();
                editor.putString(Keys.EVENT_ID, eventIdId);
                editor.putString(Keys.EVENT_NAME, editTextEventName.getText().toString());
                editor.putString(Keys.EVENT_CATEGORY_ID, categoryId);
                editor.putBoolean(Keys.EVENT_IS_ACTIVE, isActive.isChecked());
                editor.putInt(Keys.EVENT_TICKETS_AVAILABLE, Integer.parseInt(editTextTicketsAvailable.getText().toString()));
                editor.apply();

                editTextEventId.setText(eventIdId);

                String eventNameString = editTextEventName.getText().toString();

                String ticketsAvailableString = editTextTicketsAvailable.toString();
                int ticketsAvailableInt = Integer.parseInt(ticketsAvailableString);

                Event newEvent = new Event(eventNameString, categoryId, ticketsAvailableInt, isActive.isChecked());

                editTextEventId.setText(newEvent.getEventId());

                // Increment the value of EventCount by one for the specified category record
                category.setEventCount(category.getEventCount() + 1);
                viewModel.updateEventCategory(category);

                viewModel.insertEvents(newEvent);

                Toast.makeText(this, "Event saved: " + eventIdId + " to " + categoryId, Toast.LENGTH_SHORT).show();
            } else {
                // Category does not exist, display error message
                Toast.makeText(this, "Category does not exist", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    private void loadReceipt() {
//        editTextEventId.setText(sP.getString(Keys.ISSUER_NAME, ""));
//        editTextEventName.setText(sP.getString(Keys.BUYER_NAME, ""));
//        editTextCategoryId.setText(sP.getString(Keys.BUYER_ADDRESS, ""));
//        isActive.setChecked(sP.getBoolean(Keys.IS_PAID, false));
//        editTextTicketsAvailable.setText(sP.getInt(Keys.ITEM_QUANTITY, 0) + "");
//    }
}