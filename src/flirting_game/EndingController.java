package flirting_game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;

public class EndingController extends JFrame {

    private Image bgImage = new ImageIcon("res/menu/bg.png").getImage();
    private String myRole;
    private int myA, myN, topA, topN; 

    // ระบบจัดการคิวการดวล
    private List<String> duelists = new ArrayList<>();
    private Set<String> finishedPlayers = new HashSet<>(); 
    private int currentDuelistIdx = 0;
    private int currentQIndex = 0;
    private int[] battleScores = new int[4]; // เก็บคะแนน P1, P2, P3
    private java.io.PrintWriter networkOut;
    private String lastTargetChar = ""; 

    // คลังคำถาม
    private String[][] aliceQuestions = {
        {"What color was Alice's skirt when you first met?", "White", "Blue", "Green", "Black", "2"},
        {"How many times has Alice cried?", "1 time", "2 times", "3 times", "Never", "2"},
        {"How many times has Alice blushed?", "5 times", "10 times", "Countless", "Never", "1"},
        {"Where does Alice usually take a bath?", "Waterfall", "Castle", "River", "Hot Spring", "2"}
    };

    private String[][] nebulaQuestions = {
        {"Where is Nebula's castle located?", "Mountain", "Life Forest", "Forest Death End", "Dark Forest", "2"},
        {"How many times has Nebula blushed?", "2 times", "4 times", "6 times", "8 times", "3"},
        {"Which places did you pass through before meeting Nebula?", "Village & Forest", "Cave & Sea", "Capital City", "Ancient Tomb", "0"},
        {"What is Nebula's personality type?", "Tsundere", "Sundere", "Yandere", "Kuudere", "3"}
    };

