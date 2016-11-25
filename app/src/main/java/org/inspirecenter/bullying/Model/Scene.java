package org.inspirecenter.bullying.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Scene implements Serializable, Comparable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("order")
    @Expose
    private Integer order;

    @SerializedName("title")
    @Expose
    private String title;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Vector<Step> getOrderedSteps() {
        final Vector<Step> steps= new Vector<>();
        steps.addAll(this.steps);
        Collections.sort(steps);
        return steps;
    }

    private Map<String, Step> idToStepMap = new HashMap<>();

    public Step getStepById(final String id) {
        if(idToStepMap.isEmpty()) {
            for(final Step step : steps) {
                idToStepMap.put(step.getId(), step);
            }
        }
        return idToStepMap.get(id);
    }

    /**
     * @param steps The steps
     */
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public int compareTo(@NonNull Object other) {
        if(other instanceof Scene) {
            final Scene otherScene = (Scene) other;
            return order.compareTo(otherScene.order);
        } else {
            throw new RuntimeException("Cannot compare this with non-instances of " + getClass());
        }
    }
}