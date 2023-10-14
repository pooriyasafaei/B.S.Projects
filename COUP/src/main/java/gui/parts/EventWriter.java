package gui.parts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class EventWriter extends JPanel {

    private int width = 350;
    private int height = 375;

    private Board board;

    private final JTextArea events = new JTextArea();
    private final JScrollPane pane = new JScrollPane(events);
    private final Border border = BorderFactory.createMatteBorder(
            2, 2, 2, 2, Color.DARK_GRAY);

    public EventWriter(int width, int height){
        this.width = width;
        this.height = height;

        setLayout(null);
        setToolTipText("Events during game captures in this box");
        // text field settings
        events.setFont(new Font("Serif", Font.ITALIC, 15));
        events.setLineWrap(true);
        events.setWrapStyleWord(true);
        events.setOpaque(false);
        events.setEditable(false);
        DefaultCaret caret = (DefaultCaret) events.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // pane settings
        add(pane);
        pane.setBounds(2,2,width - 4,height - 4);

        // borders
        setBorder(BorderFactory.createMatteBorder(
                1, 1, 2, 3, Color.darkGray));

    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public void noticeEvent(String s){
        events.setText(events.getText() + "\n" + s);
        revalidate();
        repaint();
    }

}
