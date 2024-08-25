package com.fit2081.assignment_2.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit2081.assignment_2.entities.Event;
import com.fit2081.assignment_2.entities.EventCategory;

import java.util.List;


/**
 * ViewModel class is used for pre-processing the data,
 * before passing it to the controllers (Activity or Fragments). ViewModel class should not hold
 * direct reference to database. ViewModel class relies on repository class, hence the database is
 * accessed using the Repository class.
 */
public class ViewModel extends AndroidViewModel {
    // reference to CardRepository
    private DataRepository repository;
    private DataRepository repository1;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Event>> allEventsLiveData;
    private LiveData<List<EventCategory>> allEventCategoriesLiveData;

    public ViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new DataRepository(application);
        repository1 = new DataRepository(application);

        // get all items by calling method defined in repository class
        allEventsLiveData = repository.getAllEvents();
        allEventCategoriesLiveData = repository1.getAllEventCategories();
    }

    /**
     * ViewModel method to get all cards
     * @return LiveData of type List<Item>
     */
    public LiveData<List<Event>> getAllEventsLiveData() {
        return allEventsLiveData;
    }

    public LiveData<List<EventCategory>> getAllEventCategoriesLiveData() {
        return allEventCategoriesLiveData;
    }

    /**
     * ViewModel method to insert one single item,
     * usually calling insert method defined in repository class
     *
     * @param event object containing details of new Item to be inserted
     * @return
     */
    public int insertEvents(Event event) {
        repository.insertEvent(event);
        return 0;
    }

    public void deleteAllEvents(){
        repository.deleteAllEvents();
    }

    public void deleteRecentEvent(){
        repository.deleteMostRecentEvent();
    }

    public void insertEventCategories(EventCategory eventCategory) {
        repository1.insertEventCategory(eventCategory);
    }

    public void deleteAllEventCategories(){
        repository1.deleteAllEventCategories();
    }

    // method to update an existing event category
    public void updateEventCategory(EventCategory eventCategory) {
        repository.updateEventCategory(eventCategory);
    }

    // method to retrieve an event category by ID
    public LiveData<EventCategory> getEventCategoryById(String categoryId) {
        return repository.getEventCategoryById(categoryId);
    }

}