    public EndingController(String n1, int p1A, int p1N, String n2, int p2A, int p2N, String n3, int p3A, int p3N, String role) {
        this.myRole = role;
        this.networkOut = relationdata.globalOut;
        setTitle("Isekai Lover - Final Decisions");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                if (bgImage != null) g2d.drawImage(bgImage, 0, 0, 1280, 800, this);
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, 1280, 800);
            }
        };
        add(mainPanel);

        // Leaderboard UI
        JLabel titleLabel = new JLabel("LEADERBOARD SUMMARY", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 60));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setBounds(0, 40, 1280, 80);
        mainPanel.add(titleLabel);

        addHeaderLabel(mainPanel, "PLAYER", 200, 150);
        addHeaderLabel(mainPanel, "ALICE", 600, 150);
        addHeaderLabel(mainPanel, "NEBULA", 900, 150);

        // กำหนดตัวแปรคะแนนตามบทบาท
        if(myRole.equals("P1")) { myA = p1A; myN = p1N; topA = Math.max(p2A, p3A); topN = Math.max(p2N, p3N); }
        else if(myRole.equals("P2")) { myA = p2A; myN = p2N; topA = Math.max(p1A, p3A); topN = Math.max(p1N, p3N); }
        else { myA = p3A; myN = p3N; topA = Math.max(p1A, p2A); topN = Math.max(p1N, p2N); }
        
        String result = checkLogic(myA, myN, topA, topN);

        addScoreCard(mainPanel, n1 + (myRole.equals("P1") ? " (YOU)" : ""), p1A, p1N, 210, myRole.equals("P1"));
        addScoreCard(mainPanel, n2 + (myRole.equals("P2") ? " (YOU)" : ""), p2A, p2N, 320, myRole.equals("P2"));
        addScoreCard(mainPanel, n3 + (myRole.equals("P3") ? " (YOU)" : ""), p3A, p3N, 430, myRole.equals("P3"));

        RoundButton viewEndBtn = new RoundButton("View Ending", new Color(40, 160, 60));
        RoundButton duelBtn = new RoundButton("Duel for Ending", new Color(180, 30, 40));
        viewEndBtn.setBounds(340, 650, 280, 80);
        duelBtn.setBounds(660, 650, 280, 80);

        viewEndBtn.addActionListener(e -> {
            if (result.equals("ALONE")) goToEnding("ALONE", true);
            else if (result.equals("HAREM_WIN")) goToEnding("HAREM", true); 
            else goToEnding(result.contains("ALICE") ? "ALICE" : "NEBULA", true);
        });

        if (result.contains("BATTLE")) {
            viewEndBtn.setEnabled(false);
            duelBtn.setEnabled(true);
        } else {
            viewEndBtn.setEnabled(true);
            duelBtn.setEnabled(false);
        }

        duelBtn.addActionListener(e -> {
            duelists.clear();
            finishedPlayers.clear();
            String charName = result.contains("ALICE") ? "ALICE" : "NEBULA";
            this.lastTargetChar = charName;

            int winScore = result.contains("ALICE") ? Math.max(myA, topA) : Math.max(myN, topN);

            // กรองเฉพาะผู้เล่นที่คะแนนเท่ากันและมีชื่ออยู่จริง
            if ((result.contains("ALICE") ? p1A : p1N) == winScore) duelists.add("P1");

            // เพิ่มเงื่อนไขเช็คว่า n     2 ไม่ใช่ชื่อเริ่มต้น "Player 2" ถึงจะเพิ่มเข้าดวล
            if ((result.contains("ALICE") ? p2A : p2N) == winScore && !n2.equals("Player 2")) duelists.add("P2");

            // เพิ่มเงื่อนไขเช็คว่า n3 ไม่ใช่ชื่อเริ่มต้น "Player 3"
            if ((result.contains("ALICE") ? p3A : p3N) == winScore && !n3.equals("Player 3")) duelists.add("P3");

            currentDuelistIdx = 0;
            for(int i=0; i<4; i++) battleScores[i] = 0;
            
            advanceQueue(); 
        });

        mainPanel.add(viewEndBtn);
        mainPanel.add(duelBtn);

        initNetwork(); 
    }

    private void advanceQueue() {
        if (duelists.isEmpty()) return;

        // ถ้าทุกคนในรายชื่อดวลตอบจบแล้ว ให้ตัดสินทันที
        if (finishedPlayers.containsAll(duelists)) {
            determineWinner(lastTargetChar);
            return;
        }

        // เลื่อน Index ข้ามคนที่ตอบเสร็จแล้ว (ใช้ข้อมูลจากเครือข่าย)
        while (currentDuelistIdx < duelists.size() && finishedPlayers.contains(duelists.get(currentDuelistIdx))) {
            currentDuelistIdx++;
        }

        if (currentDuelistIdx < duelists.size()) {
            currentQIndex = 0; 
            showBattleUI(lastTargetChar);
        } else {
            determineWinner(lastTargetChar);
        }
    }

    private void showBattleUI(String targetChar) {
        getContentPane().removeAll();
        String currentPlayer = duelists.get(currentDuelistIdx); 
        boolean isMyTurn = currentPlayer.equals(myRole);
        
        String formattedName = targetChar.substring(0, 1).toUpperCase() + targetChar.substring(1).toLowerCase();
        String charPath = targetChar.equals("ALICE") ? "res/Charactor/Alice/Girl/" + formattedName + "-normal1.png" : "res/Charactor/Nebula/Nebula-normal1.png";

        JPanel battlePanel = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) g.drawImage(bgImage, 0, 0, 1280, 800, this);
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, 1280, 800);
            }
        };
        battlePanel.setBounds(0, 0, 1280, 800);
        add(battlePanel);

        // วาดตัวละคร
        ImageIcon charIcon = new ImageIcon(charPath);
        int charW = targetChar.equals("ALICE") ? 1000 : 750;
        int charH = targetChar.equals("ALICE") ? 650 : 700;
        JLabel charLabel = new JLabel(new ImageIcon(charIcon.getImage().getScaledInstance(charW, charH, Image.SCALE_SMOOTH)));
        charLabel.setBounds(targetChar.equals("ALICE") ? -240 : -120, targetChar.equals("ALICE") ? 125 : 65, charW, charH);
        battlePanel.add(charLabel);

        // กรอบคำถาม
        JPanel triviaFrame = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(25, 15, 35, 210)); 
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2d.setColor(new Color(180, 0, 255));
                g2d.setStroke(new BasicStroke(4));
                g2d.draw(new RoundRectangle2D.Double(2, 2, getWidth()-4, getHeight()-4, 30, 30));
                g2d.dispose();
            }
        };
        triviaFrame.setOpaque(false);
        triviaFrame.setBounds(650, 80, 580, 650);
        battlePanel.add(triviaFrame);

        // อัปเดต Title ให้แสดงจำนวนคนดวลจริง
        JLabel qTitle = new JLabel(currentPlayer + "'s TURN (" + (currentDuelistIdx + 1) + "/" + duelists.size() + ")", SwingConstants.CENTER);
        qTitle.setFont(new Font("Monospaced", Font.BOLD, 30));
        qTitle.setForeground(new Color(255, 215, 0));
        qTitle.setBounds(0, 20, 580, 50);
        triviaFrame.add(qTitle);

        if (isMyTurn) {
            String[][] bank = targetChar.equals("ALICE") ? aliceQuestions : nebulaQuestions;
            String[] qData = bank[currentQIndex];

            JLabel qText = new JLabel("<html><center>" + qData[0] + "</center></html>", SwingConstants.CENTER);
            qText.setFont(new Font("Monospaced", Font.PLAIN, 24));
            qText.setForeground(Color.WHITE);
            qText.setBounds(40, 80, 500, 160);
            triviaFrame.add(qText);

            for (int i = 0; i < 4; i++) {
                final int choiceIdx = i;
                RoundButton btn = new RoundButton("<html>" + qData[i+1] + "</html>", new Color(80, 40, 120));
                btn.setFont(new Font("Monospaced", Font.BOLD, 22));
                btn.setBounds(40, 260 + (i * 90), 500, 75);
                
                btn.addActionListener(e -> {
                    if (choiceIdx == Integer.parseInt(qData[5])) {
                        battleScores[Integer.parseInt(myRole.substring(1))]++;
                    }
                    currentQIndex++;
                    if (currentQIndex < 4) {
                        showBattleUI(targetChar); 
                    } else {
                        finishedPlayers.add(myRole); 
                        if (relationdata.isOnlineMode && networkOut != null) {
                            networkOut.println("BATTLE_SCORE:" + myRole + ":" + battleScores[Integer.parseInt(myRole.substring(1))]);
                        }
                        advanceQueue(); 
                    }
                });
                triviaFrame.add(btn);
            }
        } else {
            // แสดงหน้ารอสำหรับคนอื่น
            JLabel waitText = new JLabel("<html><center>PLEASE WAIT...<br>It's " + currentPlayer + "'s turn</center></html>", SwingConstants.CENTER);
            waitText.setFont(new Font("Monospaced", Font.BOLD, 28));
            waitText.setForeground(Color.WHITE);
            waitText.setBounds(40, 250, 500, 150);
            triviaFrame.add(waitText);
        }
        revalidate(); repaint();
    }

    private void determineWinner(String charName) {
        int max = -1; String winner = "";
        for (String p : duelists) { 
            int pNum = Integer.parseInt(p.substring(1));
            if (battleScores[pNum] > max) {
                max = battleScores[pNum];
                winner = p;
            }
        }
        if (!winner.isEmpty()) {
            JOptionPane.showMessageDialog(this, "The Final Winner is " + winner + "!");
            goToEnding(charName, winner.equals(myRole));
        }
    }

    private void goToEnding(String character, boolean isWin) {
        if (isWin) {
            if (character.equals("HAREM")) { relationdata.isEnding1Unlocked = true; relationdata.isEnding2Unlocked = true; relationdata.isEnding3Unlocked = true; }
            else if (character.equals("ALICE")) relationdata.isEnding2Unlocked = true;
            else if (character.equals("NEBULA")) relationdata.isEnding3Unlocked = true;
            else if (character.equals("ALONE")) relationdata.isEnding4Unlocked = true;
        }
        saveEndingsToServer();
        SwingUtilities.invokeLater(() -> { new GalleryPage().setVisible(true); dispose(); });
    }

    private void saveEndingsToServer() {
        if (relationdata.isOnlineMode) {
            new Thread(() -> {
                try (java.net.Socket socket = new java.net.Socket(relationdata.serverIP, 5000);
                    java.io.PrintWriter out = new java.io.PrintWriter(socket.getOutputStream(), true)) {
                    out.println("SET_NAME:" + relationdata.playerName);
                    out.println("SAVE_ENDINGS:" + (relationdata.isEnding1Unlocked?"1":"0") + "," + (relationdata.isEnding2Unlocked?"1":"0") + "," + (relationdata.isEnding3Unlocked?"1":"0") + "," + (relationdata.isEnding4Unlocked?"1":"0"));
                } catch (Exception e) {}
            }).start();
        }
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode || relationdata.globalSocket == null) return;
        new Thread(() -> {
            try {
                BufferedReader in = relationdata.globalIn;
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("BATTLE_UPDATE:")) {
                        String[] parts = line.substring(14).split(":");
                        String pRole = parts[0]; 
                        int pScore = Integer.parseInt(parts[1]);
                        
                        battleScores[Integer.parseInt(pRole.substring(1))] = pScore;
                        finishedPlayers.add(pRole);
                        
                        // เมื่อได้รับข้อมูลอัปเดตจากเพื่อน ให้เลื่อนคิวทันที
                        SwingUtilities.invokeLater(() -> advanceQueue());
                    }
                }
            } catch (Exception e) {}
        }).start();
    }

    private void addHeaderLabel(JPanel p, String txt, int x, int y) {
        JLabel l = new JLabel(txt, SwingConstants.CENTER);
        l.setFont(new Font("Monospaced", Font.BOLD, 22));
        l.setForeground(Color.GRAY);
        l.setBounds(x, y, 200, 40);
        p.add(l);
    }

    private void addScoreCard(JPanel parent, String name, int a, int n, int y, boolean isMe) {
        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(isMe ? new Color(50, 40, 60) : new Color(40, 40, 45));
                g2d.fill(new RoundRectangle2D.Double(0, 0, 980, 90, 25, 25));
                g2d.setColor(isMe ? new Color(180, 0, 255) : new Color(80, 80, 85));
                g2d.setStroke(new BasicStroke(isMe ? 3 : 1));
                g2d.draw(new RoundRectangle2D.Double(0, 0, 980, 90, 25, 25));
                g2d.dispose();
            }
        };
        card.setOpaque(false); card.setBounds(150, y, 980, 90);
        JLabel nme = new JLabel(name); nme.setFont(new Font("Monospaced", Font.BOLD, 28));
        nme.setForeground(isMe ? new Color(255, 215, 0) : Color.WHITE); nme.setBounds(40, 20, 450, 50);
        card.add(nme);
        
        JLabel al = new JLabel(String.valueOf(a), SwingConstants.CENTER);
        al.setFont(new Font("Monospaced", Font.BOLD, 42)); al.setForeground(Color.WHITE);
        al.setBounds(425, 20, 200, 50); card.add(al);

        JLabel ne = new JLabel(String.valueOf(n), SwingConstants.CENTER);
        ne.setFont(new Font("Monospaced", Font.BOLD, 42)); ne.setForeground(new Color(210, 160, 255));
        ne.setBounds(725, 20, 200, 50); card.add(ne);

        parent.add(card);
    }

    public static String checkLogic(int myA, int myN, int topA, int topN) {
        if (myA > topA && myN > topN) return "HAREM_WIN";
        if (myA > topA && myA > myN) return "ALICE_WIN";
        if (myN > topN && myN > myA) return "NEBULA_WIN";
        if (myA == topA && myA > 0) return "ALICE_TRIVIA_BATTLE";
        if (myN == topN && myN > 0) return "NEBULA_TRIVIA_BATTLE";
        return "ALONE";
    }
}

class RoundButton extends JButton {
    private Color baseCol;
    public RoundButton(String t, Color c) {
        super(t); 
        this.baseCol = c; 
        setContentAreaFilled(false); 
        setBorderPainted(false); 
        setFocusPainted(false);
        setForeground(Color.WHITE); 
        setFont(new Font("Monospaced", Font.BOLD, 26)); 
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    @Override 
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getModel().isPressed() ? baseCol.darker() : (getModel().isRollover() ? baseCol.brighter() : baseCol));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 45, 45); 
        g2.dispose(); 
        super.paintComponent(g);
    }
}