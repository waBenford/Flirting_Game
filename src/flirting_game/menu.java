package flirting_game;

import javax.swing.*;
import java.awt.*;

public class menu {
    public static void main(String[] args) {
        JFrame frame = new JFrame("DEMO GAME - Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 600); // ปรับความสูงเล็กน้อยให้สมดุลกับ 4 ปุ่ม
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(204, 229, 255));

        // Font
        Font thaiFontBold = new Font("Tahoma", Font.BOLD, 36);
        Font thaiFontNormal = new Font("Tahoma", Font.PLAIN, 18);

        // Header
        JLabel titleLabel = new JLabel("GAME DEMO", SwingConstants.CENTER);
        titleLabel.setFont(thaiFontBold);
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 50, 80));

        JButton startButton = createBlueButton("เริ่มเกม", thaiFontNormal);
        JButton galleryButton = createBlueButton("Gallery" ,thaiFontNormal);
        JButton optionsButton = createBlueButton("Options", thaiFontNormal);
        JButton exitButton = createBlueButton("ออกจากเกม", thaiFontNormal);

        // --- แก้ไขจุดนี้: ลบเครื่องหมายคอมเมนต์ที่เกินมาออก ---
        startButton.addActionListener(e -> {
            new part1().setVisible(true); // เปิดหน้าเนื้อเรื่อง part1
            frame.dispose(); // ปิดหน้าเมนูหลัก
        });

        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(galleryButton);
        buttonPanel.add(optionsButton);
        buttonPanel.add(exitButton);
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JButton createBlueButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(new Color(51, 153, 255));
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        // เพิ่มขอบปุ่มเล็กน้อยให้ดูสวยงาม
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        return button;
    }
}