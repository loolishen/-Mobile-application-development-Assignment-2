package com.fit2081.assignment_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.assignment_2.entities.EventCategory;
import com.fit2081.assignment_2.provider.ViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class EventCategoryActivity extends AppCompatActivity implements IMessage {

    // CURRENTLY THE ISSUE IS THAT EITHER THE CATEGORYID IS NOT BEING SAVED PROPERLY OR NOT READ PROPERLY IN DASHBOARD

    EditText editTextCategoryId, editTextCategoryName, editTextEventCount, editTextCategoryLocationNew;
    Switch isActive;


    ArrayList<EventCategory> eventCategories;
    String eventCategoriesString;

    SharedPreferences sP;
    Gson gson = new Gson();

    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        editTextCategoryId = findViewById(R.id.editTextCategoryId);
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        isActive = findViewById(R.id.isActive);
        editTextEventCount = findViewById(R.id.editTextEventCount);

        editTextCategoryLocationNew = findViewById(R.id.editTextCategoryLocation);

        viewModel = new ViewModelProvider(this).get(ViewModel.class);


        sP = getSharedPreferences(Keys.FILE_NAME, MODE_PRIVATE);

        eventCategoriesString = sP.getString(Keys.CATEGORY_LIST, "[]");
        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        eventCategories = gson.fromJson(eventCategoriesString, type);

//        eventCategories =
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.SEND_SMS",
                "android.permission.RECEIVE_SMS", "android.permission.READ_SMS"}, 0);
        SMSReceiver.bindListener(this);
    }

    private void updateFields(String _categoryName, String _eventCount, boolean _isActive, String _categoryLocation) {
        editTextCategoryName.setText(_categoryName);
        editTextEventCount.setText(_eventCount);
        isActive.setChecked(_isActive);
        editTextCategoryLocationNew.setText(_categoryLocation);
    }

    @Override
    public void messageReceived(String message) {
        Log.d("Assignment_1_TAG", "MessageReceived: " + message);

        try {
            int commandIndex = message.indexOf(':');
            String command = message.substring(0, commandIndex);
            String params = message.substring(commandIndex + 1);
            StringTokenizer sT = new StringTokenizer(params, ";");
            if (command.equals("category")) {
                String categoryName = sT.nextToken();
                String eventCount = sT.nextToken();
                String isActiveSt = sT.nextToken();
                String categoryLocation = sT.nextToken();

                if (!Utils.isNumeric(eventCount)) {
                    throw new Exception("Invalid Format");
                }

                if (isActiveSt.equals("TRUE") || isActiveSt.equals("FALSE")) {
                    boolean isActive = isActiveSt.equals("TRUE");
                    updateFields(categoryName, eventCount, isActive, categoryLocation);
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

    public void saveRecordSave() {
        String categoryName = editTextCategoryName.getText().toString();
        if (Utils.isNumeric(categoryName) || !Utils.isAlphaNumeric(categoryName)){
            Toast.makeText(this, "Invalid category name", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Integer.parseInt(editTextEventCount.getText().toString());
        } catch (Exception e){
            Toast.makeText(this, "Event count, valid integer value expected", Toast.LENGTH_SHORT).show();
            return;
        }

        String editTextCategoryLocationString = editTextCategoryLocationNew.getText().toString();

//        // Generate a unique category ID
//        String categoryId = Utils.generateCategoryId();

        // Create a new EventCategory instance with the generated category ID
        EventCategory newEventC = new EventCategory(
                categoryName,
                Integer.parseInt(editTextEventCount.getText().toString()),
                isActive.isChecked(),
                editTextCategoryLocationString);

        editTextCategoryId.setText(newEventC.getCategoryId());

//        // Add the new EventCategory to the list
//        eventCategories.add(newEventC);

//        // Save the updated list of EventCategories into SharedPreferences
//        SharedPreferences.Editor editor = sP.edit();
//        editor.putString(Keys.CATEGORY_LIST, gson.toJson(eventCategories));
//        editor.apply();

        // Save the new EventCategory into the database
        viewModel.insertEventCategories(newEventC);

        Toast.makeText(this, "Category saved successfully: " + newEventC.getCategoryId(), Toast.LENGTH_SHORT).show();

        finish();
    }

}