import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Test {
    public static void main(String[] args) {
        JButton button1 = new JButton("one");
        button1.setIcon(new ImageIcon(new BufferedImage(1, 41, BufferedImage.TYPE_INT_ARGB)));
        JButton button2 = new JButton("Two");
        JButton button3 = new JButton("Three");
        JButton button4 = new JButton("Four");
        JButton button5 = new JButton("Five");
        JButton button6 = new JButton("Six");
        ImageIcon icon = new ImageIcon("C:\\Users\\ASUS\\Desktop\\Generals\\src\\whitesilver.png");
        JButton button7 = new JButton(icon);
        button7.setBorderPainted(false);
        button7.setText("S white");
        button7.setIconTextGap(10-button7.getIcon().getIconWidth());
        button7.setBackground(Color.green);
        Box box = Box.createVerticalBox();
        box.add(button1);
        box.add(button2);
        box.add(button3);
        box.add(button4);
        box.add(button5);
        box.add(button6);
        box.add(button7);
        JFrame frame = new JFrame();
        frame.add(box);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }
}
