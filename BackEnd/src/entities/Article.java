/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author James
 */

public class Article {
    
private String articleType;
private String subHeadline;
private String publishDate;
private String state;
private String slug;
private ArrayList<String> networks;
private String headline;
private ArrayList<Thumbnail> thumbnails;
private ArrayList<String> tags;
private URL url;

    public Article(String articleType, String headline, String subHeadline, String publishDate,
            String state, String slug, ArrayList<String> networks, ArrayList<String> tags, ArrayList<Thumbnail> thumbnails) {
        this.articleType = articleType;
        this.headline = headline;
        this.subHeadline = subHeadline;
        this.publishDate = publishDate;
        this.state = state;
        this.slug = slug;
        this.networks = networks;
        this.tags = tags;
        this.thumbnails = thumbnails;
    }
    
    public String getArticleType() {
        return articleType;
    }
    
    public String getHeadline() {
        return headline;
    }
    
    public String getSubHeadline() {
        return subHeadline;
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
    
    public URL getUrl() throws MalformedURLException {
        String temp = "http://www.ign.com/articles/";
        String[] getDate = this.publishDate.split("T");
        String replaceAll = getDate[0].replaceAll("-", "/");
        temp+=replaceAll;
        temp+="/";
        temp+=this.slug;
        return new URL(temp);
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
