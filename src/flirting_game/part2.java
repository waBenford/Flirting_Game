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

public class part2 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private RoundedPanel dialoguePanel;
    private int currentIndex = 0;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isAnimating = false;
    private Clip bgmClip;    // สำหรับเพลงพื้นหลัง (วนลูป)
    private Clip effectClip; // สำหรับเสียงเอฟเฟกต์ (เล่นครั้งเดียว)
    private JButton choiceButton1, choiceButton2;
    private boolean isChoosing = false; // ล็อกไม่ให้คลิกเมาส์เปลี่ยนฉากขณะมีตัวเลือก
    private float alpha = 1.0f; 
    private JPanel fadeOverlay;
    private JLabel affinityLabel; 
    private JLabel statusLabel;
    

    private String[] imagePaths = {
            "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
            "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
            "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s2.png", 
            "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s3.png", 
            "res/scene2/s3.png", "res/scene2/s3.png", "res/scene2/s4.png", "res/scene2/s4.png", 
            "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", 
            "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", 
            "res/scene2/s6.png", "res/scene2/s6.png",   
    
    };
    
    private String[] charPaths = {
            "res/scene2/alice1.png", "res/scene2/alice1.png", "res/scene2/alice1.png",  
            "res/scene2/alice2.png", "res/scene2/alice1.png", "res/scene2/alice2.png",
            "res/scene2/alice1.png", "res/scene2/alice2.png", "res/scene2/alice1.png", 
            "res/scene2/alice2.png", "res/scene2/alice1.png","res/empty.png", "res/empty.png",
            "res/empty.png","res/empty.png", "res/empty.png", "res/empty.png","res/empty.png","res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png"
    };
    
    private String[] names = {
        "ฉัน", "ฉัน", "เด็กผู้หญิง", "เด็กผู้หญิง",
        "ฉัน", "เด็กผู้หญิง", "อริส", 
        "อริส", "อริส", "อริส", 
        "อริส", "อริส", "อริส", "ฉัน",
        "อริส", "ฉัน",  "ฉัน", "...", "ฉัน",
};

    private String[] dialogues = {
    "..เอ่อ..เธอคือ ใครหรอ?", // 0
    "..เเล้วนี่ฉันอยู่ที่ไหน..ฉันยังไม่ตายหรอ!?", // 1
    "..เอ๋..ตายหรอ??", // 2
    "ไม่เห็นจะมีใครตายเลยนะ", // 3
    "นี่เรา..อยู่ที่ไหนกันเเน่นะ..", // 4
    "อ๊ะ..ลืมเเนะนําตัวเลย", // 5
    "ฉันชื่อว่า อริส เป็นลูกของชาวนาในหมู่บ้านเเถวนี้", // 6
    "เเล้วเธอหละ?", // 7 (จุดขึ้น Choice)
    // --- ทางเลือกที่ 1 (Index 8) ---
    "งั้นหรอ...ไม่เป็นไร", // 8 
    // --- ทางเลือกที่ 2 (Index 9) ---
    "อ้าว... จำชื่อไม่ได้หรอ? ไม่เป็นไรนะ ค่อยๆ นึกไปก็ได้", // 9
    // --- จุดรวมพล (Index 10) ---
    "อืมม..งั้นเดี๋ยว..ไปที่บ้านฉันก่อนละกัน", // 10
    "ตัวเธอสะบักสะบอมมากเลย",
    "ไปอาบนํ้าก่อนเลยนะ เดี๋ยวฉันจะเตรียมกับข้าวไว้ให้",
    "ห้องนํ้าไปทางไหนหรอ?",
    "ขึ้นบันไดไปเเล้วก็เลี้ยวขวาหนะ",
    "น่ารักเเถมยังใจดีอีกต่างหาก",
    "..ต่อจากนี้จะทําไงต่อดีนะ..เห้ออ..",
    "...",
    "เอื้อมมือไปหยิบ",
    "...",
    "ไม่เคยเห็นหนังสือเเบบนี้มาก่อนเลย",
    "..เปิดอ่าน..",
    "..การใช้เวทย์ลมขั้นพื้นฐาน..",
    "..พลังเวทย์อย่างงั้นนะหรอ..",
    "..ดูเหมือนจะมีวิธีการร่ายเวทย์ด้วย.. ",
    "..ลองหน่อยละกัน..ยังไงมันก็คงเป็นหนังสือที่ทําขึ้นมาเล่นๆ",
    "..สายลมที่พัดผ่าน..จงตอบรับเสียงของฉัน!!",
    "..Wind Dash!!",
    "..เห้ย!!..เมื่อกี้มันอะไร?!",
    "เกิดอะไรขึ้นหนะ!!",
};

    public part2() {

        setTitle("ISEKAI DEMO - Part 2: First Encounter");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playSE("res/sound/soundtrack1.wav", true, -5.0f);
        playSE("res/sound/soonoo.wav", false, 5.0f);

        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1000, 800));
        backgroundLabel.setBounds(0, 0, 1000, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel(scaleImage(charPaths[0], 1000, 800));
        characterLabel.setBounds(0, 0, 1000, 800);
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
        statusLabel.setForeground(new Color(255, 204, 0)); // สีเหลืองทอง

        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);

        // สร้างแผ่นดำสำหรับทำ Fade In
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

            // สั่งให้เริ่มจางออก และเริ่มพิมพ์ข้อความแรก
            startFadeIn();
            animateText(dialogues[0]);

    layeredPane.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
        if (isChoosing) return;
        if (isAnimating) {
            stopAnimation();
            updateDialogueDisplay(dialogues[currentIndex]);
            return;
        }

        if (currentIndex == 17) {
                    if (bgmClip != null && bgmClip.isRunning()) {
                        bgmClip.stop();
                        bgmClip.close();
                    }
                }

        // ส่วนที่แก้ไขเพิ่ม: ถ้าคุยจบประโยค "ชื่อเธอฟังดูดีจัง" (Index 8)
        if (currentIndex == 8) {
            currentIndex = 10; // สั่งกระโดดข้าม Index 9 ไปประโยค "ไปบ้านฉันก่อน" ทันที
            updateScene();
            return;
        }

        // เมื่อคุยถึง "เเล้วเธอหละ?" (Index 7) และกดคลิก
        if (currentIndex == 7) {
            // โชว์ตัวเลือก และกำหนดว่าถ้าเลือกข้อไหนจะให้กระโดดไป Index อะไรใน Array
            showChoices("ฉันไม่รู้ (ฉันยังไม่ไว้ใจใคร)", "ฉันจําชื่อตัวเองไม่ได้", 8,9); 
            return; 
        }

        currentIndex++;
        if (currentIndex < dialogues.length) {

            handleSoundEffects(currentIndex);

            updateScene();
         // ค้นหาส่วนนี้ใน MouseListener ของ part2.java
        } else {
            // 1. หยุด BGM ของ Part 2 ก่อนไป Part 3
            if (bgmClip != null && bgmClip.isRunning()) {
                bgmClip.stop();
                bgmClip.close();
            }
            UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 18));
            UIManager.put("OptionPane.buttonFont", new Font("Tahoma", Font.PLAIN, 18));

            JOptionPane.showMessageDialog(null, "จบ part 2 แล้ว! กำลังเข้าสู่บทถัดไป...");
            JOptionPane.showMessageDialog(null, "กำลังเข้าสู่เนื้อเรื่องบทที่ 3...");
            
            new part3().setVisible(true);
            
            dispose(); 
        }
    }
});
    }

    private void setupDialogueUI() {
    // 1. กล่องข้อความหลัก
    dialoguePanel = new RoundedPanel(40);
    dialoguePanel.setLayout(null);
    dialoguePanel.setBounds(50, 550, 900, 180); // ปรับความสูงเป็น 180 เพื่อให้มีพื้นที่พอ
    dialoguePanel.setBackground(new Color(20, 20, 25, 215));
    layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

    // 2. ชื่อตัวละคร (ขยับขึ้นไปด้านบนของกล่อง)
    nameLabel = new JLabel(names[0]);
    nameLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
    nameLabel.setForeground(new Color(255, 204, 0)); // ใช้สีเหลืองทองตามแบบที่คุณชอบ
    
    // x: 60 (เขยิบจากซ้าย), y: 20 (อยู่ที่ขอบบนสุดของกล่อง)
    nameLabel.setBounds(60, 20, 300, 40); 
    dialoguePanel.add(nameLabel);

    // 3. บทสนทนา (ขยับลงมาข้างล่างชื่อ)
    dialogueArea = new JLabel();
    dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 24));
    dialogueArea.setForeground(Color.WHITE);
    dialogueArea.setVerticalAlignment(SwingConstants.TOP);
    
    // x: 60 (ตรงกับชื่อ), y: 75 (อยู่ต่ำกว่าชื่อ 55 พิกเซล จะได้ไม่ทับกัน)
    dialogueArea.setBounds(60, 75, 800, 100); 
    dialoguePanel.add(dialogueArea);
    
}

    private void updateScene() {
    // อัปเดตชื่อตัวละครตามลำดับปัจจุบัน
    if (currentIndex < names.length) {
        nameLabel.setText(names[currentIndex]); 
    }
    
    // อัปเดตบทสนทนา (พิมพ์ดีด)
    if (currentIndex < dialogues.length) {
        animateText(dialogues[currentIndex]); //
    }
    
    // อัปเดตภาพพื้นหลังและตัวละคร
    if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1000, 800));
    if (currentIndex < charPaths.length) characterLabel.setIcon(scaleImage(charPaths[currentIndex], 1000, 800));

}

    // แยกฟังก์ชันจัดการเสียงเพื่อให้โค้ดอ่านง่าย
    private void handleSoundEffects(int index) {
        if (index == 8 || index == 9) {
        playEffect("res/sound/soudesukaa.wav", 5.0f);
    } 
    else if (index == 10) {
        if (effectClip != null && effectClip.isRunning()) {
            effectClip.stop();
        }
    }
    if (index == 18) {
        playEffect("res/sound/soundtrack2.wav", -5.0f);
    }
    if (index == 29){
        if (effectClip != null && effectClip.isRunning()) {
            effectClip.stop();
        }
    }
    if (index == 30) {
        if (effectClip != null && effectClip.isRunning()) {
            effectClip.stop();
        }
    }
    if (index == 15){
        playEffect("res/sound/water.wav", 5.0f);
    } 
    if (index == 18){
        if (effectClip != null && effectClip.isRunning()) {
            effectClip.stop();
        }
    }
    if (index == 11){
        playEffect("res/sound/fireplace.wav", 5.0f);
    }
    if (index == 14){
        if (effectClip != null && effectClip.isRunning()) {
            effectClip.stop();
        }
    }
    if (index == 28){
        playEffect("res/sound/winddash.wav", 0.0f);
    }
    if (index == 29){
        if (effectClip != null && effectClip.isRunning()) {
            effectClip.stop();
        }
    }
}

    // --- ฟังก์ชันเล่นเสียงเอฟเฟกต์ (เล่นครั้งเดียว/ป้องกันเสียงซ้อน) ---
    public void playEffect(String path, float volume) {
        try {
            if (effectClip != null && effectClip.isRunning()) {
                effectClip.stop();
            }
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                effectClip = AudioSystem.getClip(); 
                effectClip.open(audioIn);
                FloatControl gainControl = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                effectClip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- ฟังก์ชันเล่นเสียง BGM (วนลูป) ---
    public void playSE(String path, boolean isLoop, float volume) {
        try {
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip(); 
                clip.open(audioIn);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                if (isLoop) {
                    bgmClip = clip;
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                clip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void animateText(String fullText) {
        isAnimating = true; charIndex = 0; dialogueArea.setText(""); 
        if (typewriterTimer != null && typewriterTimer.isRunning()) typewriterTimer.stop();
        typewriterTimer = new Timer(20, e -> {
            if (charIndex <= fullText.length()) {
                updateDialogueDisplay(fullText.substring(0, charIndex));
                charIndex++;
            } else { stopAnimation(); }
        });
        typewriterTimer.start();
    }

    private void startFadeIn() {
    Timer fadeTimer = new Timer(80, e -> {
        alpha -= 0.05f; // ค่อยๆ ลดค่าความมืดลง
        if (alpha <= 0) {
            alpha = 0;
            ((Timer)e.getSource()).stop(); // หยุดเมื่อใสจนมองเห็นฉากพื้นหลัง
            layeredPane.remove(fadeOverlay); // ลบแผ่นดำออกเพื่อประหยัดทรัพยากร
            layeredPane.repaint();
        }
        fadeOverlay.repaint();
    });
    fadeTimer.start();
}

    private void stopAnimation() {
        if (typewriterTimer != null) typewriterTimer.stop();
        isAnimating = false;
    }

    private void updateDialogueDisplay(String text) {
        dialogueArea.setText("<html><body style='width: 750px;'><span style='text-shadow: 1px 1px 2px black;'>" 
                            + text + "</span></body></html>");
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return null; // ป้องกันค้างถ้าหาไฟล์ภาพไม่เจอ
        }
    }

    private void showChoices(String text1, String text2, int target1, int target2) {
    // ลบปุ่มเดิมออกจากการแสดงผล (ถ้ามี)
    if (choiceButton1 != null) {
        layeredPane.remove(choiceButton1);
        choiceButton1 = null; // คืนค่าว่างเพื่อความสะอาด
    }
    if (choiceButton2 != null) {
        layeredPane.remove(choiceButton2);
        choiceButton2 = null; // คืนค่าว่างเพื่อความสะอาด
    }
    
    layeredPane.repaint(); // สั่งวาดใหม่ทันทีเพื่อล้างภาพค้าง

    isChoosing = true;
    
    // สร้างปุ่มใหม่พร้อมย้ายไปทางขวา
    choiceButton1 = createChoiceButton(text1, 350, target1); // x จะถูกตั้งในฟังก์ชันสร้างปุ่ม
    choiceButton2 = createChoiceButton(text2, 420, target2); 
    
    layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
    layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
    
    layeredPane.revalidate();
    layeredPane.repaint();
}

    private JButton createChoiceButton(String text, int y, int targetIndex) {
    JButton btn = new JButton(text);
    
    // ตั้งค่าพื้นฐาน (ย้ายไปทางขวาตามที่คุณต้องการ)
    btn.setBounds(580, y, 350, 60); 
    btn.setFont(new Font("Tahoma", Font.BOLD, 18));
    btn.setForeground(Color.WHITE);
    btn.setBackground(new Color(30, 30, 30, 220));
    btn.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 2));
    btn.setFocusPainted(false);
    btn.setContentAreaFilled(false); // ทำให้วาดสีพื้นหลังเองเพื่อความลื่นไหล
    btn.setOpaque(false);

    // --- ส่วนของ Animation ---
    btn.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            // 1. Animation ตอนเมาส์ชี้: เปลี่ยนสีพื้นหลังให้สว่างขึ้นและขยับปุ่มไปทางซ้ายนิดหน่อย
            btn.setBackground(new Color(70, 70, 70, 240));
            btn.setBounds(570, y, 360, 60); // ขยับมาทางซ้าย 10px และขยายกว้างขึ้นนิดนึง
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // 2. คืนค่าเดิมเมื่อเอาเมาส์ออก
            btn.setBackground(new Color(30, 30, 30, 220));
            btn.setBounds(580, y, 350, 60);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // 3. Animation ตอนกด: ย่อขนาดปุ่มลงเล็กน้อยให้ดูเหมือนโดนกดจริง
            btn.setBounds(585, y + 2, 340, 56);
            // เล่นเสียงคลิก (ถ้ามีไฟล์เสียง)
            // playEffect("res/sound/click_sound.wav", -5.0f); 
        }
    });

    // ส่วนของคำสั่งเมื่อคลิก (ActionListener)
    btn.addActionListener(e -> {
        layeredPane.remove(choiceButton1);
        layeredPane.remove(choiceButton2);
        choiceButton1 = null;
        choiceButton2 = null;
        isChoosing = false;

        if (targetIndex == 9) { 
            relationdata.aliceRel.addAffinity(10); // เพิ่มค่าความสัมพันธ์ 10 คะแนน
        } else if (targetIndex == 8) {
            relationdata.aliceRel.decreaseAffinity(5); // หรือลดคะแนนถ้าเลือกข้อที่ไม่ดี
        }

        // แสดง Log ใน Console เพื่อตรวจสอบ (Optional)
        affinityLabel.setText("ความสนิท: " + relationdata.aliceRel.getAffinity());
        statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
        System.out.println("Status: " + relationdata.aliceRel.getStatus() + " (" + relationdata.aliceRel.getAffinity() + ")");
        
        currentIndex = targetIndex;
        handleSoundEffects(currentIndex);
        
        updateScene();
        layeredPane.repaint();
    });

    // ต้อง Override การวาดปุ่มนิดหน่อยเพื่อให้สีพื้นหลังที่เราตั้งค่าไว้แสดงผล
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part2().setVisible(true));
    }
}

// --- เพิ่มคลาสที่ขาดหายไปเพื่อให้ Error หาย ---
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

class TopLeftRoundedPanel extends JPanel {
    private int cornerRadius;
    public TopLeftRoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        int w = getWidth();
        int h = getHeight();
        java.awt.geom.Area area = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        area.add(new java.awt.geom.Area(new java.awt.Rectangle(cornerRadius, 0, w - cornerRadius, h)));
        area.add(new java.awt.geom.Area(new java.awt.Rectangle(0, cornerRadius, w, h - cornerRadius)));
        g2.fill(area);
    }
}