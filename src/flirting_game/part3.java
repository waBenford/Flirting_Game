package flirting_game;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;

public class part3 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel; 
    private int currentIndex = 0;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Clip bgmClip;      
    private Clip effectClip;   
    private JButton choiceButton1, choiceButton2;
    private boolean isChoosing = false;
    private float alpha = 1.0f; 
    private JPanel fadeOverlay;
    private JLabel affinityLabel, statusLabel;
    private float charAlpha = 0.0f; 
    private Timer charFadeTimer;
    
    // ตัวแปรควบคุมการ Fade ฉาก
    private boolean isFading = false;

    private Map<String, ImageIcon> imageCache = new HashMap<>();

    // --- Array ข้อมูล (เหมือนเดิม) ---
    private String[] imagePaths = {
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s2.png", "res/scene3/s2.png", 
        "res/scene3/s2.png", "res/scene3/s2.png", "res/scene3/s3.png", "res/scene3/s3.png", 
        "res/scene3/s3.png", "res/scene3/s3.png", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg"
    };

    private String[] charPaths = {
        "res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy2.png","res/Charactor/Alice-happy1.png",
        "res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy2.png","res/Charactor/Alice-happy1.png",
        "res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy2.png",
        "res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy2.png","res/Charactor/Alice-happy1.png",
        "res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy2.png","res/Charactor/Alice-happy2.png","res/Charactor/Alice-happy1.png", 
        "res/Charactor/Alice-sad2_1.png","res/Charactor/Alice-sad1_1.png","res/empty.png", "res/empty.png","res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice-cry1_1.png","res/Charactor/Alice-cry2_1.png", 
        "res/Charactor/Alice-cry1_1.png","res/Charactor/Alice-sad1_1.png","res/Charactor/Alice-sad1_1.png","res/Charactor/Alice-sad2_1.png", 
        "res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png",
        "res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png",
        "res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile2_1.png","res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png"
    };

    private String[] names = {
        "อริส", "ฉัน", "ฉัน", "อริส", "ฉัน", "อริส", "อริส", "อริส",
        "อริส", "อริส", "อริส", "ฉัน", "อริส", "อริส", "อริส", "อริส",
        "ฉัน", "ฉัน", "ฉัน", "อริส", "อริส", "ฉัน", "อริส", "อริส", "อริส", "อริส", "อริส", 
        "อริส", "อริส","อริส"," ","ฉัน","อริส","อริส","อริส","อริส","อริส","อริส","อริส",
        "อริส", "ฉัน", "ฉัน", "ฉัน", "ฉัน", "ฉัน", "อริส", "อริส", "ฉัน"
    };
            
    private String[] dialogues = {
        "เมื่อกี้เธอทําอะไรหรอ??", "..ไม่รู้สิ..", "มันเหมือนว่าฉันจะใช้พลังเวทย์ได้เลย..", 
        "เห้ออ..ฉันนึกว่ามีปีศาจมาโจมตีซะอีก", "..เอ๋..ปีศาจอะไรหรอ??", "..หาา..นี่เธอไม่รู้จริงๆหรอ??",
        "เเต่ก็ช่างมันเถอะ เดี๋ยวฉันจะเล่าทุกอย่างให้ฟังละกัน??", "ในโลกนี้หนะ เป็นโลกที่ผู้คนก็ต่างใช้พลังเวทย์กันได้",
        "เเต่ก็มีบางคนที่ไม่สามารถใช้มันได้", "เเต่ถึงอย่างงั้นก็มีคนที่สามารถไต่เต้าไปจนถึงระดับสูง",
        "เเม้จะไม่มีพลังเวทย์ก็ตาม", "..เอ่อ..เเล้วปีศาจหละ??", "อ้อ..จริงด้วยเกือบลืมไปเลย",
        "โลกนี้จะมีสองเผ่าอยู่หลักๆ", "เผ่ามนุษย์เเละเผ่าปีศาจ", 
        "ไม่เหมือนกันสักหน่อย ปีศาจหน่ะเป็นเผ่าที่ชั่วร้าย", 
        "อันนี้ฉันก็ไม่รู้เหมือนกัน", "เเล้วเธออยู่บ้านคนเดียวหรอ??",
        "..พ่อกับเเม่เธอหละ??", "ฉันอยู่คนเดียวมาตั้งเเต่เด็กๆเเล้วหละ",
        "พ่อกับเเม่ของฉันท่านเสียไปนานเเล้ว", "เอ่อ..เธอพอจะเล่าให้ฉันฟังได้มั้ย",
        "..มันเป็นเรื่องเมื่อ6ปีที่เเล้ว", "หมู่บ้านของฉัน พวกเราอยู่กันอย่างมีความสุข",
        "ผู้คนก็ต่างอยู่ด้วยกันอย่างเอื้อเฟื้อ เเละพอเพียง", "จนกระทั่ง", 
        "มีปีศาจที่เเข็งเเกร่งตัวนึง ได้มาทําลายหมู่บ้านของพวกเรา",
        "มันพรากชีวิตของผู้คนไปมากมาย หนึ่งในนั้นก็มีพ่อเเม่ของฉันด้วย",
        "พ่อเเม่ของฉันปกป้องฉันจนวินาทีสุดท้าย..", "จากเหตุการณ์ครั้งนั้น ฉันเลยรอดมาได้..",
        "อริสกําลังเศร้า..", "ขอโทษนะที่ถามอะไรเเบบนั้น", "ไม่เป็นไรหหรอก", 
        "ขอบคุณนะ..", "...", "ฉันเลยคิดว่าสักวันนึง ฉันจะต้องออกเดินทาง",
        "ฝึกฝนตัวเองให้เเข็งเเกร่งมากขึ้น", "เพื่อที่ฉันจะได้เเก้เเค้นให้พ่อกับเเม่",
        "นี่...", "เธออยากจะร่วมเดินทางกับฉันมั้ย?",
        "เธอเป็นคนที่จิตใจดี เเละอ่อนโยนมาก", "เพราะอย่างงั้นฉันเลยอยากที่จะปกป้องเธอ",
        "ไม่มีเหตุผลเลยที่ฉันปฏิเสธเธอ", "เเ่นนอน!! ฉันจะออกเดินทางกับเธอ",
        "ฉันจะต้องเเข็งเเกร่งขึ้นให้ได้เหมือนกัน", "ขอบคุณนะ …",
        "เออ..ว่าเเต่เธอชื่ออะไรกันเเน่", "ฉันชื่อ..."
    };

    public part3() {
        setTitle("ISEKAI DEMO - Part 3");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);
        
        playSE("res/sound/soundtrack3.wav", true, -10.0f); 
        playSE("res/sound/fireplace.wav", true, -5.0f); 

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        characterLabel.setBounds(75, 50, 900, 1100); 
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        setupStatusUI();

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

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNext();
            }
        });
    }

    private void handleNext() {
        if (isChoosing || isFading) return; // ล็อคการกดถ้ากำลัง Fade

        if (isTyping) {
            typewriterTimer.stop();
            isTyping = false;
            dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        if (currentIndex == 14) {
            showChoices("..ปีศาจนี่เหมือนผีรึเปล่า??", "..เอ่อ..แล้วเผ่าอื่นๆหละ??", 15, 16);
            return;
        }
        if (currentIndex == 32) {
            showChoices("เข้าไปปลอบอริส", "นั่งอยู่เฉยๆ", 33, 34);
            return;
        }

        int nextIndex = currentIndex;
        if (currentIndex == 15 || currentIndex == 16) nextIndex = 17;
        else if (currentIndex == 33 || currentIndex == 34) nextIndex = 35;
        else nextIndex++;

        if (nextIndex < dialogues.length) {
            String currentBG = imagePaths[Math.min(currentIndex, imagePaths.length-1)];
            String nextBG = imagePaths[Math.min(nextIndex, imagePaths.length-1)];
            
            currentIndex = nextIndex;

            // ตรวจสอบการเปลี่ยนภาพพื้นหลังเพื่อทำ Fade
            if (!currentBG.equals(nextBG)) {
                triggerSceneFade();
            } else {
                updateScene();
            }
        } else {
            stopBGM();
            JOptionPane.showMessageDialog(null, "จบ Part 3 แล้ว!");
            new part4().setVisible(true);
            dispose(); 
        }
    }

    // เมธอดสำหรับ Fade ให้สมูทและค้างหน้าจอดำ 2 วินาที
    private void triggerSceneFade() {
        isFading = true;
        if (fadeOverlay.getParent() == null) {
            layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        }

        // 1. Fade Out: ค่อยๆ ดำสนิท (ใช้เวลาประมาณ 0.5 วินาที)
        Timer fadeOut = new Timer(15, null);
        fadeOut.addActionListener(e -> {
            alpha += 0.03f; // Step เล็กๆ เพื่อความสมูท
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                fadeOut.stop();
                
                updateScene(); // เปลี่ยนฉากขณะมืดสนิท
                
                // 2. Black Pause: ค้างไว้ 2 วินาที (2000 ms)
                Timer pauseTimer = new Timer(750, ev -> {
                    ((Timer)ev.getSource()).stop();
                    
                    // 3. Fade In: ค่อยๆ กลับมาสว่าง
                    Timer fadeIn = new Timer(15, null);
                    fadeIn.addActionListener(evt -> {
                        alpha -= 0.03f;
                        if (alpha <= 0) {
                            alpha = 0;
                            fadeIn.stop();
                            isFading = false;
                            layeredPane.remove(fadeOverlay);
                        }
                        fadeOverlay.repaint();
                    });
                    fadeIn.start();
                });
                pauseTimer.start();
            }
            fadeOverlay.repaint();
        });
        fadeOut.start();
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        backgroundLabel.setIcon(getOptimizedImage(imagePaths[Math.min(currentIndex, imagePaths.length-1)], 1000, 800));
        
        if (currentIndex < charPaths.length) {
            String newPath = charPaths[currentIndex];
            String oldPath = (currentIndex > 0) ? charPaths[currentIndex - 1] : "";
            characterLabel.setIcon(getOptimizedImage(newPath, 900, 1100));

            if (!newPath.equals(oldPath) || currentIndex == 0) {
                startCharacterFadeIn();
            }
        }
        
        updateDialogueDisplay(dialogues[currentIndex]);
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    // ... (ส่วนที่เหลือของเมธอด setupUI, Sound, Animation อื่นๆ คงเดิม) ...

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox(); 
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 550, 900, 180);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel("");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(180, 40, 90)); 
        nameLabel.setBounds(60, 10, 300, 40);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115)); 
        dialogueArea.setBounds(60, 60, 800, 110);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialoguePanel.add(dialogueArea);

        JLabel nextArrow = new JLabel("▼");
        nextArrow.setFont(new Font("Arial", Font.BOLD, 20));
        nextArrow.setForeground(new Color(0, 153, 255));
        nextArrow.setBounds(850, 130, 30, 30);
        dialoguePanel.add(nextArrow);
        Timer arrowTimer = new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible()));
        arrowTimer.start();
    }

    private void setupStatusUI() {
        JPanel relPanel = new JPanel(new GridLayout(2, 1));
        relPanel.setBounds(20, 20, 250, 60);
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

    private void updateDialogueDisplay(String text) {
        if (typewriterTimer != null) typewriterTimer.stop();
        charIndex = 0;
        isTyping = true;
        dialogueArea.setText(""); 
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 750px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else {
                ((Timer)e.getSource()).stop();
                isTyping = false;
            }
        });
        typewriterTimer.start();
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
            return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    public void playEffect(String path, float volume) {
        try {
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip temporaryClip = AudioSystem.getClip(); 
                temporaryClip.open(audioIn);
                FloatControl gainControl = (FloatControl) temporaryClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                temporaryClip.start();
                temporaryClip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) temporaryClip.close();
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void playSE(String path, boolean loop, float volume) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) return;
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            if (path.contains("soundtrack")) this.bgmClip = clip;
            else this.effectClip = clip;
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void stopBGM() {
        if (bgmClip != null) { bgmClip.stop(); bgmClip.close(); bgmClip = null; }
    }

    private void stopEffect() {
        if (effectClip != null) { effectClip.stop(); effectClip.close(); effectClip = null; }
    }

    private void handleSoundEffects(int index) {
        if (index == 15) playEffect("res/sound/chikauyo.wav", 5.0f);
        else if (index == 16) playEffect("res/sound/wakarunai.wav", 5.0f);
        if (index == 20) { stopBGM(); playSE("res/sound/soundtrack4.wav", true, -5.0f); }
        if (index == 22) { stopEffect(); playSE("res/sound/village.wav", true, -5.0f); }
        if (index == 26) { stopEffect(); screenShake(10, 1000); playSE("res/sound/monster.wav", false, -10.0f); playSE("res/sound/housefire.wav", false, -10.0f); }
        if (index == 30) { stopEffect(); playSE("res/sound/fireplace.wav", true, 0.0f); playEffect("res/sound/cry.wav", 5.0f); }
        if (index == 33) playEffect("res/sound/Arigato.wav", 5.0f);
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 350, t1);
        choiceButton2 = createChoiceButton(text2, 420, t2);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text);
        btn.setBounds(580, y, 350, 60);
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 30, 30, 220));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 2));
        btn.addActionListener(e -> {
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false;
            if (target == 33) relationdata.aliceRel.addAffinity(10);
            else if (target == 34) relationdata.aliceRel.decreaseAffinity(5);
            affinityLabel.setText("ความสนิท: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            currentIndex = target;
            updateScene();
        });
        return btn;
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(50, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0;
                ((Timer)e.getSource()).stop();
                layeredPane.remove(fadeOverlay);
                updateScene(); 
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
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

    private void startCharacterFadeIn() {
        charAlpha = 0.0f;
        if (charFadeTimer != null && charFadeTimer.isRunning()) charFadeTimer.stop();
        charFadeTimer = new Timer(30, e -> {
            charAlpha += 0.05f;
            if (charAlpha >= 1.0f) {
                charAlpha = 1.0f;
                ((Timer)e.getSource()).stop();
            }
            characterLabel.repaint();
        });
        charFadeTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part3().setVisible(true));
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
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(6, 6, getWidth() - 12, getHeight() - 12, cornerRadius - 5, cornerRadius - 5);
    }
}