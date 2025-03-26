package seedu.address.model.attribute;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

/**
 * Compares two attributes based on their numerical values.
 */
public class NumericalValueBasedAttributeComparator implements Comparator<Attribute> {
    @Override
    public int compare(Attribute o1, Attribute o2) {
        requireNonNull(o1);
        requireNonNull(o2);
        if (!o2.hasNumericalValue()) {
            return -1;
        } else if (!o1.hasNumericalValue()) {
            return 1;
        } else {
            return o1.compareToSameNameAttributeNumeric(o2);
        }
    }
}
