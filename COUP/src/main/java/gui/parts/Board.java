package gui.parts;
// bounds: 750*615

import gui.pieces.CardGui;
import logic.players.Player;
import logic.cards.Card;
import logic.game.BoardGame;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {
    private final CardGui playerCard1;
    private final CardGui playerCard2;
    private final CardGui player2Card1;
    private final CardGui player2Card2;
    private final CardGui player3Card1;
    private final CardGui player3Card2;
    private final CardGui player4Card1;
    private final CardGui player4Card2;
    private final JLabel player2Name;
    private final JLabel player2Coins;
    private JLabel playerName;
    private final JLabel playerCoins;
    private JLabel player3Name;
    private final JLabel player3Coins;
    private JLabel player4Name;
    private final JLabel player4Coins;
    private final JToggleButton player2SelectButton;
    private final JToggleButton player3SelectButton;
    private final JToggleButton player4SelectButton;
    private final CardGui selectCard1;
    private final CardGui selectCard2;

    private JLabel background;

    private final BoardGame game;
    private final Player player;

    private Card firstSelectedCard;
    private Card secondSelectedCard;
    private int selectedPlayer = -1;

    private final int cardWidth = 100;
    private final int cardHeight = 153;

    private final Color name = new Color(197, 197, 197);
    private final Color coins = new Color(222, 197, 13);

    public Board(BoardGame game, Player player) {
        this.game = game;
        this.player = player;

        //construct components
        playerCard1 = new CardGui ();
        playerCard1.setCard(game.getPlayers()[0].getCards()[0]);
        playerCard1.addActionListener(this);
        playerCard1.setActionCommand("playerCard1");

        playerCard2 = new CardGui ();
        playerCard2.setCard(game.getPlayers()[0].getCards()[1]);
        playerCard2.addActionListener(this);
        playerCard2.setActionCommand("playerCard2");

        player2Card1 = new CardGui ();
        player2Card2 = new CardGui ();
        player3Card1 = new CardGui ();
        player3Card2 = new CardGui ();
        player4Card1 = new CardGui ();
        player4Card2 = new CardGui ();

        playerName = new JLabel ("Name: " + game.getPlayers()[0].getName());
        playerName.setBackground(name);
        playerName.setOpaque(true);

        playerCoins = new JLabel ("Coins:  " + game.getPlayers()[0].getCoins());
        playerCoins.setBackground(coins);
        playerCoins.setOpaque(true);

        player2Name = new JLabel ("Name: " + game.getPlayers()[1].getName());
        player2Name.setBackground(name);
        player2Name.setOpaque(true);

        player2Coins = new JLabel ("Coins:  " + game.getPlayers()[1].getCoins());
        player2Coins.setBackground(coins);
        player2Coins.setOpaque(true);

        player3Name = new JLabel ("Name: " + game.getPlayers()[2].getName());
        player3Name.setBackground(name);
        player3Name.setOpaque(true);

        player3Coins = new JLabel ("Coins: " + game.getPlayers()[2].getCoins());
        player3Coins.setBackground(coins);
        player3Coins.setOpaque(true);


        player4Name = new JLabel ("Name: " + game.getPlayers()[3].getName());
        player4Name.setBackground(name);
        player4Name.setOpaque(true);

        player4Coins = new JLabel ("Coins: " + game.getPlayers()[3].getCoins());
        player4Coins.setBackground(coins);
        player4Coins.setOpaque(true);


        player2SelectButton = new JToggleButton ("Select", false);
        player2SelectButton.addActionListener(this);
        player2SelectButton.setActionCommand("select2");

        player3SelectButton = new JToggleButton ("Select", false);
        player3SelectButton.addActionListener(this);
        player3SelectButton.setActionCommand("select3");

        player4SelectButton = new JToggleButton ("Select", false);
        player4SelectButton.addActionListener(this);
        player4SelectButton.setActionCommand("select4");

        selectCard1 = new CardGui ();
        selectCard1.addActionListener(this);
        selectCard1.setActionCommand("selectCard1");

        selectCard2 = new CardGui ();
        selectCard2.addActionListener(this);
        selectCard2.setActionCommand("selectCard2");

        background = new JLabel();

        //adjust size and set layout
        setPreferredSize (new Dimension (750, 605));
        setLayout (null);

        //add components
        add (playerCard1);
        add (playerCard2);
        add (player2Card1);
        add (player2Card2);
        add (player3Card1);
        add (player3Card2);
        add (player4Card1);
        add (player4Card2);
        add (player2Name);
        add (player2Coins);
        add (playerName);
        add (playerCoins);
        add (player3Name);
        add (player3Coins);
        add (player4Name);
        add (player4Coins);
        add (player2SelectButton);
        add (player3SelectButton);
        add (player4SelectButton);
        add (selectCard1);
        add (selectCard2);
        add (background);

        //set component bounds (only needed by Absolute Positioning)
        playerCard1.setBounds (255, 390, 100, 153);
        playerCard2.setBounds (370, 390, 100, 153);
        player2Card1.setBounds (10, 200, 100, 153);
        player2Card2.setBounds (120, 200, 100, 153);
        player3Card1.setBounds (255, 10, 100, 153);
        player3Card2.setBounds (370, 10, 100, 153);
        player4Card1.setBounds (505, 200, 100, 153);
        player4Card2.setBounds (615, 200, 100, 153);
        playerName.setBounds (255, 550, 215, 25);
        playerCoins.setBounds (255, 580, 55, 25);
        player2Name.setBounds (10, 360, 210, 25);
        player2Coins.setBounds (10, 390, 55, 25);
        player3Name.setBounds (255, 170, 215, 25);
        player3Coins.setBounds (255, 200, 55, 25);
        player4Name.setBounds (505, 360, 210, 25);
        player4Coins.setBounds (505, 390, 55, 25);
        player2SelectButton.setBounds (120, 390, 70, 25);
        player3SelectButton.setBounds (370, 200, 70, 25);
        player4SelectButton.setBounds (615, 390, 70, 25);
        selectCard1.setBounds (5, 445, 100, 153);
        selectCard2.setBounds (110, 445, 100, 153);
        background.setBounds(0,0,750,605);

        // visibility
        hideChoosingCards();

        background.setIcon(new ImageIcon("images\\desk.jpg"));

        setBorder(BorderFactory.createMatteBorder(
                3, 3, 3, 3, Color.black));

    }

    public void refresh(){

     try { // select players
            if (game.getPlayers()[1].isDead())
                player2SelectButton.setVisible(false);
            if (game.getPlayers()[2].isDead())
                player3SelectButton.setVisible(false);
            if (game.getPlayers()[3].isDead())
                player4SelectButton.setVisible(false);

            // cards
            playerCard1.setCard(game.getPlayers()[0].getCards()[0]);
            playerCard1.showCard();
            if (playerCard1.getCard().isDead())
                playerCard1.setEnabled(false);

            playerCard2.setCard(game.getPlayers()[0].getCards()[1]);
            playerCard2.showCard();
            if (playerCard2.getCard().isDead())
                playerCard2.setEnabled(false);

            player2Card1.setCard(game.getPlayers()[1].getCards()[0]);
            if (player2Card1.getCard().isDead())
                player2Card1.showCard();
            else player2Card1.setInvisible();

            player2Card2.setCard(game.getPlayers()[1].getCards()[1]);
            if (player2Card2.getCard().isDead())
                player2Card2.showCard();
            else player2Card2.setInvisible();

            player3Card1.setCard(game.getPlayers()[2].getCards()[0]);
            if (player3Card1.getCard().isDead())
                player3Card1.showCard();
            else player3Card1.setInvisible();

            player3Card2.setCard(game.getPlayers()[2].getCards()[1]);
            if (player3Card2.getCard().isDead())
                player3Card2.showCard();
            else player3Card2.setInvisible();

            player4Card1.setCard(game.getPlayers()[3].getCards()[0]);
            if (player4Card1.getCard().isDead())
                player4Card1.showCard();
            else player4Card1.setInvisible();

            player4Card2.setCard(game.getPlayers()[3].getCards()[1]);
            if (player4Card2.getCard().isDead())
                player4Card2.showCard();
            else player4Card2.setInvisible();

            // coins
            playerCoins.setText("Coins: " + game.getPlayers()[0].getCoins());
            player2Coins.setText("Coins: " + game.getPlayers()[1].getCoins());
            player3Coins.setText("Coins: " + game.getPlayers()[2].getCoins());
            player4Coins.setText("Coins: " + game.getPlayers()[3].getCoins());
        } catch (IOException ex) {
         ex.printStackTrace();
     }

    }

    public void unselectAll(){
        unselectPlayerCards();
        firstSelectedCard = null;

        unselectChoosingCards();
        secondSelectedCard = null;

        unselectPlayers();
        selectedPlayer = -1;
    }

    public void unselectPlayerCards(){
        playerCard1.unselect();
        playerCard2.unselect();
    }

    public void unselectChoosingCards(){
        selectCard1.unselect();
        selectCard2.unselect();
    }

    public void unselectPlayers(){
        player2SelectButton.setSelected(false);
        player3SelectButton.setSelected(false);
        player4SelectButton.setSelected(false);
    }

    public void showChoosingCards(Card card1, Card card2){
        try {
            selectCard1.setCard(card1);
            selectCard1.showCard();
            selectCard2.setCard(card2);
            selectCard2.showCard();
        } catch (Exception e){
            e.printStackTrace();
        }

        selectCard1.setVisible(true);
        selectCard2.setVisible(true);
    }

    public void hideChoosingCards(){
        selectCard1.setVisible(false);
        selectCard2.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command){
            case "playerCard1":
                unselectPlayerCards();
                firstSelectedCard = playerCard1.getCard();
                playerCard1.select();
                break;

            case "playerCard2":
                unselectPlayerCards();
                firstSelectedCard = playerCard2.getCard();
                playerCard2.select();
                break;

            case "selectCard1":
                unselectChoosingCards();
                secondSelectedCard = selectCard1.getCard();
                selectCard1.select();
                break;

            case "selectCard2":
                unselectChoosingCards();
                secondSelectedCard = selectCard2.getCard();
                selectCard2.select();
                break;

            case "select2":
                unselectPlayers();
                selectedPlayer = 1;
                player2SelectButton.setSelected(true);
                break;

            case "select3":
                unselectPlayers();
                selectedPlayer = 2;
                player3SelectButton.setSelected(true);
                break;

            case "select4":
                unselectPlayers();
                selectedPlayer = 3;
                player4SelectButton.setSelected(true);
                break;
        }
    }

    public Card getFirstSelectedCard() {
        return firstSelectedCard;
    }

    public Card getSecondSelectedCard() {
        return secondSelectedCard;
    }

    public int getSelectedPlayer() {
        return selectedPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public BoardGame getGame() {
        return game;
    }

    public CardGui getPlayerCard1() {
        return playerCard1;
    }

    public CardGui getPlayerCard2() {
        return playerCard2;
    }

    public void setFirstSelectedCard(Card firstSelectedCard) {
        this.firstSelectedCard = firstSelectedCard;
    }

    public void setSecondSelectedCard(Card secondSelectedCard) {
        this.secondSelectedCard = secondSelectedCard;
    }

    public void setSelectedPlayer(int selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }

    public CardGui getSelectCard1() {
        return selectCard1;
    }

    public CardGui getSelectCard2() {
        return selectCard2;
    }
}
