package com.company;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Document implements Serializable {
    private String id;
    private String name;
    private String location; //file name or Web page

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Document() {
    }

    private Map<String, Object> tags = new HashMap<>();

    public Map<String, Object> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public Document(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Document(String id, String name, String location, Map<String, Object> tags) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.tags = tags;
    }

    public void setTags(Map<String, Object> tags) {
        this.tags = tags;
    }

    public String getLocation() {
        return location;
    }

    public void addTag(String key, Object obj) {
        tags.put(key, obj);
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "\n  Document{" +
                "id:'" + id + '\'' +
                ",name:'" + name + '\'' +
                ",location:'" + location + '\'' +
                ",tags:" + tags +
                "}";
    }
}


