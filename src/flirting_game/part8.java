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
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel; 
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

    private String[] imagePaths = {
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", //0-11
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", //12-19
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", //20-31
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s2.png", //32-43
       "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", 
       "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", //44-51
       "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", 
       "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", "res/scene8/s2.png", 
       "res/scene8/s2.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", //52-63
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", "res/scene8/s1.png", 
       "res/scene8/s1.png", //64-72
       
    };
    
    // ใช้ charPaths ชุดเดียวตามที่คุณชมพู่ต้องการ
    private String[] charPaths = { 
       "res/empty.png","res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",  "res/Charactor/Nebula/Nebula-normal2.png", //0-12
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png","res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png","res/Charactor/Nebula/Nebula-normal1.png", //13-20
       "res/Charactor/Dan/dan-normal2.png",  "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-shy2.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", //21-32 
       "res/Charactor/Nebula/Nebula-shy1.png", "res/Charactor/Nebula/Nebula-shy2.png", "res/Charactor/Nebula/Nebula-normal1.png","res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", //33-41
       "res/Charactor/Nebula/Nebula-normal1.png", "res/empty.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", //42-49
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-shy2.png", "res/Charactor/Nebula/Nebula-shy1.png","res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-shy1.png", "res/Charactor/Nebula/Nebula-shy2.png","res/Charactor/Nebula/Nebula-normal1.png", //50-57
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/empty.png", 
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png","res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png","res/Charactor/Nebula/Nebula-normal2.png","res/Charactor/Nebula/Nebula-shy1.png",
       "res/Charactor/Nebula/Nebula-normal1.png","res/Charactor/Nebula/Nebula-normal2.png","res/Charactor/Nebula/Nebula-normal1.png","res/Charactor/Nebula/Nebula-normal2.png",
    
    
    };

    private String[] names = { 
    		" ","Dan","อริส","Nebula","Nebula","Nebula","ฉัน","Nebula",
            "Nebula","Dan","อริส","Nebula","Nebula","Nebula","Nebula","ฉัน",//0-15
            "Nebula","ฉัน","Nebula","Nebula","Nebula","Dan","อริส","Nebula",
            "Nebula","Nebula","อริส","Dan","อริส","Nebula","Nebula","ฉัน",//16-31
            "Nebula","Nebula","Nebula","Nebula","Nebula","Nebula","Dan","อริส",
            "Dan","Nebula","Nebula"," ","ฉัน","Nebula","ฉัน","Nebula",//32-47
            "Nebula","ฉัน","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula",
            "Nebula","Nebula","Nebula","Nebula","Nebula"," ","Dan","อริส", //48-63
            "ฉัน","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula","Nebula",//64-72


    };
    
    private String[] dialogues = {
    		"หลังจากการต่อสู้จบลง บรรยากาศในปราสาทก็กลับมาเงียบสงบอีกครั้ง", "ไม่อยากเชื่อเลยว่าเราจะมายืนคุยกับจอมมารแบบนี้ได้", 
            "ฉันก็ยังไม่ค่อยเชื่อเหมือนกัน...", "พวกเจ้ามนุษย์นี่แปลกจริงๆ", "ปกติแล้วมนุษย์ที่มาที่นี่ จะตัวสั่นด้วยความกลัว", 
            "แต่พวกเจ้ากลับยืนคุยกันสบายๆ", "ก็เพราะเธอไม่ได้ดูเหมือนศัตรูของพวกเรานี่", "หืม?", 
            "เจ้ากล้าพูดกับจอมมารแบบนั้นเลยงั้นหรอ", "นายพูดกับจอมมารเหมือนคุยกับคนปกติเลยนะ","นะ...นายไม่กลัวเลยรึไง", //0-10
            "หึ...มนุษย์ที่คิดแบบนี้ไม่ค่อยมีหรอกนะ", //choice 1 11
            "เจ้าดูเข้าใจสถานการณ์ดีนี่", //choice 2 12
            "เจ้าพวกมนุษย์...", "ทําไมถึงกล้าเดินทางมาถึงที่นี่", "ก็เพราะเราอยากหยุดเรื่องทั้งหมดนี่", "เพื่อมนุษย์คนอื่นงั้นหรอ", 
            "ก็ประมาณนั้น", "มนุษย์ส่วนใหญ่เห็นแก่ตัว", "แต่เจ้ากลับเสี่ยงชีวิตเพื่อคนอื่น","เจ้าคนนี้น่าสนใจจริงๆ", 
            "เหมือนจอมมารกําลังชมอยู่นะเนี่ย", "นายอย่าทําตัวสนิทกับจอมมารเกินไปสิ!", //13-22
            "หึ...เจ้าคนนี้ไม่ถ่อมตัวเลยนะ", //choice 1 23
            "ใครจะไปชมเจ้ากันเล่า", //choice 2 24
            "แต่ก็ไม่ปฏิเสธหรอกนะ", "นี่พวกนายคุยกันสนิทเกินไปแล้วนะ!", "ดูเหมือนจะมีคนเริ่มหึงแล้ว", "ขะ...ใครหึงกัน!",
            "หึงงั้นหรอ?","หรือว่าเจ้าจะ..หึๆ..", "อย่าแกล้งอริสมากนักสิ", //25-31
            "ข้าก็แค่พูดเล่นเท่านั้นเอง", //choice 1 32
            "ก็เพราะพวกเจ้าดูน่าสนุกนี่", //choice 2 33
            "มนุษย์...", "ปกติข้าไม่ค่อยให้ใครอยู่ในปราสาทนานนัก", "แต่พวกเจ้าดูต่างออกไป", "ข้าจะให้พวกเจ้าพักที่นี่คืนนี้ก็ได้", 
            "จริงหรอเนี่ย", "พักในปราสาทจอมมาร...","นี่มันประสบการณ์แปลกๆจริงๆ", //34-40
            "ข้าไม่ได้ใจดีหรอก", //choice 1 41
            "เจ้านี่พูดเก่งจริงๆ", //choice 2 42
            "คืนนั้น ฉันออกมาเดินเล่นที่ระเบียงของปราสาท", "ลมเย็นดีแฮะ", "เจ้าก็มาที่นี่เหมือนกันสินะ", "อ้าว เธอก็อยู่ที่นี่หรอ", 
            "ข้ามักจะมามองป่าตอนกลางคืน", "มันเงียบดี", "เธอดูไม่เหมือนจอมมารเลยนะ", "มนุษย์คิดว่าจอมมารต้องเป็นยังไงล่ะ", //50
            "เจ้ากล้าพูดกับจอมมารแบบนั้นอีกแล้วนะ", //choice 1 51
            "เจ้าพูดเเบบนี้อีกเเล้วนะ...", //choice 2 52
            "เจ้าคนนี้...", "ไม่กลัวข้าเลยจริงๆสินะ", //43-54
            "หึ…", //choice 1 55
            "งั้นเหรอ...", //choice 2 56
            "มนุษย์อย่างเจ้าแปลกจริงๆ", "แต่ก็ไม่ได้น่ารําคาญ", "ตรงกันข้าม...","ข้ากลับรู้สึกว่าเจ้าค่อนข้างน่าสนใจ",
            "เช้าวันต่อมา...","ได้เวลาออกเดินทางแล้วสินะ","เป้าหมายต่อไปคือ Grey","ใช่",
            "มนุษย์...","ถ้าเจ้าจะไปสู้กับ Grey","ก็อย่าตายซะก่อนล่ะ", //57-67
            "หึ...", //choice 1 68
            "ถ้าเจ้ารอดกลับมาได้จริง",//choice 2 69
            "ข้าจะรอดู","มนุษย์...","เจ้าเป็นคนที่น่าสนใจจริงๆ", //72

    };

    public part8() {
        setTitle("ISEKAI DEMO - Part 8");
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
            if (currentIndex == 10) {
                showChoices("ก็เธอไม่ได้ทําอะไรพวกเรานี่", "ถ้าเธออยากฆ่าพวกเรา เราคงตายไปแล้ว", 11, 12);
                return;
            }
            if (currentIndex == 11) { currentIndex = 13; updateScene(); return; }

            // Choice 2: 
            if (currentIndex == 22) {
                showChoices("ฉันก็แค่พูดตามที่คิด", "หรือว่าเธอกําลังชมฉันอยู่?", 23, 24);
                return;
            }
            if (currentIndex == 23) { currentIndex = 25; updateScene(); return; }

            // Choice 3: 
            if (currentIndex == 31) {
                showChoices("เดี๋ยวเธอร้องไห้ขึ้นมาจะทํายังไง", "เธอแกล้งคนอื่นสนุกนักรึไง", 32, 33);
                return;
            }
            if (currentIndex == 32) { currentIndex = 34; updateScene(); return; }
            
            if (currentIndex == 40) {
                showChoices("งั้นคืนนี้ฉันจะรบกวนหน่อยนะ", "เธอใจดีกว่าที่คิดนะ", 41, 42);
                return;
            }
            if (currentIndex == 41) { currentIndex = 43; updateScene(); return; }

            if (currentIndex == 50) {
                showChoices("น่ากลัวกว่านี้มั้ง", "ไม่สวยขนาดนี้แน่ๆ", 51, 52);
                return;
            }
            if (currentIndex == 51) { currentIndex = 53; updateScene(); return; }

            if (currentIndex == 54) {
                showChoices("ก็เธอไม่ได้น่ากลัวนี่", "เพราะฉันเริ่มชินกับเธอแล้วมั้ง", 55, 56);
                return;
            }
            if (currentIndex == 55) { currentIndex = 57; updateScene(); return; }

            if (currentIndex == 67) {
                showChoices("ถ้าฉันรอดกลับมา...ฉันจะมาเจอเธออีก", "ถ้าฉันรอดกลับมา เธอต้องเลี้ยงข้าวฉัน", 68, 69);
                return;
            }
            if (currentIndex == 68) { currentIndex = 70; updateScene(); return; }


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

    private void handleSoundEffects(int index) {
        if (index == 28){
            playEffect("res/sound/Darega.wav", 0.0f);
        }
        if (index == 29){
            playEffect("res/sound_nebula/Yakimochi.wav", 0.0f);
        }
        if (index == 51){
            playEffect("res/sound_nebula/Kisama.wav", 0.0f);
        }
        if (index == 52){
            playEffect("res/sound_nebula/Nani wo.wav", 0.0f);
        }
        if (index == 56){
            playEffect("res/sound_nebula/Souka.wav", 0.0f);
        }
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
                imageCache.put(key, new ImageIcon(icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH)));
            } catch (Exception e) { return null; }
        }
        return imageCache.get(key);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part8().setVisible(true));
    }
}

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