-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 07, 2018 at 12:48 PM
-- Server version: 5.7.21
-- PHP Version: 7.0.22-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tractor`
--

-- --------------------------------------------------------

--
-- Table structure for table `ADDRESS`
--

CREATE TABLE `ADDRESS` (
  `id` int(11) NOT NULL,
  `town` varchar(64) NOT NULL,
  `party_name` varchar(64) NOT NULL,
  `addr` varchar(64) NOT NULL,
  `addr1` varchar(64) NOT NULL,
  `cell` varchar(64) NOT NULL,
  `tin` varchar(64) NOT NULL,
  `climit` varchar(64) NOT NULL,
  `pdc` varchar(64) NOT NULL,
  `bk` varchar(64) NOT NULL,
  `tpt` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ADDRESS`
--

INSERT INTO `ADDRESS` (`id`, `town`, `party_name`, `addr`, `addr1`, `cell`, `tin`, `climit`, `pdc`, `bk`, `tpt`) VALUES
(1, 'ALLUR', 'KOTA RANKULU TRACTOR', 'SPARES               MAIN ROAD', 'ALR -525       NELLORE-DT', 'CELL : 999,944443', '3780', '0', '', 'A (', 'KHI'),
INSERT INTO `ADDRESS` (`id`, `town`, `party_name`, `addr`, `addr1`, `cell`, `tin`, `climit`, `pdc`, `bk`, `tpt`) VALUES
(325, 'KA', 'AUTOMOBIELS', 'MAIN ROAD', 'K - 511', 'CELL : 9181', 'ATP/15', '0', '', '', 'NAVATA'),
(433, 'test', 'test', 'test', '', '', '', '', '', '', ''),
(434, 'test1', 'test1', 'test1', '', '', '', '', '', '', ''),
(435, 'test2', 'test2', 'test2', '', '', '', '', '', '', ''),
(9999, '  ', '  ', '  ', '  ', '  ', '  ', '  ', '  ', '  ', '  '),
(10000, 'test3', 'test', 'test23', '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `backup`
--

CREATE TABLE `backup` (
  `id` int(11) NOT NULL,
  `backup_done` tinyint(1) NOT NULL DEFAULT '0',
  `date` date NOT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `backup`
--

INSERT INTO `backup` (`id`, `backup_done`, `date`, `time_stamp`) VALUES
(1, 1, '2017-12-30', '2017-12-30 15:36:18');

-- --------------------------------------------------------

--
-- Table structure for table `bank_details`
--

CREATE TABLE `bank_details` (
  `name` varchar(64) DEFAULT NULL,
  `branch` varchar(64) DEFAULT NULL,
  `ac_no` varchar(64) DEFAULT NULL,
  `ifsc` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bank_details`
--

INSERT INTO `bank_details` (`name`, `branch`, `ac_no`, `ifsc`) VALUES
('SBI', 'Guntur Bazar', '31', 'SBIN490');

-- --------------------------------------------------------

--
-- Table structure for table `bills`
--

CREATE TABLE `bills` (
  `id` int(11) NOT NULL,
  `date_time` datetime NOT NULL,
  `s_no` int(11) NOT NULL,
  `bill_no` int(10) NOT NULL,
  `prod_code` varchar(10) NOT NULL,
  `quant` decimal(10,2) NOT NULL,
  `rate` decimal(10,2) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `phone_num` varchar(10) NOT NULL,
  `phone_num1` varchar(10) NOT NULL,
  `cancel` tinyint(1) NOT NULL DEFAULT '0',
  `cgst` double(10,2) NOT NULL DEFAULT '0.00',
  `sgst` double(10,2) NOT NULL DEFAULT '0.00',
  `is_gst` tinyint(1) NOT NULL DEFAULT '0',
  `is_igst` tinyint(1) NOT NULL DEFAULT '0',
  `deleted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bills`
--

INSERT INTO `bills` (`id`, `date_time`, `s_no`, `bill_no`, `prod_code`, `quant`, `rate`, `amount`, `phone_num`, `phone_num1`, `cancel`, `cgst`, `sgst`, `is_gst`, `is_igst`, `deleted`) VALUES
(1933, '2018-02-03 20:28:16', 1, 9, '56', '10.00', '260.00', '2600.00', '9999', '9999', 1, 2.50, 2.50, 1, 0, 1),
(2389, '2018-02-05 19:04:12', 1, 60, '4', '4.00', '6.00', '24.00', '9999', '9999', 0, 0.00, 0.00, 1, 0, 0);
INSERT INTO `bills` (`id`, `date_time`, `s_no`, `bill_no`, `prod_code`, `quant`, `rate`, `amount`, `phone_num`, `phone_num1`, `cancel`, `cgst`, `sgst`, `is_gst`, `is_igst`, `deleted`) VALUES
(2390, '2018-02-05 19:04:12', 2, 60, '4', '4.00', '6.00', '24.00', '9999', '9999', 0, 0.00, 0.00, 1, 0, 0),
(2814, '2018-02-07 11:46:23', 1, 102, '56', '6.00', '260.00', '1560.00', '33', '9999', 0, 0.00, 0.00, 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `bill_no`
--

CREATE TABLE `bill_no` (
  `id` int(11) NOT NULL,
  `no` int(11) NOT NULL DEFAULT '1',
  `quote_no` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bill_no`
--

INSERT INTO `bill_no` (`id`, `no`, `quote_no`) VALUES
(1, 103, 3);

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `phone_no` varchar(10) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `gst` varchar(15) NOT NULL,
  `aadhar_no` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`phone_no`, `first_name`, `last_name`, `state`, `city`, `gst`, `aadhar_no`) VALUES
('0987654321', 'Konkan epr', '1st line brodipet', 'Andhra Pradesh', 'guntyr', '', ''),
('9059960024', 'DEF Enterprises', '4th lane Kobaldpet', 'Andhra Pradesh', 'Guntur', '', ''),
('9490798734', 'Qamar Enterprises', '1st line', 'Andhra Pradesh', 'Guntur', '', ''),
('9948055734', 'XYZ Enterprises', 'KOTHAPET', 'Andhra Pradesh', 'gnt', '9090', '7878');

-- --------------------------------------------------------

--
-- Table structure for table `gst`
--

CREATE TABLE `gst` (
  `id` int(11) NOT NULL,
  `type` varchar(4) NOT NULL,
  `percent` double(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `gst`
--

INSERT INTO `gst` (`id`, `type`, `percent`) VALUES
(4, 'sgst', 6.00),
(5, 'igst', 12.00),
(7, 'sgst', 2.50),
(8, 'sgst', 9.00),
(9, 'sgst', 14.00),
(10, 'igst', 5.00),
(11, 'igst', 18.00),
(12, 'igst', 28.00),
(13, 'cgst', 2.50),
(14, 'cgst', 6.00),
(15, 'cgst', 9.00),
(16, 'cgst', 14.00),
(17, 'sgst', 2.50);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `low_warn` decimal(10,2) DEFAULT NULL,
  `units` varchar(10) DEFAULT NULL,
  `hsn_code` varchar(30) DEFAULT NULL,
  `cgst` decimal(10,2) DEFAULT '0.00',
  `sgst` decimal(10,2) DEFAULT '0.00',
  `prod_code` varchar(10) DEFAULT NULL,
  `mfg` varchar(45) NOT NULL,
  `prod` varchar(45) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT '0.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `low_warn`, `units`, `hsn_code`, `cgst`, `sgst`, `prod_code`, `mfg`, `prod`, `price`) VALUES
(2, '1.00', '', '', '0.00', '0.00', '1', 'BHAI', 'GEAR LEVER ROD SP BHAIRAV............', '247.00'),
(13336, '1.00', '', '', '0.00', '0.00', 'V1246', 'VASANI', 'TOP LINK JULA B-275-DI VASANI........', '455.00');

-- --------------------------------------------------------

--
-- Table structure for table `purchase`
--

CREATE TABLE `purchase` (
  `id` int(11) NOT NULL,
  `date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `prod_code` varchar(10) NOT NULL,
  `quant` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `purchase`
--

INSERT INTO `purchase` (`id`, `date_time`, `prod_code`, `quant`) VALUES
(7, '2017-11-21 23:51:02', 'P0015', '10.00'),
(10, '2017-11-22 11:37:39', '', '1.00'),
(11, '2017-11-22 11:38:12', 'V0001', '1.00'),
(12, '2017-11-22 11:38:21', 'JK0002', '1.00'),
(13, '2017-11-22 11:38:49', 'V0001', '1.00'),
(14, '2017-11-22 11:39:00', 'V0114', '1.00'),
(15, '2017-11-22 11:39:09', 'V0209', '1.00'),
(16, '2017-11-22 11:39:22', 'JK0002', '1.00'),
(17, '2017-11-22 11:39:36', 'MRF0001', '1.00'),
(18, '2017-11-22 11:39:47', 'MRF0003', '1.00'),
(19, '2017-11-22 11:40:02', 'MRF0004', '1.00'),
(20, '2017-11-22 11:40:16', 'MRF0004', '1.00'),
(21, '2017-11-22 11:40:31', 'MRF0006', '1.00'),
(22, '2017-11-22 11:40:44', 'MRF0007', '1.00'),
(23, '2017-11-22 11:40:58', 'MRF0008', '1.00'),
(24, '2017-11-22 11:41:08', 'MRF0009', '1.00'),
(25, '2017-11-22 11:41:22', 'MRF0011', '1.00'),
(26, '2017-11-22 11:41:36', 'MRF0013', '1.00'),
(27, '2017-11-22 11:41:48', 'MRF0014', '1.00'),
(28, '2017-11-22 11:42:02', 'MRF0015', '1.00'),
(29, '2017-11-22 11:42:16', 'MRF0016', '1.00'),
(30, '2017-11-22 11:42:29', 'MRF0017', '1.00'),
(31, '2017-11-22 11:42:44', 'MRF0018', '1.00'),
(32, '2017-11-22 11:42:58', 'MRF0020', '1.00'),
(33, '2017-11-22 11:43:13', 'MRF0021', '1.00'),
(34, '2017-11-22 11:43:49', 'JK0003', '1.00'),
(35, '2017-11-22 11:44:00', 'JK0004', '2.00'),
(36, '2017-11-22 11:44:11', 'JK0005', '1.00'),
(37, '2017-11-22 11:44:20', 'JK0006', '1.00'),
(38, '2017-11-22 11:44:28', 'JK0007', '1.00'),
(39, '2017-11-22 11:44:38', 'JK0008', '1.00'),
(40, '2017-11-26 14:02:00', '3456', '20.00'),
(41, '2017-11-27 16:17:58', 'H0001', '2.00'),
(42, '2017-12-15 21:19:05', 'H0001', '442.00'),
(43, '2017-12-19 17:15:13', 'H0001', '1.00'),
(44, '2017-12-19 21:49:04', 'test3', '10.00');

-- --------------------------------------------------------

--
-- Table structure for table `terms`
--

CREATE TABLE `terms` (
  `cond` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `terms`
--

INSERT INTO `terms` (`cond`) VALUES
('Goods once sold will not be taken back or exchanged'),
('All disputes subject to jurisdiction only'),
('This is another term'),
('This is one more term');

-- --------------------------------------------------------

--
-- Table structure for table `transport`
--

CREATE TABLE `transport` (
  `bill_no` int(11) NOT NULL,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(64) NOT NULL,
  `lrno` varchar(64) NOT NULL,
  `lrdate` varchar(20) NOT NULL,
  `freight` varchar(64) NOT NULL,
  `weight` varchar(20) NOT NULL,
  `cases` varchar(10) NOT NULL,
  `pvtmark` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transport`
--

INSERT INTO `transport` (`bill_no`, `date_time`, `name`, `lrno`, `lrdate`, `freight`, `weight`, `cases`, `pvtmark`) VALUES
(1, '2018-01-13 09:42:15', 'asa', 'as', 'as', 'as', 'as', 'as', 'as'),
(1, '2018-01-13 09:42:15', '', '', '', '', '', '', ''),
(1, '2018-01-13 09:42:15', '', '', '', '', '', '', ''),
(1, '2018-01-13 09:42:15', '', '', '', '', '', '', ''),
(2, '2018-01-13 09:49:25', 'asa', 'as', 'as', 'as', 'asa', 'as', 'as'),
(2, '2018-01-13 09:49:25', '', '', '', '', '', '', ''),
(2, '2018-01-13 09:49:25', '', '', '', '', '', '', ''),
(2, '2018-01-13 09:49:25', '', '', '', '', '', '', ''),
(3, '2018-01-13 09:51:50', 'as', 'as', 'as', 'as', 'as', 'as', 'as'),
(3, '2018-01-13 09:51:50', '', '', '', '', '', '', ''),
(3, '2018-01-13 09:51:50', '', '', '', '', '', '', ''),
(3, '2018-01-13 09:51:51', '', '', '', '', '', '', ''),
(4, '2018-01-13 10:07:09', 'as', 'as', 'as', 'as', 'as', 'as', 'as'),
(4, '2018-01-13 10:07:09', '', '', '', '', '', '', ''),
(4, '2018-01-13 10:07:09', '', '', '', '', '', '', ''),
(4, '2018-01-13 10:07:09', '', '', '', '', '', '', ''),
(5, '2018-01-13 10:09:22', 'as', 'as', 'as', 'assd', 'as', 'sad`as`', 'as'),
(6, '2018-01-13 10:12:37', 'as', 'as', 'as', 'as', 'as', 'as', 'as'),
(7, '2018-01-13 10:14:02', '', '', '', '', '', '', ''),
(8, '2018-01-13 10:28:36', 'as', 'as', 'as', 'as', 'as', 'as', 'as'),
(10, '2018-01-13 10:37:23', 'as', '', '', '', '', '', ''),
(3, '2018-01-19 15:39:00', 'TCI EXpress', '1234 ABG', '12-12-2017', '-', '100kg', '10', 'MANSHATM90'),
(89, '2018-02-06 13:29:36', '12', '12', '', '', '12', '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ADDRESS`
--
ALTER TABLE `ADDRESS`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `backup`
--
ALTER TABLE `backup`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bills`
--
ALTER TABLE `bills`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bill_no`
--
ALTER TABLE `bill_no`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`phone_no`);

--
-- Indexes for table `gst`
--
ALTER TABLE `gst`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `purchase`
--
ALTER TABLE `purchase`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `ADDRESS`
--
ALTER TABLE `ADDRESS`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10001;
--
-- AUTO_INCREMENT for table `backup`
--
ALTER TABLE `backup`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `bills`
--
ALTER TABLE `bills`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2815;
--
-- AUTO_INCREMENT for table `gst`
--
ALTER TABLE `gst`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13337;
--
-- AUTO_INCREMENT for table `purchase`
--
ALTER TABLE `purchase`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
