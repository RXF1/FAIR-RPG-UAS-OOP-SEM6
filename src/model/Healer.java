package model;

public class Healer extends Karakter implements Penyembuh {
    private int mana;
    private int maxMana;

    public Healer(String nama) {
        super(nama, 130, 18);
        this.mana = 25;
        this.maxMana = 70;
    }

    @Override
    public void serang(Karakter target) {
        System.out.println("🔱 [BASIC ATTACK] " + getNama() + " menusuk tongkat ke " + target.getNama());
        target.setHp(target.getHp() - this.getDamage());
        System.out.println("💥 Menghasilkan " + this.getDamage() + " damage.");
    }

    @Override
    public void gunakanSkill(Karakter target) {
        // BUFF UNTUK STAGE 3: Menggunakan pengali 3.0x agar Healer bisa finis sebelum
        // ronde 5 berakhir
        if (this.mana >= 20) {
            this.mana -= 20;
            int skillDamage = (int) (this.getDamage() * 3.0);
            int pasifHeal = 15;
            System.out.println("☀️ [SKILL: Judgment Light] " + getNama() + " mengeksekusi " + target.getNama() + "!");
            target.setHp(target.getHp() - skillDamage);
            System.out.println("💥 Cahaya suci membakar musuh sebesar " + skillDamage + " damage.");
            setHp(getHp() + pasifHeal);
            System.out.println("💚 Efek Samping Skill: Memulihkan HP pahlawan +" + pasifHeal + ".");
            // Menggunakan fungsi dari Interface untuk menyembuhkan diri sendiri
            pulihkanHp(this, 15);
        } else {
            System.out.println("❌ Mana kurang! Terpaksa memakai [BASIC ATTACK].");
            serang(target);
        }
    }

    // Ini adalah implementasi dari Interface Penyembuh
    @Override
    public void pulihkanHp(Karakter target, int jumlahHeal) {
        target.setHp(target.getHp() + jumlahHeal);
        System.out.println("💚 [EFEK PENYEMBUHAN] " + target.getNama() + " menerima +" + jumlahHeal + " HP!");
    }

    @Override
    public void gunakanHeal() {
        if (mana >= 15) {
            mana -= 15;
            System.out.println("✨ " + getNama() + " merapal 'Holy Blessing'...");
            pulihkanHp(this, 60); // Memanggil interface
        } else {
            System.out.println("❌ Mana habis! Gagal melakukan penyembuhan.");
        }
    }

    public int getMana() {
        return this.mana;
    }

    public void setMana(int mana) {
        this.mana = Math.min(maxMana, mana);
    }

    @Override
    public void terimaRewardDefense() {
        this.mana = Math.min(maxMana, this.mana + 25);
        System.out.println("⚡ REWARD DEFENSE: Healer bermeditasi saat defense dan mendapat +25 Mana!");
    }

    @Override
    public void tampilkanStatusSpesifik() {
        System.out.println("[" + getNama() + " Mana: " + mana + "/" + maxMana + "]");
    }
}