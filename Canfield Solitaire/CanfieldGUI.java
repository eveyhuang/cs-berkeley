package canfield;


import ucb.gui.TopLevel;
import ucb.gui.LayoutSpec;
import java.awt.event.MouseEvent;

/** A top-level GUI for Canfield solitaire.
 *  @author Jiaxin Huang
 */
class CanfieldGUI extends TopLevel {

    /** A new window with given TITLE and displaying GAME. */
    CanfieldGUI(String title, Game game) {
        super(title, true);
        _game = game;

        addButton("Quit", "quit", new LayoutSpec("y", 0, "x", 0));
        addButton("Clear", "clear", new LayoutSpec("y", 1, "x", 0));
        addButton("Undo", "undo", new LayoutSpec("y", 2, "x", 0));

        _display = new GameDisplay(game);
        add(_display, new LayoutSpec("y", 2, "width", 2));
        _display.setMouseHandler("release", this, "mouseReleased");
        _display.setMouseHandler("press", this, "mousePressed");
        display(true);
    }


    /** Respond to "Quit" button. */
    public void quit(String dummy) {
        System.exit(1);
    }

    /** Respond to "Clear" button: start a new game. */
    public void clear(String dummy) {
        _game.deal();
        _display.repaint();
    }

    /** Respond to "Undo" button. */
    public void undo(String dummy) {
        _game.undo();
        _display.repaint();
    }

    /** Action in response to mouse-released event EVENT. */
    public synchronized void mouseReleased(MouseEvent event) {
        int releasex = event.getX(); int releasey = event.getY();
        _releasedPile = _game.findPile(releasex, releasey);
        if (_game.isWon()) {
            _game.deal();
            _display.repaint();
        }
        if (_releasedPile != null && _chosenPile != null) {
            String from = _chosenPile.name;
            int fromindex = _chosenPile.index;
            String to = _releasedPile.name;
            int toindex = _releasedPile.index;
            if (to.equals("Stock")) {
                _game.save();
                _game.stockToWaste();
            } else if (from.equals("Waste") && to.equals("Waste")) {
                _game.save();
                _game.wasteToFoundation();
            } else if (to.equals("Foundation")) {
                if (from.equals("Reserve")) {
                    _game.save();
                    _game.reserveToFoundation();
                } else if (from.equals("Tableau")) {
                    _game.save();
                    _game.tableauToFoundation(fromindex);
                }
            } else if (to.equals("Tableau")) {
                if (from.equals("Waste")) {
                    _game.save();
                    _game.wasteToTableau(toindex);
                } else if (from.equals("Reserve")) {
                    _game.save();
                    _game.reserveToTableau(toindex);
                } else if (from.equals("Tableau")) {
                    _game.save();
                    _game.tableauToTableau(fromindex, toindex);
                } else if (from.equals("Foundation")) {
                    _game.save();
                    _game.foundationToTableau(fromindex, toindex);
                }
            }
        }
        _display.repaint();
    }


    /** Action in response to MousePressed event.*/
    public synchronized void mousePressed(MouseEvent event) {
        int pressx = event.getX(); int pressy = event.getY();
        Pile pressingPile = _game.findPile(pressx, pressy);
        if (_game.isWon()) {
            _game.deal();
            _display.repaint();
        }
        if (pressingPile != null) {
            _chosenPile = pressingPile;
        }
        _display.repaint();
    }


    /** The board widget. */
    private final GameDisplay _display;

    /** The game I am consulting. */
    private final Game _game;

    /** Point being dragged. */
    private Pile _chosenPile = null;

    /** Pile being release on. */
    private Pile _releasedPile = null;


}
