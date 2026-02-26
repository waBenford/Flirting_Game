package flirting_game;

public class relationship {
    private String characterName;
    private int affinity;

    public relationship(String characterName) {
        this.characterName = characterName;
        this.affinity = 0; 
    }

    public void addAffinity(int points) {
        this.affinity += points;
    }

    public void decreaseAffinity(int points) {
        this.affinity -= points;
    }

    // --- ส่วนที่เพิ่ม: เพื่อให้โหลดคะแนนจาก SQL ได้ ---
    public void setAffinity(int points) {
        this.affinity = points;
    }

    public String getStatus() {
        if (affinity >= 200) return "รักจนยอมตายแทนได้";
        if (affinity >= 100) return "คู่แท้";
        if (affinity >= 50)  return "เพื่อนสนิท";
        if (affinity >= 0)   return "คนรู้จัก";
        if (affinity >= -20) return "ไม่ถูกชะตา";
        return "ศัตรูคู่อาฆาต";
    }

    public int getAffinity() { return affinity; }
}