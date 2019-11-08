/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easypodcastfeedcreator.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Model class for podcast entries (items) of a podcast feed
 *
 * @author a.dridi
 */
public class PodcastEntry implements Serializable {

    private String title;
    private String description;
    private String link;
    //Image URL
    private String url;
    //number of bytes
    private Integer length;
    private String type;
    private Date publishDate;
    private String summary;
    private String subtitle;
    //Duration in hh:mm format
    private String duration;
    //Same as Link of the podcast entry
    private String guid;

    public PodcastEntry(String title, String description, String link, String url, Integer length, String type, Date publishDate, String summary, String subtitle, String duration) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.url = url;
        this.length = length;
        this.type = type;
        this.publishDate = publishDate;
        this.summary = summary;
        this.subtitle = subtitle;
        this.duration = duration;
    }

     public PodcastEntry() {
        this.title = "";
        this.description = "";
        this.link = "";
        this.url = "";
        this.length = 0;
        this.type = "/";
        this.publishDate = new Date();
        this.summary = "";
        this.subtitle = "";
        this.duration = "00:00";
    }   
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.title);
        hash = 59 * hash + Objects.hashCode(this.description);
        hash = 59 * hash + Objects.hashCode(this.link);
        hash = 59 * hash + Objects.hashCode(this.url);
        hash = 59 * hash + Objects.hashCode(this.length);
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Objects.hashCode(this.publishDate);
        hash = 59 * hash + Objects.hashCode(this.summary);
        hash = 59 * hash + Objects.hashCode(this.subtitle);
        hash = 59 * hash + Objects.hashCode(this.duration);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PodcastEntry other = (PodcastEntry) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.link, other.link)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.summary, other.summary)) {
            return false;
        }
        if (!Objects.equals(this.subtitle, other.subtitle)) {
            return false;
        }
        if (!Objects.equals(this.duration, other.duration)) {
            return false;
        }
        if (!Objects.equals(this.length, other.length)) {
            return false;
        }
        if (!Objects.equals(this.publishDate, other.publishDate)) {
            return false;
        }
        return true;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
    
    

    @Override
    public String toString() {
        return "PodcastEntry{" + "title=" + title + ", description=" + description + ", link=" + link + ", url=" + url + ", length=" + length + ", type=" + type + ", publishDate=" + publishDate + ", summary=" + summary + ", subtitle=" + subtitle + ", duration=" + duration + '}';
    }
    
}
