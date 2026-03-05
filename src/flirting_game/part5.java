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

    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    private java.io.PrintWriter networkOut;

    private boolean isFinishing = false;

    private float bgAlpha = 1.0f; 
    private Timer bgFadeTimer;    
    private String lastBgPath = ""; 
    
    private JPanel waitOverlay;
    private boolean isWaiting = false;
    
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 24);

    private String[] imagePaths = {
        "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png",
        "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
        "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
        "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
        "res/scene5/s1.png", "res/scene5/s2.png", "res/scene5/s2.png", "res/scene5/s2.png", "res/scene5/s2.png", 
        "res/scene5/s2.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png","res/scene5/s4.png", 
        "res/scene5/s4.png", "res/scene5/s4.png", "res/scene5/s4.png", "res/scene5/s4.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
        "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
        "res/scene5/s5.png", "res/scene5/s5.png", "res/scene5/s5.png", "res/scene5/s5.png", 
    };

    private String[] charPaths = {
        "res/empty.png", "res/empty.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-shy1.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", 
        "res/scene5/Alice-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/empty.png", "res/empty.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png","res/scene5/Alice-normal1.png",
        "res/empty.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png","res/scene5/Alice-normal2.png",
        "res/scene5/Alice-shy1.png", "res/scene5/Alice-normal2.png","res/scene5/Alice-normal1.png","res/scene5/Alice-shower1.png", 
        "res/scene5/Alice-shower3.png","res/scene5/Alice-shower2.png",
    };

    private String[] charPaths2 = {
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png", "res/scene5/Alice-fight1.png",
        "res/scene5/Alice-fight2.png", "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png",
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png", "res/scene5/Alice-shy2.png",
        "res/scene5/Alice-shy1.png", "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png",
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png",
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png"
    };

    private String[] names = { 
        " ", " ", "ฉัน", "ฉัน", "อริส", "อริส", "ฉัน", "อริส",
        "ฉัน", "อริส", "อริส", "อริส", "อริส", "อริส", "ฉัน", "อริส", "ฉัน",
        "ฉัน", "ฉัน", "ฉัน", " ", "ฉัน", "ฉัน","ปีศาจเดโม", "ปีศาจเดโม",
        "อริส", "ฉัน", "อริส", "อริส", "อริส", "ปีศาจเดโม", "ปีศาจเดโม", "ฉัน",
        "อริส", "อริส", "อริส","ปีศาจเดโม", "ฉัน", "อริส", "ฉัน", "อริส", " ",
        " ", " ", " ", " ", "ฉัน", "อริส", "ฉัน", "ฉัน", 
        "อริส", " ", "อริส", "ฉัน", "อริส", "อริส", "อริส","อริส","อริส"," ", " ",
    };
    
    private String[] dialogues = {
        "ตัวเรากับอริสที่กําลังอยู่ในป่า", 
        "ตัวเรากับอริสได้เดินทางออกจากหมู่บ้านมา 1 อาทิตย์เเล้ว", 
        "ฉันคิดว่าเราน่าจะออกมาไกลพอสมควรละนะ", 
        "ดีนะที่ก่อนหน้านี้ได้เเผนที่มาจากคนในหมู่บ้าน", 
        "อืออ",  
        "นายหิวรึยัง?", 
        "ก็..นิดหน่อยนะ", 
        "งั้นเรานั่งพักกินข้าวตรงนี้ก่อนมั้ย?", 
        "ก็ดีเหมือนกันนะ", 
        "นายอยากกินอะไรมั้ย?", 
        "นายนี่ละก็..", 
        "อื้อโอเค",
        "เข้าใจแล้ว",
        "เดี๋ยวฉันจะไปหาของเเถวนี้ก่อนนะ", 
        "ให้ฉันไปด้วยมั้ย?", 
        "ไม่เป็นไรหรอก นายนั่งรออยู่ตรงนี้แหละ", 
        "อือ..ก็ได้", 
        "ตอนนี้เราน่าจะยังอยู่ในเขตป่าLifeอยู่นะ", 
        "คงต้องใช้เวลาอีกนานเลยกว่าถึงจุดหมาย", 
        "ดีเลย เราจะได้ใช้เวลานี้อยู่กับอริสมากขึ้น", 
        "อร๊ายยยย", 
        "..นั่นมัน..เสียงอริสนี่!!", 
        "เกิดอะไรขึ้น อริส!!", 
        "กะจะเดินเล่นเฉยๆ ดันมาเจอมนุษย์ซะงั้น", 
        "เเต่ก็ดี ข้ากําลังหิวได้ที่เลย", 
        "…(ชื่อตัวละครเรา)!!", 
        "นี่เธอเบาดเจ็บตรงไหนรึปล่าว?", 
        "ไม่ ฉันไม่เป็นไร", 
        "อยู่ๆปีศาจมันก็เข้ามาโจมตีเเบบกระทันหัน", 
        "เเต่ก็พอหลบได้ เลยมีเเผลถลอกนิดหน่อยหนะ", 
        "นี่พ่อหนุ่ม ข้าขอเเม่หนูตรงนั้นได้ไหม?", 
        "เเล้วข้าจะไว้ชีวิตเจ้า", 
        "ไม่มีทาง!!", 
        "(เขิน)", 
        "มันใช่เวลามั้ย!! ไอ่บ้า!!", 
        "อ่าว…",
        "ยังไงข้าก็จะกินพวกเจ้าทั้งสองอยู่ดี", 
        "อริส ครั้งนี้ให้ฉันเป็นคนจัดการเอง", 
        "จะไม่เป็นไรหรอ?", 
        "เเค่นี้สบายมาก", 
        "งั้นฝากด้วยนะ", 
        "ตัวเราได้ใช้เวทย์โจมตี ปีศาจเดโมก่อน", 
        "ปีศาจเดโมหลบได้ เเละพุ่งโจมตีใส่เราทันที", 
        "เรากระโดดหลบ เเละปล่อยพลังเวทย์ที่รุนเเรงใส่ ปีศาจเดโม", 
        "ปีศาจเดโมโดนพลังเวทย์เต็มๆ", 
        "ปีศาจเดโม กลัวจะถูกกําจัด เลยใช้ม่านควันสีดําเเละหลบหนี", 
        "พลาดท่าจนได้", 
        "เเต่อย่างน้อยตอนนี้ก็คงจะปลอดภัยเเล้ว", 
        "ฉันคิดว่าปีศาจตัวเมื่อกี้ต้องเกี่ยวข้องกับจอมมารเเน่เลย", 
        "นี่เรา..ยังไม่ได้กินข้าวกันเลยนี่หน่า", 
        "จริงด้วย! งั้นเดี๋ยวฉันรีบไปทําให้นะ", 
        "หลังจากพักผ่อนเเละกินอะไรกันเสร็จเเล้ว", 
        "ฉันรู้สึกร้อนมากเลย..", 
        "จะไปอาบนํ้าไหมละ ดูเหมือนว่าจะมีลําธารใกล้ๆนะ", 
        "จริงหรอ งั้นฉันขอไปอาบนํ้าก่อน", 
        "คนลามก!!...", 
        "โอเค เดี๋ยวมานะ", 
        "รู้เเล้วน่า!!",
        "อริสกําลังอาบนํ้า", 
        "เเต่จู่ๆ ก็มีเสียง เเปลกๆที่พุ่มไม้", 
        "อริสเห็นเงาคนอยู่หลังพุ่มไม้..", 
    };

    public part5() {
        setTitle("ISEKAI DEMO - Part 5: The Journey Begins");
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

        playSE("res/sound/soundtrack8.wav", true, -10.0f);

        backgroundLabel = new JLabel() {
            @Override 
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
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
            if (isChoosing || isFinishing || isWaiting) return;
            
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

            if (currentIndex == 9) { 
                showChoices("ฉันกินได้หมดเลย ขอเเค่เป็นอาหารที่เธอทํา", "ฉันยังไงก็ได้", "อะไรก็ได้ไปเอามาเถอะ", 10, 11, 12);
                return; 
            }
            if (currentIndex == 10 || currentIndex == 11 || currentIndex == 12) { 
                currentIndex = 13; 
                updateScene(); 
                return; 
            }

            if (currentIndex == 32) { 
                showChoices("ฉันจะไม่ยกอริสให้เเกเด็ดขาด", "ไม่มีวัน เพราะอริสเป็นของฉันคนเดียวเท่านั้น", "เธอคนนี้มีดีตรงไหนกันนะ", 33, 34, 35);
                return; 
            }
            if (currentIndex == 33 || currentIndex == 34 || currentIndex == 35) { 
                currentIndex = 36; 
                updateScene(); 
                return; 
            }

            if (currentIndex == 54) { 
                showChoices("ให้ฉันไปอาบด้วยมั้ยหละ?", "เดี๋ยวฉันจะรอจรงนี้นะ มีอะไรก็เรียกได้เลย", "เร็วๆหน่อยละกัน ฉันไม่อยากรอนาน", 55, 56, 57); 
                return; 
            }
            if (currentIndex == 55 || currentIndex == 56 || currentIndex == 57) { 
                currentIndex = 58; 
                updateScene(); 
                return; 
            }

            if (currentIndex < dialogues.length - 1) {
                currentIndex++;
                updateScene();
            } else {
                finishGame(); 
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
        if (index >= 56) {
            stopBGM();
        }
        if (index == 57) playEffect("res/sound/ahhhhh.wav", -5.0f);
    }

    private void updateScene() {
        if (currentIndex >= names.length) nameLabel.setText(names[names.length-1]);
        else nameLabel.setText(names[currentIndex]);

        String currentBg = (currentIndex < imagePaths.length) ? imagePaths[currentIndex] : lastBgPath;
        if (!currentBg.equals(lastBgPath)) {
            backgroundLabel.setIcon(getOptimizedImage(currentBg, 1280, 800));
            startBackgroundFade();
            lastBgPath = currentBg;
        }

        String currentP1 = (currentIndex < charPaths.length) ? charPaths[currentIndex] : "res/empty.png";
        String prevP1 = (currentIndex > 0) ? charPaths[currentIndex - 1] : "res/empty.png";
        String currentP2 = (currentIndex < charPaths2.length) ? charPaths2[currentIndex] : "res/empty.png";
        String prevP2 = (currentIndex > 0) ? charPaths2[currentIndex - 1] : "res/empty.png";

        // --- จุดที่ 1: จัดการตัวละครด้วย Logic ใหม่ทั้งหมด ---
        handleTransition(characterLabel, prevP1, currentP1, true);
        handleTransition(characterLabel2, prevP2, currentP2, false);

        updateDialogueDisplay(dialogues[currentIndex]);
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    // ==========================================
    // --- ระบบจัดการ Transition ตัวละคร ---
    // ==========================================
    private Rectangle getCharacterBounds(String path, int index) {
        if (path.contains("demogigi")) {
            // --- แก้ไขตรงนี้: ลดค่า X ลงเพื่อให้ขยับไปทางซ้าย ---
            // ของเดิม: (index >= 41) ? 220 : (index >= 25 ? -100 : 100);
            // ของใหม่: ปรับให้ขยับซ้ายไปอีกประมาณ 150-200 พิกเซล
            int x = (index >= 41) ? 400 : (index >= 25 ? 400 : 400);
            
            int y = (index >= 41) ? -40 : -40;
            return new Rectangle(x, y, 1100, 1000);
            
        } else if (path.contains("Alice")) {
            int w = (index >= 46 || index >= 25) ? 1000 : 1000;
            int x = (index >= 46) ? 50 : (index >= 25 ? -120 : 50);
            int y = (index >= 46) ? 150 : (index >= 25 ? 150 : 150);
            return new Rectangle(x, y, w, 650);
        }
        return new Rectangle(80, 100, 1200, 800); // Default
    }

    private void handleTransition(JLabel label, String prev, String curr, boolean isChar1) {
        // 1. กรณีที่ตัวละครหายไป (Fade Out)
        if (!prev.contains("empty") && curr.contains("empty")) {
            animateFadeOut(label, prev, isChar1);
        } 
        // 2. กรณีที่ตัวละครปรากฏตัว (Slide + Fade In)
        else if (prev.contains("empty") && !curr.contains("empty")) {
            Rectangle target = getCharacterBounds(curr, currentIndex);
            // ปีศาจเลื่อนจากซ้าย, อริสเลื่อนจากขวา
            boolean fromLeft = curr.contains("demogigi"); 
            animateEntry(label, curr, target, fromLeft, isChar1);
        }
        // 3. กรณีที่แค่เปลี่ยนท่าทาง/อารมณ์ (Fade In แบบอยู่กับที่)
        else if (!curr.contains("empty") && !prev.equals(curr)) {
            Rectangle target = getCharacterBounds(curr, currentIndex);
            animateFadeInPlace(label, curr, target, isChar1);
        }
    }

    private void animateFadeOut(JLabel label, String path, boolean isChar1) {
        Timer timer = new Timer(40, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 400; // ความเร็วในการจางหาย

        timer.addActionListener(e -> {
            float progress = Math.min(1.0f, (float) (System.currentTimeMillis() - startTime) / duration);
            float alphaVal = 1.0f - progress;

            if (isChar1) charAlpha1 = alphaVal; else charAlpha2 = alphaVal;
            label.repaint();

            if (progress >= 1.0f) {
                timer.stop();
                label.setIcon(null); // ลบรูปออกเมื่อจางสนิท
            }
        });
        timer.start();
    }

    private void animateEntry(JLabel label, String path, Rectangle target, boolean fromLeft, boolean isChar1) {
        if (isChar1) charAlpha1 = 0.0f; else charAlpha2 = 0.0f;
        
        // จุดเริ่มต้นก่อนเลื่อน
        int startX = fromLeft ? target.x - 60 : target.x + 60;
        label.setIcon(getOptimizedImage(path, target.width, target.height));
        label.setBounds(startX, target.y, target.width, target.height);

        Timer animTimer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 500; // ความเร็วในการเลื่อนเข้า

        animTimer.addActionListener(e -> {
            float progress = Math.min(1.0f, (float) (System.currentTimeMillis() - startTime) / duration);
            
            // Fade In
            if (isChar1) charAlpha1 = progress; else charAlpha2 = progress;
            
            // Slide X
            int curX = (int) (startX + (target.x - startX) * progress);
            label.setBounds(curX, target.y, target.width, target.height);
            
            label.repaint();
            if (progress >= 1.0f) {
                animTimer.stop();
            }
        });
        animTimer.start();
    }

    private void animateFadeInPlace(JLabel label, String path, Rectangle target, boolean isChar1) {
        if (isChar1) charAlpha1 = 0.0f; else charAlpha2 = 0.0f;
        
        label.setIcon(getOptimizedImage(path, target.width, target.height));
        label.setBounds(target.x, target.y, target.width, target.height);

        Timer timer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 300; // ความเร็วในการเปลี่ยนรูป (เร็วหน่อยจะได้ไม่สะดุด)

        timer.addActionListener(e -> {
            float progress = Math.min(1.0f, (float) (System.currentTimeMillis() - startTime) / duration);
            
            if (isChar1) charAlpha1 = progress; else charAlpha2 = progress;
            label.repaint();

            if (progress >= 1.0f) {
                timer.stop();
            }
        });
        timer.start();
    }
    // ==========================================

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
        if (fadeOverlay.getParent() == null) {
            layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        }
        
        alpha = 0.0f; 
        Timer fadeTimer = new Timer(30, e -> {
            alpha += 0.05f; 
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                ((Timer)e.getSource()).stop();
                if (onComplete != null) onComplete.run(); 
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
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton3, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            private double scale = 1.0;
            private int alphaMod = 180; 
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
                    public void mouseEntered(MouseEvent e) {
                        startAnimation(1.05, 230); 
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        startAnimation(1.0, 180); 
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        scale = 0.95; 
                        repaint();
                    }
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

        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            layeredPane.remove(choiceButton3);
            isChoosing = false; 

            if (target == 10 || target == 33 || target == 55) {
                relationdata.aliceRel.addAffinity(10);
            } else if (target == 11 ){
                // System.out.println("คะแนนเท่าเดิม");
            } else if (target == 12 || target == 35 || target == 55){
                relationdata.aliceRel.decreaseAffinity(5); 
            } else if (target == 34 || target == 56){
                relationdata.aliceRel.addAffinity(5);
            } else if (target == 57){
                relationdata.aliceRel.decreaseAffinity(10); 
            }

            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                networkOut.println("SYNC_INDEX:" + target);
            }

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
        JPanel relPanel = new JPanel(new GridLayout(2, 1, 0, 0)); 
        relPanel.setBounds(0, 0, 280, 75); 
        relPanel.setBackground(new Color(0, 0, 0, 190)); 
        relPanel.setOpaque(true);

        relPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 105, 180), 2), 
            BorderFactory.createEmptyBorder(5, 15, 5, 10) 
        ));

        affinityLabel = new JLabel("อริส: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); 
        affinityLabel.setForeground(new Color(255, 192, 203)); 

        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE); 

        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        
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
                layeredPane.remove(fadeOverlay); 
                updateScene(); 
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void finishGame() {
        if (isFinishing) return; 
        isFinishing = true;      

        startFadeOut(() -> {
            stopAllSounds(); 
            
            SwingUtilities.invokeLater(() -> {
            	if (relationdata.isOnlineMode) {
                    showWaitPoint(); 
                } else {
                    goToNextPart(); 
                }
            });
        });
    }

    private void startBackgroundFade() {
        bgAlpha = 0.0f; 
        if (bgFadeTimer != null && bgFadeTimer.isRunning()) bgFadeTimer.stop();
        
        bgFadeTimer = new Timer(40, e -> {
            bgAlpha += 0.04f; 
            if (bgAlpha >= 1.0f) {
                bgAlpha = 1.0f;
                ((Timer)e.getSource()).stop();
            }
            backgroundLabel.repaint();
        });
        bgFadeTimer.start();
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode || relationdata.globalSocket == null) return;
        new Thread(() -> {
            try {
                // *** ดึงข้อมูลจากส่วนกลาง ไม่ต้อง new Socket ใหม่ ***
                networkOut = relationdata.globalOut;
                java.io.BufferedReader in = relationdata.globalIn;

                String line;
                while ((line = in.readLine()) != null) {
                        if (line.startsWith("LOAD_AFFINITY:")) {
                            int score = Integer.parseInt(line.substring(14));
                            relationdata.aliceRel.setAffinity(score);
                            SwingUtilities.invokeLater(() -> {
                                affinityLabel.setText("อริส: " + score); 
                                statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
                            });
                        } else if (line.startsWith("ALL_STATS:")) {
                        updateLeaderboardUI(line.substring(10));
                    }
                    else if (line.equals("PROCEED_TO_NEXT")) {
                    	goToNextPart();
                        break; // *** สำคัญ! ต้องมี break เช่นกัน ***
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
                String rawScores = parts[1]; 
                
                String aliceScore = rawScores;
                if (rawScores.contains("/")) {
                    aliceScore = rawScores.split("/")[0]; 
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
    
    private void showWaitPoint() {
        isWaiting = true;
        waitOverlay = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 220)); 
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        waitOverlay.setBounds(0, 0, 1280, 800);
        waitOverlay.setOpaque(false);
        JLabel msg = new JLabel("WAITING FOR PLAYERS...", SwingConstants.CENTER);
        msg.setFont(new Font("Monospaced", Font.BOLD, 40)); 
        msg.setForeground(Color.WHITE);
        msg.setBounds(0, 350, 1280, 100);
        waitOverlay.add(msg);
        layeredPane.add(waitOverlay, JLayeredPane.DRAG_LAYER);
        layeredPane.moveToFront(waitOverlay);
        if (networkOut != null) networkOut.println("READY_FOR_NEXT");
        revalidate(); repaint();
    }

    private void goToNextPart() {
        SwingUtilities.invokeLater(() -> {
            new part6().setVisible(true); 
            dispose(); 
        });
    }
}