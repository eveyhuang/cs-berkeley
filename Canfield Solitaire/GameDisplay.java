package canfield;

import ucb.gui.Pad;

import java.awt.*;

import javax.imageio.ImageIO;

import java.io.InputStream;
import java.io.IOException;


/** A widget that displays a Pinball playfield.
 *  @author P. N. Hilfinger
 */
class GameDisplay extends Pad {

    /** Color of display field. */
    private static final Color BACKGROUND_COLOR = Color.green.darker().darker().darker();

    /* Coordinates and lengths in pixels unless otherwise stated. */

    /** Preferred dimensions of the playing surface. */
    private static final int BOARD_WIDTH = 805, BOARD_HEIGHT = 680;

    /** Displayed dimensions of a card image. */
    private static final int CARD_HEIGHT = 125, CARD_WIDTH = 90;

    /** A graphical representation of GAME. */
    public GameDisplay(Game game) {
        _game = game;
        setPreferredSize(BOARD_WIDTH, BOARD_HEIGHT);
    }

    /** Return an Image read from the resource named NAME. */
    private Image getImage(String name) {
        InputStream in =
            getClass().getResourceAsStream("/canfield/resources/" + name);
        try {
            return ImageIO.read(in);
        } catch (IOException excp) {
            return null;
        }
    }

    /** Return an Image of CARD. */
    private Image getCardImage(Card card) {
        return getImage("playing-cards/" + card + ".png");
    }

    /** Return an Image of the back of a card. */
    private Image getBackImage() {
        return getImage("playing-cards/blue-back.png");
    }

    /** Draw CARD at X, Y on G. */
    private void paintCard(Graphics2D g, Card card, int x, int y) {
        if (card != null) {
            g.drawImage(getCardImage(card), x, y,
                        CARD_WIDTH, CARD_HEIGHT, null);
        }
    }

    /** Draw card back at X, Y on G. */
    private void paintBack(Graphics2D g, int x, int y) {
        g.drawImage(getBackImage(), x, y, CARD_WIDTH, CARD_HEIGHT, null);
    }

    private static final Color LINE_COLOR = Color.black;
    private static final Stroke LINE_STROKE = new BasicStroke(1);


    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        Rectangle b = g.getClipBounds();
        g.fillRect(0, 0, b.width, b.height);
        g.setStroke(LINE_STROKE);
        g.setColor(LINE_COLOR);

        //reserve
        Card reserve = _game.topReserve();
        paintCard(g, reserve, 70, 200);

        //stock
        paintBack(g, 70, 400);

        //waste
        if (_game.topWaste() == null) {
            g.drawRect(185, 400, CARD_WIDTH,CARD_HEIGHT);
        } else {
            Card waste = _game.topWaste();
            paintCard(g, waste, 185, 400);
        }

        //tableau
        for (int i=1, w=300; i<= Game.TABLEAU_SIZE; i ++, w +=115) {
            for (int j = 0, h=200; j <= _game.tableauSize(i) ; j++, h+=20) {
                paintCard(g, _game.getTableau(i, _game.tableauSize(i)-1-j), w, h);
            }
        }


        //foundation
        for (int i=1; i<5; i ++) {
            int x = 300+115*(i-1);
            if (_game.topFoundation(i) == null) {
                g.drawRect(x,55, CARD_WIDTH, CARD_HEIGHT);
            } else {
                Card foundation = _game.topFoundation(i);
                paintCard(g, foundation, x, 55);
            }
        }

    }


    /** Game I am displaying. */
    private final Game _game;




}
