package model;
public class Penyihir extends Karakter {
    private int mana;
    private int maxMana;

    public Penyihir(String nama) {
        super(nama, 110, 15);
        this.mana = 30;
        this.maxMana = 60;
    }

    @Override
    public void serang(Karakter target) {
        System.out.println("🪄 [BASIC ATTACK] " + getNama() + " menembakkan sihir kecil ke " + target.getNama());
        target.setHp(target.getHp() - this.getDamage());
        System.out.println("💥 Menghasilkan " + this.getDamage() + " damage.");
    }

    @Override
    public void gunakanSkill(Karakter target) {
        if (mana >= 25) {
            mana -= 25;
            // BUFF STRATEGIS: Pengali dinaikkan ke 4.2 agar menghasilkan damage pas 71 di
            // Stage 3
            int skillDamage = (int) (this.getDamage() * 4.2);
            System.out.println(
                    "☄️ [SKILL: Meteor Strike] " + getNama() + " merapal mantra raksasa ke " + target.getNama() + "!");
            target.setHp(target.getHp() - skillDamage);
            System.out.println("🔥 BOOM! Ledakan masif menghasilkan " + skillDamage + " damage sihir hancur.");
        } else {
            System.out.println("❌ Mana tidak cukup (Butuh 25 Mana)! Terpaksa memakai [BASIC ATTACK].");
            serang(target);
        }
    }

    @Override
    public void gunakanHeal() {
        if (mana >= 15) {
            mana -= 15;
            int jumlahHeal = 40;
            setHp(getHp() + jumlahHeal);
            System.out
                    .println("✨ [HEAL] " + getNama() + " merapal Sihir Restorasi. Memulihkan +" + jumlahHeal + " HP!");
        } else {
            System.out.println("❌ Mana tidak cukup untuk memulihkan diri!");
        }
    }

    @Override
    public void terimaRewardDefense() {
        this.mana = Math.min(maxMana, this.mana + 30);
        System.out.println("⚡ REWARD DEFENSE: Penyihir menyerap energi musuh menjadi +30 Mana!");
    }

    @Override
    public void tampilkanStatusSpesifik() {
        System.out.println("[" + getNama() + " Mana: " + mana + "/" + maxMana + "]");
    }
}