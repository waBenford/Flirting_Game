package flirting_game;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class LobbyWindow extends JFrame {
    private String playerName;
    private boolean isHost;
    private String roomCode;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private JTextArea playerListArea;
    private JLabel roomCodeLabel;
    private JButton readyBtn;
    private JButton startBtn;
    private JButton leaveBtn;
    private boolean isReady = false;

    public LobbyWindow(String name, boolean isHost, int maxPlayers, String codeToJoin) {
        this.playerName = name;
        this.isHost = isHost;
        this.roomCode = codeToJoin;

        setTitle("Isekai Lover - Lobby");
        setSize(1024, 600); // ปรับขนาดเท่าตัวเกม
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLayeredPane lp = new JLayeredPane();
        setContentPane(lp);

        // 1. พื้นหลัง (ใช้รูปเดียวกับหน้า Menu)
        ImageIcon bgImg = new ImageIcon("res/menu/bg.png");
        Image scaledBg = bgImg.getImage().getScaledInstance(1024, 600, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(scaledBg));
        background.setBounds(0, 0, 1024, 600);
        lp.add(background, JLayeredPane.DEFAULT_LAYER);

        // 2. แผงควบคุมหลัก (ตรงกลาง)
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // วาดสีดำโปร่งแสงทับลงไปเอง เพื่อป้องกันบัคภาพซ้อน
                g.setColor(new Color(0, 0, 0, 180)); 
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        mainPanel.setLayout(null);
        mainPanel.setOpaque(false); // *** สำคัญมาก ตรงนี้ต้องแก้เป็น false ***
        mainPanel.setBounds(262, 50, 500, 450);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        lp.add(mainPanel, JLayeredPane.PALETTE_LAYER);

        // 3. หัวข้อและรหัสห้อง
        roomCodeLabel = new JLabel("Connecting...", SwingConstants.CENTER);
        roomCodeLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        roomCodeLabel.setForeground(new Color(255, 215, 0)); // สีทอง
        roomCodeLabel.setBounds(0, 20, 500, 40);
        mainPanel.add(roomCodeLabel);

        JLabel title = new JLabel("รายชื่อนักผจญภัยในห้อง", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.PLAIN, 18));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 60, 500, 30);
        mainPanel.add(title);

        // 4. รายชื่อผู้เล่น (Scroll)
        playerListArea = new JTextArea();
        playerListArea.setEditable(false);
        playerListArea.setOpaque(false);
        playerListArea.setFont(new Font("Tahoma", Font.PLAIN, 20));
        playerListArea.setForeground(Color.CYAN);
        playerListArea.setMargin(new Insets(10, 20, 10, 10));

        JScrollPane scroll = new JScrollPane(playerListArea);
        scroll.setBounds(50, 100, 400, 250);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        mainPanel.add(scroll);

        leaveBtn = new JButton(isHost ? "ยุบห้อง" : "ออกจากห้อง");
        styleLobbyButton(leaveBtn, new Color(150, 50, 50)); // สีแดงคริมสัน
        leaveBtn.setBounds(40, 370, 200, 50);
        mainPanel.add(leaveBtn);

        readyBtn = new JButton("เตรียมพร้อม (Ready)");
        styleLobbyButton(readyBtn, new Color(50, 150, 50)); // สีเขียว
        readyBtn.setBounds(260, 370, 200, 50);

        startBtn = new JButton("เริ่มการผจญภัย");
        styleLobbyButton(startBtn, new Color(180, 130, 0)); // สีทอง/ส้ม
        startBtn.setBounds(260, 370, 200, 50);
        startBtn.setEnabled(false);

        if (isHost) {
            mainPanel.add(startBtn);
        } else {
            mainPanel.add(readyBtn);
        }

        // Action Listener
        readyBtn.addActionListener(e -> {
            isReady = !isReady;
            readyBtn.setText(isReady ? "ยกเลิก (Ready)" : "เตรียมพร้อม (Ready)");
            readyBtn.setBackground(isReady ? Color.ORANGE : new Color(50, 150, 50));
            out.println("LOBBY_READY:" + isReady);
        });

        startBtn.addActionListener(e -> out.println("START_GAME"));

        leaveBtn.addActionListener(e -> {
            returnToMenu(); // เรียกฟังก์ชันกลับหน้าเมนู (จะสร้างไว้ด้านล่าง)
        });

        startBtn.addActionListener(e -> out.println("START_GAME"));

        connectToServer(maxPlayers);
    }

    private void returnToMenu() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close(); // ตัดการเชื่อมต่อ 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            // เรียกหน้า menu ขึ้นมาใหม่ (อ้างอิงจากเมธอด main ในไฟล์ menu.java)
            menu.main(new String[0]); 
            this.dispose(); // ปิดหน้า Lobby ปัจจุบันทิ้ง
        });
    }

    private void styleLobbyButton(JButton btn, Color color) {
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createBevelBorder(0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- ส่วนการเชื่อมต่อ (เหมือนเดิมแต่เปลี่ยน localhost เป็น 127.0.0.1) ---
    private void connectToServer(int maxPlayers) {
        new Thread(() -> {
            try {
                socket = new Socket("154.84.153.179", 5000); 
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                if (isHost) out.println("CREATE_ROOM:" + playerName + ":" + maxPlayers);
                else out.println("JOIN_ROOM:" + roomCode + ":" + playerName);

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.startsWith("ROOM_CREATED:")) {
                        roomCode = msg.substring(13);
                        roomCodeLabel.setText("ROOM CODE: " + roomCode);
                    } 
                    else if (msg.startsWith("JOIN_SUCCESS:")) {
                        roomCode = msg.substring(13);
                        roomCodeLabel.setText("ROOM CODE: " + roomCode);
                    }
                    else if (msg.startsWith("JOIN_FAIL:")) {
                        JOptionPane.showMessageDialog(this, msg.substring(10));
                        System.exit(0);
                    }
                    else if (msg.startsWith("LOBBY_UPDATE:")) {
                        updatePlayerList(msg.substring(13));
                    }
                    else if (msg.startsWith("ROOM_CLOSED:")) { 
                        // แจ้งเตือนผู้เล่นคนอื่นว่าโฮสต์ยุบห้อง
                        JOptionPane.showMessageDialog(this, "โฮสต์ได้ยุบห้อง / ออกจากเกมแล้ว", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
                        // บังคับทุกคนรันฟังก์ชันกลับหน้าเมนู
                        returnToMenu(); 
                        break; // ออกจากลูปเพื่อหยุดรอรับข้อความ
                    }
                    else if (msg.equals("GAME_START")) {
                        relationdata.isOnlineMode = true;
                        relationdata.playerName = playerName;
                        
                        // *** เพิ่ม 3 บรรทัดนี้: เก็บ Socket ไว้ในส่วนกลาง ***
                        relationdata.globalSocket = this.socket;
                        relationdata.globalOut = this.out;
                        relationdata.globalIn = this.in;

                        SwingUtilities.invokeLater(() -> {
                            new part1().setVisible(true);
                            this.dispose();
                        });
                        break; // *** อย่าลืมคำว่า break; ตรงนี้นะครับ สำคัญมาก! ***
                    }
                    else if (msg.startsWith("ERROR:")) {
                        JOptionPane.showMessageDialog(this, msg.substring(6));
                    }
                }
            } catch (Exception e) {
                // *** แก้ไขบล็อก catch ตรงนี้ ***
                // เช็คว่าถ้า Socket ไม่ได้ถูกปิดโดยเราตั้งใจ (เช่น เซิร์ฟล่มจริงๆ) ค่อยเด้ง Error
                if (socket != null && !socket.isClosed()) {
                    JOptionPane.showMessageDialog(this, "เซิร์ฟเวอร์ปิดอยู่ หรือเชื่อมต่อไม่ได้");
                    System.exit(0);
                }
            }
        }).start();
    }

    private void updatePlayerList(String data) {
        StringBuilder sb = new StringBuilder();
        String[] players = data.split(";");
        boolean allReady = true;
        int count = 0;

        for (String p : players) {
            if (p.isEmpty()) continue;
            String[] info = p.split(",");
            String n = info[0];
            String r = info[1];
            boolean ready = Boolean.parseBoolean(info[2]);
            count++;
            if (!ready) allReady = false;
            
            sb.append(n.equals(playerName) ? "> " : "  ")
              .append(n).append(" [").append(r).append("] ")
              .append(ready ? "(พร้อม)" : "(รอ...)").append("\n");
        }
        playerListArea.setText(sb.toString());
        
        // *** แก้บรรทัดนี้: เปลี่ยนจาก count >= 2 เป็น count >= 1 ***
        if (isHost) {
            startBtn.setEnabled(allReady && count >= 1); 
        }
    }
}