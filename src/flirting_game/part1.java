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
    private Clip bgmClip;    
    private Clip effectClip; 

    // --- Game Components ---
    private JLabel backgroundLabel; 
    private JLabel characterLabel;  
    private JLabel dialogueArea; 
    private JLabel nameLabel;
    private RoundedPanel dialoguePanel; 
    private int currentIndex = 0; 
    
    // --- ฟอนต์ภาษาไทย (ปรับขนาดให้เข้ากับจอ 1280) ---
    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

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
        setTitle("ISEKAI DEMO - Part 1");
        setSize(1280, 800); // แก้ขนาดเฟรม
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playSE("res/sound/city_sound.wav", true, -10.0f); 

        // Background ปรับเป็น 1280x800
        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1280, 800));
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // Character ปรับพิกัดมาตรฐานเหมือน Part อื่นๆ
        characterLabel = new JLabel(scaleImage(charPaths[0], 900, 900));
        characterLabel.setBounds(190, 100, 900, 900); 
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        updateScene();

        // Fade Overlay ปรับเป็น 1280x800
        fadeOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fadeOverlay.setBounds(0, 0, 1280, 800);
        fadeOverlay.setOpaque(false);
        layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);

        startFadeIn();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAnimating) {
                    stopAnimation();
                    updateDialogueDisplay(dialogues[currentIndex]);
                    return;
                }

                if (currentIndex == 7) {
                    if (bgmClip != null && bgmClip.isRunning()) {
                        bgmClip.stop();
                        bgmClip.close();
                    }
                }

                currentIndex++; 
                if (currentIndex < dialogues.length) {
                    handleSoundEffects(currentIndex);
                    updateScene();
                } else {
                    UIManager.put("OptionPane.messageFont", THAI_FONT_PLAIN);
                    JOptionPane.showMessageDialog(null, "จบ part 1 แล้ว! กำลังเข้าสู่บทถัดไป...");
                    new part2().setVisible(true);
                    dispose();
                }
            }
        }); 
    } 

    private void setupDialogueUI() {
        dialoguePanel = new RoundedPanel(50); 
        dialoguePanel.setLayout(null);
        // ปรับตำแหน่งกึ่งกลางมาตรฐาน (90, 520, 1100, 220)
        dialoguePanel.setBounds(90, 520, 1100, 220);
        dialoguePanel.setBackground(new Color(20, 20, 25, 215));
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel();
        nameLabel.setFont(THAI_FONT_BOLD);
        nameLabel.setForeground(new Color(255, 204, 0)); 
        nameLabel.setBounds(60, 25, 400, 45); 
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(THAI_FONT_PLAIN);
        dialogueArea.setForeground(new Color(230, 230, 230));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP); 
        dialogueArea.setBounds(60, 85, 980, 110); 
        dialoguePanel.add(dialogueArea);
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < imagePaths.length) 
            backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1280, 800));
        if (currentIndex < charPaths.length) {
            characterLabel.setIcon(scaleImage(charPaths[currentIndex], 1000, 800));
            characterLabel.setBounds(190, 0, 1000, 800);
        }
        if (currentIndex < dialogues.length) animateText(dialogues[currentIndex]);
        layeredPane.repaint();
    }

    private void handleSoundEffects(int index) {
        if (index == 2 || index == 4) playEffect("res/sound/phone.wav", 0.0f);
        else if (index == 3) playEffect("res/sound/footsteps.wav", -5.0f);
        else if (index == 5) playEffect("res/sound/traffic.wav", -10.0f);
        else if (index == 7) { screenShake(15, 1000); playEffect("res/sound/carcash.wav", -5.0f); }
        else if (index == 10) playEffect("res/sound/bird.wav", -5.0f);
        else if (index == 12) playEffect("res/sound/AAno.wav", 5.0f); 
        else if (index == 13) playEffect("res/sound/huh.wav", 5.0f); 
    }

    public void playEffect(String path, float volume) {
        try {
            if (effectClip != null && effectClip.isRunning()) effectClip.stop();
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

    public void playSE(String path, boolean isLoop, float volume) {
        try {
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip(); 
                clip.open(audioIn);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                if (isLoop) { bgmClip = clip; clip.loop(Clip.LOOP_CONTINUOUSLY); }
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
        dialogueArea.setText("<html><body style='width: 950px;'><span style='text-shadow: 1px 1px 2px black;'>" 
                            + text + "</span></body></html>");
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    public void screenShake(int intensity, int duration) {
        Point originalLoc = getLocation();
        Timer shakeTimer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        shakeTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed < duration) {
                int x = (int) (Math.random() * intensity * 2 - intensity);
                int y = (int) (Math.random() * intensity * 2 - intensity);
                setLocation(originalLoc.x + x, originalLoc.y + y);
            } else {
                setLocation(originalLoc);
                ((Timer) e.getSource()).stop();
            }
        });
        shakeTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part1().setVisible(true));
    }
}