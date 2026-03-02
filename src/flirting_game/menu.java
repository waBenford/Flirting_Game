package flirting_game; // ต้องมีบรรทัดนี้ตามโครงสร้างโฟลเดอร์ของคุณ

import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*; // สำหรับระบบเสียง

public class menu {
    public static void main(String[] args) {
        // --- เริ่มเล่นเพลงประกอบทันทีที่เปิดโปรแกรม ---
        SoundManager.playBGM("res/sound/bgm.wav"); 

        JFrame frame = new JFrame("Isekai Lover");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 600);
        frame.setResizable(false);
        
        JLayeredPane lp = new JLayeredPane();
        frame.setContentPane(lp);

        // --- 1. Background ---
        // ใช้ระบบย่อรูปที่คุณทำไว้เพื่อป้องกันรูปใหญ่เกิน
        ImageIcon bgOriginal = new ImageIcon("res/menu/bg.png");
        Image bgImg = bgOriginal.getImage().getScaledInstance(1024, 600, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setBounds(0, 0, 1024, 600);
        lp.add(background, JLayeredPane.DEFAULT_LAYER);

        // --- 2. Logo ---
        int logoW = 700;
        int logoH = 350;
        JLabel logoLabel = new JLabel(getScaledIcon("res/menu/Logo.png", logoW, logoH));
        logoLabel.setBounds(170, 20, logoW, logoH);
        lp.add(logoLabel, JLayeredPane.PALETTE_LAYER);

        // --- 3. Buttons ---
        int btnW = 250; // ลดจาก 300 เหลือ 250 เพื่อไม่ให้เบียดกันเกินไป
        int btnH = 80;  // ลดจาก 100 เหลือ 80 เพื่อให้วาง 3 แถวได้พอดี
        
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        
        // ปรับ Bounds: 
        // x: 212 (กึ่งกลางของ 1024 เมื่อกว้าง 600)
        // y: 320 (ขยับลงมาข้างล่างเพื่อให้พ้นตัวอักษร Logo)
        // width: 600, height: 280 (ขนาดพื้นที่สำหรับวางปุ่ม 3 แถว)
        buttonPanel.setBounds(212, 320, 600, 280); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15); // เว้นระยะห่างระหว่างปุ่ม
        gbc.fill = GridBagConstraints.NONE;

        JButton startBtn = createImageButton("res/buttons/buttonStart.png", btnW, btnH);
        JButton onlineBtn = createImageButton("res/buttons/buttonOnline.png", btnW, btnH);
        JButton galleryBtn = createImageButton("res/buttons/buttonGallery.png", btnW, btnH);
        JButton settingBtn = createImageButton("res/buttons/buttonSetting.png", btnW, btnH);
        JButton exitBtn = createImageButton("res/buttons/buttonExit.png", btnW, btnH);

        // --- ฟังก์ชันปุ่ม Setting (ระบบปรับเสียง) ---
        settingBtn.addActionListener(e -> {
            showSettings(frame);
        });

        // --- ฟังก์ชันปุ่ม Online ---
        onlineBtn.addActionListener(e -> {
            UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.PLAIN, 18));
            String name = JOptionPane.showInputDialog(frame, "กรุณาใส่ชื่อของคุณเพื่อเข้าเล่นออนไลน์:", "Join Online", JOptionPane.QUESTION_MESSAGE);
            
