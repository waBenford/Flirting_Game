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
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // ตัวละครจะถูกตั้งค่า Bounds ใหม่ใน updateScene
        characterLabel = new JLabel();
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
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
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(getOptimizedImage(imagePaths[currentIndex], 1280, 800));
        
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("empty.png")) {
                characterLabel.setIcon(null);
            } else {
                int charW, charH, charX, charY;

                // ใช้โครงสร้าง if - else if - else เพื่อให้เลือกทำงานแค่อย่างเดียว
                if (path.contains("Nebula")) {
                    // ปรับ Nebula ให้ดูตัวใหญ่และสูงขึ้น (จอมมาร)
                    charW = 900; 
                    charH = 900;
                    charX = (1280 - charW) / 2;
                    charY = 50; // ดันขึ้นบนเพื่อให้เห็นความสูง
                } else if (path.contains("dan")) {
                    // สำหรับ Dan (รวม dan-normal2 ไว้ในเงื่อนไขนี้ได้เลยเพราะมีคำว่า dan เหมือนกัน)
                    charW = 1400;
                    charH = 1000;
                    charX = (1280 - charW) / 2;
                    charY = 60; 
                } else {
                    // สำหรับ อริส และตัวละครทั่วไป
                    charW = 1200;
                    charH = 950;
                    charX = (1280 - charW) / 2;
                    charY = 50;
                }

                characterLabel.setBounds(charX, charY, charW, charH);
                characterLabel.setIcon(getOptimizedImage(path, charW, charH));
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

        btn.setBounds(800, y, 350, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setForeground(new Color(45,65,115)); // สีข้อความ
        btn.setBackground(new Color(255, 255, 255, 150));  
        // ตั้งค่าเพื่อให้วาดปุ่มแบบกำหนดเองได้
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // ปิดการวาดขอบสี่เหลี่ยมเดิม
    
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 2)); 
        btn.addActionListener(e -> {
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false; currentIndex = target; updateScene(); 
        });
        return btn;
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
                alpha = 0; ((Timer)e.getSource()).stop();
                layeredPane.remove(fadeOverlay); updateScene(); 
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