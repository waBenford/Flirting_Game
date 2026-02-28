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
    private boolean isFading = false;

    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    private String allPlayersData = "";

    private PrintWriter networkOut;

    private Map<String, ImageIcon> imageCache = new HashMap<>();

    // --- Fonts for 1280x800 ---
    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    // --- Array ข้อมูล ---
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
        "ไม่มีเหตุผลเลยที่ฉันปฏิเสธเธอ", "เเน่นอน!! ฉันจะออกเดินทางกับเธอ",
        "ฉันจะต้องเเข็งเเกร่งขึ้นให้ได้เหมือนกัน", "ขอบคุณนะ …",
        "เออ..ว่าเเต่เธอชื่ออะไรกันเเน่", "ฉันชื่อ..."
    };

    public part3() {
        setTitle("ISEKAI DEMO - Part 3");
        setSize(1280, 800); // แก้ขนาดเฟรม
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setFocusTraversalKeysEnabled(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);
        
        playSE("res/sound/soundtrack3.wav", true, -10.0f); 
        playSE("res/sound/fireplace.wav", true, -5.0f); 

        // พื้นหลังเต็มจอ 1280x800
        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1280, 800));
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // ปรับพิกัดตัวละครมาตรฐาน (190, 100) และขนาด (900, 900)
        characterLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        characterLabel.setBounds(190, 100, 900, 900); 
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        setupRelationshipUI();
        setupStatusOverlay(); 
        setupTabKeyBinding(); 
        this.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override public void windowGainedFocus(java.awt.event.WindowEvent e) {}
            @Override public void windowLostFocus(java.awt.event.WindowEvent e) {
                if (statusOverlay != null) statusOverlay.setVisible(false); // ปิด Scoreboard เมื่อสลับจอ
            }
        });
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

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNext();
            }
        });
    }

    private void handleNext() {
        // 1. ป้องกันการคลิกซ้ำขณะเลือกตอบหรือกำลัง Fade ฉาก
        if (isChoosing || isFading) return; 

        // 2. ถ้าตัวอักษรกำลังพิมพ์อยู่ ให้หยุดและแสดงข้อความเต็มทันที
        if (isTyping) {
            if (typewriterTimer != null) typewriterTimer.stop();
            isTyping = false;
            dialogueArea.setText("<html><body style='width: 950px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        // 3. ระบบเลือกตอบ (Choice) ตามเนื้อเรื่องของ Part 3
        if (currentIndex == 14) {
            showChoices("..ปีศาจนี่เหมือนผีรึเปล่า??", "..เอ่อ..แล้วเผ่าอื่นๆหละ??", 15, 16);
            return;
        }
        if (currentIndex == 32) {
            showChoices("เข้าไปปลอบอริส", "นั่งอยู่เฉยๆ", 33, 34);
            return;
        }

        // 4. คำนวณลำดับถัดไป (จัดการกระโดดข้าม Index หลังจากเลือกตอบ)
        int nextIndex = currentIndex;
        if (currentIndex == 15 || currentIndex == 16) nextIndex = 17;
        else if (currentIndex == 33 || currentIndex == 34) nextIndex = 35;
        else nextIndex = currentIndex + 1;

        // 5. ตรวจสอบว่ายังไม่จบ Part
        if (nextIndex < dialogues.length) {
            // ตรวจสอบชื่อไฟล์ภาพเพื่อตัดสินใจว่าจะ Fade หรือไม่
            String currentBG = imagePaths[Math.min(currentIndex, imagePaths.length-1)];
            String nextBG = imagePaths[Math.min(nextIndex, imagePaths.length-1)];
            
            currentIndex = nextIndex;

            // --- ส่วนสำคัญ: ส่งลำดับฉากไปหาเครื่องเพื่อนและบันทึกลง SQL (ผ่าน Server) ---
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("SYNC_INDEX:" + currentIndex);
            }

            // 6. ตรวจสอบการเปลี่ยนภาพพื้นหลัง
            if (!currentBG.equals(nextBG)) {
                triggerSceneFade(); // ถ้าภาพเปลี่ยนให้ใช้ Effect Fade
            } else {
                updateScene(); // ถ้าภาพเดิมให้อัปเดตแค่บทสนทนา
            }
        } else {
            // เมื่อจบ Part 3 ให้หยุดเพลงและไป Part 4
            finishPartWithTimeSkip();
        }
    }

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(2, 1));
        relPanel.setBounds(25, 25, 300, 70);
        relPanel.setOpaque(false);
        affinityLabel = new JLabel("ความสนิท: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        affinityLabel.setForeground(Color.WHITE);
        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        statusLabel.setForeground(new Color(255, 204, 0));
        relPanel.add(affinityLabel); relPanel.add(statusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }

    private void setupTabKeyBinding() {
        // ใช้คำสั่งนี้เพื่อให้โปรแกรมรับคำสั่งจาก Keyboard ได้แน่นอนขึ้น
        layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "toggleTab");
        layeredPane.getActionMap().put("toggleTab", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // บังคับสลับสถานะการมองเห็น
                statusOverlay.setVisible(!statusOverlay.isVisible()); 
            }
        });
    }

    private void triggerSceneFade() {
        isFading = true;
        if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        Timer fadeOut = new Timer(15, null);
        fadeOut.addActionListener(e -> {
            alpha += 0.03f;
            if (alpha >= 1.0f) {
                alpha = 1.0f; fadeOut.stop();
                updateScene(); 
                Timer pauseTimer = new Timer(750, ev -> {
                    ((Timer)ev.getSource()).stop();
                    Timer fadeIn = new Timer(15, null);
                    fadeIn.addActionListener(evt -> {
                        alpha -= 0.03f;
                        if (alpha <= 0) { alpha = 0; fadeIn.stop(); isFading = false; layeredPane.remove(fadeOverlay); }
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
        backgroundLabel.setIcon(getOptimizedImage(imagePaths[Math.min(currentIndex, imagePaths.length-1)], 1280, 800));

        
        if (currentIndex < charPaths.length) {
            String newPath = charPaths[currentIndex];
            String oldPath = (currentIndex > 0) ? charPaths[currentIndex - 1] : "";
            characterLabel.setIcon(getOptimizedImage(newPath, 900, 900));
            characterLabel.setBounds(190, 100, 900, 900); // บังคับพิกัดมาตรฐาน

            if (!newPath.equals(oldPath) || currentIndex == 0) {
                startCharacterFadeIn();
            }
        }

        updateDialogueDisplay(dialogues[currentIndex]);
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    private void finishPartWithTimeSkip() {
        isFading = true; // ล็อคการคลิกซ้ำ
        stopBGM();
        stopEffect();
        alpha = 0.0f;
        
        // 1. ตรวจสอบว่ามี fadeOverlay หรือไม่
        if (fadeOverlay.getParent() == null) {
            layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        }

        // 2. สร้างข้อความ "ผ่านไปแล้ว 2 ปี..."
        JLabel transitionText = new JLabel("ผ่านไปแล้ว 2 ปี...", SwingConstants.CENTER);
        transitionText.setFont(new Font("Tahoma", Font.BOLD, 45)); // ขนาดใหญ่ชัดเจน
        transitionText.setForeground(Color.WHITE);
        transitionText.setBounds(0, 0, 1280, 800);
        transitionText.setOpaque(false);
        transitionText.setVisible(false); // ซ่อนไว้ก่อนจนกว่าจะจอดำ
        layeredPane.add(transitionText, JLayeredPane.DRAG_LAYER);
        layeredPane.setLayer(transitionText, JLayeredPane.DRAG_LAYER + 1); // ให้อยู่เหนือแผ่นดำ

        // 3. เริ่มการ Fade Out (ดำมืด)
        Timer fadeOut = new Timer(30, e -> {
            alpha += 0.02f; // ปรับให้ค่อยๆ ดำช้าๆ (ช้ากว่าปกติ)
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                ((Timer)e.getSource()).stop();

                // เมื่อจอดำสนิทแล้ว แสดงข้อความ
                transitionText.setVisible(true);

                // 4. หน่วงเวลาหน้าจอดำนานๆ (เช่น 3 วินาที) ตามที่ต้องการ
                Timer delayTimer = new Timer(3000, ev -> {
                    ((Timer)ev.getSource()).stop();
                    
                    // สลับไป Part 4
                    SwingUtilities.invokeLater(() -> {
                        new part4().setVisible(true);
                        dispose(); // ปิดหน้าจอ Part 3
                    });
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
            }
            fadeOverlay.repaint();
        });
        fadeOut.start();
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox(); 
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(225, 520, 800, 200);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel("");
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

    private void setupStatusUI() {
        JPanel relPanel = new JPanel(new GridLayout(2, 1));
        relPanel.setBounds(25, 25, 300, 70);
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
    }

    private void updateDialogueDisplay(String text) {
        if (typewriterTimer != null) typewriterTimer.stop();
        charIndex = 0;
        isTyping = true;
        dialogueArea.setText(""); 
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 950px;'>" + text.substring(0, charIndex) + "</body></html>");
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

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                Socket socket = new Socket(relationdata.serverIP, 5000);
                networkOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:3");

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("LOAD_AFFINITY:")) {
                        int score = Integer.parseInt(line.substring(14));
                        relationdata.aliceRel.setAffinity(score); // ดึงคะแนนจากพาร์ทก่อนมาใช้
                        SwingUtilities.invokeLater(() -> {
                            affinityLabel.setText("ความสนิท: " + score);
                            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
                        });
                    } else if (line.startsWith("ALL_STATS:")) {
                        updateLeaderboardUI(line.substring(10));
                    }
                }
            } catch (Exception e) {}
        }).start();
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

    private void updateLeaderboardUI(String data) {
        // ใช้ HTML Table เพื่อจัดคอลัมน์ให้ตรงกัน
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'>");
        sb.append("<table width='300' style='color:white; font-family:Tahoma;'>");
        sb.append("<tr style='color:#FFD700;'><th>ผู้เล่น</th><th align='right'>คะแนน</th></tr>");
        
        String[] players = data.split(",");
        for (String p : players) {
            if (!p.isEmpty() && p.contains("=")) {
                String[] parts = p.split("=");
                String name = parts[0];
                String score = parts[1];
                
                // ไฮไลต์ชื่อตัวเองเป็นสีเขียว ถ้าชื่อตรงกับที่เราตั้งไว้
                String nameColor = name.equals(relationdata.playerName) ? "#00FF7F" : "#FFFFFF";
                
                sb.append("<tr>")
                .append("<td style='color:").append(nameColor).append(";'>").append(name).append("</td>")
                .append("<td align='right' style='color:#FF69B4;'>").append(score).append(" pt</td>")
                .append("</tr>");
            }
        }
        sb.append("</table></body></html>");
        
        SwingUtilities.invokeLater(() -> {
            if (affinityStatusLabel != null) {
                affinityStatusLabel.setText(sb.toString());
                affinityStatusLabel.setVerticalAlignment(SwingConstants.TOP); // ให้รายชื่อเริ่มจากข้างบน
            }
        });
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 380, t1); //y: ขึ้น=ลง
        choiceButton2 = createChoiceButton(text2, 450, t2); //y: ขึ้น=ลง
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private void setupStatusOverlay() {
        statusOverlay = new JPanel();
        statusOverlay.setLayout(new BorderLayout(10, 10)); 
        statusOverlay.setBackground(new Color(0, 0, 0, 200)); 
        statusOverlay.setBounds(440, 150, 400, 400); 
        statusOverlay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        statusOverlay.setVisible(false);

        // 1. สร้างป้ายจำนวนผู้เล่น (ต้องสร้างเพื่อไม่ให้เกิด Null Error)
        onlineCountLabel = new JLabel("ผู้เล่นออนไลน์: 1", SwingConstants.CENTER);
        onlineCountLabel.setForeground(Color.CYAN);
        onlineCountLabel.setFont(new Font("Tahoma", Font.BOLD, 20));

        JLabel titleLabel = new JLabel("--- ความสัมพันธ์ทั้งหมด ---", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 22));

        affinityStatusLabel = new JLabel("กำลังโหลดข้อมูล...", SwingConstants.CENTER);
        affinityStatusLabel.setForeground(Color.WHITE);
        affinityStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));

        // 2. เพิ่ม UI ลงใน Panel
        statusOverlay.add(titleLabel, BorderLayout.NORTH);
        statusOverlay.add(affinityStatusLabel, BorderLayout.CENTER);
        statusOverlay.add(onlineCountLabel, BorderLayout.SOUTH); // เพิ่ม onlineCountLabel ไว้ล่างสุด
        
        layeredPane.add(statusOverlay, JLayeredPane.DRAG_LAYER);
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            // Override paintComponent เพื่อวาดปุ่มให้มีขอบโค้งมน
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // วาดพื้นหลังโค้งมน
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // วาดเส้นขอบโค้งมน
                g2.setColor(new Color(225, 105, 180)); // สีขอบเดิม
                g2.setStroke(new BasicStroke(2));   // ความหนาขอบเดิม
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);

                g2.dispose();

                // วาดข้อความและส่วนอื่นๆ ทับลงไป
                super.paintComponent(g);
            }
        };

        // เลื่อนปุ่มไปทางขวา
        btn.setBounds(800, y, 350, 60);
        btn.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn.setForeground(new Color(45,65,115));
        btn.setBackground(new Color(255, 255, 255, 150));

        // ตั้งค่าเพื่อให้วาดปุ่มแบบกำหนดเองได้
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // ปิดการวาดขอบสี่เหลี่ยมเดิม

        btn.addActionListener(e -> {
            layeredPane.remove(choiceButton1); layeredPane.remove(choiceButton2);
            isChoosing = false;
            if (target == 33) relationdata.aliceRel.addAffinity(10);
            else if (target == 34) relationdata.aliceRel.decreaseAffinity(5);

            // --- ส่วนที่ขาดไป: ต้องส่งไปบอก Server ด้วยคะแนนถึงจะถูกเซฟลง SQL ---
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                networkOut.println("SYNC_INDEX:" + target);
            }

            affinityLabel.setText("ความสนิท: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            currentIndex = target; updateScene();
        });
        return btn;
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(50, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) { alpha = 0; ((Timer)e.getSource()).stop(); layeredPane.remove(fadeOverlay); }
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
            } else { setLocation(originalLoc); ((Timer) e.getSource()).stop(); }
        });
        shakeTimer.start();
    }

    private void startCharacterFadeIn() {
        charAlpha = 0.0f;
        if (charFadeTimer != null && charFadeTimer.isRunning()) charFadeTimer.stop();
        charFadeTimer = new Timer(30, e -> {
            charAlpha += 0.05f;
            if (charAlpha >= 1.0f) { charAlpha = 1.0f; ((Timer)e.getSource()).stop(); }
            characterLabel.repaint();
        });
        charFadeTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part3().setVisible(true));
    }
}