package gui.pieces;

import gui.Icons;
import logic.cards.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardGui extends JButton {

    private Card card;

    private final Border green = BorderFactory.createMatteBorder(
            2, 2, 2, 2, Color.green);

    private final Border black = BorderFactory.createMatteBorder(
            2, 2, 2, 2, Color.darkGray);

    public CardGui() {
        super();
        setBorder(black);
        setSize(100, 153);
        setInvisible();
    }

    public void select(){
        setBorder(green);
    }

    public void unselect(){
        setBorder(black);
    }

    public void setInvisible(){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("images\\unknown.jpg"));
            Image dimg = img.getScaledInstance(this.getWidth(), this.getHeight(),
                    Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            this.setIcon(imageIcon);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showCard() throws IOException {
        String path = "";
        if(card == null) {
            setInvisible();
            return;
        }

        switch (card.getCardType()){
            case ambassador:
                path = Icons.getAmbassador();
                break;

            case assassin:
                path = Icons.getAssassin();
                break;

            case captain:
                path = Icons.getCaptain();
                break;

            case contessa:
                path = Icons.getContessa();
                break;

            case duke:
                path = Icons.getDuke();
                break;
        }

        BufferedImage img = ImageIO.read(new File(path));
        Image dimg = img.getScaledInstance(this.getWidth(), this.getHeight(),
                Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        this.setIcon(imageIcon);

    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
