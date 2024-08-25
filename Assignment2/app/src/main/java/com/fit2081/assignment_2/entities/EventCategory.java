package com.fit2081.assignment_2.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fit2081.assignment_2.Utils;


@Entity(tableName = "eventcategories")
public class EventCategory {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private  int id;
    private String eventLocation;
    private String name;
    private String categoryId;
    private int eventCount;
    private boolean isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public EventCategory(String name, int eventCount, boolean isActive, String eventLocation) {
        this.categoryId = Utils.generateCategoryId();
        this.name = name;
        this.eventCount = eventCount;
        this.isActive = isActive;
        this.eventLocation = eventLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
