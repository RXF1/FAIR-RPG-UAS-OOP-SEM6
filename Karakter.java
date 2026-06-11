public abstract class Karakter {
    private String nama;
    private int hp;
    private int maxHp;
    private int damage;
    private boolean isDefending;

    public Karakter(String nama, int hp, int damage) {
        this.nama = nama;
        this.hp = hp;
        this.maxHp = hp;
        this.damage = damage;
        this.isDefending = false;
    }

    public abstract void serang(Karakter target);

    public abstract void gunakanSkill(Karakter target);

    public abstract void gunakanHeal();

    public abstract void tampilkanStatusSpesifik();

    public abstract void terimaRewardDefense();

    public boolean isAlive() {
        return this.hp > 0;
    }

    public String getNama() {
        return nama;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setHp(int hp) {
        if (hp < 0)
            this.hp = 0;
        else if (hp > this.maxHp)
            this.hp = this.maxHp;
        else
            this.hp = hp;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isDefending() {
        return isDefending;
    }

    public void setDefending(boolean defending) {
        this.isDefending = defending;
    }
}