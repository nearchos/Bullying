
package org.inspirecenter.bullying.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Scene implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("background")
    @Expose
    private String background;

    @SerializedName("soundtrack")
    @Expose
    private String soundtrack;

    @SerializedName("steps")
    @Expose
    private List<Step> steps = new ArrayList<Step>();

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The background
     */
    public String getBackground() {
        return background;
    }

    /**
     * @param background The background
     */
    public void setBackground(String background) {
        this.background = background;
    }

    /**
     * @return The soundtrack
     */
    public String getSoundtrack() {
        return soundtrack;
    }

    /**
     * @param soundtrack The soundtrack
     */
    public void setSoundtrack(String soundtrack) {
        this.soundtrack = soundtrack;
    }

    /**
     * @return The steps
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * @param steps The steps
     */
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

}
