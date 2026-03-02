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
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();
    
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

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
       "res/Charactor/Nebula/Nebula-shy1.png", "res/Charactor/Nebula/Nebula-shy2.png", "res/Charactor/Nebula/Nebula-normal1.png","res/Charactor/Nebula/Nebula-normal2.png", 
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
        setTitle("ISEKAI DEMO - Part 8 (Dual Character)");
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

        // --- เพิ่ม Label 2 ตัวสำหรับซ้ายและขวา ---
        leftCharLabel = new JLabel();
        layeredPane.add(leftCharLabel, JLayeredPane.PALETTE_LAYER);

        rightCharLabel = new JLabel();
        layeredPane.add(rightCharLabel, JLayeredPane.PALETTE_LAYER);

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
            String currentName = names[currentIndex];

            if (path.contains("empty.png")) {
                leftCharLabel.setIcon(null);
                rightCharLabel.setIcon(null);
            } else {
                // ขนาดตัวละคร
                int w = 900, h = 900; 
                
                // Logic การแยกข้าง: 
                // ถ้าเป็น Nebula ให้ไปอยู่ขวา (Right)
                // ถ้าเป็น Dan, อริส หรือ "ฉัน" ให้ไปอยู่ซ้าย (Left)
                if (path.contains("Nebula")) {
                    rightCharLabel.setBounds(500, 50, w, h);
                    rightCharLabel.setIcon(getOptimizedImage(path, w, h));
                    // (Optional) สั่งให้ตัวละครอื่นจางลง หรือหายไปเมื่อ Nebula พูด
                    // leftCharLabel.setIcon(null); 
                } else {
                    int specialW = path.contains("dan") ? 1400 : 1200;
                    int specialH = path.contains("dan") ? 1000 : 950;
                    leftCharLabel.setBounds(-250, 50, specialW, specialH);
                    leftCharLabel.setIcon(getOptimizedImage(path, specialW, specialH));
                    // rightCharLabel.setIcon(null);
                }
            }
        }
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    // --- ส่วน Method อื่นๆ คงเดิมตามโครงสร้างคุณ ---
    // (createChoiceButton, handleNext, setupDialogueUI, etc.)
    // ... [โค้ดส่วนที่เหลือของคุณ] ...

    private void handleNext() {
        if (isChoosing) return;
        if (isTyping) {
            stopTypewriter();
            dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        // Choice Logic เดิมของคุณ
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
        dialogueArea.setBounds(60, 60, 700, 110);
        dialoguePanel.add(dialogueArea);

        JLabel nextArrow = new JLabel("▼");
        nextArrow.setBounds(750, 130, 30, 30);
        dialoguePanel.add(nextArrow);
        new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible())).start();
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 380, t1);
        choiceButton2 = createChoiceButton(text2, 450, t2);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        repaint();
    }

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
                    public void mouseEntered(MouseEvent e) { startAnimation(1.05, 200); }
                    public void mouseExited(MouseEvent e) { startAnimation(1.0, 150); }
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
        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false; currentIndex = target; updateScene();
        });
        return btn;
    }

    // --- ระบบเสียงและ Typewriter เหมือนเดิม ---
    private void startTypewriter(String text) {
        stopTypewriter();
        isTyping = true;
        charIndex = 0;
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 700px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else { stopTypewriter(); }
        });
        typewriterTimer.start();
    }

    private void stopTypewriter() { if (typewriterTimer != null) typewriterTimer.stop(); isTyping = false; }

    private void handleSoundEffects(int index) {
        if (index == 28) playEffect("res/sound/Darega.wav", 5.0f);
        if (index == 29) playEffect("res/sound_nebula/Yakimochi.wav", 0.0f);
        if (index == 51) playEffect("res/sound_nebula/Kisama.wav", 0.0f);
        if (index == 52) playEffect("res/sound_nebula/Nani wo.wav", 0.0f);
        if (index == 56) playEffect("res/sound_nebula/Souka.wav", 0.0f);
    }

    public void playBGM(String path, float volume) {
        try {
            if (bgmClip != null && bgmClip.isRunning()) return;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioIn);
            ((FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void playEffect(String path, float volume) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume);
            clip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) {
            try {
                Image img = new ImageIcon(path).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                imageCache.put(key, new ImageIcon(img));
            } catch (Exception e) { return null; }
        }
        return imageCache.get(key);
    }

    private void finishGame() {
        JOptionPane.showMessageDialog(null, "จบ Part 8! กำลังเดินทางไป Grey...");
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part8().setVisible(true));
    }
}

// คลาส VisualNovelBox ไว้ท้ายไฟล์เหมือนเดิม
class VisualNovelBox extends JPanel {
    public VisualNovelBox() { setOpaque(false); }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 250, 255, 180), 0, getHeight(), new Color(255, 235, 245, 230));
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2d.setColor(new Color(255, 150, 200, 200));
        g2d.setStroke(new BasicStroke(4f));
        g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 30, 30);
    }
}