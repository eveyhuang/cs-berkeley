
package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static db61b.Utils.*;

/** A single table in a database.
 *  @author Jiaxin Huang
 */
class Table implements Iterable<Row> {
    /** A new Table named NAME whose columns are give by COLUMNTITLES,
     *  which must be distinct (else exception thrown). */
    Table(String name, String[] columnTitles) {
        _name = name;
        for (int i = 0; i < columnTitles.length; i++) {
            for (int j = i + 1; j < columnTitles.length; j++) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("No Duplicates!");
                }
            }
            _titles = columnTitles;
        }
    }

    /** A new Table named NAME whose column names are give by COLUMNTITLES. */
    Table(String name, List<String> columnTitles) {
        this(name, columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    int numColumns() {
        return _titles.length;
    }

    /** Returns my name. */
    String name() {
        return _name;
    }

    /** Returns a TableIterator over my rows in an unspecified order. */
    TableIterator tableIterator() {
        return new TableIterator(this);
    }

    /** Returns an iterator that returns my rows in an unspecfied order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    String title(int k) {
        return _titles[k];
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    int columnIndex(String title) {
        for (int i = 0; i < _titles.length; i = i + 1) {
            if (_titles[i].equals(title)) {
                return i;
            }
        }
        return -1;
    }

    /** Return the number of Rows in this table. */
    int size() {
        return _rows.size();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    boolean add(Row row) {
        if (!_rows.contains(row)) {
            _rows.add(row);
            return true;
        }
        return false;
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table = new Table(name, columnNames);
            String current = input.readLine();
            while (current != null) {
                columnNames = current.split(",");
                table.add(new Row(columnNames));
                current = input.readLine();

            }

        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep;
            sep = "";
            output = new PrintStream(name + ".db");
            int i;
            for (i = 0; i < _titles.length; i++) {
                output.print(_titles[i] + ",");
            }
            output.println(_titles[i]);
            Iterator<Row> rowIterator = _rows.iterator();
            while (rowIterator.hasNext()) {
                Row value = rowIterator.next();
                int n;
                for (n = 0; n < _titles.length - 1; n++) {
                    output.print(value.get(n) + ",");
                }
                output.println(_titles[n]);
            }
        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output, separated by spaces
     *  and indented by two spaces. */
    void print() {
        Iterator<Row> rowIterator = _rows.iterator();
        while (rowIterator.hasNext()) {
            Row value = rowIterator.next();
            System.out.print("  ");
            for (int n = 0; n < _titles.length; n++) {
                System.out.print(value.get(n) + " ");
            }
            System.out.println();
        }
    }

    /** @return To get my arraylist of rows. */
    ArrayList<Row> getrows() {
        return _rows;
    }



    /** My name. */
    private final String _name;
    /** My column titles. */
    private String[] _titles = new String[]{};
    /** My list of rows. */
    private ArrayList<Row> _rows = new ArrayList<Row>();
}

