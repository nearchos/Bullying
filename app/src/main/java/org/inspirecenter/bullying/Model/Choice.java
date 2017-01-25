
package org.inspirecenter.bullying.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Choice implements Serializable, Comparable {

    @SerializedName("order")
    @Expose
    private Integer order;

    @SerializedName("prompt")
    @Expose
    private String prompt;

    @SerializedName("prompt-sound")
    @Expose
    private String promptSound;

    @SerializedName("score")
    @Expose
    private Integer score;

    @SerializedName("feedback")
    @Expose
    private String feedback;

    @SerializedName("feedback-sound")
    @Expose
    private String feedbackSound;

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
     * @return The prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * @param prompt The prompt
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * @return The promptSound
     */
    public String getPromptSound() {
        return promptSound;
    }

    /**
     * @param promptSound The prompt-sound
     */
    public void setPromptSound(String promptSound) {
        this.promptSound = promptSound;
    }

    /**
     * @return The score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @param score The score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * @return The feedback
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * @param feedback The feedback
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * @return The feedbackSound
     */
    public String getFeedbackSound() {
        return feedbackSound;
    }

    /**
     * @param feedbackSound The feedback-sound
     */
    public void setFeedbackSound(String feedbackSound) {
        this.feedbackSound = feedbackSound;
    }

    @Override
    public int compareTo(@NonNull Object other) {
        if(other instanceof Choice) {
            final Choice otherChoice = (Choice) other;
            return order.compareTo(otherChoice.order);
        } else {
            throw new RuntimeException("Cannot compare this with non-instances of " + getClass());
        }
    }

    @Override
    public String toString() {
        return "Choice{" +
                "order=" + order +
                ", prompt='" + prompt + '\'' +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
