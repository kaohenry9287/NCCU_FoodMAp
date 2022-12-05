-- MySQL dump 10.13  Distrib 8.0.28, for Linux (x86_64)
--
-- Host: localhost    Database: NCCUDelicacyDatabase
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `Favorite`
--

DROP TABLE IF EXISTS `Favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Favorite` (
  `favor_id` varchar(100) NOT NULL,
  `googleLocationId` varchar(100) DEFAULT NULL,
  `restaurantName` varchar(100) DEFAULT NULL,
  `rate` float DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`favor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Favorite`
--

LOCK TABLES `Favorite` WRITE;
/*!40000 ALTER TABLE `Favorite` DISABLE KEYS */;
INSERT INTO `Favorite` VALUES ('ChIJ0ZqmDHyqQjQR7DTqk1Ru5g4','ChIJ0ZqmDHyqQjQR7DTqk1Ru5g4','焿大王/政大店',3.7,'其他'),('ChIJ3ZnMlHuqQjQRMj4lIpQR6xE','ChIJ3ZnMlHuqQjQRMj4lIpQR6xE','Juicy Bun Burger 就是棒 美式餐廳 政大店',4.4,'American'),('ChIJL-BJs3aqQjQRT5fEKex6ruo','ChIJL-BJs3aqQjQRT5fEKex6ruo','麥當勞 台北指南店',3,'其他'),('ChIJQ1jvPnyqQjQRbwfSpTCf8GQ','ChIJQ1jvPnyqQjQRbwfSpTCf8GQ','金鮨日式料理',3.9,'Japanese'),('ChIJuWyOjnuqQjQR3UU6aSUKDSI','ChIJuWyOjnuqQjQR3UU6aSUKDSI','悅來牛肉麵',4.4,'其他'),('ChIJvSzfT-6rQjQRP76qvwCmvtc','ChIJvSzfT-6rQjQRP76qvwCmvtc','石鍋軒',3.1,'Korean');
/*!40000 ALTER TABLE `Favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FoodType`
--

DROP TABLE IF EXISTS `FoodType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FoodType` (
  `ft_id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(100) DEFAULT NULL,
  `googleLocationId` varchar(100) DEFAULT NULL,
  `restaurant_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ft_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FoodType`
--

LOCK TABLES `FoodType` WRITE;
/*!40000 ALTER TABLE `FoodType` DISABLE KEYS */;
INSERT INTO `FoodType` VALUES (2,'Korean','ChIJK02BU3qqQjQRo9z66N5frgw','韓大佬韓式精緻料理'),(3,'Korean','ChIJ3fFp9XuqQjQR8f4hwYth-fM','高句麗'),(4,'Korean','ChIJlVNNNHqqQjQRXL9yiUysvlo','阿里郎韓國小吃'),(5,'Korean','ChIJvSzfT-6rQjQRP76qvwCmvtc','石鍋軒'),(6,'Japanese','ChIJQ1jvPnyqQjQRbwfSpTCf8GQ','金鮨日式料理'),(7,'Japanese','ChIJSdF_b3eqQjQRO9DIKJf0dEM','松町和風小舖'),(8,'Japanese','ChIJD_xDKXGqQjQR6oWWwDwWB1w','京橋日式料理'),(9,'Japanese','ChIJ10ahyQurQjQR6MJuVmN4Lk0','匠記日式食堂'),(10,'Japanese','ChIJxa7pVHeqQjQRybHL1XO_1Zk','三好屋和風料理'),(11,'Japanese','ChIJWVPNrH2qQjQRnJ4MvFZb06I','湯饌'),(12,'Japanese','ChIJ14cFqO-rQjQRKqdpWn_gE7k','我和一心日本咖哩麵包'),(13,'Japanese','ChIJIwbkaWoBaDQRqLNjju8Ct-s','辛百田食事處'),(14,'Japanese','ChIJoV_MT3qqQjQRCeOWjO1JBZM','樂山食堂'),(15,'Japanese','ChIJj8coxnuqQjQRomIFmFbbuI4','日式威廉髮藝 政大店'),(16,'American','ChIJ3ZnMlHuqQjQRMj4lIpQR6xE','Juicy Bun Burger 就是棒 美式餐廳 政大店'),(17,'American','ChIJFeKl3XqqQjQReqBHOIw7rpU','美式早餐屋'),(18,'Thai','ChIJE5a2MnGqQjQRuKZYhzikdV0','泰國小普吉'),(19,'Thai','ChIJfbS2N3qqQjQRAD63fHjZoMQ','小曼谷泰國雲南料理 政大店'),(20,'Thai','ChIJBa8zTysBaDQRWVVSdEfvdBo','泰越小吃'),(21,'Thai','ChIJ7dsOfT2rQjQRpuUvHSwHkmo','華新泰式舒壓養生會館(木柵店)/台北指壓/台北泰式按摩/台北按摩推薦/文山區按摩/台北全身去角質/台北油壓'),(22,'Thai','ChIJ3RnLS3eqQjQRS9NpVqWmpfo','泰味娘'),(23,'Taiwanese','ChIJk_SJzHuqQjQRmNJsIWa_OrE','永康街左撇子'),(24,'Taiwanese','ChIJZwFvtXuqQjQR6xVW-uRxpD8','四五大街'),(25,'Taiwanese','ChIJv8xxsparQjQRYWFktvZlDhw','梁社漢排骨 文山指南店'),(26,'Chinese','ChIJx4k0RSCrQjQRAP-eCTBhjzk','甄好味食堂'),(27,'Chinese','ChIJzT0JyXuqQjQRxBU2ET5FbEc','敏忠餐廳'),(28,'Chinese','ChIJ17XbO3yqQjQRTlGDPgIZQXg','江記水盆羊肉'),(29,'Chinese','ChIJ97N8KHGqQjQRwyZ8CRnNiuQ','潮飯'),(30,'Chinese','ChIJgakfwnuqQjQRMUR7ju2CKH8','美香味快餐'),(31,'Chinese','ChIJi1EVVnqqQjQR6SVGj_z2fZM','巴東蜀味'),(32,'Chinese','ChIJJTX3yHuqQjQRtjdWiyT9BE4','四川飯館'),(33,'Chinese','ChIJ5zjUqXWrQjQRvHUT5zwnbCk','喜記港式燒臘'),(34,'Chinese','ChIJc9di6k8BaDQR5d_DLafcPYo','恆光街炒飯炒麵'),(35,'Chinese','ChIJrZG478irQjQRXWDri99RTz0','MJ餛飩超人'),(36,'Chinese','ChIJQb1e2W2rQjQRxuHgy3EWaHc','阿貴貴早餐/午餐簡餐'),(37,'Chinese','ChIJN53pHneqQjQR5tGVIXHTjp4','海天小館');
/*!40000 ALTER TABLE `FoodType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `userName` varchar(100) DEFAULT NULL,
  `pic` varchar(100) DEFAULT NULL,
  `account` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'testName','/Users/maoxunhuang/eclipse-workspace/FinalProject/src/images/user-images/user.png','abc','123'),(5,'Final Test','/Users/maoxunhuang/eclipse-workspace/FinalProject/bin//images/user-images/LOGO.png','a22908352@gmail.com','a12345678'),(6,'政大吃貨','/Users/maoxunhuang/eclipse-workspace/FinalProject/bin//images/user-images/LOGO.png','110306019@g.nccu.edu.tw','a12345678');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_Favorite`
--

DROP TABLE IF EXISTS `User_Favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User_Favorite` (
  `uf_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `favor_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uf_id`),
  KEY `User_Favorite_FK` (`user_id`),
  KEY `User_Favorite_FK_1` (`favor_id`),
  CONSTRAINT `User_Favorite_FK` FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`),
  CONSTRAINT `User_Favorite_FK_1` FOREIGN KEY (`favor_id`) REFERENCES `Favorite` (`favor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_Favorite`
--

LOCK TABLES `User_Favorite` WRITE;
/*!40000 ALTER TABLE `User_Favorite` DISABLE KEYS */;
INSERT INTO `User_Favorite` VALUES (7,1,'ChIJ3ZnMlHuqQjQRMj4lIpQR6xE'),(12,1,'ChIJuWyOjnuqQjQR3UU6aSUKDSI'),(15,5,'ChIJQ1jvPnyqQjQRbwfSpTCf8GQ'),(16,5,'ChIJ3ZnMlHuqQjQRMj4lIpQR6xE'),(18,6,'ChIJQ1jvPnyqQjQRbwfSpTCf8GQ'),(20,6,'ChIJvSzfT-6rQjQRP76qvwCmvtc');
/*!40000 ALTER TABLE `User_Favorite` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-04  2:23:05
