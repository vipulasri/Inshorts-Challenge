package com.github.vipul.inshortschallenge.utils;

import com.github.vipul.inshortschallenge.model.News;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

/**
 * Created by Vipul on 17/09/17.
 */

public class DateComparator extends Ordering<News> {
    public int compare(News left, News right) {
        return (int) (left.getTimestamp() - right.getTimestamp());
    }
}
