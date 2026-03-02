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
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    private JLabel nebulaAffinityLabel, nebulaStatusLabel; // เพิ่มของ Nebula
    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    private java.io.PrintWriter networkOut;

    private JPanel bgFadeOverlay;
    
    private float charAlpha = 0.0f; // สำหรับเก็บค่าความโปร่งใสของตัวละคร
    private Timer charFadeTimer;    // Timer สำหรับจัดการ Animation
    private String lastCharPath = ""; // ใช้เช็คว่าตัวละครเปลี่ยนหรือไม่ เพื่อไม่ให้เล่น Fade ซ้ำ

    private float bgAlpha = 0.0f; // 0.0 = ใส, 1.0 = ดำสนิท
    private Timer bgFadeTimer;
    private String lastBgPath = ""; // ใช้เช็คการเปลี่ยนฉากหลัง

    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    // --- ข้อมูล Array (คงเดิม) ---
    private String[] imagePaths = {
       "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", 
       "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", "res/scene7/s1.png", 
       "res/scene7/s1.png", "res/scene7/s2.png", "res/scene7/s2.png", "res/scene7/s2.png", 
       "res/scene7/s2.png", "res/scene7/s2.png", "res/scene7/s3.png", "res/scene7/s3.png", 
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
       "res/scene7/s3.png", "res/scene7/s3.png"
    };
    
    private String[] charPaths = { 
       "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", 
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/scene5/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
       "res/scene5/Alice-normal2.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/scene5/Alice-normal2.png", //0-12
       "res/scene5/Alice-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
       "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", //13-24
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", //29
       "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", //25-35
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/empty.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", //36-51
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/scene5/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", //52-63
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png","res/Charactor/Nebula/Nebula-normal1.png","res/Charactor/Nebula/Nebula-normal2.png","res/scene5/Alice-normal2.png","res/Charactor/Dan/dan-normal2.png", //64-69
    };

    private String[] names = { 
        " ", " ", "Dan", "Dan", "Dan", "ฉัน", "อริส", "อริส",
        "อริส", " ", " ", "Dan", "ฉัน", "อริส", "???", "???", //0-15
        "ฉัน", "???", "Nebula", "อริส", "ฉัน", "ฉัน", "Nebula",
        "Nebula", "ฉัน", "ฉัน", "Nebula", "Nebula", "ฉัน",
        "อริส","Nebula", "Nebula", "Nebula", "Nebula", "ฉัน", //16-34
        "Nebula", "อริส", "Nebula", "Nebula", "Nebula", "Dan",
        "Nebula", "Nebula", "ฉัน", "Nebula", "Nebula", "Nebula", //35-46
        "Nebula", "Nebula", "Nebula", "Nebula", "Nebula", "Dan",
        "อริส", "ฉัน", "Nebula", "Nebula", "Nebula", "Nebula","Nebula", //47-59
        "Nebula", "Dan", "Nebula", "Nebula", "ฉัน", "Nebula", "Nebula", "Nebula",
        "อริส", "Dan", //60-69
    };
    private String[] dialogues = {
        "หลังจากเดินทางมาหลายสัปดาห์…","ในที่สุดพวกเราก็มาถึงป่า Death End", 
        "ที่นี่แหละ…ป่า Death End", "จากนี้ไปต้องระวังตัวให้ดี", "ปีศาจในป่านี้แข็งแกร่งกว่าที่พวกเธอเคยเจอมา", 
        "เข้าใจแล้ว", "ป่านี้มันน่ากลัวกว่าที่คิดอีกนะ...", "อะ...อือ...", "อือ เข้าใจแล้ว", 
        "หลังจากเดินลึกเข้าไปในป่า...","พวกเราก็พบกับปราสาทขนาดใหญ่", "นั่นไง…ปราสาทของจอมมาร", //0-11
        "ในที่สุดก็มาถึงสักที", "บรรยากาศมันน่ากลัวจัง…", "มนุษย์งั้นหรอ...", "กล้ามาถึงที่นี่ได้ก็นับว่ากล้าดีนะ", 
        "ใครกัน!?", "ข้าคือจอมมาร...", "ชื่อของข้าคือ Nebula", "จอมมาร…!!", //12-19
        "ในที่สุดก็เจอตัวแล้ว", "เป็นแกสินะ ที่สั่งให้ปีศาจโจมตีหมู่บ้าน", "หืม?", "เจ้ากําลังพูดเรื่องอะไร?", 
        "อย่ามาแกล้งทําเป็นไม่รู้!", "พวกเราจะหยุดแกที่นี่!", "ฮ่าๆๆ", "มนุษย์นี่น่าสนใจจริงๆ", 
        "ถ้าอยากลองก็เข้ามา", "รับนี่ไป!","Ice Lance!", "น่าสนุกดีนี่", "แต่พลังแค่นี้...", //20-32
        "ยังห่างไกลนะ", "พอแค่นี้ก่อนดีกว่า", "อะไรนะ?", "ข้าไม่ได้เป็นคนสั่งปีศาจพวกนั้น", 
        "อะไรนะ!?", "คนที่ทําเรื่องพวกนั้นคือ...", "จอมมารอีกคนหนึ่ง", "ชื่อของมันคือ Grey", 
        "จอมมารอีกคนงั้นหรอ...", "มนุษย์...เจ้าค่อนข้างแข็งแกร่งกว่าที่คิดนะ", "ปกติแล้วมนุษย์ที่มาถึงที่นี่ มักจะหนีหรือไม่ก็ตายไปแล้ว", //33-43
        "ก็แค่ทําในสิ่งที่ต้องทํา", "หืม...น่าสนใจดีนี่", "เจ้ากล้าต่อสู้กับจอมมารโดยไม่ลังเลเลยงั้นหรอ?", 
        "หึ...มนุษย์ที่พูดแบบนี้กับข้าเป็นคนแรกเลยนะ", "เจ้านี่แปลกดีจริงๆ", "ปกติมนุษย์จะกลัวข้า...", "แต่เจ้ากลับยืนคุยกับข้าเฉยๆ", 
        "เจ้ากล้าพูดกับจอมมารแบบนั้นเลยหรอ", "มะ…มนุษย์นี่พูดอะไรของเจ้า…", //44-52 
        "นี่พวกนายกําลังจีบจอมมารกันอยู่รึไงเนี่ย...", "นะ…นายไปพูดอะไรกับจอมมารแบบนั้นกัน!!", 
        "ถ้าอย่างนั้น...จอมมารที่อยู่เบื้องหลังเรื่องพวกนี้ก็คือ Grey งั้นสินะ", "ใช่", "เขาเคยเป็นหนึ่งในจอมมารที่อยู่ภายใต้การปกครองของข้า",
        "แต่แนวคิดของเขาแตกต่างจากข้า", "ข้าเชื่อว่ามนุษย์กับปีศาจสามารถอยู่ร่วมกันได้", "แต่ Grey เชื่อว่ามนุษย์ควรถูกกําจัดให้หมด", //53-60
        "งั้นเขาก็แยกตัวออกไปสินะ...", "ใช่", "และตอนนี้เขากําลังสร้างกองทัพปีศาจของตัวเอง", "ถ้าอย่างนั้น...เขาอยู่ที่ไหน", 
        "Grey ซ่อนตัวอยู่ที่...","หุบเขาเงามืด ทางตะวันตกของป่า Death End","ที่นั่นมีป้อมปราการของเขาอยู่","งั้นเราก็มีจุดหมายต่อไปแล้วสินะ",
        "แต่ที่นั่นอันตรายกว่าที่นี่อีก", //61-69

    };

    public part7() {
        setTitle("ISEKAI DEMO - Part 7");
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
        // ใส่ไว้ในเลเยอร์ที่อยู่เหนือ Background เล็กน้อย
        layeredPane.add(bgFadeOverlay, Integer.valueOf(JLayeredPane.DEFAULT_LAYER + 1));
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // เพิ่ม RenderingHints เพื่อความสมูท
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

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
            @Override
            public void mouseClicked(MouseEvent e) {
                    handleNext();
                
            }
        });
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        if (currentIndex < imagePaths.length) {
        String newBgPath = imagePaths[currentIndex];
            if (!newBgPath.equals(lastBgPath)) {
                startBackgroundTransition(newBgPath);
                lastBgPath = newBgPath;
            }
        }
        
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("empty.png")) {
                characterLabel.setIcon(null);
                lastCharPath = path;
            } else if (!path.equals(lastCharPath)) { // ถ้าเป็นรูปใหม่ ให้เริ่ม Fade
                int charW, charH, charX, charY;

                if (path.contains("Nebula")) {
                    charW = 900; charH = 900;
                    charX = (1280 - charW) / 2; charY = 50;
                } else if (path.contains("dan")) {
                    charW = 1400; charH = 1000;
                    charX = (1280 - charW) / 2; charY = 60; 
                } else {
                    charW = 1200; charH = 1000;
                    charX = (1280 - charW) / 2; charY = 50;
                }

                characterLabel.setBounds(charX, charY, charW, charH);
                characterLabel.setIcon(getOptimizedImage(path, charW, charH));
                
                // เริ่มการ Fade In ตัวละคร
                startCharacterFadeIn();
                lastCharPath = path;
            }
        }
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    private void handleNext() {
            if (isChoosing) return;
            if (isTyping) {
                stopTypewriter();
                dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
                return;
            }

            // --- Choice Logic ---
            // Choice 1: 
            if (currentIndex == 6) {
                showChoices("ไม่ต้องกลัวหรอก ฉันอยู่ข้างๆเธอ", "ถ้าระวังตัวดีๆก็น่าจะไม่เป็นไร", 7, 8);
                return;
            }
            if (currentIndex == 7) { currentIndex = 9; updateScene(); return; }

            // Choice 2: 
            if (currentIndex == 46) {
                showChoices("ต่อให้เธอเป็นจอมมาร ถ้าทําร้ายผู้บริสุทธิ์ฉันก็จะสู้", "ก็แค่คิดว่าเธอคงไม่ใช่คนเลวจริงๆ", 47, 48);
                return;
            }
            if (currentIndex == 47) { currentIndex = 49; updateScene(); return; }

            // Choice 3: 
            if (currentIndex == 50) {
                showChoices("เพราะเธอไม่ได้ดูน่ากลัวขนาดนั้น", "ถ้าจอมมารสวยขนาดนี้ ใครจะกลัวลง", 51, 52);
                return;
            }
            if (currentIndex == 51) { currentIndex = 53; updateScene(); return; }

            if (currentIndex < dialogues.length - 1) {
                currentIndex++;
                updateScene();
            } else {
                finishGame();
            }
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
        if (index == 7){
            playEffect("res/sound/emmm.wav", 5.0f);
        }
        if (index == 8){
            playEffect("res/sound/wakarunai.wav", 5.0f);
        }
        if (index == 14){
            playEffect("res/sound_nebula/ningennoka.wav", 0.0f);
        }
        if (index == 22){
            playEffect("res/sound_nebula/naanii.wav", 0.0f);
        }
        if (index == 26){
            playEffect("res/sound_nebula/hahaha.wav", 0.0f);
        }
        if (index == 47){
            playEffect("res/sound_nebula/hahaha.wav", 0.0f);
        }
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                java.net.Socket socket = new java.net.Socket(relationdata.serverIP, 5000);
                networkOut = new java.io.PrintWriter(socket.getOutputStream(), true);
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));

                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:7"); // แก้ไขเป็น Part 7

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("LOAD_AFFINITY:")) {
                        int score = Integer.parseInt(line.substring(14));
                        relationdata.aliceRel.setAffinity(score);
                        SwingUtilities.invokeLater(() -> {
                            affinityLabel.setText("อริส: " + score);
                            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus()); // ตรวจสอบให้เป็นรูปแบบนี้
                        });
                    } 
                    else if (line.startsWith("LOAD_NEBULA:")) {
                        int nScore = Integer.parseInt(line.substring(12));
                        relationdata.nebulaRel.setAffinity(nScore);
                        SwingUtilities.invokeLater(() -> {
                            nebulaAffinityLabel.setText("เนบิวล่า: " + nScore);
                            nebulaStatusLabel.setText("สถานะ: " + relationdata.nebulaRel.getStatus()); // ตรวจสอบให้เป็นรูปแบบนี้
                        });
                    }
                    else if (line.startsWith("ALL_STATS:")) {
                        updateLeaderboardUI(line.substring(10));
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 380, t1); //y: ขึ้น=ลง
        choiceButton2 = createChoiceButton(text2, 450, t2); //y: ขึ้น=ลง
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        choiceButton1.setVisible(true);
        choiceButton2.setVisible(true);
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
        layeredPane.getActionMap().put("toggleTab", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                statusOverlay.setVisible(!statusOverlay.isVisible()); 
            }
        });
    }

    private void updateLeaderboardUI(String data) {
        // กำหนดความกว้างตาราง 360
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'><table width='360' style='color:white; font-family:Tahoma;'>");
        
        // ส่วนหัวตาราง: ปรับ อริส ให้ align='right' และกำหนด width เพื่อบีบคอลัมน์ให้ชิดกัน
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

                String color = name.equals(relationdata.playerName) ? "#00FF7F" : "white";
                
                // ปรับส่วนข้อมูลของ อริส ให้เป็น align='right' เพื่อให้ตัวเลขอยู่ใกล้กับเนบิวล่า
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

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            // --- ตัวแปรสำหรับระบบ Animation ---
            private double scale = 1.0;
            private int alphaMod = 150; // ความโปร่งใสเริ่มต้นตามโค้ดพาร์ท 6 ของคุณ
            private Timer animTimer;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // --- คำนวณ Scale Animation ขยายจากจุดศูนย์กลาง ---
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                g2.translate(centerX, centerY);
                g2.scale(scale, scale);
                g2.translate(-centerX, -centerY);

                // วาดพื้นหลังโค้งมน (จะชัดขึ้นเมื่อเมาส์ชี้)
                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // วาดเส้นขอบสีชมพูเข้ม (สีเดิมที่คุณกำหนดไว้)
                g2.setColor(new Color(225, 105, 180)); 
                g2.setStroke(new BasicStroke(2));   
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);

                g2.dispose();
                super.paintComponent(g); // วาดข้อความทับลงไป
            }

            {
                // เพิ่ม Mouse Event สำหรับจัดการ Animation
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        startAnimation(1.05, 200); // เมื่อชี้: ขยาย 5% และพื้นหลังชัดขึ้น
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        startAnimation(1.0, 150); // เมื่อเอาออก: กลับสู่ขนาดปกติ
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        scale = 0.95; // เมื่อกด: ปุ่มยุบตัวลงเล็กน้อยเพื่อให้ดูมีแรงกด
                        repaint();
                    }
                });
            }

            private void startAnimation(double targetScale, int targetAlpha) {
                if (animTimer != null && animTimer.isRunning()) animTimer.stop();
                animTimer = new Timer(15, ev -> {
                    // ค่อยๆ ปรับขนาดปุ่มให้นุ่มนวล
                    if (scale < targetScale) scale += 0.01;
                    else if (scale > targetScale) scale -= 0.01;

                    // ค่อยๆ ปรับความชัดของพื้นหลัง
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

        // --- ตั้งค่าดีไซน์ปุ่ม (ตามพาร์ท 6 เดิมของคุณ) ---
        btn.setBounds(800, y, 350, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setForeground(new Color(45, 65, 115)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // เปลี่ยนเมาส์เป็นรูปมือ

        // ปิดการวาดส่วนเกินของ Swing ปกติ
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        
        // --- Logic การทำงาน (พาร์ท 6 ยังไม่มี Affinity) ---
        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false; 

            if (target == 7) { 
                relationdata.aliceRel.addAffinity(10);
                // ส่งของอริสไป Server
                if (relationdata.isOnlineMode && networkOut != null) {
                    new Thread(() -> { networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity()); }).start();
                }
            } else if (target == 48 || target == 52 || target == 47 || target == 51) {
                if (target == 48 || target == 52) relationdata.nebulaRel.addAffinity(15);
                else relationdata.nebulaRel.addAffinity(5);
                
                // ส่งของ Nebula ไป Server
                if (relationdata.isOnlineMode && networkOut != null) {
                    new Thread(() -> { networkOut.println("UPDATE_NEBULA_AFFINITY:" + relationdata.nebulaRel.getAffinity()); }).start();
                }
            }

            // ซิงค์ตำแหน่งฉากเสมอ
            if (relationdata.isOnlineMode && networkOut != null) {
                new Thread(() -> { networkOut.println("SYNC_INDEX:" + target); }).start();
            }

            // อัปเดต UI ทั้งหมดให้เป็นค่าปัจจุบัน
            affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus()); // แก้ตรงนี้
            nebulaAffinityLabel.setText("เนบิวล่า: " + relationdata.nebulaRel.getAffinity());
            nebulaStatusLabel.setText("สถานะ: " + relationdata.nebulaRel.getStatus()); // แก้ตรงนี้

            currentIndex = target; 
            updateScene();
        });

        return btn;
    }

    private void startCharacterFadeIn() {
        if (charFadeTimer != null && charFadeTimer.isRunning()) charFadeTimer.stop();
        
        charAlpha = 0.0f; 
        charFadeTimer = new Timer(20, e -> { // วิ่งที่ ~60fps
            charAlpha += 0.04f; // เพิ่มทีละน้อยๆ ให้ดูเนียนตา
            if (charAlpha >= 1.0f) {
                charAlpha = 1.0f;
                ((Timer)e.getSource()).stop();
            }
            characterLabel.repaint(); // บังคับวาดใหม่เฉพาะส่วนตัวละคร
        });
        charFadeTimer.start();
    }

    public void playBGM(String path, float volume) {
        try {
            // ถ้าเพลงเดิมเล่นอยู่และเป็นเพลงเดิม ไม่ต้องเริ่มใหม่
            if (bgmClip != null && bgmClip.isRunning()) {
                return; 
            }
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioIn);
                FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY); // เล่นวนลูป
                bgmClip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void playEffect(String path, float volume) {
        try {
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                // สร้าง Clip ใหม่ทุกครั้งที่เล่น Effect เพื่อให้เสียงซ้อนกันได้ (ถ้าต้องการ)
                // หรือจะใช้ effectClip ตัวเดียวถ้าต้องการให้เสียงเก่าหยุดก่อน
                Clip clip = AudioSystem.getClip(); 
                clip.open(audioIn);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                clip.start();
                
                // เก็บอ้างอิงไว้เผื่อสั่งหยุด manual
                this.effectClip = clip; 
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void startBackgroundTransition(String newPath) {
    if (bgFadeTimer != null && bgFadeTimer.isRunning()) bgFadeTimer.stop();

    // ขั้นตอนที่ 1: ค่อยๆ มืดลง (Fade to Black)
    bgFadeTimer = new Timer(20, null); // สร้าง Timer เปล่าก่อน
    bgFadeTimer.addActionListener(e -> {
        bgAlpha += 0.05f; // ความเร็วในการมืดลง
        if (bgAlpha >= 1.0f) {
            bgAlpha = 1.0f;
            bgFadeTimer.stop();
            
            // เปลี่ยนรูปภาพพื้นหลังเมื่อหน้าจอมืดสนิท
            backgroundLabel.setIcon(getOptimizedImage(newPath, 1280, 800));
            
            // ขั้นตอนที่ 2: ค่อยๆ สว่างขึ้น (Fade In)
            Timer fadeIn = new Timer(25, ev -> {
                bgAlpha -= 0.05f; // ปรับให้สว่างขึ้นช้าๆ (ยิ่งค่าน้อยยิ่งช้า)
                if (bgAlpha <= 0.0f) {
                    bgAlpha = 0.0f;
                    ((Timer)ev.getSource()).stop();
                }
                bgFadeOverlay.repaint();
            });
            fadeIn.start();
        }
        bgFadeOverlay.repaint();
    });
    bgFadeTimer.start();
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
        alpha = 1.0f; // เริ่มต้นที่หน้าจอดำสนิท
        Timer fadeTimer = new Timer(50, e -> {
            alpha -= 0.01f; // ค่อยๆ ลดความมืด (ปรับให้ช้าลงตามที่ต้องการ)
            if (alpha <= 0) {
                alpha = 0; 
                ((Timer)e.getSource()).stop();
                layeredPane.remove(fadeOverlay); // ลบแผ่นดำออกเพื่อให้คลิกหน้าจอได้
                updateScene(); 
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void setupRelationshipUI() {
        // 1. ปรับตำแหน่งติดซ้ายบน (0, 0) และตั้งขนาดให้กระชับ
        JPanel relPanel = new JPanel(new GridLayout(4, 1, 0, 0)); 
        relPanel.setBounds(0, 0, 280, 120); 
        
        // 2. ใช้พื้นหลังสีดำโปร่งแสงเพื่อให้สีชื่อตัวละครเด่นขึ้นมา
        relPanel.setBackground(new Color(0, 0, 0, 190)); 
        relPanel.setOpaque(true);

        // 3. เพิ่มกรอบสีชมพู (Pink) หนา 2 พิกเซล และใส่ Padding ด้านใน
        relPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 105, 180), 2), // กรอบสีชมพู
            BorderFactory.createEmptyBorder(5, 15, 5, 10) // ระยะห่างจากขอบ
        ));

        // 4. เปลี่ยนสีชื่อตัวละครให้สว่างและอ่านง่ายขึ้นบนพื้นหลังดำ
        
        // --- ส่วนของ อริส (ใช้สีชมพูสว่าง) ---
        affinityLabel = new JLabel("อริส: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); 
        affinityLabel.setForeground(new Color(255, 192, 203)); // Pink

        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE); // สถานะใช้สีขาวพื้นฐาน

        // --- ส่วนของ เนบิวล่า (ใช้สีม่วงสว่าง/ลาเวนเดอร์) ---
        nebulaAffinityLabel = new JLabel("เนบิวล่า: " + relationdata.nebulaRel.getAffinity());
        nebulaAffinityLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        nebulaAffinityLabel.setForeground(new Color(210, 160, 255)); // Light Purple

        nebulaStatusLabel = new JLabel("สถานะ: " + relationdata.nebulaRel.getStatus());
        nebulaStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        nebulaStatusLabel.setForeground(Color.WHITE);

        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        relPanel.add(nebulaAffinityLabel);
        relPanel.add(nebulaStatusLabel);
        
        // แสดงผลในเลเยอร์หน้าสุด
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }

    private void startTypewriter(String text) {
        stopTypewriter();
        isTyping = true;
        charIndex = 0;
        dialogueArea.setText("");
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 950px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else { stopTypewriter(); }
        });
        typewriterTimer.start();
    }

    private void stopTypewriter() {
        if (typewriterTimer != null) typewriterTimer.stop();
        isTyping = false;
    }

    private void finishGame() {
        UIManager.put("OptionPane.messageFont", THAI_FONT);
        JOptionPane.showMessageDialog(null, "End Part 6!");
        System.exit(0);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part7().setVisible(true));
    }
}

// --- VisualNovelBox Class (คงเดิม) ---
/*class VisualNovelBox extends JPanel {
    private int cornerRadius = 30;
    public VisualNovelBox() { setOpaque(false); }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 250, 255, 180), 0, getHeight(), new Color(255, 235, 245, 230));
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2d.setColor(new Color(255, 150, 200, 200));
        g2d.setStroke(new BasicStroke(4f));
        g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, cornerRadius, cornerRadius);
    }
}*/