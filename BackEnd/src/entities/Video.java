/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author James
 */

public class Video {
    
private String name;
private String description;
private String publishDate;
private String longTitle;
private long duration;
private URL url;
private String slug;
private ArrayList<String> networks;
private String state;
private ArrayList<String> tags;
private ArrayList<Thumbnail> thumbnails;


    public Video(String name, String description, String publishDate, String longTitle,long duration,URL url,
            String state, String slug, ArrayList<String> networks, ArrayList<String> tags, ArrayList<Thumbnail> thumbnails) {
        this.name = name;
        this.description = description;
        this.longTitle = longTitle;
        this.publishDate = publishDate;
        this.duration = duration;
        this.url = url;
        this.state = state;
        this.slug = slug;
        this.networks = networks;
        this.tags = tags;
        this.thumbnails = thumbnails;
    }
    
     public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getLongTitle() {
        return longTitle;
    }
    
    public String getPubDate() {
        return publishDate;
    }
    
    public String getState() {
        return state;
    }
    
    public String getSlug() {
        return slug;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public URL getUrl() {
        return url;
    }
    
    public ArrayList<Thumbnail> getThumbnails() {
        ArrayList<Thumbnail> temp = new ArrayList<>();
        for (int i = 0; i < thumbnails.size(); i++) {
            temp.add(thumbnails.get(i));
        }
        return temp;
    }
    
    public ArrayList<String> getNetworks() {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < networks.size(); i++) {
            temp.add(networks.get(i));
        }
        return temp;
    }
    
    public ArrayList<String> getTags() {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            temp.add(tags.get(i));
        }
        return temp;
    }    
}
