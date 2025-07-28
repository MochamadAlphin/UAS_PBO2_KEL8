# UAS_PBO2_KEL8

📦 Laporan Aplikasi Inventaris Barang
1. Latar Belakang
Pengelolaan barang di gudang merupakan aktivitas penting dalam rantai pasokan perusahaan. Banyak gudang masih mencatat inventaris secara manual atau menggunakan spreadsheet sederhana yang rawan terhadap kesalahan pencatatan, kehilangan data, dan duplikasi informasi.

Sistem Informasi Inventaris Gudang ini bertujuan untuk menyediakan solusi digital yang membantu admin gudang dan manajemen dalam memantau pergerakan stok, memprediksi kebutuhan pengadaan, serta menjaga efisiensi operasional. Aplikasi ini adalah sistem manajemen inventaris barang berbasis JavaFX dan dirancang menggunakan prinsip Object-Oriented Programming (OOP) untuk memastikan kode yang modular, dapat digunakan kembali, dan mudah dikelola.

2. Fitur Utama
Aplikasi ini dilengkapi dengan berbagai fitur untuk manajemen inventaris yang komprehensif.

- Login & Role-Based Access

Sistem autentikasi untuk memastikan hanya pengguna terdaftar yang dapat mengakses sistem. Hak akses dibagi berdasarkan peran (Admin, Petugas Gudang, Supervisor).

- Dashboard

Halaman utama yang menampilkan ringkasan data penting seperti jumlah barang, transaksi masuk terakhir, dan transaksi keluar terakhir.

- Manajemen Data Barang

Fungsi CRUD (Create, Read, Update, Delete) untuk mengelola data barang, termasuk kode, nama, kategori, dan jumlah stok.

- Transaksi Barang Masuk

Formulir khusus untuk mencatat setiap barang yang masuk ke gudang, memperbarui stok secara otomatis.

- Transaksi Barang Keluar

Formulir untuk mencatat barang yang keluar dari gudang, baik untuk penjualan maupun penggunaan internal.

- Monitoring & Laporan

Menyediakan laporan aktivitas inventaris (barang masuk, keluar, dan stok saat ini) yang dapat difilter berdasarkan periode tertentu.

- Notifikasi Stok

Sistem akan memberikan notifikasi otomatis jika stok barang mencapai batas minimum yang telah ditentukan.

3. Aktor dalam Sistem
Berikut adalah peran pengguna yang terlibat dalam sistem dan tugas utamanya:

- Admin

Mengelola data pengguna (tambah, ubah), kategori barang, dan lokasi penyimpanan. Memiliki akses penuh ke semua fitur.

- Petugas Gudang

Mencatat transaksi barang masuk dan keluar, serta mengelola data stok barang.

- Supervisor

Melihat laporan stok dan transaksi untuk membuat keputusan terkait pengadaan atau strategi bisnis.

4. Penerapan Konsep OOP
Aplikasi ini menerapkan konsep-konsep inti Object-Oriented Programming (OOP) berikut:

a. Encapsulation (Enkapsulasi)
Data (atribut) dan operasi (metode) yang terkait dibungkus menjadi satu kesatuan dalam sebuah kelas. Akses ke atribut dikontrol melalui metode getter dan setter untuk menjaga integritas data.

Contoh:

public class Barang {
    private String kode;
    private String nama;
    private int stok;

  public String getKode() { 
        return kode; 
    }
    
public void setKode(String kode) { 
        this.kode = kode;
    }
}

b. Inheritance (Pewarisan)
Sebuah kelas turunan (subclass) dapat mewarisi atribut dan metode dari kelas induknya (superclass). Ini berguna untuk menghindari duplikasi kode.

Contoh:

// Kelas Induk
public class Transaksi {
    private LocalDate tanggal;
    private int jumlah;
  
}

// Kelas Turunan
public class TransaksiMasuk extends Transaksi {
    private String namaPemasok;
    // Mewarisi 'tanggal' dan 'jumlah' dari kelas Transaksi
}

c. Polymorphism (Polimorfisme)
Kemampuan objek untuk merespons secara berbeda terhadap pemanggilan metode yang sama, tergantung pada tipe aktual objek tersebut. Hal ini sering dicapai melalui method overriding.

Contoh:

public abstract class Laporan {
    public abstract void tampilkanLaporan();
}

public class LaporanBarangMasuk extends Laporan {
    @Override
    public void tampilkanLaporan() {
        System.out.println("Menampilkan laporan detail barang masuk...");
    }
}

public class LaporanBarangKeluar extends Laporan {
    @Override
    public void tampilkanLaporan() {
        System.out.println("Menampilkan laporan detail barang keluar...");
    }
}

d. Abstraction (Abstraksi)
Menyembunyikan detail implementasi yang kompleks dan hanya menampilkan fungsionalitas yang esensial kepada pengguna. Abstraksi dapat dicapai dengan menggunakan abstract class atau interface.

Contoh:

public interface TransaksiInterface {
    void simpan();
    void hapus();
    void update();
}

public class BarangMasukController implements TransaksiInterface {
    @Override
    public void simpan() {
        // Logika untuk menyimpan data barang masuk
    }
    
}
