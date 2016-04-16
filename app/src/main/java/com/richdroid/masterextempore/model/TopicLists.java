package com.richdroid.masterextempore.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by harshikesh.kumar on 16/04/16.
 */
public class TopicLists {

  @SerializedName("topics")
  private ArrayList<Topic> allTopic;

  public ArrayList<Topic> getAllTopic() {
    return allTopic;
  }
}
