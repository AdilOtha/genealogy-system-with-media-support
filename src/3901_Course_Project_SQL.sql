CREATE DATABASE  IF NOT EXISTS `3901_course_project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `3901_course_project`;
-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: 3901_course_project
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
  PRIMARY KEY (`event_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_types`
--

LOCK TABLES `event_types` WRITE;
/*!40000 ALTER TABLE `event_types` DISABLE KEYS */;
INSERT INTO `event_types` VALUES (1,'birth'),(2,'marriage'),(3,'divorce');
/*!40000 ALTER TABLE `event_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  `event_id` int NOT NULL AUTO_INCREMENT,
  `event_type_id` int DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `event_event_type_idx` (`event_type_id`),
  CONSTRAINT `event_event_type` FOREIGN KEY (`event_type_id`) REFERENCES `event_types` (`event_type_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` VALUES (1,1),(2,1),(3,1);
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_attribute_types`
--

DROP TABLE IF EXISTS `media_attribute_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_attribute_types` (
  `attribute_id` int NOT NULL AUTO_INCREMENT,
  `attribute_type` varchar(45) NOT NULL,
  PRIMARY KEY (`attribute_id`),
  UNIQUE KEY `attribute_type_UNIQUE` (`attribute_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_attribute_types`
--

LOCK TABLES `media_attribute_types` WRITE;
/*!40000 ALTER TABLE `media_attribute_types` DISABLE KEYS */;
/*!40000 ALTER TABLE `media_attribute_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_attributes`
--

DROP TABLE IF EXISTS `media_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_attributes` (
  `attribute_id` int NOT NULL,
  `attribute_value` varchar(45) NOT NULL,
  `media_id` int NOT NULL,
  PRIMARY KEY (`attribute_id`,`media_id`),
  KEY `media_media_id_idx` (`media_id`),
  CONSTRAINT `media_attribute_id` FOREIGN KEY (`attribute_id`) REFERENCES `media_attribute_types` (`attribute_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_media_id` FOREIGN KEY (`media_id`) REFERENCES `media_details` (`media_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_attributes`
--

LOCK TABLES `media_attributes` WRITE;
/*!40000 ALTER TABLE `media_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `media_attributes` ENABLE KEYS */;
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
  `date_created` date DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`media_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_details`
--

LOCK TABLES `media_details` WRITE;
/*!40000 ALTER TABLE `media_details` DISABLE KEYS */;
INSERT INTO `media_details` VALUES (1,'C:/Home/Family/1.png',NULL,NULL);
/*!40000 ALTER TABLE `media_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_tags`
--

DROP TABLE IF EXISTS `media_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_tags` (
  `tag_id` int NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(100) NOT NULL,
  `media_id` int DEFAULT NULL,
  PRIMARY KEY (`tag_id`),
  KEY `tag_media_id_idx` (`media_id`),
  CONSTRAINT `tag_media_id` FOREIGN KEY (`media_id`) REFERENCES `media_details` (`media_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_tags`
--

LOCK TABLES `media_tags` WRITE;
/*!40000 ALTER TABLE `media_tags` DISABLE KEYS */;
INSERT INTO `media_tags` VALUES (1,'Superheroes',1);
/*!40000 ALTER TABLE `media_tags` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_attributes`
--

LOCK TABLES `person_attributes` WRITE;
/*!40000 ALTER TABLE `person_attributes` DISABLE KEYS */;
INSERT INTO `person_attributes` VALUES (2,1,'1930-11-01'),(2,3,'Soldier'),(4,1,'1790-01-01'),(4,2,'2019-01-01'),(4,3,'God of Mischief'),(10,1,'1975-02-23'),(10,2,'2019-08-31'),(10,3,'Spy');
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_attributes_types`
--

LOCK TABLES `person_attributes_types` WRITE;
/*!40000 ALTER TABLE `person_attributes_types` DISABLE KEYS */;
INSERT INTO `person_attributes_types` VALUES (5,'birth_location'),(1,'date of birth'),(2,'date of death'),(6,'death_location'),(3,'occupation');
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
  `name` varchar(45) NOT NULL,
  `gender_id` int DEFAULT NULL,
  PRIMARY KEY (`person_id`),
  KEY `person_gender_id_idx` (`gender_id`),
  CONSTRAINT `person_gender_id` FOREIGN KEY (`gender_id`) REFERENCES `person_genders` (`gender_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_details`
--

LOCK TABLES `person_details` WRITE;
/*!40000 ALTER TABLE `person_details` DISABLE KEYS */;
INSERT INTO `person_details` VALUES (1,'Tony Stark',1),(2,'Steve Rogers',1),(4,'Loki',1),(10,'Natasha Romanoff',2),(12,'Odin',1),(13,'Evan Romanoff',1),(14,'Martha Rogers',2);
/*!40000 ALTER TABLE `person_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_event_relations`
--

DROP TABLE IF EXISTS `person_event_relations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_event_relations` (
  `person_id` int NOT NULL,
  `event_id` int NOT NULL,
  `relation_id` int NOT NULL,
  PRIMARY KEY (`person_id`,`event_id`,`relation_id`),
  KEY `relation_ref_idx` (`relation_id`),
  KEY `event_ref_idx` (`event_id`),
  CONSTRAINT `event_ref` FOREIGN KEY (`event_id`) REFERENCES `events` (`event_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `person_ref` FOREIGN KEY (`person_id`) REFERENCES `person_details` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relation_ref` FOREIGN KEY (`relation_id`) REFERENCES `relation_types` (`relation_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_event_relations`
--

LOCK TABLES `person_event_relations` WRITE;
/*!40000 ALTER TABLE `person_event_relations` DISABLE KEYS */;
INSERT INTO `person_event_relations` VALUES (12,2,1),(13,1,1),(14,3,2),(2,3,3),(4,2,3),(10,1,3);
/*!40000 ALTER TABLE `person_event_relations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_genders`
--

DROP TABLE IF EXISTS `person_genders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_genders` (
  `gender_id` int NOT NULL AUTO_INCREMENT,
  `gender` varchar(45) NOT NULL,
  PRIMARY KEY (`gender_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_genders`
--

LOCK TABLES `person_genders` WRITE;
/*!40000 ALTER TABLE `person_genders` DISABLE KEYS */;
INSERT INTO `person_genders` VALUES (1,'male'),(2,'female');
/*!40000 ALTER TABLE `person_genders` ENABLE KEYS */;
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
  PRIMARY KEY (`person_id`,`media_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_media`
--

LOCK TABLES `person_media` WRITE;
/*!40000 ALTER TABLE `person_media` DISABLE KEYS */;
INSERT INTO `person_media` VALUES (1,1),(2,1),(4,1),(10,1);
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
  `note` varchar(45) DEFAULT NULL,
  `person_id` int DEFAULT NULL,
  PRIMARY KEY (`note_id`),
  KEY `note_person_id_idx` (`person_id`),
  CONSTRAINT `note_person_id` FOREIGN KEY (`person_id`) REFERENCES `person_details` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_notes`
--

LOCK TABLES `person_notes` WRITE;
/*!40000 ALTER TABLE `person_notes` DISABLE KEYS */;
INSERT INTO `person_notes` VALUES (1,'Part of Avengers group of heroes',10);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_references`
--

LOCK TABLES `person_references` WRITE;
/*!40000 ALTER TABLE `person_references` DISABLE KEYS */;
INSERT INTO `person_references` VALUES (1,'Appeared in Avengers',10);
/*!40000 ALTER TABLE `person_references` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relation_types`
--

DROP TABLE IF EXISTS `relation_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `relation_types` (
  `relation_id` int NOT NULL AUTO_INCREMENT,
  `relation_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`relation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relation_types`
--

LOCK TABLES `relation_types` WRITE;
/*!40000 ALTER TABLE `relation_types` DISABLE KEYS */;
INSERT INTO `relation_types` VALUES (1,'father'),(2,'mother'),(3,'child'),(4,'husband'),(5,'wife'),(6,'parent');
/*!40000 ALTER TABLE `relation_types` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-30 15:41:50
