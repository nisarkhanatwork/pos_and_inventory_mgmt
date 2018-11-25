-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Feb 08, 2018 at 05:55 AM
-- Server version: 5.7.19
-- PHP Version: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tractor_bkp`
--

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
CREATE TABLE IF NOT EXISTS `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `town` varchar(64) NOT NULL,
  `party_name` varchar(64) NOT NULL,
  `addr` varchar(64) NOT NULL,
  `addr1` varchar(64) NOT NULL,
  `cell` varchar(64) NOT NULL,
  `tin` varchar(64) NOT NULL,
  `climit` varchar(64) NOT NULL,
  `pdc` varchar(64) NOT NULL,
  `bk` varchar(64) NOT NULL,
  `tpt` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `address`
--

INSERT INTO `address` (`id`, `town`, `party_name`, `addr`, `addr1`, `cell`, `tin`, `climit`, `pdc`, `bk`, `tpt`) VALUES
(1, 'LUR', 'KKULU TRACTOR', 'SPARES               MAIN ROAD', 'AUR -515       NELLORE-DT', 'CELL : 986,943', '3780', '0', '', 'AR (NE', 'KRI'),
(432, 'CHII', 'SRIES & AUTO', 'MOBIS, AI COMPLEX', 'CHIN - 50  (W.G)', 'CELL : 77,929', 'ELR/01/1/3055', '0', '', '', ''),
(433, 'test', 'test', 'test', '', '', '', '', '', '', ''),
(434, 'test1', 'test1', 'test1', '', '', '', '', '', '', ''),
(435, 'test2', 'test2', 'test2', '', '', '', '', '', '', ''),
(9999, '  ', '  ', '  ', '  ', '  ', '  ', '  ', '  ', '  ', '  '),
(10000, 'test3', 'test', 'test23', '', '', '', '', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `bills`
--

DROP TABLE IF EXISTS `bills`;
CREATE TABLE IF NOT EXISTS `bills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
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
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bills`
--

INSERT INTO `bills` (`id`, `date_time`, `s_no`, `bill_no`, `prod_code`, `quant`, `rate`, `amount`, `phone_num`, `phone_num1`, `cancel`, `cgst`, `sgst`, `is_gst`, `is_igst`, `deleted`) VALUES
(1, '2018-02-07 01:23:19', 1, 3, '12', '12.00', '7.75', '93.00', '6', '6', 0, 0.00, 0.00, 1, 0, 0),
(2, '2018-02-07 01:24:41', 1, 4, '76', '2.00', '3.00', '6.00', '6', '6', 0, 0.00, 0.00, 1, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `bill_no`
--

DROP TABLE IF EXISTS `bill_no`;
CREATE TABLE IF NOT EXISTS `bill_no` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `no` int(11) NOT NULL,
  `quote_no` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bill_no`
--

INSERT INTO `bill_no` (`id`, `no`, `quote_no`) VALUES
(1, 5, 1);

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
CREATE TABLE IF NOT EXISTS `customer` (
  `phone_no` varchar(10) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `gst` varchar(15) NOT NULL,
  `aadhar_no` varchar(12) NOT NULL,
  PRIMARY KEY (`phone_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`phone_no`, `first_name`, `last_name`, `state`, `city`, `gst`, `aadhar_no`) VALUES
('0987654321', 'Konkan epr', '1st line brodipet', 'Andhra Pradesh', 'guntyr', '', ''),
('1111111111', 'JKL Enterprises', '23 westford', 'Andhra Pradesh', 'Guntur', '12345', ''),
('1234567890', 'GHI', 'asas', 'Andhra Pradesh', 'Tenali', '', ''),
('2222222222', 'MNO Enterprises', '3rd line pothuri', 'Andhra Pradesh', 'Guntur', '', ''),
('3333333333', 'PQR  estates', '4th line HJS nagar', 'Andhra Pradesh', 'Guntur', '', ''),
('4444444444', 'Lovely associates', '934 street', 'Andhra Pradesh', 'Guntur', '', ''),
('5555555555', 'Punna Engineering', 'police line', 'Andhra Pradesh', 'Guntur', '', ''),
('6666666666', 'WERT tyres', 'pop', 'Andhra Pradesh', 'Gunur', '', ''),
('9059960024', 'DEF Enterprises', '4th lane Kobaldpet', 'Andhra Pradesh', 'Guntur', '', ''),
('9490798734', 'Qamar Enterprises', '1st line', 'Andhra Pradesh', 'Guntur', '', ''),
('9948055734', 'XYZ Enterprises', 'KOTHAPET', 'Andhra Pradesh', 'gnt', '9090', '7878');

-- --------------------------------------------------------

--
-- Table structure for table `gst`
--

DROP TABLE IF EXISTS `gst`;
CREATE TABLE IF NOT EXISTS `gst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(4) NOT NULL,
  `percent` double(5,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;

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

DROP TABLE IF EXISTS `products`;
CREATE TABLE IF NOT EXISTS `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `low_warn` decimal(10,2) NOT NULL DEFAULT '0.00',
  `units` varchar(10) DEFAULT NULL,
  `hsn_code` varchar(30) DEFAULT NULL,
  `cgst` decimal(10,2) DEFAULT '0.00',
  `sgst` decimal(10,2) DEFAULT '0.00',
  `prod_code` varchar(10) DEFAULT NULL,
  `mfg` varchar(45) NOT NULL,
  `prod` varchar(45) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7031 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `low_warn`, `units`, `hsn_code`, `cgst`, `sgst`, `prod_code`, `mfg`, `prod`, `price`) VALUES
(2, '1.00', '', '', '0.00', '0.00', '1', 'BHAI', 'GEAR LEVER ROD SP BHAIRAV............', '247.00'),
(7030, '1.00', '', '', '0.00', '0.00', 'K1062', 'FS', 'GEAR BOX BALLS & SPRING KIT FORD.....', '16.50');

-- --------------------------------------------------------

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
CREATE TABLE IF NOT EXISTS `purchase` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prod_code` varchar(10) NOT NULL,
  `quant` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `transport`
--

DROP TABLE IF EXISTS `transport`;
CREATE TABLE IF NOT EXISTS `transport` (
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
(10, '2018-01-13 10:37:23', 'as', '', '', '', '', '', '');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
