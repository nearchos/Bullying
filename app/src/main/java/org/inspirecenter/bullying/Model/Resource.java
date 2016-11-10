
package org.inspirecenter.bullying.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Resource implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("buffered")
    @Expose
    private Boolean buffered;

    @SerializedName("alt")
    @Expose
    private String alt;

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
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return The buffered
     */
    public Boolean getBuffered() {
        return buffered;
    }

    /**
     * @param buffered The buffered
     */
    public void setBuffered(Boolean buffered) {
        this.buffered = buffered;
    }

    /**
     * @return The alt
     */
    public String getAlt() {
        return alt;
    }

    /**
     * @param alt The alt
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }

}
