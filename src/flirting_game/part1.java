package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class part1 extends JFrame {
    private JLabel backgroundLabel; 
    private JLabel characterLabel;  
    private JLabel dialogueArea; // เปลี่ยนเป็น JLabel เพื่อรองรับ HTML Shadow
    private JLabel nameLabel;
    private RoundedPanel dialoguePanel; // ประกาศไว้ที่นี่เพื่อให้เรียก repaint ได้ถูกต้อง
    private int currentIndex = 0; 
    
    private String[] imagePaths = {
        "res/scene1/s1.png", "res/scene1/s2.png", "res/scene1/s3.png", "res/scene1/s4.png",
        "res/scene1/s5.png", "res/scene1/s6.png", "res/scene1/s7.png", "res/scene1/s8.png",
        "res/scene1/s9.png", "res/scene1/s9.png", "res/scene1/s9.png", "res/scene1/s10.png",
        "res/scene1/s10.png", "res/scene1/s11.png"
    };

    private String[] charPaths = {
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png",  "res/scene1/s11g.png" 
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
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel(scaleImage(charPaths[0], 1000, 800));
        characterLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        dialoguePanel = new RoundedPanel(30); 
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 580, 900, 160); 
        dialoguePanel.setBackground(new Color(20, 20, 25, 215));
        
        dialoguePanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25)); 
        
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        // --- กล่องชื่อสีเหลืองพร้อมเงา ---
        JPanel nameBox = new JPanel();
        nameBox.setLayout(null);
        nameBox.setBackground(new Color(255, 204, 0)); 
        nameBox.setBounds(0, 0, 160, 35); 
        nameBox.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 120)));
        dialoguePanel.add(nameBox);

        nameLabel = new JLabel("<html><span style='text-shadow: 2px 2px 3px rgba(0,0,0,0.5);'>" + names[0] + "</span></html>");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        nameLabel.setForeground(Color.BLACK); 
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setBounds(0, 0, 160, 35);
        nameBox.add(nameLabel);

        // --- บทสนทนาพร้อมเงา (ใช้ JLabel แทน JTextArea) ---
        dialogueArea = new JLabel("<html><body style='width: 750px;'><span style='text-shadow: 1px 1px 2px black;'>" + dialogues[0] + "</span></body></html>");
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 22));
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP); 
        dialogueArea.setBounds(25, 55, 850, 100); 
        dialoguePanel.add(dialogueArea);

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentIndex++; 
                if (currentIndex < dialogues.length) {
                    // อัปเดตข้อความพร้อม HTML Shadow
                    dialogueArea.setText("<html><body style='width: 750px;'><span style='text-shadow: 1px 1px 2px black;'>" + dialogues[currentIndex] + "</span></body></html>");
                    nameLabel.setText("<html><span style='text-shadow: 2px 2px 3px rgba(0,0,0,0.5);'>" + names[currentIndex] + "</span></html>");
                    
                    backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1000, 800));
                    characterLabel.setIcon(scaleImage(charPaths[currentIndex], 1000, 800));
                    
                    layeredPane.repaint(); // บังคับวาดใหม่ทั้งเลเยอร์
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
class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }
}