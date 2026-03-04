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

public class part8 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, leftCharLabel, rightCharLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel; 
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

    private float alpha = 1.0f; 
    private JPanel fadeOverlay; 
    private float leftAlpha = 0.0f; 
    private float rightAlpha = 0.0f;
    private Timer leftFadeTimer, rightFadeTimer;
    private String lastLeftPath = "res/empty.png";
    private String lastRightPath = "res/empty.png";
    private float bgAlpha = 0.0f; 
    private Timer bgFadeTimer;
    private String lastBgPath = "";
    private JPanel bgFadeOverlay; 

    private String[] leftCharPaths = { 
    /* 0-9   */ "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png",
    /* 10-14 */ "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
    /* 15-20 */ "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
    /* 21-23 */ "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png",
    /* 24-27 */ "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png",
    /* 28-32 */ "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
    /* 33-36 */ "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png",
    /* 37-42 */ "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png",
    /* 43-46 */ "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png",
    /* 47-52 */ "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
    /* 53-57 */ "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
    /* 58-62 */ "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
    /* 63-70 */ "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
    /* 71-76 */ "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png",
    /* 77-80 */ "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png"
};

    private String[] rightCharPaths = { 
    /* 0-9   */ "res/empty.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png",
    /* 10-14 */ "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
    /* 15-20 */ "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
    /* 21-23 */ "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
    /* 24-27 */ "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
    /* 28-32 */ "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
    /* 33-36 */ "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
    /* 37-42 */ "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
    /* 43-46 */ "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
    /* 47-52 */ "res/empty.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
    /* 53-57 */ "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-shy2.png", "res/Charactor/Nebula/Nebula-shy1.png", "res/Charactor/Nebula/Nebula-normal2.png",
    /* 58-62 */ "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-shy2.png",
    /* 63-70 */ "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
    /* 71-76 */ "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
    /* 77-80 */ "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png"
};

    private String[] imagePaths = {
    /* 0-9   */ "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png",
    /* 10-19 */ "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png",
    /* 20-29 */ "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png",
    /* 30-39 */ "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png",
    /* 40-46 */ "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png",
    /* 47-56 */ "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png",
    /* 57-66 */ "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png",
    /* 67-76 */ "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png",
    /* 77-80 */ "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png"
};
    private String[] names = {
    /* 0-9   */ " ", "Dan", "อริส", "Nebula", "Nebula", "Nebula", "ฉัน", "Nebula", "Nebula", "Dan",
    /* 10-20 */ "อริส", "Nebula", "Nebula", "Nebula","Nebula","Nebula","ฉัน","Nebula","ฉัน","Nebula","Nebula",
    /* 21-31 */ "Nebula", "Dan", "อริส", "Nebula", "Nebula", "Nebula","Nebula","อริส", "Dan", "อริส", "Nebula",
    /* 32-42 */ "Nebula", "ฉัน", "Nebula", "Nebula", "Nebula","Nebula","Nebula","Nebula","Nebula","Dan","อริส",
    /* 43-53 */ "Dan", "Nebula", "Nebula", "Nebula", " ", "ฉัน", "Nebula", "ฉัน", "Nebula", "Nebula", "ฉัน",
    /* 54-65 */ "Nebula", "Nebula", "Nebula","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula",
    /* 66-75 */ "Nebula", " ", "Dan", "อริส", "ฉัน", "Nebula", "Nebula", "Nebula", "Nebula", "Nebula",
    /* 76-79 */ "Nebula", "Nebula","Nebula", "Nebula"
    };
    private String[] dialogues = { 
        "หลังจากการต่อสู้จบลง บรรยากาศในปราสาทก็กลับมาเงียบสงบอีกครั้ง", // 0
        "ไม่อยากเชื่อเลยว่าเราจะมายืนคุยกับจอมมารแบบนี้ได้", // 1
        "ฉันก็ยังไม่ค่อยเชื่อเหมือนกัน...", // 2
        "พวกเจ้ามนุษย์นี่แปลกจริงๆ", // 3
        "ปกติแล้วมนุษย์ที่มาที่นี่ จะตัวสั่นด้วยความกลัว", // 4
        "แต่พวกเจ้ากลับยืนคุยกันสบายๆ", // 5
        "ก็เพราะเธอไม่ได้ดูเหมือนศัตรูของพวกเรานี่", // 6
        "หืม?", // 7
        "เจ้ากล้าพูดกับจอมมารแบบนั้นเลยงั้นหรอ", // 8
        "นายพูดกับจอมมารเหมือนคุยกับคนปกติเลยนะ", // 9
        "นะ...นายไม่กลัวเลยรึไง", // 10
        "หึ...มนุษย์ที่คิดแบบนี้ไม่ค่อยมีหรอกนะ", // 11 choice1
        "เจ้าดูเข้าใจสถานการณ์ดีนี่", // 12 choice2
        "ปากเก่งจริงๆเลยนะ", // 13 choice3
        "เจ้าพวกมนุษย์...", // 14
        "ทําไมถึงกล้าเดินทางมาถึงที่นี่", // 15
        "ก็เพราะเราอยากหยุดเรื่องทั้งหมดนี่", // 16
        "เพื่อมนุษย์คนอื่นงั้นหรอ", // 17
        "ก็ประมาณนั้น", // 18
        "มนุษย์ส่วนใหญ่เห็นแก่ตัว", // 19
        "แต่เจ้ากลับเสี่ยงชีวิตเพื่อคนอื่น", // 20
        "เจ้าคนนี้น่าสนใจจริงๆ", // 21
        "เหมือนจอมมารกําลังชมอยู่นะเนี่ย", // 22
        "นายอย่าทําตัวสนิทกับจอมมารเกินไปสิ!", // 23
        "หึ...เจ้าคนนี้ไม่ถ่อมตัวเลยนะ", // 24 choice1
        "ใครจะไปชมเจ้ากันเล่า", // 25 choice2
        "ก็ถือว่าดี", // 26 choice3
        "แต่ก็ไม่ปฏิเสธหรอกนะ", // 27
        "นี่พวกนายคุยกันสนิทเกินไปแล้วนะ!", // 28
        "ดูเหมือนจะมีคนเริ่มหึงแล้ว", // 29
        "ขะ...ใครหึงกัน!", // 30
        "หึงงั้นหรอ?", // 31
        "หรือว่าเจ้าจะ..หึๆ..", // 32
        "อย่าแกล้งอริสมากนักสิ", // 33
        "ข้าก็แค่พูดเล่นเท่านั้นเอง", // 34 choice1
        "ก็เพราะพวกเจ้าดูน่าสนุกนี่", // 35 choice2
        "...",// 36 choice3
        "มนุษย์...", // 37
        "ปกติข้าไม่ค่อยให้ใครอยู่ในปราสาทนานนัก", // 38
        "แต่พวกเจ้าดูต่างออกไป", // 39
        "ข้าจะให้พวกเจ้าพักที่นี่คืนนี้ก็ได้", // 40
        "จริงหรอเนี่ย", // 41
        "พักในปราสาทจอมมาร...", // 42
        "นี่มันประสบการณ์แปลกๆจริงๆ", // 43
        "ข้าไม่ได้ใจดีหรอก", // 44 choice1
        "เจ้านี่พูดเก่งจริงๆ", // 45 choice2
        "ก็เอาตามนั้นละกัน", // 46 choice3
        "คืนนั้น ฉันออกมาเดินเล่นที่ระเบียงของปราสาท", // 47
        "ลมเย็นดีแฮะ", // 48
        "เจ้าก็มาที่นี่เหมือนกันสินะ", // 49
        "อ้าว เธอก็อยู่ที่นี่หรอ", // 50
        "ข้ามักจะมามองป่าตอนกลางคืน", // 51
        "มันเงียบดี", // 52
        "เธอดูไม่เหมือนจอมมารเลยนะ", // 53
        "มนุษย์คิดว่าจอมมารต้องเป็นยังไงล่ะ", // 54
        "เจ้ากล้าพูดกับจอมมารแบบนั้นอีกแล้วนะ", // 55 choice1
        "เจ้าพูดเเบบนี้อีกเเล้วนะ...", // 56 choice2
        "เห้อ...", // 57 choice3
        "เจ้าคนนี้...", // 58
        "ไม่กลัวข้าเลยจริงๆสินะ", // 59
        "หึ…", // 60 choice1
        "งั้นเหรอ...", // 61 choice2
        "เจ้าบ้านี่…",// 62 choice3
        "มนุษย์อย่างเจ้าแปลกจริงๆ", // 63
        "แต่ก็ไม่ได้น่ารําคาญ", // 64
        "ตรงกันข้าม...", // 65
        "ข้ากลับรู้สึกว่าเจ้าคือน่าสนใจ", // 66
        "เช้าวันต่อมา...", // 67
        "ได้เวลาออกเดินทางแล้วสินะ", // 68
        "เป้าหมายต่อไปคือ Grey", // 69
        "ใช่", // 70
        "มนุษย์...", // 71
        "ถ้าเจ้าจะไปสู้กับ Grey", // 72
        "ก็อย่าตายซะก่อนล่ะ", // 73
        "หึ...", // 74 choice1
        "หึ...", // 75 choice2
        "หึ...", // 76 choice3
        "ถ้าเจ้ารอดกลับมาได้จริง", // 77
        "ข้าจะรอดู", // 78
        "มนุษย์...", // 79
        "เจ้าเป็นคนที่น่าสนใจจริงๆ" //80
    };

    public part8() {
        setTitle("ISEKAI DEMO - Part 8 (Dual Path System)");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playBGM("res/sound/soundtrack13.wav", -10.0f);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

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
        bgFadeOverlay.setEnabled(false); 
        layeredPane.add(bgFadeOverlay, Integer.valueOf(JLayeredPane.DEFAULT_LAYER + 1));

        leftCharLabel = createLeftFadeLabel();
        layeredPane.add(leftCharLabel, JLayeredPane.PALETTE_LAYER);
        rightCharLabel = createRightFadeLabel();
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

    private void updateScene() {
            if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
            if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
            
            // --- จัดการ Background ---
            if (currentIndex < imagePaths.length) {
                String newBg = imagePaths[currentIndex];
                if (!newBg.equals(lastBgPath)) { 
                    startBackgroundTransition(newBg); 
                    lastBgPath = newBg; 
                }
            }

            String lp = (currentIndex < leftCharPaths.length) ? leftCharPaths[currentIndex] : "res/empty.png";
            String rp = (currentIndex < rightCharPaths.length) ? rightCharPaths[currentIndex] : "res/empty.png";
            
            boolean hasLeft = !lp.contains("empty.png");
            boolean hasRight = !rp.contains("empty.png");
            boolean lastHadLeft = !lastLeftPath.contains("empty.png");
            boolean lastHadRight = !lastRightPath.contains("empty.png");

            // --- จัดการตัวละครฝั่งซ้าย (Left Character) ---
            if (!hasLeft) {
                // ถ้าเดิมเคยมี แต่ตอนนี้ไม่มี ให้ Fade Out
                if (lastHadLeft) startLeftFadeOut();
            } else {
                int sw = lp.contains("Dan") ? 1500 : (lp.contains("Alice") ? 1100 : 1000);
                int sh = lp.contains("Dan") ? 1100 : (lp.contains("Alice") ? 900 : 1000);
                int posX = hasRight ? -400 : (1280 - sw) / 2;

                leftCharLabel.setBounds(posX, 50, sw, sh);
                
                if (!lp.equals(lastLeftPath)) {
                    leftCharLabel.setIcon(getOptimizedImage(lp, sw, sh));
                    // Fade In เฉพาะตอนที่ตัวละคร "โผล่มาจากความว่างเปล่า" 
                    // ถ้าแค่เปลี่ยนท่า (เดิมมีอยู่แล้ว) ไม่ต้อง Fade เพื่อให้ภาพต่อเนื่อง
                    if (!lastHadLeft) {
                        startLeftFadeIn();
                    }
                }
            }
            lastLeftPath = lp;

            // --- จัดการตัวละครฝั่งขวา (Right Character) ---
            if (!hasRight) {
                if (lastHadRight) startRightFadeOut();
            } else {
                int sw = rp.contains("Nebula") ? 900 : (rp.contains("Alice") ? 1100 : 1000);
                int sh = rp.contains("Nebula") ? 900 : (rp.contains("Alice") ? 900 : 900);
                int posX = hasLeft ? 400 : (1280 - sw) / 2;

                rightCharLabel.setBounds(posX, 100, sw, sh);
                
                if (!rp.equals(lastRightPath)) {
                    rightCharLabel.setIcon(getOptimizedImage(rp, sw, sh));
                    // Fade In เฉพาะตอนที่ "ตัวละครเพิ่งปรากฏตัว"
                    if (!lastHadRight) {
                        startRightFadeIn();
                    }
                }
            }
            lastRightPath = rp;

            handleSoundEffects(currentIndex);
            layeredPane.repaint();
    }
    private JLabel createLeftFadeLabel() {
        return new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, leftAlpha));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
    }

    private JLabel createRightFadeLabel() {
        return new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, rightAlpha));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
    }

    private void startLeftFadeIn() {
        if (leftFadeTimer != null) leftFadeTimer.stop();
        leftFadeTimer = new Timer(20, e -> {
            leftAlpha += 0.05f;
            if (leftAlpha >= 1.0f) { leftAlpha = 1.0f; leftFadeTimer.stop(); }
            leftCharLabel.repaint();
        });
        leftFadeTimer.start();
    }

    private void startLeftFadeOut() {
        if (leftFadeTimer != null) leftFadeTimer.stop();
        leftFadeTimer = new Timer(20, e -> {
            leftAlpha -= 0.05f;
            if (leftAlpha <= 0.0f) { leftAlpha = 0.0f; leftFadeTimer.stop(); leftCharLabel.setIcon(null); }
            leftCharLabel.repaint();
        });
        leftFadeTimer.start();
    }

    private void startRightFadeIn() {
        if (rightFadeTimer != null) rightFadeTimer.stop();
        rightFadeTimer = new Timer(20, e -> {
            rightAlpha += 0.05f;
            if (rightAlpha >= 1.0f) { rightAlpha = 1.0f; rightFadeTimer.stop(); }
            rightCharLabel.repaint();
        });
        rightFadeTimer.start();
    }

    private void startRightFadeOut() {
        if (rightFadeTimer != null) rightFadeTimer.stop();
        rightFadeTimer = new Timer(20, e -> {
            rightAlpha -= 0.05f;
            if (rightAlpha <= 0.0f) { rightAlpha = 0.0f; rightFadeTimer.stop(); rightCharLabel.setIcon(null); }
            rightCharLabel.repaint();
        });
        rightFadeTimer.start();
    }

    private void startBackgroundTransition(String newPath) {
        if (bgFadeTimer != null) bgFadeTimer.stop();
        bgFadeTimer = new Timer(20, null);
        bgFadeTimer.addActionListener(e -> {
            bgAlpha += 0.08f;
            if (bgAlpha >= 1.0f) {
                bgAlpha = 1.0f; bgFadeTimer.stop();
                backgroundLabel.setIcon(getOptimizedImage(newPath, 1280, 800));
                Timer fadeIn = new Timer(25, ev -> {
                    bgAlpha -= 0.08f;
                    if (bgAlpha <= 0.0f) { bgAlpha = 0.0f; ((Timer)ev.getSource()).stop(); }
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
        Timer fadeInTimer = new Timer(50, null);
        fadeInTimer.addActionListener(e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0;
                fadeInTimer.stop();
                fadeOverlay.setVisible(false);
                layeredPane.remove(fadeOverlay); // ลบออกเพื่อไม่ให้ทับปุ่ม
                layeredPane.repaint();
            }
            fadeOverlay.repaint();
        });
        fadeInTimer.start();
    }

    private void showChoices(String c1, String c2, String c3, int t1, int t2, int t3) {
        isChoosing = true;
        if (choiceButton1 != null) layeredPane.remove(choiceButton1);
        if (choiceButton2 != null) layeredPane.remove(choiceButton2);
        if (choiceButton3 != null) layeredPane.remove(choiceButton3);
        // ปรับ Y ตามโค้ด Part 6 เพื่อไม่ให้ทับกล่องข้อความ
        choiceButton1 = createChoiceButton(c1, 250, t1);
        choiceButton2 = createChoiceButton(c2, 320, t2);
        choiceButton3 = createChoiceButton(c3, 390, t3);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton3, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            private double scale = 1.0;
            private int alphaMod = 170; 
            private Timer animTimer;
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(getWidth()/2, getHeight()/2);
                g2.scale(scale, scale);
                g2.translate(-getWidth()/2, -getHeight()/2);
                
                // สไตล์ Glassmorphism ตามแบบ Part 6
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255, alphaMod), 
                                                    0, getHeight(), new Color(230, 230, 230, alphaMod));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2.setColor(new Color(255, 102, 153));
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 18, 18);
                
                g2.dispose();
                super.paintComponent(g);
            }
            { addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { startAnimation(1.03, 230); }
                @Override public void mouseExited(MouseEvent e) { startAnimation(1.0, 170); }
            }); }
            private void startAnimation(double ts, int ta) {
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
        // X=800 (ขวาบน)
        btn.setBounds(800, y, 380, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 17));
        btn.setForeground(new Color(50, 50, 50));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1); 
            layeredPane.remove(choiceButton2);
            layeredPane.remove(choiceButton3);
            isChoosing = false; 
            if (target == 34){
                relationdata.aliceRel.addAffinity(5);
            } else if (target == 36){
                relationdata.aliceRel.decreaseAffinity(5);
            }
            if (target == 62){
                relationdata.nebulaRel.addAffinity(15);
            } else if (target == 12 || target == 25 || target == 45 || target == 61 || target == 74){
                relationdata.nebulaRel.addAffinity(10);
            } else if (target == 11 || target == 24 || target == 35 || target == 56 || target == 60 || target == 75){
                relationdata.nebulaRel.addAffinity(5);
            } else if (target == 13 || target == 36 || target == 46 || target == 57){
                relationdata.nebulaRel.decreaseAffinity(5);
            } else if (target == 26 || target == 44 || target == 55 || target == 76){             
            }
           
            
            if (relationdata.isOnlineMode && networkOut != null) {
                new Thread(() -> {
                    networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                    networkOut.println("UPDATE_NEBULA_AFFINITY:" + relationdata.nebulaRel.getAffinity());
                }).start();
            }
            currentIndex = target; 
            updateScene();
            affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            nebulaAffinityLabel.setText("เนบิวล่า: " + relationdata.nebulaRel.getAffinity());
            nebulaStatusLabel.setText("สถานะ: " + relationdata.nebulaRel.getStatus());
        });
        return btn;
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                java.net.Socket socket = new java.net.Socket(relationdata.serverIP, 5000);
                networkOut = new java.io.PrintWriter(socket.getOutputStream(), true);
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:8");
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

    private void handleNext() {
        if (isChoosing || isFinishing) return;
        if (isTyping) { stopTypewriter(); dialogueArea.setText("<html><body style='width: 700px;'>" + dialogues[currentIndex] + "</body></html>"); return; }
        
        if (currentIndex == 10) { 
            showChoices("ก็เธอไม่ได้ทําอะไรพวกเรานี่", "ถ้าเธออยากฆ่าพวกเรา เราคงตายไปแล้ว", "ก็เเค่จอมมารจะกลัวทําไม", 11, 12, 13); 
            return; 
        }
        if (currentIndex == 11 || currentIndex == 12 || currentIndex == 13) { 
            currentIndex = 14; updateScene(); 
            return; 
        }
        if (currentIndex == 23) { 
            showChoices("ฉันก็แค่พูดตามที่คิด", "หรือว่าเธอกําลังชมฉันอยู่?", "ฉันเเค่ไม่อยากให้มันวุ่นวาย", 24, 25, 26); 
            return; 
        }
        if (currentIndex == 24 || currentIndex == 25 || currentIndex == 26) { 
            currentIndex = 27; updateScene(); 
            return; 
        }
        if (currentIndex == 33) { 
            showChoices("เดี๋ยวเธอร้องไห้ขึ้นมาจะทํายังไง", "เธอแกล้งคนอื่นสนุกนักรึไง", "ไร้สาระกันจริงๆ", 34, 35, 36); 
            return; 
        }
        if (currentIndex == 34 || currentIndex == 35 || currentIndex == 36) { 
            currentIndex = 37; updateScene(); 
            return; 
        }
        if (currentIndex == 43) { 
            showChoices("งั้นคืนนี้ฉันจะรบกวนหน่อยนะ", "เธอใจดีกว่าที่คิดนะ", "ก็ดีเหมือนกัน", 44, 45, 46); 
            return; 
        }
        if (currentIndex == 44 || currentIndex == 45 || currentIndex == 46) { 
            currentIndex = 47; updateScene(); 
            return; 
        }
        if (currentIndex == 54) { 
            showChoices("น่ากลัวกว่านี้มั้ง", "ไม่สวยขนาดนี้แน่ๆ", "ก็ไม่รู้สิ หน้าตาน่ากลัวมั้ง", 55, 56, 57); 
            return; 
        }
        if (currentIndex == 55 || currentIndex == 56 || currentIndex == 57) { 
            currentIndex = 58; updateScene(); 
            return; 
        }
        if (currentIndex == 59) { 
            showChoices("ก็เธอไม่ได้น่ากลัวนี่", "เพราะฉันเริ่มชินกับเธอแล้วมั้ง", "เพราะฉันอาจจะเริ่มชอบเธอก็ได้", 60, 61, 62); 
            return; 
        }
        if (currentIndex == 60 || currentIndex == 61 || currentIndex == 62) { 
            currentIndex = 63; updateScene(); 
            return; 
        }
        if (currentIndex == 73) { 
            showChoices("ถ้าฉันรอดกลับมา...ฉันจะมาเจอเธออีก", "ถ้าฉันรอดกลับมา เธอต้องเลี้ยงข้าวฉัน","ฉันไม่ตายง่ายๆหรอกน่า", 74, 75, 76); 
            return; 
        }
        if (currentIndex == 74 || currentIndex == 75 || currentIndex == 76) { 
            currentIndex = 77; updateScene(); 
            return; 
        }

        if (currentIndex < dialogues.length - 1) { currentIndex++; updateScene(); } 
        else { finishGame(); }
    }

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(4, 1, 0, 0)); 
        relPanel.setBounds(20, 20, 280, 120); 
        relPanel.setBackground(new Color(0, 0, 0, 190)); 
        relPanel.setOpaque(true);
        relPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 105, 180), 2), BorderFactory.createEmptyBorder(5, 15, 5, 10)));
        affinityLabel = createRelLabel("อริส: " + relationdata.aliceRel.getAffinity(), new Color(255, 192, 203), 18);
        statusLabel = createRelLabel("สถานะ: " + relationdata.aliceRel.getStatus(), Color.WHITE, 14);
        nebulaAffinityLabel = createRelLabel("เนบิวล่า: " + relationdata.nebulaRel.getAffinity(), new Color(210, 160, 255), 18);
        nebulaStatusLabel = createRelLabel("สถานะ: " + relationdata.nebulaRel.getStatus(), Color.WHITE, 14);
        relPanel.add(affinityLabel); relPanel.add(statusLabel); relPanel.add(nebulaAffinityLabel); relPanel.add(nebulaStatusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }
    
    private JLabel createRelLabel(String t, Color c, int s) { JLabel l = new JLabel(t); l.setFont(new Font("Tahoma", Font.BOLD, s)); l.setForeground(c); return l; }

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
                String[] parts = p.split("="); 
                String name = parts[0]; String rawScores = parts[1]; 
                String aliceValue = "0", nebulaValue = "0";
                if (rawScores.contains("/")) {
                    String[] scoreParts = rawScores.split("/");
                    aliceValue = scoreParts[0]; nebulaValue = scoreParts[1];
                } else aliceValue = rawScores;
                String color = name.equals(relationdata.playerName) ? "#00FF7F" : "white";
                sb.append("<tr>").append("<td style='color:").append(color).append(";'>").append(name).append("</td>").append("<td align='right' style='color:#FFC0CB;'>").append(aliceValue).append("</td>").append("<td align='right' style='color:#DA70D6;'>").append(nebulaValue).append("</td>").append("</tr>");
            }
        }
        sb.append("</table></body></html>");
        SwingUtilities.invokeLater(() -> { affinityStatusLabel.setText(sb.toString()); onlineCountLabel.setText("ผู้เล่นออนไลน์: " + data.split(",").length); });
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox(); dialoguePanel.setLayout(null); dialoguePanel.setBounds(225, 520, 800, 200);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);
        nameLabel = new JLabel(); nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26)); nameLabel.setForeground(new Color(180, 40, 90)); nameLabel.setBounds(60, 10, 300, 40); dialoguePanel.add(nameLabel);
        dialogueArea = new JLabel(); dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22)); dialogueArea.setForeground(new Color(45, 65, 115));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP); dialogueArea.setBounds(60, 60, 700, 110); dialoguePanel.add(dialogueArea);
    }

    private void startTypewriter(String t) {
        stopTypewriter(); isTyping = true; charIndex = 0;
        typewriterTimer = new Timer(30, e -> { if (charIndex < t.length()) { charIndex++; dialogueArea.setText("<html><body style='width: 700px;'>" + t.substring(0, charIndex) + "</body></html>"); } else stopTypewriter(); });
        typewriterTimer.start();
    }
    private void stopTypewriter() { if (typewriterTimer != null) typewriterTimer.stop(); isTyping = false; }

    private void handleSoundEffects(int i) {
        if (i == 28) playEffect("res/sound/Darega.wav", 5.0f);
        if (i == 29) playEffect("res/sound_nebula/Yakimochi.wav", 0.0f);
        if (i == 51) playEffect("res/sound_nebula/Kisama.wav", 0.0f);
    }

    public void playBGM(String p, float v) { try { if (bgmClip != null && bgmClip.isRunning()) return; AudioInputStream ai = AudioSystem.getAudioInputStream(new File(p)); bgmClip = AudioSystem.getClip(); bgmClip.open(ai); ((FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(v); bgmClip.loop(Clip.LOOP_CONTINUOUSLY); bgmClip.start(); } catch (Exception e) {} }
    public void playEffect(String p, float v) { try { AudioInputStream ai = AudioSystem.getAudioInputStream(new File(p)); Clip c = AudioSystem.getClip(); c.open(ai); ((FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN)).setValue(v); c.start(); } catch (Exception e) {} }

    private ImageIcon getOptimizedImage(String p, int w, int h) {
        String k = p + w + h; if (!imageCache.containsKey(k)) { try { Image i = new ImageIcon(p).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH); imageCache.put(k, new ImageIcon(i)); } catch (Exception e) { return null; } } return imageCache.get(k);
    }

    private void finishGame() {
        if (isFinishing) return; isFinishing = true;
        if (fadeOverlay == null) { fadeOverlay = new JPanel() { @Override protected void paintComponent(Graphics g) { Graphics2D g2d = (Graphics2D) g; g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255))); g2d.fillRect(0, 0, getWidth(), getHeight()); } }; fadeOverlay.setBounds(0, 0, 1280, 800); fadeOverlay.setOpaque(false); }
        if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        alpha = 0.0f;
        new Timer(30, e -> {
            alpha += 0.02f;
            if (alpha >= 1.0f) { alpha = 1.0f; ((Timer)e.getSource()).stop(); if (bgmClip != null) { bgmClip.stop(); bgmClip.close(); } SwingUtilities.invokeLater(() -> { new part9().setVisible(true); dispose(); }); }
            fadeOverlay.repaint();
        }).start();
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new part8().setVisible(true)); }
}

class VisualNovelBox extends JPanel {
    public VisualNovelBox() { setOpaque(false); }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, new Color(245, 250, 255, 180), 0, getHeight(), new Color(255, 235, 245, 230)));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); g2.setColor(new Color(255, 150, 200, 200));
        g2.setStroke(new BasicStroke(4f)); g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 30, 30);
    }
}