CREATE database clientsDB;

use clientsDB;


CREATE TABLE IF NOT EXISTS `clients` (
  `ID` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `FirstName` varchar(15) NOT NULL,
  `LastName` varchar(15) NOT NULL,
  `Pesel` varchar(11) NOT NULL,
  `Sex` varchar(5) NOT NULL,
  `DocumentType` varchar(17) NOT NULL,
  `DocumentNumber` varchar(10) NOT NULL
);
