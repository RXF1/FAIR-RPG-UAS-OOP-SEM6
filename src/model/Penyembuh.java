// Ini adalah Interface. Hanya berisi deklarasi, tanpa isi (mirip abstract)
package model;
public interface Penyembuh {
    // Siapapun class yang memakai interface ini, WAJIB punya cara menyembuhkan target
    void pulihkanHp(Karakter target, int jumlahHeal);
}