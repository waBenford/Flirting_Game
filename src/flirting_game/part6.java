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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;

public class part6 extends JFrame {
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

    private PrintWriter networkOut;
    
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    private String[] imagePaths = {
       "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png",
       "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s1new.png",
       "res/scene6/s1new.png", "res/scene6/s1new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", //0-11
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", //12-23
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", //24-35
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", 
       "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", "res/scene6/s2new.png", //36-43
    };
    
    private String[] charPaths = {
       "res/scene5/Alice-shower2.png", "res/scene5/Alice-shower2.png", "res/scene5/Alice-shower2.png", "res/Charactor/Dan/dan-showhand1.png",
       "res/Charactor/Dan/dan-showhand1.png", "res/scene5/Alice-shower3.png", "res/scene5/Alice-shower3.png", "res/scene5/Alice-shower3.png", 
       "res/scene5/Alice-shower1.png", "res/scene5/Alice-shower1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png",//0-11
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Alice/Girl/Alice-shy2.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", //12-23
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png",
       "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", //24-35
       "res/Charactor/Alice/Girl/Alice-shy2.png","res/Charactor/Alice/Girl/Alice-shy1.png", "res/Charactor/Dan/dan-normal2.png","res/Charactor/Dan/dan-normal2.png", 
       "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", 
       "res/Charactor/Alice/Girl/Alice-normal2.png", "res/empty.png", "res/empty.png", "res/empty.png", //36-47
    };
    
    private String[] names = { 
    		"อริส", "อริส", "อริส", "คนลึกลับ", "ฉัน", "อริส", "ฉัน", "อริส", 
            "อริส", "อริส", "ฉัน", "Dan", "Dan", "อริส", "อริส", "อริส", //0-16
            "Dan", "Dan", "ฉัน", "ฉัน", "Dan", "Dan", "ฉัน", "ฉัน", 
            "ฉัน", "ฉัน", "ฉัน", "ฉัน", "Dan", "Dan", "Dan", "Dan", //17-32
            "ฉัน", "อริส", "อริส", "ฉัน", "อริส","อริส", "Dan", "Dan", //33-40
            "ฉัน", "Dan", "Dan", "ฉัน", "อริส", " ", " ", " ", //41-48
    		};
    
