// Enum digunakan untuk menyimpan daftar pilihan yang tetap/pasti
public enum TipeHero {
    KESATRIA("Bisa menahan serangan dengan perisai"),
    PENYIHIR("Memiliki damage sihir area yang besar"),
    HEALER("Bisa memulihkan HP teman");

    private final String deskripsi;

    // Konstruktor Enum
    TipeHero(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
}