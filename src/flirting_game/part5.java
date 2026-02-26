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

public class part5 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, characterLabel2, dialogueArea, nameLabel;
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
    
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 24);

    private String[] imagePaths = {
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png",
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
       "res/scene5/s2.png", "res/scene5/s2.png", "res/scene5/s2.png", "res/scene5/s2.png", 
       "res/scene5/s2.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s4.png", 
       "res/scene5/s4.png", "res/scene5/s4.png", "res/scene5/s4.png", "res/scene5/s4.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s5.png",
       "res/scene5/s5.png", "res/scene5/s5.png", 
    };

    private String[] charPaths = {
        "res/empty.png", "res/empty.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-shy1.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png",
        "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/empty.png", "res/empty.png", 
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/empty.png",
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-shy1.png",
        "res/scene5/Alice-normal2.png", "res/scene5/Alice-shower1.png", "res/scene5/Alice-shower3.png", "res/scene5/Alice-shower2.png",
    };

    private String[] charPaths2 = {
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png", "res/scene5/Alice-fight1.png",
        "res/scene5/Alice-fight2.png", "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png",
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-shy1.png", "res/scene5/Alice-shy2.png",
        "res/scene5/Alice-fight2.png", "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png",
        "res/scene5/Alice-fight1.png", "res/scene5/Alice-fight2.png",
    };

    private String[] names = { 
        " ", " ", "ฉัน", "ฉัน", "อริส", "อริส", "ฉัน", "อริส",
        "ฉัน", "อริส", "อริส", "อริส", "อริส", "ฉัน", "อริส", "ฉัน",
        "ฉัน", "ฉัน", "ฉัน", " ", "ฉัน", "ฉัน","ปีศาจเดโม", "ปีศาจเดโม",
        "อริส", "ฉัน", "อริส", "อริส", "อริส", "ปีศาจเดโม", "ปีศาจเดโม", "ฉัน",
        "อริส", "อริส", "ปีศาจเดโม", "ฉัน", "อริส", "ฉัน", "อริส", " ",
        " ", " ", " ", " ", "ฉัน", "อริส", "ฉัน", "ฉัน", 
        "อริส", " ", "อริส", "ฉัน", "อริส", "อริส", "อริส", " ", " ",
    };
    
    private String[] dialogues = {
        "ตัวเรากับอริสที่กําลังอยู่ในป่า", "ตัวเรากับอริสได้เดินทางออกจากหมู่บ้านมา 1 อาทิตย์เเล้ว",
        "ฉันคิดว่าเราน่าจะออกมาไกลพอสมควรละนะ", "ดีนะที่ก่อนหน้านี้ได้เเผนที่มาจากคนในหมู่บ้าน",
        "อืออ", "นายหิวรึยัง?", "ก็..นิดหน่อยนะ","งั้นเรานั่งพักกินข้าวตรงนี้ก่อนมั้ย?",
        "ก็ดีเหมือนกันนะ", "นายอยากกินอะไรมั้ย?",  "นายนี่ละก็..", "อื้อโอเค", 
        "เดี๋ยวฉันจะไปหาของเเถวนี้ก่อนนะ", "ให้ฉันไปด้วยมั้ย?", "ไม่เป็นไรหรอก นายนั่งรออยู่ตรงนี้แหละ",
        "อือ..ก็ได้", "ตอนนี้เราน่าจะยังอยู่ในเขตป่าLifeอยู่นะ", "คงต้องใช้เวลาอีกนานเลยกว่าถึงจุดหมาย",
        "ดีเลย เราจะได้ใช้เวลานี้อยู่กับอริสมากขึ้น", "อร๊ายยยย", "..นั่นมัน..เสียงอริสนี่!!",
        "เกิดอะไรขึ้น อริส!!", "กะจะเดินเล่นเฉยๆ ดันมาเจอมนุษย์ซะงั้น", "เเต่ก็ดี ข้ากําลังหิวได้ที่เลย",
        "…(ชื่อตัวละครเรา)!!", "นี่เธอเบาดเจ็บตรงไหนรึปล่าว?", "ไม่ ฉันไม่เป็นไร",
        "อยู่ๆปีศาจมันก็เข้ามาโจมตีเเบบกระทันหัน", "เเต่ก็พอหลบได้ เลยมีเเผลถลอกนิดหน่อยหนะ",
        "นี่พ่อหนุ่ม ข้าขอเเม่หนูตรงนั้นได้ไหม?", "เเล้วข้าจะไว้ชีวิตเจ้า", "ไม่มีทาง!!",
        "(เขิน)", "มันใช่เวลามั้ย!! ไอ่บ้า!!", "ยังไงข้าก็จะกินพวกเจ้าทั้งสองอยู่ดี",
        "อริส ครั้งนี้ให้ฉันเป็นคนจัดการเอง", "จะไม่เป็นไรหรอ?", "เเค่นี้สบายมาก",
        "งั้นฝากด้วยนะ", "ตัวเราได้ใช้เวทย์โจมตี ปีศาจเดโมก่อน", "ปีศาจเดโมหลบได้ เเละพุ่งโจมตีใส่เราทันที",
        "เรากระโดดหลบ เเละปล่อยพลังเวทย์ที่รุนเเรงใส่ ปีศาจเดโม", "ปีศาจเดโมโดนพลังเวทย์เต็มๆ",
        "ปีศาจเดโม กลัวจะถูกกําจัด เลยใช้ม่านควันสีดําเเละหลบหนี", "พลาดท่าจนได้", "เเต่อย่างน้อยตอนนี้ก็คงจะปลอดภัยเเล้ว",
        "ฉันคิดว่าปีศาจตัวเมื่อกี้ต้องเกี่ยวข้องกับจอมมารเเน่เลย","นี่เรา..ยังไม่ได้กินข้าวกันเลยนี่หน่า","จริงด้วย! งั้นเดี๋ยวฉันรีบไปทําให้นะ",
        "หลังจากพักผ่อนเเละกินอะไรกันเสร็จเเล้ว","ฉันรู้สึกร้อนมากเลย..","จะไปอาบนํ้าไหมละ ดูเหมือนว่าจะมีลําธารใกล้ๆนะ",
        "จริงหรอ งั้นฉันขอไปอาบนํ้าก่อน","คนลามก!!...","โอเค เดี๋ยวมานะ","อริสกําลังอาบนํ้า",
        "เเต่จู่ๆ ก็มีเสียง เเปลกๆที่พุ่มไม้", "อริสเห็นเงาคนอยู่หลังพุ่มไม้..",
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

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel();
        layeredPane.add(characterLabel, Integer.valueOf(100));
        characterLabel2 = new JLabel();
        layeredPane.add(characterLabel2, Integer.valueOf(101));

        setupDialogueUI();
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
        // 1. ป้องกันการคลิกซ้ำขณะกำลังเลือกตอบ
        if (isChoosing) return;

        // 2. ถ้าตัวอักษรกำลังพิมพ์อยู่ ให้หยุดและแสดงข้อความเต็มทันที
        if (isTyping) {
            if (typewriterTimer != null) typewriterTimer.stop();
            isTyping = false;
            dialogueArea.setText("<html><body style='width: 950px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        // 3. ระบบ Choice Logic สำหรับ Part 5
        if (currentIndex == 9) { 
            showChoices("ฉันกินได้หมดเลย ขอเเค่เป็นอาหารที่เธอทํา", "ฉันยังไงก็ได้", 10, 11);
            return; 
        }
        
        if (currentIndex == 31) { 
            showChoices("ฉันจะไม่ยกอริสให้เเกเด็ดขาด", "ไม่มีวัน เพราะอริสเป็นของฉันคนเดียวเท่านั้น", 32, 33);
            return; 
        }
        
        if (currentIndex == 52) { 
            showChoices("ให้ฉันไปอาบด้วยมั้ยหละ?", "เดี๋ยวฉันจะรอตรงนี้นะ มีอะไรก็เรียกได้เลย", 53, 54);
            return; 
        }

        // 4. การกระโดดข้าม Index หลังจากเลือกตอบเสร็จ
        int nextIndex = currentIndex;
        if (currentIndex == 10 || currentIndex == 11) nextIndex = 12;
        else if (currentIndex == 32 || currentIndex == 33) nextIndex = 34;
        else if (currentIndex == 53 || currentIndex == 54) nextIndex = 55;
        else nextIndex = currentIndex + 1;

        // 5. ตรวจสอบว่ายังไม่จบ Part
        if (nextIndex < dialogues.length) {
            currentIndex = nextIndex;

            // --- ส่วนสำคัญ: ส่งเลขฉากปัจจุบันไปหาเพื่อนคนอื่น ---
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("SYNC_INDEX:" + currentIndex);
            }

            updateScene();
        } else {
            // เมื่อจบ Part 5
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
            backgroundLabel.setIcon(getOptimizedImage(imagePaths[currentIndex], 1280, 800));
        }

        handleSoundEffects(currentIndex);
        updateDialogueDisplay(dialogues[currentIndex]);
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
            if (currentIndex >= 39) label.setBounds(220, 70, 900, 800); 
            else if (currentIndex >= 25) label.setBounds(-180, 50, 900, 800); 
            else label.setBounds(100, 50, 900, 800); 
        }
        else if (path.contains("Alice")) {
            label.setIcon(getOptimizedImage(path, 1200, 800));
            if (currentIndex >= 46) label.setBounds(50, 100, 950, 800); 
            else if (currentIndex >= 39) label.setIcon(null); 
            else if (currentIndex >= 25) label.setBounds(230, 80, 950, 800); 
            else label.setBounds(80, 100, 1200, 800); 
        }
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

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(30, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0; ((Timer)e.getSource()).stop();
                layeredPane.remove(fadeOverlay); updateScene(); 
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void finishGame() {
        stopAllSounds(); // หยุดทุกอย่างก่อนเด้งหน้าต่าง
        UIManager.put("OptionPane.messageFont", THAI_FONT);
        JOptionPane.showMessageDialog(null, "จบ Part 5: การผจญภัยกำลังจะเริ่มขึ้น!");
        dispose();
        System.exit(0); // มั่นใจว่าโปรแกรมและ Thread ทั้งหมดถูกปิด
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part5().setVisible(true));
    }
}

class VisualNovelBox extends JPanel {
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
}