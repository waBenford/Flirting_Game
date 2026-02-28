package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;
import javax.swing.*;

public class part1 extends JFrame {
    private JLayeredPane layeredPane;
    private float alpha = 1.0f;
    private JPanel fadeOverlay;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isAnimating = false;
    private Clip bgmClip;
    private Clip effectClip;
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel; 
    private int currentIndex = 0;
    private boolean isFading = false;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;

    private PrintWriter networkOut;

    // --- ฟอนต์ภาษาไทยสำหรับจอ 1280 ---
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
        "บรรยาย", "บรรยาย", "ฉัน", "ฉัน", "...", "...", "...",
        "คนขับรถ", "ฉัน", "ฉัน", "บรรยาย", "ฉัน", "เด็กผู้หญิง", "ฉัน"
    };

    private String[] dialogues = {
        "ในโลกที่แออัดวุ่นวาย", "ผู้คนก็ต่างใช้ชีวิตด้วยความเร่งรีบ",
        "ฉัน ก็เป็นพนักงานเงินเดือนทั่วไป ไม่ได้ต่างอะไรจากคนอื่นๆ",
        "ฉัน คอยถามตัวเองอยู่เสมอว่าอยากจะทําอะไรกันเเน่",
        "...", "...", "...", "เห้ย!! ปรี๊นนนนนนนน",
        "นี่ฉัน.. ตายละหรอ..", "ยังไม่ได้ลบไฟล์รูปในคอมพิวเตอร์เลย..",
        "จิ๊บ จิ๊บ จิ๊บ เเละเสียงลมที่กระทบใบไม้...",
        "..ที่นี่ไหนกัน..ฉันยังไม่ตายหรอ..",
        "(เสียงเด็กผู้หญิง) ..เอ่ออ..คือว่าเป็นอะไรรึปล่าวคะ..", "..."
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

        // พื้นหลังปรับเป็น 1280x800
        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1280, 800));
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // ปรับพิกัดตัวละครมาตรฐาน (190, 100, 900, 900)
        characterLabel = new JLabel(scaleImage(charPaths[0], 900, 900));
        characterLabel.setBounds(190, 100, 900, 900);
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        initNetwork();

        // แผ่นดำสำหรับ Fade In (1280x800)
        fadeOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
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
                handleNext();
            }
        });
    }

    private void handleNext() {
        if (isFading) return; 
        
        if (isAnimating) {
            stopAnimation();
            updateDialogueDisplay(dialogues[currentIndex]);
            return;
        }

        // --- ตรวจสอบฉากสุดท้าย ---
        if (currentIndex >= dialogues.length - 1) {
            stopBGM();
            // เริ่ม Fade Out แบบพิเศษสำหรับเปลี่ยน Part
            fadeOutToNextPart(); 
            return;
        }

        // การเปลี่ยนฉากปกติระหว่างเล่น
        performSceneFade(() -> {
            currentIndex++; 
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("SYNC_INDEX:" + currentIndex);
            }
            if (currentIndex == 7) stopBGM();
            handleSoundEffects(currentIndex);
            updateScene();
        });
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox();
        dialoguePanel.setLayout(null);
        // ปรับตำแหน่งกล่องข้อความให้อยู่กลางจอ (x=90, width=1100)
        dialoguePanel.setBounds(225, 520, 800, 200);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel(names[0]);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26)); 
        nameLabel.setForeground(new Color(180, 40, 90));
        nameLabel.setBounds(60, 25, 400, 45);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115));
        dialogueArea.setBounds(60, 85, 980, 110);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialoguePanel.add(dialogueArea);

        JLabel nextArrow = new JLabel("▼");
        nextArrow.setFont(new Font("Tahoma", Font.BOLD, 20));
        nextArrow.setForeground(new Color(0, 153, 255));
        nextArrow.setBounds(1040, 170, 30, 30);
        dialoguePanel.add(nextArrow);
        
        Timer arrowTimer = new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible()));
        arrowTimer.start();
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1280, 800));
        if (currentIndex < charPaths.length) {
            characterLabel.setIcon(scaleImage(charPaths[currentIndex], 900, 900));
            characterLabel.setBounds(190, 0, 900, 900);
        }
        if (currentIndex < dialogues.length) animateText(dialogues[currentIndex]);
        
        layeredPane.repaint();
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(50, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0;
                ((Timer) e.getSource()).stop();
                layeredPane.remove(fadeOverlay);
                updateScene();
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void handleSoundEffects(int index) {
        if (index == 2 || index == 4) playEffect("res/sound/phone.wav", 0.0f);
        else if (index == 3) playEffect("res/sound/footsteps.wav", -5.0f);
        else if (index == 5) playEffect("res/sound/traffic.wav", -10.0f);
        else if (index == 7) { screenShake(15, 1000); playEffect("res/sound/carcash.wav", -5.0f); } 
        else if (index == 10) {
            if (effectClip != null) { effectClip.stop(); effectClip.close(); }
            playEffect("res/sound/bird.wav", -5.0f);
        }
        else if (index == 12) playEffect("res/sound/AAno.wav", 5.0f);
        else if (index == 13) playEffect("res/sound/huh.wav", 5.0f);
    }

    public void playEffect(String path, float volume) {
        try {
            if (effectClip != null) { effectClip.stop(); effectClip.close(); }
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                effectClip = AudioSystem.getClip();
                effectClip.open(audioIn);
                FloatControl gc = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
                gc.setValue(volume);
                effectClip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void fadeOutToNextPart() {
    isFading = true;
    alpha = 0.0f;
    
    if (fadeOverlay.getParent() == null) {
        layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
    }

    Timer fadeOut = new Timer(30, null);
    fadeOut.addActionListener(e -> {
        alpha += 0.05f; // ค่อยๆ ดำขึ้น
        if (alpha >= 1.0f) {
            alpha = 1.0f;
            fadeOut.stop();

            // ส่งข้อมูลเน็ตเวิร์ค
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("END_PART:1");
            }

            // หน่วงเวลาเล็กน้อย (200ms) ให้ OS เคลียร์ Memory ก่อนสลับหน้าจอ
            Timer transitionTimer = new Timer(200, ev -> {
                SwingUtilities.invokeLater(() -> {
                    part2 next = new part2();
                    next.setVisible(true);
                    dispose(); // ปิดหน้าปัจจุบัน
                });
            });
            transitionTimer.setRepeats(false);
            transitionTimer.start();
        }
        fadeOverlay.repaint();
    });
    fadeOut.start();
}

    public void playSE(String path, boolean isLoop, float volume) {
        try {
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioIn);
                FloatControl gc = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                gc.setValue(volume);
                if (isLoop) bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                bgmClip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void stopBGM() {
        if (bgmClip != null) { bgmClip.stop(); bgmClip.close(); bgmClip = null; }
    }

    private void animateText(String fullText) {
        isAnimating = true;
        charIndex = 0;
        dialogueArea.setText("");
        if (typewriterTimer != null) typewriterTimer.stop();
        typewriterTimer = new Timer(30, e -> {
            if (charIndex <= fullText.length()) {
                updateDialogueDisplay(fullText.substring(0, charIndex));
                charIndex++;
            } else { stopAnimation(); }
        });
        typewriterTimer.start();
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) {
            try {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                imageCache.put(key, new ImageIcon(img));
            } catch (Exception e) { return null; }
        }
        return imageCache.get(key);
    }

    private void stopAnimation() {
        if (typewriterTimer != null) typewriterTimer.stop();
        isAnimating = false;
    }

    private void updateDialogueDisplay(String text) {
        dialogueArea.setText("<html><body style='width: 950px;'>" + text + "</body></html>");
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
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

    private void performSceneFade(Runnable onBlack) {
        isFading = true; 
        alpha = 0.0f;
        
        // ตรวจสอบว่า fadeOverlay ถูกเพิ่มเข้าไปใน layeredPane หรือยัง
        if (fadeOverlay.getParent() == null) {
            layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        }
        
        // ขั้นตอนที่ 1: Fade Out (ดำมืด)
        Timer fadeOut = new Timer(20, null); // เร็วขึ้นเล็กน้อยเพื่อความกระฉับกระเฉง
        fadeOut.addActionListener(e -> {
            alpha += 0.1f; // เพิ่มความเร็วการ Fade
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                fadeOut.stop();
                
                // เปลี่ยนฉาก/เสียง ในขณะที่จอมืด
                onBlack.run(); 
                
                // รอสักครู่ (100ms) แล้วค่อย Fade In
                Timer waitTimer = new Timer(100, ev -> {
                    ((Timer)ev.getSource()).stop();
                    
                    // ขั้นตอนที่ 2: Fade In (กลับมาสว่าง)
                    Timer fadeIn = new Timer(20, eve -> {
                        alpha -= 0.1f;
                        if (alpha <= 0) {
                            alpha = 0;
                            ((Timer)eve.getSource()).stop();
                            isFading = false;
                        }
                        fadeOverlay.repaint();
                    });
                    fadeIn.start();
                });
                waitTimer.setRepeats(false);
                waitTimer.start();
            }
            fadeOverlay.repaint();
        });
        fadeOut.start();
    }

    private void setupStatusOverlay() {
        statusOverlay = new JPanel();
        statusOverlay.setLayout(new GridLayout(3, 1));
        statusOverlay.setBackground(new Color(0, 0, 0, 180)); // พื้นหลังดำโปร่งแสง
        statusOverlay.setBounds(440, 200, 400, 200); // กึ่งกลางจอ 1280x800
        statusOverlay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        statusOverlay.setVisible(false); // เริ่มต้นให้ซ่อนไว้

        onlineCountLabel = new JLabel("ผู้เล่นออนไลน์: " + relationdata.onlinePlayerCount, SwingConstants.CENTER);
        onlineCountLabel.setForeground(Color.CYAN);
        onlineCountLabel.setFont(new Font("Tahoma", Font.BOLD, 24));

        affinityStatusLabel = new JLabel("ความสัมพันธ์: " + relationdata.aliceRel.getAffinity(), SwingConstants.CENTER);
        affinityStatusLabel.setForeground(Color.PINK);
        affinityStatusLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        
        JLabel hintLabel = new JLabel("กด Tab อีกครั้งเพื่อปิด", SwingConstants.CENTER);
        hintLabel.setForeground(Color.WHITE);
        hintLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));

        statusOverlay.add(onlineCountLabel);
        statusOverlay.add(affinityStatusLabel);
        statusOverlay.add(hintLabel);
        
        layeredPane.add(statusOverlay, JLayeredPane.DRAG_LAYER); // นำไปวางไว้เลเยอร์บนสุด
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        
        new Thread(() -> {
            try {
                Socket socket = new Socket(relationdata.serverIP, 5000);
                networkOut = new PrintWriter(socket.getOutputStream(), true);
                
                // --- ส่วนที่เพิ่ม: ส่งชื่อของเราไปบอก Server ทันทีที่ต่อติด ---
                networkOut.println("SET_NAME:" + relationdata.playerName);
                
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // ... ส่วนที่เหลือคงเดิม (การรับ SYNC_INDEX และ ALL_STATS) ...
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part1().setVisible(true));
    }
}

class VisualNovelBox extends JPanel {
    private int cornerRadius = 30;
    public VisualNovelBox() { setOpaque(false); }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(245, 250, 255, 180), 
            0, getHeight(), new Color(255, 235, 245, 230)
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.setColor(new Color(255, 150, 200, 200));
        g2.setStroke(new BasicStroke(4f));
        g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, cornerRadius, cornerRadius);
    }
}