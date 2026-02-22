package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;
import javax.swing.*;

public class part2 extends JFrame {
    // --- UI Components ---
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel; // เปลี่ยนเป็น VisualNovelBox จาก Part 3
    private JButton choiceButton1, choiceButton2;
    private JPanel fadeOverlay;
    private JLabel affinityLabel, statusLabel;

    // --- Game Logic State ---
    private int currentIndex = 0;
    private int charIndex = 0;
    private boolean isAnimating = false;
    private boolean isChoosing = false;
    private float alpha = 1.0f;
    private Timer typewriterTimer;

    // --- Audio ---
    private Clip bgmClip;
    private Clip effectClip;

    // ระบบ Cache เพื่อความ Smooth
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    // --- Data Arrays (คงเดิมตาม Part 2) ---
    private String[] imagePaths = {
        "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
        "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
        "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s2.png",
        "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s3.png",
        "res/scene2/s3.png", "res/scene2/s3.png", "res/scene2/s4.png", "res/scene2/s4.png",
        "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png",
        "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png",
        "res/scene2/s6.png", "res/scene2/s6.png"
    };

    private String[] charPaths = {
        "res/scene2/alice1.png", "res/scene2/alice1.png", "res/scene2/alice1.png",
        "res/scene2/alice2.png", "res/scene2/alice1.png", "res/scene2/alice2.png",
        "res/scene2/alice1.png", "res/scene2/alice2.png", "res/scene2/alice1.png",
        "res/scene2/alice2.png", "res/scene2/alice1.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png"
    };

    private String[] names = {
        "ฉัน", "ฉัน", "เด็กผู้หญิง", "เด็กผู้หญิง", "ฉัน", "เด็กผู้หญิง", "อริส",
        "อริส", "อริส", "อริส", "อริส", "อริส", "อริส", "ฉัน", "อริส", "ฉัน", "ฉัน", "...", "ฉัน"
    };

    private String[] dialogues = {
        "..เอ่อ..เธอคือ ใครหรอ?", "..เเล้วนี่ฉันอยู่ที่ไหน..ฉันยังไม่ตายหรอ!?", "..เอ๋..ตายหรอ??",
        "ไม่เห็นจะมีใครตายเลยนะ", "นี่เรา..อยู่ที่ไหนกันเเน่นะ..", "อ๊ะ..ลืมเเนะนําตัวเลย",
        "ฉันชื่อว่า อริส เป็นลูกของชาวนาในหมู่บ้านเเถวนี้", "เเล้วเธอหละ?", "งั้นหรอ...ไม่เป็นไร",
        "อ้าว... จำชื่อไม่ได้หรอ? ไม่เป็นไรนะ ค่อยๆ นึกไปก็ได้", "อืมม..งั้นเดี๋ยว..ไปที่บ้านฉันก่อนละกัน",
        "ตัวเธอสะบักสะบอมมากเลย", "ไปอาบนํ้าก่อนเลยนะ เดี๋ยวฉันจะเตรียมกับข้าวไว้ให้",
        "ห้องนํ้าไปทางไหนหรอ?", "ขึ้นบันไดไปเเล้วก็เลี้ยวขวาหนะ", "น่ารักเเถมยังใจดีอีกต่างหาก",
        "..ต่อจากนี้จะทําไงต่อดีนะ..เห้ออ..", "...", "เอื้อมมือไปหยิบ", "...",
        "ไม่เคยเห็นหนังสือเเบบนี้มาก่อนเลย", "..เปิดอ่าน..", "..การใช้เวทย์ลมขั้นพื้นฐาน..",
        "..พลังเวทย์อย่างงั้นนะหรอ..", "..ดูเหมือนจะมีวิธีการร่ายเวทย์ด้วย.. ",
        "..ลองหน่อยละกัน..ยังไงมันก็คงเป็นหนังสือที่ทําขึ้นมาเล่นๆ",
        "..สายลมที่พัดผ่าน..จงตอบรับเสียงของฉัน!!", "..Wind Dash!!",
        "..เห้ย!!..เมื่อกี้มันอะไร?!", "เกิดอะไรขึ้นหนะ!!"
    };

