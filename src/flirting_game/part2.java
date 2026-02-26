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
import javax.sound.sampled.*;
import javax.swing.*;

public class part2 extends JFrame {
    // --- UI Components ---
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel; 
    private JButton choiceButton1, choiceButton2;
    private JPanel fadeOverlay;
    private JLabel affinityLabel, statusLabel;
    
    // UI สำหรับระบบ Tab Leaderboard
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;

    // --- Game Logic State ---
    private int currentIndex = 0;
    private int charIndex = 0;
    private boolean isAnimating = false;
    private boolean isChoosing = false;
    private float alpha = 1.0f;
    private Timer typewriterTimer;

    // --- Audio ---
    private Clip bgmClip;
    private Clip effectClip;

    // --- Networking ---
    private PrintWriter networkOut;

    private Map<String, ImageIcon> imageCache = new HashMap<>();
    private float charAlpha = 0.0f;
    private Timer charFadeTimer;
    private boolean isFading = false;

    private final Font THAI_FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 28);

    // --- Data Arrays ---
    private String[] imagePaths = {
        "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
        "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png",
        "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s1.png", "res/scene2/s2.png",
        "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s2.png", "res/scene2/s3.png",
        "res/scene2/s3.png", "res/scene2/s3.png", "res/scene2/s4.png", "res/scene2/s4.png",
        "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png",
        "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png", "res/scene2/s5.png",
        "res/scene2/s6.png", "res/scene2/s6.png"
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
        "ฉัน", "ฉัน", "เด็กผู้หญิง", "เด็กผู้หญิง", "ฉัน", "เด็กผู้หญิง", "อริส",
        "อริส", "อริส", "อริส", "อริส", "อริส", "อริส", "ฉัน", "อริส", "ฉัน", "ฉัน", "...", "ฉัน"
    };

    private String[] dialogues = {
        "..เอ่อ..เธอคือ ใครหรอ?", "..เเล้วนี่ฉันอยู่ที่ไหน..ฉันยังไม่ตายหรอ!?", "..เอ๋..ตายหรอ??",
        "ไม่เห็นจะมีใครตายเลยนะ", "นี่เรา..อยู่ที่ไหนกันเเน่นะ..", "อ๊ะ..ลืมเเนะนําตัวเลย",
        "ฉันชื่อว่า อริส เป็นลูกของชาวนาในหมู่บ้านเเถวนี้", "เเล้วเธอหละ?", "งั้นหรอ...ไม่เป็นไร",
        "อ้าว... จำชื่อไม่ได้หรอ? ไม่เป็นไรนะ ค่อยๆ นึกไปก็ได้", "อืมม..งั้นเดี๋ยว..ไปที่บ้านฉันก่อนละกัน",
        "ตัวเธอสะบักสะบอมมากเลย", "ไปอาบนํ้าก่อนเลยนะ เดี๋ยวฉันจะเตรียมกับข้าวไว้ให้",
        "ห้องนํ้าไปทางไหนหรอ?", "ขึ้นบันไดไปเเล้วก็เลี้ยวขวาหนะ", "น่ารักเเถมยังใจดีอีกต่างหาก",
        "..ต่อจากนี้จะทําไงต่อดีนะ..เห้ออ..", "...", "เอื้อมมือไปหยิบ", "...",
        "ไม่เคยเห็นหนังสือเเบบนี้มาก่อนเลย", "..เปิดอ่าน..", "..การใช้เวทย์ลมขั้นพื้นฐาน..",
        "..พลังเวทย์อย่างงั้นนะหรอ..", "..ดูเหมือนจะมีวิธีการร่ายเวทย์ด้วย.. ",
        "..ลองหน่อยละกัน..ยังไงมันก็คงเป็นหนังสือที่ทําขึ้นมาเล่นๆ",
        "..สายลมที่พัดผ่าน..จงตอบรับเสียงของฉัน!!", "..Wind Dash!!",
        "..เห้ย!!..เมื่อกี้มันอะไร?!", "เกิดอะไรขึ้นหนะ!!"
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

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel() {
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

        // --- ลำดับการเรียกเมธอด (สำคัญมากเพื่อป้องกัน NPE) ---
        setupDialogueUI();
        setupRelationshipUI();
        setupStatusOverlay(); 
        setupTabKeyBinding(); 
        setupFadeOverlay();
        initNetwork();

        this.addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                // เมื่อกลับมาที่หน้าต่างเกม (ไม่ต้องทำอะไรเป็นพิเศษ)
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {
                // เมื่อผู้เล่นพับจอ, Alt-Tab หรือคลิกไปหน้าต่างอื่น
                if (statusOverlay != null && statusOverlay.isVisible()) {
                    statusOverlay.setVisible(false); // สั่งซ่อนทันที
                }
            }
        });

        startFadeIn();
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleInteraction();
            }
        });
    }

    private void initNetwork() {
        // 1. ตรวจสอบว่าเปิดโหมดออนไลน์ไว้หรือไม่
        if (!relationdata.isOnlineMode) return;
        
        new Thread(() -> {
            try {
                // 2. เชื่อมต่อไปยัง Server ตาม IP ที่ตั้งไว้
                Socket socket = new Socket(relationdata.serverIP, 5000);
                networkOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // --- ขั้นตอนสำคัญ: ส่งข้อมูลเพื่อระบุตัวตนและพาร์ท ---
                
                // 3. ส่งชื่อไปบอก Server (Server จะเตะชื่อซ้ำออกให้อัตโนมัติ)
                networkOut.println("SET_NAME:" + relationdata.playerName);
                
                // 4. ส่งเลขพาร์ทไปบอก Server (เพื่อให้เริ่มที่ Index 0 ของพาร์ท 2)
                networkOut.println("SET_PART:2"); 

                String line;
                // ใช้ try-with-resources หรือจัดการการอ่านข้อมูล
                while ((line = in.readLine()) != null) {
                    // A. รับการซิงค์ฉาก
                    if (line.startsWith("SYNC_INDEX:")) {
                        int remoteIndex = Integer.parseInt(line.substring(11));
                        SwingUtilities.invokeLater(() -> {
                            if (remoteIndex != currentIndex) {
                                currentIndex = remoteIndex;
                                updateScene(); 
                            }
                        });
                    }
                    // B. รับจำนวนผู้เล่นออนไลน์
                    else if (line.startsWith("PLAYER_COUNT:")) {
                        relationdata.onlinePlayerCount = Integer.parseInt(line.substring(13));
                        SwingUtilities.invokeLater(() -> {
                            if (onlineCountLabel != null) {
                                onlineCountLabel.setText("ผู้เล่นออนไลน์: " + relationdata.onlinePlayerCount);
                            }
                        });
                    }
                    // C. อัปเดต Leaderboard (กรองค่า null จาก Server แล้ว)
                    else if (line.startsWith("ALL_STATS:")) {
                        updateLeaderboardUI(line.substring(10));
                    }
                }
            } catch (java.net.SocketException se) {
                // จัดการ Error ตอนปิดพาร์ท เพื่อไม่ให้ Terminal ขึ้นตัวแดง
                System.out.println("ระบบออนไลน์พาร์ท 2 ปิดการเชื่อมต่อแล้ว");
            } catch (Exception e) {
                System.err.println("Network Error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void handleInteraction() {
        if (isChoosing || isFading) return;
        if (isAnimating) {
            stopAnimation();
            updateDialogueDisplay(dialogues[currentIndex]);
            return;
        }

        if (currentIndex == 7) {
            showChoices("ฉันไม่รู้ (ฉันยังไม่ไว้ใจใคร)", "ฉันจําชื่อตัวเองไม่ได้", 8, 9);
            return;
        }

        int nextIndex = (currentIndex == 8) ? 10 : currentIndex + 1;

        if (nextIndex == 11 || nextIndex == 15 || nextIndex == 20 || currentIndex == 17) {
            performSceneFade(() -> {
                currentIndex = nextIndex;
                syncOnline(); // แจ้งตำแหน่งฉากไปที่ Server
                updateScene();
                if (currentIndex == 15) playEffect("res/sound/water.wav", 5.0f);
            });
            return;
        }

        if (nextIndex < dialogues.length) {
            currentIndex = nextIndex;
            syncOnline();
            handleSoundEffects(currentIndex);
            updateScene();
        } else {
            finishPart();
        }
    }

    private void syncOnline() {
        if (relationdata.isOnlineMode && networkOut != null) {
            networkOut.println("SYNC_INDEX:" + currentIndex);
        }
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 380, t1);
        choiceButton2 = createChoiceButton(text2, 450, t2);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(new Color(225, 105, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBounds(800, y, 350, 60);
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setBackground(new Color(255, 255, 255, 180));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            layeredPane.remove(choiceButton1); layeredPane.remove(choiceButton2);
            isChoosing = false;
            if (target == 9) relationdata.aliceRel.addAffinity(10);
            else if (target == 8) relationdata.aliceRel.decreaseAffinity(5);

            // ส่งคะแนนและฉากใหม่ไปที่ Server
            if (relationdata.isOnlineMode && networkOut != null) {
                networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                networkOut.println("SYNC_INDEX:" + target);
            }

            affinityLabel.setText("ความสนิท: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            currentIndex = target; updateScene();
        });
        return btn;
    }

    private void setupStatusOverlay() {
        statusOverlay = new JPanel(new BorderLayout(15, 15));
        // ปรับสีพื้นหลังให้ดำโปร่งใส (Alpha 180) และขอบสีทอง
        statusOverlay.setBackground(new Color(20, 20, 25, 180)); 
        statusOverlay.setBounds(440, 150, 400, 450); // เพิ่มความสูง
        statusOverlay.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        statusOverlay.setVisible(false);

        onlineCountLabel = new JLabel("ONLINE: 1", SwingConstants.CENTER);
        onlineCountLabel.setForeground(new Color(0, 255, 255));
        onlineCountLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        affinityStatusLabel = new JLabel("", SwingConstants.CENTER);
        
        JLabel title = new JLabel("🏆 Leaderboard", SwingConstants.CENTER);
        title.setForeground(Color.YELLOW);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        statusOverlay.add(title, BorderLayout.NORTH);
        statusOverlay.add(new JScrollPane(affinityStatusLabel) {{ 
            getViewport().setOpaque(false); setOpaque(false); setBorder(null); 
        }}, BorderLayout.CENTER); // ใส่ ScrollPane กันรายชื่อยาวเกินจอ
        statusOverlay.add(onlineCountLabel, BorderLayout.SOUTH);
        
        layeredPane.add(statusOverlay, JLayeredPane.DRAG_LAYER);
    }

    private void setupTabKeyBinding() {
        // ใช้คำสั่งนี้เพื่อให้โปรแกรมรับคำสั่งจาก Keyboard ได้แน่นอนขึ้น
        layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "toggleTab");
        layeredPane.getActionMap().put("toggleTab", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // บังคับสลับสถานะการมองเห็น
                statusOverlay.setVisible(!statusOverlay.isVisible()); 
            }
        });
    }

    private void updateLeaderboardUI(String data) {
        // ใช้ HTML Table เพื่อจัดคอลัมน์ให้ตรงกัน
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'>");
        sb.append("<table width='300' style='color:white; font-family:Tahoma;'>");
        sb.append("<tr style='color:#FFD700;'><th>ผู้เล่น</th><th align='right'>คะแนน</th></tr>");
        
        String[] players = data.split(",");
        for (String p : players) {
            if (!p.isEmpty() && p.contains("=")) {
                String[] parts = p.split("=");
                String name = parts[0];
                String score = parts[1];
                
                // ไฮไลต์ชื่อตัวเองเป็นสีเขียว ถ้าชื่อตรงกับที่เราตั้งไว้
                String nameColor = name.equals(relationdata.playerName) ? "#00FF7F" : "#FFFFFF";
                
                sb.append("<tr>")
                .append("<td style='color:").append(nameColor).append(";'>").append(name).append("</td>")
                .append("<td align='right' style='color:#FF69B4;'>").append(score).append(" pt</td>")
                .append("</tr>");
            }
        }
        sb.append("</table></body></html>");
        
        SwingUtilities.invokeLater(() -> {
            if (affinityStatusLabel != null) {
                affinityStatusLabel.setText(sb.toString());
                affinityStatusLabel.setVerticalAlignment(SwingConstants.TOP); // ให้รายชื่อเริ่มจากข้างบน
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
        nameLabel.setBounds(60, 25, 400, 45);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 85, 980, 110);
        dialoguePanel.add(dialogueArea);
    }

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(2, 1));
        relPanel.setBounds(25, 25, 300, 70);
        relPanel.setOpaque(false);
        affinityLabel = new JLabel("ความสนิท: " + relationdata.aliceRel.getAffinity());
        affinityLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        affinityLabel.setForeground(Color.WHITE);
        statusLabel = new JLabel("สถานะ: " + relationdata.aliceRel.getStatus());
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        statusLabel.setForeground(new Color(255, 204, 0));
        relPanel.add(affinityLabel); relPanel.add(statusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }

    private void setupFadeOverlay() {
        fadeOverlay = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
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
            characterLabel.setIcon(scaleImage(charPaths[currentIndex], 900, 900));
            characterLabel.setBounds(190, 0, 900, 900);
            if (currentIndex == 0 || !charPaths[currentIndex].equals(charPaths[Math.max(0, currentIndex-1)])) startCharacterFadeIn();
        }
    }

    private void handleSoundEffects(int index) {
        if (index == 8 || index == 9) playEffect("res/sound/soudesukaa.wav", 5.0f);
        else if (index == 11) playEffect("res/sound/fireplace.wav", 5.0f);
        else if (index == 18) { stopBGM(); playSE("res/sound/soundtrack2.wav", true, -5.0f); }
        else if (index == 28) { playEffect("res/sound/winddash.wav", 0.0f); screenShake(10, 1000); }
    }

    public void playEffect(String path, float volume) {
        try {
            if (effectClip != null && effectClip.isRunning()) effectClip.stop();
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                effectClip = AudioSystem.getClip(); effectClip.open(audioIn);
                FloatControl gc = (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
                gc.setValue(volume); effectClip.start();
            }
        } catch (Exception e) {}
    }

    public void playSE(String path, boolean loop, float volume) {
        try {
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream ai = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip(); clip.open(ai);
                FloatControl gc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gc.setValue(volume);
                if (loop) { bgmClip = clip; clip.loop(Clip.LOOP_CONTINUOUSLY); }
                clip.start();
            }
        } catch (Exception e) {}
    }

    private void stopBGM() { if (bgmClip != null && bgmClip.isRunning()) { bgmClip.stop(); bgmClip.close(); } }

    private void animateText(String fullText) {
        isAnimating = true; charIndex = 0; dialogueArea.setText("");
        if (typewriterTimer != null && typewriterTimer.isRunning()) typewriterTimer.stop();
        typewriterTimer = new Timer(20, e -> {
            if (charIndex <= fullText.length()) {
                updateDialogueDisplay(fullText.substring(0, charIndex++));
            } else { stopAnimation(); }
        });
        typewriterTimer.start();
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
        dialogueArea.setText("<html><body style='width: 950px;'>" + text + "</body></html>");
    }

    public ImageIcon scaleImage(String path, int width, int height) {
        try { return new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)); }
        catch (Exception e) { return null; }
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
        Timer fadeOut = new Timer(30, e -> {
            alpha += 0.05f;
            if (alpha >= 1.0f) {
                alpha = 1.0f; ((Timer)e.getSource()).stop();
                onBlack.run();
                Timer waitTimer = new Timer(600, ev -> {
                    ((Timer)ev.getSource()).stop();
                    Timer fadeIn = new Timer(30, eve -> {
                        alpha -= 0.05f;
                        if (alpha <= 0) { alpha = 0; ((Timer)eve.getSource()).stop(); isFading = false; }
                        fadeOverlay.repaint();
                    });
                    fadeIn.start();
                });
                waitTimer.setRepeats(false); waitTimer.start();
            }
            fadeOverlay.repaint();
        });
        fadeOut.start();
    }

    private void finishPart() {
        try { if(networkOut != null) networkOut.close(); } catch(Exception e) {}
        stopBGM();
        UIManager.put("OptionPane.messageFont", THAI_FONT_PLAIN);
        JOptionPane.showMessageDialog(null, "จบ Part 2 แล้ว! กำลังเข้าสู่บทถัดไป...");
        new part3().setVisible(true); dispose();
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new part2().setVisible(true)); }
}

class VisualNovelBox extends JPanel {
    public VisualNovelBox() { setOpaque(false); }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(new GradientPaint(0, 0, new Color(245, 250, 255, 180), 0, getHeight(), new Color(255, 235, 245, 230)));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2d.setColor(new Color(255, 150, 200, 200));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 30, 30);
        g2d.dispose();
    }
}