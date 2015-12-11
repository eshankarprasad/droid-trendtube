package org.trendtube.app.model;

import java.io.Serializable;

/**
 * Created by ankitgarg on 24/2/14.
 */
public class BasicListViewItem implements Serializable {

  private String mId;
  private String mValue;
  private int mPostion;

  public int getPostion() {
    return mPostion;
  }

  public void setPostion(int mPostion) {
    this.mPostion = mPostion;
  }

  public BasicListViewItem(String id, String value) {
    mId = id;
    mValue = value;
    mPostion = -1;
  }

  public String getId() {
    return mId;
  }

  public void setId(String mId) {
    this.mId = mId;
  }

  public String getValue() {
    return mValue;
  }

  public void setValue(String mValue) {
    this.mValue = mValue;
  }

  @Override
  public String toString() {
    return "BasicListViewItem{" +
        "mId='" + mId + '\'' +
        ", mValue='" + mValue + '\'' +
        ", mPostion=" + mPostion +
        '}';
  }
}
