/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.codemodelextractor.java.model;

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

    @Override
    public String toString() {
        return lineNumber + "|" + type + "|" + isOrphan + "|" + text.replace("\\n", "").trim();
    }
}
