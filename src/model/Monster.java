package model;
public class Monster extends Karakter {
    private int polaAksi;

    public Monster(int levelStage) {
        super(
                (levelStage == 1) ? "Goblin Kroco" : (levelStage == 2) ? "Orc Prajurit" : "Raja Naga Hitam",
                (levelStage == 1) ? 65 : (levelStage == 2) ? 120 : 190,
                (levelStage == 1) ? 12 : (levelStage == 2) ? 20 : 32);
        this.polaAksi = 1;
    }

    public void siapkanTindakanRondeBerikutnya(int ronde) {
        if (ronde % 2 == 0) {
            this.polaAksi = 2;
            System.out.println(" INDIKATOR BAHAYA: " + getNama()
                    + " mengumpulkan tenaga! Dia akan meluncurkan [SERANGAN AMUKAN] (2x Damage)!");
        } else {
            this.polaAksi = 1;
            System.out.println(" INDIKATOR MUSUH: " + getNama() + " bersiap melakukan serangan biasa.");
        }
    }

    @Override
    public void serang(Karakter target) {
        int damageAsli = this.getDamage();
        if (this.polaAksi == 2) {
            damageAsli = damageAsli * 2;
        }

        if (target.isDefending()) {
            int damageTereduksi = damageAsli / 3;
            target.setHp(target.getHp() - damageTereduksi);
            System.out.println(" [DEFENSE SUCCESS] Pertahanan sempurna! " + target.getNama() + " meredam serangan.");
            System.out.println(" " + target.getNama() + " hanya menerima " + damageTereduksi + " damage.");

            // COUNTER ATTACK MECHANIC
            int counterDamage = target.getDamage();
            this.setHp(this.getHp() - counterDamage);
            System.out.println(" [COUNTER ATTACK] Serangan balik instan! Menghasilkan " + counterDamage
                    + " damage ke " + getNama() + "!");

            target.terimaRewardDefense();
        } else {
            target.setHp(target.getHp() - damageAsli);
            System.out.println("👹 [MONSTER ATTACK] " + getNama() + " menghantam telak!");
            System.out.println(" " + target.getNama() + " menerima damage penuh sebesar " + damageAsli + " damage!");
        }
    }

    @Override
    public void gunakanSkill(Karakter target) {
    }

    @Override
    public void gunakanHeal() {
    }

    @Override
    public void terimaRewardDefense() {
    }

    @Override
    public void tampilkanStatusSpesifik() {
    }
}