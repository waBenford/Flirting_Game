package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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
    private Timer typewriterTimer;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Map<String, ImageIcon> imageCache = new HashMap<>();
    
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);
    private final Font THAI_FONT_BOLD = new Font("Tahoma", Font.BOLD, 30);

    // --- แก้ไขจุดนี้: เรียกใช้ฟังก์ชันเพื่อสร้าง Array พื้นหลัง 105 ฉาก ---
    private String[] imagePaths = createBackgrounds();
    
    // ฟังก์ชันกำหนดเงื่อนไขการเปลี่ยน Background
    private String[] createBackgrounds() {
        String[] paths = new String[188];
        for (int i = 0; i < 188; i++) {
            if (i < 10) paths[i] = "res/scene9/s1.png";
            else if (i < 65) paths[i] = "res/scene9/s2.png";
            else if (i < 90) paths[i] = "res/scene9/s3.png";
            else if (i < 96) paths[i] = "res/scene9/s4.png";
            else paths[i] = "res/scene9/s5.png"; // ฉากที่ 105 (ดัชนี 104)
        }
        return paths;
    }
    //
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
            "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", // Nebula (ถ้ามีรูป Nebula ให้เปลี่ยน res/empty.png เป็น path รูป Nebula)
            
            // 36-40: คุยกับ Nebula
            "res/Charactor/Alice/Girl/Alice-normal1.png","res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/empty.png",
            
            // 41-51: Choices Nebula + ตกลงเดินทางด้วยกัน
            "res/empty.png", "res/empty.png", // 41, 42 (Choices Nebula)
            "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal1.png",  "res/empty.png",
            "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/empty.png", 
            
            // 52-62: ระหว่างเดินทาง
            "res/empty.png", "res/empty.png", // 52, 53 (Choices)
            "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/empty.png",
            "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png",
            
            // 63-77: เข้าใกล้หุบเขาเงามืด
            "res/empty.png", "res/empty.png", // 63, 64 (Choices)
            "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png",  "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            
            // 78-89: พักผ่อน + เดินทางต่อ
            "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", // 78, 79 (พักผ่อน)
            "res/empty.png", "res/empty.png", // 80, 81 (Choices)
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            
            // 90-101: ถึงป้อม Grey + เผชิญหน้า
            "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            
            // 102-114: Grey ปล่อยพลัง + การต่อสู้ดุเดือด
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/empty.png", "res/empty.png",
            
            // 115-125: เวทย์ทำลายล้าง
            "res/Charactor/Alice/Girl/Alice-fight1.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png",
            
            // 126-131: Dan ตัดสินใจสละชีพ
            "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png",
            "res/empty.png", "res/Charactor/Dan/dan-normal2.png",
            
            // 132-143: Dan พุ่งเข้าหา Grey
            "res/Charactor/Alice/Girl/Alice-cry1.png", "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/empty.png",
            "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-cry1.png",
            
            // 144-151: Dan สั่งเสีย
            "res/Charactor/Alice/Girl/Alice-cry2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-cry2.png", "res/Charactor/Dan/dan-normal1.png",
            "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png",
            
            // 152-159: Dan เสียชีวิต
            "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-cry2.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            
            // 160-170: การโจมตีสุดท้ายของ Nebula และอริส
            "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
            
            // 171-178: บทส่งท้าย (ฝังดาบ)
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/Charactor/Alice/Girl/Alice-cry1.png", "res/empty.png", "res/empty.png", "res/empty.png"
        };

    private String[] names = { 
        " ", " ","Dan","อริส","ฉัน","Dan","Dan","อริส",
        "อริส","อริส","Dan","Dan","อริส","ฉัน"," ","ปีศาจ",
        "Dan","อริส","ฉัน","อริส","Dan","ฉัน","ปีศาจ","Dan",
        "อริส","ฉัน"," "," ","ปีศาจ"," ","Dan","อริส",
        "ฉัน","Nebula","ฉัน","Dan","อริส","Nebula","Dan","ฉัน",
        "ฉัน","Nebula","Nebula","Dan","Nebula","อริส","Nebula","Nebula",
        "Nebula","Dan","Nebula","ฉัน","Nebula","Nebula"," ","Dan",
        "Dan","Nebula","Dan","อริส","Nebula","ฉัน","Nebula","Nebula","Nebula",
        "Dan","Dan","อริส","ฉัน"," ","Nebula","Nebula","Dan",
        "อริส","ฉัน","Nebula","Nebula","Nebula","Dan","อริส","Nebula",
        "Nebula","Nebula","Nebula","ฉัน","Nebula"," "," "," ",
        " ","Dan","อริส","Nebula","ฉัน"," "," ","Grey",
        "Grey","Grey","Nebula","Grey","Grey","Grey","Nebula",
        "Grey","Grey"," "," ","อริส","Dan","Grey","Grey",
        "ฉัน","อริส"," ","อริส","Nebula","Grey"," "," ",
        "Dan","Dan","Nebula","Nebula","Dan","Dan","ฉัน","Dan",
        "Dan","Dan","ฉัน","Dan","Dan","Dan","อริส","Dan",
        " ","Grey"," "," ","Grey"," "," "," ",
        " ","ฉัน","อริส","Dan","ฉัน"," ","Dan","Dan",
        "Dan","Dan","Dan"," "," "," ","อริส","ฉัน",
        "Nebula","Nebula","ฉัน"," ","Nebula","อริส","ฉัน"," ",
        " ","Grey","Grey"," "," "," ","อริส","ฉัน",
        "Nebula","Nebula"," ","ฉัน","อริส"," "," ",
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
        "กรี๊ดด!!", "ปีศาจหลายตัวถูกทําลายทันที","นั่นมัน...", "พลังเวทย์แบบนั้น...", 
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
        "ขอโทษครับ...", "ฉันยังไม่ชินเลยจริงๆ","มนุษย์นี่วุ่นวายจริงๆ", "แต่เธอก็ยังตามพวกเรามาอยู่ดี", 
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
        "หึๆ…", "Nebula…","เจ้าถึงกับมาที่นี่ด้วยตัวเองเลยงั้นหรอ", "Grey…", //90-101
        "แล้วมนุษย์พวกนี้คืออะไร", "มนุษย์กับจอมมารร่วมมือกันงั้นหรอ...", "น่าขําสิ้นดี", "Grey หยุดเรื่องทั้งหมดซะ", 
        "ข้าไม่มีวันหยุด", "โลกนี้ควรเป็นของปีศาจ!", "พลังเวทย์สีดํามหาศาลระเบิดออกจากร่างของ Grey", "พื้นห้องเริ่มแตก",
        "พลังเวทย์มันเพิ่มขึ้น!","ระวัง!","พวกเจ้าจะตายที่นี่!","Grey ยิงเวทย์ขนาดมหึมาใส่ทุกคน","หลบเร็ว!",//102-114
        "Ice Shield!","โล่นํ้าแข็งแตกทันที","ไม่ไหว!","พลังของมันเพิ่มขึ้นมาก...",
        "หมดแค่นี้งั้นหรอ?","Grey เริ่มร่ายเวทย์ขนาดใหญ่","พื้นทั้งห้องเริ่มสั่น","เดี๋ยวก่อน...",
        "เวทย์นั่น..."," มันคือเวทย์ทําลายล้าง","ถ้ามันปล่อยออกมา ทุกคนจะตาย",//115-125
        "...งั้นเหรอ","Dan?","ฟังนะ...","มีแค่ตอนที่มันกําลังร่ายเวทย์นี่แหละ",
        "ที่มันเปิดช่องว่าง","เดี๋ยวก่อน นายจะทําอะไร!","นายยังต้องไปต่อ","นายคือคนที่หยุดมันได้",//126-131
        "Dan อย่า!","ฝากที่เหลือด้วยนะ","Dan วิ่งพุ่งเข้าไปหา Grey","มนุษย์คิดจะทําอะไร?",
        "Dan ใช้พลังทั้งหมดโจมตี","ดาบแทงเข้าที่ตัว Grey","แก!","Grey ปล่อยพลังระเบิดออกมา",
        "แรงระเบิดมหาศาลเกิดขึ้น","ฝุ่นควันค่อยๆจางลง","Dan ล้มอยู่กับพื้น","Dan!!",//132-143
        "ไม่จริง...","...ดูเหมือนข้าจะไม่รอดแล้วแฮะ","อย่าพูดแบบนั้น!","ไม่เป็นไรหรอก",  
        "อย่างน้อยก็ได้ผจญภัยสนุกดี","ไปหยุด Grey ซะ","...อย่าให้การตายของฉันเสียเปล่า","Dan หลับตาลง", //144-151
        "มือค่อยๆตกลงกับพื้น","Dan เสียชีวิต","...Dan","...Grey",
        "มนุษย์","เขาเปิดช่องให้แล้ว","นี่คือโอกาสเดียว"," ...ไปกัน", //152-159
        "Nebula ใช้เวทย์ขนาดใหญ่","ตอนนี้!","Ice Lance!",
        "จบแค่นี้แหละ!","พลังทั้งหมดพุ่งใส่ Grey","เป็นไปไม่ได้…","ข้า…จะไม่แพ้มนุษย์!",
        "การระเบิดครั้งสุดท้ายเกิดขึ้น","Grey พ่ายแพ้","หมอกสีดําเริ่มสลาย","...จบแล้ว", //160-170
        " ...เราชนะแล้ว","เขาเป็นนักผจญภัยที่ดี","เขาตายเพื่อช่วยพวกเจ้า","พวกเราฝังดาบของ Dan ไว้ที่หุบเขา",
        "ขอบคุณนะ","Dan...","ลมพัดผ่านหุบเขาเงามืด","เหมือนกับการอําลา",//171-178
    };

    public part9() {
        setTitle("ISEKAI  - Part 9: Path to Darkness");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        playBGM("res/sound/soundtrack6.wav", -10.0f);

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 1280, 800);
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        characterLabel = new JLabel();
        layeredPane.add(characterLabel, JLayeredPane.PALETTE_LAYER);

        setupDialogueUI();
        updateScene();

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleNext();
            }
        });
    }

    private void updateScene() {
        if (currentIndex < names.length) nameLabel.setText(names[currentIndex]);
        if (currentIndex < dialogues.length) startTypewriter(dialogues[currentIndex]);
        if (currentIndex < imagePaths.length) backgroundLabel.setIcon(getOptimizedImage(imagePaths[currentIndex], 1280, 800));
        
        if (currentIndex < charPaths.length) {
            String path = charPaths[currentIndex];
            if (path.contains("empty.png")) {
                characterLabel.setIcon(null);
            } else {
                int charW, charH, charX, charY;

                // ใช้โครงสร้าง if - else if - else เพื่อให้เลือกทำงานแค่อย่างเดียว
                if (path.contains("Nebula")) {
                    // ปรับ Nebula ให้ดูตัวใหญ่และสูงขึ้น (จอมมาร)
                    charW = 900; 
                    charH = 900;
                    charX = (1280 - charW) / 2;
                    charY = 50; // ดันขึ้นบนเพื่อให้เห็นความสูง
                } else if (path.contains("dan")) {
                    // สำหรับ Dan (รวม dan-normal2 ไว้ในเงื่อนไขนี้ได้เลยเพราะมีคำว่า dan เหมือนกัน)
                    charW = 1400;
                    charH = 1000;
                    charX = (1280 - charW) / 2;
                    charY = 60; 
                } else {
                    // สำหรับ อริส และตัวละครทั่วไป
                    charW = 1200;
                    charH = 950;
                    charX = (1280 - charW) / 2;
                    charY = 50;
                }

                characterLabel.setBounds(charX, charY, charW, charH);
                characterLabel.setIcon(getOptimizedImage(path, charW, charH));
            }
        }
        handleSoundEffects(currentIndex);
        layeredPane.repaint();
    }

    private void handleNext() {
            if (isChoosing) return;
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
            if (currentIndex == 8) { currentIndex = 10; updateScene(); return; }

            // Choice 2: 
            if (currentIndex == 40) {
                showChoices("โดยเฉพาะฉัน?", "เธอตามมาช่วยพวกเราสินะ", 41, 42);
                return;
            }
            if (currentIndex == 41) { currentIndex = 43; updateScene(); return; }

            // Choice 3: 
            if (currentIndex == 51) {
                showChoices("ดีเลย ฉันอุ่นใจขึ้นเยอะ", "ถ้ามีเธออยู่ ฉันก็ไม่กลัวอะไรแล้ว", 52, 53);
                return;
            }
            if (currentIndex == 52) { currentIndex = 54; updateScene(); return; }

            // Choice 4:
            if (currentIndex == 62) {
                showChoices("หรือว่าเธออยากอยู่ใกล้ฉัน?", "หรือว่าเธอเริ่มชอบพวกเราแล้ว?", 63, 64);
                return;
            }
            if (currentIndex == 63) { currentIndex = 65; updateScene(); return; }
            
            // Choice 5:
            if (currentIndex == 79) {
                showChoices("Nebula ขอบคุณนะที่มาช่วย", "ดีใจนะที่เธอมาที่นี่", 80, 81);
                return;
            }
            if (currentIndex == 80) { currentIndex = 82; updateScene(); return; }

            if (currentIndex < dialogues.length - 1) {
                currentIndex++;
                updateScene();
            } else {
                finishGame();
            }
    }

    private void handleSoundEffects(int index) {
        if (index == 3){
            playEffect("res/sound/AAno.wav", 5.0f);
        }
        if (index == 9){
            playEffect("res/sound/Baka janai no.wav", 5.0f);
        }
        if (index == 33){
            playEffect("res/sound_nebula/hahaha.wav", 5.0f);
        }
        if (index == 41){
            playEffect("res/sound_nebula/Nani wo.wav", 5.0f);
        }
        if (index == 44){
            playEffect("res/sound_nebula/Damere.wav", 5.0f);
        }
        if (index == 52){
            playEffect("res/sound_nebula/lisugi.wav", 5.0f);
        }
        if(index == 141){
            stopBGM();
            playBGM("res/sound/soundtrack14.wav", -5.0f);
        }
    }

    private void showChoices(String text1, String text2, int t1, int t2) {
        isChoosing = true;
        choiceButton1 = createChoiceButton(text1, 380, t1); //y: ขึ้น=ลง
        choiceButton2 = createChoiceButton(text2, 450, t2); //y: ขึ้น=ลง
        layeredPane.add(choiceButton1, JLayeredPane.POPUP_LAYER);
        layeredPane.add(choiceButton2, JLayeredPane.POPUP_LAYER);
        choiceButton1.setVisible(true);
        choiceButton2.setVisible(true);
        layeredPane.repaint();
    }

    private JButton createChoiceButton(String text, int y, int target) {
        JButton btn = new JButton(text) {
            // --- ตัวแปรสำหรับระบบ Animation ---
            private double scale = 1.0;
            private int alphaMod = 150; // ความโปร่งใสเริ่มต้นตามโค้ดพาร์ท 6 ของคุณ
            private Timer animTimer;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // --- คำนวณ Scale Animation ขยายจากจุดศูนย์กลาง ---
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                g2.translate(centerX, centerY);
                g2.scale(scale, scale);
                g2.translate(-centerX, -centerY);

                // วาดพื้นหลังโค้งมน (จะชัดขึ้นเมื่อเมาส์ชี้)
                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // วาดเส้นขอบสีชมพูเข้ม (สีเดิมที่คุณกำหนดไว้)
                g2.setColor(new Color(225, 105, 180)); 
                g2.setStroke(new BasicStroke(2));   
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);

                g2.dispose();
                super.paintComponent(g); // วาดข้อความทับลงไป
            }

            {
                // เพิ่ม Mouse Event สำหรับจัดการ Animation
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        startAnimation(1.05, 200); // เมื่อชี้: ขยาย 5% และพื้นหลังชัดขึ้น
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        startAnimation(1.0, 150); // เมื่อเอาออก: กลับสู่ขนาดปกติ
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        scale = 0.95; // เมื่อกด: ปุ่มยุบตัวลงเล็กน้อยเพื่อให้ดูมีแรงกด
                        repaint();
                    }
                });
            }

            private void startAnimation(double targetScale, int targetAlpha) {
                if (animTimer != null && animTimer.isRunning()) animTimer.stop();
                animTimer = new Timer(15, ev -> {
                    // ค่อยๆ ปรับขนาดปุ่มให้นุ่มนวล
                    if (scale < targetScale) scale += 0.01;
                    else if (scale > targetScale) scale -= 0.01;

                    // ค่อยๆ ปรับความชัดของพื้นหลัง
                    if (alphaMod < targetAlpha) alphaMod += 5;
                    else if (alphaMod > targetAlpha) alphaMod -= 5;

                    if (Math.abs(scale - targetScale) < 0.01 && alphaMod == targetAlpha) {
                        scale = targetScale;
                        ((Timer)ev.getSource()).stop();
                    }
                    repaint();
                });
                animTimer.start();
            }
        };

        // --- ตั้งค่าดีไซน์ปุ่ม (ตามพาร์ท 6 เดิมของคุณ) ---
        btn.setBounds(800, y, 350, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setForeground(new Color(45, 65, 115)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // เปลี่ยนเมาส์เป็นรูปมือ

        // ปิดการวาดส่วนเกินของ Swing ปกติ
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        
        // --- Logic การทำงาน (พาร์ท 6 ยังไม่มี Affinity) ---
        btn.addActionListener(e -> {
            playEffect("res/sound/click.wav", 0.0f);
            layeredPane.remove(choiceButton1);
            layeredPane.remove(choiceButton2);
            isChoosing = false; 
            currentIndex = target; 
            updateScene(); 
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
        Timer arrowTimer = new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible()));
        arrowTimer.start();
    }

    public void playBGM(String path, float volume) {
        try {
            // ถ้าเพลงเดิมเล่นอยู่และเป็นเพลงเดิม ไม่ต้องเริ่มใหม่
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
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY); // เล่นวนลูป
                bgmClip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void playEffect(String path, float volume) {
        try {
            File soundFile = new File(path); 
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                // สร้าง Clip ใหม่ทุกครั้งที่เล่น Effect เพื่อให้เสียงซ้อนกันได้ (ถ้าต้องการ)
                // หรือจะใช้ effectClip ตัวเดียวถ้าต้องการให้เสียงเก่าหยุดก่อน
                Clip clip = AudioSystem.getClip(); 
                clip.open(audioIn);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); 
                clip.start();
                
                // เก็บอ้างอิงไว้เผื่อสั่งหยุด manual
                this.effectClip = clip; 
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void stopBGM() {
        try {
            if (bgmClip != null) {
                if (bgmClip.isRunning()) bgmClip.stop();
                bgmClip.flush();
                bgmClip.close();
                bgmClip = null;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void stopEffect() {
        if (effectClip != null) { effectClip.stop(); effectClip.close(); effectClip = null; }
    }

    private void finishGame() {
        UIManager.put("OptionPane.messageFont", THAI_FONT);
        JOptionPane.showMessageDialog(null, "End Part 6!");
        System.exit(0);
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
            } else { stopTypewriter(); }
        });
        typewriterTimer.start();
    }

    private void stopTypewriter() {
        if (typewriterTimer != null) typewriterTimer.stop();
        isTyping = false;
    }

    private ImageIcon getOptimizedImage(String path, int w, int h) {
        String key = path + w + h;
        if (!imageCache.containsKey(key)) {
            try {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                imageCache.put(key, new ImageIcon(img));
            } catch (Exception e) { return null; }
        }
        return imageCache.get(key);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part9().setVisible(true));
    }
}

/*class VisualNovelBox extends JPanel {
    private int cornerRadius = 30;
    public VisualNovelBox() { setOpaque(false); }
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
}*/