package flirting_game;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;

public class part3 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private RoundedPanel dialoguePanel; // เปลี่ยนเป็น RoundedPanel ให้ตรงกับ class ข้างล่าง
    private int currentIndex = 0;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private Clip bgmClip;      
    private Clip effectClip;   
    private JButton choiceButton1, choiceButton2;
    private boolean isChoosing = false;
    private float alpha = 1.0f; 
    private JPanel fadeOverlay;
    private JLabel affinityLabel; 
    private JLabel statusLabel;

    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 24);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 24);

    // --- Array ข้อมูล (คงเดิมตามที่คุณส่งมา) ---
    private String[] imagePaths = {
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s2.png", "res/scene3/s2.png", 
        "res/scene3/s2.png", "res/scene3/s2.png", "res/scene3/s3.png", "res/scene3/s3.png", 
        "res/scene3/s3.png", "res/scene3/s3.png", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg", "res/scene3/s1.jpg",
        "res/scene3/s1.jpg",
    };

    private String[] charPaths = {
        "res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy2.png","res/Charactor/Alice-happy1.png",
        "res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy2.png","res/Charactor/Alice-happy1.png",
        "res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy2.png",
        "res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy2.png","res/Charactor/Alice-happy1.png",
        "res/Charactor/Alice-happy1.png","res/Charactor/Alice-happy2.png","res/Charactor/Alice-happy2.png","res/Charactor/Alice-happy1.png", 
        "res/Charactor/Alice-sad2_1.png","res/Charactor/Alice-sad1_1.png","res/empty.png", "res/empty.png","res/empty.png", "res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice-cry1_1.png","res/Charactor/Alice-cry2_1.png", 
        "res/Charactor/Alice-cry1_1.png","res/Charactor/Alice-sad1_1.png","res/Charactor/Alice-sad1_1.png","res/Charactor/Alice-sad2_1.png", 
        "res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png",
        "res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png",
        "res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile2_1.png","res/Charactor/Alice-smile1_1.png","res/Charactor/Alice-smile1_1.png",
    };

    private String[] names = {
        "อริส", "ฉัน", "ฉัน", "อริส", "ฉัน", "อริส", "อริส", "อริส",
        "อริส", "อริส", "อริส", "ฉัน", "อริส", "อริส", "อริส", "อริส",
        "ฉัน", "ฉัน", "ฉัน", "อริส", "อริส", "ฉัน", "อริส", "อริส", "อริส", "อริส", "อริส", 
        "อริส", "อริส","อริส"," ","ฉัน","อริส","อริส","อริส","อริส","อริส","อริส","อริส",
        "อริส", "ฉัน", "ฉัน", "ฉัน", "ฉัน", "ฉัน", "อริส", "อริส", "ฉัน",
    };
            
    private String[] dialogues = {
        "เมื่อกี้เธอทําอะไรหรอ??", "..ไม่รู้สิ..", "มันเหมือนว่าฉันจะใช้พลังเวทย์ได้เลย..", 
        "เห้ออ..ฉันนึกว่ามีปีศาจมาโจมตีซะอีก", "..เอ๋..ปีศาจอะไรหรอ??", "..หาา..นี่เธอไม่รู้จริงๆหรอ??",
        "เเต่ก็ช่างมันเถอะ เดี๋ยวฉันจะเล่าทุกอย่างให้ฟังละกัน??", "ในโลกนี้หนะ เป็นโลกที่ผู้คนก็ต่างใช้พลังเวทย์กันได้",
        "เเต่ก็มีบางคนที่ไม่สามารถใช้มันได้", "เเต่ถึงอย่างงั้นก็มีคนที่สามารถไต่เต้าไปจนถึงระดับสูง",
        "เเม้จะไม่มีพลังเวทย์ก็ตาม", "..เอ่อ..เเล้วปีศาจหละ??", "อ้อ..จริงด้วยเกือบลืมไปเลย",
        "โลกนี้จะมีสองเผ่าอยู่หลักๆ", "เผ่ามนุษย์เเละเผ่าปีศาจ", 
        "ไม่เหมือนกันสักหน่อย ปีศาจหน่ะเป็นเผ่าที่ชั่วร้าย", 
        "อันนี้ฉันก็ไม่รู้เหมือนกัน",
        "เเล้วเธออยู่บ้านคนเดียวหรอ??",
        "..พ่อกับเเม่เธอหละ??", 
        "ฉันอยู่คนเดียวมาตั้งเเต่เด็กๆเเล้วหละ",
        "พ่อกับเเม่ของฉันท่านเสียไปนานเเล้ว",
        "เอ่อ..เธอพอจะเล่าให้ฉันฟังได้มั้ย",
        "..มันเป็นเรื่องเมื่อ6ปีที่เเล้ว", 
        "หมู่บ้านของฉัน พวกเราอยู่กันอย่างมีความสุข",
        "ผู้คนก็ต่างอยู่ด้วยกันอย่างเอื้อเฟื้อ เเละพอเพียง",
        "จนกระทั่ง", 
        "มีปีศาจที่เเข็งเเกร่งตัวนึง ได้มาทําลายหมู่บ้านของพวกเรา",
        "มันพรากชีวิตของผู้คนไปมากมาย หนึ่งในนั้นก็มีพ่อเเม่ของฉันด้วย",
        "พ่อเเม่ของฉันปกป้องฉันจนวินาทีสุดท้าย..",
        "จากเหตุการณ์ครั้งนั้น ฉันเลยรอดมาได้..",
        "อริสกําลังเศร้า..",
        "ขอโทษนะที่ถามอะไรเเบบนั้น",
        "ไม่เป็นไรหหรอก", 
        "ขอบคุณนะ..", 
        "...", 
        "ฉันเลยคิดว่าสักวันนึง ฉันจะต้องออกเดินทาง",
        "ฝึกฝนตัวเองให้เเข็งเเกร่งมากขึ้น",
        "เพื่อที่ฉันจะได้เเก้เเค้นให้พ่อกับเเม่",
        "นี่...", 
        "เธออยากจะร่วมเดินทางกับฉันมั้ย?",
        "เธอเป็นคนที่จิตใจดี เเละอ่อนโยนมาก",
        "เพราะอย่างงั้นฉันเลยอยากที่จะปกป้องเธอ",
        "ไม่มีเหตุผลเลยที่ฉันปฏิเสธเธอ",
        "เเน่นอน!! ฉันจะออกเดินทางกับเธอ",
        "ฉันจะต้องเเข็งเเกร่งขึ้นให้ได้เหมือนกัน", 
        "ขอบคุณนะ …",
        "เออ..ว่าเเต่เธอชื่ออะไรกันเเน่",
        "ฉันชื่อ...",
    };

    public part3() {
        setTitle("ISEKAI DEMO - Part 3");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);
        
        playSE("res/sound/soundtrack3.wav", true, -10.0f); 
        playSE("res/sound/fireplace.wav", true, -5.0f); 
        playSE("res/sound/Doushitano.wav", false, 5.0f); 

        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel(scaleImage(charPaths[0], 900, 1100));
        characterLabel.setBounds(75, 50, 900, 1100); 
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();

        JPanel relPanel = new JPanel();
        relPanel.setLayout(new GridLayout(2, 1));
        relPanel.setBounds(20, 20, 250, 60);
        relPanel.setOpaque(false);

        affinityLabel = new JLabel("ความสนิท: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        affinityLabel.setForeground(Color.WHITE);

        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        statusLabel.setForeground(new Color(255, 204, 0));

        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);

        fadeOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fadeOverlay.setBounds(0, 0, 1000, 800);
        fadeOverlay.setOpaque(false);
        layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);

        startFadeIn();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isChoosing) return;

                // --- จุดที่แก้ไข: จัดลำดับเงื่อนไขใหม่ ---
                
                // 1. ตรวจสอบจุดขึ้นตัวเลือกก่อนเปลี่ยน Index
                if (currentIndex == 14) {
                    showChoices("..ปีศาจนี่เหมือนผีรึเปล่า??", "..เอ่อ..แล้วเผ่าอื่นๆหละ??", 15, 16);
                    return;
                }
                if (currentIndex == 32) {
                    showChoices("เข้าไปปลอบอริส", "นั่งอยู่เฉยๆ", 33, 34);
                    return;
                }

                // 2. ตรวจสอบทางแยกหลังจากกดปุ่ม Choice (กระโดดข้ามบท)
                if (currentIndex == 15 || currentIndex == 16) {
                    currentIndex = 17;
                } else if (currentIndex == 33 || currentIndex == 34) {
                    currentIndex = 35; 
                } else {
                    currentIndex++;
                }

                // 3. ตรวจสอบว่าจบ Part หรือยัง
                if (currentIndex < dialogues.length) {
                    handleSoundEffects(currentIndex);
                    updateScene();
                } else {
                    UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 18));
                    JOptionPane.showMessageDialog(null, "จบ Part 3 แล้ว!");
                    new part4().setVisible(true);
                    dispose(); 
                }
            }
        });
    }

    private void setupDialogueUI() {
        dialoguePanel = new RoundedPanel(40);
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(50, 550, 900, 180);
        dialoguePanel.setBackground(new Color(20, 20, 25, 215));
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel(names[0]);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        nameLabel.setForeground(new Color(255, 204, 0));
        nameLabel.setBounds(60, 20, 300, 40); 
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 24));
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 75, 800, 100); 
        dialoguePanel.add(dialogueArea);
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) updateDialogueDisplay(dialogues[currentIndex]);
        
        backgroundLabel.setIcon(scaleImage(imagePaths[Math.min(currentIndex, imagePaths.length-1)], 1000, 800));
        characterLabel.setIcon(scaleImage(charPaths[Math.min(currentIndex, charPaths.length-1)], 900, 1100));
        layeredPane.repaint();
    }

    private void updateDialogueDisplay(String text) {
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }
        charIndex = 0;
        dialogueArea.setText(""); 
        typewriterTimer = new Timer(35, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                String currentText = text.substring(0, charIndex);
                dialogueArea.setText("<html><body style='width: 750px;'>" + currentText + "</body></html>");
            } else {
                typewriterTimer.stop();
            }
        });
        typewriterTimer.start();
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(50, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0;
                ((Timer)e.getSource()).stop();
                layeredPane.remove(fadeOverlay);
                updateDialogueDisplay(dialogues[0]); 
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void stopBGM() {
        if (bgmClip != null) {
            if (bgmClip.isRunning()) bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
        }
    }

    private void stopEffect() {
        if (effectClip != null) {
            if (effectClip.isRunning()) effectClip.stop();
            effectClip.close();
            effectClip = null; 
        }
    }

    private void handleSoundEffects(int index) {
        if (index == 20) {
            stopBGM(); 
            playSE("res/sound/soundtrack4.wav", true, -5.0f);
        }
        if (index == 22) {
            stopEffect(); 
            playSE("res/sound/village.wav", true, -5.0f); 
        }
        if (index == 26) {
            stopEffect(); 
            screenShake(10, 1000);
            playSE("res/sound/monster.wav", false, -10.0f);
            playSE("res/sound/housefire.wav", false, -10.0f); 
        }
        if (index == 30) {
            stopEffect(); 
            playSE("res/sound/fireplace.wav", true, 0.0f); 
            playEffect("res/sound/cry.wav", 5.0f);
        }
        if (index == 33) {
            playEffect("res/sound/Arigato.wav", 5.0f);
        }
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
                temporaryClip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) temporaryClip.close();
                });
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

    private void showChoices(String text1, String text2, int target1, int target2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 350, target1);
        choiceButton2 = createChoiceButton(text2, 420, target2); 
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int targetIndex) {
        JButton btn = new JButton(text);
        btn.setBounds(580, y, 350, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 30, 30, 220));
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 2));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(70, 70, 70, 240));
                btn.setBounds(570, y, 360, 60);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(30, 30, 30, 220));
                btn.setBounds(580, y, 350, 60);
            }
        });

        btn.addActionListener(e -> {
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false;

            if (targetIndex == 33) { 
                relationdata.aliceRel.addAffinity(10);
            } else if (targetIndex == 34) { 
                relationdata.aliceRel.decreaseAffinity(5);
            }
            
            affinityLabel.setText("ความสนิท: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());

            currentIndex = targetIndex;
            handleSoundEffects(currentIndex);
            updateScene();
        });

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);
                super.paint(g, c);
            }
        });
        return btn;
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    public void screenShake(int intensity, int duration) {
        Point originalLoc = getLocation();
        Timer shakeTimer = new Timer(20, null);
        final long startTime = System.currentTimeMillis();

        shakeTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed < duration) {
                int x = (int) (Math.random() * intensity * 2 - intensity);
                int y = (int) (Math.random() * intensity * 2 - intensity);
                setLocation(originalLoc.x + x, originalLoc.y + y);
            } else {
                setLocation(originalLoc);
                ((Timer) e.getSource()).stop();
            }
        });
        shakeTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part3().setVisible(true));
    }
}

class RoundedPanel extends JPanel {
    private int cornerRadius;
    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false); 
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }
}