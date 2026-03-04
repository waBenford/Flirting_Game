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
    private JButton choiceButton1, choiceButton2;
    private boolean isChoosing = false;
    private boolean isFinishing = false; // เพิ่มบรรทัดนี้เพื่อป้องกันการคลิกซ้ำตอนจบ
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    // --- ระบบความสัมพันธ์และ Network ---
    private JLabel nebulaAffinityLabel, nebulaStatusLabel;
    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    private java.io.PrintWriter networkOut;

    // --- ระบบ Fade ---
    private float alpha = 1.0f; 
    private JPanel fadeOverlay; 
    private float charAlpha = 0.0f; 
    private Timer charFadeTimer;
    private String lastCharPath = "";
    private float bgAlpha = 0.0f; 
    private Timer bgFadeTimer;
    private String lastBgPath = "";
    private JPanel bgFadeOverlay; 
    
    // --- ข้อมูล Array ---
    private String[] imagePaths = {
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s2.png", 
       "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", 
       "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", 
       "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", 
       "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", 
       "res/scene8/s2.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png"
    };
    
    private String[] charPaths = { 
       "res/empty.png","res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",  "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png","res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png","res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Dan/dan-normal2.png",  "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-shy2.png",
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png","res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/empty.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-shy2.png", "res/Charactor/Nebula/Nebula-shy1.png","res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-shy1.png", "res/Charactor/Nebula/Nebula-shy2.png","res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/empty.png", 
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png","res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png","res/Charactor/Nebula/Nebula-normal2.png","res/Charactor/Nebula/Nebula-shy1.png",
       "res/Charactor/Nebula/Nebula-normal1.png","res/Charactor/Nebula/Nebula-normal2.png","res/Charactor/Nebula/Nebula-normal1.png","res/Charactor/Nebula/Nebula-normal2.png",
    };

    private String[] names = { 
            " ","Dan","อริส","Nebula","Nebula","Nebula","ฉัน","Nebula",
            "Nebula","Dan","อริส","Nebula","Nebula","Nebula","Nebula","ฉัน",
            "Nebula","ฉัน","Nebula","Nebula","Nebula","Dan","อริส","Nebula",
            "Nebula","Nebula","อริส","Dan","อริส","Nebula","Nebula","ฉัน",
            "Nebula","Nebula","Nebula","Nebula","Nebula","Nebula","Dan","อริส",
            "Dan","Nebula","Nebula"," ","ฉัน","Nebula","ฉัน","Nebula",
            "Nebula","ฉัน","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula",
            "Nebula","Nebula","Nebula","Nebula","Nebula"," ","Dan","อริส", 
            "ฉัน","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula",
    };
    
    private String[] dialogues = {
            "หลังจากการต่อสู้จบลง บรรยากาศในปราสาทก็กลับมาเงียบสงบอีกครั้ง", "ไม่อยากเชื่อเลยว่าเราจะมายืนคุยกับจอมมารแบบนี้ได้", 
            "ฉันก็ยังไม่ค่อยเชื่อเหมือนกัน...", "พวกเจ้ามนุษย์นี่แปลกจริงๆ", "ปกติแล้วมนุษย์ที่มาที่นี่ จะตัวสั่นด้วยความกลัว", 
            "แต่พวกเจ้ากลับยืนคุยกันสบายๆ", "ก็เพราะเธอไม่ได้ดูเหมือนศัตรูของพวกเรานี่", "หืม?", 
            "เจ้ากล้าพูดกับจอมมารแบบนั้นเลยงั้นหรอ", "นายพูดกับจอมมารเหมือนคุยกับคนปกติเลยนะ","นะ...นายไม่กลัวเลยรึไง", 
            "หึ...มนุษย์ที่คิดแบบนี้ไม่ค่อยมีหรอกนะ", 
            "เจ้าดูเข้าใจสถานการณ์ดีนี่", 
            "เจ้าพวกมนุษย์...", "ทําไมถึงกล้าเดินทางมาถึงที่นี่", "ก็เพราะเราอยากหยุดเรื่องทั้งหมดนี่", "เพื่อมนุษย์คนอื่นงั้นหรอ", 
            "ก็ประมาณนั้น", "มนุษย์ส่วนใหญ่เห็นแก่ตัว", "แต่เจ้ากลับเสี่ยงชีวิตเพื่อคนอื่น","เจ้าคนนี้น่าสนใจจริงๆ", 
            "เหมือนจอมมารกําลังชมอยู่นะเนี่ย", "นายอย่าทําตัวสนิทกับจอมมารเกินไปสิ!", 
            "หึ...เจ้าคนนี้ไม่ถ่อมตัวเลยนะ", 
            "ใครจะไปชมเจ้ากันเล่า", 
            "แต่ก็ไม่ปฏิเสธหรอกนะ", "นี่พวกนายคุยกันสนิทเกินไปแล้วนะ!", "ดูเหมือนจะมีคนเริ่มหึงแล้ว", "ขะ...ใครหึงกัน!",
            "หึงงั้นหรอ?","หรือว่าเจ้าจะ..หึๆ..", "อย่าแกล้งอริสมากนักสิ", 
            "ข้าก็แค่พูดเล่นเท่านั้นเอง", 
            "ก็เพราะพวกเจ้าดูน่าสนุกนี่", 
            "มนุษย์...", "ปกติข้าไม่ค่อยให้ใครอยู่ในปราสาทนานนัก", "แต่พวกเจ้าดูต่างออกไป", "ข้าจะให้พวกเจ้าพักที่นี่คืนนี้ก็ได้", 
            "จริงหรอเนี่ย", "พักในปราสาทจอมมาร...","นี่มันประสบการณ์แปลกๆจริงๆ", 
            "ข้าไม่ได้ใจดีหรอก", 
            "เจ้านี่พูดเก่งจริงๆ", 
            "คืนนั้น ฉันออกมาเดินเล่นที่ระเบียงของปราสาท", "ลมเย็นดีแฮะ", "เจ้าก็มาที่นี่เหมือนกันสินะ", "อ้าว เธอก็อยู่ที่นี่หรอ", 
            "ข้ามักจะมามองป่าตอนกลางคืน", "มันเงียบดี", "เธอดูไม่เหมือนจอมมารเลยนะ", "มนุษย์คิดว่าจอมมารต้องเป็นยังไงล่ะ", 
            "เจ้ากล้าพูดกับจอมมารแบบนั้นอีกแล้วนะ", 
            "เจ้าพูดเเบบนี้อีกเเล้วนะ...", 
            "เจ้าคนนี้...", "ไม่กลัวข้าเลยจริงๆสินะ", 
            "หึ…", 
            "งั้นเหรอ...", 
            "มนุษย์อย่างเจ้าแปลกจริงๆ", "แต่ก็ไม่ได้น่ารําคาญ", "ตรงกันข้าม...","ข้ากลับรู้สึกว่าเจ้าค่อนข้างน่าสนใจ",
            "เช้าวันต่อมา...","ได้เวลาออกเดินทางแล้วสินะ","เป้าหมายต่อไปคือ Grey","ใช่",
            "มนุษย์...","ถ้าเจ้าจะไปสู้กับ Grey","ก็อย่าตายซะก่อนล่ะ", 
            "หึ...", 
            "ถ้าเจ้ารอดกลับมาได้จริง",
            "ข้าจะรอดู","มนุษย์...","เจ้าเป็นคนที่น่าสนใจจริงๆ", 
    };

    public part8() {
        setTitle("ISEKAI DEMO - Part 8 (Total Upgrade)");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playBGM("res/sound/soundtrack13.wav", -10.0f);

        // --- ฉากหลัง ---
        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        bgFadeOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, (int)(bgAlpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        bgFadeOverlay.setBounds(0, 0, 1280, 800);
        bgFadeOverlay.setOpaque(false);
        layeredPane.add(bgFadeOverlay, Integer.valueOf(JLayeredPane.DEFAULT_LAYER + 1));

        // --- ตัวละคร ---
        leftCharLabel = createFadeLabel();
        layeredPane.add(leftCharLabel, JLayeredPane.PALETTE_LAYER);
        rightCharLabel = createFadeLabel();
        layeredPane.add(rightCharLabel, JLayeredPane.PALETTE_LAYER);

        // --- แผ่น Fade เริ่มเกม ---
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

    private JLabel createFadeLabel() {
        return new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha));
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
        
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("empty.png")) {
                leftCharLabel.setIcon(null); rightCharLabel.setIcon(null); lastCharPath = path;
            } else if (!path.equals(lastCharPath)) {
                int w = 900, h = 900; 
                if (path.contains("Nebula")) {
                    rightCharLabel.setBounds(500, 50, w, h); rightCharLabel.setIcon(getOptimizedImage(path, w, h));
                } else {
                    int sw = path.contains("dan") ? 1400 : 1200;
                    int sh = path.contains("dan") ? 1000 : 950;
                    leftCharLabel.setBounds(-250, 50, sw, sh); leftCharLabel.setIcon(getOptimizedImage(path, sw, sh));
                }
                startCharacterFadeIn(); lastCharPath = path;
            }
        }
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    // --- ระบบ Animation ---
    private void startCharacterFadeIn() {
        if (charFadeTimer != null) charFadeTimer.stop();
        charAlpha = 0.0f;
        charFadeTimer = new Timer(20, e -> {
            charAlpha += 0.05f;
            if (charAlpha >= 1.0f) { charAlpha = 1.0f; ((Timer)e.getSource()).stop(); }
            leftCharLabel.repaint(); rightCharLabel.repaint();
        });
        charFadeTimer.start();
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
        new Timer(50, e -> {
            alpha -= 0.02f;
            if (alpha <= 0) { alpha = 0; ((Timer)e.getSource()).stop(); layeredPane.remove(fadeOverlay); }
            fadeOverlay.repaint();
        }).start();
    }

    // --- ปุ่ม Choice พร้อมระบบ Hover ---
    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            private double scale = 1.0;
            private int alphaMod = 150; 
            private Timer animTimer;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(getWidth()/2, getHeight()/2);
                g2.scale(scale, scale);
                g2.translate(-getWidth()/2, -getHeight()/2);
                
                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(new Color(225, 105, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }

            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { startAnimation(1.05, 200); }
                    @Override public void mouseExited(MouseEvent e) { startAnimation(1.0, 150); }
                });
            }

            private void startAnimation(double ts, int ta) {
                if (animTimer != null) animTimer.stop();
                animTimer = new Timer(15, e -> {
                    scale += (ts - scale) * 0.2;
                    if (alphaMod < ta) alphaMod += 5; else if (alphaMod > ta) alphaMod -= 5;
                    if (Math.abs(scale - ts) < 0.001 && alphaMod == ta) ((Timer)e.getSource()).stop();
                    repaint();
                });
                animTimer.start();
            }
        };

        btn.setBounds(800, y, 350, 60);
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1); layeredPane.remove(choiceButton2);
            isChoosing = false; 
            
            // --- Sync Relationship ---
            if (target == 32) { 
                relationdata.aliceRel.addAffinity(10);
                if (relationdata.isOnlineMode && networkOut != null) new Thread(() -> networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity())).start();
            } else if (target == 12 || target == 24 || target == 42 || target == 56 || target == 69) {
                relationdata.nebulaRel.addAffinity(10);
                if (relationdata.isOnlineMode && networkOut != null) new Thread(() -> networkOut.println("UPDATE_NEBULA_AFFINITY:" + relationdata.nebulaRel.getAffinity())).start();
            } else {
                relationdata.nebulaRel.addAffinity(5);
                if (relationdata.isOnlineMode && networkOut != null) new Thread(() -> networkOut.println("UPDATE_NEBULA_AFFINITY:" + relationdata.nebulaRel.getAffinity())).start();
            }
            
            currentIndex = target; updateScene();
            
            // อัปเดต UI ทันที
            affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            nebulaAffinityLabel.setText("เนบิวล่า: " + relationdata.nebulaRel.getAffinity());
            nebulaStatusLabel.setText("สถานะ: " + relationdata.nebulaRel.getStatus());
        });
        return btn;
    }

    // --- ส่วนประกอบอื่นๆ (UI, Network, Sounds) ---
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
        
        // Choices Logic
        if (currentIndex == 10) { showChoices("ก็เธอไม่ได้ทําอะไรพวกเรานี่", "ถ้าเธออยากฆ่าพวกเรา เราคงตายไปแล้ว", 11, 12); return; }
        if (currentIndex == 11) { currentIndex = 13; updateScene(); return; }
        if (currentIndex == 22) { showChoices("ฉันก็แค่พูดตามที่คิด", "หรือว่าเธอกําลังชมฉันอยู่?", 23, 24); return; }
        if (currentIndex == 23) { currentIndex = 25; updateScene(); return; }
        if (currentIndex == 31) { showChoices("เดี๋ยวเธอร้องไห้ขึ้นมาจะทํายังไง", "เธอแกล้งคนอื่นสนุกนักรึไง", 32, 33); return; }
        if (currentIndex == 32) { currentIndex = 34; updateScene(); return; }
        if (currentIndex == 40) { showChoices("งั้นคืนนี้ฉันจะรบกวนหน่อยนะ", "เธอใจดีกว่าที่คิดนะ", 41, 42); return; }
        if (currentIndex == 41) { currentIndex = 43; updateScene(); return; }
        if (currentIndex == 50) { showChoices("น่ากลัวกว่านี้มั้ง", "ไม่สวยขนาดนี้แน่ๆ", 51, 52); return; }
        if (currentIndex == 51) { currentIndex = 53; updateScene(); return; }
        if (currentIndex == 54) { showChoices("ก็เธอไม่ได้น่ากลัวนี่", "เพราะฉันเริ่มชินกับเธอแล้วมั้ง", 55, 56); return; }
        if (currentIndex == 55) { currentIndex = 57; updateScene(); return; }
        if (currentIndex == 67) { showChoices("ถ้าฉันรอดกลับมา...ฉันจะมาเจอเธออีก", "ถ้าฉันรอดกลับมา เธอต้องเลี้ยงข้าวฉัน", 68, 69); return; }
        if (currentIndex == 68) { currentIndex = 70; updateScene(); return; }

        if (currentIndex < dialogues.length - 1) { currentIndex++; updateScene(); } 
        else { finishGame(); }
    }

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(4, 1, 0, 0)); 
        relPanel.setBounds(0, 0, 280, 120); 
        relPanel.setBackground(new Color(0, 0, 0, 190)); 
        relPanel.setOpaque(true);
        relPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 105, 180), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 10)
        ));

        // --- แก้ไขจุดนี้: ดึงค่าเริ่มต้นจาก relationdata มาใส่เลยเหมือน Part 7 ---
        affinityLabel = createRelLabel("อริส: " + relationdata.aliceRel.getAffinity(), new Color(255, 192, 203), 18);
        statusLabel = createRelLabel("สถานะ: " + relationdata.aliceRel.getStatus(), Color.WHITE, 14);
        
        nebulaAffinityLabel = createRelLabel("เนบิวล่า: " + relationdata.nebulaRel.getAffinity(), new Color(210, 160, 255), 18);
        nebulaStatusLabel = createRelLabel("สถานะ: " + relationdata.nebulaRel.getStatus(), Color.WHITE, 14);
        // ---------------------------------------------------------------

        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        relPanel.add(nebulaAffinityLabel);
        relPanel.add(nebulaStatusLabel);
        
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
        // กำหนดความกว้างตาราง 360 และ Font Tahoma เหมือน Part 7
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'><table width='360' style='color:white; font-family:Tahoma;'>");
        
        // ส่วนหัวตาราง: สีทอง (#FFD700) และการจัดชิดซ้าย/ขวาที่แน่นอน
        sb.append("<tr style='color:#FFD700;'>")
        .append("<th align='left' width='160'>ผู้เล่น</th>")
        .append("<th align='right' width='90'>อริส</th>")
        .append("<th align='right' width='90'>เนบิวล่า</th>")
        .append("</tr>");

        for (String p : data.split(",")) {
            if (p.contains("=")) {
                String[] parts = p.split("="); 
                String name = parts[0];
                String rawScores = parts[1]; 
                
                String aliceValue = "0";
                String nebulaValue = "0";
                
                if (rawScores.contains("/")) {
                    String[] scoreParts = rawScores.split("/");
                    aliceValue = scoreParts[0];
                    nebulaValue = scoreParts[1];
                } else {
                    aliceValue = rawScores;
                }

                // สีเขียว (#00FF7F) สำหรับชื่อผู้เล่นปัจจุบัน
                String color = name.equals(relationdata.playerName) ? "#00FF7F" : "white";
                
                // การจัดระดับสี: อริส (#FFC0CB - ชมพู), เนบิวล่า (#DA70D6 - ม่วง)
                sb.append("<tr>")
                .append("<td style='color:").append(color).append(";'>").append(name).append("</td>")
                .append("<td align='right' style='color:#FFC0CB;'>").append(aliceValue).append("</td>")
                .append("<td align='right' style='color:#DA70D6;'>").append(nebulaValue).append("</td>")
                .append("</tr>");
            }
        }
        sb.append("</table></body></html>");
        
        SwingUtilities.invokeLater(() -> {
            affinityStatusLabel.setText(sb.toString());
            onlineCountLabel.setText("ผู้เล่นออนไลน์: " + data.split(",").length);
        });
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox(); dialoguePanel.setLayout(null); dialoguePanel.setBounds(225, 520, 800, 200);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);
        nameLabel = new JLabel(); nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26)); nameLabel.setForeground(new Color(180, 40, 90)); nameLabel.setBounds(60, 10, 300, 40); dialoguePanel.add(nameLabel);
        dialogueArea = new JLabel(); dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22)); dialogueArea.setForeground(new Color(45, 65, 115));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP); dialogueArea.setBounds(60, 60, 700, 110); dialoguePanel.add(dialogueArea);
    }

    private void showChoices(String t1, String t2, int target1, int target2) {
        isChoosing = true; choiceButton1 = createChoiceButton(t1, 380, target1); choiceButton2 = createChoiceButton(t2, 450, target2);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER); layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER); repaint();
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

    public void playBGM(String p, float v) {
        try { if (bgmClip != null && bgmClip.isRunning()) return; AudioInputStream ai = AudioSystem.getAudioInputStream(new File(p));
        bgmClip = AudioSystem.getClip(); bgmClip.open(ai); ((FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(v);
        bgmClip.loop(Clip.LOOP_CONTINUOUSLY); bgmClip.start(); } catch (Exception e) {}
    }

    public void playEffect(String p, float v) {
        try { AudioInputStream ai = AudioSystem.getAudioInputStream(new File(p)); Clip c = AudioSystem.getClip(); c.open(ai); ((FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN)).setValue(v); c.start(); } catch (Exception e) {}
    }

    private ImageIcon getOptimizedImage(String p, int w, int h) {
        String k = p + w + h; if (!imageCache.containsKey(k)) { try { Image i = new ImageIcon(p).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH); imageCache.put(k, new ImageIcon(i)); } catch (Exception e) { return null; } } return imageCache.get(k);
    }

    private void stopAllSounds() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
        if (effectClip != null && effectClip.isRunning()) {
            effectClip.stop();
            effectClip.close();
        }
    }

    private void startFadeOut() {
        alpha = 0.0f; 
        Timer fadeOutTimer = new Timer(30, e -> {
            alpha += 0.02f; 
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                ((Timer)e.getSource()).stop();
                
                stopAllSounds(); // หยุดเสียงทั้งหมดก่อนไปต่อ
                
                SwingUtilities.invokeLater(() -> {
                    // --- แก้ไขตรงนี้: เอา // ออก ---
                    new part9().setVisible(true); 
                    
                    dispose(); // ปิดหน้า Part 8 ทิ้ง
                });
            }
            fadeOverlay.repaint();
        });
        fadeOutTimer.start();
    }

    private void finishGame() {
        if (isFinishing) return; // ป้องกันการเรียกซ้ำถ้าฟังก์ชันกำลังทำงานอยู่
        isFinishing = true;      // ล็อคสถานะทัน
        // 1. สร้างหรือนำ fadeOverlay กลับมาใช้อีกครั้ง
        if (fadeOverlay == null) {
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
        }
        
        // ตรวจสอบว่าถูกเพิ่มเข้า layeredPane หรือยัง (ใส่ไว้ชั้นบนสุด)
        if (fadeOverlay.getParent() == null) {
            layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        }

        startFadeOut();
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