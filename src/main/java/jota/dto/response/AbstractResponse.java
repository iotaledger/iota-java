package jota.dto.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Abstract response.
 */
public abstract class AbstractResponse {

    private Long duration;

    /**
     * Gets the duration.
     *
     * @return The duration.
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Sets the duration.
     *
     * @param duration The duration
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    /**
     * Returns a String that represents this object.
     *
     * @return Returns a string representation of this object.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }
}
