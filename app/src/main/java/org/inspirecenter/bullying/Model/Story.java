
package org.inspirecenter.bullying.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Story implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("copyright")
    @Expose
    private String copyright;

    @SerializedName("year")
    @Expose
    private Integer year;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("thumbnail")
    @Expose
    private String thubmnail;

    @SerializedName("authors")
    @Expose
    private List<Author> authors = new ArrayList<Author>();

    @SerializedName("resources")
    @Expose
    private List<Resource> resources = new ArrayList<Resource>();

    @SerializedName("interactions")
    @Expose
    private List<Interaction> interactions = new ArrayList<Interaction>();

    @SerializedName("scenes")
    @Expose
    private List<Scene> scenes = new ArrayList<Scene>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The copyright
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * @param copyright The copyright
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * @return The year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year The year
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The thumbnail
     */
    public String getThumbnail() {
        return thubmnail;
    }

    /**
     * @param thumbnail The thumbnail
     */
    public void setThumbnail(String thumbnail) {
        this.thubmnail = thumbnail;
    }

    /**
     * @return The authors
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * @param authors The authors
     */
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    /**
     * @return The resources
     */
    public List<Resource> getResources() {
        return resources;
    }

    private Map<String,Resource> idToResourceMap = new HashMap<>();

    /**
     * @param resources The resources
     */
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public Resource getResourceById(final String id) {
        if(idToResourceMap.isEmpty()) {
            for(final Resource resource : resources) {
                idToResourceMap.put(resource.getId(), resource);
            }
        }
        return idToResourceMap.get(id);
    }

    /**
     * @return The interactions
     */
    public List<Interaction> getInteractions() {
        return interactions;
    }

    /**
     * @param interactions The interactions
     */
    public void setInteractions(List<Interaction> interactions) {
        this.interactions = interactions;
    }

    /**
     * @return The scenes
     */
    public List<Scene> getScenes() {
        return scenes;
    }

    public Vector<Scene> getOrderedScenes() {
        final Vector<Scene> scenes = new Vector<>();
        scenes.addAll(this.scenes);
        Collections.sort(scenes);
        return scenes;
    }

    /**
     * @param scenes The scenes
     */
    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
    }

}