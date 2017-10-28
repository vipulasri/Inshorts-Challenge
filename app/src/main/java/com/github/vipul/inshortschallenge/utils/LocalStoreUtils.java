package com.github.vipul.inshortschallenge.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocalStoreUtils {
    private static GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Gson gson = gsonBuilder.create();

    private static final String PREF_FILE_NAME = "com.github.vipul.inshortschallenge";
    private static final String KEY_NEWS_BOOKMARK = "news_bookmarked";

    public static void addToBookmarks(final Context context, String newsId) {
        SharedPreferences sp = null;
        try {
            sp = getSharedPreference(context);
            Set<String> set = sp.getStringSet(KEY_NEWS_BOOKMARK, null);
            if (set == null) set = new HashSet<>();
            set.add(newsId);

            SharedPreferences.Editor editor = getSharedEditor(context);
            editor.clear();

            editor.putStringSet(KEY_NEWS_BOOKMARK, set).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeFromBookmarks(final Context context, String newsId) {
        SharedPreferences sp = null;
        try {
            sp = getSharedPreference(context);
            Set<String> set = sp.getStringSet(KEY_NEWS_BOOKMARK, null);
            if (set == null) set = new HashSet<>();
            set.remove(newsId);

            SharedPreferences.Editor editor = getSharedEditor(context);
            editor.clear();

            editor.putStringSet(KEY_NEWS_BOOKMARK, set).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean hasInBookmarks(final Context context, String newsId) {
        SharedPreferences sp = null;
        boolean isBookmarked = false;
        try {
            sp = getSharedPreference(context);
            Set<String> set = sp.getStringSet(KEY_NEWS_BOOKMARK, null);
            if (set == null) set = new HashSet<>();
            isBookmarked = set.contains(newsId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isBookmarked;
    }

    public static List<String> getBookmarks(final Context context) {

        List<String> bookmarks = null;
        try {
            SharedPreferences pref = getSharedPreference(context);

            Set<String> set = pref.getStringSet(KEY_NEWS_BOOKMARK, Collections.EMPTY_SET);
            bookmarks = new ArrayList<>();

            if (set.isEmpty()) {
                bookmarks = null;
            } else {
                for (String s : set) {
                    String user = gson.fromJson(s, String.class);
                    bookmarks.add(user);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookmarks;
    }


    public static void clearSession(Context context) {
        try {
            SharedPreferences.Editor editor = getSharedEditor(context);
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SharedPreferences.Editor getSharedEditor(Context context)
            throws Exception {
        if (context == null) {
            throw new Exception("Context null Exception");
        }
        return getSharedPreference(context).edit();
    }

    private static SharedPreferences getSharedPreference(Context context)
            throws Exception {
        if (context == null) {
            throw new Exception("Context null Exception");
        }
        return context.getSharedPreferences(PREF_FILE_NAME, 0);
    }
}
