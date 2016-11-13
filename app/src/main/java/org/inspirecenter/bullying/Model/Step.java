
package org.inspirecenter.bullying.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Step implements Serializable, Comparable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("order")
    @Expose
    private Integer order;

    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("resource-id")
    @Expose
    private String resourceId;

    @SerializedName("transition-in")
    @Expose
    private String transitionIn;

    @SerializedName("delay-in")
    @Expose
    private Integer delayIn;

    @SerializedName("transition-out")
    @Expose
    private String transitionOut;

    @SerializedName("delay-out")
    @Expose
    private Integer delayOut;

    @SerializedName("progression")
    @Expose
    private String progression;

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
     * @return The order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @param order The order
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * @return The action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action The action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return The resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * @param resourceId The resource-id
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * @return The transitionIn
     */
    public String getTransitionIn() {
        return transitionIn;
    }

    /**
     * @param transitionIn The transition-in
     */
    public void setTransitionIn(String transitionIn) {
        this.transitionIn = transitionIn;
    }

    /**
     * @return The delayIn
     */
    public Integer getDelayIn() {
        return delayIn;
    }

    /**
     * @param delayIn The delay-in
     */
    public void setDelayIn(Integer delayIn) {
        this.delayIn = delayIn;
    }

    /**
     * @return The transitionOut
     */
    public String getTransitionOut() {
        return transitionOut;
    }

    /**
     * @param transitionOut The transition-out
     */
    public void setTransitionOut(String transitionOut) {
        this.transitionOut = transitionOut;
    }

    /**
     * @return The delayOut
     */
    public Integer getDelayOut() {
        return delayOut;
    }

    /**
     * @param delayOut The delay-out
     */
    public void setDelayOut(Integer delayOut) {
        this.delayOut = delayOut;
    }

    /**
     * @return The progression
     */
    public String getProgression() {
        return progression;
    }

    /**
     * @param progression The progression
     */
    public void setProgression(String progression) {
        this.progression = progression;
    }

    @Override
    public int compareTo(@NonNull final Object other) {
        if(other instanceof Step) {
            final Step otherStep = (Step) other;
            return order.compareTo(otherStep.getOrder());
        } else {
            throw new RuntimeException("Cannot compare this with non-instances of " + getClass());
        }
    }
}