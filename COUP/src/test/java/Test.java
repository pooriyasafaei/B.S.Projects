import com.google.gson.Gson;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Test implements ActionListener {

    private int counter = 0;
    static JToggleButton button = new JToggleButton("select", false);
    static Test test = new Test();

    public static void main(String args[]) throws InterruptedException, IOException {
        JFrame frame = new JFrame("Text Slider");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(25, 25, 600, 400);
        frame.setLayout(null);

        final JTextArea textField = new JTextArea();

        textField.setText("ssss");
        textField.setFont(new Font("Serif", Font.ITALIC, 15));
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        textField.setOpaque(false);
        textField.setEditable(false);

        JScrollPane scrollBar = new JScrollPane(textField);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        DefaultCaret caret = (DefaultCaret)textField.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);


      //  scrollBar.setVisible(true);
        panel.add(scrollBar);

        scrollBar.setBounds(5, 5, 150, 100);
        scrollBar.setBackground(Color.GREEN);

        Border br = BorderFactory.createLineBorder(Color.black);

        panel.setLayout(null);

        Container c = frame.getContentPane();
        c.add(panel);

        panel.setBounds(200, 25, 160, 110);
        panel.setVisible(true);
        panel.setBorder(BorderFactory.createMatteBorder(
                5, 5, 5, 5, Color.green));

        frame.setVisible(true);

        JPanel panel1 = new JPanel();
        panel1.setBounds(300, 300,20,20);
        panel1.setBorder(BorderFactory.createMatteBorder(
                5, 5, 5, 5, Color.red));
        c.add(panel1);
        c.setBackground(Color.pink);

        button.setBorder(BorderFactory.createMatteBorder(
                2, 2, 2, 2, Color.red));
        c.add(button);

        button.setVisible(true);
        button.setEnabled(true);
        button.setBounds(0,0, 100,152);
        button.addActionListener(test);

        BufferedImage img = ImageIO.read(new File("images\\assassin.jpg"));
        Image dimg = img.getScaledInstance(button.getWidth(), button.getHeight(),
                Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        button.setIcon(imageIcon);

        while (true){
            Thread.sleep(1000);
            textField.setText(textField.getText() + "\n\nsssssssssssssssssssssssss");
        }
    }

    public void test(){
        System.out.println("this is a test number " + Test2.pluser());
    }

    int contor = 0;
    @Override
    public void actionPerformed(ActionEvent e) {
        button.setBorder(BorderFactory.createMatteBorder(
                2, 2, 2, 2, Color.green));
       // button.setVisible(false);
        button.setSelected(false);
        System.out.println(++contor);
    }
}

class panelx extends JFrame{
    panelx(){
        setTitle("JPANEL CREATION");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        //setting the bounds for the JFrame
        setBounds(100,100,645,470);
        Border br = BorderFactory.createLineBorder(Color.black);
        Container c=getContentPane();
        //Creating a JPanel for the JFrame
        JPanel panel=new JPanel();
        JPanel panel2=new JPanel();
        JPanel panel3=new JPanel();
        JPanel panel4=new JPanel();
        //setting the panel layout as null
        panel.setLayout(null);
        panel2.setLayout(null);
        panel3.setLayout(null);
        panel4.setLayout(null);
        //adding a label element to the panel
        JLabel label=new JLabel("Panel 1");
        JLabel label2=new JLabel("Panel 2");
        JLabel label3=new JLabel("Panel 3");
        JLabel label4=new JLabel("Panel 4");
        label.setBounds(120,50,200,50);
        label2.setBounds(120,50,200,50);
        label3.setBounds(120,50,200,50);
        label4.setBounds(120,50,200,50);
        panel.add(label);
        panel2.add(label2);
        panel3.add(label3);
        panel4.add(label4);
        // changing the background color of the panel to yellow
        //Panel 1
        panel.setBackground(Color.yellow);
        panel.setBounds(10,10,300,200);
        //Panel 2
        panel2.setBackground(Color.red);
        panel2.setBounds(320,10,300,200);
        //Panel 3
        panel3.setBackground(Color.green);
        panel3.setBounds(10,220,300,200);
        //Panel 4
        panel4.setBackground(Color.cyan);
        panel4.setBounds(320,220,300,200);

        // Panel border
        panel.setBorder(br);
        panel2.setBorder(br);
        panel3.setBorder(br);
        panel4.setBorder(br);

        //adding the panel to the Container of the JFrame
        c.add(panel);
        c.add(panel2);
        c.add(panel3);
        c.add(panel4);

        setVisible(true);
    }
    public static void main(String[] args) {
        new panelx();
    }
}

class Test2{
    public static int counter = 0;
    public static void main(String[] args) throws InterruptedException {
        Test t = new Test();
        Thread thread1 = new Thread(t::test);
        Thread thread2 = new Thread(t::test);

//START the threads
        thread1.start();
       // Thread.sleep(10);
        thread2.start();
    }

    public static int pluser(){
        return ++counter;
    }
}