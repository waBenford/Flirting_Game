package flirting_game;

public class relationship {
    private String characterName;
    private int affinity;

    public relationship(String characterName) {
        this.characterName = characterName;
        this.affinity = 0; // เริ่มต้นที่ 0 เหมือนเดิม
    }
                        
    // เพิ่มค่า: ไม่มีการจำกัดเพดาน 100
    public void addAffinity(int points) {
        this.affinity += points;
        System.out.println(">> " + characterName + " รู้สึกดีกับคุณมากขึ้น!");
    }

    // ลดค่า: สามารถติดลบได้ (กลายเป็นศัตรู)
    public void decreaseAffinity(int points) {
        this.affinity -= points;
        System.out.println(">> " + characterName + " เริ่มไม่พอใจคุณ...");
    }

    // ระบบฉายา/สถานะ ที่รองรับค่าติดลบและค่าที่สูงมาก
    public String getStatus() {
        if (affinity >= 200) return "รักจนยอมตายแทนได้";
        if (affinity >= 100) return "คู่แท้";
        if (affinity >= 50)  return "เพื่อนสนิท";
        if (affinity >= 0)   return "คนรู้จัก";
        if (affinity >= -20) return "ไม่ถูกชะตา";
        if (affinity >= -50) return "คู่อริ";
        return "ศัตรูคู่อาฆาต";
    }

    public int getAffinity() {
        return affinity;
    }

    public String getCharacterName() {
        return characterName;
    }
}