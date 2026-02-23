package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class part5 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private RoundedPanel dialoguePanel; 
    private float alpha = 1.0f;
    private JPanel fadeOverlay;
    private int currentIndex = 0;
    private JButton choiceButton1, choiceButton2;
    private boolean isChoosing = false;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    
    // --- ฟอนต์ภาษาไทยมาตรฐานสำหรับจอ 1280 ---
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    private String[] imagePaths = {
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png",
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", 
       "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s1.png", "res/scene5/s2.png", 
       "res/scene5/s2.png", "res/scene5/s2.png", "res/scene5/s2.png", "res/scene5/s2.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s4.png", "res/scene5/s4.png", 
       "res/scene5/s4.png", "res/scene5/s4.png", "res/scene5/s4.png", "res/scene5/s3.png", 
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png",
       "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s3.png", "res/scene5/s5.png",
       "res/scene5/s5.png", "res/scene5/s5.png", 
    };

    private String[] charPaths = {
       "res/empty.png","res/empty.png","res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-shy1.png", 
       "res/scene5/Alice-shy2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/empty.png",
       "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
       "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/Alice-shy1.png",
       "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal1.png",
       "res/scene5/Alice-normal2.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", "res/scene5/demogigi1.png", 
       "res/scene5/demogigi1.png", "res/scene5/demogigi2.png", "res/empty.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", 
       "res/scene5/Alice-normal2.png", "res/scene5/Alice-normal1.png", "res/scene5/Alice-shower1.png", "res/scene5/Alice-shower1.png",
       "res/scene5/Alice-shower2.png", 
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
        "ก็ดีเหมือนกันนะ", "นายอยากกินอะไรมั้ย?", "ฉันกินได้หมดเลย ขอเเค่เป็นอาหารที่เธอทํา",
        "ฉันยังไงก็ได้", "นายนี่ละก็..", "อื้อโอเค", "เดี๋ยวฉันจะไปหาของเเถวนี้ก่อนนะ",
        "ให้ฉันไปด้วยมั้ย?", "ไม่เป็นไรหหรอก นายนั่งรออยู่ตรงนี้แหละ", "อือ..ก็ได้",
        "ตอนนี้เราน่าจะยังอยู่ในเขตป่าLifeอยู่นะ", "คงต้องใช้เวลาอีกนานเลยกว่าถึงจุดหมาย",
        "ดีเลย เราจะได้ใช้เวลานี้อยู่กับอริสมากขึ้น", "อร๊ายยยย", "..นั่นมัน..เสียงอริสนี่!!",
        "เกิดอะไรขึ้น อริส!!", "กะจะเดินเล่นเฉยๆ ดันมาเจอมนุษย์ซะงั้น", "เเต่ก็ดี ข้ากําลังหิวได้ที่เลย",
        "…(ชื่อตัวละครเรา)!!", "นี่เธอพะอจะบาดเจ็บตรงไหนรึปล่าว?", "ไม่ ฉันไม่เป็นไร",
        "อยู่ๆปีศาจมันก็เข้ามาโจมตีเเบบกระทันหัน", "เเต่ก็พอหลบได้ เลยมีเเผลถลอกนิดหน่อยหนะ",
        "นี่พ่อหนุ่ม ข้าขอเเม่หนูตรงนั้นได้ไหม?", "เเล้วข้าจะไว้ชีวิตเจ้า", "ไม่มีทาง!!",
        "(เขิน)", "มันใช่เวลามั้ย!! ไอ่บ้า!!", "ยังไงข้าก็จะกินพวกเจ้าทั้งสองอยู่ดี",
        "อริส ครั้งนี้ให้ฉันเป็นคนจัดการเอง", "จะไม่เป็นไรหรอ?", "เเค่นี้สบายมาก",
        "งั้นฝากด้วยนะ", "ตัวเราได้ใช้เวทย์โจมตี ปีศาจเดโมก่อน", "ปีศาจเดโมหลบได้ เเละพุ่งโจมตีใส่เราทันที",
        "เรากระโดดหลบ เเละปล่อยพลังเวทย์ที่รุนเเรงใส่ ปีศาจเดโม", "ปีศาจเดโมโดนพลังเวทย์เต็มๆ",
        "ปีศาจเดโม กลัวจะถูกกําจัด เลยใช้ม่านควันสีดําเเละหลบหนี", "พลาดท่าจนได้", "เเต่อย่างน้อยตอนนี้ก็คงจะปลอดภัยเเล้ว",
        "ฉันคิดว่าปีศาจตัวเมื่อกี้ต้องเกี่ยวข้องกับจอมมารเเน่เลย","นี่เรา..ยังไม่ได้กินข้าวกันเลยนี่หน่า","จริงด้วย! งั้นเดี๋ยวฉันรีบไปทําให้นะ",
        "หลังจากพักผ่อนเเละกินอะไรกันเสร็จเเล้ว","ฉันรู้สึกร้อนมากเลย..","จะไปอาบนํ้าไหมละ ดูเหมือนว่าจะมีลําธารใกล้ๆนะ",
        "จริงหรอ งั้นฉันขอไปอาบนํ้าก่อน","คนลามก!!...","โอเค เดี๋ยวมานะ","เเต่จู่ๆ ก็มีเสียง เเปลกๆที่พุ่มไม้","อริสเห็นเงาคนอยู่หลังพุ่มไม้..",
    };

    public part5() {
        setTitle("ISEKAI DEMO - Part 5");
        setSize(1280, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel();
        characterLabel.setBounds(190, 100, 900, 900); 
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        updateScene();
        
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
        if (isChoosing) return;
        if (isTyping) {
            typewriterTimer.stop();
            isTyping = false;
            dialogueArea.setText("<html><body style='width: 950px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        if (currentIndex == 11) { showChoices("ฉันกินได้หมดเลย ขอเเค่เป็นอาหารที่เธอทํา", "ฉันยังไงก็ได้", 12, 13); return; }
        if (currentIndex == 17) { showChoices("ฉันชอบอาหารฝีมือเธอที่สุดเลย", "ก็อร่อยดีนะ", 18, 19); return; }
        if (currentIndex == 38) { showChoices("พุ่งเข้าไปปกป้องอริส", "บอกให้อริสหลบเอง", 39, 40); return; }
        if (currentIndex == 57) { showChoices("เราจะไปเดทกันไงละจ๊ะ อริสจัง", "ที่อยู่ของจอมมารยังไงหละ ", 58, 59); return; }

        if (currentIndex < dialogues.length - 1) {
            currentIndex++;
            updateScene();
        } else {
            finishGame();
        }
    }

    private void finishGame() {
        UIManager.put("OptionPane.messageFont", THAI_FONT);
        JOptionPane.showMessageDialog(null, "จบ Part 5: การผจญภัยกำลังจะเริ่มขึ้น!");
        try {
            new part6().setVisible(true);
            dispose();
        } catch (Exception e) {
            System.exit(0);
        }
    }

    private void setupDialogueUI() {
        dialoguePanel = new RoundedPanel(50);
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(90, 520, 1100, 220); 
        dialoguePanel.setBackground(new Color(20, 20, 25, 215));
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel();
        nameLabel.setFont(THAI_FONT_BOLD);
        nameLabel.setForeground(new Color(255, 204, 0)); 
        nameLabel.setBounds(60, 25, 400, 45); 
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(THAI_FONT);
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 85, 980, 110); 
        dialoguePanel.add(dialogueArea);
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1280, 800));
        if (currentIndex < charPaths.length) characterLabel.setIcon(scaleImage(charPaths[currentIndex], 900, 900));
    }

    private void startTypewriter(String text) {
        if (typewriterTimer != null) typewriterTimer.stop();
        isTyping = true; charIndex = 0; dialogueArea.setText("");
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 950px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else { typewriterTimer.stop(); isTyping = false; }
        });
        typewriterTimer.start();
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true; 
        choiceButton1 = createChoiceButton(text1, 380, t1); 
        choiceButton2 = createChoiceButton(text2, 480, t2); 
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text);
        btn.setBounds(415, y, 450, 75); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 22));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 30, 35, 225)); 
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 2));
        btn.setFocusPainted(false);
        btn.addActionListener(e -> {
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false; currentIndex = target; updateScene(); 
        });
        return btn;
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) { return null; }
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(40, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) {
                alpha = 0; ((Timer)e.getSource()).stop();
                layeredPane.remove(fadeOverlay); updateScene(); 
            }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part5().setVisible(true));
    }
}

// --- คลาส RoundedPanel สำหรับวาดพื้นหลังกล่องข้อความขอบมน ---
class RoundedPanel extends JPanel {
    private int cornerRadius;
    public RoundedPanel(int radius) { this.cornerRadius = radius; setOpaque(false); }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }
}