/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model;

import java.util.Objects;

/**
 * @author Jan Keim
 *
 */
public class CodeComment {
    private String type;
    private String text;
    private int lineNumber;
    private boolean isOrphan;

    CodeComment(String type, String text, int lineNumber, boolean isOrphan) {
        this.type = type;
        this.text = text;
        this.lineNumber = lineNumber;
        this.isOrphan = isOrphan;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @return the lineNumber
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return the isOrphan
     */
    public boolean isOrphan() {
        return isOrphan;
    }

    @Override
    public String toString() {
        return lineNumber + "|" + type + "|" + isOrphan + "|" + text.replace("\\n", "").trim();
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, text);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        var other = (CodeComment) obj;
        return lineNumber == other.lineNumber && Objects.equals(text, other.text);
    }
}
