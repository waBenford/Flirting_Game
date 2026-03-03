package flirting_game;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class GalleryPage extends JFrame {

    public GalleryPage() {
        setTitle("Gallery");
        setSize(1024, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // --- เล่นเพลงสำหรับหน้า Gallery (ถ้ามี) ---
        // SoundManager.playBGM("res/sound/gallery_music.wav");

        // 1. Background (สมมติว่ามีไฟล์นี้อยู่)
        ImageIcon bgIcon = new ImageIcon("res/menu/bg.png");
        JLabel background = new JLabel(new ImageIcon(bgIcon.getImage().getScaledInstance(1024, 600, Image.SCALE_SMOOTH)));
        background.setBounds(0, 0, 1024, 600);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        // 2. Title
        JLabel titleLabel = new JLabel("ฉากจบทั้งหมด", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 30, 1024, 50);
        layeredPane.add(titleLabel, JLayeredPane.PALETTE_LAYER);

        // 3. Ending Placeholders
        JPanel endingsPanel = new JPanel(new GridLayout(1, 3, 40, 0)); // 1 แถว, 3 คอลัมน์, ระยะห่างแนวนอน 40px
        endingsPanel.setBounds(62, 120, 900, 350);
        endingsPanel.setOpaque(false);

        // สร้างปุ่มสำหรับฉากจบแต่ละอัน
        JButton ending1 = createEndingSlot("res/gallery/ending1.png", "res/gallery/ending1_thumb.png", relationdata.isEnding1Unlocked);
        JButton ending2 = createEndingSlot("res/gallery/ending2.png", "res/gallery/ending2_thumb.png", relationdata.isEnding2Unlocked);
        JButton ending3 = createEndingSlot("res/gallery/ending3.png", "res/gallery/ending3_thumb.png", relationdata.isEnding3Unlocked);

        endingsPanel.add(ending1);
        endingsPanel.add(ending2);
        endingsPanel.add(ending3);

        layeredPane.add(endingsPanel, JLayeredPane.PALETTE_LAYER);

        // 4. Back Button
        JButton backButton = new JButton("กลับเมนูหลัก");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        backButton.setBounds(412, 500, 200, 50);
        backButton.addActionListener(e -> {
            // กลับไปหน้าเมนู
            menu.main(new String[0]);
            dispose();
        });
        layeredPane.add(backButton, JLayeredPane.PALETTE_LAYER);
    }

    private JButton createEndingSlot(String fullImage, String thumbImage, boolean isUnlocked) {
        ImageIcon unlockedIcon = getScaledIcon(thumbImage, 270, 350);
        ImageIcon lockedIcon = getScaledIcon("res/gallery/locked.png", 270, 350); // รูปสำหรับฉากจบที่ยังไม่ปลดล็อค

        JButton slot = new JButton();
        slot.setOpaque(false);
        slot.setContentAreaFilled(false);
        slot.setBorderPainted(false);

        if (isUnlocked) {
            slot.setIcon(unlockedIcon);
            slot.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // เพิ่ม action listener เพื่อดูภาพเต็ม
            slot.addActionListener(e -> viewEnding(fullImage));
        } else {
            slot.setIcon(lockedIcon);
            slot.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        return slot;
    }

    private void viewEnding(String fullImagePath) {
        JDialog endingViewer = new JDialog(this, "Ending CG", true);
        endingViewer.setSize(1024, 600);
        endingViewer.setLocationRelativeTo(this);
        
        ImageIcon fullEndingIcon = getScaledIcon(fullImagePath, 1024, 600);
        JLabel endingLabel = new JLabel(fullEndingIcon);
        
        endingViewer.add(endingLabel);
        endingViewer.setVisible(true);
    }

    private ImageIcon getScaledIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getIconWidth() == -1) {
            // สร้างภาพ Placeholder กรณีหาไฟล์ไม่เจอ
            BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = placeholder.createGraphics();
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Tahoma", Font.BOLD, 150));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString("?", (width - fm.stringWidth("?")) / 2, (height - fm.getHeight()) / 2 + fm.getAscent());
            g2d.dispose();
            return new ImageIcon(placeholder);
        }
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }
}