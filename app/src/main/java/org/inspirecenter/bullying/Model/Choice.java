
package org.inspirecenter.bullying.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Choice {

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
    @SerializedName("option-text")
    @Expose
    private String optionText;

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

    /**
     * @return The optionText
     */
    public String getOptionText() {
        return optionText;
    }

    /**
     * @param optionText The option-text
     */
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

}
