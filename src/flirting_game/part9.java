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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;

public class part9 extends JFrame {

    private JLayeredPane layeredPane;
    private JLabel backgroundLabel, characterLabel, dialogueArea, nameLabel;
    private VisualNovelBox dialoguePanel;
    private int currentIndex = 0;
    private Clip bgmClip;
    private Clip effectClip;
    private JButton choiceButton1, choiceButton2;
    private boolean isChoosing = false;
    private boolean isFinishing = false;
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    // --- ระบบความสัมพันธ์และ Network ---
    private JLabel nebulaAffinityLabel, nebulaStatusLabel;
    private JLabel affinityLabel, statusLabel;
    private JPanel statusOverlay;
    private JLabel onlineCountLabel, affinityStatusLabel;
    private PrintWriter networkOut;
    private String allPlayersData = "";

    // --- ระบบ Fade ---
    private float alpha = 1.0f;
    private JPanel fadeOverlay;
    private float charAlpha = 0.0f;
    private Timer charFadeTimer;
    private String lastCharPath = "";
    private float bgAlpha = 0.0f;
    private Timer bgFadeTimer;
    private String lastBgPath = "";
    private JPanel bgFadeOverlay;

    private JPanel waitOverlay;
    private boolean isWaiting = false;

    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);

    private String[] imagePaths = createBackgrounds();

    private String[] createBackgrounds() {
        String[] paths = new String[188];
        for (int i = 0; i < 188; i++) {
            if (i < 10) {
                paths[i] = "res/scene9/s1.png";
            } else if (i < 65) {
                paths[i] = "res/scene9/s2.png";
            } else if (i < 90) {
                paths[i] = "res/scene9/s3.png";
            } else if (i < 96) {
                paths[i] = "res/scene9/s4.png";
            } else {
                paths[i] = "res/scene9/s5.png";
            }
        }
        return paths;
    }

    private String[] charPaths = {
        // 0-7: เริ่มเดินทาง (บรรยาย + Dan + อริส)
        "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
        // 8-15: ทางเลือก + เริ่มเจอปีศาจ
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-shy2.png", // 8, 9 (Choices)
        "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal2.png",
        "res/Charactor/Alice/Girl/Alice-normal2.png", "res/empty.png", "res/empty.png", "res/empty.png", // ปีศาจ

        // 16-23: ฉากต่อสู้ช่วงแรก
        "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
        "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png",
        // 24-35: ปีศาจมาเพิ่ม + Nebula ปรากฏตัว
        "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/empty.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Dan/dan-normal1.png", // Nebula (ถ้ามีรูป Nebula ให้เปลี่ยน res/empty.png เป็น path รูป Nebula)

        // 36-40: คุยกับ Nebula
        "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
        // 41-51: Choices Nebula + ตกลงเดินทางด้วยกัน
        "res/Charactor/Nebula/Nebula-shy2.png", "res/Charactor/Nebula/Nebula-normal2.png", // 41, 42 (Choices Nebula)
        "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Nebula/Nebula-shy1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png",
        "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png",
        // 52-62: ระหว่างเดินทาง
        "res/Charactor/Nebula/Nebula-shy1.png", "res/Charactor/Nebula/Nebula-shy2.png", // 52, 53 (Choices)
        "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png",
        "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png",
        // 63-77: เข้าใกล้หุบเขาเงามืด
        "res/Charactor/Nebula/Nebula-shy2.png", "res/Charactor/Nebula/Nebula-normal1.png", // 63, 64 (Choices)
        "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/empty.png", "res/empty.png",
        "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
        "res/empty.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
        // 78-89: พักผ่อน + เดินทางต่อ
        "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Dan/dan-normal2.png", // 78, 79 (พักผ่อน)
        "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Nebula/Nebula-normal2.png", // 80, 81 (Choices)
        "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
        "res/Charactor/Nebula/Nebula-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png",
        // 90-101: ถึงป้อม Grey + เผชิญหน้า
        "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png",
        "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
        "res/Charactor/Gray/Gray-normal2.png", "res/Charactor/Gray/Gray-normal1.png", "res/Charactor/Gray/Gray-normal2.png", "res/Charactor/Gray/Gray-normal1.png",
        // 102-114: Grey ปล่อยพลัง + การต่อสู้ดุเดือด
        "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Gray/Gray-normal1.png", "res/Charactor/Gray/Gray-normal2.png", "res/Charactor/Gray/Gray-normal1.png",
        "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Gray/Gray-normal2.png", "res/Charactor/Gray/Gray-shout.png", "res/Charactor/Gray/Gray-shout.png",
        "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight1.png", "res/Charactor/Dan/dan-fight1.png", "res/Charactor/Gray/Gray-shout.png", "res/Charactor/Gray/Gray-fight1.png",
        // 115-125: เวทย์ทำลายล้าง
        "res/Charactor/Gray/Gray-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
        "res/Charactor/Nebula/Nebula-fight2.png", "res/Charactor/Gray/Gray-fight1.png", "res/Charactor/Gray/Gray-fight1.png", "res/empty.png",
        "res/Charactor/Dan/dan-fight1.png", "res/Charactor/Dan/dan-fight2.png", "res/Charactor/Nebula/Nebula-fight1.png",
        // 126-131: Dan ตัดสินใจสละชีพ
        "res/Charactor/Nebula/Nebula-fight2.png", "res/Charactor/Dan/dan-fight2.png", "res/Charactor/Dan/dan-fight1.png", "res/Charactor/Dan/dan-fight2.png",
        "res/Charactor/Dan/dan-fight1.png", "res/Charactor/Dan/dan-fight2.png",
        // 132-143: Dan พุ่งเข้าหา Grey
        "res/Charactor/Dan/dan-fight1.png", "res/Charactor/Dan/dan-fight2.png", "res/Charactor/Dan/dan-fight2.png", "res/Charactor/Alice/Girl/Alice-fight2.png",
        "res/Charactor/Dan/dan-fight1.png", "res/Charactor/Gray/Gray-fight1.png", "res/Charactor/Gray/Gray-fight1.png", "res/Charactor/Dan/dan-fight1.png",
        "res/Charactor/Dan/dan-fight2.png", "res/Charactor/Gray/Gray-fight1.png", "res/Charactor/Gray/Gray-shout.png", "res/Charactor/Gray/Gray-shout.png",
        // 144-151: Dan สั่งเสีย
        "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-cry2.png",
        "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", //ถึงแถวนี้

        // 152-159: Dan เสียชีวิต
        "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png",
        "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-cry2.png", "res/Charactor/Nebula/Nebula-fight1.png",
        // 160-170: การโจมตีสุดท้ายของ Nebula และอริส
        "res/Charactor/Nebula/Nebula-fight2.png", "res/Charactor/Nebula/Nebula-fight1.png", "res/Charactor/Nebula/Nebula-fight2.png",
        "res/Charactor/Nebula/Nebula-fight1.png", "res/Charactor/Nebula/Nebula-fight1.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-fight2.png",
        "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Gray/Gray-injured.png", "res/Charactor/Gray/Gray-injured.png", "res/Charactor/Gray/Gray-injured.png",
        // 171-178: บทส่งท้าย (ฝังดาบ)
        "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/Charactor/Alice/Girl/Alice-cry1.png",
        "res/Charactor/Nebula/Nebula-normal2.png", "res/Charactor/Nebula/Nebula-normal1.png", "res/Charactor/Nebula/Nebula-normal2.png", "res/empty.png",
        "res/Charactor/Alice/Girl/Alice-cry2.png", "res/empty.png", "res/empty.png",};

    private String[] names = {
        " ", " ", "Dan", "อริส", "ฉัน", "Dan", "Dan", "อริส",
        "อริส", "อริส", "Dan", "Dan", "อริส", "ฉัน", " ", "ปีศาจ", //0-15
        "Dan", "อริส", "ฉัน", "อริส", "Dan", "ฉัน", "ปีศาจ", "Dan",
        "อริส", "ฉัน", " ", " ", "ปีศาจ", " ", "Dan", "อริส", //16-31
        "ฉัน", "Nebula", "ฉัน", "Dan", "อริส", "Nebula", "Dan", "ฉัน",
        "ฉัน", "Nebula", "Nebula", "Dan", "Nebula", "อริส", "Nebula", "Nebula", //32-47
        "Nebula", "Dan", "Nebula", "ฉัน", "Nebula", "Nebula", " ", "Dan",
        "Dan", "Nebula", "Dan", "อริส", "Nebula", "ฉัน", "Nebula", "Nebula", "Nebula", //48-64
        "Dan", "Dan", "อริส", "ฉัน", " ", "Nebula", "Nebula", "Dan",//65-72
        "อริส", "ฉัน", "Nebula", "Nebula", "Nebula", "Dan", "อริส", "Nebula", //73-80
        "Nebula", "Nebula", "Nebula", "ฉัน", "Nebula", " ", " ", " ", //81-88
        " ", "Dan", "อริส", "Nebula", "ฉัน", " ", " ", " ", //89-96
        "Grey", "Grey", "Grey", "Grey", "Nebula", "Grey", "Grey",
        "Grey", "Nebula", "Grey", "Grey", " ", " ", "อริส", "Dan",//97-111
        "Grey", " ", "ฉัน", "อริส", " ", "อริส", "Nebula", "Grey",
        " ", " ", "Dan", "Dan", "Nebula", "Nebula", "Dan", "ฉัน",//112-127
        "Dan", "Dan", "Dan", "ฉัน", "Dan", "Dan", "อริส", "Dan",
        " ", "Grey", " ", " ", "Grey", " ", " ", " ",//128-143
        " ", "ฉัน", "อริส", "Dan", "ฉัน", "Dan", "Dan", "Dan",//144-151
        "Dan", "Dan", " ", " ", "อริส", "ฉัน", "Nebula", "Nebula",
        "Nebula", "ฉัน", " ", "Nebula", "อริส", "อริส", " ", "Grey",//152-167
        "Grey", " ", " ", " ", "อริส", " ", "Nebula", "Nebula", //168-175
        " ", "ฉัน", "อริส", " ", " ", //176-180
    };

    // บทพูด 1-60 (สามารถเพิ่มให้ครบ 105 ได้ในลักษณะเดียวกัน)
    private String[] dialogues = {
        "หลังจากออกจากปราสาทของ Nebula...", "พวกเราเริ่มเดินทางไปยังหุบเขาเงามืด",
        "ถ้าเดินตามเส้นทางนี้ อีกไม่นานก็จะถึงเขตของ Grey แล้ว", "บรรยากาศเริ่มแปลกๆนะ...",
        "อาจจะเป็นเพราะเราเข้าใกล้เขตของมันแล้ว", "ใช่...", "ปีศาจแถวนี้น่าจะเป็นลูกน้องของ Grey", "งั้นต้องระวังให้มากขึ้นแล้ว", //0-7 
        "อือ ฝากด้วยนะ", //choice 1 8
        "นะ...นายพูดแบบนั้นอีกแล้ว",//choice 2 9
        "เดี๋ยวก่อน...", "ฉันรู้สึกถึงบางอย่าง",
        "ฉันก็เหมือนกัน...", "ระวัง!", "ปีศาจหลายตัวพุ่งออกมาจากเงามืด", "กร๊าาา!!",
        "มาแล้ว!", "เตรียมตัว!", "เข้ามาเลย!", "Ice Lance!",
        "ระวังด้านหลัง!", "รับนี่ไป!", "กร๊าาา!", "จํานวนมันเยอะเกินไป!", //10-23 
        "พวกมันยังมาอีก!", "บ้าจริง...", "ทันใดนั้น...", "พลังเวทย์สีม่วงพุ่งลงมาจากด้านบน",
        "กรี๊ดด!!", "ปีศาจหลายตัวถูกทําลายทันที", "นั่นมัน...", "พลังเวทย์แบบนั้น...",
        "หรือว่า...", "ดูเหมือนพวกเจ้าจะลําบากกันอยู่นะ", "Nebula!?", "จอมมารตามมาที่นี่ได้ยังไงเนี่ย!?", //24-35
        "นี่เธอตามพวกเรามาเหรอ?", "ข้าแค่ผ่านมาเท่านั้นเอง", "ไม่มีทางบังเอิญขนาดนั้นหรอก...", "หรือว่า...", //39
        "เธอเป็นห่วงพวกเรา?", //40
        "จะ...จะบ้าเหรอ!", //choice1 41
        "ข้าแค่ไม่อยากให้พวกเจ้าตายง่ายๆก็เท่านั้น",//choice2 42 
        "หน้าจอมมารแดงแล้วนะ...", "หุบปากไป!", "นี่มันสถานการณ์อะไรเนี่ย...", "ปีศาจพวกนี้เป็นลูกน้องของ Grey",
        "ถ้าเข้าใกล้หุบเขาเงามืดมากขึ้น", "พวกมันจะยิ่งแข็งแกร่งขึ้น", "งั้นเราต้องรีบไป",
        "ข้าจะไปกับพวกเจ้าสักพัก", "จริงเหรอ?", //42-51 
        "เจ้าพูดเกินไปแล้ว", //choice1 52
        "เจ้าคนนี้พูดเก่งจริงๆ", //choice2 53
        "หลังจากนั้น พวกเราก็เดินทางต่อ", "มีจอมมารเดินข้างๆแบบนี้...", "มันแปลกจริงๆ", "เจ้าจะบ่นอีกนานไหม",
        "ขอโทษครับ...", "ฉันยังไม่ชินเลยจริงๆ", "มนุษย์นี่วุ่นวายจริงๆ", "แต่เธอก็ยังตามพวกเรามาอยู่ดี",
        "...", //62
        "เจ้านี่พูดอะไรของเจ้า!", //choice1 63
        "อย่าพูดเรื่องไร้สาระแบบนั้น!", //choice2 64
        "เดี๋ยวก่อน...", //65
        "ดูข้างหน้า", "นั่นมัน...", "หุบเขาเงามืด...", "หมอกสีดําปกคลุมหุบเขาขนาดมหึมา",
        "ที่นั่นแหละ", "อาณาเขตของ Grey", "พลังเวทย์มันหนักมาก...", "ฉันรู้สึกขนลุกเลย",
        "งั้นจอมมาร Grey ก็อยู่ที่นี่สินะ", "ใช่", "และจากนี้ไป...", "มันจะอันตรายกว่าที่ผ่านมา",//66-77 
        "งั้นเราพักก่อนดีไหม", "ฉันก็คิดแบบนั้น",//79
        "ข้าแค่ผ่านมาเท่านั้นเอง",//choice1 80
        "เจ้าพูดไม่หยุดเลยนะ...", //choice2 81
        "แต่ว่า...", " เจ้าก็อย่าตายซะก่อนล่ะ", "ถ้ามีเธออยู่ ฉันคงไม่ตายง่ายๆหรอก", "หึ…",
        "หลังจากพักกันเสร็จ", "พวกเราก็มุ่งหน้าเข้าไปในหุบเขา", "หมอกสีดําหนาขึ้นเรื่อยๆ", "จนกระทั่ง…",//82-89
        "ป้อมปราการขนาดใหญ่ก็ปรากฏขึ้น", "นั่นไง…ป้อมของ Grey", "พลังเวทย์มันแรงมาก…", "Grey อยู่ข้างในแน่นอน",
        "งั้นก็ไปกัน", "พวกเราเปิดประตูป้อมเข้าไป", "เสียงหนึ่งดังขึ้นจากห้องโถง", "ในที่สุดก็มาถึงจนได้สินะ",
        "หึๆ…", "Nebula…", "เจ้าถึงกับมาที่นี่ด้วยตัวเองเลยงั้นหรอ", "Grey…", //90-101
        "แล้วมนุษย์พวกนี้คืออะไร", "มนุษย์กับจอมมารร่วมมือกันงั้นหรอ...", "น่าขําสิ้นดี", "Grey หยุดเรื่องทั้งหมดซะ",
        "ข้าไม่มีวันหยุด", "โลกนี้ควรเป็นของปีศาจ!", "พลังเวทย์สีดํามหาศาลระเบิดออกจากร่างของ Grey", "พื้นห้องเริ่มแตก",
        "พลังเวทย์มันเพิ่มขึ้น!", "ระวัง!", "พวกเจ้าจะตายที่นี่!", "Grey ยิงเวทย์ขนาดมหึมาใส่ทุกคน", "หลบเร็ว!",//102-114
        "Ice Shield!", "โล่นํ้าแข็งแตกทันที", "ไม่ไหว!", "พลังของมันเพิ่มขึ้นมาก...",
        "หมดแค่นี้งั้นหรอ?", "Grey เริ่มร่ายเวทย์ขนาดใหญ่", "พื้นทั้งห้องเริ่มสั่น", "เดี๋ยวก่อน...",
        "เวทย์นั่น...", " มันคือเวทย์ทําลายล้าง", "ถ้ามันปล่อยออกมา ทุกคนจะตาย",//115-125
        "...งั้นเหรอ", "Dan?", "ฟังนะ...", "มีแค่ตอนที่มันกําลังร่ายเวทย์นี่แหละ",
        "ที่มันเปิดช่องว่าง", "เดี๋ยวก่อน นายจะทําอะไร!", "นายยังต้องไปต่อ", "นายคือคนที่หยุดมันได้",//126-131
        "Dan อย่า!", "ฝากที่เหลือด้วยนะ", "Dan วิ่งพุ่งเข้าไปหา Grey", "มนุษย์คิดจะทําอะไร?",
        "Dan ใช้พลังทั้งหมดโจมตี", "ดาบแทงเข้าที่ตัว Grey", "แก!", "Grey ปล่อยพลังระเบิดออกมา",
        "แรงระเบิดมหาศาลเกิดขึ้น", "ฝุ่นควันค่อยๆจางลง", "Dan ล้มอยู่กับพื้น", "Dan!!",//132-143
        "ไม่จริง...", "...ดูเหมือนข้าจะไม่รอดแล้วแฮะ", "อย่าพูดแบบนั้น!", "ไม่เป็นไรหรอก",
        "อย่างน้อยก็ได้ผจญภัยสนุกดี", "ไปหยุด Grey ซะ", "...อย่าให้การตายของฉันเสียเปล่า", "Dan หลับตาลง", //144-151
        "มือค่อยๆตกลงกับพื้น", "Dan เสียชีวิต", "...Dan", "...Grey",
        "มนุษย์", "เขาเปิดช่องให้แล้ว", "นี่คือโอกาสเดียว", " ...ไปกัน", //152-159
        "Nebula ใช้เวทย์ขนาดใหญ่", "ตอนนี้!", "Ice Lance!",
        "จบแค่นี้แหละ!", "พลังทั้งหมดพุ่งใส่ Grey", "เป็นไปไม่ได้…", "ข้า…จะไม่แพ้มนุษย์!",
        "การระเบิดครั้งสุดท้ายเกิดขึ้น", "Grey พ่ายแพ้", "หมอกสีดําเริ่มสลาย", "...จบแล้ว", //160-170
        " ...เราชนะแล้ว", "เขาเป็นนักผจญภัยที่ดี", "เขาตายเพื่อช่วยพวกเจ้า", "พวกเราฝังดาบของ Dan ไว้ที่หุบเขา",
        "ขอบคุณนะ", "Dan...", "ลมพัดผ่านหุบเขาเงามืด", "เหมือนกับการอําลา",//171-178
    };

    public part9() {
        setTitle("ISEKAI - Part 9: Path to Darkness");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playBGM("res/sound/soundtrack6.wav", -10.0f);

        // --- ฉากหลังและแผ่น Fade Transition ---
        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        bgFadeOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, (int) (bgAlpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        bgFadeOverlay.setBounds(0, 0, 1280, 800);
        bgFadeOverlay.setOpaque(false);
        layeredPane.add(bgFadeOverlay, Integer.valueOf(JLayeredPane.DEFAULT_LAYER + 1));

        // --- ตัวละครพร้อมระบบ Fade ---
        characterLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, charAlpha));
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        // --- แผ่น Fade In ตอนเริ่มเกม ---
        fadeOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fadeOverlay.setBounds(0, 0, 1280, 800);
        fadeOverlay.setOpaque(false);
        layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);

        startFadeIn();
        setupDialogueUI();
        setupRelationshipUI(); // แสดงผลคะแนนทันทีที่นี่
        setupStatusOverlay();
        setupTabKeyBinding();
        initNetwork();
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNext();
            }
        });
    }

    private void updateScene() {
        if (currentIndex < names.length) {
            nameLabel.setText(names[currentIndex]);
        }
        if (currentIndex < dialogues.length) {
            startTypewriter(dialogues[currentIndex]);
        }

        // Background Transition
        if (currentIndex < imagePaths.length) {
            String newBg = imagePaths[currentIndex];
            if (!newBg.equals(lastBgPath)) {
                startBackgroundTransition(newBg);
                lastBgPath = newBg;
            }
        }

        // Character Fade
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("empty.png")) {
                characterLabel.setIcon(null);
                lastCharPath = path;
            } else if (!path.equals(lastCharPath)) {
                int charW, charH, charX, charY;
                if (path.contains("Nebula")) {
                    charW = 900;
                    charH = 900;
                    charX = (1280 - charW) / 2;
                    charY = 50;
                } else if (path.contains("dan")) {
                    charW = 1400;
                    charH = 1000;
                    charX = (1280 - charW) / 2;
                    charY = 60;
                } else {
                    charW = 1200;
                    charH = 950;
                    charX = (1280 - charW) / 2;
                    charY = 50;
                }
                characterLabel.setBounds(charX, charY, charW, charH);
                characterLabel.setIcon(getOptimizedImage(path, charW, charH));
                startCharacterFadeIn();
                lastCharPath = path;
            }
        }
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    private void startCharacterFadeIn() {
        if (charFadeTimer != null) {
            charFadeTimer.stop();
        }
        charAlpha = 0.0f;
        charFadeTimer = new Timer(20, e -> {
            charAlpha += 0.05f;
            if (charAlpha >= 1.0f) {
                charAlpha = 1.0f;
                ((Timer) e.getSource()).stop();
            }
            characterLabel.repaint();
        });
        charFadeTimer.start();
    }

    private void startBackgroundTransition(String newPath) {
        if (bgFadeTimer != null) {
            bgFadeTimer.stop();
        }
        bgFadeTimer = new Timer(20, null);
        bgFadeTimer.addActionListener(e -> {
            bgAlpha += 0.08f;
            if (bgAlpha >= 1.0f) {
                bgAlpha = 1.0f;
                bgFadeTimer.stop();
                backgroundLabel.setIcon(getOptimizedImage(newPath, 1280, 800));
                Timer fadeIn = new Timer(25, ev -> {
                    bgAlpha -= 0.08f;
                    if (bgAlpha <= 0.0f) {
                        bgAlpha = 0.0f;
                        ((Timer) ev.getSource()).stop();
                    }
                    bgFadeOverlay.repaint();
                });
                fadeIn.start();
            }
            bgFadeOverlay.repaint();
        });
        bgFadeTimer.start();
    }

    private void startFadeIn() {
        alpha = 1.0f;
        new Timer(50, e -> {
            alpha -= 0.02f;
            if (alpha <= 0) {
                alpha = 0;
                ((Timer) e.getSource()).stop();
                layeredPane.remove(fadeOverlay);
            }
            fadeOverlay.repaint();
        }).start();
    }

    private void setupRelationshipUI() {
        JPanel relPanel = new JPanel(new GridLayout(4, 1, 0, 0));
        relPanel.setBounds(0, 0, 280, 120);
        relPanel.setBackground(new Color(0, 0, 0, 190));
        relPanel.setOpaque(true);
        relPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 105, 180), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 10)
        ));

        // แก้ไขให้ดึงค่าเริ่มต้นมาแสดงทันที
        affinityLabel = createRelLabel("อริส: " + relationdata.aliceRel.getAffinity(), new Color(255, 192, 203), 18);
        statusLabel = createRelLabel("สถานะ: " + relationdata.aliceRel.getStatus(), Color.WHITE, 14);
        nebulaAffinityLabel = createRelLabel("เนบิวล่า: " + relationdata.nebulaRel.getAffinity(), new Color(210, 160, 255), 18);
        nebulaStatusLabel = createRelLabel("สถานะ: " + relationdata.nebulaRel.getStatus(), Color.WHITE, 14);

        relPanel.add(affinityLabel);
        relPanel.add(statusLabel);
        relPanel.add(nebulaAffinityLabel);
        relPanel.add(nebulaStatusLabel);
        layeredPane.add(relPanel, JLayeredPane.POPUP_LAYER);
    }

    private JLabel createRelLabel(String t, Color c, int s) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Tahoma", Font.BOLD, s));
        l.setForeground(c);
        return l;
    }

    private void setupStatusOverlay() {
        statusOverlay = new JPanel(new BorderLayout(10, 10));
        statusOverlay.setBackground(new Color(0, 0, 0, 210));
        statusOverlay.setBounds(440, 150, 400, 400);
        statusOverlay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        statusOverlay.setVisible(false);

        JLabel titleLabel = new JLabel("--- Scoreboard ---", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        affinityStatusLabel = new JLabel("กำลังโหลดข้อมูล...", SwingConstants.CENTER);
        affinityStatusLabel.setForeground(Color.WHITE);
        affinityStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        affinityStatusLabel.setVerticalAlignment(SwingConstants.TOP);
        onlineCountLabel = new JLabel("ผู้เล่นออนไลน์: 1", SwingConstants.CENTER);
        onlineCountLabel.setForeground(Color.CYAN);
        onlineCountLabel.setFont(new Font("Tahoma", Font.BOLD, 20));

        statusOverlay.add(titleLabel, BorderLayout.NORTH);
        statusOverlay.add(affinityStatusLabel, BorderLayout.CENTER);
        statusOverlay.add(onlineCountLabel, BorderLayout.SOUTH);
        layeredPane.add(statusOverlay, JLayeredPane.DRAG_LAYER);
    }

    private void setupTabKeyBinding() {
        layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("TAB"), "toggleTab");
        layeredPane.getActionMap().put("toggleTab", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                statusOverlay.setVisible(!statusOverlay.isVisible());
            }
        });
    }

    private void updateLeaderboardUI(String data) {
        StringBuilder sb = new StringBuilder("<html><body style='padding:10px;'><table width='360' style='color:white; font-family:Tahoma;'>");
        sb.append("<tr style='color:#FFD700;'><th align='left' width='160'>ผู้เล่น</th><th align='right' width='90'>อริส</th><th align='right' width='90'>เนบิวล่า</th></tr>");
        for (String p : data.split(",")) {
            if (p.contains("=")) {
                String[] pts = p.split("=");
                String name = pts[0], aV = "0", nV = "0";
                if (pts[1].contains("/")) {
                    String[] sc = pts[1].split("/");
                    aV = sc[0];
                    nV = sc[1];
                } else {
                    aV = pts[1];
                }
                String col = name.equals(relationdata.playerName) ? "#00FF7F" : "white";
                sb.append("<tr><td style='color:").append(col).append(";'>").append(name).append("</td>")
                        .append("<td align='right' style='color:#FFC0CB;'>").append(aV).append("</td>")
                        .append("<td align='right' style='color:#DA70D6;'>").append(nV).append("</td></tr>");
            }
        }
        sb.append("</table></body></html>");
        SwingUtilities.invokeLater(() -> {
            affinityStatusLabel.setText(sb.toString());
            onlineCountLabel.setText("ผู้เล่นออนไลน์: " + data.split(",").length);
        });
    }

    private void handleNext() {
        if (isChoosing || isWaiting) {
            return;
        }
        if (isTyping) {
            stopTypewriter();
            dialogueArea.setText("<html><body style='width: 750px;'>" + dialogues[currentIndex] + "</body></html>");
            return;
        }

        // --- Choice Logic ---
        // Choice 1: 
        if (currentIndex == 7) {
            showChoices("ไม่ต้องห่วง ฉันจะคอยดูรอบๆเอง", "ถ้าเกิดอะไรขึ้น ฉันจะปกป้องเธอ", 8, 9);
            return;
        }
        if (currentIndex == 8) {
            currentIndex = 10;
            updateScene();
            return;
        }

        // Choice 2: 
        if (currentIndex == 40) {
            showChoices("โดยเฉพาะฉัน?", "เธอตามมาช่วยพวกเราสินะ", 41, 42);
            return;
        }
        if (currentIndex == 41) {
            currentIndex = 43;
            updateScene();
            return;
        }

        // Choice 3: 
        if (currentIndex == 51) {
            showChoices("ดีเลย ฉันอุ่นใจขึ้นเยอะ", "ถ้ามีเธออยู่ ฉันก็ไม่กลัวอะไรแล้ว", 52, 53);
            return;
        }
        if (currentIndex == 52) {
            currentIndex = 54;
            updateScene();
            return;
        }

        // Choice 4:
        if (currentIndex == 62) {
            showChoices("หรือว่าเธออยากอยู่ใกล้ฉัน?", "หรือว่าเธอเริ่มชอบพวกเราแล้ว?", 63, 64);
            return;
        }
        if (currentIndex == 63) {
            currentIndex = 65;
            updateScene();
            return;
        }

        // Choice 5:
        if (currentIndex == 79) {
            showChoices("Nebula ขอบคุณนะที่มาช่วย", "ดีใจนะที่เธอมาที่นี่", 80, 81);
            return;
        }
        if (currentIndex == 80) {
            currentIndex = 82;
            updateScene();
            return;
        }

        if (currentIndex < dialogues.length - 1) {
            currentIndex++;
            updateScene();
        } else {
            finishGame();
        }
    }

    private void handleSoundEffects(int index) {
        if (index == 3) {
            playEffect("res/sound/AAno.wav", 5.0f);
        }
        if (index == 9) {
            playEffect("res/sound/Baka janai no.wav", 5.0f);
        }
        if (index == 33) {
            playEffect("res/sound_nebula/hahaha.wav", 5.0f);
        }
        if (index == 41) {
            playEffect("res/sound_nebula/Nani wo.wav", 5.0f);
        }
        if (index == 44) {
            playEffect("res/sound_nebula/Damere.wav", 5.0f);
        }
        if (index == 52) {
            playEffect("res/sound_nebula/lisugi.wav", 5.0f);
        }
        if (index == 141) {
            stopBGM();
            playBGM("res/sound/soundtrack14.wav", -5.0f);
        }
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 380, t1);
        choiceButton2 = createChoiceButton(text2, 450, t2);
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        choiceButton1.setVisible(true);
        choiceButton2.setVisible(true);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            private double scale = 1.0;
            private int alphaMod = 150;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(getWidth() / 2, getHeight() / 2);
                g2.scale(scale, scale);
                g2.translate(-getWidth() / 2, -getHeight() / 2);
                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(new Color(225, 105, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBounds(800, y, 350, 60);
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);

        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false;

            // อัปเดตคะแนนและส่งไป Server
            if (target == 8 || target == 52) {
                relationdata.aliceRel.addAffinity(50);
                if (relationdata.isOnlineMode && networkOut != null) {
                    networkOut.println("UPDATE_AFFINITY:" + relationdata.aliceRel.getAffinity());
                }
            } else if (target == 42 || target == 63 || target == 81) {
                relationdata.nebulaRel.addAffinity(10);
                if (relationdata.isOnlineMode && networkOut != null) {
                    networkOut.println("UPDATE_NEBULA_AFFINITY:" + relationdata.nebulaRel.getAffinity());
                }
            }

            currentIndex = target;
            updateScene();

            affinityLabel.setText("อริส: " + relationdata.aliceRel.getAffinity());
            statusLabel.setText("สถานะ: " + relationdata.aliceRel.getStatus());
            nebulaAffinityLabel.setText("เนบิวล่า: " + relationdata.nebulaRel.getAffinity());
            nebulaStatusLabel.setText("สถานะ: " + relationdata.nebulaRel.getStatus());
        });
        return btn;
    }

    private void setupDialogueUI() {
        dialoguePanel = new VisualNovelBox();
        dialoguePanel.setLayout(null);
        dialoguePanel.setBounds(225, 520, 800, 200);
        layeredPane.add(dialoguePanel, JLayeredPane.MODAL_LAYER);

        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(180, 40, 90));
        nameLabel.setBounds(60, 10, 300, 40);
        dialoguePanel.add(nameLabel);

        dialogueArea = new JLabel();
        dialogueArea.setFont(new Font("Tahoma", Font.BOLD, 22));
        dialogueArea.setForeground(new Color(45, 65, 115));
        dialogueArea.setVerticalAlignment(SwingConstants.TOP);
        dialogueArea.setBounds(60, 60, 800, 110);
        dialoguePanel.add(dialogueArea);

        JLabel nextArrow = new JLabel("▼");
        nextArrow.setFont(new Font("Arial", Font.BOLD, 20));
        nextArrow.setForeground(new Color(0, 153, 255));
        nextArrow.setBounds(750, 130, 30, 30);
        dialoguePanel.add(nextArrow);
        new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible())).start();
    }

    public void playBGM(String path, float volume) {
        try {
            if (bgmClip != null && bgmClip.isRunning()) {
                return;
            }
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioIn);
                FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                bgmClip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playEffect(String path, float volume) {
        try {
            File soundFile = new File(path);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume);
                clip.start();
                this.effectClip = clip;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopBGM() {
        try {
            if (bgmClip != null) {
                if (bgmClip.isRunning()) {
                    bgmClip.stop();
                }
                bgmClip.flush();
                bgmClip.close();
                bgmClip = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishGame() {
        if (isFinishing) {
            return;
        }
        isFinishing = true;

        if (fadeOverlay.getParent() == null) {
            layeredPane.add(fadeOverlay, JLayeredPane.DRAG_LAYER);
        }

        alpha = 0.0f;
        Timer fadeOutTimer = new Timer(30, e -> {
            alpha += 0.02f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                ((Timer) e.getSource()).stop();
                stopBGM();

                if (relationdata.isOnlineMode) {
                    showWaitPoint(); // แสดงหน้าจอดำรอเพื่อน
                } else {
                    goToNextPart(); // ถ้าเล่นคนเดียวให้ข้ามไปคำนวณฉากจบเลย
                }
            }
            fadeOverlay.repaint();
        });
        fadeOutTimer.start();
    }

    private void processOfflineEnding() {
        int aliceScore = relationdata.aliceRel.getAffinity();
        int nebulaScore = relationdata.nebulaRel.getAffinity();
        String message = "จบการผจญภัย (โหมดออฟไลน์)\n";

        // เงื่อนไขการปลดล็อคฉากจบใน Gallery
        if (aliceScore >= 50) {
            relationdata.isEnding2Unlocked = true;
            message += "- ปลดล็อคฉากจบ Alice\n";
        }
        if (nebulaScore >= 50) {
            relationdata.isEnding3Unlocked = true;
            message += "- ปลดล็อคฉากจบ Nebula\n";
        }
        if (relationdata.isEnding2Unlocked && relationdata.isEnding3Unlocked) {
            relationdata.isEnding1Unlocked = true;
            message += "- ปลดล็อคฉากจบพิเศษ Harem!\n";
        }

        JOptionPane.showMessageDialog(this, message);
        openGallery();
    }

    private void calculateOnlineEnding() {
        // 1. เตรียมชื่อและคะแนนเริ่มต้น (P1 คือตัวเราเสมอ)
        String n1 = relationdata.playerName;
        String n2 = "Player 2";
        String n3 = "Player 3";

        int p1A = relationdata.aliceRel.getAffinity();
        int p1N = relationdata.nebulaRel.getAffinity();
        int p2A = 0, p2N = 0, p3A = 0, p3N = 0;

        // 2. ถ้ามีข้อมูลออนไลน์ ให้แยกข้อมูลมาทับค่าเริ่มต้น
        if (allPlayersData != null && !allPlayersData.isEmpty()) {
            String[] players = allPlayersData.split(",");
            int otherIndex = 2;

            for (String p : players) {
                if (p.contains("=")) {
                    String[] parts = p.split("=");
                    String name = parts[0];
                    if (name.equals(relationdata.playerName)) {
                        continue;
                    }

                    int aScore = 0, nScore = 0;
                    if (parts[1].contains("/")) {
                        String[] sc = parts[1].split("/");
                        aScore = Integer.parseInt(sc[0]);
                        nScore = Integer.parseInt(sc[1]);
                    }

                    if (otherIndex == 2) {
                        n2 = name;
                        p2A = aScore;
                        p2N = nScore;
                        otherIndex++;
                    } else if (otherIndex == 3) {
                        n3 = name;
                        p3A = aScore;
                        p3N = nScore;
                        break;
                    }
                }
            }
        }

        // 3. สร้างตัวแปร final เพื่อแก้ Error (effectively final) ในรูป image_f2e80c.png
        final String resN1 = n1;
        final String resN2 = n2;
        final String resN3 = n3;
        final int resP1A = p1A;
        final int resP1N = p1N;
        final int resP2A = p2A;
        final int resP2N = p2N;
        final int resP3A = p3A;
        final int resP3N = p3N;

        // 4. ส่งไปที่ EndingController ทันที ไม่ว่าจะออนไลน์หรือออฟไลน์
        SwingUtilities.invokeLater(() -> {
            new EndingController(resN1, resP1A, resP1N, resN2, resP2A, resP2N, resN3, resP3A, resP3N, "P1").setVisible(true);
            dispose();
        });
    }

    private void startTypewriter(String text) {
        stopTypewriter();
        isTyping = true;
        charIndex = 0;
        dialogueArea.setText("");
        typewriterTimer = new Timer(30, e -> {
            if (charIndex < text.length()) {
                charIndex++;
                dialogueArea.setText("<html><body style='width: 950px;'>" + text.substring(0, charIndex) + "</body></html>");
            } else {
                stopTypewriter();
            }
        });
        typewriterTimer.start();
    }

    private void stopTypewriter() {
        if (typewriterTimer != null) {
            typewriterTimer.stop();
        }
        isTyping = false;
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) {
            try {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                imageCache.put(key, new ImageIcon(img));
            } catch (Exception e) {
                return null;
            }
        }
        return imageCache.get(key);
    }

    private void saveEndingsToServer() {
        if (relationdata.isOnlineMode && networkOut != null) {
            new Thread(() -> {
                // ส่งค่า 1 (ปลดล็อค) หรือ 0 (ยังไม่ปลด) ไปที่ Server
                String e1 = relationdata.isEnding1Unlocked ? "1" : "0";
                String e2 = relationdata.isEnding2Unlocked ? "1" : "0";
                String e3 = relationdata.isEnding3Unlocked ? "1" : "0";
                String e4 = relationdata.isEnding4Unlocked ? "1" : "0";
                networkOut.println("SAVE_ENDINGS:" + e1 + "," + e2 + "," + e3 + "," + e4);
            }).start();
        }
    }

    private void openGallery() {
        saveEndingsToServer(); // บันทึกลง SQL ก่อนเปิดหน้า Gallery
        SwingUtilities.invokeLater(() -> {
            new GalleryPage().setVisible(true);
            dispose();
        });
    }

    private void initNetwork() {
        if (!relationdata.isOnlineMode) {
            return;
        }
        new Thread(() -> {
            try {
                Socket socket = new Socket(relationdata.serverIP, 5000);
                networkOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                networkOut.println("SET_NAME:" + relationdata.playerName);
                networkOut.println("SET_PART:9");
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("ALL_STATS:")) {
                        allPlayersData = line.substring(10); // เก็บข้อมูลสรุปเพื่อใช้ตอนจบ
                        updateLeaderboardUI(allPlayersData);
                    } else if (line.startsWith("LOAD_AFFINITY:")) {
                        int score = Integer.parseInt(line.substring(14));
                        relationdata.aliceRel.setAffinity(score); // รับค่าอริสตรงๆ
                    } else if (line.startsWith("LOAD_NEBULA:")) {
                        int nScore = Integer.parseInt(line.substring(12));
                        relationdata.nebulaRel.setAffinity(nScore); // รับค่าเนบิวล่าตรงๆ
                    } else if (line.startsWith("LOAD_ENDINGS:")) {
                        // รับสถานะ Gallery จาก SQL
                        String[] eds = line.substring(13).split(",");
                        if (eds.length > 0) {
                            relationdata.isEnding1Unlocked = eds[0].equals("1");
                        }
                        if (eds.length > 1) {
                            relationdata.isEnding2Unlocked = eds[1].equals("1");
                        }
                        if (eds.length > 2) {
                            relationdata.isEnding3Unlocked = eds[2].equals("1");
                        }
                        if (eds.length > 3) {
                            relationdata.isEnding4Unlocked = eds[3].equals("1");
                        }
                    }
                    if (line.equals("PROCEED_TO_NEXT")) {
                        goToNextPart();
                    }
                }
            } catch (Exception e) {
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part9().setVisible(true));
    }

    private void showWaitPoint() {
        isWaiting = true;
        waitOverlay = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 220));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        waitOverlay.setBounds(0, 0, 1280, 800);
        waitOverlay.setOpaque(false);
        JLabel msg = new JLabel("WAITING FOR FRIENDS...", SwingConstants.CENTER);
        msg.setFont(new Font("Monospaced", Font.BOLD, 40));
        msg.setForeground(Color.WHITE);
        msg.setBounds(0, 350, 1280, 100);
        waitOverlay.add(msg);
        layeredPane.add(waitOverlay, JLayeredPane.DRAG_LAYER);
        layeredPane.moveToFront(waitOverlay);
        if (networkOut != null) {
            networkOut.println("READY_FOR_NEXT");
        }
        revalidate();
        repaint();
    }

    private void goToNextPart() {
        SwingUtilities.invokeLater(() -> {
            // *** ตรงนี้ต้องเปลี่ยนชื่อ Class ตาม Part เป้าหมาย ***
            calculateOnlineEnding();
            dispose();
        });
    }
}

/* class VisualNovelBox extends JPanel {

    private int cornerRadius = 30;

    public VisualNovelBox() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 250, 255, 180), 0, getHeight(), new Color(255, 235, 245, 230));
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2d.setColor(new Color(255, 150, 200, 200));
        g2d.setStroke(new BasicStroke(4f));
        g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, cornerRadius, cornerRadius);
    }
} */
