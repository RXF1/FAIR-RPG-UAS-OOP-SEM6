public class Kesatria extends Karakter {
    private int skillSlot;

    public Kesatria(String nama) {
        super(nama, 160, 25);
        this.skillSlot = 1;
    }

    @Override
    public void serang(Karakter target) {
        System.out.println("⚔️ [BASIC ATTACK] " + getNama() + " menebas " + target.getNama());
        target.setHp(target.getHp() - this.getDamage());
        System.out.println("💥 Menghasilkan " + this.getDamage() + " damage.");
    }

    @Override
    public void gunakanSkill(Karakter target) {
        if (skillSlot > 0) {
            skillSlot--;
            int skillDamage = this.getDamage() * 2;
            System.out.println("🛡️ [SKILL: Shield Slam] " + getNama() + " menghantam " + target.getNama() + "!");
            target.setHp(target.getHp() - skillDamage);
            System.out.println("💥 DAMAGE KRITIKAL! Menghasilkan " + skillDamage + " damage.");
        } else {
            System.out.println("❌ Slot skill habis! Terpaksa memakai [BASIC ATTACK].");
            serang(target);
        }
    }

    @Override
    public void gunakanHeal() {
        int jumlahHeal = 35;
        setHp(getHp() + jumlahHeal);
        System.out.println("🩹 [HEAL] " + getNama() + " memakai Potion Medis. Memulihkan +" + jumlahHeal + " HP!");
    }

    @Override
    public void terimaRewardDefense() {
        if (skillSlot < 2) {
            skillSlot++;
            System.out.println("⚡ REWARD DEFENSE: Kesatria mendapatkan +1 Slot Skill dari momentum bertahan!");
        }
    }

    @Override
    public void tampilkanStatusSpesifik() {
        System.out.println("[" + getNama() + " Slot Skill: " + skillSlot + "/2]");
    }
}