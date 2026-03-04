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

public class part7 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, leftCharLabel, rightCharLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel; 
    private float alpha = 1.0f;
    private JPanel fadeOverlay;
    private int currentIndex = 0;
    private Clip bgmClip;      
    private Clip effectClip;
    private JButton choiceButton1, choiceButton2, choiceButton3;
    private boolean isChoosing = false;
    private boolean isFinishing = false;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    private JLabel nebulaAffinityLabel, nebulaStatusLabel;
    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    private java.io.PrintWriter networkOut;

    private JPanel bgFadeOverlay;
    private float bgAlpha = 0.0f; 
    private Timer bgFadeTimer;
    private String lastBgPath = "";

    // --- ระบบ Dual Character ใหม่ ---
    private float leftAlpha = 0.0f; 
    private float rightAlpha = 0.0f;
    private Timer leftFadeTimer, rightFadeTimer;
    private String lastLeftPath = "", lastRightPath = "";

    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    // ข้อมูล Array ฉากหลัง (คงเดิม)
    private String[] imagePaths = {
       "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", 
       "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", 
       "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s2.png", "res/scene7/s2.png", 
       "res/scene7/s2.png", "res/scene7/s2.png", "res/scene7/s2.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png", 
       "res/scene7/s3.png", "res/scene7/s3.png", "res/scene7/s3.png"
    };

    // แยกฝั่งซ้าย (เน้น Dan และ Alice ฝั่งผู้เล่น)
    private String[] leftCharPaths = { 
       "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
       "res/Charactor/Dan/dan-normal1.png","res/scene5/Alice-shy2.png","res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight1.png", "res/empty.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Alice/Girl/Alice-fight1.png", 
       "res/Charactor/Alice/Girl/Alice-fight1.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight2.png", 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png","res/empty.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png"
    };

    // แยกฝั่งขวา (เน้น Nebula และตัวละคร NPC/ศัตรู)
    private String[] rightCharPaths = { 
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal2.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/scene5/Alice-normal1.png",  "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-shy1.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png"
    };

    private String[] names = { " ", " ", "Dan", "Dan", "Dan", "ฉัน", "อริส", "อริส", "อริส","อริส", " ", " ", "Dan", "ฉัน", "อริส", "???", "???", "ฉัน", "???", "Nebula", "อริส", "ฉัน", "ฉัน", "Nebula", "Nebula", "ฉัน", "ฉัน", "Nebula", "Nebula", "ฉัน", "อริส","Nebula", "Nebula", "Nebula", "Nebula","Nebula","ฉัน", "Nebula", "อริส", "Nebula", "Nebula", "Nebula", "Dan", "Nebula", "Nebula", "ฉัน", "Nebula", "Nebula", "Nebula", "Nebula", "Nebula", "Nebula", "Nebula", "Nebula","Nebula","Nebula","Dan", "อริส", "ฉัน", "Nebula", "Nebula", "Nebula", "Nebula","Nebula", "Nebula", "Dan", "Nebula", "Nebula", "ฉัน", "Nebula", "Nebula", "Nebula", "อริส", "Dan" };
    private String[] dialogues = { "หลังจากเดินทางมาหลายสัปดาห์…","ในที่สุดพวกเราก็มาถึงป่า Death End", "ที่นี่แหละ…ป่า Death End", "จากนี้ไปต้องระวังตัวให้ดี", "ปีศาจในป่านี้แข็งแกร่งกว่าที่พวกเธอเคยเจอมา", "เข้าใจแล้ว", "ป่านี้มันน่ากลัวกว่าที่คิดอีกนะ...", "อะ...อือ...", "อือ เข้าใจแล้ว", "ก็ฉันกลัวนี่!!", "หลังจากเดินลึกเข้าไปในป่า...","พวกเราก็พบกับปราสาทขนาดใหญ่", "นั่นไง…ปราสาทของจอมมาร", "ในที่สุดก็มาถึงสักที", "บรรยากาศมันน่ากลัวจัง…", "มนุษย์งั้นหรอ...", "กล้ามาถึงที่นี่ได้ก็นับว่ากล้าดีนะ", "ใครกัน!?", "ข้าคือจอมมาร...", "ชื่อของข้าคือ Nebula", "จอมมาร…!!", "ในที่สุดก็เจอตัวแล้ว", "เป็นแกสินะ ที่สั่งให้ปีศาจโจมตีหมู่บ้าน", "หืม?", "เจ้ากําลังพูดเรื่องอะไร?", "อย่ามาแกล้งทําเป็นไม่รู้!", "พวกเราจะหยุดแกที่นี่!", "ฮ่าๆๆ", "มนุษย์นี่น่าสนใจจริงๆ", "ถ้าอยากลองก็เข้ามา", "รับนี่ไป!","Ice Lance!", "น่าสนุกดีนี่", "แต่พลังแค่นี้...", "ยังห่างไกลนะ", "พอแค่นี้ก่อนดีกว่า", "อะไรนะ?", "ข้าไม่ได้เป็นคนสั่งปีศาจพวกนั้น", "อะไรนะ!?", "คนที่ทําเรื่องพวกนั้นคือ...", "จอมมารอีกคนหนึ่ง", "ชื่อของมันคือ Grey", "จอมมารอีกคนงั้นหรอ...", "มนุษย์...เจ้าค่อนข้างแข็งแกร่งกว่าที่คิดนะ", "ปกติแล้วมนุษย์ที่มาถึงที่นี่ มักจะหนีหรือไม่ก็ตายไปแล้ว", "ก็แค่ทําในสิ่งที่ต้องทํา", "หืม...น่าสนใจดีนี่", "เจ้ากล้าต่อสู้กับจอมมารโดยไม่ลังเลเลยงั้นหรอ?", "หึ...มนุษย์ที่พูดแบบนี้กับข้าเป็นคนแรกเลยนะ", "เจ้านี่แปลกดีจริงๆ", "เเค่นี้หรอ...", "ปกติมนุษย์จะกลัวข้า...", "แต่เจ้ากลับยืนคุยกับข้าเฉยๆ", "เจ้ากล้าพูดกับจอมมารแบบนั้นเลยหรอ", "มะ…มนุษย์นี่พูดอะไรของเจ้า…", "เป็นงั้น ก็ดี..", "นี่พวกนายกําลังจีบจอมมารกันอยู่รึไงเนี่ย...", "นะ…นายไปพูดอะไรกับจอมมารแบบนั้นกัน!!", "ถ้าอย่างนั้น...จอมมารที่อยู่เบื้องหลังเรื่องพวกนี้ก็คือ Grey งั้นสินะ", "ใช่", "เขาเคยเป็นหนึ่งในจอมมารที่อยู่ภายใต้การปกครองของข้า", "แต่แนวคิดของเขาแตกต่างจากข้า", "ข้าเชื่อว่ามนุษย์กับปีศาจสามารถอยู่ร่วมกันได้", "แต่ Grey เชื่อว่ามนุษย์ควรถูกกําจัดให้หมด", "งั้นเขาก็แยกตัวออกไปสินะ...", "ใช่", "และตอนนี้เขากําลังสร้างกองทัพปีศาจของตัวเอง", "ถ้าอย่างนั้น...เขาอยู่ที่ไหน", "Grey ซ่อนตัวอยู่ที่...","หุบเขาเงามืด ทางตะวันตกของป่า Death End","ที่นั่นมีป้อมปราการของเขาอยู่","งั้นเราก็มีจุดหมายต่อไปแล้วสินะ", "แต่ที่นั่นอันตรายกว่าที่นี่อีก" };

    public part7() {
        setTitle("ISEKAI DEMO - Part 7 (Dual System)");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playBGM("res/sound/soundrack11.wav", -5.0f);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        
        bgFadeOverlay = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, (int)(bgAlpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        bgFadeOverlay.setBounds(0, 0, 1280, 800);
        bgFadeOverlay.setOpaque(false);
        layeredPane.add(bgFadeOverlay, Integer.valueOf(JLayeredPane.DEFAULT_LAYER + 1));
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // --- สร้าง Dual Labels พร้อมระบบ Fade เหมือน Part 8 ---
        leftCharLabel = createFadeLabel("left");
        layeredPane.add(leftCharLabel, JLayeredPane.PALETTE_LAYER);
        
        rightCharLabel = createFadeLabel("right");
        layeredPane.add(rightCharLabel, JLayeredPane.PALETTE_LAYER);

        fadeOverlay = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fadeOverlay.setBounds(0, 0, 1280, 800);
        fadeOverlay.setOpaque(false);
        layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);

        startFadeIn();
        setupDialogueUI();
        setupRelationshipUI();
        setupStatusOverlay(); 
        setupTabKeyBinding(); 
        initNetwork();
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { handleNext(); }
        });
    }

    private JLabel createFadeLabel(String side) {
        return new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                float currentAlpha = side.equals("left") ? leftAlpha : rightAlpha;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentAlpha));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        
        if (currentIndex < imagePaths.length) {
            String newBg = imagePaths[currentIndex];
            if (!newBg.equals(lastBgPath)) { startBackgroundTransition(newBg); lastBgPath = newBg; }
        }

        String lp = (currentIndex < leftCharPaths.length) ? leftCharPaths[currentIndex] : "res/empty.png";
        String rp = (currentIndex < rightCharPaths.length) ? rightCharPaths[currentIndex] : "res/empty.png";
        
        boolean hasLeft = !lp.contains("empty.png");
        boolean hasRight = !rp.contains("empty.png");

        // --- [จัดการฝั่งซ้าย] ---
        if (!hasLeft) {
            if (!lastLeftPath.equals(lp)) startFadeEffect("left", false);
        } else {
            int sw, sh, posY;
            // กำหนดขนาดตามตัวละคร
            if (lp.contains("Dan")) {
                sw = 1200; sh = 800; posY = 50;
            } else if (lp.contains("Alice")) {
                sw = 1000; sh = 700; posY = 60; // ขนาด Alice
            } else {
                sw = 1200; sh = 1000; posY = 50;
            }
            
            // ถ้ามีตัวละครฝั่งขวา ให้เขยิบไปทางซ้ายสุด (-280) ถ้าไม่มีให้ดีดเข้ากลาง
            int posX = hasRight ? -200 : (1280 - sw) / 2;
            leftCharLabel.setBounds(posX, posY, sw, sh);
            
            if (!lp.equals(lastLeftPath)) {
                leftCharLabel.setIcon(getOptimizedImage(lp, sw, sh));
                startFadeEffect("left", true);
            }
        }
        lastLeftPath = lp;

        // --- [จัดการฝั่งขวา] ---
        if (!hasRight) {
            if (!lastRightPath.equals(rp)) startFadeEffect("right", false);
        } else {
            int sw, sh, posY;
            // รองรับทั้ง Nebula และ Alice (ที่ย้ายมาฝั่งขวา)
            if (rp.contains("Nebula")) {
                sw = 1000; sh = 1000; posY = 0; // Nebula ตัวสูงสง่า
            } else if (rp.contains("Alice")) {
                sw = 1000; sh = 700; posY = 60; // Alice ขนาดเท่าเดิม
            } else {
                sw = 1200; sh = 1000; posY = 50;
            }

            // ถ้ามีตัวละครฝั่งซ้าย ให้เขยิบไปทางขวา (500) ถ้าไม่มีให้ดีดเข้ากลาง
            int posX = hasLeft ? 500 : (1280 - sw) / 2;
            rightCharLabel.setBounds(posX, posY, sw, sh);

            if (!rp.equals(lastRightPath)) {
                rightCharLabel.setIcon(getOptimizedImage(rp, sw, sh));
                startFadeEffect("right", true);
            }
        }
        lastRightPath = rp;

        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    private void startFadeEffect(String side, boolean fadeIn) {
        Timer timer = side.equals("left") ? leftFadeTimer : rightFadeTimer;
        if (timer != null) timer.stop();
        
        Timer newTimer = new Timer(20, e -> {
            if (side.equals("left")) {
                leftAlpha += fadeIn ? 0.05f : -0.05f;
                if (leftAlpha >= 1.0f) { leftAlpha = 1.0f; ((Timer)e.getSource()).stop(); }
                else if (leftAlpha <= 0.0f) { leftAlpha = 0.0f; ((Timer)e.getSource()).stop(); leftCharLabel.setIcon(null); }
                leftCharLabel.repaint();
            } else {
                rightAlpha += fadeIn ? 0.05f : -0.05f;
                if (rightAlpha >= 1.0f) { rightAlpha = 1.0f; ((Timer)e.getSource()).stop(); }
                else if (rightAlpha <= 0.0f) { rightAlpha = 0.0f; ((Timer)e.getSource()).stop(); rightCharLabel.setIcon(null); }
                rightCharLabel.repaint();
            }
        });
        
        if (side.equals("left")) leftFadeTimer = newTimer; else rightFadeTimer = newTimer;
        newTimer.start();
    }

    // --- ส่วนที่เหลือ (Textbox, Choice, Sound, Network) คงเดิมตามโค้ดของคุณ ---

    private void handleNext() {
        if (isChoosing || isFinishing) return;
        if (isTyping) {
            stopTypewriter();
            dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        if (currentIndex == 6) { showChoices("ไม่ต้องกลัวหรอก ฉันอยู่ข้างๆเธอ", "ถ้าระวังตัวดีๆก็น่าจะไม่เป็นไร", "นี่เธอจะกลัวอะไรนักหนา", 7, 8, 9); return; }
        if (currentIndex == 7 || currentIndex == 8 || currentIndex == 9) { currentIndex = 10; updateScene(); return; }

        if (currentIndex == 47) { showChoices("ต่อให้เธอเป็นจอมมาร ถ้าทําร้ายผู้บริสุทธิ์ฉันก็จะสู้", "ก็แค่คิดว่าเธอคงไม่ใช่คนเลวจริงๆ","ฉันเเค่อยากจะต่อสู้เท่านั้นเเหละ",48, 49, 50); return; }
        if (currentIndex == 48 || currentIndex == 49 || currentIndex == 50) { currentIndex = 51; updateScene(); return; }

        if (currentIndex == 52) { showChoices("เพราะเธอไม่ได้ดูน่ากลัวขนาดนั้น", "ถ้าจอมมารสวยขนาดนี้ ใครจะกลัวลง", "ก็นะ ฉันไม่ค่อยกลัวอะไรมากหรอก", 53, 54, 55); return; }
        if (currentIndex == 53 || currentIndex == 54 || currentIndex == 55) { currentIndex = 56; updateScene(); return; }

        if (currentIndex < dialogues.length - 1) { currentIndex++; updateScene(); } 
        else { finishGame(); }
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

    private void handleSoundEffects(int index) {
        if (index == 7) playEffect("res/sound/emmm.wav", 5.0f);
        if (index == 8) playEffect("res/sound/wakarunai.wav", 5.0f);
        if (index == 14) playEffect("res/sound_nebula/ningennoka.wav", 0.0f);
        if (index == 22) playEffect("res/sound_nebula/naanii.wav", 0.0f);
        if (index == 26) playEffect("res/sound_nebula/hahaha.wav", 0.0f);
        if (index == 47) playEffect("res/sound_nebula/hahaha.wav", 0.0f);
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                java.net.Socket socket = new java.net.Socket(relationdata.serverIP, 5000);
                networkOut = new java.io.PrintWriter(socket.getOutputStream(), true);
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:7");
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("LOAD_AFFINITY:")) {
                        int score = Integer.parseInt(line.substring(14));
                        relationdata.aliceRel.setAffinity(score);
                        SwingUtilities.invokeLater(() -> { affinityLabel.setText("อริส: " + score); statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus()); });
                    } else if (line.startsWith("LOAD_NEBULA:")) {
                        int nScore = Integer.parseInt(line.substring(12));
                        relationdata.nebulaRel.setAffinity(nScore);
                        SwingUtilities.invokeLater(() -> { nebulaAffinityLabel.setText("เนบิวล่า: " + nScore); nebulaStatusLabel.setText("สถานะ: " + relationdata.nebulaRel.getStatus()); });
                    } else if (line.startsWith("ALL_STATS:")) { updateLeaderboardUI(line.substring(10)); }
                }
            } catch (Exception e) {}
        }).start();
    }

    private void showChoices(String text1, String text2, String text3, int t1, int t2, int t3) {
        isChoosing = true;
        if (choiceButton1 != null) layeredPane.remove(choiceButton1);
        if (choiceButton2 != null) layeredPane.remove(choiceButton2);
        if (choiceButton3 != null) layeredPane.remove(choiceButton3);
        choiceButton1 = createChoiceButton(text1, 250, t1);
        choiceButton2 = createChoiceButton(text2, 320, t2);
        choiceButton3 = createChoiceButton(text3, 390, t3);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton3, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private void setupStatusOverlay() {
        statusOverlay = new JPanel(new BorderLayout(10, 10));
        statusOverlay.setBackground(new Color(0, 0, 0, 210)); 
        statusOverlay.setBounds(440, 150, 400, 400); 
        statusOverlay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        statusOverlay.setVisible(false);
        onlineCountLabel = new JLabel("ผู้เล่นออนไลน์: 1", SwingConstants.CENTER);
        onlineCountLabel.setForeground(Color.CYAN); 
        onlineCountLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        JLabel titleLabel = new JLabel("--- Scoreboard ---", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW); 
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        affinityStatusLabel = new JLabel("กำลังโหลดข้อมูล...", SwingConstants.CENTER);
        affinityStatusLabel.setForeground(Color.WHITE); 
        affinityStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        affinityStatusLabel.setVerticalAlignment(SwingConstants.TOP);
        statusOverlay.add(titleLabel, BorderLayout.NORTH);
        statusOverlay.add(affinityStatusLabel, BorderLayout.CENTER);
        statusOverlay.add(onlineCountLabel, BorderLayout.SOUTH); 
        layeredPane.add(statusOverlay, JLayeredPane.DRAG_LAYER);
    }

    private void setupTabKeyBinding() {
        layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "toggleTab");
        layeredPane.getActionMap().put("toggleTab", new AbstractAction() { @Override public void actionPerformed(java.awt.event.ActionEvent e) { statusOverlay.setVisible(!statusOverlay.isVisible()); } });
    }

    private void updateLeaderboardUI(String data) {
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'><table width='360' style='color:white; font-family:Tahoma;'>");
        sb.append("<tr style='color:#FFD700;'><th align='left' width='160'>ผู้เล่น</th><th align='right' width='90'>อริส</th><th align='right' width='90'>เนบิวล่า</th></tr>");
        for (String p : data.split(",")) {
            if (p.contains("=")) {
                String[] parts = p.split("="); String name = parts[0]; String rawScores = parts[1]; 
                String aliceValue = "0", nebulaValue = "0";
                if (rawScores.contains("/")) { String[] sp = rawScores.split("/"); aliceValue = sp[0]; nebulaValue = sp[1]; } 
                else aliceValue = rawScores;
                String color = name.equals(relationdata.playerName) ? "#00FF7F" : "white";
                sb.append("<tr>").append("<td style='color:").append(color).append(";'>").append(name).append("</td>").append("<td align='right' style='color:#FFC0CB;'>").append(aliceValue).append("</td>").append("<td align='right' style='color:#DA70D6;'>").append(nebulaValue).append("</td>").append("</tr>");
            }
        }
        sb.append("</table></body></html>");
        SwingUtilities.invokeLater(() -> { affinityStatusLabel.setText(sb.toString()); onlineCountLabel.setText("ผู้เล่นออนไลน์: " + data.split(",").length); });
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            private double scale = 1.0;
            private int alphaMod = 150;
            private Timer animTimer;
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int centerX = getWidth() / 2; int centerY = getHeight() / 2;
                g2.translate(centerX, centerY); g2.scale(scale, scale); g2.translate(-centerX, -centerY);
                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(new Color(225, 105, 180)); 
                g2.setStroke(new BasicStroke(2));   
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
                g2.dispose(); super.paintComponent(g);
            }
            { addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { startAnimation(1.05, 200); }
                @Override public void mouseExited(MouseEvent e) { startAnimation(1.0, 150); }
            }); }
            private void startAnimation(double ts, int ta) {
                if (animTimer != null) animTimer.stop();
                animTimer = new Timer(15, ev -> {
                    scale += (ts - scale) * 0.2;
                    if (alphaMod < ta) alphaMod += 5; else if (alphaMod > ta) alphaMod -= 5;
                    if (Math.abs(scale - ts) < 0.01 && alphaMod == ta) ((Timer)ev.getSource()).stop();
                    repaint();
                });
                animTimer.start();
            }
        };
        btn.setBounds(800, y, 350, 60); btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        btn.setForeground(new Color(45, 65, 115)); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false); btn.setFocusPainted(false); btn.setBorderPainted(false); 
        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1); layeredPane.remove(choiceButton2); layeredPane.remove(choiceButton3);
            isChoosing = false; 
            if (target == 7) relationdata.aliceRel.addAffinity(10);
            else if (target == 9) relationdata.aliceRel.decreaseAffinity(5);
            if (target == 49 || target == 54) relationdata.nebulaRel.addAffinity(15);
            else if (target == 48 || target == 53) relationdata.nebulaRel.addAffinity(5);
            else if (target == 50) relationdata.nebulaRel.decreaseAffinity(5);
            if (relationdata.isOnlineMode && networkOut != null) {
                new Thread(() -> {
                    networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                    networkOut.println("UPDATE_NEBULA_AFFINITY:" + relationdata.nebulaRel.getAffinity());
                }).start();
            }
            affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            nebulaAffinityLabel.setText("เนบิวล่า: " + relationdata.nebulaRel.getAffinity());
            nebulaStatusLabel.setText("สถานะ: " + relationdata.nebulaRel.getStatus());
            currentIndex = target; updateScene();
        });
        return btn;
    }

    public void playBGM(String path, float volume) {
        try { if (bgmClip != null && bgmClip.isRunning()) return; 
        AudioInputStream ai = AudioSystem.getAudioInputStream(new File(path));
        bgmClip = AudioSystem.getClip(); bgmClip.open(ai);
        ((FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume);
        bgmClip.loop(Clip.LOOP_CONTINUOUSLY); bgmClip.start(); } catch (Exception e) {}
    }

    public void playEffect(String path, float volume) {
        try { AudioInputStream ai = AudioSystem.getAudioInputStream(new File(path));
        Clip c = AudioSystem.getClip(); c.open(ai);
        ((FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume);
        c.start(); } catch (Exception e) {}
    }

    private void startBackgroundTransition(String newPath) {
        if (bgFadeTimer != null) bgFadeTimer.stop();
        bgFadeTimer = new Timer(20, e -> {
            bgAlpha += 0.05f;
            if (bgAlpha >= 1.0f) {
                bgAlpha = 1.0f; bgFadeTimer.stop();
                backgroundLabel.setIcon(getOptimizedImage(newPath, 1280, 800));
                Timer fadeIn = new Timer(25, ev -> {
                    bgAlpha -= 0.05f; if (bgAlpha <= 0.0f) { bgAlpha = 0.0f; ((Timer)ev.getSource()).stop(); }
                    bgFadeOverlay.repaint();
                });
                fadeIn.start();
            }
            bgFadeOverlay.repaint();
        });
        bgFadeTimer.start();
    }

    private void startFadeIn() {
        alpha = 1.0f;
        Timer ft = new Timer(50, e -> {
            alpha -= 0.02f;
            if (alpha <= 0) { alpha = 0; ((Timer)e.getSource()).stop(); layeredPane.remove(fadeOverlay); updateScene(); }
            fadeOverlay.repaint();
        });
        ft.start();
    }

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(4, 1, 0, 0)); 
        relPanel.setBounds(0, 0, 280, 120); 
        relPanel.setBackground(new Color(0, 0, 0, 190)); relPanel.setOpaque(true);
        relPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 105, 180), 2), BorderFactory.createEmptyBorder(5, 15, 5, 10)));
        affinityLabel = createRelLabel("อริส: " + relationdata.aliceRel.getAffinity(), new Color(255, 192, 203), 18);
        statusLabel = createRelLabel("สถานะ: " + relationdata.aliceRel.getStatus(), Color.WHITE, 14);
        nebulaAffinityLabel = createRelLabel("เนบิวล่า: " + relationdata.nebulaRel.getAffinity(), new Color(210, 160, 255), 18);
        nebulaStatusLabel = createRelLabel("สถานะ: " + relationdata.nebulaRel.getStatus(), Color.WHITE, 14);
        relPanel.add(affinityLabel); relPanel.add(statusLabel); relPanel.add(nebulaAffinityLabel); relPanel.add(nebulaStatusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }
    private JLabel createRelLabel(String t, Color c, int s) { JLabel l = new JLabel(t); l.setFont(new Font("Tahoma", Font.BOLD, s)); l.setForeground(c); return l; }

    private void startTypewriter(String text) {
        stopTypewriter(); isTyping = true; charIndex = 0;
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) { charIndex++; dialogueArea.setText("<html><body style='width: 950px;'>" + text.substring(0, charIndex) + "</body></html>"); } 
            else { stopTypewriter(); }
        });
        typewriterTimer.start();
    }
    private void stopTypewriter() { if (typewriterTimer != null) typewriterTimer.stop(); isTyping = false; }

    private void finishGame() {
        if (isFinishing) return; isFinishing = true;
        if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        alpha = 0.0f;
        new Timer(30, e -> {
            alpha += 0.03f;
            if (alpha >= 1.0f) {
                alpha = 1.0f; ((Timer)e.getSource()).stop();
                if (bgmClip != null) { bgmClip.stop(); bgmClip.close(); }
                SwingUtilities.invokeLater(() -> { new part8().setVisible(true); dispose(); });
            }
            fadeOverlay.repaint();
        }).start();
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) {
            try { Image img = new ImageIcon(path).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH); imageCache.put(key, new ImageIcon(img)); } catch (Exception e) { return null; }
        }
        return imageCache.get(key);
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new part7().setVisible(true)); }
}

class VisualNovelBox extends JPanel {
    public VisualNovelBox() { setOpaque(false); }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, new Color(245, 250, 255, 180), 0, getHeight(), new Color(255, 235, 245, 230)));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); g2.setColor(new Color(255, 150, 200, 200));
        g2.setStroke(new BasicStroke(4f)); g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 30, 30);
    }
}