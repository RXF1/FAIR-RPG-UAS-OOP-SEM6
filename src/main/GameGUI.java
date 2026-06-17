package main;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import model.*;


public class GameGUI extends JFrame {
    private JLabel lblImagePlayer, lblImageMusuh;
    private Karakter player;
    private Monster musuh;
    private int level = 1;
    private int ronde = 1;
    private boolean isMonsterStunned = false;

    // Komponen Visual UI
    private JTextArea logArea;
    private JLabel lblPlayer, lblMusuh, lblStage;
    private JButton btnAttack, btnSkill, btnDefend, btnHeal;

    public GameGUI() {
        pilihKarakter(); // Munculkan pop-up di awal
        setupUI();       // Bangun Jendela Game
        redirectLog();   // Trik Sulap: Belokkan System.out ke Layar GUI
        mulaiLevelBaru();
    }

    // 1. FASE SETUP AWAL
    private void pilihKarakter() {
        String nama = JOptionPane.showInputDialog(this, "Masukkan Nama Hero:", "Fair RPG", JOptionPane.QUESTION_MESSAGE);
        if(nama == null || nama.trim().isEmpty()) nama = "Pahlawan";

        String[] options = {"1. Knight", "2. Wizard", "3. Healer"};
        int choice = JOptionPane.showOptionDialog(this, "Pilih Class Karakter:", "Pemilihan Class",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 1) player = new Penyihir(nama);
        else if (choice == 2) player = new Healer(nama);
        else player = new Kesatria(nama);
    }

    private void setupUI() {
        setTitle("Turn-Based RPG - GUI Edition");
        setSize(850, 700); // Ukuran jendela diperbesar untuk mengakomodasi gambar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Panel Atas (Bar HP)
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.setBackground(new Color(40, 44, 52));
        
        lblPlayer = new JLabel("Player: ", SwingConstants.CENTER);
        lblPlayer.setForeground(new Color(97, 175, 239));
        lblPlayer.setFont(new Font("SansSerif", Font.BOLD, 16));

        lblStage = new JLabel("STAGE 1", SwingConstants.CENTER);
        lblStage.setForeground(Color.WHITE);
        lblStage.setFont(new Font("SansSerif", Font.BOLD, 18));

        lblMusuh = new JLabel("Musuh: ", SwingConstants.CENTER);
        lblMusuh.setForeground(new Color(224, 108, 117));
        lblMusuh.setFont(new Font("SansSerif", Font.BOLD, 16));

        topPanel.add(lblPlayer);
        topPanel.add(lblStage);
        topPanel.add(lblMusuh);
        add(topPanel, BorderLayout.NORTH);

        // --- BAGIAN BARU: Panel Gambar Karakter ---
        JPanel panelVisual = new JPanel(new GridLayout(1, 2));
        panelVisual.setBackground(new Color(30, 34, 40));
        panelVisual.setPreferredSize(new Dimension(850, 200)); // Tinggi ruang gambar 200 piksel

        lblImagePlayer = new JLabel("Hero", SwingConstants.CENTER);
        lblImagePlayer.setForeground(Color.WHITE);
        
        lblImageMusuh = new JLabel("Monster", SwingConstants.CENTER);
        lblImageMusuh.setForeground(Color.WHITE);

        // Memuat file gambar dari folder assets
        muatGambar(lblImagePlayer, "assets/hero.png");
        muatGambar(lblImageMusuh, "assets/monster.png");

        panelVisual.add(lblImagePlayer);
        panelVisual.add(lblImageMusuh);
        // ------------------------------------------

        // Panel Tengah (Layout Gabungan antara Gambar dan Log Teks)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(panelVisual, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(new Color(152, 195, 121));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Panel Bawah (Tombol Kontrol)
        JPanel bottomPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnAttack = new JButton("1. ATTACK");
        btnSkill  = new JButton("2. SKILL");
        btnDefend = new JButton("3. DEFEND");
        btnHeal   = new JButton("4. HEAL");

        bottomPanel.add(btnAttack);
        bottomPanel.add(btnSkill);
        bottomPanel.add(btnDefend);
        bottomPanel.add(btnHeal);
        add(bottomPanel, BorderLayout.SOUTH);

        btnAttack.addActionListener(e -> prosesGiliran(1));
        btnSkill.addActionListener(e -> prosesGiliran(2));
        btnDefend.addActionListener(e -> prosesGiliran(3));
        btnHeal.addActionListener(e -> prosesGiliran(4));
    }

    // Fungsi untuk memuat dan mengatur ukuran (scaling) gambar secara proporsional
    private void muatGambar(JLabel label, String pathFile) {
        try {
            ImageIcon iconAsli = new ImageIcon(pathFile);
            // Mengubah ukuran gambar menjadi 150x150 piksel dengan algoritma pelembutan (SCALE_SMOOTH)
            Image gambarResize = iconAsli.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(gambarResize));
            label.setText(""); // Menghapus teks bawaan jika gambar berhasil dimuat
        } catch (Exception e) {
            label.setText("File gambar tidak ditemukan");
        }
    }

