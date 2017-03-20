/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.net.URL;

/**
 *
 * @author James
 */
public class Thumbnail {
    private final String size;
    private final long width;
    private final long height;
    private final URL url;
    
    public Thumbnail(String size, URL url, long width, long height) {
        this.size = size;
        this.url = url;
        this.width = width;
        this.height = height;
    }
    
    public URL getURL() {
        return url;
    }
    
    public String getSize() {
        return size;
    }
    
    public long getWidth() {
        return width;
    }
    
    public long getHeight() {
        return height;
    }
}
