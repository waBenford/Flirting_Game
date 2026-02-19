package flirting_game;

import java.awt.*;
import javax.swing.*;

public class menu {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Isekai Lover");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 600);
        frame.setResizable(false);
        
        JLayeredPane lp = new JLayeredPane();
        frame.setContentPane(lp);

        // --- 1. Background ---
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
        int btnW = 300;
        int btnH = 150;
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(360, 200, btnW, 320);

        // สร้างปุ่มแยกทีละตัวเพื่อใส่ Action
        JButton startBtn = createImageButton("res/buttons/buttonStart.png", btnW, btnH);
        JButton galleryBtn = createImageButton("res/buttons/buttonGallery.png", btnW, btnH);
        JButton settingBtn = createImageButton("res/buttons/buttonSetting.png", btnW, btnH);
        JButton exitBtn = createImageButton("res/buttons/buttonExit.png", btnW, btnH);

        // --- เพิ่มฟังก์ชันการกดปุ่ม (Actions) ---

        // ปุ่ม Start: ปิดหน้าเมนูแล้วไปหน้า part1
        startBtn.addActionListener(e -> {
            // ตรวจสอบว่าคุณมีไฟล์ part1.java หรือคลาส part1 แล้ว
            try {
                new part1().setVisible(true);
                frame.dispose(); // ปิดหน้าเมนู            
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "ยังไม่ได้สร้างคลาส part1 หรือมีข้อผิดพลาด");
            }
        });

        // ปุ่ม Exit: ปิดโปรแกรมทันที
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame,
                "คุณต้องการออกจากเกมใช่หรือไม่?", "ยืนยัน", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // เพิ่มปุ่มลงใน Panel
        buttonPanel.add(startBtn);
        buttonPanel.add(galleryBtn);
        buttonPanel.add(settingBtn);
        buttonPanel.add(exitBtn);
        
        lp.add(buttonPanel, JLayeredPane.MODAL_LAYER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
        // สร้างรูปที่ใหญ่ขึ้นเล็กน้อยสำหรับตอน Hover
        ImageIcon hoverIcon = getScaledIcon(path, w + 10, h + 10); 

        JButton button = new JButton(normalIcon);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setIcon(hoverIcon);
                // ขยับตำแหน่งเล็กน้อยเพื่อให้ดูเหมือนขยายจากจุดศูนย์กลาง
                button.setBounds(button.getX() - 5, button.getY() - 5, w + 15, h + 15);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setIcon(normalIcon);
                button.setBounds(button.getX() + 5, button.getY() + 5, w, h);
            }
        });
        return button;
    }
}