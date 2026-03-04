package flirting_game;

public class relationdata {

    public static relationship aliceRel = new relationship("อริส");
    public static relationship nebulaRel = new relationship("Nebula"); // เพิ่มตัวแปรความสัมพันธ์ Nebula
    public static boolean isOnlineMode = false;
    public static String serverIP = "154.84.153.179"; // ใส่ IP ของ Server คุณที่นี่
    public static String playerName = "Guest";      // << เพิ่มบรรทัดนี้เพื่อเก็บชื่อ
    public static int onlinePlayerCount = 1;

    public static java.net.Socket globalSocket;
    public static java.io.PrintWriter globalOut;
    public static java.io.BufferedReader globalIn;

    // --- สถานะการปลดล็อคฉากจบ (ทดสอบ: ปลดล็อคฉากแรกไว้เป็นตัวอย่าง) ---
    public static boolean isEnding1Unlocked = false;
    public static boolean isEnding2Unlocked = false;
    public static boolean isEnding3Unlocked = false;
    public static boolean isEnding4Unlocked = false; // ฉากจบโสด (Alone)
}
