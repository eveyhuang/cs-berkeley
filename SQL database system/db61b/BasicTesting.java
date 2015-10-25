package db61b;
import static org.junit.Assert.*;
import org.junit.Test;


import java.util.List;
import java.util.Arrays;



/**
 * @author Jiaxin Huang
 * Tests for row and table
 */

public class BasicTesting {

    @Test
    public void testRow() {
        Row r = new Row(new String[]{"My", "name", "is", "Evey", "Huang"});
        Row r1 = new Row(new String[]{"hell", "hi", "is", "test", "passes"});

        assertEquals(5, r.size());
        assertEquals("Evey", r.get(3));
        assertEquals("hi", r1.get(1));
        assertEquals(true, r.equals(r));
        assertEquals(false, r.equals(r1));

    }

    @Test
    public void testColumn() {
        List<String> newTable = Arrays.asList("one", "two", "three");
        Table t = new Table("t", newTable);
        Column c = new Column(t, "one");
    }

    @Test
    public void testTable() {
        List<String> newTable = Arrays.asList("one", "two", "three");
        Table t = new Table("t", newTable);
        assertEquals(3, t.numColumns());
        assertEquals("two", t.title(1));
        assertEquals(2, t.columnIndex("three"));
        Row myRow = new Row(new String[]{"Any", "Amy", "Abbie"});
        assertEquals(true, t.add(myRow));
        assertEquals(false, t.add(myRow));

    }

    @Test
    public void testColumnSize() {
        Table t = new Table("t", new String[] {"Basic", "Test", "Col"});
        assertEquals(3, t.numColumns());
        Table td = new Table("td", new String[] {"B", "W", "T", "C"});
        assertEquals(4, td.numColumns());
    }

    @Test
    public void testColumnGet() {
        Table t = new Table("t", new String[] {"Hey", "Im", "Testing"});
        assertEquals("Testing", t.title(2));
    }


    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(BasicTesting.class));
    }

}
