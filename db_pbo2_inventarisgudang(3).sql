-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 29 Jul 2025 pada 05.43
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_pbo2_inventarisgudang`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang`
--

CREATE TABLE `barang` (
  `Id_Barang` int(10) NOT NULL,
  `Nama_Barang` varchar(100) NOT NULL,
  `Kategori_Barang` varchar(50) NOT NULL,
  `Satuan` varchar(50) NOT NULL,
  `Stok` int(10) NOT NULL,
  `Tanggal_Pengiriman` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `barang`
--

INSERT INTO `barang` (`Id_Barang`, `Nama_Barang`, `Kategori_Barang`, `Satuan`, `Stok`, `Tanggal_Pengiriman`) VALUES
(2, 'Kursi Penumpang', 'Kebutuhan Interior Gerbong', 'Pcs', 12, '2025-07-28'),
(3, 'Ac', 'Kebutuhan Interior Gerbong', 'Pcs', 12, '2025-07-28'),
(4, 'Peredam Kabin', 'Lainnya', 'Roll', 4, '2025-07-28'),
(5, 'Bantal', 'Kebutuhan Interior Gerbong', 'Pcs', 60, '2025-07-28'),
(6, 'ban kereta', 'Perkakas & Alat Berat', 'Pcs', 4, '2025-07-28'),
(7, 'Meja', 'Kebutuhan Interior Gerbong', 'Unit', 16, '2025-07-28'),
(8, 'Peredam', 'Kebutuhan Interior Gerbong', 'Unit', 15, '2025-07-28'),
(9, 'Lampu Kabin', 'Kebutuhan Interior Gerbong', 'Unit', 10, '2025-07-28');

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang_keluar`
--

CREATE TABLE `barang_keluar` (
  `Id_Keluar` int(10) NOT NULL,
  `Id_Barang` int(10) NOT NULL,
  `Jumlah_Keluar` int(10) NOT NULL,
  `Tanggal_Keluar` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `barang_keluar`
--

INSERT INTO `barang_keluar` (`Id_Keluar`, `Id_Barang`, `Jumlah_Keluar`, `Tanggal_Keluar`) VALUES
(2, 3, 1, '2025-07-28'),
(3, 2, 1, '2025-07-28'),
(6, 4, 1, '2025-07-28'),
(7, 5, 50, '2025-07-28'),
(8, 6, 1, '2025-07-28'),
(12, 7, 13, '2025-07-28'),
(13, 9, 5, '2025-07-28');

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang_masuk`
--

CREATE TABLE `barang_masuk` (
  `Id_BarangMasuk` int(10) NOT NULL,
  `Id_Barang` int(10) NOT NULL,
  `Jumlah_Masuk` int(10) NOT NULL,
  `Tanggal_Masuk` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `barang_masuk`
--

INSERT INTO `barang_masuk` (`Id_BarangMasuk`, `Id_Barang`, `Jumlah_Masuk`, `Tanggal_Masuk`) VALUES
(1, 3, 1, '2025-07-28'),
(2, 3, 1, '2025-07-28'),
(3, 2, 1, '2025-07-28'),
(4, 5, 10, '2025-07-28'),
(5, 6, 1, '2025-07-28'),
(6, 7, 11, '2025-07-28'),
(7, 9, 5, '2025-07-28');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `Id_User` int(10) NOT NULL,
  `Username` varchar(20) NOT NULL,
  `Password` int(20) NOT NULL,
  `Nama` varchar(100) NOT NULL,
  `Role` enum('Admin','Petugas Gudang','Manajer') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`Id_User`, `Username`, `Password`, `Nama`, `Role`) VALUES
(1, 'admin', 123, 'Rizki', 'Admin'),
(3, 'petugas', 123, 'raihan', 'Petugas Gudang'),
(5, 'manajer', 123, 'alphin', 'Manajer');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`Id_Barang`);

--
-- Indeks untuk tabel `barang_keluar`
--
ALTER TABLE `barang_keluar`
  ADD PRIMARY KEY (`Id_Keluar`),
  ADD UNIQUE KEY `Id_Barang` (`Id_Barang`),
  ADD KEY `Id_Barang_2` (`Id_Barang`);

--
-- Indeks untuk tabel `barang_masuk`
--
ALTER TABLE `barang_masuk`
  ADD PRIMARY KEY (`Id_BarangMasuk`),
  ADD KEY `Id_Barang` (`Id_Barang`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`Id_User`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `barang`
--
ALTER TABLE `barang`
  MODIFY `Id_Barang` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT untuk tabel `barang_keluar`
--
ALTER TABLE `barang_keluar`
  MODIFY `Id_Keluar` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT untuk tabel `barang_masuk`
--
ALTER TABLE `barang_masuk`
  MODIFY `Id_BarangMasuk` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `user`
--
ALTER TABLE `user`
  MODIFY `Id_User` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `barang_keluar`
--
ALTER TABLE `barang_keluar`
  ADD CONSTRAINT `barang_keluar_ibfk_1` FOREIGN KEY (`Id_Barang`) REFERENCES `barang` (`Id_Barang`);

--
-- Ketidakleluasaan untuk tabel `barang_masuk`
--
ALTER TABLE `barang_masuk`
  ADD CONSTRAINT `barang_masuk_ibfk_1` FOREIGN KEY (`Id_Barang`) REFERENCES `barang` (`Id_Barang`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
