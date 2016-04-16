package com.richdroid.masterextempore.model;

import java.util.ArrayList;

/**
 * Created by harshikesh.kumar on 16/04/16.
 */
public class Topic {

  private String id;
  private String topicName;
  private String level;
  private String category;
  ArrayList<String> links;

  public ArrayList<String> getLinks() {
    return links;
  }

  public String getId() {
    return id;
  }

  public String getTopicName() {
    return topicName;
  }

  public String getLevel() {
    return level;
  }

  public String getCategory() {
    return category;
  }

}