    public part2() {
        setTitle("ISEKAI DEMO - Part 2: First Encounter");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // --- Start Audio ---
        playSE("res/sound/soundtrack1.wav", true, -5.0f);
        playSE("res/sound/soonoo.wav", false, 5.0f);

        // --- Background & Character ---
        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel(scaleImage(charPaths[0], 1000, 800));
        characterLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        // --- Setup UI ---
        setupDialogueUI();
        setupRelationshipUI();
        setupFadeOverlay();

        startFadeIn();
        animateText(dialogues[0]);

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleInteraction();
            }
        });
    }

    private void setupDialogueUI() {
        // ใช้ VisualNovelBox จาก Part 3
        dialoguePanel = new VisualNovelBox();
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 550, 900, 180);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel(names[0]);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(180, 40, 90));
        nameLabel.setBounds(60, 10, 300, 40);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 60, 810, 110);
        dialoguePanel.add(dialogueArea);

        JLabel nextArrow = new JLabel("▼");
        nextArrow.setFont(new Font("Arial", Font.BOLD, 20));
        nextArrow.setForeground(new Color(0, 153, 255));
        nextArrow.setBounds(850, 130, 30, 30);
        dialoguePanel.add(nextArrow);
        Timer arrowTimer = new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible()));
        arrowTimer.start();
    }

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(2, 1));
        relPanel.setBounds(25, 25, 250, 60);
        relPanel.setOpaque(false);

        affinityLabel = new JLabel("ความสนิท: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        affinityLabel.setForeground(Color.WHITE);

        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        statusLabel.setForeground(new Color(255, 204, 0));

        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }

    private void setupFadeOverlay() {
        fadeOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fadeOverlay.setBounds(0, 0, 1000, 800);
        fadeOverlay.setOpaque(false);
        layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
    }

    private void handleInteraction() {
        if (isChoosing) return;
        if (isAnimating) {
            stopAnimation();
            updateDialogueDisplay(dialogues[currentIndex]);
            return;
        }

        if (currentIndex == 17) stopBGM();
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
            finishPart();
        }
    }

    private void finishPart() {
        stopBGM();
        UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 18));
        JOptionPane.showMessageDialog(null, "จบ part 2 แล้ว! กำลังเข้าสู่บทถัดไป...");
        new part3().setVisible(true);
        dispose();
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) animateText(dialogues[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1000, 800));
        if (currentIndex < charPaths.length) characterLabel.setIcon(scaleImage(charPaths[currentIndex], 1000, 800));
    }

    private void handleSoundEffects(int index) {
        if (index == 8 || index == 9) playEffect("res/sound/soudesukaa.wav", 5.0f);
        else if (index == 15) playEffect("res/sound/water.wav", 5.0f);
        else if (index == 11) playEffect("res/sound/fireplace.wav", 5.0f);
        else if (index == 18) playSE("res/sound/soundtrack2.wav", true, -5.0f);
        else if (index == 28) {
            playEffect("res/sound/winddash.wav", 0.0f);
            screenShake(10, 1000);
        }

        if (index == 10 || index == 14 || index == 18 || index >= 29) {
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

    private void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
    }

    private void animateText(String fullText) {
        isAnimating = true;
        charIndex = 0;
        dialogueArea.setText("");
        if (typewriterTimer != null && typewriterTimer.isRunning()) typewriterTimer.stop();
        
        typewriterTimer = new Timer(20, e -> {
            if (charIndex <= fullText.length()) {
                updateDialogueDisplay(fullText.substring(0, charIndex));
                charIndex++;
            } else {
                stopAnimation();
            }
        });
        typewriterTimer.start();
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(50, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0;
                ((Timer)e.getSource()).stop();
                layeredPane.remove(fadeOverlay);
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
        dialogueArea.setText("<html><body style='width: 750px;'><span style='text-shadow: 1px 1px 2px black;'>"
                + text + "</span></body></html>");
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) {
            imageCache.put(key, scaleImage(path, w, h));
        }
        return imageCache.get(key);
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    private void showChoices(String text1, String text2, int target1, int target2) {
        if (choiceButton1 != null) layeredPane.remove(choiceButton1);
        if (choiceButton2 != null) layeredPane.remove(choiceButton2);
        
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 350, target1);
        choiceButton2 = createChoiceButton(text2, 420, target2);

        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int targetIndex) {
        JButton btn = new JButton(text);
        btn.setBounds(580, y, 350, 60);
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 30, 30, 220));
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 2));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(70, 70, 70, 240));
                btn.setBounds(570, y, 360, 60);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(30, 30, 30, 220));
                btn.setBounds(580, y, 350, 60);
            }
        });

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
            layeredPane.repaint();
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

class VisualNovelBox extends JPanel {
    private int cornerRadius = 30;
    public VisualNovelBox() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- Background Gradient (ขาวไปชมพูอ่อน กึ่งโปร่งใสแบบ Part 3) ---
        GradientPaint gradient = new GradientPaint(
        0, 0, new Color(245, 250, 255, 180), 
        0, getHeight(), new Color(255, 235, 245, 230)
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // --- Outer Border (สีชมพูเข้ม) ---
        g2d.setColor(new Color(255, 150, 200, 200));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
        
        // --- Inner Glow (เส้นขอบขาวบางๆ ด้านใน) ---
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(6, 6, getWidth() - 12, getHeight() - 12, cornerRadius, cornerRadius);
        g2d.dispose();
    }
}