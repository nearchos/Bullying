
package org.inspirecenter.bullying.Model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoryGson {

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
     * @return The thubmnail
     */
    public String getThubmnail() {
        return thubmnail;
    }

    /**
     * @param thubmnail The thubmnail
     */
    public void setThubmnail(String thubmnail) {
        this.thubmnail = thubmnail;
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

    /**
     * @param resources The resources
     */
    public void setResources(List<Resource> resources) {
        this.resources = resources;
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

    /**
     * @param scenes The scenes
     */
    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
    }

}
