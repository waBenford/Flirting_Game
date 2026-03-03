package flirting_game;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;
import javax.swing.*;

public class part2 extends JFrame {
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel; 
    private JButton choiceButton1, choiceButton2, choiceButton3; 
    private JPanel fadeOverlay;
    private JLabel affinityLabel, statusLabel;
    
    // Scoreboard Components (ปรับให้เหมือน Part 3)
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;

    private int currentIndex = 0;
    private int charIndex = 0;
    private boolean isAnimating = false;
    private boolean isChoosing = false;
    private float alpha = 1.0f;
    private Timer typewriterTimer;
    private boolean isFading = false;

    private Clip bgmClip;
    private Clip effectClip;

    private Map<String, ImageIcon> imageCache = new HashMap<>();
    private float charAlpha = 0.0f;
    private Timer charFadeTimer;

    private PrintWriter networkOut;

    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    // --- Data Arrays (คงเดิมตามเนื้อเรื่อง Part 2) ---
    private String[] imagePaths = {
        "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
        "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
        "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
        "res/scene2/s1.png", "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s2.png", 
        "res/scene2/s2.png", "res/scene2/s3.png", "res/scene2/s3.png", "res/scene2/s3.png", 
        "res/scene2/s4.png", "res/scene2/s4.png", "res/scene2/s4.png", "res/scene2/s5.png", 
        "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", 
        "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s6.png", "res/scene2/s6.png",
    };

    private String[] charPaths = {
        "res/scene2/alice1.png", "res/scene2/alice1.png", "res/scene2/alice1.png",
        "res/scene2/alice2.png", "res/scene2/alice1.png", "res/scene2/alice2.png",
        "res/scene2/alice1.png", "res/scene2/alice2.png", "res/scene2/alice1.png",
        "res/scene2/alice2.png", "res/scene2/alice1.png", "res/scene2/alice1.png", 
        "res/scene2/alice2.png","res/scene2/alice1.png", "res/scene2/alice2.png", 
        "res/empty.png", "res/empty.png", "res/empty.png","res/empty.png", 
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png", 
        "res/empty.png","res/empty.png", "res/empty.png", "res/empty.png"
    };

    private String[] names = {
        "ฉัน", "ฉัน", "เด็กผู้หญิง", "เด็กผู้หญิง", "ฉัน", "เด็กผู้หญิง", "อริส", //0-6
        "อริส", "อริส", "อริส", "อริส", "ฉัน", "อริส", "อริส", "ฉัน", 
        "อริส", "ฉัน", "ฉัน", "...", "ฉัน" //18
    };

    private String[] dialogues = {
        "..เอ่อ..เธอคือ ใครหรอ?", "..เเล้วนี่ฉันอยู่ที่ไหน..ฉันยังไม่ตายหรอ!?", "..เอ๋..ตายหรอ??",
        "ไม่เห็นจะมีใครตายเลยนะ", "นี่เรา..อยู่ที่ไหนกันเเน่นะ..", "อ๊ะ..ลืมเเนะนําตัวเลย", //0-5
        "ฉันชื่อว่า อริส เป็นลูกของชาวนาในหมู่บ้านเเถวนี้","เเล้วเธอหละ?",//7
        "งั้นหรอ เเล้วเธอมาจากไหนหรอ?",//8
        "เเปลกคนจัง..งั้นเธอมาจากไหนหละ?",//9
        "เออ...เธอมาจากที่ไหนหรอ?",//10
        "..ฉันก็ไม่รู้เหมือนกัน",//11
        "อืมม..งั้นเดี๋ยว..ไปที่บ้านฉันก่อนละกัน",//12
        "ตัวเธอสะบักสะบอมมากเลย", "ไปอาบนํ้าก่อนเลยนะ เดี๋ยวฉันจะเตรียมกับข้าวไว้ให้",
        "ห้องนํ้าไปทางไหนหรอ?", "ขึ้นบันไดไปเเล้วก็เลี้ยวขวาหนะ", "น่ารักเเถมยังใจดีอีกต่างหาก",//13-17
        "..ต่อจากนี้จะทําไงต่อดีนะ..เห้ออ..", "...", "เอื้อมมือไปหยิบ", "...",
        "ไม่เคยเห็นหนังสือเเบบนี้มาก่อนเลย", "..เปิดอ่าน..", "..การใช้เวทย์ลมขั้นพื้นฐาน..", //18-24
        "..พลังเวทย์อย่างงั้นนะหรอ..", "..ดูเหมือนจะมีวิธีการร่ายเวทย์ด้วย..",
        "..ลองหน่อยละกัน..ยังไงมันก็คงเป็นหนังสือที่ทําขึ้นมาเล่นๆ",
        "..สายลมที่พัดผ่าน..จงตอบรับเสียงของฉัน!!", "..Wind Dash!!",//25-29
        "..เห้ย!!..เมื่อกี้มันอะไร?!", "เกิดอะไรขึ้นหนะ!!"//31
    };

