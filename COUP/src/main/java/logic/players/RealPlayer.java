package logic.players;

import gui.MainFrame;
import logic.cards.Card;
import logic.game.Actions;

public class RealPlayer extends Player{

    private MainFrame frame;

    public RealPlayer(String name, int playerNumber, Card card1, Card card2) {
        super(name, playerNumber, false);
        cards[0] = card1;
        cards[1] = card2;
    }

    @Override
    public void startCall() {
        frame.getBoard().refresh();
        frame.getActionController().startPhase();
    }

    @Override
    public void challengeCall(Actions action) {
        frame.getBoard().refresh();
        frame.getActionController().challengePhase();
    }

    @Override
    public void blockCall(Actions action) {
        frame.getBoard().refresh();
        frame.getActionController().blockPhase();
    }

    @Override
    public void kill() {
        frame.getBoard().refresh();
        frame.getActionController().killPhase();
    }

    @Override
    public void choose(Card card1, Card card2) {
        frame.getBoard().refresh();
        frame.getActionController().choosePhase(card1, card2);
    }

    public MainFrame getFrame() {
        return frame;
    }

    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }
}
