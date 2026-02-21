package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class part1 extends JFrame {
    // --- Variables for Animation & Sound ---
    private JLayeredPane layeredPane; 
    private float alpha = 1.0f; 
    private JPanel fadeOverlay;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isAnimating = false;
    private Clip bgmClip;    // สำหรับเพลงพื้นหลัง (วนลูป)
    private Clip effectClip; // สำหรับเสียงเอฟเฟกต์ (เล่นครั้งเดียว)

    // --- Game Components ---
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

        // --- เริ่มเล่นเพลงบรรยากาศเมือง (BGM) ---
        playSE("res/sound/city_sound.wav", true, -10.0f); 

        // Background
        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // Character
        characterLabel = new JLabel(scaleImage(charPaths[0], 1000, 800));
        characterLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        // Dialogue UI (RoundedPanel)
        dialoguePanel = new RoundedPanel(40); 
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 550, 900, 180);
        dialoguePanel.setBackground(new Color(20, 20, 25, 215));
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        // Name Label
        nameLabel = new JLabel();
        updateNameLabel(names[0]);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        nameLabel.setForeground(new Color(255, 204, 0)); 

        nameLabel.setBounds(60, 20, 300, 40); 
        dialoguePanel.add(nameLabel);

        // Dialogue Area
        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 24));
        dialogueArea.setForeground(new Color(230, 230, 230));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP); 

        dialogueArea.setBounds(60, 70, 800, 100); 
        dialoguePanel.add(dialogueArea);

        // Fade Overlay
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

        // Mouse Listener สำหรับเปลี่ยนฉากและจัดการเสียง
        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAnimating) {
                    stopAnimation();
                    updateDialogueDisplay(dialogues[currentIndex]);
                    return;
                }

                // สั่งหยุดเพลงบรรยากาศเมื่อถึงฉากอุบัติเหตุ (Index 7)
                if (currentIndex == 7) {
                    if (bgmClip != null && bgmClip.isRunning()) {
                        bgmClip.stop();
                        bgmClip.close();
                    }
                }

                currentIndex++; 
                if (currentIndex < dialogues.length) {
                    // --- ส่วนจัดการเสียง Sound Effects ตามลำดับเหตุการณ์ ---
                    handleSoundEffects(currentIndex);

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

    // แยกฟังก์ชันจัดการเสียงเพื่อให้โค้ดอ่านง่าย
    private void handleSoundEffects(int index) {
        if (index == 2 || index == 4) {
            playEffect("res/sound/phone.wav", 0.0f);
        } else if (index == 3) {
            playEffect("res/sound/footsteps.wav", -5.0f);
        } else if (index == 5) {
            playEffect("res/sound/traffic.wav", -10.0f);
        } else if (index == 7) {
            playEffect("res/sound/carcash.wav", -5.0f);
        } else if (index == 10) {
            playEffect("res/sound/bird.wav", -5.0f);
        } else if (index == 12) {
            playEffect("res/sound/AAno.wav", 5.0f); 
        } else if (index == 13) {
            playEffect("res/sound/huh.wav", 5.0f); 
        }
    }

    // --- ฟังก์ชันเล่นเสียงเอฟเฟกต์ (เล่นครั้งเดียว/ป้องกันเสียงซ้อน) ---
    public void playEffect(String path, float volume) {
        try {
            if (effectClip != null && effectClip.isRunning()) {
                effectClip.stop();
            }
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                effectClip = AudioSystem.getClip(); 
                effectClip.open(audioIn);
                FloatControl gainControl = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                effectClip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- ฟังก์ชันเล่นเสียง BGM (วนลูป) ---
    public void playSE(String path, boolean isLoop, float volume) {
        try {
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip(); 
                clip.open(audioIn);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                if (isLoop) {
                    bgmClip = clip;
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                clip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(80, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0;
                ((Timer)e.getSource()).stop();
                layeredPane.remove(fadeOverlay);
                layeredPane.repaint();
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void animateText(String fullText) {
        isAnimating = true; charIndex = 0; dialogueArea.setText(""); 
        if (typewriterTimer != null && typewriterTimer.isRunning()) typewriterTimer.stop();
        typewriterTimer = new Timer(20, e -> {
            if (charIndex <= fullText.length()) {
                updateDialogueDisplay(fullText.substring(0, charIndex));
                charIndex++;
            } else { stopAnimation(); }
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

    private void updateNameLabel(String name) { nameLabel.setText(name); }

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

// --- คลาสสำหรับกล่องข้อความขอบมน ---
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
        GradientPaint gp = new GradientPaint(0, 0, new Color(30, 30, 30, 180), 0, getHeight(), new Color(60, 60, 60, 220));
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.setColor(new Color(255, 255, 255, 50));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
    }
}