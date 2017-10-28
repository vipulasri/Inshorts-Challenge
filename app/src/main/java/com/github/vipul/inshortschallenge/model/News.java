package com.github.vipul.inshortschallenge.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.vipul.inshortschallenge.R;
import com.google.gson.annotations.SerializedName;

public class News implements Parcelable {

    @SerializedName("CATEGORY")
    private String mCategory;
    @SerializedName("HOSTNAME")
    private String mHostname;
    @SerializedName("ID")
    private String mId;
    @SerializedName("PUBLISHER")
    private String mPublisher;
    @SerializedName("TIMESTAMP")
    private Long mTimestamp;
    @SerializedName("TITLE")
    private String mTitle;
    @SerializedName("URL")
    private String mUrl;

    public News() {
    }

    public News(String mCategory, String mHostname, String mId, String mPublisher, Long mTimestamp, String mTitle, String mUrl) {
        this.mCategory = mCategory;
        this.mHostname = mHostname;
        this.mId = mId;
        this.mPublisher = mPublisher;
        this.mTimestamp = mTimestamp;
        this.mTitle = mTitle;
        this.mUrl = mUrl;
    }

    public String getCategory() {
        switch (mCategory) {
            case "b": return "Business";
            case "t": return "Science & Technology";
            case "e": return "Entertainment";
            case "m": return "Health";
            default: return mCategory;
        }
    }

    public void setCategory(String Category) {
        mCategory = Category;
    }

    public String getHostname() {
        return mHostname;
    }

    public void setHostname(String Hostname) {
        mHostname = Hostname;
    }

    public String getId() {
        return mId;
    }

    public void setId(String Id) {
        mId = Id;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public void setPublisher(String Publisher) {
        mPublisher = Publisher;
    }

    public Long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Long Timestamp) {
        mTimestamp = Timestamp;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String Title) {
        mTitle = Title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String Url) {
        mUrl = Url;
    }

    public int getCategoryColor(){
        switch (mCategory) {
            case "b": return R.color.colorPink500;
            case "t": return R.color.colorRed500;
            case "e": return R.color.colorBlue500;
            case "m": return R.color.colorPurple500;
            default: return R.color.colorGrey200;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCategory);
        dest.writeString(this.mHostname);
        dest.writeString(this.mId);
        dest.writeString(this.mPublisher);
        dest.writeValue(this.mTimestamp);
        dest.writeString(this.mTitle);
        dest.writeString(this.mUrl);
    }

    protected News(Parcel in) {
        this.mCategory = in.readString();
        this.mHostname = in.readString();
        this.mId = in.readString();
        this.mPublisher = in.readString();
        this.mTimestamp = (Long) in.readValue(Long.class.getClassLoader());
        this.mTitle = in.readString();
        this.mUrl = in.readString();
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
