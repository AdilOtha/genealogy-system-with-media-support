-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: 3901_course_project_b00900955
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `event_types`
--

DROP TABLE IF EXISTS `event_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_types` (
  `event_type_id` int NOT NULL AUTO_INCREMENT,
  `event_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`event_type_id`),
  UNIQUE KEY `event_name_UNIQUE` (`event_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_types`
--

LOCK TABLES `event_types` WRITE;
/*!40000 ALTER TABLE `event_types` DISABLE KEYS */;
INSERT INTO `event_types` VALUES (2,'divorce'),(1,'marriage');
/*!40000 ALTER TABLE `event_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_attributes`
--

DROP TABLE IF EXISTS `media_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_attributes` (
  `media_id` int NOT NULL,
  `attribute_id` int NOT NULL,
  `attribute_value` varchar(200) NOT NULL,
  PRIMARY KEY (`media_id`,`attribute_id`),
  KEY `attribute_type_ref_idx` (`attribute_id`),
  CONSTRAINT `attribute_type_ref` FOREIGN KEY (`attribute_id`) REFERENCES `media_attributes_types` (`attribute_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_attribute_ref` FOREIGN KEY (`media_id`) REFERENCES `media_details` (`media_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_attributes`
--

LOCK TABLES `media_attributes` WRITE;
/*!40000 ALTER TABLE `media_attributes` DISABLE KEYS */;
INSERT INTO `media_attributes` VALUES (1,2,'1975-02'),(1,3,'Halifax'),(1,4,'Dalhousie University'),(2,4,'Dalhousie University'),(3,2,'2000'),(3,4,'South Park St'),(4,2,'2000'),(4,4,'South Park St'),(7,2,'2021-07'),(7,3,'Mumbai'),(7,4,'Juhu Beach'),(8,4,'South Park St');
/*!40000 ALTER TABLE `media_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_attributes_types`
--

DROP TABLE IF EXISTS `media_attributes_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_attributes_types` (
  `attribute_id` int NOT NULL AUTO_INCREMENT,
  `attribute_type` varchar(45) NOT NULL,
  PRIMARY KEY (`attribute_id`),
  UNIQUE KEY `attribute_type_UNIQUE` (`attribute_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_attributes_types`
--

LOCK TABLES `media_attributes_types` WRITE;
/*!40000 ALTER TABLE `media_attributes_types` DISABLE KEYS */;
INSERT INTO `media_attributes_types` VALUES (3,'city'),(2,'date'),(4,'location'),(1,'year');
/*!40000 ALTER TABLE `media_attributes_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_details`
--

DROP TABLE IF EXISTS `media_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_details` (
  `media_id` int NOT NULL AUTO_INCREMENT,
  `file_location` varchar(200) NOT NULL,
  PRIMARY KEY (`media_id`),
  UNIQUE KEY `file_location_UNIQUE` (`file_location`)
) ENGINE=InnoDB AUTO_INCREMENT=1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_details`
--

LOCK TABLES `media_details` WRITE;
/*!40000 ALTER TABLE `media_details` DISABLE KEYS */;
INSERT INTO `media_details` VALUES (1,'C:/Home/Family/1.png'),(4,'C:/Me/Friends/group.jpg'),(9,'D:/media/image.png'),(2,'D:/media/travel/halifax.jpg'),(7,'E:/photos/family.png'),(3,'E:/photos/home.jpg'),(8,'F:/photos/college.png');
/*!40000 ALTER TABLE `media_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_tags`
--

DROP TABLE IF EXISTS `media_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_tags` (
  `tag_id` int NOT NULL,
  `media_id` int NOT NULL,
  PRIMARY KEY (`tag_id`,`media_id`),
  KEY `media_ref_idx` (`media_id`),
  CONSTRAINT `media_ref` FOREIGN KEY (`media_id`) REFERENCES `media_details` (`media_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tag_ref` FOREIGN KEY (`tag_id`) REFERENCES `media_tags_types` (`tag_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_tags`
--

LOCK TABLES `media_tags` WRITE;
/*!40000 ALTER TABLE `media_tags` DISABLE KEYS */;
INSERT INTO `media_tags` VALUES (1,1),(4,1),(5,1),(3,2),(4,2),(2,3),(4,3),(2,4),(4,4),(3,7),(5,7);
/*!40000 ALTER TABLE `media_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_tags_types`
--

DROP TABLE IF EXISTS `media_tags_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_tags_types` (
  `tag_id` int NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(100) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_name_UNIQUE` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_tags_types`
--

LOCK TABLES `media_tags_types` WRITE;
/*!40000 ALTER TABLE `media_tags_types` DISABLE KEYS */;
INSERT INTO `media_tags_types` VALUES (4,'Education'),(5,'Food'),(2,'General'),(1,'Superheroes'),(3,'Travel');
/*!40000 ALTER TABLE `media_tags_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parent_child`
--

DROP TABLE IF EXISTS `parent_child`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parent_child` (
  `parent_id` int NOT NULL,
  `child_id` int NOT NULL,
  PRIMARY KEY (`parent_id`,`child_id`),
  KEY `child_ref_idx` (`child_id`),
  CONSTRAINT `child_ref` FOREIGN KEY (`child_id`) REFERENCES `person_details` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `parent_ref` FOREIGN KEY (`parent_id`) REFERENCES `person_details` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parent_child`
--

LOCK TABLES `parent_child` WRITE;
/*!40000 ALTER TABLE `parent_child` DISABLE KEYS */;
INSERT INTO `parent_child` VALUES (42,1),(43,1),(15,17),(16,17),(17,19),(18,19),(19,21),(20,21),(21,23),(22,23),(15,24),(15,25),(15,26),(27,29),(28,29),(29,31),(30,31),(29,32),(30,32),(29,33),(30,33),(31,36),(34,36),(33,37),(35,37);
/*!40000 ALTER TABLE `parent_child` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_attributes`
--

DROP TABLE IF EXISTS `person_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_attributes` (
  `person_id` int NOT NULL,
  `attribute_id` int NOT NULL,
  `attribute_value` varchar(200) NOT NULL,
  PRIMARY KEY (`person_id`,`attribute_id`),
  KEY `attribute_attribute_id_idx` (`attribute_id`),
  CONSTRAINT `attribute_attribute_id` FOREIGN KEY (`attribute_id`) REFERENCES `person_attributes_types` (`attribute_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `person_person_id` FOREIGN KEY (`person_id`) REFERENCES `person_details` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_attributes`
--

LOCK TABLES `person_attributes` WRITE;
/*!40000 ALTER TABLE `person_attributes` DISABLE KEYS */;
INSERT INTO `person_attributes` VALUES (2,1,'1930-11-01'),(2,3,'Soldier'),(4,1,'1790-01-01'),(4,2,'2019-01-01'),(4,3,'God of Mischief'),(10,1,'1975-02-23'),(10,2,'2019-08-31'),(10,3,'Spy'),(14,1,'1860-01-10'),(14,2,'1910-02-12'),(14,3,'Accountant'),(14,5,'Baltimore, USA'),(15,1,'1975-02-23'),(15,2,'2019-08-31'),(15,3,'Software Developer'),(15,10,'male'),(42,1,'1920-05-10'),(42,2,'1980-08-31'),(42,3,'Mechanical Engineer'),(42,10,'male');
/*!40000 ALTER TABLE `person_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_attributes_types`
--

DROP TABLE IF EXISTS `person_attributes_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_attributes_types` (
  `attribute_id` int NOT NULL AUTO_INCREMENT,
  `attribute_type` varchar(45) NOT NULL,
  PRIMARY KEY (`attribute_id`),
  UNIQUE KEY `attribute_type_UNIQUE` (`attribute_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_attributes_types`
--

LOCK TABLES `person_attributes_types` WRITE;
/*!40000 ALTER TABLE `person_attributes_types` DISABLE KEYS */;
INSERT INTO `person_attributes_types` VALUES (5,'birth location'),(1,'date of birth'),(2,'date of death'),(6,'death location'),(10,'gender'),(3,'occupation');
/*!40000 ALTER TABLE `person_attributes_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_details`
--

DROP TABLE IF EXISTS `person_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_details` (
  `person_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_details`
--

LOCK TABLES `person_details` WRITE;
/*!40000 ALTER TABLE `person_details` DISABLE KEYS */;
INSERT INTO `person_details` VALUES (1,'Tony Stark'),(2,'Steve Rogers'),(4,'Loki'),(10,'Natasha Romanoff'),(12,'Odin'),(13,'Evan Romanoff'),(14,'Martha Rogers'),(15,'A'),(16,'B'),(17,'C'),(18,'D'),(19,'E'),(20,'F'),(21,'G'),(22,'H'),(23,'I'),(24,'X'),(25,'Y'),(26,'Z'),(27,'0'),(28,'Node1'),(29,'Node2'),(30,'Node3'),(31,'Node4'),(32,'Node5'),(33,'Node6'),(34,'Node7'),(35,'Node8'),(36,'Node9'),(37,'Node10'),(38,'Adrian'),(39,'Chris'),(42,'Howard Stark'),(43,'Maria Stark'),(44,'John Doe'),(45,'Jane Doe');
/*!40000 ALTER TABLE `person_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_events`
--

DROP TABLE IF EXISTS `person_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_events` (
  `event_id` int NOT NULL AUTO_INCREMENT,
  `person_id_1` int NOT NULL,
  `person_id_2` int NOT NULL,
  `event_type_id` int NOT NULL,
  PRIMARY KEY (`event_id`),
  KEY `event_type_ref_idx` (`event_type_id`),
  KEY `person_1_ref_idx` (`person_id_1`),
  KEY `person_2_ref_idx` (`person_id_2`),
  CONSTRAINT `event_type_ref` FOREIGN KEY (`event_type_id`) REFERENCES `event_types` (`event_type_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `person_1_ref` FOREIGN KEY (`person_id_1`) REFERENCES `person_details` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `person_2_ref` FOREIGN KEY (`person_id_2`) REFERENCES `person_details` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_events`
--

LOCK TABLES `person_events` WRITE;
/*!40000 ALTER TABLE `person_events` DISABLE KEYS */;
INSERT INTO `person_events` VALUES (1,15,16,1),(5,18,17,1),(6,18,17,2),(10,19,20,1),(13,19,20,2),(14,19,20,1),(15,21,22,2),(16,21,22,1),(17,21,22,2);
/*!40000 ALTER TABLE `person_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_media`
--

DROP TABLE IF EXISTS `person_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_media` (
  `person_id` int NOT NULL,
  `media_id` int NOT NULL,
  PRIMARY KEY (`person_id`,`media_id`),
  KEY `media_details_ref_idx` (`media_id`),
  CONSTRAINT `media_details_ref` FOREIGN KEY (`media_id`) REFERENCES `media_details` (`media_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `person_details_ref` FOREIGN KEY (`person_id`) REFERENCES `person_details` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_media`
--

LOCK TABLES `person_media` WRITE;
/*!40000 ALTER TABLE `person_media` DISABLE KEYS */;
INSERT INTO `person_media` VALUES (1,1),(2,1),(4,1),(10,1),(24,1),(25,2),(17,3),(26,4),(15,7),(17,7),(24,7),(25,7);
/*!40000 ALTER TABLE `person_media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_notes`
--

DROP TABLE IF EXISTS `person_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_notes` (
  `note_id` int NOT NULL AUTO_INCREMENT,
  `note` varchar(200) DEFAULT NULL,
  `person_id` int DEFAULT NULL,
  PRIMARY KEY (`note_id`),
  KEY `note_person_id_idx` (`person_id`),
  CONSTRAINT `note_person_id` FOREIGN KEY (`person_id`) REFERENCES `person_details` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_notes`
--

LOCK TABLES `person_notes` WRITE;
/*!40000 ALTER TABLE `person_notes` DISABLE KEYS */;
INSERT INTO `person_notes` VALUES (1,'Part of Avengers group of heroes',10),(2,'Good handwriting',15),(3,'Has multiple degrees',15),(4,'Skilled in Martial Arts',10),(5,'Cooking expert',39),(6,'Can play football',39);
/*!40000 ALTER TABLE `person_notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_references`
--

DROP TABLE IF EXISTS `person_references`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_references` (
  `reference_id` int NOT NULL AUTO_INCREMENT,
  `reference` varchar(200) NOT NULL,
  `person_id` int DEFAULT NULL,
  PRIMARY KEY (`reference_id`),
  KEY `reference_person_id_idx` (`person_id`),
  CONSTRAINT `reference_person_id` FOREIGN KEY (`person_id`) REFERENCES `person_details` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_references`
--

LOCK TABLES `person_references` WRITE;
/*!40000 ALTER TABLE `person_references` DISABLE KEYS */;
INSERT INTO `person_references` VALUES (1,'Nick Fury',10),(2,'Wanda Maximoff',10),(3,'Kyle',15),(4,'Sarah',15),(6,'John',39),(7,'Dave',39);
/*!40000 ALTER TABLE `person_references` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-14 10:55:38
