package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class part1 extends JFrame {
    private JLayeredPane layeredPane; 
    private float alpha = 1.0f; 
    private JPanel fadeOverlay;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isAnimating = false;

    private JLabel backgroundLabel; 
    private JLabel characterLabel;  
    private JLabel dialogueArea; 
    private JLabel nameLabel;
    private RoundedPanel dialoguePanel; 
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
        
        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel(scaleImage(charPaths[0], 1000, 800));
        characterLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        dialoguePanel = new RoundedPanel(40); 
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 550, 900, 180);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel();
        updateNameLabel(names[0]);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        nameLabel.setForeground(Color.WHITE); 
        nameLabel.setBounds(40, 15, 200, 40); 
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 22));
        dialogueArea.setForeground(new Color(230, 230, 230));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP); 
        dialogueArea.setBounds(50, 65, 800, 100); 
        dialoguePanel.add(dialogueArea);

        fadeOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fadeOverlay.setBounds(0, 0, 1000, 800);
        fadeOverlay.setOpaque(false);
        layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);

        startFadeIn();
        animateText(dialogues[0]);

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAnimating) {
                    stopAnimation();
                    updateDialogueDisplay(dialogues[currentIndex]);
                    return;
                }

                currentIndex++; 
                if (currentIndex < dialogues.length) {
                    updateNameLabel(names[currentIndex]);
                    backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1000, 800));
                    characterLabel.setIcon(scaleImage(charPaths[currentIndex], 1000, 800));
                    animateText(dialogues[currentIndex]);
                    layeredPane.repaint();
                } else {
                    UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(null, "จบpart1แล้ว! กำลังเข้าสู่บทถัดไป...");
                    new part2().setVisible(true); 
                    dispose(); 
                }
            }
        }); 
    } 

    private void startFadeIn() {
        Timer fadeTimer = new Timer(80, null);
        fadeTimer.addActionListener(e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0;
                fadeTimer.stop();
                fadeOverlay.setVisible(false); // ซ่อนเพื่อให้คลิกทะลุได้
                layeredPane.remove(fadeOverlay);
                layeredPane.repaint();
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void animateText(String fullText) {
        isAnimating = true;
        charIndex = 0;
        dialogueArea.setText(""); 
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
        typewriterTimer = new Timer(20, e -> {
            if (charIndex <= fullText.length()) {
                String partialText = fullText.substring(0, charIndex);
                updateDialogueDisplay(partialText);
                charIndex++;
            } else {
                stopAnimation();
            }
        });
        typewriterTimer.start();
    }

    private void stopAnimation() {
        if (typewriterTimer != null) typewriterTimer.stop();
        isAnimating = false;
    }

    private void updateDialogueDisplay(String text) {
        dialogueArea.setText("<html><body style='width: 750px;'><span style='text-shadow: 1px 1px 2px black;'>" 
                             + text + "</span></body></html>");
    }

    private void updateNameLabel(String name) {
        nameLabel.setText(name);
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
        GradientPaint gp = new GradientPaint(0, 0, new Color(30, 30, 30, 180), 
                                            0, getHeight(), new Color(60, 60, 60, 220));
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.setColor(new Color(255, 255, 255, 50));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
    }
}

class TopLeftRoundedPanel extends JPanel {
    private int cornerRadius;
    public TopLeftRoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        int w = getWidth();
        int h = getHeight();
        java.awt.geom.RoundRectangle2D rect = new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius);
        java.awt.geom.Area area = new java.awt.geom.Area(rect);
        area.add(new java.awt.geom.Area(new Rectangle(cornerRadius, 0, w - cornerRadius, h)));
        area.add(new java.awt.geom.Area(new Rectangle(0, cornerRadius, w, h - cornerRadius)));
        g2.fill(area);
    }
}