    private String[] dialogues = {
        "ใครอยู่ตรงนั้น!?", "ออกมาเดี๋ยวนี้นะ!!", "ถ้าไม่ออกมา ฉันจะใช้พลังเวทย์ใส่เเกทั้งเเบบนี้เเหละ", 
        "ใจเย็นก่อน ฉันไม่ได้คิดร้าย", "อริส เกิดอะไรขึ้น!!", "นายไม่ต้องวิ่งมาขนาดนั้นก็ได้ ฉันไม่ได้เป็นอะไรสักหน่อย",
        "ก็ฉันได้ยินเสียงเธอร้องนี่", "นายเป็นห่วงฉันขนาดนั้นเลยหรอ?", 
        "นะ…นายพูดอะไรแบบนั้นกัน…","อือ ก็จริงของนาย", //choice 1-2 //0-9
        "สรุปเเล้วนายเป็นใครกัน?", "ฉันชื่อ Dan เป็นนักผจญภัยหนะ", "พวกเธอสองคนเป็นคู่รักกันหรอ?", 
        "ดะ…เดี๋ยวสิ! ใครบอกว่าเป็นแบบนั้น!", "นะ…นายพูดอะไรของนายเนี่ย!!", //choice 3-4
        "เเล้วทําไมเมื่อกี้ต้องซ่อนด้วย", "ฉันไม่ได้จะเเอบดูเธอหรอกนะ ฉันเเค่เดินผ่านมา", "เเล้วพวกเธอหละชื่ออะไร กําลังจะไปที่ไหนกัน?", //10-17
        "ฉันชื่อ..(ชื่อตัวละครเรา) ส่วนนี่ก็ อริส", "พวกเรากําลังจะมุ่งหน้าไปที่ป่า Death End", "Death End หรอ..",
        "มีเหตุผลอะไรที่พวกเธอต้องไปที่เเบบนั้นหรอ?", "ช่วงนี้เริ่มมีปีศาจโจมตี ในหลายๆพื้นที่", "เเละดูเหมือนว่าจะมีคนที่คอยสั่งเจ้าพวกนั้น",
        "คนที่สามารถสั่งเจ้าพวกนั้นได้คงต้องเป็นคนที่เเข็งเเกร่งมากเเน่ๆ", "คนเดียวที่ทําเเบบนั้นได้ คือจอมมาร", //18-25
        "เพราะเเบบนั้นพวกเราเลยออกเดินทางเพื่อไปยังที่อยู่ของจอมมาร","เเหละจบเรื่องนี้ จะได้ไม่มีผู้คนต้องบาดเจ็บ",
        "มันค่อนข้างอันตรายนะ", "ป่า Death End เป็นป่าที่มีความซับซ้อนของเส้นทาง", 
        "อีกทั้งยังมีปีศาจเเละต้นไม้อาถรรพ์ที่สามารถทําร้ายเราได้ตลอดเวลา","ฉันก็เคยเข้าไปครั้งนึง เเต่ก็สามารถรอดออกมาได้", //26-31
        "งั้นนายช่วยมาร่วมเดินทางกับพวกเราหน่อยจะได้มั้ย", "นี่นายเเน่ใจเเล้วหรอ?", "หมอนั่นอาจจะเป็นคนไม่ดีก็ได้นะ",
        "ไม่เป็นไรหรอก ดูเเล้วคนๆนี้ก็ไม่น่ามีพิษภัยอะไร", 
        "เข้าใจเเล้ว...","เอาตามนั้นก็ได้", //choice 5-6 //32-37
        "ดูเหมือนพวกเธอจะสนิทกันดีนะ", "ถ้าอย่างนั้น...ฉันจะนําทางพวกเธอไปที่ป่า Death End เอง","จริงหรอ!?",
        "แต่เส้นทางมันยาวนะ พวกเธอคงต้องเตรียมตัวให้พร้อม", "เพราะถ้าเข้าไปในป่านั้นแล้ว...จะไม่มีทางถอยกลับง่ายๆ",
        "ไม่เป็นไรหรอก พวกเราตัดสินใจแล้ว", "อือ!","หลังจากนั้น พวกเราเริ่มออกเดินทางไปยังป่า Death End",
        "การเดินทางที่ยาวนานได้เริ่มต้นขึ้น", "เวลาผ่านไปหลายสัปดาห์...", //37-47
                        
    };

