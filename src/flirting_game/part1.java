package flirting_game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class part1 extends JFrame {
    private JLabel backgroundLabel; // สำหรับฉากหลัง
    private JLabel characterLabel;  // สำหรับตัวละคร
    private JTextArea dialogueArea;
    private JLabel nameLabel;
    private int currentIndex = 0; 
    
    // 1. รายชื่อไฟล์รูปภาพพื้นหลัง (แยกชั้นล่าง)
    private String[] bgPaths = {
        "res/scene1/s1.png", "res/scene1/s2.png", "res/scene1/s3.png", "res/scene1/s4.png",
        "res/scene1/s5.png", "res/scene1/s6.png", "res/scene1/s7.png", "res/scene1/s8.png",
        "res/scene1/s9.png", "res/scene1/s9.png", "res/scene1/s9.png", "res/scene1/s10.png",
        "res/scene1/s10.png", "res/scene1/s11.png"
    };

    // 2. รายชื่อไฟล์รูปตัวละคร (แยกชั้นบน - ควรเป็นไฟล์ .png โปร่งใส)
    // หากช่วงไหนไม่มีตัวละคร ให้ใส่เส้นทางไฟล์ภาพเปล่า หรือใช้ภาพเดิมซ้ำ
    private String[] charPaths = {
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png",  "res//scene1/s11g.png" // ตัวอย่าง: เด็กผู้หญิงปรากฏตัวในฉากที่ 13
    };

    private String[] names = {
        "บรรยาย", "บรรยาย", "ฉัน", "ฉัน", 
        "...", "...", "...", 
        "คนขับรถ", "ฉัน", "ฉัน", 
        "บรรยาย", "ฉัน", "เด็กผู้หญิง", "ฉัน"
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

    public part1() {
        setTitle("ISEKAI DEMO - Layered Scene");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // --- Layer 1: รูปภาพพื้นหลัง (ชั้นล่างสุด) ---
        backgroundLabel = new JLabel(scaleImage(bgPaths[0], 1000, 700));
        backgroundLabel.setBounds(0, 0, 1000, 700);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // --- Layer 2: รูปตัวละคร (ชั้นกลาง) ---
        // ปรับตำแหน่ง x, y ตามความเหมาะสม (ในตัวอย่างคือวางไว้ตรงกลาง)
        characterLabel = new JLabel(scaleImage(charPaths[0], 1000, 700));
        characterLabel.setBounds(0, 0, 1000, 700);
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        // --- Layer 3: แถบข้อความ (ชั้นบนสุด) ---
        JPanel dialoguePanel = new JPanel();
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(40, 473, 900, 180);
        dialoguePanel.setBackground(new Color(0, 51, 102, 210)); 
        dialoguePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel(names[0]);
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

        // ระบบคลิกเพื่อเปลี่ยนฉาก
        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentIndex++; 
                
                if (currentIndex < dialogues.length) {
                    dialogueArea.setText(dialogues[currentIndex]);
                    nameLabel.setText(names[currentIndex]);
                    
                    // อัปเดตรูปพื้นหลัง และรูปตัวละครพร้อมกัน
                    backgroundLabel.setIcon(scaleImage(bgPaths[currentIndex], 1000, 700));
                    characterLabel.setIcon(scaleImage(charPaths[currentIndex], 1000, 700));
                } else {
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