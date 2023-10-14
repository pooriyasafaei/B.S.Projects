package gui.parts;

import logic.cards.Card;
import logic.game.Actions;
import logic.game.BoardGame;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class ActionController extends JPanel implements ActionListener {
    private final JButton coupButton;
    private final JButton murderButton;
    private final JButton stealButton;
    private final JButton take1Button;
    private final JButton take2Button;
    private final JButton take3Button;
    private final JButton challengeButton;
    private final JButton blockButton;
    private final JButton skipButton;
    private final JButton changeButton;
    private final JButton chooseButton;
    private final JButton killCardButton;
    private final JButton replaceButton;

    private Board board;

    private final int width = 350;
    private final int height = 255;

    public ActionController(Board board) {
        this.board = board;
        //construct components
        coupButton = new JButton ("Coup");
        coupButton.addActionListener(this);
        coupButton.setActionCommand("coup");

        murderButton = new JButton ("Murder");
        murderButton.addActionListener(this);
        murderButton.setActionCommand("murder");

        stealButton = new JButton ("Steal");
        stealButton.addActionListener(this);
        stealButton.setActionCommand("steal");

        take1Button = new JButton ("Take 1");
        take1Button.addActionListener(this);
        take1Button.setActionCommand("take1");

        take2Button = new JButton ("Take 2");
        take2Button.addActionListener(this);
        take2Button.setActionCommand("take2");

        take3Button = new JButton ("Take 3");
        take3Button.addActionListener(this);
        take3Button.setActionCommand("take3");

        challengeButton = new JButton ("Challenge");
        challengeButton.addActionListener(this);
        challengeButton.setActionCommand("challenge");

        blockButton = new JButton ("Block");
        blockButton.addActionListener(this);
        blockButton.setActionCommand("block");

        skipButton = new JButton ("Skip");
        skipButton.addActionListener(this);
        skipButton.setActionCommand("skip");

        changeButton = new JButton ("Change 1");
        changeButton.addActionListener(this);
        changeButton.setActionCommand("change");

        chooseButton = new JButton ("Choose 2");
        chooseButton.addActionListener(this);
        chooseButton.setActionCommand("choose");

        replaceButton = new JButton ("Replace");
        replaceButton.addActionListener(this);
        replaceButton.setActionCommand("replace");

        killCardButton = new JButton ("Give up");
        killCardButton.addActionListener(this);
        killCardButton.setActionCommand("kill");
        killCardButton.setBackground(new Color(215, 48, 48));

        //adjust size and set layout
        setPreferredSize (new Dimension(width, height));
        setLayout (null);

        //add components
        add (coupButton);
        add (murderButton);
        add (stealButton);
        add (take1Button);
        add (take2Button);
        add (take3Button);
        add (challengeButton);
        add (blockButton);
        add (skipButton);
        add (changeButton);
        add (chooseButton);
        add (replaceButton);
        add (killCardButton);

        //set component bounds (only needed by Absolute Positioning)
        coupButton.setBounds (15, 20, 92, 30);
        murderButton.setBounds (125, 20, 92, 30);
        stealButton.setBounds (235, 20, 92, 30);
        take1Button.setBounds (15, 60, 92, 30);
        take2Button.setBounds (125, 60, 92, 30);
        take3Button.setBounds (235, 60, 92, 30);
        challengeButton.setBounds (15, 160, 92, 30);
        blockButton.setBounds (125, 160, 92, 30);
        skipButton.setBounds (235, 160, 90, 30);
        changeButton.setBounds (15, 100, 92, 30);
        chooseButton.setBounds (125, 100, 92, 30);
        replaceButton.setBounds (235, 100, 92, 30);

        killCardButton.setBounds (15, 215, 90, 30);

        setBackground(new Color(106, 140, 66));

        setBorder(BorderFactory.createMatteBorder(
                0, 2, 2, 1, Color.DARK_GRAY));

        disableAll();

    }

    public void disableAll() {
        coupButton.setEnabled(false);
        murderButton.setEnabled(false);
        stealButton.setEnabled(false);
        take1Button.setEnabled(false);
        take2Button.setEnabled(false);
        take3Button.setEnabled(false);
        challengeButton.setEnabled(false);
        blockButton.setEnabled(false);
        skipButton.setEnabled(false);
        changeButton.setEnabled(false);
        chooseButton.setEnabled(false);
        replaceButton.setEnabled(false);
    }

    public void challengePhase() {
        disableAll();
        challengeButton.setEnabled(true);
        skipButton.setEnabled(true);
    }

    public void blockPhase() {
        disableAll();
        blockButton.setEnabled(true);
        skipButton.setEnabled(true);
    }

    public void startPhase() {
        disableAll();

        if(board.getPlayer().getCoins() >= 7) coupButton.setEnabled(true);

        if(board.getPlayer().getCoins() >= 10) return;

        if(board.getPlayer().getCoins() >= 3) murderButton.setEnabled(true);

        if(board.getPlayer().getCoins() >= 1) changeButton.setEnabled(true);

        stealButton.setEnabled(true);
        take1Button.setEnabled(true);
        take2Button.setEnabled(true);
        take3Button.setEnabled(true);
        chooseButton.setEnabled(true);
    }

    public void killPhase() {
        disableAll();
        killCardButton.setEnabled(true);
        revalidate();
        repaint();
    }

    public void choosePhase(Card card1, Card card2) {
        disableAll();
        board.showChoosingCards(card1, card2);
        replaceButton.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        BoardGame game = board.getGame();

        if (command.equals("coup")) {
            if (board.getSelectedPlayer() < 0) {
                game.setMessage("select a player first");
                return;
            }
            if (board.getFirstSelectedCard() == null) {
                game.setMessage("select a card for action first");
                return;
            }
            game.callBack(board.getPlayer().getPlayerNumber(), Actions.coup, board.getSelectedPlayer(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("murder")) {
            if (board.getSelectedPlayer() < 0) {
                game.setMessage("select a player first");
                return;
            }
            if (board.getFirstSelectedCard() == null) {
                game.setMessage("select a card for action first");
                return;
            }

            game.callBack(board.getPlayer().getPlayerNumber(), Actions.murder, board.getSelectedPlayer(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("steal")) {
            if (board.getSelectedPlayer() < 0) {
                game.setMessage("select a player first");
                return;
            }
            if (board.getFirstSelectedCard() == null) {
                game.setMessage("select a card for action first");
                return;
            }

            game.callBack(board.getPlayer().getPlayerNumber(), Actions.steal, board.getSelectedPlayer(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("take1")) {

            game.callBack(board.getPlayer().getPlayerNumber(), Actions.take1, board.getPlayer().getPlayerNumber(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("take2")) {

            game.callBack(board.getPlayer().getPlayerNumber(), Actions.take2, board.getPlayer().getPlayerNumber(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("take3")) {

            if (board.getFirstSelectedCard() == null) {
                game.setMessage("select a card for action first");
                return;
            }

            game.callBack(board.getPlayer().getPlayerNumber(), Actions.take3, board.getPlayer().getPlayerNumber(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("challenge")) {

            if (board.getFirstSelectedCard() == null) {
                game.setMessage("select a card for action first");
                return;
            }

            game.callBack(board.getPlayer().getPlayerNumber(), Actions.challenge, board.getPlayer().getPlayerNumber(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("block")) {

            if (board.getFirstSelectedCard() == null) {
                game.setMessage("select a card for action first");
                return;
            }

            game.callBack(board.getPlayer().getPlayerNumber(), Actions.block, board.getPlayer().getPlayerNumber(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("skip")) {

            game.callBack(board.getPlayer().getPlayerNumber(), Actions.skip, board.getPlayer().getPlayerNumber(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("change")) {

            if (board.getFirstSelectedCard() == null) {
                game.setMessage("select a card for action first");
                return;
            }

            game.callBack(board.getPlayer().getPlayerNumber(), Actions.change, board.getPlayer().getPlayerNumber(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("choose")) {

            if (board.getFirstSelectedCard() == null) {
                game.setMessage("select a card from your hand for action first");
                return;
            }

            game.callBack(board.getPlayer().getPlayerNumber(), Actions.choose, board.getPlayer().getPlayerNumber(),
                    board.getFirstSelectedCard());

            board.unselectAll();
            disableAll();
        }

        if (command.equals("replace")) {

            if (board.getFirstSelectedCard() == null || board.getSecondSelectedCard() == null) {
                if (!game.getPlayers()[0].getCards()[0].isDead()) {
                    game.giveMe(board.getPlayer().getPlayerNumber(), -1, 0);
                } else {
                    game.giveMe(board.getPlayer().getPlayerNumber(), -1, 1);

                }
                board.unselectAll();
                board.hideChoosingCards();
                disableAll();
                return;
            }

            if (board.getFirstSelectedCard() == board.getPlayerCard1().getCard()) {
                if (board.getSecondSelectedCard() == board.getSelectCard1().getCard()) {
                    game.giveMe(board.getPlayer().getPlayerNumber(), 0, 0);
                } else {
                    game.giveMe(board.getPlayer().getPlayerNumber(), 1, 0);

                }
            } else {
                if (board.getSecondSelectedCard() == board.getSelectCard1().getCard()) {
                    game.giveMe(board.getPlayer().getPlayerNumber(), 0, 1);
                } else {
                    game.giveMe(board.getPlayer().getPlayerNumber(), 1, 1);

                }
            }

            board.unselectAll();
            board.hideChoosingCards();
            disableAll();
        }

        if (command.equals("kill")) {
            if (board.getFirstSelectedCard() == null) {
                game.setMessage("select a card from your hand for action first");
                return;
            }

            if (!game.isGiveUpPhase() || game.getChallengers()[1] != board.getPlayer()) {
                game.setMessage("Why should give up your card in this situation?!");
                return;
            }

            game.killed(board.getPlayer().getPlayerNumber(), board.getFirstSelectedCard());
            board.unselectAll();
            disableAll();
        }

    }
}