
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

    @SerializedName("description")
    @Expose
    private String description;

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

    @SerializedName("dialogs")
    @Expose
    private List<Dialog> dialogs = new ArrayList<Dialog>();

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

    public String getDescription() {
        return description;
    }

    /**
     * @return The copyright
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * @return The year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return The thumbnail
     */
    public String getThumbnail() {
        return thubmnail;
    }

    /**
     * @return The authors
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * @return The resources
     */
    public List<Resource> getResources() {
        return resources;
    }

    private Map<String,Resource> idToResourceMap = new HashMap<>();

    public Resource getResourceById(final String id) {
        if(idToResourceMap.isEmpty()) {
            for(final Resource resource : resources) {
                idToResourceMap.put(resource.getId(), resource);
            }
        }
        return idToResourceMap.get(id);
    }

    private Map<String,Dialog> idToDialogMap = new HashMap<>();

    public Dialog getDialogById(final String id) {
        if(idToDialogMap.isEmpty()) {
            for(final Dialog dialog : dialogs) {
                idToDialogMap.put(dialog.getId(), dialog);
            }
        }
        return idToDialogMap.get(id);
    }

    private Map<String,Interaction> idToInteractionMap = new HashMap<>();

    public Interaction getInteractionById(final String id) {
        if(idToInteractionMap.isEmpty()) {
            for(final Interaction interaction : interactions) {
                idToInteractionMap.put(interaction.getId(), interaction);
            }
        }

        return idToInteractionMap.get(id);
    }

    public Vector<Scene> getOrderedScenes() {
        final Vector<Scene> scenes = new Vector<>();
        scenes.addAll(this.scenes);
        Collections.sort(scenes);
        return scenes;
    }

    private Map<String, Scene> idToSceneMap = new HashMap<>();

    public Scene getSceneById(final String id) {
        if(idToSceneMap.isEmpty()) {
            for(final Scene scene : scenes) {
                idToSceneMap.put(scene.getId(), scene);
            }
        }
        return idToSceneMap.get(id);
    }
}