            if (name != null && !name.trim().isEmpty()) {
                //สั่งหยุดเพลงก่อนไปหน้าถัดไป
                SoundManager.stopBGM();
                // เก็บค่าลง relationdata 
                // relationdata.isOnlineMode = true;
                // relationdata.playerName = name.trim();
                new part1().setVisible(true); 
                frame.dispose();
            } else if (name != null) {
                JOptionPane.showMessageDialog(frame, "กรุณาใส่ชื่อก่อนเข้าเล่นครับ");
            }
        });

        startBtn.addActionListener(e -> {
            try {
                //สั่งหยุดเพลงก่อนไปหน้าถัดไป
                SoundManager.stopBGM();

                new part1().setVisible(true);
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "ยังไม่ได้สร้างคลาส part1 หรือมีข้อผิดพลาด");
            }
        });

        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "คุณต้องการออกจากเกมใช่หรือไม่?", "ยืนยัน", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // --- การจัดวาง (GridBagLayout) ---
        
        // แถวที่ 1: Start (ซ้าย) | Online (ขวา)
        gbc.gridy = 0;
        gbc.gridx = 0; gbc.gridwidth = 1;
        buttonPanel.add(startBtn, gbc);
        gbc.gridx = 1;
        buttonPanel.add(onlineBtn, gbc);

        // แถวที่ 2: Setting (ซ้าย) | Gallery (ขวา)
        gbc.gridy = 1;
        gbc.gridx = 0;
        buttonPanel.add(settingBtn, gbc);
        gbc.gridx = 1;
        buttonPanel.add(galleryBtn, gbc);

        // แถวที่ 3: Exit (ตรงกลาง)
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // สั่งให้ปุ่มครอบคลุมพื้นที่ 2 คอลัมน์
        gbc.anchor = GridBagConstraints.CENTER; // จัดให้อยู่กึ่งกลาง
        buttonPanel.add(exitBtn, gbc);
        
        lp.add(buttonPanel, JLayeredPane.MODAL_LAYER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // --- ฟังก์ชันแสดงหน้าต่าง Setting ---
    private static void showSettings(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Settings", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 30));

        JLabel volLabel = new JLabel("Music Volume: " + SoundManager.getCurrentVolumePercentage() + "%"); 
        volLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        dialog.add(volLabel);

        // แถบเลื่อนปรับเสียง 0-100 (ห้ามพิมพ์ min:, max: ลงไป)
        JSlider volumeSlider = new JSlider(0, 100, SoundManager.getCurrentVolumePercentage());
        volumeSlider.setPreferredSize(new Dimension(300, 50));

        volumeSlider.addChangeListener(e -> {
            float volume = volumeSlider.getValue() / 100f;
            SoundManager.setVolume(volume);
        });
        dialog.add(volumeSlider);

        volumeSlider.addChangeListener(e -> {
            int value = volumeSlider.getValue();
            float volume = value / 100f;
            
            // ถ้าเลื่อนเป็น 0 ให้เปลี่ยนข้อความเป็นปิดเสียง 
            if (value == 0) {
                volLabel.setText("Music Volume: Muted ");
            } else {
                volLabel.setText("Music Volume: " + value + "% ");
            }
        
            // ส่งค่าไปที่ระบบเสียงจริง
            SoundManager.setVolume(volume);
    });

        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        dialog.add(closeBtn);

        dialog.setVisible(true);
    }

    private static ImageIcon getScaledIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getIconWidth() == -1) return new ImageIcon();
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    private static JButton createImageButton(String path, int w, int h) {
        // โหลดรูปภาพเตรียมไว้
        final Image img = new ImageIcon(path).getImage();

        // สร้างปุ่มแบบกำหนดเองที่มีตัวแปร scale และ timer เป็นของตัวเอง
        JButton button = new JButton() {
            private double scale = 1.0;
            private Timer animTimer;

            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));

                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        startAnimation(1.1); // ขยาย 10%
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        startAnimation(1.0); // กลับสู่ขนาดปกติ
                    }
                });
            }

            private void startAnimation(double targetScale) {
                if (animTimer != null && animTimer.isRunning()) animTimer.stop();
                animTimer = new Timer(15, e -> {
                    if (Math.abs(scale - targetScale) < 0.01) {
                        scale = targetScale;
                        ((Timer)e.getSource()).stop();
                    } else {
                        scale += (targetScale - scale) * 0.2; // Easing effect
                    }
                    repaint();
                });
                animTimer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int dw = (int) (w * scale);
                int dh = (int) (h * scale);
                int x = (getWidth() - dw) / 2;
                int y = (getHeight() - dh) / 2;

                g2.drawImage(img, x, y, dw, dh, null);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension((int)(w * 1.2), (int)(h * 1.2)); // เผื่อพื้นที่ขยาย
            }
        };

        return button;
    }
}

// --- คลาสจัดการเสียง (Sound Manager) ---
class SoundManager {
    private static Clip clip;
    private static FloatControl volumeControl;
    // 1. เก็บค่าระดับเสียงเป็นตัวแปรกลาง (0.0 ถึง 1.0) เริ่มต้นที่ 0.5 (50%)
    private static float globalVolume = 0.5f; 

    public static void playBGM(String path) {
        try {
            stopBGM(); // สั่งหยุดของเก่าก่อน
            File file = new File(path);
            if (!file.exists()) return;
            
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(stream);
            
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // 2. ทันทีที่เล่นเพลงใหม่ ให้ใช้ระดับเสียงปัจจุบันทันที!
                applyVolume(globalVolume); 
            }
            
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. ฟังก์ชันปรับเสียงที่ใช้สูตร Logarithmic เพื่อความนุ่มนวล
    public static void setVolume(float volume) {
        globalVolume = volume; // จำค่าไว้
        applyVolume(volume);   // สั่งปรับเสียงของเพลงที่เล่นอยู่ตอนนี้
    }

    private static void applyVolume(float volume) {
        if (volumeControl != null) {
            float volumeAdjusted = Math.max(volume, 0.0001f); 
            float dB = (float) (Math.log10(volumeAdjusted) * 20);
            
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            if (dB < min) dB = min;
            if (dB > max) dB = max;
            
            volumeControl.setValue(dB);
        }
    }

    public static void stopBGM() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    // 4. ไว้สำหรับดึงค่าไปโชว์ในแถบ Slider ตอนเปิดหน้า Setting
    public static int getCurrentVolumePercentage() {
        return (int)(globalVolume * 100);
    }
}