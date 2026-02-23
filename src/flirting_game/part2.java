package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;

public class part2 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private RoundedPanel dialoguePanel;
    private int currentIndex = 0;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isAnimating = false;
    private Clip bgmClip;      
    private Clip effectClip;   
    private JButton choiceButton1, choiceButton2;
    private boolean isChoosing = false; 
    private float alpha = 1.0f; 
    private JPanel fadeOverlay;
    private JLabel affinityLabel; 
    private JLabel statusLabel;

    // --- ฟอนต์ภาษาไทย (ปรับขนาดให้เข้ากับจอ 1280) ---
    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    private String[] imagePaths = {
            "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
            "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
            "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s2.png", 
            "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s3.png", 
            "res/scene2/s3.png", "res/scene2/s3.png", "res/scene2/s4.png", "res/scene2/s4.png", 
            "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", 
            "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", 
            "res/scene2/s6.png", "res/scene2/s6.png",   
    };
    
    private String[] charPaths = {
            "res/scene2/alice1.png", "res/scene2/alice1.png", "res/scene2/alice1.png",  
            "res/scene2/alice2.png", "res/scene2/alice1.png", "res/scene2/alice2.png",
            "res/scene2/alice1.png", "res/scene2/alice2.png", "res/scene2/alice1.png", 
            "res/scene2/alice2.png", "res/scene2/alice1.png","res/empty.png", "res/empty.png",
            "res/empty.png","res/empty.png", "res/empty.png", "res/empty.png","res/empty.png","res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png"
    };
    
    private String[] names = {
        "ฉัน", "ฉัน", "เด็กผู้หญิง", "เด็กผู้หญิง",
        "ฉัน", "เด็กผู้หญิง", "อริส", 
        "อริส", "อริส", "อริส", 
        "อริส", "อริส", "อริส", "ฉัน",
        "อริส", "ฉัน",  "ฉัน", "...", "ฉัน",
    };

    private String[] dialogues = {
    "..เอ่อ..เธอคือ ใครหรอ?", // 0
    "..เเล้วนี่ฉันอยู่ที่ไหน..ฉันยังไม่ตายหรอ!?", // 1
    "..เอ๋..ตายหรอ??", // 2
    "ไม่เห็นจะมีใครตายเลยนะ", // 3
    "นี่เรา..อยู่ที่ไหนกันเเน่นะ..", // 4
    "อ๊ะ..ลืมเเนะนําตัวเลย", // 5
    "ฉันชื่อว่า อริส เป็นลูกของชาวนาในหมู่บ้านเเถวนี้", // 6
    "เเล้วเธอหละ?", // 7 (จุดขึ้น Choice)
    "งั้นหรอ...ไม่เป็นไร", // 8 
    "อ้าว... จำชื่อไม่ได้หรอ? ไม่เป็นไรนะ ค่อยๆ นึกไปก็ได้", // 9
    "อืมม..งั้นเดี๋ยว..ไปที่บ้านฉันก่อนละกัน", // 10
    "ตัวเธอสะบักสะบอมมากเลย",
    "ไปอาบนํ้าก่อนเลยนะ เดี๋ยวฉันจะเตรียมกับข้าวไว้ให้",
    "ห้องนํ้าไปทางไหนหรอ?",
    "ขึ้นบันไดไปเเล้วก็เลี้ยวขวาหนะ",
    "น่ารักเเถมยังใจดีอีกต่างหาก",
    "..ต่อจากนี้จะทําไงต่อดีนะ..เห้ออ..",
    "...",
    "เอื้อมมือไปหยิบ",
    "...",
    "ไม่เคยเห็นหนังสือเเบบนี้มาก่อนเลย",
    "..เปิดอ่าน..",
    "..การใช้เวทย์ลมขั้นพื้นฐาน..",
    "..พลังเวทย์อย่างงั้นนะหรอ..",
    "..ดูเหมือนจะมีวิธีการร่ายเวทย์ด้วย.. ",
    "..ลองหน่อยละกัน..ยังไงมันก็คงเป็นหนังสือที่ทําขึ้นมาเล่นๆ",
    "..สายลมที่พัดผ่าน..จงตอบรับเสียงของฉัน!!",
    "..Wind Dash!!",
    "..เห้ย!!..เมื่อกี้มันอะไร?!",
    "เกิดอะไรขึ้นหนะ!!",
    };

    public part2() {
        setTitle("ISEKAI DEMO - Part 2: First Encounter");
        setSize(1280, 800); // แก้ขนาดเฟรม
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playSE("res/sound/soundtrack1.wav", true, -5.0f);
        playSE("res/sound/soonoo.wav", false, 5.0f);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // ปรับพิกัดตัวละครมาตรฐาน (190, 100, 900, 900) เพื่อไม่ให้บีบ
        characterLabel = new JLabel();
        characterLabel.setBounds(190, 100, 900, 900);
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();

        JPanel relPanel = new JPanel();
        relPanel.setLayout(new GridLayout(2, 1));
        relPanel.setBounds(20, 20, 300, 70); // ขยายขอบเขตจอ
        relPanel.setOpaque(false);

        affinityLabel = new JLabel("ความสนิท: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        affinityLabel.setForeground(Color.WHITE);

        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        statusLabel.setForeground(new Color(255, 204, 0));

        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);

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
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isChoosing) return;
                if (isAnimating) {
                    stopAnimation();
                    updateDialogueDisplay(dialogues[currentIndex]);
                    return;
                }

                if (currentIndex == 8) {
                    currentIndex = 10;
                    updateScene();
                    return;
                }

                if (currentIndex == 7) {
                    showChoices("ฉันไม่รู้ (ฉันยังไม่ไว้ใจใคร)", "ฉันจําชื่อตัวเองไม่ได้", 8, 9); 
                    return; 
                }

                currentIndex++;
                if (currentIndex < dialogues.length) {
                    handleSoundEffects(currentIndex);
                    updateScene();
                } else {
                    stopBGM();
                    UIManager.put("OptionPane.messageFont", THAI_FONT_PLAIN);
                    JOptionPane.showMessageDialog(null, "จบ part 2 แล้ว! กำลังเข้าสู่เนื้อเรื่องบทที่ 3...");
                    new part3().setVisible(true);
                    dispose(); 
                }
            }
        });
    }

    private void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
    }

    private void setupDialogueUI() {
        dialoguePanel = new RoundedPanel(50);
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(90, 520, 1100, 220); // ขนาดมาตรฐานจอใหญ่
        dialoguePanel.setBackground(new Color(20, 20, 25, 215));
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel();
        nameLabel.setFont(THAI_FONT_BOLD);
        nameLabel.setForeground(new Color(255, 204, 0)); 
        nameLabel.setBounds(60, 25, 400, 45); 
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(THAI_FONT_PLAIN);
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 85, 980, 110); 
        dialoguePanel.add(dialogueArea);
    }

    private void updateScene() {
        if (currentIndex < names.length) {
            nameLabel.setText(names[currentIndex]); 
        }
        if (currentIndex < dialogues.length) {
            animateText(dialogues[currentIndex]);
        }
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1280, 800));
        if (currentIndex < charPaths.length) {
            characterLabel.setIcon(scaleImage(charPaths[currentIndex], 900, 900));
            characterLabel.setBounds(190, 0, 900, 900);
        }
    }

    private void handleSoundEffects(int index) {
        if (index == 8 || index == 9) playEffect("res/sound/soudesukaa.wav", 5.0f);
        if (index == 18) playEffect("res/sound/soundtrack2.wav", -5.0f);
        if (index == 15) playEffect("res/sound/water.wav", 5.0f);
        if (index == 11) playEffect("res/sound/fireplace.wav", 5.0f);
        if (index == 28) {
            playEffect("res/sound/winddash.wav", 0.0f);
            screenShake(10, 1000);
        }
        if (index == 17 || index == 29 || index == 30 || index == 14) {
            if (effectClip != null && effectClip.isRunning()) effectClip.stop();
        }
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
                if (isLoop) {
                    bgmClip = clip;
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                clip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
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

    private void showChoices(String text1, String text2, int target1, int target2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 380, target1); 
        choiceButton2 = createChoiceButton(text2, 480, target2); 
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int targetIndex) {
        JButton btn = new JButton(text);
        btn.setBounds(415, y, 450, 75); // ปรับกึ่งกลางจอ 1280
        btn.setFont(new Font("Tahoma", Font.BOLD, 22));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 30, 30, 220));
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 2));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false;
            if (targetIndex == 9) relationdata.aliceRel.addAffinity(10);
            else if (targetIndex == 8) relationdata.aliceRel.decreaseAffinity(5);
            affinityLabel.setText("ความสนิท: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            currentIndex = targetIndex;
            handleSoundEffects(currentIndex);
            updateScene();
        });

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);
                super.paint(g, c);
            }
        });
        return btn;
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
        SwingUtilities.invokeLater(() -> new part2().setVisible(true));
    }
}