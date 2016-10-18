package org.agoenka.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: agoenka
 * Created At: 10/16/2016
 * Version: ${VERSION}
 */

public class Video implements Serializable {

    private String id;
    private String key;
    private String name;
    private String type;
    private int size;

    private Video (JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("id");
        this.key = jsonObject.getString("key");
        this.name = jsonObject.getString("name");
        this.type = jsonObject.getString("type");
        this.size = jsonObject.getInt("size");
    }

    public static List<Video> fromJsonArray(JSONArray array) {
        List<Video> videos = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try {
                videos.add(new Video(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return videos;
    }

    public static Video findVideo (List<Video> videos, String type, int sequence) {
        List<Video> typedVideos = new ArrayList<>();
        for (Video video: videos) {
            if (type.equalsIgnoreCase(video.getType())) {
                typedVideos.add(video);
            }
        }

        if (sequence < typedVideos.size()) return typedVideos.get(sequence);
        else if (typedVideos.size() > 0) return typedVideos.get(0);
        else if (videos.size() > 0) return videos.get(0);
        else return null;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    private String getType() {
        return type;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public int getSize() {
        return size;
    }
}
