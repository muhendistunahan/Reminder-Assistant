package com.example.term_project.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * İnternetten gelen JSON verisini ayrıştırır (Exception Handling - OOP).
 */
public final class QuoteParser {

    private QuoteParser() {
    }

    public static String parseQuote(String json) {
        try {
            JSONObject object = new JSONObject(json);
            String content = object.getString("content");
            String author = object.getString("author");
            return "\"" + content + "\" — " + author;
        } catch (JSONException e) {
            return "Her gün küçük adımlar büyük değişimler yaratır.";
        }
    }
}
