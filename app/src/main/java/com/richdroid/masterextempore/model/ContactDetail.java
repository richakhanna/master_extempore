package com.richdroid.masterextempore.model;

/**
 * Created by richa.khanna on 3/18/16.
 */
public class ContactDetail {

    private String name;
    private String email;
    private String location;
    private String type;
    private boolean isToShowDeleteIcon;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public boolean isToShowDeleteIcon() {
        return isToShowDeleteIcon;
    }

    public void setToShowDeleteIcon(boolean toShowDeleteIcon) {
        isToShowDeleteIcon = toShowDeleteIcon;
    }
}
