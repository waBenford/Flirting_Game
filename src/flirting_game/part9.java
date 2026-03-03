package flirting_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
    
    private PrintWriter networkOut;
    private String allPlayersData = ""; 
    
    private final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 28);

    private String[] imagePaths = createBackgrounds();
    
    private String[] createBackgrounds() {
        String[] paths = new String[188];
        for (int i = 0; i < 188; i++) {
            if (i < 10) paths[i] = "res/scene9/s1.png";
            else if (i < 65) paths[i] = "res/scene9/s2.png";
            else if (i < 90) paths[i] = "res/scene9/s3.png";
            else if (i < 96) paths[i] = "res/scene9/s4.png";
            else paths[i] = "res/scene9/s5.png";
        }
        return paths;
    }

    private String[] charPaths = { 
            "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
            "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal2.png",
            "res/Charactor/Alice/Girl/Alice-normal1.png", "res/Charactor/Alice/Girl/Alice-shy2.png", 
            "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Dan/dan-normal2.png", 
            "res/Charactor/Alice/Girl/Alice-normal2.png", "res/empty.png", "res/empty.png", "res/empty.png", 
            "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
            "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png",
            "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/empty.png", "res/empty.png", 
            "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", 
            "res/Charactor/Alice/Girl/Alice-normal1.png","res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", 
            "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal1.png",  "res/empty.png",
            "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png", "res/empty.png", 
            "res/empty.png", "res/empty.png", 
            "res/empty.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal2.png", "res/empty.png",
            "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", 
            "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png",  "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", 
            "res/empty.png", "res/empty.png", 
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-normal1.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-fight2.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/Charactor/Alice/Girl/Alice-fight1.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal2.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png",
            "res/empty.png", "res/Charactor/Dan/dan-normal2.png",
            "res/Charactor/Alice/Girl/Alice-cry1.png", "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/empty.png",
            "res/Charactor/Dan/dan-normal2.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/Charactor/Dan/dan-normal2.png", "res/Charactor/Alice/Girl/Alice-cry1.png",
            "res/Charactor/Alice/Girl/Alice-cry2.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Alice/Girl/Alice-cry2.png", "res/Charactor/Dan/dan-normal1.png",
            "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/Charactor/Dan/dan-normal1.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-cry2.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-fight1.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/empty.png",
            "res/empty.png", "res/empty.png", "res/empty.png", "res/Charactor/Alice/Girl/Alice-normal1.png",
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
    
    private String[] dialogues = {
        "หลังจากออกจากปราสาทของ Nebula...", "พวกเราเริ่มเดินทางไปยังหุบเขาเงามืด", 
        "ถ้าเดินตามเส้นทางนี้ อีกไม่นานก็จะถึงเขตของ Grey แล้ว", "บรรยากาศเริ่มแปลกๆนะ...", 
        "อาจจะเป็นเพราะเราเข้าใกล้เขตของมันแล้ว", "ใช่...", "ปีศาจแถวนี้น่าจะเป็นลูกน้องของ Grey", "งั้นต้องระวังให้มากขึ้นแล้ว",
        "อือ ฝากด้วยนะ", "นะ...นายพูดแบบนั้นอีกแล้ว",
        "เดี๋ยวก่อน...", "ฉันรู้สึกถึงบางอย่าง", 
        "ฉันก็เหมือนกัน...", "ระวัง!", "ปีศาจหลายตัวพุ่งออกมาจากเงามืด", "กร๊าาา!!", 
        "มาแล้ว!", "เตรียมตัว!", "เข้ามาเลย!", "Ice Lance!",
        "ระวังด้านหลัง!", "รับนี่ไป!", "กร๊าาา!", "จํานวนมันเยอะเกินไป!", 
        "พวกมันยังมาอีก!", "บ้าจริง...", "ทันใดนั้น...", "พลังเวทย์สีม่วงพุ่งลงมาจากด้านบน", 
        "กรี๊ดด!!", "ปีศาจหลายตัวถูกทําลายทันที","นั่นมัน...", "พลังเวทย์แบบนั้น...", 
        "หรือว่า...", "ดูเหมือนพวกเจ้าจะลําบากกันอยู่นะ", "Nebula!?", "จอมมารตามมาที่นี่ได้ยังไงเนี่ย!?",
        "นี่เธอตามพวกเรามาเหรอ?", "ข้าแค่ผ่านมาเท่านั้นเอง", "ไม่มีทางบังเอิญขนาดนั้นหรอก...", "หรือว่า...",
        "เธอเป็นห่วงพวกเรา?", "จะ...จะบ้าเหรอ!", "ข้าแค่ไม่อยากให้พวกเจ้าตายง่ายๆก็เท่านั้น",
        "หน้าจอมมารแดงแล้วนะ...", "หุบปากไป!", "นี่มันสถานการณ์อะไรเนี่ย...", "ปีศาจพวกนี้เป็นลูกน้องของ Grey", 
        "ถ้าเข้าใกล้หุบเขาเงามืดมากขึ้น", "พวกมันจะยิ่งแข็งแกร่งขึ้น", "งั้นเราต้องรีบไป",
        "ข้าจะไปกับพวกเจ้าสักพัก", "จริงเหรอ?", "เจ้าพูดเกินไปแล้ว", "เจ้าคนนี้พูดเก่งจริงๆ",
        "หลังจากนั้น พวกเราก็เดินทางต่อ", "มีจอมมารเดินข้างๆแบบนี้...", "มันแปลกจริงๆ", "เจ้าจะบ่นอีกนานไหม", 
        "ขอโทษครับ...", "ฉันยังไม่ชินเลยจริงๆ","มนุษย์นี่วุ่นวายจริงๆ", "แต่เธอก็ยังตามพวกเรามาอยู่ดี", 
        "...", "เจ้านี่พูดอะไรของเจ้า!", "อย่าพูดเรื่องไร้สาระแบบนั้น!", 
        "เดี๋ยวก่อน...", "ดูข้างหน้า", "นั่นมัน...", "หุบเขาเงามืด...", "หมอกสีดําปกคลุมหุบเขาขนาดมหึมา",
        "ที่นั่นแหละ", "อาณาเขตของ Grey", "พลังเวทย์มันหนักมาก...", "ฉันรู้สึกขนลุกเลย", 
        "งั้นจอมมาร Grey ก็อยู่ที่นี่สินะ", "ใช่", "และจากนี้ไป...", "มันจะอันตรายกว่าที่ผ่านมา",
        "งั้นเราพักก่อนดีไหม", "ฉันก็คิดแบบนั้น",
        "ข้าแค่ผ่านมาเท่านั้นเอง", "เจ้าพูดไม่หยุดเลยนะ...", 
        "แต่ว่า...", " เจ้าก็อย่าตายซะก่อนล่ะ", "ถ้ามีเธออยู่ ฉันคงไม่ตายง่ายๆหรอก", "หึ…", 
        "หลังจากพักกันเสร็จ", "พวกเราก็มุ่งหน้าเข้าไปในหุบเขา", "หมอกสีดําหนาขึ้นเรื่อยๆ", "จนกระทั่ง…",
        "ป้อมปราการขนาดใหญ่ก็ปรากฏขึ้น", "นั่นไง…ป้อมของ Grey", "พลังเวทย์มันแรงมาก…", "Grey อยู่ข้างในแน่นอน", 
        "งั้นก็ไปกัน", "พวกเราเปิดประตูป้อมเข้าไป", "เสียงหนึ่งดังขึ้นจากห้องโถง", "ในที่สุดก็มาถึงจนได้สินะ", 
        "หึๆ…", "Nebula…","เจ้าถึงกับมาที่นี่ด้วยตัวเองเลยงั้นหรอ", "Grey…",
        "แล้วมนุษย์พวกนี้คืออะไร", "มนุษย์กับจอมมารร่วมมือกันงั้นหรอ...", "น่าขําสิ้นดี", "Grey หยุดเรื่องทั้งหมดซะ", 
        "ข้าไม่มีวันหยุด", "โลกนี้ควรเป็นของปีศาจ!", "พลังเวทย์สีดํามหาศาลระเบิดออกจากร่างของ Grey", "พื้นห้องเริ่มแตก",
        "พลังเวทย์มันเพิ่มขึ้น!","ระวัง!","พวกเจ้าจะตายที่นี่!","Grey ยิงเวทย์ขนาดมหึมาใส่ทุกคน","หลบเร็ว!",
        "Ice Shield!","โล่นํ้าแข็งแตกทันที","ไม่ไหว!","พลังของมันเพิ่มขึ้นมาก...",
        "หมดแค่นี้งั้นหรอ?","Grey เริ่มร่ายเวทย์ขนาดใหญ่","พื้นทั้งห้องเริ่มสั่น","เดี๋ยวก่อน...",
        "เวทย์นั่น..."," มันคือเวทย์ทําลายล้าง","ถ้ามันปล่อยออกมา ทุกคนจะตาย",
        "...งั้นเหรอ","Dan?","ฟังนะ...","มีแค่ตอนที่มันกําลังร่ายเวทย์นี่แหละ",
        "ที่มันเปิดช่องว่าง","เดี๋ยวก่อน นายจะทําอะไร!","นายยังต้องไปต่อ","นายคือคนที่หยุดมันได้",
        "Dan อย่า!","ฝากที่เหลือด้วยนะ","Dan วิ่งพุ่งเข้าไปหา Grey","มนุษย์คิดจะทําอะไร?",
        "Dan ใช้พลังทั้งหมดโจมตี","ดาบแทงเข้าที่ตัว Grey","แก!","Grey ปล่อยพลังระเบิดออกมา",
        "แรงระเบิดมหาศาลเกิดขึ้น","ฝุ่นควันค่อยๆจางลง","Dan ล้มอยู่กับพื้น","Dan!!",
        "ไม่จริง...","...ดูเหมือนข้าจะไม่รอดแล้วแฮะ","อย่าพูดแบบนั้น!","ไม่เป็นไรหรอก",  
        "อย่างน้อยก็ได้ผจญภัยสนุกดี","ไปหยุด Grey ซะ","...อย่าให้การตายของฉันเสียเปล่า","Dan หลับตาลง", 
        "มือค่อยๆตกลงกับพื้น","Dan เสียชีวิต","...Dan","...Grey",
        "มนุษย์","เขาเปิดช่องให้แล้ว","นี่คือโอกาสเดียว"," ...ไปกัน", 
        "Nebula ใช้เวทย์ขนาดใหญ่","ตอนนี้!","Ice Lance!",
        "จบแค่นี้แหละ!","พลังทั้งหมดพุ่งใส่ Grey","เป็นไปไม่ได้…","ข้า…จะไม่แพ้มนุษย์!",
        "การระเบิดครั้งสุดท้ายเกิดขึ้น","Grey พ่ายแพ้","หมอกสีดําเริ่มสลาย","...จบแล้ว", 
        " ...เราชนะแล้ว","เขาเป็นนักผจญภัยที่ดี","เขาตายเพื่อช่วยพวกเจ้า","พวกเราฝังดาบของ Dan ไว้ที่หุบเขา",
        "ขอบคุณนะ","Dan...","ลมพัดผ่านหุบเขาเงามืด","เหมือนกับการอําลา",
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
        initNetwork(); 

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
                if (path.contains("Nebula")) {
                    charW = 900; charH = 900;
                    charX = (1280 - charW) / 2;
                    charY = 50; 
                } else if (path.contains("dan")) {
                    charW = 1400; charH = 1000;
                    charX = (1280 - charW) / 2;
                    charY = 60; 
                } else {
                    charW = 1200; charH = 950;
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

        if (currentIndex == 7) {
            showChoices("อือ ฝากด้วยนะ", "นะ...นายพูดแบบนั้นอีกแล้ว", 8, 9);
            return;
        }
        if (currentIndex == 8) { currentIndex = 10; updateScene(); return; }

        if (currentIndex == 40) {
            showChoices("จะ...จะบ้าเหรอ!", "ข้าแค่ไม่อยากให้พวกเจ้าตายง่ายๆก็เท่านั้น", 41, 42);
            return;
        }
        if (currentIndex == 41) { currentIndex = 43; updateScene(); return; }

        if (currentIndex == 51) {
            showChoices("เจ้าพูดเกินไปแล้ว", "เจ้าคนนี้พูดเก่งจริงๆ", 52, 53);
            return;
        }
        if (currentIndex == 52) { currentIndex = 54; updateScene(); return; }

        if (currentIndex == 62) {
            showChoices("เจ้านี่พูดอะไรของเจ้า!", "อย่าพูดเรื่องไร้สาระแบบนั้น!", 63, 64);
            return;
        }
        if (currentIndex == 63) { currentIndex = 65; updateScene(); return; }

        if (currentIndex == 79) {
            showChoices("ข้าแค่ผ่านมาเท่านั้นเอง", "เจ้าพูดไม่หยุดเลยนะ...", 80, 81);
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
        if (index == 3) playEffect("res/sound/AAno.wav", 5.0f);
        if (index == 9) playEffect("res/sound/Baka janai no.wav", 5.0f);
        if (index == 33) playEffect("res/sound_nebula/hahaha.wav", 5.0f);
        if (index == 41) playEffect("res/sound_nebula/Nani wo.wav", 5.0f);
        if (index == 44) playEffect("res/sound_nebula/Damere.wav", 5.0f);
        if (index == 52) playEffect("res/sound_nebula/lisugi.wav", 5.0f);
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
            private Timer animTimer;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                g2.translate(centerX, centerY);
                g2.scale(scale, scale);
                g2.translate(-centerX, -centerY);
                g2.setColor(new Color(255, 255, 255, alphaMod));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(new Color(225, 105, 180)); 
                g2.setStroke(new BasicStroke(2));   
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { startAnimation(1.05, 200); }
                    @Override
                    public void mouseExited(MouseEvent e) { startAnimation(1.0, 150); }
                    @Override
                    public void mousePressed(MouseEvent e) { scale = 0.95; repaint(); }
                });
            }

            private void startAnimation(double targetScale, int targetAlpha) {
                if (animTimer != null && animTimer.isRunning()) animTimer.stop();
                animTimer = new Timer(15, ev -> {
                    if (scale < targetScale) scale += 0.01;
                    else if (scale > targetScale) scale -= 0.01;
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

        btn.setBounds(800, y, 350, 60); 
        btn.setFont(new Font("Tahoma", Font.BOLD, 16));
        btn.setForeground(new Color(45, 65, 115)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        
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
        new Timer(500, ev -> nextArrow.setVisible(!nextArrow.isVisible())).start();
    }

    public void playBGM(String path, float volume) {
        try {
            if (bgmClip != null && bgmClip.isRunning()) return; 
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
        } catch (Exception e) { e.printStackTrace(); }
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

    private void finishGame() {
        UIManager.put("OptionPane.messageFont", THAI_FONT);
        if (relationdata.isOnlineMode) {
            calculateOnlineEnding();
        } else {
            JOptionPane.showMessageDialog(this, "จบเกม (Offline Mode)");
            if (relationdata.aliceRel.getAffinity() > 50) relationdata.isEnding2Unlocked = true;
            if (relationdata.nebulaRel.getAffinity() > 50) relationdata.isEnding3Unlocked = true;
            if (relationdata.isEnding2Unlocked && relationdata.isEnding3Unlocked) relationdata.isEnding1Unlocked = true;
            new GalleryPage().setVisible(true);
            dispose();
        }
    }

    private void calculateOnlineEnding() {
        if (allPlayersData == null || allPlayersData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ไม่สามารถดึงข้อมูลผู้เล่นอื่นได้");
            return;
        }
        int myAlice = relationdata.aliceRel.getAffinity();
        int myNebula = relationdata.nebulaRel.getAffinity();
        String myName = relationdata.playerName;
        int maxAlice = -9999, maxNebula = -9999;
        
        for (String p : allPlayersData.split(",")) {
            if (p.contains("=")) {
                int score = Integer.parseInt(p.split("=")[1]);
                int aScore = score / 1000;
                int nScore = score % 1000;
                if (aScore > maxAlice) maxAlice = aScore;
                if (nScore > maxNebula) maxNebula = nScore;
            }
        }

        boolean grandWinnerExists = false, amIGrandWinner = false;
        for (String p : allPlayersData.split(",")) {
            if (p.contains("=")) {
                String[] parts = p.split("=");
                int score = Integer.parseInt(parts[1]);
                if (score / 1000 == maxAlice && score % 1000 == maxNebula) {
                    grandWinnerExists = true;
                    if (parts[0].equals(myName)) amIGrandWinner = true;
                }
            }
        }

        String message = "";
        if (grandWinnerExists) {
            if (amIGrandWinner) {
                relationdata.isEnding1Unlocked = true; // ต้องเช็กว่าใน relationdata มีตัวแปรนี้ไหม
                message = "ยินดีด้วย! คุณชนะการแข่งขันและปลดล็อคฉากจบ Harem!";
            } else {
                message = "เสียใจด้วย... ผู้เล่นอื่นทำคะแนนสูงสุดทั้งคู่";
            }
        } else {
            boolean gotEnding = false;
            if (myAlice == maxAlice) { 
                relationdata.isEnding2Unlocked = true; 
                message += "ปลดล็อค Alice Ending\n"; 
                gotEnding = true; 
            }
            if (myNebula == maxNebula) { 
                relationdata.isEnding3Unlocked = true; 
                message += "ปลดล็อค Nebula Ending\n"; 
                gotEnding = true; 
            }
            if (!gotEnding) message = "คุณทำคะแนนไม่ถึงเกณฑ์สูงสุด";
        }

        JOptionPane.showMessageDialog(this, message);
        // ลบ b: ออกเพื่อให้รันผ่าน
        new GalleryPage().setVisible(true); 
        dispose();
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

    private void initNetwork() {
        if (!relationdata.isOnlineMode) return;
        new Thread(() -> {
            try {
                Socket socket = new Socket(relationdata.serverIP, 5000);
                networkOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                networkOut.println("SET_NAME:" + relationdata.playerName);
                int combinedScore = (relationdata.aliceRel.getAffinity() * 1000) + relationdata.nebulaRel.getAffinity();
                networkOut.println("UPDATE_AFFINITY:" + combinedScore);
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("ALL_STATS:")) allPlayersData = line.substring(10);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new part9().setVisible(true));
    }
}

class VisualNovelBox extends JPanel {
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
}