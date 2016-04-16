package com.richdroid.masterextempore.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by richa.khanna on 3/23/16.
 */
public class AllContactResponse {

    @SerializedName("all_contacts")
    private ArrayList<ContactDetail> allContactDetails;

    public ArrayList<ContactDetail> getAllContactDetails() {
        return allContactDetails;
    }
}