    public part6() {
        setTitle("ISEKAI DEMO - Part 6: Hidden Shadow");
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

        playSE("res/sound/soundtrack12.wav", true, -10.0f);
        playEffect("res/sound/ahhhhh.wav", 0.0f);

        // 1. พื้นหลัง
        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // 2. ตัวละคร (พิกัดมาตรฐาน 190, 100, 900, 900)
        characterLabel = new JLabel();
        characterLabel.setBounds(190, 100, 900, 900); 
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        initNetwork();
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNext();
            }
        });
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

    private void updateScene() {
        if (currentIndex < names.length) {
            nameLabel.setText(names[currentIndex]);
        } else {
            nameLabel.setText(""); // ป้องกัน Error ถ้าใส่ชื่อไม่ครบ
        }
        
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1280, 800));
        
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            
            // ตรวจสอบว่าถ้าเป็นภาพ Dan ให้ปรับความกว้างเพิ่มขึ้นเพื่อไม่ให้ภาพดูบีบ
            if (path.contains("dan-normal2")) {
                characterLabel.setBounds(-60, 100, 1500, 1000); 
                characterLabel.setIcon(scaleImage(path, 1500, 1000));
            } else if (path.contains("dan")) { 
                characterLabel.setBounds(-50, 100, 1400, 1000);
                characterLabel.setIcon(scaleImage(path, 1400, 1000));
            } else {
                characterLabel.setBounds(40, 100, 1200, 900);
                characterLabel.setIcon(scaleImage(path, 1200, 900));
            }
        }
        stopEffect();
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    private void handleNext() {
        // 1. ป้องกันการคลิกซ้ำขณะกำลังเลือกตอบ
        if (isChoosing) return;

        // 2. ถ้าตัวอักษรกำลังพิมพ์อยู่ ให้หยุดและแสดงข้อความเต็มทันที
        if (isTyping) {
            stopTypewriter();
            dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        // 3. ระบบ Choice Logic สำหรับ Part 6
        // Choice 1: อริสถามเรื่องความสำคัญ
        if (currentIndex == 7) {
            showChoices("ก็เธอสําคัญกับฉันนี่", "ใครๆก็ต้องช่วยเพื่อนอยู่แล้ว", 8, 9);
            return;
        }

        // Choice 2: Dan ถามว่าเป็นคู่รักกันหรอ
        if (currentIndex == 12) {
            showChoices("ตอนนี้อาจจะยังไม่ใช่เเต่อนาคตไม่เเน่", "ถ้าเธออยากเป็นก็ได้นะ", 13, 14);
            return;
        }

        // Choice 3: การตัดสินใจเดินทางร่วมกับ Dan
        if (currentIndex == 35) {
            showChoices("ถ้าเกิดอะไรขึ้น ฉันจะปกป้องเธอเอง", " เพราะงั้น ไม่ต้องห่วงหรอก", 36, 37);
            return;
        }

        // 4. การกระโดดข้าม Index หลังจากเลือกตอบเสร็จ (เพื่อข้ามฉากของตัวเลือกอื่น)
        int nextIndex = currentIndex;
        if (currentIndex == 8 || currentIndex == 9) nextIndex = 10;
        else if (currentIndex == 13 || currentIndex == 14) nextIndex = 15;
        else if (currentIndex == 36 || currentIndex == 37) nextIndex = 38;
        else nextIndex = currentIndex + 1;

        // 5. ตรวจสอบว่ายังไม่จบ Part
        if (nextIndex < dialogues.length) {
            currentIndex = nextIndex;

            // --- ส่วนสำคัญ: ส่งเลขฉากปัจจุบันไปหาเพื่อนคนอื่นในวง Online ---
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("SYNC_INDEX:" + currentIndex);
            }

            updateScene();
        } else {
            // เมื่อจบ Part 6 (สามารถเปลี่ยนเป็นเปิด Part 7 ได้เลย)
            finishGame();
        }
    }

    private void handleSoundEffects(int index) {
        if (index == 8){
            playEffect("res/sound/baka.wav", 5.0f);
        }
        if (index == 9){
            playEffect("res/sound/emmm.wav", 5.0f);
        } 
        if (index == 13){
            playEffect("res/sound/choochoto.wav", 5.0f);
        }
        if (index == 14){
            playEffect("res/sound/Baka janai no.wav", 5.0f);
        }
        if (index == 37){
            playEffect("res/sound/wakaarunai.wav", 5.0f);
        }
        if (index == 38){
            playEffect("res/sound/soredeiikedo.wav", 5.0f);
        }
        if (index == 44){
            playEffect("res/sound/emmm.wav", 5.0f);
        }
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 380, t1); //y: ขึ้น=ลง
        choiceButton2 = createChoiceButton(text2, 450, t2); //y: ขึ้น=ลง
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
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

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) imageCache.put(key, scaleImage(path, w, h));
        return imageCache.get(key);
    }

    public void playEffect(String path, float volume) {
        try {
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                stopEffect();

                Clip temporaryClip = AudioSystem.getClip(); 
                temporaryClip.open(audioIn);

                FloatControl gainControl = (FloatControl) temporaryClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                temporaryClip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        
        new Thread(() -> {
            try {
                Socket socket = new Socket(relationdata.serverIP, 5000);
                networkOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("SYNC_INDEX:")) {
                        int remoteIndex = Integer.parseInt(line.substring(11));
                        SwingUtilities.invokeLater(() -> {
                            if (remoteIndex != currentIndex) {
                                currentIndex = remoteIndex;
                                updateScene();
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
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
                dialogueArea.setText("<html><body style='width: 750px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else {
                stopTypewriter();
            }
        });
        typewriterTimer.start();
    }

    private void stopTypewriter() {
        if (typewriterTimer != null) typewriterTimer.stop();
        isTyping = false;
    }

    private void finishGame() {
        stopAllSounds();
        UIManager.put("OptionPane.messageFont", THAI_FONT);
        JOptionPane.showMessageDialog(null, "End Part 6!");
        System.exit(0);
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part6().setVisible(true));
    }
}

class VisualNovelBox extends JPanel {
    private int cornerRadius;

    public VisualNovelBox() {
        this.cornerRadius = 30;
        setOpaque(false);
    }

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