package com.fit2081.assignment_2.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fit2081.assignment_2.entities.Event;
import com.fit2081.assignment_2.entities.EventCategory;

import java.util.List;

public class DataRepository {

    // private class variable to hold reference to DAO
    private DAO dao;
    private DAO dao1;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Event>> allEventsLiveData;
    private LiveData<List<EventCategory>> allEventCategoriesLiveData;

    // constructor to initialise the repository class
    DataRepository(Application application) {
        // get reference/instance of the database
        EMADatabase db = EMADatabase.getDatabase(application);

        // get reference to DAO, to perform CRUD operations
        dao = db.dao();
        // get reference to DAO, to perform CRUD operations
        dao1 = db.dao();

        // once the class is initialised get all the items in the form of LiveData
        allEventsLiveData = dao.getAllEvents();
        allEventCategoriesLiveData = dao1.getAllEventCategories();
    }

    /**
     * Repository method to get all events
     * @return LiveData of type List<Event>
     */
    LiveData<List<Event>> getAllEvents() {
        return allEventsLiveData;
    }

    LiveData<List<EventCategory>> getAllEventCategories() {
        return allEventCategoriesLiveData;
    }

    /**
     * Repository method to insert one single item
     * @param event object containing details of new Item to be inserted
     */
    void insertEvent(Event event) {
        EMADatabase.databaseWriteExecutor.execute(() -> dao.addEvent(event));
    }

    void deleteAllEvents(){
        EMADatabase.databaseWriteExecutor.execute(() -> dao.deleteAllEvents());
    }

    public void deleteMostRecentEvent() {
        EMADatabase.databaseWriteExecutor.execute(() -> dao.deleteMostRecentEvent());
    }

    void insertEventCategory(EventCategory eventCategory) {
        EMADatabase.databaseWriteExecutor.execute(() -> dao1.addEventCategory(eventCategory));
    }

    void deleteAllEventCategories() {
        EMADatabase.databaseWriteExecutor.execute(() -> dao1.deleteAllEventCategories());
    }

    public void incrementCategoryCount(String categoryId) {
        EMADatabase.databaseWriteExecutor.execute(() -> dao.incrementCategoryCount(categoryId));
    }

    public void decrementCategoryCount(String categoryId) {
        EMADatabase.databaseWriteExecutor.execute(() -> dao.decrementCategoryCount(categoryId));
    }

    // uses this to update the event count
    void updateEventCategory(EventCategory eventCategory) {
        EMADatabase.databaseWriteExecutor.execute(() -> dao.updateEventCategory(eventCategory));
    }

    LiveData<EventCategory> getEventCategoryById(String categoryId) {
        return dao.getEventCategoryById(categoryId);
    }
}