//menyalurkan output terminal ke TextArea GUI dengan format UTF-8 (Emoji)
    private void redirectLog() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                logArea.append(String.valueOf((char) b));
                // Otomatis scroll ke bawah setiap ada teks baru
                logArea.setCaretPosition(logArea.getDocument().getLength());
            }
        };
        try {
            // Memaksa format UTF-8 agar emoji terbaca
            System.setOut(new PrintStream(out, true, "UTF-8"));
        } catch (Exception e) {
            System.setOut(new PrintStream(out, true));
        }
    }

    // 2. FASE LOGIKA GAME ENGINE
        private void updateStatus() {
        String statusTambahan = "";
        
        // Pengecekan tipe hero untuk memunculkan status spesifik
        if (player instanceof Penyihir) {
            statusTambahan = " | Mana: " + ((Penyihir) player).getMana();
        } else if (player instanceof Healer) {
            statusTambahan = " | Mana: " + ((Healer) player).getMana();
        } else if (player instanceof Kesatria) {
            statusTambahan = " | Slot Skill: " + ((Kesatria) player).getSkillSlot() + "/2";
        }

        // Tampilkan ke layar atas
        lblPlayer.setText(player.getNama() + " | HP: " + player.getHp() + statusTambahan);
        
        if(musuh != null) {
            lblMusuh.setText(musuh.getNama() + " | HP: " + musuh.getHp());
        }
        lblStage.setText("STAGE " + level + " | Ronde " + ronde + "/5");
    }

    private void mulaiLevelBaru() {
        musuh = new Monster(level);
        ronde = 1;
        System.out.println("\n==================================================");
        System.out.println("🔺 MEMASUKI STAGE " + level + " : " + musuh.getNama().toUpperCase());
        System.out.println("==================================================");
        musuh.siapkanTindakanRondeBerikutnya(ronde);
        updateStatus();
    }

    // FUNGSI INI ADALAH PENGGANTI LOOPING "WHILE" YANG LAMA
    private void prosesGiliran(int aksi) {
        if (!player.isAlive() || !musuh.isAlive() || ronde > 5) return;

        System.out.println("\n--------------------------------------------------");
        System.out.println("--- RONDE " + ronde + " ---");
        player.setDefending(false);
        isMonsterStunned = false;

        // Giliran Player
        System.out.println("\n-> Giliran Player:");
        if (aksi == 1) player.serang(musuh);
        else if (aksi == 2) { player.gunakanSkill(musuh); isMonsterStunned = true; }
        else if (aksi == 3) { player.setDefending(true); System.out.println(" " + player.getNama() + " merunduk untuk defense!"); }
        else if (aksi == 4) player.gunakanHeal();

        updateStatus();

        if (!musuh.isAlive()) {
            menangStage();
            return;
        }

        // Giliran Musuh
        System.out.println("\n-> Giliran Musuh:");
        if (isMonsterStunned && !player.isDefending()) {
            int damage = musuh.getDamage() / 2;
            player.setHp(player.getHp() - damage);
            System.out.println("💫 Musuh limbung! " + player.getNama() + " hanya menerima " + damage + " damage.");
        } else {
            musuh.serang(player);
        }

        updateStatus();

        if (!musuh.isAlive()) {
            System.out.println("\n " + musuh.getNama() + " tewas terkena Counter Attack!");
            menangStage();
            return;
        }

        if (!player.isAlive()) {
            System.out.println("\n KAMU GAGAL! Perjalanan terhenti.");
            matikanTombol();
            return;
        }

        ronde++;
        if (ronde > 5) {
            System.out.println("\n⏳ KESEMPATAN HABIS! Gagal mengalahkan musuh dalam 5 ronde.");
            matikanTombol();
        } else {
            System.out.println("");
            musuh.siapkanTindakanRondeBerikutnya(ronde);
        }
    }

    private void menangStage() {
        System.out.println("\n " + musuh.getNama() + " berhasil dihancurkan!");
        if (level < 3) {
            level++;
            player.setHp(player.getMaxHp());
            int peningkat = (int)(player.getDamage() * 0.10);
            player.setDamage(player.getDamage() + peningkat);
            System.out.println("✨ STAGE CLEAR! HP dipulihkan penuh. Damage naik +" + peningkat);
            
            // Jeda pop-up sebelum pindah ke ronde berikutnya
            JOptionPane.showMessageDialog(this, "Stage " + (level-1) + " Clear! Bersiap lawan Bos berikutnya.");
            logArea.setText(""); // Bersihkan layar
            mulaiLevelBaru();
        } else {
            System.out.println("\n🏆 KEMENANGAN MUTLAK! Raja Naga Hitam Telah Tumbang.");
            JOptionPane.showMessageDialog(this, "SELAMAT! KAMU MENAMATKAN GAME INI!");
            matikanTombol();
        }
    }

    private void matikanTombol() {
        btnAttack.setEnabled(false);
        btnSkill.setEnabled(false);
        btnDefend.setEnabled(false);
        btnHeal.setEnabled(false);
    }

    // MAIN PROGRAM KHUSUS GUI
    public static void main(String[] args) {
        // Aturan standar Java Swing untuk memulai aplikasi visual
        SwingUtilities.invokeLater(() -> {
            new GameGUI().setVisible(true);
        });
    }
}