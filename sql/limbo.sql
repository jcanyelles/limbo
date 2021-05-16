-- MySQL dump 10.13  Distrib 8.0.24, for Linux (x86_64)
--
-- Host: localhost    Database: limbo
-- ------------------------------------------------------
-- Server version	8.0.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `limbo2`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `limbo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `limbo`;

--
-- Table structure for table `adreca`
--

DROP TABLE IF EXISTS `adreca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `adreca` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `client_id` int unsigned DEFAULT NULL,
  `carrer` varchar(150) NOT NULL,
  `numero` varchar(40) NOT NULL,
  `pis` int DEFAULT NULL,
  `porta` varchar(40) DEFAULT NULL,
  `CP` varchar(5) NOT NULL,
  `ciutat_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `adreca_client_fk` (`client_id`),
  KEY `adreca_ciutat_fk` (`ciutat_id`),
  CONSTRAINT `adreca_ciutat_fk` FOREIGN KEY (`ciutat_id`) REFERENCES `ciutat` (`id`),
  CONSTRAINT `adreca_client_fk` FOREIGN KEY (`client_id`) REFERENCES `client` (`numero_client`),
  CONSTRAINT `CP_check` CHECK ((length(`CP`) = 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adreca`
--

LOCK TABLES `adreca` WRITE;
/*!40000 ALTER TABLE `adreca` DISABLE KEYS */;
/*!40000 ALTER TABLE `adreca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(15) NOT NULL,
  `descripcio` mediumtext,
  PRIMARY KEY (`id`),
  KEY `nom` (`nom`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Beverages','Soft drinks, coffees, teas, beers, and ales'),(2,'Condiments','Sweet and savory sauces, relishes, spreads, and seasonings'),(3,'Confections','Desserts, candies, and sweet breads'),(4,'Dairy Products','Cheeses'),(5,'Grains/Cereals','Breads, crackers, pasta, and cereal'),(6,'Meat/Poultry','Prepared meats'),(7,'Produce','Dried fruit and bean curd'),(8,'Seafood','Seaweed and fish');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ciutat`
--

DROP TABLE IF EXISTS `ciutat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ciutat` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `nom` varchar(150) NOT NULL,
  `provincia_id` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ciutat_provincia_fk` (`provincia_id`),
  CONSTRAINT `ciutat_provincia_fk` FOREIGN KEY (`provincia_id`) REFERENCES `provincia` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ciutat`
--

LOCK TABLES `ciutat` WRITE;
/*!40000 ALTER TABLE `ciutat` DISABLE KEYS */;
INSERT INTO `ciutat` VALUES (1,'Álava',16),(2,'Albacete',8),(3,'Alicante',10),(4,'Almería',1),(5,'Ávila',7),(6,'Badajoz',11),(7,'Illes Balears',4),(8,'Barcelona',9),(9,'Burgos',7),(10,'Cáceres',11),(11,'Cádiz',1),(12,'Castellón',10),(13,'Ciudad Real',8),(14,'Córdoba',1),(15,'A Coruña',12),(16,'Cuenca',8),(17,'Girona',9),(18,'Granada',1),(19,'Guadalajara',8),(20,'Guipúzcoa',16),(21,'Huelva',1),(22,'Huesca',2),(23,'Jaén',1),(24,'León',7),(25,'Lleida',9),(26,'La Rioja',17),(27,'Lugo',12),(28,'Madrid',13),(29,'Málaga',1),(30,'Murcia',14),(31,'Navarra',15),(32,'Ourense',12),(33,'Asturias',3),(34,'Palencia',7),(35,'Las Palmas',5),(36,'Pontevedra',12),(37,'Salamanca',7),(38,'Santa Cruz de Tenerife',5),(39,'Cantabria',6),(40,'Segovia',7),(41,'Sevilla',1),(42,'Soria',7),(43,'Tarragona',9),(44,'Teruel',2),(45,'Toledo',8),(46,'Valencia',10),(47,'Valladolid',7),(48,'Vizcaya',16),(49,'Zamora',7),(50,'Zaragoza',2),(51,'Ceuta',18),(52,'Melilla',19);
/*!40000 ALTER TABLE `ciutat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `numero_client` int unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(150) NOT NULL,
  `nom` varchar(150) NOT NULL,
  `cognom1` varchar(100) DEFAULT NULL,
  `cognom2` varchar(100) DEFAULT NULL,
  `username` varchar(150) NOT NULL,
  `contrasenya` varchar(200) NOT NULL,
  PRIMARY KEY (`numero_client`),
  UNIQUE KEY `client_email_unique` (`email`),
  UNIQUE KEY `client_nick_unique` (`username`),
  CONSTRAINT `email_check` CHECK (regexp_like(`email`,_utf8mb3'^[A-Z0-9._%-]+@[A-Z0-9.-]+.[A-Z]{2,4}$'))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compra`
--

DROP TABLE IF EXISTS `compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compra` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `targeta_id` int unsigned DEFAULT NULL,
  `adreca_id` int unsigned DEFAULT NULL,
  `client_id` int unsigned DEFAULT NULL,
  `id_transaccio` varchar(100) NOT NULL,
  `data` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `compra_targeta_fk` (`targeta_id`),
  KEY `compra_adreca_fk` (`adreca_id`),
  KEY `compra_client_fk` (`client_id`),
  CONSTRAINT `compra_adreca_fk` FOREIGN KEY (`adreca_id`) REFERENCES `adreca` (`id`),
  CONSTRAINT `compra_client_fk` FOREIGN KEY (`client_id`) REFERENCES `client` (`numero_client`),
  CONSTRAINT `compra_targeta_fk` FOREIGN KEY (`targeta_id`) REFERENCES `targeta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compra`
--

LOCK TABLES `compra` WRITE;
/*!40000 ALTER TABLE `compra` DISABLE KEYS */;
/*!40000 ALTER TABLE `compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detall_compra`
--

DROP TABLE IF EXISTS `detall_compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detall_compra` (
  `compra_id` int unsigned NOT NULL,
  `producte_id` int unsigned NOT NULL,
  `pvp` double NOT NULL,
  `pes` double DEFAULT NULL,
  `unitats_producte` int NOT NULL,
  PRIMARY KEY (`compra_id`,`producte_id`),
  KEY `detall_compra_producte_fk` (`producte_id`),
  CONSTRAINT `detall_compra_compra_fk` FOREIGN KEY (`compra_id`) REFERENCES `compra` (`id`),
  CONSTRAINT `detall_compra_producte_fk` FOREIGN KEY (`producte_id`) REFERENCES `producte` (`id`),
  CONSTRAINT `check_unitats_producte` CHECK ((`unitats_producte` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detall_compra`
--

LOCK TABLES `detall_compra` WRITE;
/*!40000 ALTER TABLE `detall_compra` DISABLE KEYS */;
/*!40000 ALTER TABLE `detall_compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producte`
--

DROP TABLE IF EXISTS `producte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producte` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `nom` varchar(150) NOT NULL,
  `descripcio` mediumtext,
  `pvp` double NOT NULL,
  `iva` int NOT NULL,
  `marca` varchar(150) DEFAULT NULL,
  `unitat_mesura` varchar(100) DEFAULT NULL,
  `pes` double DEFAULT NULL,
  `categoria` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `producte_nom_unique` (`nom`),
  KEY `categoria` (`categoria`),
  CONSTRAINT `producte_ibfk_1` FOREIGN KEY (`categoria`) REFERENCES `categoria` (`id`),
  CONSTRAINT `IVA_check` CHECK ((`IVA` in (4,10,21)))
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producte`
--

LOCK TABLES `producte` WRITE;
/*!40000 ALTER TABLE `producte` DISABLE KEYS */;
INSERT INTO `producte` VALUES (1,'Cte de Blaye','Cte de Blaye 12 - 75 cl bottles',263.5,21,'Aux joyeux ecclsiastiques',NULL,NULL,1),(2,'Chartreuse verte','Chartreuse verte 750 cc per bottle',18,21,'Aux joyeux ecclsiastiques',NULL,NULL,1),(3,'Sasquatch Ale','Sasquatch Ale 24 - 12 oz bottles',14,21,'Bigfoot Breweries',NULL,NULL,1),(4,'Steeleye Stout','Steeleye Stout 24 - 12 oz bottles',18,21,'Bigfoot Breweries',NULL,NULL,1),(5,'Laughing Lumberjack Lager','Laughing Lumberjack Lager 24 - 12 oz bottles',14,21,'Bigfoot Breweries',NULL,NULL,1),(6,'Queso Cabrales','Queso Cabrales 1 kg pkg.',21,21,'Cooperativa de Quesos \'Las Cabras\'',NULL,NULL,4),(7,'Queso Manchego La Pastora','Queso Manchego La Pastora 10 - 500 g pkgs.',38,21,'Cooperativa de Quesos \'Las Cabras\'',NULL,NULL,4),(8,'Escargots de Bourgogne','Escargots de Bourgogne 24 pieces',13.25,21,'Escargots Nouveaux',NULL,NULL,8),(9,'Chai','Chai 10 boxes x 20 bags',18,21,'Exotic Liquids',NULL,NULL,1),(10,'Chang','Chang 24 - 12 oz bottles',19,21,'Exotic Liquids',NULL,NULL,1),(11,'Aniseed Syrup','Aniseed Syrup 12 - 550 ml bottles',10,21,'Exotic Liquids',NULL,NULL,2),(12,'Gorgonzola Telino','Gorgonzola Telino 12 - 100 g pkgs',12.5,21,'Formaggi Fortini s.r.l.',NULL,NULL,4),(13,'Mascarpone Fabioli','Mascarpone Fabioli 24 - 200 g pkgs.',32,21,'Formaggi Fortini s.r.l.',NULL,NULL,4),(14,'Mozzarella di Giovanni','Mozzarella di Giovanni 24 - 200 g pkgs.',34.8,21,'Formaggi Fortini s.r.l.',NULL,NULL,4),(15,'Sirop d\'rable','Sirop d\'rable 24 - 500 ml bottles',28.5,21,'Forts d\'rables',NULL,NULL,2),(16,'Tarte au sucre','Tarte au sucre 48 pies',49.3,21,'Forts d\'rables',NULL,NULL,3),(17,'Manjimup Dried Apples','Manjimup Dried Apples 50 - 300 g pkgs.',53,21,'G\'day, Mate',NULL,NULL,7),(18,'Filo Mix','Filo Mix 16 - 2 kg boxes',7,21,'G\'day, Mate',NULL,NULL,5),(19,'Perth Pasties','Perth Pasties 48 pieces',32.8,21,'G\'day, Mate',NULL,NULL,6),(20,'Raclette Courdavault','Raclette Courdavault 5 kg pkg.',55,21,'Gai pturage',NULL,NULL,4),(21,'Camembert Pierrot','Camembert Pierrot 15 - 300 g rounds',34,21,'Gai pturage',NULL,NULL,4),(22,'Grandma\'s Boysenberry Spread','Grandma\'s Boysenberry Spread 12 - 8 oz jars',25,21,'Grandma Kelly\'s Homestead',NULL,NULL,2),(23,'Uncle Bob\'s Organic Dried Pears','Uncle Bob\'s Organic Dried Pears 12 - 1 lb pkgs.',30,21,'Grandma Kelly\'s Homestead',NULL,NULL,7),(24,'Northwoods Cranberry Sauce','Northwoods Cranberry Sauce 12 - 12 oz jars',40,21,'Grandma Kelly\'s Homestead',NULL,NULL,2),(25,'NuNuCa Nu-Nougat-Creme','NuNuCa Nu-Nougat-Creme 20 - 450 g glasses',14,21,'Heli Swaren GmbH & Co. KG',NULL,NULL,3),(26,'Gumbr Gummibrchen','Gumbr Gummibrchen 100 - 250 g bags',31.23,21,'Heli Swaren GmbH & Co. KG',NULL,NULL,3),(27,'Schoggi Schokolade','Schoggi Schokolade 100 - 100 g pieces',43.9,21,'Heli Swaren GmbH & Co. KG',NULL,NULL,3),(28,'Maxilaku','Maxilaku 24 - 50 g pkgs.',20,21,'Karkki Oy',NULL,NULL,3),(29,'Valkoinen suklaa','Valkoinen suklaa 12 - 100 g bars',16.25,21,'Karkki Oy',NULL,NULL,3),(30,'Lakkalikri','Lakkalikri 500 ml',18,21,'Karkki Oy',NULL,NULL,1),(31,'Singaporean Hokkien Fried Mee','Singaporean Hokkien Fried Mee 32 - 1 kg pkgs.',14,21,'Leka Trading',NULL,NULL,5),(32,'Ipoh Coffee','Ipoh Coffee 16 - 500 g tins',46,21,'Leka Trading',NULL,NULL,1),(33,'Gula Malacca','Gula Malacca 20 - 2 kg bags',19.45,21,'Leka Trading',NULL,NULL,2),(34,'Rogede sild','Rogede sild 1k pkg.',9.5,21,'Lyngbysild',NULL,NULL,8),(35,'Spegesild','Spegesild 4 - 450 g glasses',12,21,'Lyngbysild',NULL,NULL,8),(36,'Tourtire','Tourtire 16 pies',7.45,21,'Ma Maison',NULL,NULL,6),(37,'Pt chinois','Pt chinois 24 boxes x 2 pies',24,21,'Ma Maison',NULL,NULL,6),(38,'Konbu','Konbu 2 kg box',6,21,'Mayumi\'s',NULL,NULL,8),(39,'Tofu','Tofu 40 - 100 g pkgs.',23.25,21,'Mayumi\'s',NULL,NULL,7),(40,'Genen Shouyu','Genen Shouyu 24 - 250 ml bottles',15.5,21,'Mayumi\'s',NULL,NULL,2),(41,'Boston Crab Meat','Boston Crab Meat 24 - 4 oz tins',18.4,21,'New England Seafood Cannery',NULL,NULL,8),(42,'Jack\'s New England Clam Chowder','Jack\'s New England Clam Chowder 12 - 12 oz cans',9.65,21,'New England Seafood Cannery',NULL,NULL,8),(43,'Chef Anton\'s Cajun Seasoning','Chef Anton\'s Cajun Seasoning 48 - 6 oz jars',22,21,'New Orleans Cajun Delights',NULL,NULL,2),(44,'Chef Anton\'s Gumbo Mix','Chef Anton\'s Gumbo Mix 36 boxes',21.35,21,'New Orleans Cajun Delights',NULL,NULL,2),(45,'Louisiana Fiery Hot Pepper Sauce','Louisiana Fiery Hot Pepper Sauce 32 - 8 oz bottles',21.05,21,'New Orleans Cajun Delights',NULL,NULL,2),(46,'Louisiana Hot Spiced Okra','Louisiana Hot Spiced Okra 24 - 8 oz jars',17,21,'New Orleans Cajun Delights',NULL,NULL,2),(47,'Nord-Ost Matjeshering','Nord-Ost Matjeshering 10 - 200 g glasses',25.89,21,'Nord-Ost-Fisch Handelsgesellschaft mbH',NULL,NULL,8),(48,'Geitost','Geitost 500 g',2.5,21,'Norske Meierier',NULL,NULL,4),(49,'Gudbrandsdalsost','Gudbrandsdalsost 10 kg pkg.',36,21,'Norske Meierier',NULL,NULL,4),(50,'Flotemysost','Flotemysost 10 - 500 g pkgs.',21.5,21,'Norske Meierier',NULL,NULL,4),(51,'Gnocchi di nonna Alice','Gnocchi di nonna Alice 24 - 250 g pkgs.',38,21,'Pasta Buttini s.r.l.',NULL,NULL,5),(52,'Ravioli Angelo','Ravioli Angelo 24 - 250 g pkgs.',19.5,21,'Pasta Buttini s.r.l.',NULL,NULL,5),(53,'Pavlova','Pavlova 32 - 500 g boxes',17.45,21,'Pavlova, Ltd.',NULL,NULL,3),(54,'Alice Mutton','Alice Mutton 20 - 1 kg tins',39,21,'Pavlova, Ltd.',NULL,NULL,6),(55,'Carnarvon Tigers','Carnarvon Tigers 16 kg pkg.',62.5,21,'Pavlova, Ltd.',NULL,NULL,8),(56,'Vegie-spread','Vegie-spread 15 - 625 g jars',43.9,21,'Pavlova, Ltd.',NULL,NULL,2),(57,'Outback Lager','Outback Lager 24 - 355 ml bottles',15,21,'Pavlova, Ltd.',NULL,NULL,1),(58,'Gustaf\'s Knckebrd','Gustaf\'s Knckebrd 24 - 500 g pkgs.',21,21,'PB Knckebrd AB',NULL,NULL,5),(59,'Tunnbrd','Tunnbrd 12 - 250 g pkgs.',9,21,'PB Knckebrd AB',NULL,NULL,5),(60,'Rssle Sauerkraut','Rssle Sauerkraut 25 - 825 g cans',45.6,21,'Plutzer Lebensmittelgromrkte AG',NULL,NULL,7),(61,'Thringer Rostbratwurst','Thringer Rostbratwurst 50 bags x 30 sausgs.',123.79,21,'Plutzer Lebensmittelgromrkte AG',NULL,NULL,6),(62,'Wimmers gute Semmelkndel','Wimmers gute Semmelkndel 20 bags x 4 pieces',33.25,21,'Plutzer Lebensmittelgromrkte AG',NULL,NULL,5),(63,'Rhnbru Klosterbier','Rhnbru Klosterbier 24 - 0.5 l bottles',7.75,21,'Plutzer Lebensmittelgromrkte AG',NULL,NULL,1),(64,'Original Frankfurter grne Soe','Original Frankfurter grne Soe 12 boxes',13,21,'Plutzer Lebensmittelgromrkte AG',NULL,NULL,2),(65,'Guaran Fantstica','Guaran Fantstica 12 - 355 ml cans',4.5,21,'Refrescos Americanas LTDA',NULL,NULL,1),(66,'Teatime Chocolate Biscuits','Teatime Chocolate Biscuits 10 boxes x 12 pieces',9.2,21,'Specialty Biscuits, Ltd.',NULL,NULL,3),(67,'Sir Rodney\'s Marmalade','Sir Rodney\'s Marmalade 30 gift boxes',81,21,'Specialty Biscuits, Ltd.',NULL,NULL,3),(68,'Sir Rodney\'s Scones','Sir Rodney\'s Scones 24 pkgs. x 4 pieces',10,21,'Specialty Biscuits, Ltd.',NULL,NULL,3),(69,'Scottish Longbreads','Scottish Longbreads 10 boxes x 8 pieces',12.5,21,'Specialty Biscuits, Ltd.',NULL,NULL,3),(70,'Inlagd Sill','Inlagd Sill 24 - 250 g  jars',19,21,'Svensk Sjfda AB',NULL,NULL,8),(71,'Gravad lax','Gravad lax 12 - 500 g pkgs.',26,21,'Svensk Sjfda AB',NULL,NULL,8),(72,'Rd Kaviar','Rd Kaviar 24 - 150 g jars',15,21,'Svensk Sjfda AB',NULL,NULL,8),(73,'Mishi Kobe Niku','Mishi Kobe Niku 18 - 500 g pkgs.',97,21,'Tokyo Traders',NULL,NULL,6),(74,'Ikura','Ikura 12 - 200 ml jars',31,21,'Tokyo Traders',NULL,NULL,8),(75,'Longlife Tofu','Longlife Tofu 5 kg pkg.',10,21,'Tokyo Traders',NULL,NULL,7),(76,'Zaanse koeken','Zaanse koeken 10 - 4 oz boxes',9.5,21,'Zaanse Snoepfabriek',NULL,NULL,3),(77,'Chocolade','Chocolade 10 pkgs.',12.75,21,'Zaanse Snoepfabriek',NULL,NULL,3);
/*!40000 ALTER TABLE `producte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provincia`
--

DROP TABLE IF EXISTS `provincia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `provincia` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `nom` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `provincia_nom_unique` (`nom`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provincia`
--

LOCK TABLES `provincia` WRITE;
/*!40000 ALTER TABLE `provincia` DISABLE KEYS */;
INSERT INTO `provincia` VALUES (1,'Andalucía'),(2,'Aragón'),(5,'Canarias'),(6,'Cantabria'),(8,'Castilla - La Mancha'),(7,'Castilla y León'),(9,'Cataluña'),(18,'Ceuta'),(13,'Comunidad de Madrid '),(15,'Comunidad Foral de Navarra'),(10,'Comunitat Valenciana'),(11,'Extremadura'),(12,'Galicia'),(4,'Illes Balears'),(17,'La Rioja'),(19,'Melilla'),(16,'País Vasco'),(3,'Principado de Asturias'),(14,'Región de Murcia');
/*!40000 ALTER TABLE `provincia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `targeta`
--

DROP TABLE IF EXISTS `targeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `targeta` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tipus` enum('Visa','Mastercard','Maestro') NOT NULL,
  `numero` bigint unsigned NOT NULL,
  `data_caducitat` date NOT NULL,
  `codi_seguretat` int DEFAULT NULL,
  `client_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `targeta_client_fk` (`client_id`),
  CONSTRAINT `targeta_client_fk` FOREIGN KEY (`client_id`) REFERENCES `client` (`numero_client`),
  CONSTRAINT `codi_seguretat_check` CHECK ((length(`codi_seguretat`) = 3))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `targeta`
--

LOCK TABLES `targeta` WRITE;
/*!40000 ALTER TABLE `targeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `targeta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-16 20:14:11
