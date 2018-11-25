-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 07, 2018 at 12:52 PM
-- Server version: 5.7.21
-- PHP Version: 7.0.22-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jaasdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `keytbl`
--

CREATE TABLE `keytbl` (
  `userid` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `mykey` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `keytbl`
--

INSERT INTO `keytbl` (`userid`, `password`, `mykey`) VALUES
('testcust.ralab@gmail.com', 'Password~123', 'F0WzV64HK38rqy6rIxgAKaa2nKP1VEMZsJG5aS/5Gjwy9lWzyA3XhOJ7gK0DeGAK');

-- --------------------------------------------------------

--
-- Table structure for table `user_auth`
--

CREATE TABLE `user_auth` (
  `userid` varchar(16) DEFAULT NULL,
  `password` varchar(16) DEFAULT NULL,
  `first_name` varchar(64) DEFAULT NULL,
  `last_name` varchar(64) DEFAULT NULL,
  `update_perm` int(11) DEFAULT '0',
  `delete_perm` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_auth`
--

INSERT INTO `user_auth` (`userid`, `password`, `first_name`, `last_name`, `update_perm`, `delete_perm`) VALUES
('reyaz', 'reyaz123', 'Reyaz', 'shaik', 0, 0),
('asif', 'asif123', 'Asif', 'Shaik', 0, 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
