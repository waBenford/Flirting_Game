package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;
import javax.swing.*;

public class part7 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, leftCharLabel, rightCharLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel; 
    private int currentIndex = 0;
    private Clip bgmClip;      
    private JButton choiceButton1, choiceButton2;
    private boolean isChoosing = false;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    // --- ข้อมูลเนื้อหา (คงเดิมตามที่คุณให้มา) ---
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
       "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/scene5/Alice-normal2.png", "res/Charactor/Alice/Girl/Alice-shy1.png",
       "res/scene5/Alice-normal2.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/empty.png", 
       "res/scene5/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/empty.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/empty.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/empty.png", "res/empty.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/empty.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/empty.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Dan/dan-normal2.png", 
       "res/scene5/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", 
       "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", 
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
       "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/scene5/Alice-normal2.png", "res/Charactor/Dan/dan-normal2.png"
    };

    private String[] names = { 
        " ", " ", "Dan", "Dan", "Dan", "ฉัน", "อริส", "อริส", "อริส", " ", " ", "Dan", "ฉัน", "อริส", "???", "???", 
        "ฉัน", "???", "Nebula", "อริส", "ฉัน", "ฉัน", "Nebula", "Nebula", "ฉัน", "ฉัน", "Nebula", "Nebula", "ฉัน",
        "อริส","Nebula", "Nebula", "Nebula", "Nebula", "ฉัน", "Nebula", "อริส", "Nebula", "Nebula", "Nebula", "Dan",
        "Nebula", "Nebula", "ฉัน", "Nebula", "Nebula", "Nebula", "Nebula", "Nebula", "Nebula", "Nebula", "Nebula", "Dan",
        "อริส", "ฉัน", "Nebula", "Nebula", "Nebula", "Nebula","Nebula", "Nebula", "Dan", "Nebula", "Nebula", "ฉัน", 
        "Nebula", "Nebula", "Nebula", "อริส", "Dan"
    };

    private String[] dialogues = {
        "หลังจากเดินทางมาหลายสัปดาห์…","ในที่สุดพวกเราก็มาถึงป่า Death End", 
        "ที่นี่แหละ…ป่า Death End", "จากนี้ไปต้องระวังตัวให้ดี", "ปีศาจในป่านี้แข็งแกร่งกว่าที่พวกเธอเคยเจอมา", 
        "เข้าใจแล้ว", "ป่านี้มันน่ากลัวกว่าที่คิดอีกนะ...", "อะ...อือ...", "อือ เข้าใจแล้ว", 
        "หลังจากเดินลึกเข้าไปในป่า...","พวกเราก็พบกับปราสาทขนาดใหญ่", "นั่นไง…ปราสาทของจอมมาร", 
        "ในที่สุดก็มาถึงสักที", "บรรยากาศมันน่ากลัวจัง…", "มนุษย์งั้นหรอ...", "กล้ามาถึงที่นี่ได้ก็นับว่ากล้าดีนะ", 
        "ใครกัน!?", "ข้าคือจอมมาร...", "ชื่อของข้าคือ Nebula", "จอมมาร…!!", 
        "ในที่สุดก็เจอตัวแล้ว", "เป็นแกสินะ ที่สั่งให้ปีศาจโจมตีหมู่บ้าน", "หืม?", "เจ้ากําลังพูดเรื่องอะไร?", 
        "อย่ามาแกล้งทําเป็นไม่รู้!", "พวกเราจะหยุดแกที่นี่!", "ฮ่าๆๆ", "มนุษย์นี่น่าสนใจจริงๆ", 
        "ถ้าอยากลองก็เข้ามา", "รับนี่ไป!","Ice Lance!", "น่าสนุกดีนี่", "แต่พลังแค่นี้...", 
        "ยังห่างไกลนะ", "พอแค่นี้ก่อนดีกว่า", "อะไรนะ?", "ข้าไม่ได้เป็นคนสั่งปีศาจพวกนั้น", 
        "อะไรนะ!?", "คนที่ทําเรื่องพวกนั้นคือ...", "จอมมารอีกคนหนึ่ง", "ชื่อของมันคือ Grey", 
        "จอมมารอีกคนงั้นหรอ...", "มนุษย์...เจ้าค่อนข้างแข็งแกร่งกว่าที่คิดนะ", "ปกติแล้วมนุษย์ที่มาถึงที่นี่ มักจะหนีหรือไม่ก็ตายไปแล้ว", 
        "ก็แค่ทําในสิ่งที่ต้องทํา", "หืม...น่าสนใจดีนี่", "เจ้ากล้าต่อสู้กับจอมมารโดยไม่ลังเลเลยงั้นหรอ?", 
        "หึ...มนุษย์ที่พูดแบบนี้กับข้าเป็นคนแรกเลยนะ", "เจ้านี่แปลกดีจริงๆ", "ปกติมนุษย์จะกลัวข้า...", "แต่เจ้ากลับยืนคุยกับข้าเฉยๆ", 
        "เจ้ากล้าพูดกับจอมมารแบบนั้นเลยหรอ", "มะ…มนุษย์นี่พูดอะไรของเจ้า…", 
        "นี่พวกนายกําลังจีบจอมมารกันอยู่รึไงเนี่ย...", "นะ…นายไปพูดอะไรกับจอมมารแบบนั้นกัน!!", 
        "ถ้าอย่างนั้น...จอมมารที่อยู่เบื้องหลังเรื่องพวกนี้ก็คือ Grey งั้นสินะ", "ใช่", "เขาเคยเป็นหนึ่งในจอมมารที่อยู่ภายใต้การปกครองของข้า",
        "แต่แนวคิดของเขาแตกต่างจากข้า", "ข้าเชื่อว่ามนุษย์กับปีศาจสามารถอยู่ร่วมกันได้", "แต่ Grey เชื่อว่ามนุษย์ควรถูกกําจัดให้หมด", 
        "งั้นเขาก็แยกตัวออกไปสินะ...", "ใช่", "และตอนนี้เขากําลังสร้างกองทัพปีศาจของตัวเอง", "ถ้าอย่างนั้น...เขาอยู่ที่ไหน", 
        "Grey ซ่อนตัวอยู่ที่...","หุบเขาเงามืด ทางตะวันตกของป่า Death End","ที่นั่นมีป้อมปราการของเขาอยู่","งั้นเราก็มีจุดหมายต่อไปแล้วสินะ",
        "แต่ที่นั่นอันตรายกว่าที่นี่อีก"
    };

    public part7() {
        setTitle("ISEKAI DEMO - Part 7 (Dual Character)");
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
            @Override public void mouseClicked(MouseEvent e) { handleNext(); }
        });
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(getOptimizedImage(imagePaths[currentIndex], 1280, 800));
        
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("empty.png")) {
                leftCharLabel.setIcon(null);
                rightCharLabel.setIcon(null);
            } else {
                // Logic วางตัวละคร (อ้างอิงจาก Part 8 ที่คุณส่งมา)
                int w = 900, h = 900;
                
                if (path.contains("Nebula")) {
                    // Nebula ให้อยู่ทางขวา
                    rightCharLabel.setBounds(500, 50, w, h);
                    rightCharLabel.setIcon(getOptimizedImage(path, w, h));
                    // (Optional) ถ้าต้องการให้ Alice/Dan หายไปตอนจอมมารพูด ก็สั่ง leftCharLabel.setIcon(null);
                } else {
                    // Dan, Alice ให้อยู่ทางซ้าย
                    int specW = path.contains("dan") ? 1400 : 1200;
                    int specH = path.contains("dan") ? 1000 : 950;
                    leftCharLabel.setBounds(-250, 50, specW, specH);
                    leftCharLabel.setIcon(getOptimizedImage(path, specW, specH));
                }
            }
        }
        layeredPane.repaint();
    }

    // --- Choice และ UI Logic (คงเดิม) ---
    private void handleNext() {
        if (isChoosing) return;
        if (isTyping) {
            stopTypewriter();
            dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        if (currentIndex == 6) { showChoices("ไม่ต้องกลัวหรอก ฉันอยู่ข้างๆเธอ", "ถ้าระวังตัวดีๆก็น่าจะไม่เป็นไร", 7, 8); return; }
        if (currentIndex == 46) { showChoices("ต่อให้เธอเป็นจอมมาร ถ้าทำร้ายคนฉันก็จะสู้", "ก็แค่คิดว่าเธอคงไม่ใช่คนเลว", 47, 48); return; }
        if (currentIndex == 50) { showChoices("เพราะเธอไม่ได้ดูน่ากลัวขนาดนั้น", "ถ้าจอมมารสวยขนาดนี้ ใครจะกลัวลง", 51, 52); return; }

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

    public void playBGM(String path, float volume) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioIn);
            FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
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
        JOptionPane.showMessageDialog(null, "จบ Part 7! พร้อมไปต่อ Part 8...");
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part7().setVisible(true));
    }
}

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