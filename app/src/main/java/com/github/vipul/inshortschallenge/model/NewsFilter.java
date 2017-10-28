package com.github.vipul.inshortschallenge.model;

import java.util.List;

/**
 * Created by Vipul on 17/09/17.
 */

public class NewsFilter {

    private List<String> mCategories;
    private List<String> mPublishers;

    public List<String> getCategories() {
        return mCategories;
    }

    public void setCategories(List<String> categories) {
        this.mCategories = categories;
    }

    public List<String> getPublishers() {
        return mPublishers;
    }

    public void setPublishers(List<String> publishers) {
        this.mPublishers = publishers;
    }
}
