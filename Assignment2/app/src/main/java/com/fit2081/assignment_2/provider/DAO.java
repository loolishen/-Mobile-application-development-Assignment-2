package com.fit2081.assignment_2.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fit2081.assignment_2.entities.Event;
import com.fit2081.assignment_2.entities.EventCategory;

import java.util.List;

@Dao
public interface DAO {

    @Query("select * from events")
    LiveData<List<Event>> getAllEvents();

    @Insert
    void addEvent(Event event);

    @Query("select * from eventcategories")
    LiveData<List<EventCategory>> getAllEventCategories();

    @Insert
    void addEventCategory(EventCategory eventCategory);

    @Query("DELETE FROM eventcategories")
    void deleteAllEventCategories();

    @Query("DELETE FROM events")
    void deleteAllEvents();
    @Query("DELETE FROM events WHERE id = (SELECT MAX(id) FROM events)")
    void deleteMostRecentEvent();

    @Query("UPDATE eventcategories SET eventCount = eventCount + 1 WHERE categoryId = :categoryId")
    void incrementCategoryCount(String categoryId);

    @Query("UPDATE eventcategories SET eventCount = eventCount - 1 WHERE categoryId = :categoryId")
    void decrementCategoryCount(String categoryId);

    @Update
    void updateEventCategory(EventCategory eventCategory);

    @Query("SELECT * FROM eventcategories WHERE categoryId = :categoryId")
    LiveData<EventCategory> getEventCategoryById(String categoryId);
}