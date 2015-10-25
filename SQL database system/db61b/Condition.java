
package db61b;

import java.util.List;

/** Represents a single 'where' condition in a 'select' command.
 *  @author Jiaxin Huang */
class Condition {

    /** Internally, we represent our relation as a 3-bit value whose
     *  bits denote whether the relation allows the left value to be
     *  greater than the right (GT), equal to it (EQ),
     *  or less than it (LT). */
    private static final int GT = 1, EQ = 2, LT = 4;

    /** A Condition representing COL1 RELATION COL2, where COL1 and COL2
     *  are column designators. and RELATION is one of the
     *  strings "<", ">", "<=", ">=", "=", or "!=". */
    Condition(Column col1, String relation, Column col2) {
        _col1 = col1;
        _col2 = col2;
        _relation = relation;
    }

    /** A Condition representing COL1 RELATION 'VAL2', where COL1 is
     *  a column designator, VAL2 is a literal value (without the
     *  quotes), and RELATION is one of the strings "<", ">", "<=",
     *  ">=", "=", or "!=".
     */
    Condition(Column col1, String relation, String val2) {

        this(col1, relation, new Literal(val2));

    }

    /** Assuming that ROWS are rows from the respective tables from which
     *  my columns are selected, returns the result of performing the test I
     *  denote. */
    boolean test() {
        int value;
        if (_col2 != null) {
            value = _col1.value().compareTo(_col2.value());
            return testcase(value);
        } else {
            value = _col1.value().compareTo(_val2);
            return testcase(value);

        }
    }

    /**  @return boolean of different cases for testing.
     *   @param value is the result of comparing. */
    private boolean testcase(int value) {
        switch (_relation) {
        case "<" :
            return value < 0;
        case ">" :
            return value > 0;
        case "<=" :
            return value <= 0;
        case ">=" :
            return value >= 0;
        case "=" :
            return value == 0;
        case "!=" :
            return value != 0;
        default:
            return false;
        }
    }


    /** Return true iff all CONDITIONS are satisfied. */
    static boolean test(List<Condition> conditions) {

        for (Condition cond : conditions) {
            if (!cond.test()) {
                return false;
            }
        }
        return true;
    }


    /** columns. */
    private Column _col1, _col2;

    /** relation. */
    private String _relation;

    /** comparison result. */
    private String _val2;


}
