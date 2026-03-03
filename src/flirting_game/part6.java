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
    private JLabel backgroundLabel, characterLabel, characterLabel2, dialogueArea, nameLabel;
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
    
    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    private java.io.PrintWriter networkOut;

    private String lastBgPath = "";
    private String lastCharPath = "";
    private float charAlpha = 0.0f; 
    private Timer charFadeTimer;

    // Font สำหรับภาษาไทย
    private final Font thaiFont = new Font("Tahoma", Font.BOLD, 18);

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
       "res/scene5/Alice-shower1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png",
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
        "อีกทั้งยังมีปีศาจเเละต้นไม้าอาถรรพ์ที่สามารถทําร้ายเราได้ตลอดเวลา","ฉันก็เคยเข้าไปครั้งนึง เเต่ก็สามารถรอดออกมาได้", 
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
        characterLabel = createCharacterLabel(); 
        characterLabel2 = createCharacterLabel();
        
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(characterLabel2, JLayeredPane.PALETTE_LAYER);

        // แก้ไขชื่อผู้เล่น
        for (int i = 0; i < dialogues.length; i++) {
            if (dialogues[i].contains("(ชื่อตัวละครเรา)")) {
                dialogues[i] = dialogues[i].replace("(ชื่อตัวละครเรา)", relationdata.playerName);
            }
        }

        setupDialogueUI();
        setupRelationshipUI(); // <--- ส่วนที่แก้ Font ภาษาไทย
        setupStatusOverlay();
        setupTabKeyBinding();
        initNetwork();
        
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

    private JLabel createCharacterLabel() {
        return new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha)); 
                super.paintComponent(g2d); 
                g2d.dispose();
            }
        };
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);

        // พื้นหลัง
        if (currentIndex < imagePaths.length) {
            String currentBg = imagePaths[currentIndex];
            if (!currentBg.equals(lastBgPath)) {
                backgroundLabel.setIcon(getOptimizedImage(currentBg, 1280, 800));
                lastBgPath = currentBg;
            }
        }

        // ตัวละคร (Dual System)
        if (currentIndex < charPaths.length) {
            String currentChar = charPaths[currentIndex];
            
            boolean isDualScene = (currentIndex >= 10 && currentIndex <= 44);

            if (isDualScene) {
                characterLabel.setBounds(-200, 50, 1000, 900); 
                characterLabel2.setBounds(200, 50, 1400, 1000); 

                if (currentChar.toLowerCase().contains("dan")) {
                    characterLabel2.setIcon(getOptimizedImage(currentChar, 1400, 1000));
                } else if (!currentChar.contains("empty")) {
                    characterLabel.setIcon(getOptimizedImage(currentChar, 1000, 900));
                }
            } else {
                characterLabel.setBounds(140, 50, 1000, 950);
                characterLabel2.setIcon(null);
                if (currentChar.contains("empty")) characterLabel.setIcon(null);
                else characterLabel.setIcon(getOptimizedImage(currentChar, 1000, 900));
            }

            if (!currentChar.equals(lastCharPath)) {
                startCharacterFadeIn();
                lastCharPath = currentChar;
            }
        }
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(2, 1)); 
        relPanel.setBounds(20, 20, 280, 80); // ปรับตำแหน่งซ้ายบน
        relPanel.setBackground(new Color(0, 0, 0, 180)); 
        relPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 105, 180), 2));
        
        affinityLabel = new JLabel("อริส: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(thaiFont); // ใช้ Font ไทยที่กำหนดไว้
        affinityLabel.setForeground(Color.WHITE);
        affinityLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(thaiFont); // ใช้ Font ไทยที่กำหนดไว้
        statusLabel.setForeground(Color.PINK);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        relPanel.add(affinityLabel); 
        relPanel.add(statusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
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
        new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible())).start();
    }

    // --- ส่วนที่เหลือของโค้ดคงเดิม ---
    private void handleNext() {
        if (isChoosing || isFading) return; 
        if (isTyping) {
            stopTypewriter();
            dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        if (currentIndex == 7) { showChoices("ก็เธอสําคัญกับฉันนี่", "ใครๆก็ต้องช่วยเพื่อนอยู่แล้ว", 8, 9); return; }
        if (currentIndex == 8) { jumpToIndex(10); return; }
        if (currentIndex == 12) { showChoices("ตอนนี้อาจจะยังไม่ใช่เเต่อนาคตไม่เเน่", "ถ้าเธออยากเป็นก็ได้นะ", 13, 14); return; }
        if (currentIndex == 13) { jumpToIndex(15); return; }
        if (currentIndex == 35) { showChoices("ถ้าเกิดอะไรขึ้น ฉันจะปกป้องเธอเอง", " เพราะงั้น ไม่ต้องห่วงหรอก", 36, 37); return; }
        if (currentIndex == 36) { jumpToIndex(38); return; }

        int nextIndex = currentIndex + 1;
        if (nextIndex < dialogues.length) {
            if (!imagePaths[currentIndex].equals(imagePaths[nextIndex])) {
                performSceneFade(() -> { currentIndex = nextIndex; syncOnline(); updateScene(); });
            } else {
                currentIndex = nextIndex; syncOnline(); updateScene();
            }
        } else {
            finishGame();
        }
    }

    private void jumpToIndex(int target) {
        currentIndex = target; syncOnline(); updateScene();
    }

    private void syncOnline() { if (relationdata.isOnlineMode && networkOut != null) networkOut.println("SYNC_INDEX:" + currentIndex); }

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
        // ปรับพิกัด Y ให้อยู่เหนือกล่องข้อความ (กล่องข้อความอยู่ที่ Y=520)
        // ปุ่มแรกอยู่ที่ 350, ปุ่มสองอยู่ที่ 425
        choiceButton1 = createChoiceButton(text1, 350, t1); 
        choiceButton2 = createChoiceButton(text2, 425, t2); 
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            private double scale = 1.0;
            private int alphaMod = 170; // ความโปร่งแสงเริ่มต้น
            private Timer animTimer;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Animation Scale
                g2.translate(w / 2, h / 2);
                g2.scale(scale, scale);
                g2.translate(-w / 2, -h / 2);
                
                // พื้นหลังไล่เฉดสีขาว-เทาอ่อน สไตล์ Glassmorphism
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255, alphaMod), 
                                                    0, h, new Color(230, 230, 230, alphaMod));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, 20, 20);
                
                // เส้นขอบสีชมพูเข้ม (Theme อริส)
                g2.setColor(new Color(255, 102, 153));
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 18, 18);
                
                g2.dispose();
                super.paintComponent(g);
            }

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { startAnim(1.03, 230); }
                    @Override
                    public void mouseExited(MouseEvent e) { startAnim(1.0, 170); }
                });
            }

            private void startAnim(double ts, int ta) {
                if (animTimer != null) animTimer.stop();
                animTimer = new Timer(15, e -> {
                    scale += (ts - scale) * 0.2;
                    if (alphaMod < ta) alphaMod += 10; else if (alphaMod > ta) alphaMod -= 10;
                    if (Math.abs(scale - ts) < 0.001 && alphaMod == ta) ((Timer)e.getSource()).stop();
                    repaint();
                });
                animTimer.start();
            }
        };

        // ตำแหน่ง X=800 คือด้านขวาบนของกล่องข้อความ
        btn.setBounds(800, y, 380, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 17));
        btn.setForeground(new Color(50, 50, 50));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1); 
            layeredPane.remove(choiceButton2);
            isChoosing = false; 
            
            // อัปเดตค่าความสัมพันธ์
            if (target == 8 || target == 13 || target == 36) {
                relationdata.aliceRel.addAffinity(10); 
            } else {
                relationdata.aliceRel.decreaseAffinity(5);
            }
            
            if (affinityLabel != null) affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            jumpToIndex(target);
        });
        
        return btn;
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) imageCache.put(key, scaleImage(path, w, h));
        return imageCache.get(key);
    }

    private void setupStatusOverlay() {
        statusOverlay = new JPanel(new BorderLayout());
        statusOverlay.setBounds(440, 150, 400, 400); 
        statusOverlay.setBackground(new Color(0, 0, 0, 210)); 
        statusOverlay.setVisible(false);
        affinityStatusLabel = new JLabel("", SwingConstants.CENTER);
        affinityStatusLabel.setForeground(Color.WHITE);
        statusOverlay.add(affinityStatusLabel, BorderLayout.CENTER);
        layeredPane.add(statusOverlay, JLayeredPane.DRAG_LAYER);
    }

    private void setupTabKeyBinding() {
        layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "toggleTab");
        layeredPane.getActionMap().put("toggleTab", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { statusOverlay.setVisible(!statusOverlay.isVisible()); }
        });
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                java.net.Socket s = new java.net.Socket(relationdata.serverIP, 5000);
                networkOut = new java.io.PrintWriter(s.getOutputStream(), true);
            } catch (Exception e) {}
        }).start();
    }

    private void playSE(String path, boolean loop, float vol) {
        try {
            AudioInputStream ai = AudioSystem.getAudioInputStream(new File(path));
            bgmClip = AudioSystem.getClip(); bgmClip.open(ai);
            FloatControl fc = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
            fc.setValue(vol); if (loop) bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) {}
    }

    public void playEffect(String path, float vol) {
        try {
            AudioInputStream ai = AudioSystem.getAudioInputStream(new File(path));
            Clip c = AudioSystem.getClip(); c.open(ai);
            FloatControl fc = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            fc.setValue(vol); c.start();
        } catch (Exception e) {}
    }

    private void stopAllSounds() { if (bgmClip != null) bgmClip.stop(); }

    private void startFadeIn() {
        Timer ft = new Timer(35, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) { alpha = 0; ((Timer)e.getSource()).stop(); fadeOverlay.setVisible(false); }
            fadeOverlay.repaint();
        });
        ft.start();
    }

    private void startTypewriter(String text) {
        stopTypewriter(); isTyping = true; charIndex = 0;
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 750px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else stopTypewriter();
        });
        typewriterTimer.start();
    }

    private void stopTypewriter() { if (typewriterTimer != null) typewriterTimer.stop(); isTyping = false; }

    private void performSceneFade(Runnable onBlack) {
        isFading = true; alpha = 0.0f; fadeOverlay.setVisible(true);
        Timer fo = new Timer(20, e -> {
            alpha += 0.05f;
            if (alpha >= 1.0f) {
                alpha = 1.0f; ((Timer)e.getSource()).stop(); onBlack.run();
                Timer fi = new Timer(20, ev -> {
                    alpha -= 0.05f; if (alpha <= 0) { alpha = 0; ((Timer)ev.getSource()).stop(); isFading = false; fadeOverlay.setVisible(false); }
                    fadeOverlay.repaint();
                }); fi.start();
            }
            fadeOverlay.repaint();
        });
        fo.start();
    }

    private void startCharacterFadeIn() {
        charAlpha = 0.0f;
        if (charFadeTimer != null) charFadeTimer.stop();
        charFadeTimer = new Timer(30, e -> {
            charAlpha += 0.05f; if (charAlpha >= 1.0f) { charAlpha = 1.0f; ((Timer)e.getSource()).stop(); }
            characterLabel.repaint(); characterLabel2.repaint();
        });
        charFadeTimer.start();
    }

    private void finishGame() {
        if (isFading) return; isFading = true;
        performSceneFade(() -> { new part7().setVisible(true); dispose(); });
    }

    public ImageIcon scaleImage(String p, int w, int h) {
        try { return new ImageIcon(new ImageIcon(p).getImage().getScaledInstance(w, h, Image.SCALE_FAST)); } catch (Exception e) { return null; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part6().setVisible(true));
    }
}