    public part2() {
        setTitle("ISEKAI DEMO - Part 2");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playSE("res/sound/soundtrack1.wav", true, -5.0f);
        playSE("res/sound/soonoo.wav", false, 5.0f);

        backgroundLabel = new JLabel(scaleImage(imagePaths[0], 1280, 800));
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel(scaleImage(charPaths[0], 900, 900)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        characterLabel.setBounds(190, 100, 900, 900); 
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        setupRelationshipUI();
        setupStatusOverlay(); // เรียกใช้ UI Scoreboard แบบ Part 3
        setupTabKeyBinding(); 
        setupFadeOverlay();
        initNetwork();

        startFadeIn();
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleInteraction();
            }
        });
    }

    private void setupTabKeyBinding() {
        layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "toggleScoreboard");
        layeredPane.getActionMap().put("toggleScoreboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusOverlay.setVisible(!statusOverlay.isVisible());
                if (statusOverlay.isVisible()) {
                    layeredPane.moveToFront(statusOverlay);
                }
            }
        });
    }

    private void setupStatusOverlay() {
        // ใช้เลย์เอาต์ BorderLayout ตาม Part 3
        statusOverlay = new JPanel();
        statusOverlay.setLayout(new BorderLayout(10, 10)); 
        statusOverlay.setBackground(new Color(0, 0, 0, 200)); 
        statusOverlay.setBounds(440, 150, 400, 400); 
        statusOverlay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        statusOverlay.setVisible(false);

        onlineCountLabel = new JLabel("ผู้เล่นออนไลน์: 1", SwingConstants.CENTER);
        onlineCountLabel.setForeground(Color.CYAN);
        onlineCountLabel.setFont(new Font("Tahoma", Font.BOLD, 20));

        JLabel titleLabel = new JLabel("--- ความสัมพันธ์ทั้งหมด ---", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 22));

        affinityStatusLabel = new JLabel("กำลังโหลดข้อมูล...", SwingConstants.CENTER);
        affinityStatusLabel.setForeground(Color.WHITE);
        affinityStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        affinityStatusLabel.setVerticalAlignment(SwingConstants.TOP);

        statusOverlay.add(titleLabel, BorderLayout.NORTH);
        statusOverlay.add(affinityStatusLabel, BorderLayout.CENTER);
        statusOverlay.add(onlineCountLabel, BorderLayout.SOUTH); 
        
        layeredPane.add(statusOverlay, JLayeredPane.DRAG_LAYER);
    }

    private void updateLeaderboardUI(String data) {
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'>");
        sb.append("<table width='320' style='color:white; font-family:Tahoma;'>");
        sb.append("<tr style='color:#FFD700;'><th>ผู้เล่น</th><th align='right'>คะแนน (อริส)</th></tr>");
        
        for (String p : data.split(",")) {
            if (p.contains("=")) {
                String[] parts = p.split("=");
                String name = parts[0];
                String rawScores = parts[1]; // เช่น "10/0"
                
                // --- แก้ไขตรงนี้: แยกเอาเฉพาะคะแนนแรกมาแสดง ---
                String aliceScore = rawScores;
                if (rawScores.contains("/")) {
                    aliceScore = rawScores.split("/")[0]; // เอาเฉพาะตัวหน้าเครื่องหมาย /
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

    private void handleInteraction() {
        if (isChoosing || isFading) return;
        if (isAnimating) { stopAnimation(); updateDialogueDisplay(dialogues[currentIndex]); return; }

        if (currentIndex == 7) {
            showChoices("ฉันไม่รู้ (ฉันยังไม่ไว้ใจใคร)", "ฉันจําชื่อตัวเองไม่ได้", "ฉันไม่ค่อยเเน่ใจเลย..", 8, 9, 10);
            return;
        }
        if (currentIndex == 8 || currentIndex == 9 || currentIndex == 10) {
            currentIndex = 11;
            updateScene();
            return;
        }

        if (currentIndex == 10 || currentIndex == 14 || currentIndex == 19) {
            performSceneFade(() -> {
                currentIndex++; syncIndex(); updateScene();
                if (currentIndex == 15) {
                    playEffect("res/sound/water.wav", 5.0f);
                    if (effectClip != null) effectClip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            });
            return;
        }

        if (currentIndex == 17) {
            performSceneFade(() -> {
                currentIndex++; syncIndex();
                if (effectClip != null) { effectClip.stop(); effectClip.close(); effectClip = null; }
                handleSoundEffects(currentIndex);
                updateScene();
            });
            return;
        }

        if (currentIndex < dialogues.length - 1) {
            currentIndex++; syncIndex(); handleSoundEffects(currentIndex); updateScene();
        } else {
            finishPart(); 
        }
    }

    private void syncIndex() {
        if (relationdata.isOnlineMode && networkOut != null) {
            networkOut.println("SYNC_INDEX:" + currentIndex);
        }
    }

    private void stopAllSounds() {
        if (bgmClip != null) { bgmClip.stop(); bgmClip.close(); bgmClip = null; }
        if (effectClip != null) { effectClip.stop(); effectClip.close(); effectClip = null; }
    }

    private void finishPart() {
        isFading = true;
        stopAllSounds(); 
        alpha = 0.0f;
        if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);

        Timer fadeOut = new Timer(30, e -> {
            alpha += 0.05f;
            if (alpha >= 1.0f) {
                alpha = 1.0f; ((Timer)e.getSource()).stop();
                Timer transitionTimer = new Timer(200, ev -> {
                    SwingUtilities.invokeLater(() -> {
                        new part3().setVisible(true);
                        dispose();
                    });
                });
                transitionTimer.setRepeats(false);
                transitionTimer.start();
            }
            fadeOverlay.repaint();
        });
        fadeOut.start();
    }

    private void handleSoundEffects(int index) {
        if (index == 8 ) playEffect("res/sound/soudesukaa.wav", 5.0f);
        else if (index == 11) playEffect("res/sound/fireplace.wav", 5.0f);
        else if (index == 18) {
            stopBGM();
            playSE("res/sound/soundtrack2.wav", true, -5.0f);
        }
        else if (index == 28) {
            playEffect("res/sound/winddash.wav", 0.0f);
            screenShake(10, 1000);
        }
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                Socket socket = new Socket(relationdata.serverIP, 5000);
                networkOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:2"); // เพิ่มบรรทัดนี้ครับ

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("SYNC_INDEX:")) {
                        int remoteIndex = Integer.parseInt(line.substring(11));
                        SwingUtilities.invokeLater(() -> {
                            if (remoteIndex != currentIndex) { currentIndex = remoteIndex; updateScene(); }
                        });
                    } else if (line.startsWith("ALL_STATS:")) {
                        // ส่งข้อมูลไปประมวลผลตาราง HTML
                        updateLeaderboardUI(line.substring(10));
                    }
                    if (line.startsWith("LOAD_AFFINITY:")) {
                        int score = Integer.parseInt(line.substring(14));
                        relationdata.aliceRel.setAffinity(score);
                        SwingUtilities.invokeLater(() -> {
                            // แก้ไขตรงนี้เช่นกันครับ
                            affinityLabel.setText("อริส: " + score); 
                            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
                        });
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox();
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(225, 520, 800, 200);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel();
        nameLabel.setFont(THAI_FONT_BOLD);
        nameLabel.setForeground(new Color(180, 40, 90));
        nameLabel.setBounds(60, 25, 400, 45);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 85, 700, 110);
        dialoguePanel.add(dialogueArea);

        JLabel nextArrow = new JLabel("▼");
        nextArrow.setFont(new Font("Tahoma", Font.BOLD, 20));
        nextArrow.setForeground(new Color(0, 153, 255));
        nextArrow.setBounds(740, 160, 30, 30);
        dialoguePanel.add(nextArrow);
        new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible())).start();
    }

    private void setupRelationshipUI() {
        // 1. ปรับตำแหน่งติดซ้ายบน (0, 0) และลดความสูงลงเพื่อให้พอดีกับคนเดียว
        JPanel relPanel = new JPanel(new GridLayout(2, 1, 0, 0)); 
        relPanel.setBounds(0, 0, 280, 75); 
        
        // 2. ใช้พื้นหลังสีดำโปร่งแสงเพื่อให้ข้อความอ่านง่ายบนทุกพื้นหลัง
        relPanel.setBackground(new Color(0, 0, 0, 190)); 
        relPanel.setOpaque(true);

        // 3. เพิ่มกรอบสีชมพูหนา 2 พิกเซล และใส่ระยะห่าง (Padding) ด้านใน
        relPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 105, 180), 2), // กรอบสีชมพู
            BorderFactory.createEmptyBorder(5, 15, 5, 10) // ระยะห่างจากขอบ
        ));

        // 4. ตั้งค่าการแสดงผลของ อริส (ใช้โทนสีเดียวกับ Part 7)
        affinityLabel = new JLabel("อริส: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 18)); 
        affinityLabel.setForeground(new Color(255, 192, 203)); // สีชมพูสว่าง

        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setForeground(Color.WHITE); // สีขาวอ่านง่าย

        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        
        // นำไปวางไว้ในเลเยอร์ POPUP เพื่อให้อยู่หน้าสุดเสมอ
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }

    private void setupFadeOverlay() {
        fadeOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fadeOverlay.setBounds(0, 0, 1280, 800);
        fadeOverlay.setOpaque(false);
        layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) animateText(dialogues[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(scaleImage(imagePaths[currentIndex], 1280, 800));
        if (currentIndex < charPaths.length) {
            characterLabel.setIcon(scaleImage(charPaths[currentIndex], 1200, 800));
            if (currentIndex == 0 || !charPaths[currentIndex].equals(charPaths[Math.max(0, currentIndex-1)])) {
                startCharacterFadeIn();
            }
        }
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    public void playEffect(String path, float volume) {
        try {
            if (effectClip != null && effectClip.isRunning()) effectClip.stop();
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                effectClip = AudioSystem.getClip();
                effectClip.open(audioIn);
                FloatControl gc = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
                gc.setValue(volume);
                effectClip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void playSE(String path, boolean isLoop, float volume) {
        try {
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                FloatControl gc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gc.setValue(volume);
                if (isLoop) { stopBGM(); bgmClip = clip; clip.loop(Clip.LOOP_CONTINUOUSLY); }
                clip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void stopBGM() {
        if (bgmClip != null) { bgmClip.stop(); bgmClip.close(); bgmClip = null; }
    }

    private void animateText(String fullText) {
        isAnimating = true; charIndex = 0; dialogueArea.setText("");
        if (typewriterTimer != null && typewriterTimer.isRunning()) typewriterTimer.stop();
        typewriterTimer = new Timer(20, e -> {
            if (charIndex <= fullText.length()) { updateDialogueDisplay(fullText.substring(0, charIndex)); charIndex++; } 
            else { stopAnimation(); }
        });
        typewriterTimer.start();
    }

    private void startFadeIn() {
        Timer fadeTimer = new Timer(50, e -> {
            alpha -= 0.05f;
            if (alpha <= 0) { alpha = 0; ((Timer)e.getSource()).stop(); layeredPane.remove(fadeOverlay); }
            fadeOverlay.repaint();
        });
        fadeTimer.start();
    }

    private void stopAnimation() { if (typewriterTimer != null) typewriterTimer.stop(); isAnimating = false; }

    private void updateDialogueDisplay(String text) {
        dialogueArea.setText("<html><body style='width: 700px;'>" + text + "</body></html>");
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try { return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)); } 
        catch (Exception e) { return null; }
    }

    private void showChoices(String text1, String text2, String text3, int target1, int target2, int target3) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 310, target1);
        choiceButton2 = createChoiceButton(text2, 380, target2);
        choiceButton3 = createChoiceButton(text3, 450, target3);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton3, JLayeredPane.POPUP_LAYER);

        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            // --- ตัวแปรสำหรับระบบ Animation ---
            private double scale = 1.0;
            private int alphaMod = 180; // ค่าความโปร่งใสพื้นหลัง
            private Timer animTimer;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // คำนวณจุดศูนย์กลางเพื่อทำ Scale Animation
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                g2.translate(centerX, centerY);
                g2.scale(scale, scale);
                g2.translate(-centerX, -centerY);

                // วาดพื้นหลังโค้งมน (สีจะสว่างขึ้นเมื่อ Hover)
                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // วาดเส้นขอบสีชมพู
                g2.setColor(new Color(225, 105, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);

                g2.dispose();
                super.paintComponent(g);
            }

            {
                // ใส่ Event การเคลื่อนไหวของเมาส์
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        startAnimation(1.05, 230); // ขยายตัวขึ้นและสว่างขึ้น
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        startAnimation(1.0, 180); // กลับสู่ขนาดปกติ
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        scale = 0.95; // เอฟเฟกต์ปุ่มยุบตอนคลิก
                        repaint();
                    }
                });
            }

            private void startAnimation(double targetScale, int targetAlpha) {
                if (animTimer != null && animTimer.isRunning()) animTimer.stop();
                animTimer = new Timer(15, ev -> {
                    // ค่อยๆ ปรับขนาด (Smooth Scale)
                    if (scale < targetScale) scale += 0.01;
                    else if (scale > targetScale) scale -= 0.01;

                    // ค่อยๆ ปรับความสว่างพื้นหลัง
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

        // --- ตั้งค่าคุณสมบัติพื้นฐานของปุ่ม ---
        btn.setBounds(800, y, 350, 60);
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- Logic การกดปุ่มและระบบ Affinity ---
        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            // ลบปุ่มออกเมื่อเลือกแล้ว
            layeredPane.remove(choiceButton1); 
            layeredPane.remove(choiceButton2);
            layeredPane.remove(choiceButton3);
            isChoosing = false;

            // ตรวจสอบเงื่อนไขคะแนนความสนิท (อ้างอิงจาก target ที่ส่งมา)
            if (target == 8 || target == 9 || target == 10) {
                System.out.println("Neutral Choice: No affinity change.");
            }

            // ส่งข้อมูลไปยัง Server (ถ้าเปิด Online Mode)
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                networkOut.println("SYNC_INDEX:" + target);
            }

            // อัปเดตการแสดงผลคะแนนบนหน้าจอ
            if (affinityLabel != null) {
                affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            }
            if (statusLabel != null) {
                statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            }

            currentIndex = target; 
            updateScene();
        });

        return btn;
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
            } else { setLocation(originalLoc); ((Timer) e.getSource()).stop(); }
        });
        shakeTimer.start();
    }

    private void startCharacterFadeIn() {
        charAlpha = 0.0f;
        if (charFadeTimer != null && charFadeTimer.isRunning()) charFadeTimer.stop();
        charFadeTimer = new Timer(30, e -> {
            charAlpha += 0.04f;
            if (charAlpha >= 1.0f) { charAlpha = 1.0f; ((Timer)e.getSource()).stop(); }
            characterLabel.repaint();
        });
        charFadeTimer.start();
    }

    private void performSceneFade(Runnable onBlack) {
        isFading = true; alpha = 0.0f;
        if (fadeOverlay.getParent() == null) layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        Timer fadeOut = new Timer(30, null);
        fadeOut.addActionListener(e -> {
            alpha += 0.05f;
            if (alpha >= 1.0f) {
                alpha = 1.0f; fadeOut.stop(); onBlack.run();
                new Timer(600, ev -> {
                    ((Timer)ev.getSource()).stop();
                    Timer fadeIn = new Timer(30, eve -> {
                        alpha -= 0.05f;
                        if (alpha <= 0) { alpha = 0; ((Timer)eve.getSource()).stop(); isFading = false; }
                        fadeOverlay.repaint();
                    });
                    fadeIn.start();
                }).start();
            }
            fadeOverlay.repaint();
        });
        fadeOut.start();
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new part2().setVisible(true)); }
}

/*class VisualNovelBox extends JPanel {
    private int cornerRadius = 30;
    public VisualNovelBox() { setOpaque(false); }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(245, 250, 255, 180), 
            0, getHeight(), new Color(255, 235, 245, 230)
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2d.setColor(new Color(255, 150, 200, 200));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);
        g2d.dispose();
    }
}*/