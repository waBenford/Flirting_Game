package flirting_game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class part1 extends JFrame {
    private JLabel characterLabel;
    private JTextArea dialogueArea;
    private JLabel nameLabel;
    private int currentIndex = 0; 
    
    private String[] imagePaths = {
        "res/s1.png", "res/s2.png", "res/s3.png", "res/s4.png",
        "res/s5.png", "res/s6.png", "res/s7.png", "res/s8.png",
        "res/s9.png", "res/s9.png", "res/s9.png", "res/s10.png",
        "res/s10.png", "res/s11.png"
    };

    private String[] dialogues = {
        "ในโลกที่แออัดวุ่นวาย",
        "ผู้คนก็ต่างใช้ชีวิตด้วยความเร่งรีบ",
        "ฉัน ก็เป็นพนักงานเงินเดือนทั่วไป ไม่ได้ต่างอะไรจากคนอื่นๆ",
        "ฉัน คอยถามตัวเองอยู่เสมอว่าอยากจะทําอะไรกันเเน่",
        "...", "...", "...",
        "เห้ย!! ปรี๊นนนนนนนน",
        "นี่ฉัน.. ตายละหรอ..",
        "ยังไม่ได้ลบไฟล์รูปในคอมพิวเตอร์เลย..",
        "จิ๊บ จิ๊บ จิ๊บ เเละเสียงลมที่กระทบใบไม้...",
        "..ที่นี่ไหนกัน..ฉันยังไม่ตายหรอ..",
        "(เสียงเด็กผู้หญิง) ..เอ่ออ..คือว่าเป็นอะไรรึปล่าวคะ..",
        "..."
    };

    // เพิ่ม Array สำหรับชื่อผู้พูดในแต่ละประโยค
    private String[] names = {
        "บรรยาย", "บรรยาย", "ฉัน", "ฉัน", 
        "...", "...", "...", 
        "คนขับรถ", "ฉัน", "ฉัน", 
        "บรรยาย", "ฉัน", "???", "ฉัน"
    };

    public part1() {
        setTitle("ISEKAI DEMO - Click to Next Scene");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        characterLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        characterLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(characterLabel, JLayeredPane.DEFAULT_LAYER);

        JPanel dialoguePanel = new JPanel();
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 550, 900, 180);
        dialoguePanel.setBackground(new Color(0, 51, 102, 210)); 
        dialoguePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        layeredPane.add(dialoguePanel, JLayeredPane.PALETTE_LAYER);

        nameLabel = new JLabel(names[0]); // ใช้ข้อมูลจาก Array names
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        nameLabel.setForeground(Color.YELLOW);
        nameLabel.setBounds(30, 15, 200, 30);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JTextArea(dialogues[0]);
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 22));
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setEditable(false);
        dialogueArea.setOpaque(false);
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setBounds(30, 60, 840, 100);
        dialoguePanel.add(dialogueArea);

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentIndex++; 
                
                if (currentIndex < dialogues.length) {
                    dialogueArea.setText(dialogues[currentIndex]);
                    nameLabel.setText(names[currentIndex]); // เปลี่ยนชื่อผู้พูดตามบท
                    characterLabel.setIcon(scaleImage(imagePaths[currentIndex], 1000, 800));
                } else {
                    // แก้ไขจุดที่ผิด: ลบคอมม่าเกินหลังข้อความออก
                    JOptionPane.showMessageDialog(null, "จบช่วงนำทางแล้ว!");
                    System.exit(0);
                }
            }
        });
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part1().setVisible(true));
    }
}