package seedu.address.model.attribute;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Optional;

import seedu.address.logic.parser.ParserUtil;

/**
 * Represents an Attribute in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidAttribute(String)}.
 * The hash code of an Attribute is that of the attribute name.
 */
public class Attribute implements Comparable<Attribute> {
    public static final String PROHIBITED_CHARACTERS = "/\\=";
    public static final String MESSAGE_CONSTRAINTS = "Attribute names and values should not contain /, \\, or =. "
            + "Attribute names and values should not be longer than 50 characters.";
    public static final String MESSAGE_CONSTRAINTS_FOR_NAME = "Attribute names should not contain /, \\, or =.";
    public static final String MESSAGE_CONSTRAINT_NON_EMPTY_FOR_NAME = "Attribute names cannot be empty.";
    public static final String MESSAGE_CONSTRAINT_NON_EMPTY_FOR_SITE_LINK = "Website domain links cannot be empty.";
    public static final String NO_DUPLICATES = "Duplicate attribute names with different values are not allowed!";
    public static final String CAPITALISATION_NOTE =
        "Note that attribute names that differ only in capitalisation are treated as duplicates.";
    public static final String NO_DUPLICATES_CASE_INSENSITIVITY = "Duplicate attribute names are not allowed!\n"
            + CAPITALISATION_NOTE;
    public static final String VALIDATION_REGEX = "[^\\\\/=]+";
    public static final int VALIDATION_LENGTH = 50;
    public static final String MESSAGE_USAGE =
        "An attribute must consist of exactly one name and one value (both non-empty), separated by =.";

    private final String attributeName;
    private final String attributeValue; //default attribute value
    private final Optional<Double> attributeNumericalValue;
    private final Optional<String> siteLink;

    /**
     * Constructs an {@code Attribute}.
     *
     * @param attributeName A valid attribute name.
     * @param attributeValue A valid attribute value.
     */
    public Attribute(String attributeName, String attributeValue) {
        requireNonNull(attributeName);
        requireNonNull(attributeValue);
        checkArgument(isValidAttribute(attributeName), MESSAGE_CONSTRAINTS);
        checkArgument(isValidAttribute(attributeValue), MESSAGE_CONSTRAINTS);
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.attributeNumericalValue = ParserUtil.parseStringValueToNumericalValue(attributeValue);
        this.siteLink = Optional.empty();
    }

    private Attribute(String attributeName, String attributeValue, String siteLink) {
        requireNonNull(attributeName);
        requireNonNull(attributeValue);
        requireNonNull(siteLink);
        checkArgument(isValidAttribute(attributeName), MESSAGE_CONSTRAINTS);
        checkArgument(isValidAttribute(attributeValue), MESSAGE_CONSTRAINTS);
        this.attributeName = attributeName;
        this.attributeNumericalValue = ParserUtil.parseStringValueToNumericalValue(attributeValue);
        this.attributeValue = attributeValue;
        this.siteLink = Optional.of(siteLink);
    }

    /**
     * Returns the attribute name in lowercase.
     * This behaviour is desirable for the default getter since attribute names should be treated
     * case-insensitively.
     *
     * @see #getCaseAwareAttributeName()
     */
    public String getAttributeName() {
        return attributeName.toLowerCase();
    }

    /**
     * Returns the attribute name in the original case.
     *
     * @see #getAttributeName()
     */
    public String getCaseAwareAttributeName() {
        return attributeName;
    }

    /**
     * Returns the attribute value as a string.
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * Checks if the attribute value can be parsed into a Double.
     */
    public boolean hasNumericalValue() {
        requireNonNull(this.attributeNumericalValue);
        return this.attributeNumericalValue.isPresent();
    }

    /**
     * Returns the {@code Attribute} displayed in the style "name: value".
     * The actual case of the attribute name is used.
     */
    public String getDisplayText() {
        return attributeName + ": " + attributeValue;
    }

    /**
     * Checks if this attribute has a site link associated with it.
     */
    public boolean hasSiteLink() {
        return siteLink.isPresent();
    }

