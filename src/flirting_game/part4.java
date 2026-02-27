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
import javax.sound.sampled.*;
import javax.swing.*;

public class part4 extends JFrame {
    // --- UI Components ---
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, characterLabel2, dialogueArea, nameLabel;
    private float alpha = 1.0f;
    private JPanel fadeOverlay;
    private VisualNovelBox dialoguePanel; 
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
    
    // UI สำหรับระบบ Leaderboard (Tab)
    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;

    private float charAlpha = 0.0f; // ค่าความโปร่งใส 0.0 - 1.0
    private Timer charFadeTimer;    // ตัวจับเวลาสำหรับ Fade ตัวละคร

    // --- Networking ---
    private PrintWriter networkOut;
    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 28);

    // --- ข้อมูล Array (คงเดิม) ---
    private String[] imagePaths = {
        "res/scene4/s1.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
        "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
        "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png", "res/scene4/s2.png",
        "res/scene4/s2.png", "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", 
        "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png","res/scene4/s3.png",
        "res/scene4/s4.1.png", "res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s4.png",
        "res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s4.png", "res/scene4/s5.png", 
        "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", 
        "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png", "res/scene4/s5.png",
        "res/scene4/s5.png", "res/scene4/s6.png", "res/scene4/s6.png","res/scene4/s6.png", "res/scene4/s6.png",
        "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png", "res/scene4/s6.png",
        "res/scene4/s6.png", "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png", 
        "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s7.png",
        "res/scene4/s7.png", "res/scene4/s7.png", "res/scene4/s3.png", "res/scene4/s3.png",
        "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png",
        "res/scene4/s3.png", "res/scene4/s3.png", "res/scene4/s3.png"
    };
    
    private String[] charPaths = { /* Mc Layer */
        "res/empty.png", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", 
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body2.PNG", "res/Charactor/Mc/body1.PNG", 
        "res/Charactor/Mc/body2.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG",
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG",
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG",
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", 
        "res/Charactor/Mc/body1.PNG", "res/Charactor/Mc/body1.PNG", "res/empty.png", 
        "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", 
        "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", "res/Charactor/factor/Uncle.png", 
        "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG",
        "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG",
        "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG", "res/Charactor/factor/demon1.PNG",
        "res/Charactor/factor/demon1.PNG", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/factor/demon2.PNG", 
        "res/Charactor/factor/demon2.png", "res/Charactor/factor/demon2.png", "res/Charactor/factor/demon2.png", 
        "res/Charactor/factor/demon2.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png"
    };

    private String[] charPaths2 = { /* Alice Layer */
        "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
        "res/Charactor/Alice/Girl/Alice-shy2.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-shy2.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
        "res/Charactor/Alice/Girl/Alice-shy2.png", "res/empty.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
        "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png",
        "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png",
        "res/Charactor/Alice/Girl/Alice-fight1.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight1.png", 
        "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight1.png", 
        "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
        "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png",
        "res/Charactor/Alice/Girl/Alice-fight1.png","res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/Charactor/Alice/Girl/Alice-normal2.png","res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/Charactor/Alice/Girl/Alice-shy1.png","res/Charactor/Alice/Girl/Alice-normal2.png",
        "res/Charactor/Alice/Girl/Alice-normal1.png",
    };

    private String[] names = { /* รายชื่อตามฉาก */
        " ", "ฉัน", "ฉัน", "อริส", "ฉัน", "อริส", "ฉัน", "อริส", 
        "อริส", "อริส", "อริส", "อริส", "อริส", "อริส", "ฉัน", "อริส", 
        " ", "อริส", "อริส", "อริส", "ลุง", "อริส", "ลุง", "ลุง", 
        "อริส", "อริส", "ฉัน", "ปีศาจ", "ปีศาจ", "ฉัน", "ปีศาจ", "ฉัน", 
        "ปีศาจ", "ปีศาจ", "อริส","อริส", "ปีศาจ", "อริส", " ", "อริส", " ", 
        "อริส", "ฉัน", "ปีศาจ", "ปีศาจ", "ปีศาจ", "ฉัน", "ฉัน","ปีศาจ", 
        "ปีศาจ", "ปีศาจ", "อริส", "ฉัน", "อริส", "อริส", "ฉัน", "อริส", 
        "อริส", "อริส", "ฉัน"
    };
    
    private String[] dialogues = { /* บทสนทนาตามฉาก */
        "เวลาผ่าน2ปี", "นี่ก็ผ่านไป2ปีเเล้ว หลังจากที่ฉันได้มาอยู่ในโลกนี้", 
        "ตอนนี้ฉันก็น่าจะเเข็งเเกร่งขึ้นบ้างละหละ", "...(ชื่อตัวละครเรา) ข้าวเที่ยงเสร็จละนะ", 
        "โอเค กําลังจะไปเดี๋ยวนี้แหละ", "..กําลังยืนดูตัวเรา..", "นี่เธอเเอบดูกล้ามฉันรึปล่าว?", 
        "ปล่าวซะหน่อย ใครมันจะไปดูกัน", "ช่างเรื่องนั้นเถอะ", "นี่..นายคิดว่าชุดนี้เหมาะกับฉันมั้ย?", 
        "มันเป็นชุดสําหรับเดินทางหนะ", "น่ารักอะไรกัน..บ้าจริง", "โถ่ว..นี่นายจะไม่ชมฉันเลยบ้างรึไง", 
        "เมื่อกี้นายกําลังฝึกหรอ?", "อือ..ก็นิดหน่อยอะ", "รีบกินสิเดี๋ยวมันจะเย็นเอานะ", 
        "(กําลังกิน)", "เป็นไงอร่อยมั้ย?", "จะ..จริงหรอ..งั้นก็กินเยอะๆเลยนะ", 
        "อือๆก็ดีเเล้ว", "นี่!!เปิดประตูหน่อย!!", "เกิดอะไรขึ้นหรอคะ?", 
        "เอ่อ..คือว่า..มันมีปีศาจมาบุกโจมตีหมู่บ้าน", "มีชาวบ้านหลายคนที่ได้รับบาดเจ็บ เเต่ส่วนใหญ่ก็หนีออกมาได้", 
        "เเย่ละสิ! ต้องรีบไปจัดการเเล้ว!", "ไปกันเถอะ..(ชื่อตัวละครเรา)", "โอเค!!", 
        "ไม่มีพวกเก่งๆเลยรึไง ฮ่าๆ", "มีเเต่ชาวบ้านกระจอกๆเเบบนี้ ก็ไม่สนุกนะเส้", 
        "นี่เเกกําลังทําอะไร!!", "ก็กําลังเล่นสนุกอยู่ไงหละ ฮ่าๆ", "เล่นสนุกอย่างงั้นหรอ?", 
        "พวกเเกมันก็ไม่ต่างอะไรจากหนอนเเมลง!!", "ชีวิตของพวกเเกก็มีไว้ให้พวกข้าสนุกเท่านั้น", 
        "เลวที่สุด..", "ฉันจะไม่ให้อภัยพวกเเกเด็ดขาด!! ", "เเน่จริงก็เข้ามา!!", 
        "เวทย์นํ้าเเข็ง Ice shot!!","ปีศาจหลบได้ เเละกําลังจะโจมตี อริส", "ขอบคุณที่ช่วยนะ..(ชื่อตัวละครเรา)", "อริสหลบการโจมตีได้", 
        "เวทย์นํ้าเเข็ง Ice floor", "รับไปซะ! เวทย์ลม wind storm", "เอ่อ..พลังเวทย์ขนาดนี้..มันเป็นใครกันนะ!?", 
        "อ้ากกกก!!", "ข้าเเพ้หรอเนี่ย", "ดูเหมือนเเกจะประเมินตัวเองไว้สูงเลยสินะ", 
        "เอาหละ..ใครเป็นคนส่งเเกมา", "เเกรู้ไปจะได้อะไรขึ้นมา", "อย่างพวกเเก ไม่มีทางชนะท่านผู้นั้นได้หรอก", 
        "ท่านจอมมารผู้นั้นหนะ..", "จอมมารหรอ?", "อริส เธอรู้เรื่องจอมมารคนนั้นบ้างรึปล่าว?", 
        "ฉันเคยได้ยินว่ามีจอมมารคนนึงที่อยู่ลึกสุดของป่า death end", "เเต่จอมมารคนนั้นดูเหมือนจะเป็น คนที่รักความสงบสุขมาก", 
        "ฟังดูเเล้วไม่มีเหตุที่จอมมารคนนั้นจะทําเรื่องเเบบนี้เลย", "อริส ฉันว่ามันถึงเวลาที่เราต้องออกเดินทางเเล้วหละ", 
        "เเล้วเราจะไปที่ไหนกันหรอ?", "ไปเดทอะไรบ้ารึปล่าว อร๊ายยยย", "ขอบคุณนะ", 
        "ไม่งั้นอาจจะมีผู้คนต้องตายไปมากกว่านี้"
    };

    public part4() {
        setTitle("ISEKAI DEMO - Part 4");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusTraversalKeysEnabled(false); // ปลดล็อกปุ่ม Tab

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playSE("res/sound/soundtrack5.wav", true, -10.0f);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
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
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        // เปลี่ยนการสร้าง characterLabel2 (อริส) ให้มี PaintComponent พิเศษ
        characterLabel2 = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        layeredPane.add(characterLabel2, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        setupRelationshipUI();
        setupStatusOverlay(); 
        setupTabKeyBinding(); 
        setupFadeOverlay();
        initNetwork();

        // ปิด Scoreboard เมื่อพับจอ
        this.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override public void windowGainedFocus(java.awt.event.WindowEvent e) {}
            @Override public void windowLostFocus(java.awt.event.WindowEvent e) {
                if (statusOverlay != null && statusOverlay.isVisible()) {
                    statusOverlay.setVisible(false);
                }
            }
        });

        startFadeIn();
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { handleNext(); }
        });
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                Socket socket = new Socket(relationdata.serverIP, 5000);
                networkOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:4"); // ระบุพาร์ท 4 ไปที่ Server

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("LOAD_AFFINITY:")) {
                        int score = Integer.parseInt(line.substring(14));
                        relationdata.aliceRel.setAffinity(score);
                        SwingUtilities.invokeLater(this::updateAffinityUI);
                    } else if (line.startsWith("SYNC_INDEX:")) {
                        int remoteIdx = Integer.parseInt(line.substring(11));
                        if (remoteIdx != currentIndex) {
                            currentIndex = remoteIdx;
                            SwingUtilities.invokeLater(this::updateScene);
                        }
                    } else if (line.startsWith("ALL_STATS:")) {
                        updateLeaderboardUI(line.substring(10));
                    }
                }
            } catch (Exception e) { System.err.println("Network Error"); }
        }).start();
    }

    private void handleNext() {
        if (isChoosing || isFading) return;
        if (isTyping) { 
            if(typewriterTimer != null) typewriterTimer.stop(); 
            isTyping = false; 
            updateDialogueDisplay(dialogues[currentIndex]); 
            return; 
        }

        // --- ระบบ Choice ---
        if (currentIndex == 10) { showChoices("น่ารักมากๆเลย เหมาะกับเธอสุดๆ", "ก็พอได้นะ", 11, 12); return; }
        if (currentIndex == 17) { showChoices("ฉันชอบอาหารฝีมือเธอที่สุดเลย", "ก็อร่อยดีนะ", 18, 19); return; }
        if (currentIndex == 38) { showChoices("พุ่งเข้าไปปกป้องอริส", "บอกให้อริสหลบเอง", 39, 40); return; }
        if (currentIndex == 57) { showChoices("เราจะไปเดทกันไงละจ๊ะ อริสจัง", "ที่อยู่ของจอมมารยังไงหละ", 58, 59); return; }

        // คำนวณลำดับถัดไป
        int nextIdx = currentIndex;
        if (currentIndex == 11) nextIdx = 13;
        else if (currentIndex == 18) nextIdx = 20;
        else if (currentIndex == 39) nextIdx = 41;
        else if (currentIndex == 58) nextIdx = 60;
        else nextIdx++;

        if (nextIdx < dialogues.length) {
            final int targetIdx = nextIdx;
            
            // --- จุดแก้ไขสำคัญ: ตรวจสอบ imagePaths ---
            if (!imagePaths[currentIndex].equals(imagePaths[targetIdx])) {
                // ถ้าภาพพื้นหลังเปลี่ยน ให้เล่นเอฟเฟกต์ Fade
                performSceneFade(() -> {
                    currentIndex = targetIdx;
                    syncOnline();
                    updateScene();
                });
            } else {
                // ถ้าภาพเดิม ให้ข้ามไปฉากถัดไปทันทีโดยไม่ต้อง Fade
                currentIndex = targetIdx;
                syncOnline();
                updateScene();
            }
        } else { finishPart(); }
    }

    private void syncOnline() {
        if (relationdata.isOnlineMode && networkOut != null) {
            networkOut.println("SYNC_INDEX:" + currentIndex);
        }
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true; 
        choiceButton1 = createChoiceButton(text1, 380, t1);
        choiceButton2 = createChoiceButton(text2, 450, t2);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private void startCharacterFadeIn() {
        charAlpha = 0.0f; // เริ่มที่ตัวล่องหน
        if (charFadeTimer != null && charFadeTimer.isRunning()) charFadeTimer.stop();
        
        charFadeTimer = new Timer(30, e -> {
            charAlpha += 0.05f; // ค่อยๆ ชัดขึ้นทีละนิด
            if (charAlpha >= 1.0f) {
                charAlpha = 1.0f;
                ((Timer)e.getSource()).stop();
            }
            // สั่งให้ Label ทั้งคู่ทำการวาดใหม่ด้วยค่า Alpha ปัจจุบัน
            characterLabel.repaint();
            characterLabel2.repaint();
        });
        charFadeTimer.start();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground()); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(new Color(225, 105, 180)); g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setBounds(800, y, 350, 60); btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setBackground(new Color(255, 255, 255, 180)); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.addActionListener(e -> {
            layeredPane.remove(choiceButton1); layeredPane.remove(choiceButton2); isChoosing = false;
            if (target == 11 || target == 18 || target == 39 || target == 59) relationdata.aliceRel.addAffinity(10);
            else relationdata.aliceRel.decreaseAffinity(5);
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                networkOut.println("SYNC_INDEX:" + target);
            }
            updateAffinityUI(); currentIndex = target; updateScene();
        });
        return btn;
    }

    private void setupStatusOverlay() {
        statusOverlay = new JPanel(new BorderLayout(15, 15));
        statusOverlay.setBackground(new Color(20, 20, 25, 200)); 
        statusOverlay.setBounds(440, 150, 400, 450);
        statusOverlay.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        statusOverlay.setVisible(false);

        onlineCountLabel = new JLabel("ผู้เล่นออนไลน์: 1", SwingConstants.CENTER);
        onlineCountLabel.setForeground(Color.CYAN);
        affinityStatusLabel = new JLabel("", SwingConstants.CENTER);
        
        statusOverlay.add(new JLabel("🏆 Leaderboard", SwingConstants.CENTER) {{ 
            setForeground(Color.YELLOW); setFont(new Font("Tahoma", Font.BOLD, 22)); 
        }}, BorderLayout.NORTH);
        statusOverlay.add(new JScrollPane(affinityStatusLabel) {{ 
            getViewport().setOpaque(false); setOpaque(false); setBorder(null); 
        }}, BorderLayout.CENTER);
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
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'>");
        sb.append("<table width='320' style='color:white; font-family:Tahoma;'>");
        sb.append("<tr style='color:#FFD700;'><th>ผู้เล่น</th><th align='right'>คะแนน</th></tr>");
        for (String p : data.split(",")) {
            if (p.contains("=")) {
                String[] parts = p.split("=");
                String color = parts[0].equals(relationdata.playerName) ? "#00FF7F" : "white";
                sb.append("<tr><td style='color:").append(color).append(";'>").append(parts[0])
                  .append("</td><td align='right' style='color:#FF69B4;'>").append(parts[1]).append(" pt</td></tr>");
            }
        }
        sb.append("</table></body></html>");
        SwingUtilities.invokeLater(() -> affinityStatusLabel.setText(sb.toString()));
    }

    private void performSceneFade(Runnable onBlack) {
        isFading = true; alpha = 0.0f;
        if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        Timer fadeOut = new Timer(30, e -> {
            alpha += 0.1f;
            if (alpha >= 1.0f) {
                alpha = 1.0f; ((Timer)e.getSource()).stop(); onBlack.run();
                new Timer(300, ev -> {
                    ((Timer)ev.getSource()).stop();
                    new Timer(30, eve -> {
                        alpha -= 0.1f; if (alpha <= 0) { alpha = 0; ((Timer)eve.getSource()).stop(); isFading = false; }
                        fadeOverlay.repaint();
                    }).start();
                }).start();
            }
            fadeOverlay.repaint();
        });
        fadeOut.start();
    }

    private void updateScene() {
        // 1. อัปเดตชื่อผู้พูดและเริ่มระบบพิมพ์ตัวอักษร (Typewriter)
        if (currentIndex < names.length) {
            nameLabel.setText(names[currentIndex]);
        } else {
            nameLabel.setText("");
        }
        
        if (currentIndex < dialogues.length) {
            startTypewriter(dialogues[currentIndex]);
        }

        // 2. จัดการเสียง BGM และ Sound Effects ตามลำดับฉาก
        handleSoundEffects(currentIndex);

        // 3. อัปเดตภาพพื้นหลัง (ดึงจาก Cache เพื่อความลื่นไหล)
        backgroundLabel.setIcon(getOptimizedImage(imagePaths[currentIndex], 1280, 800));

        // 4. ระบบตรวจสอบการเปลี่ยนรูปตัวละครเพื่อเล่นเอฟเฟกต์ Fade In
        String currentPath1 = (currentIndex < charPaths.length) ? charPaths[currentIndex] : "";
        String lastPath1 = (currentIndex > 0) ? charPaths[currentIndex - 1] : "";
        
        String currentPath2 = (currentIndex < charPaths2.length) ? charPaths2[currentIndex] : "";
        String lastPath2 = (currentIndex > 0) ? charPaths2[currentIndex - 1] : "";

        // ถ้ามีการเปลี่ยนรูปตัวละคร Mc หรือ อริส/ตัวประกอบ หรือเป็นฉากเริ่มเกม ให้เริ่ม Fade In
        if (!currentPath1.equals(lastPath1) || !currentPath2.equals(lastPath2) || currentIndex == 0) {
            startCharacterFadeIn();
        }

        // 5. อัปเดตตำแหน่งและขนาดของเลเยอร์ตัวละครทั้ง 2 เลเยอร์
        updateCharacterLayer(characterLabel, charPaths);  // เลเยอร์ Mc (ซ้าย)
        updateCharacterLayer(characterLabel2, charPaths2); // เลเยอร์ อริส/ศัตรู (ขวา)
        
        // วาดหน้าจอใหม่เพื่อให้การแสดงผลถูกต้อง
        layeredPane.repaint();
    }

    private void updateCharacterLayer(JLabel label, String[] paths) {
            if (currentIndex >= paths.length || paths[currentIndex].contains("empty")) { 
                label.setIcon(null); 
                return; 
            }
            
            String path = paths[currentIndex];
            
            // 1. เลเยอร์พระเอก (Mc) - อยู่ซ้าย
            if (path.contains("Mc/body")) { 
                label.setIcon(getOptimizedImage(path, 500, 900)); 
                label.setBounds(50, 100, 600, 900); 
            } 
            // 2. เลเยอร์อริส (Alice) - อยู่ขวา
            else if (path.contains("Alice") || path.contains("Girl")) { 
                label.setIcon(getOptimizedImage(path, 1050, 700)); 
                label.setBounds(420, 70, 1300, 900); 
            } 
            // 3. เลเยอร์ลุง (Uncle) - อยู่ซ้าย
            else if (path.contains("Uncle.png")) { 
                label.setIcon(getOptimizedImage(path, 900, 900)); 
                label.setBounds(-100, 225, 900, 900); 
            } 
            // 4. เลเยอร์ปีศาจ (Demon) - ปรับให้ไปอยู่ด้านซ้าย (X = 50) เพื่อไม่ให้ทับนางเอก
            else if (path.contains("demon")) { 
                label.setIcon(getOptimizedImage(path, 800, 900)); 
                label.setBounds(50, 100, 800, 900); // ปรับจาก 420 เป็น 50
            }
            // 5. อื่นๆ (Default)
            else { 
                label.setIcon(getOptimizedImage(path, 800, 900)); 
                label.setBounds(420, 100, 800, 900); 
            }
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox(); dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(225, 520, 800, 200); layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);
        nameLabel = new JLabel(); nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(180, 40, 90)); nameLabel.setBounds(60, 15, 300, 40); dialoguePanel.add(nameLabel);
        dialogueArea = new JLabel(); dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115)); dialogueArea.setBounds(60, 65, 700, 110);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP); dialoguePanel.add(dialogueArea);
    }

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(2, 1)); relPanel.setBounds(25, 25, 300, 70); relPanel.setOpaque(false);
        affinityLabel = new JLabel("ความสนิท: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 22)); affinityLabel.setForeground(Color.WHITE);
        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 20)); statusLabel.setForeground(new Color(255, 204, 0));
        relPanel.add(affinityLabel); relPanel.add(statusLabel); layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }

    private void updateAffinityUI() {
        affinityLabel.setText("ความสนิท: " + relationdata.aliceRel.getAffinity());
        statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
    }

    private void startTypewriter(String text) {
        if (typewriterTimer != null) typewriterTimer.stop();
        charIndex = 0; isTyping = true; dialogueArea.setText("");
        typewriterTimer = new Timer(25, e -> {
            if (charIndex < text.length()) { charIndex++; updateDialogueDisplay(text.substring(0, charIndex)); }
            else { typewriterTimer.stop(); isTyping = false; }
        }); typewriterTimer.start();
    }

    private void updateDialogueDisplay(String text) {
        dialogueArea.setText("<html><body style='width: 700px;'>" + text + "</body></html>");
    }

    private void startFadeIn() {
        Timer ft = new Timer(45, e -> { alpha -= 0.05f; if (alpha <= 0) { alpha = 0; ((Timer)e.getSource()).stop(); layeredPane.remove(fadeOverlay); } fadeOverlay.repaint(); }); ft.start();
    }

    private void setupFadeOverlay() {
        fadeOverlay = new JPanel() { @Override protected void paintComponent(Graphics g) { Graphics2D g2d = (Graphics2D) g; g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255))); g2d.fillRect(0, 0, getWidth(), getHeight()); } };
        fadeOverlay.setBounds(0, 0, 1280, 800); fadeOverlay.setOpaque(false);
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) { imageCache.put(key, scaleImage(path, w, h)); }
        return imageCache.get(key);
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try { return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)); }
        catch (Exception e) { return null; }
    }

    private void playSE(String path, boolean loop, float volume) {
        try {
            File soundFile = new File(path); if (!soundFile.exists()) return;
            AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip(); clip.open(ai);
            FloatControl gc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); gc.setValue(volume);
            if (loop) { this.bgmClip = clip; clip.loop(Clip.LOOP_CONTINUOUSLY); } clip.start();
        } catch (Exception e) {}
    }

    private void stopBGM() {
        if (bgmClip != null) {
            if (bgmClip.isRunning()) {
                bgmClip.stop();
            }
            bgmClip.flush();
            bgmClip.close(); 
            bgmClip = null;  
        }
    }

    private void handleSoundEffects(int index) {
        if (index == 11){
            playSE("res/sound/baka.wav", false, 5.0f);
        }
        if (index == 12){
            playSE("res/sound/muuuu.wav", false, 5.0f);
        }
        if (index == 18){
            playSE("res/sound/hhonto.wav", false, 5.0f);
        }
        if (index == 19){
            playSE("res/sound/emmm.wav", false, 5.0f);
        }
        if (index == 22) { 
            stopBGM();
            playSE("res/sound/soundtrack6.wav", true, -10.0f); 
        }
        if (index == 27){
            playSE("res/sound/evillaugh.wav", false, -10.0f);
            playSE("res/sound/housefire.wav", false, -10.0f);
        }
        if (index == 39){
            playSE("res/sound/Arigato.wav", false, 0.0f);
        }
        if (index == 46) {
            stopBGM();
            playSE("res/sound/soundtrack7.wav", true, -10.0f); 
        }
        if (index == 58){
            playSE("res/sound/Baka janai no.wav", false, 5.0f);
        }
        if (index == 59){
            playSE("res/sound/Arigato.wav", false, 0.0f);
        }

    }

    private void finishPart() {
        if (networkOut != null) networkOut.close();
        if (bgmClip != null) { bgmClip.stop(); bgmClip.close(); }
        JOptionPane.showMessageDialog(null, "จบ Part 4: การผจญภัยกำลังจะเริ่มขึ้น!");
        new part5().setVisible(true);
        dispose();
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new part4().setVisible(true)); }
}

// คลาสเสริมที่ใช้ร่วมกันทก Part
class VisualNovelBox extends JPanel {
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
}