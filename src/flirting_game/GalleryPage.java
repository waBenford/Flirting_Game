package flirting_game;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class GalleryPage extends JFrame {
    private JLayeredPane layeredPane;

    public GalleryPage() {
        setTitle("Gallery");
        setSize(1024, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // 1. ตรวจสอบสถานะออนไลน์และดึงข้อมูลจาก SQL ก่อนแสดงผล
        if (relationdata.isOnlineMode) {
            syncGalleryFromSQL(); 
        } else {
            refreshGalleryUI(); // ถ้าออฟไลน์ให้แสดงตามค่าที่มีในเครื่อง
        }
    }

    // --- ระบบดึงข้อมูลฉากจบจาก SQL (Sync) ---
    private void syncGalleryFromSQL() {
        new Thread(() -> {
            try (java.net.Socket socket = new java.net.Socket(relationdata.serverIP, 5000);
                 java.io.PrintWriter out = new java.io.PrintWriter(socket.getOutputStream(), true);
                 java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()))) {
                
                // ส่งชื่อและระบุพาร์ทเพื่อขอโหลดข้อมูลสถานะฉากจบ
                out.println("SET_NAME:" + relationdata.playerName);
                out.println("SET_PART:9"); 

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("LOAD_ENDINGS:")) {
                        String[] eds = line.substring(13).split(",");
                        // อัปเดตตัวแปรในเครื่องตามข้อมูลจริงใน SQL
                        relationdata.isEnding1Unlocked = eds[0].equals("1");
                        relationdata.isEnding2Unlocked = eds[1].equals("1");
                        relationdata.isEnding3Unlocked = eds[2].equals("1");
                        
                        // เมื่อโหลดข้อมูลเสร็จ ให้วาด UI ใหม่ทันที
                        SwingUtilities.invokeLater(() -> refreshGalleryUI());
                        break; 
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // หากเชื่อมต่อไม่ได้ ให้แสดงผลตามค่าที่มีอยู่
                SwingUtilities.invokeLater(() -> refreshGalleryUI());
            }
        }).start();
    }

    // --- ฟังก์ชันสำหรับวาด UI ทั้งหมดให้เหมือนเดิม ---
    private void refreshGalleryUI() {
        layeredPane.removeAll();

        // 1. Background
        ImageIcon bgIcon = new ImageIcon("res/menu/bg.png");
        JLabel background = new JLabel(new ImageIcon(bgIcon.getImage().getScaledInstance(1024, 600, Image.SCALE_SMOOTH)));
        background.setBounds(0, 0, 1024, 600);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        // 2. Title
        JLabel titleLabel = new JLabel("ฉากจบทั้งหมด", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 30, 1024, 50);
        layeredPane.add(titleLabel, JLayeredPane.MODAL_LAYER);

        // 3. ฉากจบทั้ง 3 ช่อง (ดึงค่าจาก relationdata ที่อัปเดตแล้ว)
        layeredPane.add(createEndingSlot(1, 100, 150, relationdata.isEnding1Unlocked, "res/gallery/ending1_thumb.png", "res/gallery/ending1.png"), JLayeredPane.PALETTE_LAYER);
        layeredPane.add(createEndingSlot(2, 400, 150, relationdata.isEnding2Unlocked, "res/gallery/ending2_thumb.png", "res/gallery/ending2.png"), JLayeredPane.PALETTE_LAYER);
        layeredPane.add(createEndingSlot(3, 700, 150, relationdata.isEnding3Unlocked, "res/gallery/ending3_thumb.png", "res/gallery/ending3.png"), JLayeredPane.PALETTE_LAYER);

        // 4. ปุ่มกลับเมนูหลัก
        JButton backBtn = new JButton("กลับเมนูหลัก");
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        backBtn.setBounds(412, 500, 200, 40);
        backBtn.addActionListener(e -> { 
            new menu().main(null); 
            dispose(); 
        });
        layeredPane.add(backBtn, JLayeredPane.MODAL_LAYER);

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    private JPanel createEndingSlot(int id, int x, int y, boolean isUnlocked, String thumbPath, String fullPath) {
        JPanel slot = new JPanel(new BorderLayout());
        slot.setBounds(x, y, 220, 300);
        slot.setOpaque(false);

        // ถ้าปลดล็อคแล้วให้โชว์รูป Thumb ถ้าไม่ให้โชว์ locked.png
        String displayPath = isUnlocked ? thumbPath : "res/gallery/locked.png";
        ImageIcon icon = getScaledIcon(displayPath, 220, 250);
        
        JButton imgBtn = new JButton(icon);
        imgBtn.setContentAreaFilled(false);
        imgBtn.setBorderPainted(true);
        imgBtn.setFocusPainted(false);
        imgBtn.setEnabled(isUnlocked); // กดดูได้เฉพาะอันที่ปลดแล้ว

        if (isUnlocked) {
            imgBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            imgBtn.addActionListener(e -> viewEnding(fullPath));
        }

        JLabel label = new JLabel("Ending " + id, SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 18));
        label.setForeground(Color.WHITE);

        slot.add(imgBtn, BorderLayout.CENTER);
        slot.add(label, BorderLayout.SOUTH);

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
            // สร้างภาพ Placeholder กรณีหาไฟล์ไม่เจอ (ขึ้นเครื่องหมาย ?)
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
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GalleryPage().setVisible(true));
    }
}