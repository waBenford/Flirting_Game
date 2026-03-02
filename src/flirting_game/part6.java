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

public class part6 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel;
    private float alpha = 1.0f;
    private JPanel fadeOverlay;
    private int currentIndex = 0;
    private Clip bgmClip;      
    private Clip effectClip;
    private JButton choiceButton1, choiceButton2;
    private boolean isChoosing = false;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private boolean isFading = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();
    
    // UI ระบบความสัมพันธ์และ Network ที่ส่งต่อมาจาก Part 5
    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    private java.io.PrintWriter networkOut;

    private String lastBgPath = "";
    private String lastCharPath = "";

    private float charAlpha1 = 0.0f; 
    private Timer charFadeTimer1;
    
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    private String[] imagePaths = {
       "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png",
       "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png",
       "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png",
       "res/scene6/s2new.png"
    };
    
    private String[] charPaths = {
       "res/scene5/Alice-shower2.png", "res/scene5/Alice-shower2.png", "res/scene5/Alice-shower2.png", "res/Charactor/Dan/dan-showhand1.png",
       "res/Charactor/Dan/dan-showhand1.png", "res/scene5/Alice-shower3.png", "res/scene5/Alice-shower3.png", "res/scene5/Alice-shower3.png", 
       "res/scene5/Alice-shower1.png", "res/scene5/Alice-shower1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Alice/Girl/Alice-shy2.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/Charactor/Alice/Girl/Alice-shy2.png","res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Dan/dan-normal2.png","res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/Charactor/Alice/Girl/Alice-normal2.png", "res/empty.png", "res/empty.png", "res/empty.png","res/empty.png", 
    };
    
    private String[] names = { 
            "อริส", "อริส", "อริส", "คนลึกลับ", "ฉัน", "อริส", "ฉัน", "อริส", 
            "อริส", "อริส", "ฉัน", "Dan", "Dan", "อริส", "อริส", "อริส", 
            "Dan", "Dan", "ฉัน", "ฉัน", "Dan", "Dan", "ฉัน", "ฉัน", 
            "ฉัน", "ฉัน", "ฉัน", "ฉัน", "Dan", "Dan", "Dan", "Dan", 
            "ฉัน", "อริส", "อริส", "ฉัน", "อริส","อริส", "Dan", "Dan", 
            "ฉัน", "Dan", "Dan", "ฉัน", "อริส", " ", " ", " ", " "
            };
    
    private String[] dialogues = {
        "ใครอยู่ตรงนั้น!?", "ออกมาเดี๋ยวนี้นะ!!", "ถ้าไม่ออกมา ฉันจะใช้พลังเวทย์ใส่เเกทั้งเเบบนี้เเหละ", 
        "ใจเย็นก่อน ฉันไม่ได้คิดร้าย", "อริส เกิดอะไรขึ้น!!", "นายไม่ต้องวิ่งมาขนาดนั้นก็ได้ ฉันไม่ได้เป็นอะไรสักหน่อย",
        "ก็ฉันได้ยินเสียงเธอร้องนี่", "นายเป็นห่วงฉันขนาดนั้นเลยหรอ?", 
        "นะ…นายพูดอะไรแบบนั้นกัน…","อือ ก็จริงของนาย", 
        "สรุปเเล้วนายเป็นใครกัน?", "ฉันชื่อ Dan เป็นนักผจญภัยหนะ", "พวกเธอสองคนเป็นคู่รักกันหรอ?", 
        "ดะ…เดี๋ยวสิ! ใครบอกว่าเป็นแบบนั้น!", "นะ…นายพูดอะไรของนายเนี่ย!!", 
        "เเล้วทําไมเมื่อกี้ต้องซ่อนด้วย", "ฉันไม่ได้จะเเอบดูเธอหรอกนะ ฉันเเค่เดินผ่านมา", "เเล้วพวกเธอหละชื่ออะไร กําลังจะไปที่ไหนกัน?", 
        "ฉันชื่อ..(ชื่อตัวละครเรา) ส่วนนี่ก็ อริส", "พวกเรากําลังจะมุ่งหน้าไปที่ป่า Death End", "Death End หรอ..",
        "มีเหตุผลอะไรที่พวกเธอต้องไปที่เเบบนั้นหรอ?", "ช่วงนี้เริ่มมีปีศาจโจมตี ในหลายๆพื้นที่", "เเละดูเหมือนว่าจะมีคนที่คอยสั่งเจ้าพวกนั้น",
        "คนที่สามารถสั่งเจ้าพวกนั้นได้คงต้องเป็นคนที่เเข็งเเกร่งมากเเน่ๆ", "คนเดียวที่ทําเเบบนั้นได้ คือจอมมาร", 
        "เพราะเเบบนั้นพวกเราเลยออกเดินทางเพื่อไปยังที่อยู่ของจอมมาร","เเหละจบเรื่องนี้ จะได้ไม่มีผู้คนต้องบาดเจ็บ",
        "มันค่อนข้างอันตรายนะ", "ป่า Death End เป็นป่าที่มีความซับซ้อนของเส้นทาง", 
        "อีกทั้งยังมีปีศาจเเละต้นไม้อาถรรพ์ที่สามารถทําร้ายเราได้ตลอดเวลา","ฉันก็เคยเข้าไปครั้งนึง เเต่ก็สามารถรอดออกมาได้", 
        "งั้นนายช่วยมาร่วมเดินทางกับพวกเราหน่อยจะได้มั้ย", "นี่นายเเน่ใจเเล้วหรอ?", "หมอนั่นอาจจะเป็นคนไม่ดีก็ได้นะ",
        "ไม่เป็นไรหรอก ดูเเล้วคนๆนี้ก็ไม่น่ามีพิษภัยอะไร", 
        "เข้าใจเเล้ว...","เอาตามนั้นก็ได้", 
        "ดูเหมือนพวกเธอจะสนิทกันดีนะ", "ถ้าอย่างนั้น...ฉันจะนําทางพวกเธอไปที่ป่า Death End เอง","จริงหรอ!?",
        "แต่เส้นทางมันยาวนะ พวกเธอคงต้องเตรียมตัวให้พร้อม", "เพราะถ้าเข้าไปในป่านั้นแล้ว...จะไม่มีทางถอยกลับง่ายๆ",
        "ไม่เป็นไรหรอก พวกเราตัดสินใจแล้ว", "อือ!","หลังจากนั้น พวกเราเริ่มออกเดินทางไปยังป่า Death End",
        "การเดินทางที่ยาวนานได้เริ่มต้นขึ้น", "เวลาผ่านไปหลายสัปดาห์...","...", 
    };

    public part6() {
        setTitle("ISEKAI DEMO - Part 6: Hidden Shadow");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                stopAllSounds(); 
                System.exit(0);
            }
        });

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playSE("res/sound/soundtrack12.wav", true, -10.0f);
        playEffect("res/sound/ahhhhh.wav", 0.0f);

        // 1. พื้นหลัง
        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // 2. ตัวละคร
        characterLabel = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha1)); 
                super.paintComponent(g2d); 
                g2d.dispose();
            }
        };
        characterLabel.setBounds(40, 100, 1200, 900); 
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        // อัปเดตชื่อตัวละครจาก relationdata แบบที่ Part 5 ทำ
        for (int i = 0; i < dialogues.length; i++) {
            if (dialogues[i].contains("(ชื่อตัวละครเรา)")) {
                dialogues[i] = dialogues[i].replace("(ชื่อตัวละครเรา)", relationdata.playerName);
            }
        }

        setupDialogueUI();
        
        // ติดตั้งระบบความสัมพันธ์และ Network ให้ทำงานต่อจาก Part 5
        setupRelationshipUI();
        setupStatusOverlay();
        setupTabKeyBinding();
        initNetwork();
        
        // สร้างฉาก Fade In
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
                handleNext();
            }
        });
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox();
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(225, 520, 800, 200);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(180, 40, 90));
        nameLabel.setBounds(60, 10, 300, 40);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 60, 800, 110);
        dialoguePanel.add(dialogueArea);

        JLabel nextArrow = new JLabel("▼");
        nextArrow.setFont(new Font("Arial", Font.BOLD, 20));
        nextArrow.setForeground(new Color(0, 153, 255));
        nextArrow.setBounds(750, 130, 30, 30);
        dialoguePanel.add(nextArrow);
        Timer arrowTimer = new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible()));
        arrowTimer.start();
    }

    private void updateScene() {
        if (currentIndex < names.length) {
            nameLabel.setText(names[currentIndex]);
        }

        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);

        // --- เช็คภาพพื้นหลัง: ถ้าเป็นภาพเดิม ไม่ต้องโหลดใหม่ ---
        if (currentIndex < imagePaths.length) {
            String currentBg = imagePaths[currentIndex];
            if (!currentBg.equals(lastBgPath)) {
                backgroundLabel.setIcon(getOptimizedImage(currentBg, 1280, 800));
                lastBgPath = currentBg;
            }
        }

        if (currentIndex < charPaths.length) {
            String currentChar = charPaths[currentIndex];
            if (!currentChar.equals(lastCharPath)) {
                // ถ้าเปลี่ยนจากไม่มีตัวละคร เป็นมีตัวละคร หรือเปลี่ยนรูปใหม่ ให้ทำ Fade
                if (!currentChar.contains("empty")) {
                    startCharacterFadeIn();
                } else {
                    charAlpha1 = 0.0f; // ถ้าเป็น empty ให้หายไปทันที
                }

                if (currentChar.contains("dan-normal2")) {
                    characterLabel.setBounds(-60, 100, 1500, 1000);
                    characterLabel.setIcon(getOptimizedImage(currentChar, 1500, 1000));
                } else if (currentChar.contains("dan")) {
                    characterLabel.setBounds(-50, 100, 1400, 1000);
                    characterLabel.setIcon(getOptimizedImage(currentChar, 1400, 1000));
                } else if (currentChar.contains("empty")) {
                    characterLabel.setIcon(null);
                } else {
                    characterLabel.setBounds(40, 100, 1200, 900);
                    characterLabel.setIcon(getOptimizedImage(currentChar, 1200, 900));
                }
                lastCharPath = currentChar;
            }
        }
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    private void handleNext() {
        if (isChoosing || isFading) return; 

        // ถ้ากำลังพิมพ์ ให้หยุดพิมพ์และแสดงข้อความเต็มทันที
        if (isTyping) {
            stopTypewriter();
            dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        if (currentIndex == 7) { showChoices("ก็เธอสําคัญกับฉันนี่", "ใครๆก็ต้องช่วยเพื่อนอยู่แล้ว", 8, 9); return; }
        if (currentIndex == 8) { currentIndex = 10; updateScene(); return; }
        if (currentIndex == 12) { showChoices("ตอนนี้อาจจะยังไม่ใช่เเต่อนาคตไม่เเน่", "ถ้าเธออยากเป็นก็ได้นะ", 13, 14); return; }
        if (currentIndex == 13) { currentIndex = 15; updateScene(); return; }
        if (currentIndex == 35) { showChoices("ถ้าเกิดอะไรขึ้น ฉันจะปกป้องเธอเอง", " เพราะงั้น ไม่ต้องห่วงหรอก", 36, 37); return; }
        if (currentIndex == 36) { currentIndex = 38; updateScene(); return; }

        int nextIndex = currentIndex + 1;
        jumpToIndex(nextIndex);

        if (nextIndex < dialogues.length) {
            // ตรวจสอบว่าภาพพื้นหลังเปลี่ยนหรือไม่
            String currentBG = imagePaths[currentIndex];
            String nextBG = imagePaths[nextIndex];

            if (!currentBG.equals(nextBG)) {
                // ถ้าภาพเปลี่ยน ให้เรียกใช้ระบบ Fade ฉาก
                performSceneFade(() -> {
                    currentIndex = nextIndex;
                    syncOnline(); 
                    updateScene();
                });
            } else {
                // ถ้าภาพเดิม อัปเดตปกติ
                currentIndex = nextIndex;
                syncOnline();
                updateScene();
            }
        } else {
            finishGame();
        }
    }

    private void syncOnline() {
        if (relationdata.isOnlineMode && networkOut != null) {
            networkOut.println("SYNC_INDEX:" + currentIndex);
        }
    }

    private void handleSoundEffects(int index) {
        if (index == 8) playEffect("res/sound/baka.wav", 5.0f);
        if (index == 9) playEffect("res/sound/emmm.wav", 5.0f);
        if (index == 13) playEffect("res/sound/choochoto.wav", 5.0f);
        if (index == 14) playEffect("res/sound/Baka janai no.wav", 5.0f);
        if (index == 36) playEffect("res/sound/wakarunai.wav", 5.0f);
        if (index == 37) playEffect("res/sound/soredeiikedo.wav", 5.0f);
        if (index == 44) playEffect("res/sound/emmm.wav", 5.0f);
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 380, t1); 
        choiceButton2 = createChoiceButton(text2, 450, t2); 
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            private double scale = 1.0;
            private int alphaMod = 150; 
            private Timer animTimer;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                g2.translate(centerX, centerY);
                g2.scale(scale, scale);
                g2.translate(-centerX, -centerY);

                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                g2.setColor(new Color(225, 105, 180)); 
                g2.setStroke(new BasicStroke(2));   
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);

                g2.dispose();
                super.paintComponent(g); 
            }

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { startAnimation(1.05, 200); }
                    @Override
                    public void mouseExited(MouseEvent e) { startAnimation(1.0, 150); }
                    @Override
                    public void mousePressed(MouseEvent e) { scale = 0.95; repaint(); }
                });
            }

            private void startAnimation(double targetScale, int targetAlpha) {
                if (animTimer != null && animTimer.isRunning()) animTimer.stop();
                animTimer = new Timer(15, ev -> {
                    if (scale < targetScale) scale += 0.01;
                    else if (scale > targetScale) scale -= 0.01;

                    if (alphaMod < targetAlpha) alphaMod += 5;
                    else if (alphaMod > targetAlpha) alphaMod -= 5;

                    if (Math.abs(scale - targetScale) < 0.01 && alphaMod == targetAlpha) {
                        scale = targetScale;
                        ((Timer)ev.getSource()).stop();
                    }
                    repaint();
                });
                animTimer.start();
            }
        };

        btn.setBounds(800, y, 350, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setForeground(new Color(45, 65, 115)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        
        // --- อัปเดตการคิดคะแนน Affinity แบบเดียวกับ Part 5 ---
        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false; 

            if (target == 8 || target == 13 || target == 36) {
                relationdata.aliceRel.addAffinity(10); 
            } else {
                relationdata.aliceRel.decreaseAffinity(5); 
            }

            // --- แก้ไขตรงนี้: แยก Network ออกเป็น Thread ใหม่ ---
            if (relationdata.isOnlineMode && networkOut != null) {
                new Thread(() -> {
                    networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                    networkOut.println("SYNC_INDEX:" + target);
                }).start();
            }

            if (affinityLabel != null) {
                affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            }
            if (statusLabel != null) statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            jumpToIndex(target);

            currentIndex = target; 
            updateScene(); 
        });

        return btn;
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) imageCache.put(key, scaleImage(path, w, h));
        return imageCache.get(key);
    }
    
    // ----------- ส่วนของ UI Network และ ความสัมพันธ์ที่ยกมาจาก Part 5 -----------

    private void setupRelationshipUI() {
        // 1. ปรับตำแหน่งติดซ้ายบน (0, 0) และลดความสูงลงเพื่อให้พอดีกับคนเดียว
        JPanel relPanel = new JPanel(new GridLayout(2, 1, 0, 0)); 
        relPanel.setBounds(0, 0, 280, 75); 
        
        // 2. ใช้พื้นหลังสีดำโปร่งแสงเพื่อให้ข้อความอ่านง่ายบนทุกพื้นหลัง
        relPanel.setBackground(new Color(0, 0, 0, 190)); 
        relPanel.setOpaque(true);

        // 3. เพิ่มกรอบสีชมพูหนา 2 พิกเซล และใส่ระยะห่าง (Padding) ด้านใน
        relPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 105, 180), 2), // กรอบสีชมพู
            BorderFactory.createEmptyBorder(5, 15, 5, 10) // ระยะห่างจากขอบ
        ));

        // 4. ตั้งค่าการแสดงผลของ อริส (ใช้โทนสีเดียวกับ Part 7)
        affinityLabel = new JLabel("อริส: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); 
        affinityLabel.setForeground(new Color(255, 192, 203)); // สีชมพูสว่าง

        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE); // สีขาวอ่านง่าย

        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        
        // นำไปวางไว้ในเลเยอร์ POPUP เพื่อให้อยู่หน้าสุดเสมอ
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }

    private void setupStatusOverlay() {
        statusOverlay = new JPanel(new BorderLayout(10, 10));
        statusOverlay.setBackground(new Color(0, 0, 0, 210)); 
        statusOverlay.setBounds(440, 150, 400, 400); 
        statusOverlay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        statusOverlay.setVisible(false);

        onlineCountLabel = new JLabel("ผู้เล่นออนไลน์: 1", SwingConstants.CENTER);
        onlineCountLabel.setForeground(Color.CYAN); onlineCountLabel.setFont(new Font("Tahoma", Font.BOLD, 20));

        JLabel titleLabel = new JLabel("--- ความสัมพันธ์ทั้งหมด ---", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW); titleLabel.setFont(new Font("Tahoma", Font.BOLD, 22));

        affinityStatusLabel = new JLabel("กำลังโหลดข้อมูล...", SwingConstants.CENTER);
        affinityStatusLabel.setForeground(Color.WHITE); affinityStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        affinityStatusLabel.setVerticalAlignment(SwingConstants.TOP);

        statusOverlay.add(titleLabel, BorderLayout.NORTH);
        statusOverlay.add(affinityStatusLabel, BorderLayout.CENTER);
        statusOverlay.add(onlineCountLabel, BorderLayout.SOUTH); 
        layeredPane.add(statusOverlay, JLayeredPane.DRAG_LAYER);
    }

    private void setupTabKeyBinding() {
        layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "toggleTab");
        layeredPane.getActionMap().put("toggleTab", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                statusOverlay.setVisible(!statusOverlay.isVisible()); 
            }
        });
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                java.net.Socket socket = new java.net.Socket(relationdata.serverIP, 5000);
                networkOut = new java.io.PrintWriter(socket.getOutputStream(), true);
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));

                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:6"); // แจ้งว่าอยู่ Part 6 แล้ว

                String line;
                while ((line = in.readLine()) != null) {
                    // ค้นหาในเมธอด initNetwork()
                    if (line.startsWith("LOAD_AFFINITY:")) {
                        int score = Integer.parseInt(line.substring(14));
                        relationdata.aliceRel.setAffinity(score);
                        SwingUtilities.invokeLater(() -> {
                            // แก้ไขตรงนี้เช่นกันครับ
                            affinityLabel.setText("อริส: " + score); 
                            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
                        });
                    } else if (line.startsWith("ALL_STATS:")) {
                        updateLeaderboardUI(line.substring(10));
                    }
                }
            } catch (Exception e) {}
        }).start();
    }

    private void updateLeaderboardUI(String data) {
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'>");
        sb.append("<table width='320' style='color:white; font-family:Tahoma;'>");
        sb.append("<tr style='color:#FFD700;'><th>ผู้เล่น</th><th align='right'>คะแนน (อริส)</th></tr>");
        
        for (String p : data.split(",")) {
            if (p.contains("=")) {
                String[] parts = p.split("=");
                String name = parts[0];
                String rawScores = parts[1]; // เช่น "10/0"
                
                // --- แก้ไขตรงนี้: แยกเอาเฉพาะคะแนนแรกมาแสดง ---
                String aliceScore = rawScores;
                if (rawScores.contains("/")) {
                    aliceScore = rawScores.split("/")[0]; // เอาเฉพาะตัวหน้าเครื่องหมาย /
                }

                String color = name.equals(relationdata.playerName) ? "#00FF7F" : "white";
                sb.append("<tr>")
                .append("<td style='color:").append(color).append(";'>").append(name).append("</td>")
                .append("<td align='right' style='color:#FF69B4;'>").append(aliceScore).append(" pt</td>")
                .append("</tr>");
            }
        }
        sb.append("</table></body></html>");
        
        SwingUtilities.invokeLater(() -> {
            affinityStatusLabel.setText(sb.toString());
            onlineCountLabel.setText("ผู้เล่นออนไลน์: " + data.split(",").length);
        });
    }
    
    // ----------- ส่วนควบคุมเสียงและ Effect -----------

    public void playEffect(String path, float volume) {
        try {
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                stopEffect(); 
                effectClip = AudioSystem.getClip(); 
                effectClip.open(audioIn);

                FloatControl gainControl = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                effectClip.start();
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    private void jumpToIndex(int targetIndex) {
        if (targetIndex < dialogues.length && targetIndex < imagePaths.length) {
            String currentBG = imagePaths[currentIndex];
            String nextBG = imagePaths[targetIndex];

            if (!currentBG.equals(nextBG)) {
                // ถ้าภาพเปลี่ยน ให้ Fade
                performSceneFade(() -> {
                    currentIndex = targetIndex;
                    syncOnline(); 
                    updateScene();
                });
            } else {
                // ถ้าภาพเดิม ไม่ต้อง Fade
                currentIndex = targetIndex;
                syncOnline();
                updateScene();
            }
        } else {
            finishGame();
        }
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
        try {
            if (bgmClip != null) {
                if (bgmClip.isRunning()) bgmClip.stop();
                bgmClip.flush();
                bgmClip.close();
                bgmClip = null;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void stopEffect() {
        if (effectClip != null) { effectClip.stop(); effectClip.close(); effectClip = null; }
    }

    private void stopAllSounds() {
        stopBGM();
        if (effectClip != null) { effectClip.stop(); effectClip.close(); effectClip = null; }
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(35, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0; 
                ((Timer)e.getSource()).stop();
                fadeOverlay.setVisible(false); // [แก้ไขตรงนี้] เปลี่ยนจาก remove เป็น setVisible(false)
                updateScene(); 
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void startTypewriter(String text) {
        stopTypewriter();
        isTyping = true;
        charIndex = 0;
        dialogueArea.setText("");
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 750px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else {
                stopTypewriter();
            }
        });
        typewriterTimer.start();
    }

    private void stopTypewriter() {
        if (typewriterTimer != null) typewriterTimer.stop();
        isTyping = false;
    }

    private void finishGame() {
        // --- เพิ่มบรรทัดเช็คและล็อคตรงนี้ ---
        if (isFading) return; 
        isFading = true; 
        // ------------------------------

        // 1. เริ่มการ Fade Out (จอมืดลงช้าๆ)
        startFadeOut(() -> {
            stopAllSounds(); 

            // 2. สร้างข้อความบอกเวลา "ผ่านมาหลายสัปดาห์"
            JLabel timeSkipLabel = new JLabel("ผ่านไปแล้วหลายสัปดาห์...", SwingConstants.CENTER);
            timeSkipLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
            timeSkipLabel.setForeground(Color.WHITE);
            timeSkipLabel.setBounds(0, 0, 1280, 800);
            
            layeredPane.add(timeSkipLabel, JLayeredPane.DRAG_LAYER);
            layeredPane.setLayer(timeSkipLabel, JLayeredPane.DRAG_LAYER + 1);
            
            // 3. หน่วงเวลา 3 วินาที ก่อนไป Part 7
            Timer delayTimer = new Timer(3000, ev -> {
                ((Timer)ev.getSource()).stop();
                
                SwingUtilities.invokeLater(() -> {
                    new part7().setVisible(true); // จะเปิดแค่หน้าต่างเดียวเพราะโดนล็อคไว้แล้ว
                    dispose(); 
                });
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        });
    }

    private void startCharacterFadeIn() {
        charAlpha1 = 0.0f;
        if (charFadeTimer1 != null && charFadeTimer1.isRunning()) charFadeTimer1.stop();
        charFadeTimer1 = new Timer(30, e -> {
            charAlpha1 += 0.05f; // ปรับให้ช้าลง (เดิม 0.1f)
            if (charAlpha1 >= 1.0f) { 
                charAlpha1 = 1.0f; 
                ((Timer)e.getSource()).stop(); 
            }
            characterLabel.repaint();
        });
        charFadeTimer1.start();
    }

    // ระบบ Fade จอเมื่อเปลี่ยนฉากพื้นหลัง
    private void performSceneFade(Runnable onBlack) {
        if (isFading) return; 
        isFading = true; 
        alpha = 0.0f;
        
        // ตรวจสอบว่ามีอยู่ไหม ถ้าไม่มีให้แอดครั้งเดียวและตั้งไว้บนสุด
        if (fadeOverlay.getParent() == null) {
            layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        }
        fadeOverlay.setVisible(true);
        layeredPane.setLayer(fadeOverlay, JLayeredPane.DRAG_LAYER); // บังคับให้อยู่หน้าสุดเสมอ
        
        // 1. Fade Out (ทำให้จอมืดลง)
        // เปลี่ยนจาก 0.08f เป็น 0.04f เพื่อให้ใช้เวลาประมาณ 0.5 วินาที
        Timer fadeOut = new Timer(20, null);
        fadeOut.addActionListener(e -> {
            alpha += 0.04f; 
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                fadeOut.stop();
                
                // เปลี่ยนภาพพื้นหลังตอนจอมืดสนิท
                onBlack.run(); 
                
                // 2. Fade In (ทำให้จอสว่างขึ้น)
                Timer fadeIn = new Timer(20, eve -> {
                    alpha -= 0.04f;
                    if (alpha <= 0) {
                        alpha = 0;
                        ((Timer)eve.getSource()).stop();
                        isFading = false;
                        fadeOverlay.setVisible(false); // ซ่อนไว้เพื่อไม่ให้ขวางการคลิก
                    }
                    fadeOverlay.repaint();
                });
                fadeIn.start();
            }
            fadeOverlay.repaint();
        });
        fadeOut.start();
    }

    private void startFadeOut(Runnable onComplete) {
        if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        
        fadeOverlay.setVisible(true); // [เพิ่มบรรทัดนี้] เพื่อให้แผ่นสีดำกลับมาแสดงผลตอนจบเกม
        alpha = 0.0f;
        
        Timer fadeTimer = new Timer(30, e -> {
            alpha += 0.03f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                ((Timer)e.getSource()).stop();
                if (onComplete != null) onComplete.run();
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            // เปลี่ยนจาก SCALE_SMOOTH เป็น SCALE_FAST เพื่อความลื่นไหล
            return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_FAST));
        } catch (Exception e) { 
            return null; 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part6().setVisible(true));
    }
}