    /**
     * Returns an {@code Attribute} formatted for user copying.
     * This method cannot be called when this attribute does not have a site link associated with it.
     * In such a case, assertion will be thrown.
     */
    public String getCopyableText() {
        assert siteLink.isPresent() : "Site link must be present to retrieve a copyable text.";
        return siteLink.orElse("Error") + this.attributeValue;
    }

    private Attribute setSiteLink(String newLink) {
        requireNonNull(newLink);
        if (siteLink.isPresent() && siteLink.get().equals(newLink)) {
            return this;
        }
        return new Attribute(attributeName, attributeValue, newLink);
    }

    private Attribute removeSiteLink() {
        if (!siteLink.isPresent()) {
            return this;
        }
        return new Attribute(attributeName, attributeValue);
    }

    /**
     * Updates the site link with the given {@code newLink}.
     * If the newLink contains a new site link, the site link will be updated with the value.
     * If the newLink is empty, then the site link will be removed.
     *
     * @return A new instance of Attribute with the updated site link.
     */
    public Attribute updateSiteLink(Optional<String> newLink) {
        requireNonNull(newLink);
        if (newLink.isPresent()) {
            return setSiteLink(newLink.get());
        } else {
            return removeSiteLink();
        }
    }

    /**
     * Returns {@code siteLink}, which is either the site link or an empty Optional.
     */
    public Optional<String> getSiteLink() {
        return siteLink;
    }

    /**
     * Returns true if a given string is a valid attribute name or value.
     */
    public static boolean isValidAttribute(String test) {
        return test.matches(VALIDATION_REGEX) && test.length() <= VALIDATION_LENGTH;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other instanceof Attribute attribute) {
            return attributeName.equalsIgnoreCase(attribute.attributeName)
                    && attributeValue.equals(attribute.attributeValue);
        }

        return false;
    }

    /**
     * Checks if attribute name matches a given string, case insensitively.
     *
     * @param name Attribute name to check against
     * @return True if it matches, false otherwise
     */
    public boolean matchesName(String name) {
        return attributeName.equalsIgnoreCase(name);
    }

    /**
     * Compare to another attributes with the same attribute name by their default attribute value.
     *
     * @param other The other attribute to compare with
     * @return 0 if they have the same attribute value, -1 if attribute1 has a smaller value, 1 otherwise
     */
    public int compareToSameNameAttributeDefault(Attribute other, boolean isAscending) {
        assert this.matchesName(other.attributeName);
        if (isAscending) {
            return this.attributeValue.compareTo(other.attributeValue);
        } else {
            return -this.attributeValue.compareTo(other.attributeValue);
        }

    }

    /**
     * Compare to another attributes with the same attribute name by their numerical attribute value.
     *
     * @param other The other attribute to compare with
     * @return 0 if they have the same attribute value, -1 if attribute1 has a smaller value, 1 otherwise
     */
    public int compareToSameNameAttributeNumeric(Attribute other, boolean isAscending) {
        assert this.matchesName(other.attributeName);
        if (!other.hasNumericalValue()) {
            return -1;
        } else if (!this.hasNumericalValue()) {
            return 1;
        } else {
            assert this.attributeNumericalValue.isPresent();
            assert other.attributeNumericalValue.isPresent();
            if (isAscending) {
                return this.attributeNumericalValue.get().compareTo(other.attributeNumericalValue.get());
            } else {
                return -this.attributeNumericalValue.get().compareTo(other.attributeNumericalValue.get());
            }

        }
    }

    @Override
    public int hashCode() {
        return attributeName.toLowerCase().hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return String.format("[%s: %s]", attributeName, attributeValue);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Attributes with a site link appear after attributes without a site link.
     */
    @Override
    public int compareTo(Attribute other) {
        return (this.hasSiteLink() == other.hasSiteLink())
                ? this.getAttributeName().compareTo(other.getAttributeName())
                : (this.hasSiteLink() ? 1 : -1);
    }
}
