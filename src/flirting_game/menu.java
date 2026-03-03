package flirting_game; // ต้องมีบรรทัดนี้ตามโครงสร้างโฟลเดอร์ของคุณ

import java.awt.*;
import java.awt.image.BufferedImage;
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

        
        // --- 3. Buttons (สร้างปุ่มไว้ด้านบนสุดเสมอ!) ---
     // --- 3. Buttons Setup ---
        int btnW = 250;
        int btnH = 155;
        
        // กำหนดขนาดพื้นที่ปุ่มให้ใหญ่พอสำหรับอนิเมชันขยายตัวตอนชี้เมาส์
        int buttonBoundsW = btnW + 10;
        int buttonBoundsH = btnH + 10;

        JButton startBtn   = createImageButton("res/buttons/buttonStart.png", btnW, btnH);
        JButton onlineBtn  = createImageButton("res/buttons/buttonOnline.png", btnW, btnH);
        JButton galleryBtn = createImageButton("res/buttons/buttonGallery.png", btnW, btnH);
        JButton exitBtn    = createImageButton("res/buttons/buttonExit.png", btnW, btnH);

        // --- 4. สร้าง Panel (null layout) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setOpaque(false);

        // ปรับขนาด Panel ให้กว้างพอสำหรับปุ่ม
        buttonPanel.setBounds(232, 180, 560, 350); 

        // ระยะห่างที่กิมต้องการ (ปรับตามค่าเดิมที่กิมตั้งไว้)
        int gapX = -10; 
        int gapY = -80;
        JButton settingBtn = createImageButton("res/buttons/buttonSetting.png", btnW, btnH);

        // แถวที่ 1
        startBtn.setBounds(70, 0, buttonBoundsW, buttonBoundsH);
        onlineBtn.setBounds(btnW + gapX, 0, buttonBoundsW, buttonBoundsH);

        // แถวที่ 2
        galleryBtn.setBounds(70, btnH + gapY, buttonBoundsW, buttonBoundsH);
        settingBtn.setBounds(btnW + gapX, btnH + gapY, buttonBoundsW, buttonBoundsH);

        // แถวที่ 3 (Exit)
        int centerX = (560 - buttonBoundsW) / 2;
        exitBtn.setBounds(centerX, (btnH + gapY) * 2, buttonBoundsW, buttonBoundsH);

        // *** สำคัญ: แอดปุ่มจาก "ล่างขึ้นบน" เพื่อไม่ให้ Hitbox ปุ่มบนไปบังปุ่มล่าง ***
        buttonPanel.add(exitBtn);
        buttonPanel.add(settingBtn);
        buttonPanel.add(galleryBtn);
        buttonPanel.add(onlineBtn);
        buttonPanel.add(startBtn);

        lp.add(buttonPanel, JLayeredPane.MODAL_LAYER);
        

        // --- ฟังก์ชันปุ่ม Gallery ---
        galleryBtn.addActionListener(e -> {
            SoundManager.stopBGM(); // หยุดเพลงเมนู
            new GalleryPage().setVisible(true); // เปิดหน้า Gallery
            frame.dispose(); // ปิดหน้าเมนู
        });

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
                relationdata.isOnlineMode = true;
                relationdata.playerName = name.trim();
                new part1().setVisible(true); 
                frame.dispose();
            } else if (name != null) {
                JOptionPane.showMessageDialog(frame, "กรุณาใส่ชื่อก่อนเข้าเล่น");
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
        ImageIcon normalIcon = getScaledIcon(path, w, h);
        ImageIcon hoverIcon = getScaledIcon(path, w + 10, h + 10); // เผื่อที่ให้ขยายเยอะขึ้นนิดนึง

        // สร้าง BufferedImage เพื่อเช็ค Pixel (Hitbox แบบละเอียด)
        BufferedImage pixelMap = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = pixelMap.createGraphics();
        normalIcon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();

        JButton button = new JButton(normalIcon) {
            // Override contains เพื่อเช็ค Pixel Transparency (ความโปร่งใส)
            @Override
            public boolean contains(int x, int y) {
                int iconX = (getWidth() - w) / 2;
                int iconY = (getHeight() - h) / 2;

                // 1. เช็คกรอบสี่เหลี่ยมก่อน (เพื่อความเร็ว)
                if (x < iconX || x >= iconX + w || y < iconY || y >= iconY + h) {
                    return false;
                }

                // 2. เช็คค่าสีของ Pixel จริงๆ (ถ้าโปร่งใส = ไม่โดน)
                int imgX = x - iconX;
                int imgY = y - iconY;
                int pixel = pixelMap.getRGB(imgX, imgY);
                
                // เช็คค่า Alpha (ความทึบ) ว่ามากกว่า 10 หรือไม่ (ตัดส่วนเงาจางๆ หรือส่วนใสออก)
                return ((pixel >> 24) & 0xff) > 10;
            }
        };
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- เพิ่ม 2 บรรทัดนี้เพื่อให้รูปอยู่กลาง Hitbox เสมอ ---
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setIcon(hoverIcon);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setIcon(normalIcon);
            }
        });
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