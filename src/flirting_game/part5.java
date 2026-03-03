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

public class part5 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, characterLabel2, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel;
    private float alpha = 1.0f;
    private JPanel fadeOverlay;
    private int currentIndex = 0;
    private Clip bgmClip;      
    private Clip effectClip;
    private JButton choiceButton1, choiceButton2, choiceButton3;
    private boolean isChoosing = false;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    private float charAlpha1 = 0.0f; 
    private float charAlpha2 = 0.0f; 
    private Timer charFadeTimer1, charFadeTimer2;

    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    private java.io.PrintWriter networkOut;

    private boolean isFinishing = false;

    private float bgAlpha = 1.0f; // ค่าความโปร่งใสของพื้นหลัง
    private Timer bgFadeTimer;    // Timer สำหรับ Fade พื้นหลัง
    private String lastBgPath = ""; // เก็บเส้นทางรูปภาพล่าสุดเพื่อเช็คการเปลี่ยนแปลง
    private boolean isBgFading = false; // เช็คสถานะการ Fade
    
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 24);

    private String[] imagePaths = {
        "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png",
        "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", //0-7 
        "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
        "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", //8-15
        "res/scene5/s1.png", "res/scene5/s2.png", "res/scene5/s2.png", "res/scene5/s2.png", "res/scene5/s2.png", // 16-20
        "res/scene5/s2.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", // 21-24
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", //25-32
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png","res/scene5/s4.png", //33-40
        "res/scene5/s4.png", "res/scene5/s4.png", "res/scene5/s4.png", "res/scene5/s4.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", //41-48
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", //49-56
        "res/scene5/s5.png", "res/scene5/s5.png", "res/scene5/s5.png", "res/scene5/s5.png", //57-60
    };

    private String[] charPaths = {
        "res/empty.png", "res/empty.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", //0-8 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-shy1.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", //9-15
        "res/scene5/Alice-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",//16-26
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",//27-34
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",//35-42
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/empty.png", "res/empty.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png","res/scene5/Alice-normal1.png",//43-50
        "res/empty.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png","res/scene5/Alice-normal2.png",
        "res/scene5/Alice-shy1.png", "res/scene5/Alice-normal2.png","res/scene5/Alice-normal1.png","res/scene5/Alice-shower1.png", //51-58
        "res/scene5/Alice-shower3.png","res/scene5/Alice-shower2.png",//60
    };

    private String[] charPaths2 = {
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", //0-9
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",//10-19 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png", "res/scene5/Alice-fight1.png",//20-27
        "res/scene5/Alice-fight2.png", "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png",
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png", "res/scene5/Alice-shy2.png",//28-33
        "res/scene5/Alice-shy1.png", "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png",
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png",//34-38
        // --- เพิ่มส่วนที่ขาดหายไปตรงนี้เพื่อให้ครบ 58 รายการ ---
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png", "res/empty.png", "res/empty.png", "res/empty.png",//39-43
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",//44-53
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png"//54-57
    };

    private String[] names = { 
        " ", " ", "ฉัน", "ฉัน", "อริส", "อริส", "ฉัน", "อริส",
        "ฉัน", "อริส", "อริส", "อริส", "อริส", "อริส", "ฉัน", "อริส", "ฉัน",//0-16
        "ฉัน", "ฉัน", "ฉัน", " ", "ฉัน", "ฉัน","ปีศาจเดโม", "ปีศาจเดโม",//17-24
        "อริส", "ฉัน", "อริส", "อริส", "อริส", "ปีศาจเดโม", "ปีศาจเดโม", "ฉัน",//25-32
        "อริส", "อริส", "อริส","ปีศาจเดโม", "ฉัน", "อริส", "ฉัน", "อริส", " ",//33-40
        " ", " ", " ", " ", "ฉัน", "อริส", "ฉัน", "ฉัน", //41-48
        "อริส", " ", "อริส", "ฉัน", "อริส", "อริส", "อริส","อริส","อริส"," ", " ",//49-57
    };
    
    private String[] dialogues = {
        "ตัวเรากับอริสที่กําลังอยู่ในป่า", // 0
        "ตัวเรากับอริสได้เดินทางออกจากหมู่บ้านมา 1 อาทิตย์เเล้ว", // 1
        "ฉันคิดว่าเราน่าจะออกมาไกลพอสมควรละนะ", // 2
        "ดีนะที่ก่อนหน้านี้ได้เเผนที่มาจากคนในหมู่บ้าน", // 3
        "อืออ", // 4 
        "นายหิวรึยัง?", // 5
        "ก็..นิดหน่อยนะ", // 6
        "งั้นเรานั่งพักกินข้าวตรงนี้ก่อนมั้ย?", // 7
        "ก็ดีเหมือนกันนะ", // 8
        "นายอยากกินอะไรมั้ย?", // 9
        "นายนี่ละก็..", // 10 choice1
        "อื้อโอเค",// 11 choice2
        "เข้าใจแล้ว",// 12 choice3
        "เดี๋ยวฉันจะไปหาของเเถวนี้ก่อนนะ", // 13
        "ให้ฉันไปด้วยมั้ย?", // 14
        "ไม่เป็นไรหรอก นายนั่งรออยู่ตรงนี้แหละ", // 15
        "อือ..ก็ได้", // 16
        "ตอนนี้เราน่าจะยังอยู่ในเขตป่าLifeอยู่นะ", // 17
        "คงต้องใช้เวลาอีกนานเลยกว่าถึงจุดหมาย", // 18
        "ดีเลย เราจะได้ใช้เวลานี้อยู่กับอริสมากขึ้น", // 19
        "อร๊ายยยย", // 20
        "..นั่นมัน..เสียงอริสนี่!!", // 21
        "เกิดอะไรขึ้น อริส!!", // 22
        "กะจะเดินเล่นเฉยๆ ดันมาเจอมนุษย์ซะงั้น", // 23
        "เเต่ก็ดี ข้ากําลังหิวได้ที่เลย", // 24
        "…(ชื่อตัวละครเรา)!!", // 25
        "นี่เธอเบาดเจ็บตรงไหนรึปล่าว?", // 26
        "ไม่ ฉันไม่เป็นไร", // 27
        "อยู่ๆปีศาจมันก็เข้ามาโจมตีเเบบกระทันหัน", // 28
        "เเต่ก็พอหลบได้ เลยมีเเผลถลอกนิดหน่อยหนะ", // 29
        "นี่พ่อหนุ่ม ข้าขอเเม่หนูตรงนั้นได้ไหม?", // 30
        "เเล้วข้าจะไว้ชีวิตเจ้า", // 31
        "ไม่มีทาง!!", // 32
        "(เขิน)", // 33 choice1
        "มันใช่เวลามั้ย!! ไอ่บ้า!!", // 34 choice2
        "อ่าว…",//35 choice3
        "ยังไงข้าก็จะกินพวกเจ้าทั้งสองอยู่ดี", // 36
        "อริส ครั้งนี้ให้ฉันเป็นคนจัดการเอง", // 37
        "จะไม่เป็นไรหรอ?", // 38
        "เเค่นี้สบายมาก", // 39
        "งั้นฝากด้วยนะ", // 40
        "ตัวเราได้ใช้เวทย์โจมตี ปีศาจเดโมก่อน", // 41
        "ปีศาจเดโมหลบได้ เเละพุ่งโจมตีใส่เราทันที", // 42
        "เรากระโดดหลบ เเละปล่อยพลังเวทย์ที่รุนเเรงใส่ ปีศาจเดโม", // 43
        "ปีศาจเดโมโดนพลังเวทย์เต็มๆ", // 44
        "ปีศาจเดโม กลัวจะถูกกําจัด เลยใช้ม่านควันสีดําเเละหลบหนี", // 45
        "พลาดท่าจนได้", // 46
        "เเต่อย่างน้อยตอนนี้ก็คงจะปลอดภัยเเล้ว", // 47
        "ฉันคิดว่าปีศาจตัวเมื่อกี้ต้องเกี่ยวข้องกับจอมมารเเน่เลย", // 48
        "นี่เรา..ยังไม่ได้กินข้าวกันเลยนี่หน่า", // 49
        "จริงด้วย! งั้นเดี๋ยวฉันรีบไปทําให้นะ", // 50
        "หลังจากพักผ่อนเเละกินอะไรกันเสร็จเเล้ว", // 51
        "ฉันรู้สึกร้อนมากเลย..", // 52
        "จะไปอาบนํ้าไหมละ ดูเหมือนว่าจะมีลําธารใกล้ๆนะ", // 53
        "จริงหรอ งั้นฉันขอไปอาบนํ้าก่อน", // 54
        "คนลามก!!...", // 55 choice1
        "โอเค เดี๋ยวมานะ", // 56 choice2
        "รู้เเล้วน่า!!",// 57 choice3
        "อริสกําลังอาบนํ้า", // 58
        "เเต่จู่ๆ ก็มีเสียง เเปลกๆที่พุ่มไม้", // 59
        "อริสเห็นเงาคนอยู่หลังพุ่มไม้..", // 60
    };

    public part5() {
        setTitle("ISEKAI DEMO - Part 5: The Journey Begins");
        setSize(1280, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // แก้ปัญหา: ปิดหน้าต่างแล้วเพลงไม่หยุด
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                stopAllSounds(); 
                System.exit(0);
            }
        });

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playSE("res/sound/soundtrack8.wav", true, -10.0f);

        backgroundLabel = new JLabel() {
            @Override 
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // กำหนดค่าความโปร่งใสตามตัวแปร bgAlpha
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, bgAlpha));
                super.paintComponent(g2d); 
                g2d.dispose();
            }
        };
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha1)); 
                super.paintComponent(g2d); g2d.dispose();
            }
        };
        layeredPane.add(characterLabel, Integer.valueOf(100));

        characterLabel2 = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha2)); 
                super.paintComponent(g2d); g2d.dispose();
            }
        };
        layeredPane.add(characterLabel2, Integer.valueOf(101));

        setupDialogueUI();
        for (int i = 0; i < dialogues.length; i++) {
            if (dialogues[i].contains("(ชื่อตัวละครเรา)")) {
                dialogues[i] = dialogues[i].replace("(ชื่อตัวละครเรา)", relationdata.playerName);
            }
        }
        setupRelationshipUI();
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

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNext();
            }
        });
    }

    private void handleNext() {
            if (isChoosing || isFinishing) return;
            
            // ป้องกัน Error กรณีดัชนีเกิน
            if (currentIndex >= dialogues.length) {
                finishGame();
                return;
            }

            if (isTyping) {
                if (typewriterTimer != null) typewriterTimer.stop();
                isTyping = false;
                dialogueArea.setText("<html><body style='width: 950px;'>" + dialogues[currentIndex] + "</body></html>");
                return;
            }

            // --- Choice 1:  ---
            if (currentIndex == 9) { 

                showChoices("ฉันกินได้หมดเลย ขอเเค่เป็นอาหารที่เธอทํา", "ฉันยังไงก็ได้", "อะไรก็ได้ไปเอามาเถอะ", 10, 11, 12);
                return; 
            }
            if (currentIndex == 10 || currentIndex == 11 || currentIndex == 12) { 
                currentIndex = 13; 
                updateScene(); 
                return; 
            }

            // --- Choice 2:---
            if (currentIndex == 32) { 
                // มี 2 ตัวเลือก
                showChoices("ฉันจะไม่ยกอริสให้เเกเด็ดขาด", "ไม่มีวัน เพราะอริสเป็นของฉันคนเดียวเท่านั้น", "เธอคนนี้มีดีตรงไหนกันนะ", 33, 34, 35);
                return; 
            }
            if (currentIndex == 33 || currentIndex == 34 || currentIndex == 35) { 
                currentIndex = 36; 
                updateScene(); 
                return; 
            }

            // --- Choice 3: ---
            if (currentIndex == 54) { 

                showChoices("ให้ฉันไปอาบด้วยมั้ยหละ?", "เดี๋ยวฉันจะรอจรงนี้นะ มีอะไรก็เรียกได้เลย", "เร็วๆหน่อยละกัน ฉันไม่อยากรอนาน", 55, 56, 57); 
                return; 
            }
            if (currentIndex == 55 || currentIndex == 56 || currentIndex == 57) { 
                currentIndex = 58; 
                updateScene(); 
                return; 
            }

            // --- การดำเนินเนื้อเรื่องปกติ ---
            if (currentIndex < dialogues.length - 1) {
                currentIndex++;
                updateScene();
            } else {
                finishGame(); // จบที่ Index 60
            }
    }

    private void handleSoundEffects(int index) {
        if (index == 10) playEffect("res/sound/antate.wav", 5.0f);
        if (index == 19) playEffect("res/sound/jaaaaa.wav", 0.0f);
        if (index == 22) {
            stopBGM();
            playSE("res/sound/soundtrack9.wav", false, -10.0f);
        }
        if (index == 33) playEffect("res/sound/echi.wav", 5.0f);
        if (index == 39) playEffect("res/sound/winddash.wav", 0.0f);
        if (index == 46) {
            stopBGM();
            playSE("res/sound/soundrack11.wav", false, -10.0f);
        } 
        if (index == 53) playEffect("res/sound/Baka janai no.wav", 0.0f);
        // แก้ไข: หยุด BGM ตั้งแต่ 56 เป็นต้นไป
        if (index >= 56) {
            stopBGM();
        }
        if (index == 57) playEffect("res/sound/ahhhhh.wav", -5.0f);
    }

    private void updateScene() {
        if (currentIndex >= names.length) nameLabel.setText(names[names.length-1]);
        else nameLabel.setText(names[currentIndex]);


        if (currentIndex < imagePaths.length) {
            String currentBg = imagePaths[currentIndex];
            
            // ถ้า Path รูปภาพเปลี่ยนไปจากเดิม
            if (!currentBg.equals(lastBgPath)) {
                backgroundLabel.setIcon(getOptimizedImage(currentBg, 1280, 800));
                startBackgroundFade(); // สั่งให้ Fade In รูปใหม่
                lastBgPath = currentBg; // อัปเดต Path ล่าสุด
            }
        }

        handleSoundEffects(currentIndex);
        updateDialogueDisplay(dialogues[currentIndex]);

        // --- เช็คการเปลี่ยนรูปเพื่อทำ Fade ---
        String currentP1 = (currentIndex < charPaths.length) ? charPaths[currentIndex] : "res/empty.png";
        String prevP1 = (currentIndex > 0) ? charPaths[currentIndex - 1] : "res/empty.png";
        String currentP2 = (currentIndex < charPaths2.length) ? charPaths2[currentIndex] : "res/empty.png";
        String prevP2 = (currentIndex > 0) ? charPaths2[currentIndex - 1] : "res/empty.png";

        if (!currentP1.equals(prevP1)) {
            if (currentP1.contains("empty")) charAlpha1 = 0.0f;
            else startCharacterFadeIn1();
        }
        if (!currentP2.equals(prevP2)) {
            if (currentP2.contains("empty")) charAlpha2 = 0.0f;
            else startCharacterFadeIn2();
        }

        updateCharacterLayer(characterLabel2, charPaths2);
        updateCharacterLayer(characterLabel, charPaths);
        layeredPane.repaint();
    }

    private void updateCharacterLayer(JLabel label, String[] paths) {
        if (currentIndex >= paths.length || paths[currentIndex].contains("empty")) {
            label.setIcon(null);
            return;
        }
        String path = paths[currentIndex];
        if (path.contains("demogigi")) {
            label.setIcon(getOptimizedImage(path, 900, 800));
            if (currentIndex >= 41) label.setBounds(220, 70, 900, 800); 
            else if (currentIndex >= 25) label.setBounds(-100, 50, 900, 800); 
            else label.setBounds(100, 50, 900, 800); 
        }
        else if (path.contains("Alice")) {
            label.setIcon(getOptimizedImage(path, 1200, 800));
            if (currentIndex >= 46) label.setBounds(50, 100, 950, 800); 
            else if (currentIndex >= 41) label.setIcon(null); 
            else if (currentIndex >= 25) label.setBounds(360, 80, 950, 800); 
            else label.setBounds(80, 100, 1200, 800); 
        }
    }

    private void startCharacterFadeIn1() {
        charAlpha1 = 0.0f;
        if (charFadeTimer1 != null && charFadeTimer1.isRunning()) charFadeTimer1.stop();
        charFadeTimer1 = new Timer(30, e -> {
            charAlpha1 += 0.1f;
            if (charAlpha1 >= 1.0f) { charAlpha1 = 1.0f; ((Timer)e.getSource()).stop(); }
            characterLabel.repaint();
        });
        charFadeTimer1.start();
    }

    private void startCharacterFadeIn2() {
        charAlpha2 = 0.0f;
        if (charFadeTimer2 != null && charFadeTimer2.isRunning()) charFadeTimer2.stop();
        charFadeTimer2 = new Timer(30, e -> {
            charAlpha2 += 0.1f;
            if (charAlpha2 >= 1.0f) { charAlpha2 = 1.0f; ((Timer)e.getSource()).stop(); }
            characterLabel2.repaint();
        });
        charFadeTimer2.start();
    }
    
    private void updateDialogueDisplay(String text) {
        if (typewriterTimer != null) typewriterTimer.stop();
        charIndex = 0;
        isTyping = true;
        dialogueArea.setText("");
        typewriterTimer = new Timer(25, e -> {
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
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void startFadeOut(Runnable onComplete) {
        // นำ fadeOverlay กลับมาแสดงผล (ถ้าถูกลบออกไปตอน startFadeIn)
        if (fadeOverlay.getParent() == null) {
            layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        }
        
        alpha = 0.0f; // เริ่มต้นที่โปร่งใส
        Timer fadeTimer = new Timer(30, e -> {
            alpha += 0.05f; // ค่อยๆ เพิ่มความมืด
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                ((Timer)e.getSource()).stop();
                if (onComplete != null) onComplete.run(); // เมื่อมืดสนิทแล้ว ให้ทำงานที่ส่งมา
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
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

    private void stopAllSounds() {
        stopBGM();
        if (effectClip != null) { effectClip.stop(); effectClip.close(); effectClip = null; }
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox(); 
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(225, 520, 800, 200);
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

    private void showChoices(String text1, String text2, String text3, int t1, int t2, int t3) {
        isChoosing = true; 

        if (choiceButton1 != null) layeredPane.remove(choiceButton1);
        if (choiceButton2 != null) layeredPane.remove(choiceButton2);
        if (choiceButton3 != null) layeredPane.remove(choiceButton3);

        choiceButton1 = createChoiceButton(text1, 250, t1); 
        choiceButton2 = createChoiceButton(text2, 320, t2); 
        choiceButton3 = createChoiceButton(text3, 390, t3); 
        // ใช้ POPUP_LAYER เพื่อให้ปุ่มอยู่บนสุดเสมอ
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton3, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            // --- ตัวแปรสำหรับระบบ Animation ---
            private double scale = 1.0;
            private int alphaMod = 180; // ค่าความโปร่งใสเริ่มต้นตามที่คุณตั้งไว้
            private Timer animTimer;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // --- Effect ขยายจากจุดกลางปุ่ม ---
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                g2.translate(centerX, centerY);
                g2.scale(scale, scale);
                g2.translate(-centerX, -centerY);

                // วาดพื้นหลังโค้งมน (จะสว่างขึ้นเมื่อเมาส์ Hover)
                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // วาดเส้นขอบสีชมพูเข้ม (ธีมหลักของคุณ)
                g2.setColor(new Color(225, 105, 180)); 
                g2.setStroke(new BasicStroke(2));   
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);

                g2.dispose();
                super.paintComponent(g); // วาดข้อความ
            }

            {
                // เพิ่ม Mouse Listener สำหรับดักจับการเคลื่อนไหวเมาส์
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        startAnimation(1.05, 230); // ขยาย 5% และสว่างขึ้น
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        startAnimation(1.0, 180); // กลับสู่ปกติ
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        scale = 0.95; // ปุ่มยุบลงตอนคลิก
                        repaint();
                    }
                });
            }

            private void startAnimation(double targetScale, int targetAlpha) {
                if (animTimer != null && animTimer.isRunning()) animTimer.stop();
                animTimer = new Timer(15, ev -> {
                    // ปรับ Scale นุ่มๆ
                    if (scale < targetScale) scale += 0.01;
                    else if (scale > targetScale) scale -= 0.01;

                    // ปรับค่าความชัดพื้นหลัง
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

        // --- ตั้งค่าดีไซน์ปุ่ม ---
        btn.setBounds(800, y, 350, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setForeground(new Color(45, 65, 115)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // เปลี่ยนเป็นรูปมือ

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 

        // --- Logic การทำงานและระบบ Affinity (Part 5) ---
        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            layeredPane.remove(choiceButton3);
            isChoosing = false; 

            // ตรวจสอบคำตอบ: กินอะไรก็ได้ (10), ไม่ยกให้ใคร (32), สัญญาว่าจะรอ (54)
            if (target == 10 || target == 33 || target == 34 || target == 55) {
                relationdata.aliceRel.addAffinity(10); 
            } else if (target == 11 || target == 56){
                System.out.println("คะแนนเท่าเดิม");
            } else {
                relationdata.aliceRel.decreaseAffinity(5); 
            }

            // ส่งข้อมูลไป Server (Online Mode)
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                networkOut.println("SYNC_INDEX:" + target);
            }

            // อัปเดต UI คะแนน
            if (affinityLabel != null) {
                affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            }
            if (statusLabel != null) statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());

            currentIndex = target; 
            updateScene(); 
            layeredPane.repaint();
        });

        return btn;
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) imageCache.put(key, scaleImage(path, w, h));
        return imageCache.get(key);
    }

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

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(30, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0; 
                ((Timer)e.getSource()).stop();
                layeredPane.remove(fadeOverlay); // สำคัญ: ต้องลบออกเพื่อให้คลิกปุ่มหรือฉากได้
                updateScene(); 
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void finishGame() {
        if (isFinishing) return; // ถ้ากำลังจบอยู่แล้ว ให้กดซ้ำไม่ได้
        isFinishing = true;      // ล็อคสถานะทันที

        // เริ่มกระบวนการ Fade Out ก่อน
        startFadeOut(() -> {
            stopAllSounds(); // หยุดเสียงของ Part 5
            
            // เมื่อหน้าจอมืดสนิทแล้ว จึงสลับหน้าจอไป Part 6
            SwingUtilities.invokeLater(() -> {
                new part6().setVisible(true); // จะเปิดเพียงครั้งเดียวเพราะโดนล็อคไว้แล้ว
                dispose(); // ปิดหน้าจอ Part 5
            });
        });
    }

    private void startBackgroundFade() {
        bgAlpha = 0.0f; // เริ่มจากมืด/โปร่งใส
        isBgFading = true;
        
        if (bgFadeTimer != null && bgFadeTimer.isRunning()) bgFadeTimer.stop();
        
        // ปรับตัวเลข 50 (ms) คือความถี่, 0.02f คือความเร็ว (ยิ่งน้อยยิ่งนาน)
        bgFadeTimer = new Timer(40, e -> {
            bgAlpha += 0.04f; 
            if (bgAlpha >= 1.0f) {
                bgAlpha = 1.0f;
                isBgFading = false;
                ((Timer)e.getSource()).stop();
            }
            backgroundLabel.repaint();
        });
        bgFadeTimer.start();
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                java.net.Socket socket = new java.net.Socket(relationdata.serverIP, 5000);
                networkOut = new java.io.PrintWriter(socket.getOutputStream(), true);
                java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));

                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:5");

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part5().setVisible(true));
    }
}

/*class VisualNovelBox extends JPanel {
    private int cornerRadius = 30;
    public VisualNovelBox() { setOpaque(false); }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 250, 255, 180), 0, getHeight(), new Color(255, 235, 245, 230));
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.setColor(new Color(255, 150, 200, 200));
        g2.setStroke(new BasicStroke(4f));
        g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, cornerRadius, cornerRadius);
    }
}*/