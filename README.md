# 📦 UAS_PBO2_KEL8 - Laporan Aplikasi Inventaris Barang

## 1. Latar Belakang

Pengelolaan barang di gudang merupakan aktivitas penting dalam rantai pasokan perusahaan. Banyak gudang masih mencatat inventaris secara manual atau menggunakan spreadsheet sederhana yang rawan terhadap kesalahan pencatatan, kehilangan data, dan duplikasi informasi.

**Sistem Informasi Inventaris Gudang** ini bertujuan untuk menyediakan solusi digital yang membantu admin gudang dan manajemen dalam memantau pergerakan stok, memprediksi kebutuhan pengadaan, serta menjaga efisiensi operasional.  
Aplikasi ini dibangun berbasis **JavaFX** dan menerapkan prinsip **Object-Oriented Programming (OOP)** agar kode lebih modular, reusable, dan mudah dikelola.

---

## 2. Fitur Utama

Aplikasi ini dilengkapi dengan berbagai fitur manajemen inventaris yang komprehensif:

- **🔐 Login & Role-Based Access**  
  Autentikasi pengguna berdasarkan peran: `Admin`, `Petugas Gudang`, dan `Manajer`.

- **📊 Dashboard**  
  Menampilkan ringkasan seperti jumlah total barang, transaksi terakhir, dan status stok.

- **📦 Manajemen Data Barang**  
  Fitur **CRUD** untuk data barang (kode, nama, kategori, stok).

- **📥 Transaksi Barang Masuk**  
  Form input barang masuk yang otomatis memperbarui stok.

- **📤 Transaksi Barang Keluar**  
  Pencatatan pengeluaran barang untuk penjualan atau penggunaan internal.

- **📑 Monitoring & Laporan**  
  Laporan barang masuk, keluar, dan stok saat ini (bisa difilter).

- **🔔 Notifikasi Stok**  
  Sistem memberi peringatan jika stok barang mencapai batas minimum.

---

## 3. Aktor dalam Sistem

| Aktor           | Tugas                                                                 |
|----------------|-----------------------------------------------------------------------|
| **Admin**       | Melihat Data Barang |
| **Petugas Gudang** | Input transaksi masuk & keluar, kelola data barang.                   |
| **Manajer**  | Melihat laporan, ambil keputusan terkait stok & pengadaan.            |

---

## 4. Penerapan Konsep OOP

### a. Encapsulation (Enkapsulasi)

Menyatukan data dan method dalam kelas. Akses data dikendalikan via getter/setter.

```java
public class Barang {
    private String kode;
    private String nama;
    private int stok;

    public String getKode() { 
        return kode; 
    }

    public void setKode(String kode) { 
        this.kode = kode;
    }
}

```
b. Inheritance (Pewarisan)
Subclass mewarisi method dari superclass. Contoh: BarangMasukController mewarisi kontrak dari Initializable.


```java
public class BarangMasukController implements Initializable {
    // ...
}

```
c. Polymorphism (Polimorfisme)
Method yang sama memberikan respons berbeda tergantung objeknya. Contoh: toString() di-overridden.

```java
// Di file Barang.java
@Override
public String toString() {
    return getNamaBarang();
}

// Di file BarangKeluarItem.java
@Override
public String toString() {
    return getNamaBarang();
}

```

d. Abstraction (Abstraksi)
Menyembunyikan detail dan hanya menunjukkan antarmuka penting ke pengguna.
```java

// Di file Barang.java
public class Barang {
    private final SimpleStringProperty namaBarang;

    public String getNamaBarang() {
        return namaBarang.get();
    }
}

```
Dokumentasi aplikasi pbo2 : https://drive.google.com/file/d/1KAPy1WltU9Bp6Pae98y9k-UWomQYSwDv/view
