package main;

import java.util.Scanner;
import model.*; // Mengimpor semua class, enum, dan interface dari folder model

public class MainGame {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Karakter player = null;

        System.out.println("========================================");
        System.out.println("   RPG HERO QUEST: FAIR STRATEGY GAME   ");
        System.out.println("========================================");
        System.out.print("Masukkan nama Hero: ");
        String namaHero = input.nextLine();
        
        System.out.println("\nPilih Class Karakter:");
        System.out.println("1. Knight -> " + TipeHero.KESATRIA.getDeskripsi());
        System.out.println("2. Wizard -> " + TipeHero.PENYIHIR.getDeskripsi());
        System.out.println("3. Healer -> " + TipeHero.HEALER.getDeskripsi());
        System.out.print("Pilihan (1-3): ");
        int pilihan = input.nextInt();

        // Ini juga harus TipeHero, BUKAN MainGame
        TipeHero heroTerpilih; 
        switch (pilihan) {
            case 2:
                heroTerpilih = TipeHero.PENYIHIR;
                player = new Penyihir(namaHero);
                break;
            case 3:
                heroTerpilih = TipeHero.HEALER;
                player = new Healer(namaHero);
                break;
            case 1:
            default:
                heroTerpilih = TipeHero.KESATRIA;
                player = new Kesatria(namaHero);
                break;
        }

        System.out.println("\n⚔️ Kamu memilih jalan sebagai " + heroTerpilih.name() + "!");

        boolean kalahGame = false;

        for (int level = 1; level <= 3; level++) {
            if (!player.isAlive())
                break;

            Monster musuh = new Monster(level);

            System.out.println("\n========================================");
            System.out.println("🔺 MEMASUKI LEVEL STAGE " + level + " : " + musuh.getNama().toUpperCase());
            System.out.println("========================================");
            System.out.println("Status Musuh -> HP: " + musuh.getHp() + " | ATK: " + musuh.getDamage());
            System.out.println("Status Hero  -> ATK Saat Ini: " + player.getDamage());

            int ronde = 1;
            int maksRonde = 5;
            boolean musuhTumbang = false;

            while (ronde <= maksRonde && player.isAlive() && musuh.isAlive()) {
                System.out.println("\n----------------------------------------");
                System.out.println("[Ronde " + ronde + "/5]");
                System.out.println("[" + player.getNama() + " HP: " + player.getHp() + "/" + player.getMaxHp() + "]");
                player.tampilkanStatusSpesifik();
                System.out.println("VS [" + musuh.getNama() + " HP: " + musuh.getHp() + "]");
                System.out.println("----------------------------------------");

                musuh.siapkanTindakanRondeBerikutnya(ronde);
                System.out.println("----------------------------------------");

                player.setDefending(false);
                boolean isMonsterStunned = false;

                System.out.println("Tentukan Tindakan Mekanikmu:");
                System.out.println("1. BASIC ATTACK  2. SKILL  3. DEFENSE  4. HEAL");
                System.out.print("Pilih (1-4): ");
                int aksi = input.nextInt();

                System.out.println("\n-> Giliran Player:");
                if (aksi == 1) {
                    player.serang(musuh);
                } else if (aksi == 2) {
                    player.gunakanSkill(musuh);
                    isMonsterStunned = true;
                } else if (aksi == 3) {
                    player.setDefending(true);
                    System.out.println("🛡️ " + player.getNama() + " merunduk dan bersiap melakukan defense!");
                } else if (aksi == 4) {
                    player.gunakanHeal();
                }

                if (!musuh.isAlive()) {
                    musuhTumbang = true;
                    System.out.println("\n💀 " + musuh.getNama() + " berhasil dihancurkan!");
                    break;
                }

                System.out.println("\n-> Giliran Musuh:");

                if (isMonsterStunned && !player.isDefending()) {
                    int damageAsliKarakter = musuh.getDamage();
                    if (ronde % 2 == 0)
                        damageAsliKarakter *= 2;

                    int damageTerguncang = damageAsliKarakter / 2;
                    player.setHp(player.getHp() - damageTerguncang);
                    System.out.println("💫 Efek Guncangan Skill: Serangan " + musuh.getNama() + " limbung!");
                    System.out.println("💥 " + player.getNama() + " hanya menerima " + damageTerguncang + " damage.");
                } else {
                    musuh.serang(player);
                }

                if (!musuh.isAlive()) {
                    musuhTumbang = true;
                    System.out.println("\n💀 " + musuh.getNama() + " tewas akibat Counter Attack-mu!");
                    break;
                }

                if (!player.isAlive()) {
                    kalahGame = true;
                    break;
                }

                ronde++;
            }

            if (!musuhTumbang && player.isAlive()) {
                System.out.println("\n⏳ KESEMPATAN HABIS! Kamu gagal mengalahkan " + musuh.getNama() + " dalam 5 ronde.");
                kalahGame = true;
                break;
            }

            if (kalahGame)
                break;

            if (level < 3) {
                player.setHp(player.getMaxHp());
                int peningkatanDamage = (int) (player.getDamage() * 0.10);
                player.setDamage(player.getDamage() + peningkatanDamage);

                System.out.println("\n✨ STAGE CLEAR! " + player.getNama() + " berhasil membersihkan area.");
                System.out.println("❤️ Sistem Campfire: HP kamu dipulihkan PENUH kembali ke " + player.getMaxHp() + " HP!");
                System.out.println("🔥 Efek Naik Level: Damage permanen bertambah +" + peningkatanDamage + " (Kini: " + player.getDamage() + ")!");
            }
        }

        System.out.println("\n========================================");
        System.out.println("           RINGKASAN HASIL PERMAINAN    ");
        System.out.println("========================================");

        if (!kalahGame && player.isAlive()) {
            System.out.println("🏆 KEMENANGAN MUTLAK! Mekanik Strategimu Luar Biasa Jago!");
        } else {
            System.out.println("💀 KAMU GAGAL! Perjalanan pahlawanmu harus terhenti.");
        }
        System.out.println("========================================");
        input.close();
    }
}