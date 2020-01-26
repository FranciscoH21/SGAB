-- MySQL dump 10.16  Distrib 10.1.38-MariaDB, for Win32 (AMD64)
--
-- Host: localhost    Database: biblioteca
-- ------------------------------------------------------
-- Server version	10.1.38-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `actividades`
--

DROP TABLE IF EXISTS `actividades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actividades` (
  `IdActividad` varchar(5) NOT NULL,
  `NombreA` varchar(40) DEFAULT NULL,
  `CostoTotalA` int(11) DEFAULT NULL,
  `CapacidadUsuarios` int(11) DEFAULT NULL,
  `FechaInicioA` varchar(10) DEFAULT NULL,
  `FechaFinA` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`IdActividad`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividades`
--

LOCK TABLES `actividades` WRITE;
/*!40000 ALTER TABLE `actividades` DISABLE KEYS */;
INSERT INTO `actividades` VALUES ('AT100','Ingles VI',100,35,'15/05/2019','29/06/2019');
/*!40000 ALTER TABLE `actividades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clases`
--

DROP TABLE IF EXISTS `clases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clases` (
  `IdActividad` varchar(5) NOT NULL,
  `DiaC` varchar(10) NOT NULL,
  `HoraC` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`DiaC`,`IdActividad`),
  KEY `IdActividad` (`IdActividad`),
  CONSTRAINT `clases_ibfk_1` FOREIGN KEY (`IdActividad`) REFERENCES `actividades` (`IdActividad`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clases`
--

LOCK TABLES `clases` WRITE;
/*!40000 ALTER TABLE `clases` DISABLE KEYS */;
INSERT INTO `clases` VALUES ('AT100','Lunes','09:00-11:00'),('AT100','Miercoles','09:00-11:00'),('AT100','Viernes','09:00-11:00');
/*!40000 ALTER TABLE `clases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_prestamos`
--

DROP TABLE IF EXISTS `detalle_prestamos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detalle_prestamos` (
  `IdPrestamo` varchar(5) NOT NULL,
  `IdUsuario` varchar(5) NOT NULL,
  `FechaInicioP` varchar(10) DEFAULT NULL,
  `FechaFinP` varchar(10) DEFAULT NULL,
  `Cantidad` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdPrestamo`,`IdUsuario`),
  KEY `IdUsuario` (`IdUsuario`),
  CONSTRAINT `detalle_prestamos_ibfk_1` FOREIGN KEY (`IdUsuario`) REFERENCES `usuarios` (`IdUsuario`),
  CONSTRAINT `detalle_prestamos_ibfk_2` FOREIGN KEY (`IdPrestamo`) REFERENCES `prestamos` (`IdPrestamo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_prestamos`
--

LOCK TABLES `detalle_prestamos` WRITE;
/*!40000 ALTER TABLE `detalle_prestamos` DISABLE KEYS */;
INSERT INTO `detalle_prestamos` VALUES ('PS100','HF100','15/05/2019','19/05/2019',2);
/*!40000 ALTER TABLE `detalle_prestamos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_registros`
--

DROP TABLE IF EXISTS `detalle_registros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detalle_registros` (
  `IdRegistro` varchar(5) NOT NULL,
  `Abono` int(11) DEFAULT NULL,
  `FechaAbonoR` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`IdRegistro`),
  CONSTRAINT `detalle_registros_ibfk_1` FOREIGN KEY (`IdRegistro`) REFERENCES `registros` (`IdRegistro`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_registros`
--

LOCK TABLES `detalle_registros` WRITE;
/*!40000 ALTER TABLE `detalle_registros` DISABLE KEYS */;
INSERT INTO `detalle_registros` VALUES ('RF100',10,'25/05/2019');
/*!40000 ALTER TABLE `detalle_registros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `horario`
--

DROP TABLE IF EXISTS `horario`;
/*!50001 DROP VIEW IF EXISTS `horario`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `horario` (
  `IdActividad` tinyint NOT NULL,
  `NombreA` tinyint NOT NULL,
  `DiaC` tinyint NOT NULL,
  `HoraC` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `libros`
--

DROP TABLE IF EXISTS `libros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `libros` (
  `ISBN` varchar(13) NOT NULL,
  `Titulo` varchar(50) DEFAULT NULL,
  `Autor` varchar(50) DEFAULT NULL,
  `Editorial` varchar(30) DEFAULT NULL,
  `Edicion` int(11) DEFAULT NULL,
  `Categoria` varchar(35) DEFAULT NULL,
  `Anio` int(11) DEFAULT NULL,
  `Existencia` int(11) DEFAULT NULL,
  PRIMARY KEY (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libros`
--

LOCK TABLES `libros` WRITE;
/*!40000 ALTER TABLE `libros` DISABLE KEYS */;
INSERT INTO `libros` VALUES ('9781473695986','Brief Answers To The Big Questions','Stephen Hawking','John Murray',1,'Tecnología (Ciencias Aplicadas)',2018,1),('9789871386147','Ciencia de la autorealización','Swami','Book Trust',1,'Literatura y Retórica',2015,1),('9789871609062','Arquitectura De Computadoras','Patricia Quiroga','Alfaomega',1,'Tecnología (Ciencias Aplicadas)',2010,1),('9789873832116','Java A Fondo','Pablo Augusto','Alfaomega',4,'Filosofía y Psicología',1995,1);
/*!40000 ALTER TABLE `libros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materiales`
--

DROP TABLE IF EXISTS `materiales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `materiales` (
  `IdMaterial` varchar(5) NOT NULL,
  `NombreM` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`IdMaterial`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materiales`
--

LOCK TABLES `materiales` WRITE;
/*!40000 ALTER TABLE `materiales` DISABLE KEYS */;
INSERT INTO `materiales` VALUES ('1','Libro'),('2','Computadora'),('3','Publicaciones Periodicas'),('4','Videos'),('5','Otros');
/*!40000 ALTER TABLE `materiales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `multas`
--

DROP TABLE IF EXISTS `multas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multas` (
  `IdMulta` varchar(5) NOT NULL,
  `Motivo` varchar(30) DEFAULT NULL,
  `CostoM` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdMulta`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `multas`
--

LOCK TABLES `multas` WRITE;
/*!40000 ALTER TABLE `multas` DISABLE KEYS */;
INSERT INTO `multas` VALUES ('100','Daño',15),('101','Extravio',0),('102','Retardo',10);
/*!40000 ALTER TABLE `multas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `multasp`
--

DROP TABLE IF EXISTS `multasp`;
/*!50001 DROP VIEW IF EXISTS `multasp`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `multasp` (
  `IdPago` tinyint NOT NULL,
  `IdUsuario` tinyint NOT NULL,
  `NombreU` tinyint NOT NULL,
  `Motivo` tinyint NOT NULL,
  `CostoM` tinyint NOT NULL,
  `FechaMulta` tinyint NOT NULL,
  `FechaPago` tinyint NOT NULL,
  `Estado` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `pagos`
--

DROP TABLE IF EXISTS `pagos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pagos` (
  `IdPago` varchar(5) NOT NULL,
  `IdUsuario` varchar(5) NOT NULL,
  `IdMulta` varchar(5) NOT NULL,
  `FechaMulta` varchar(10) DEFAULT NULL,
  `FechaPago` varchar(10) DEFAULT NULL,
  `Estado` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`IdPago`,`IdUsuario`),
  KEY `IdUsuario` (`IdUsuario`),
  KEY `IdMulta` (`IdMulta`),
  CONSTRAINT `pagos_ibfk_1` FOREIGN KEY (`IdUsuario`) REFERENCES `usuarios` (`IdUsuario`),
  CONSTRAINT `pagos_ibfk_2` FOREIGN KEY (`IdMulta`) REFERENCES `multas` (`IdMulta`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagos`
--

LOCK TABLES `pagos` WRITE;
/*!40000 ALTER TABLE `pagos` DISABLE KEYS */;
INSERT INTO `pagos` VALUES ('MF100','HF100','102','15/05/2019','16/05/2019','Pagada');
/*!40000 ALTER TABLE `pagos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prestamos`
--

DROP TABLE IF EXISTS `prestamos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prestamos` (
  `IdPrestamo` varchar(5) NOT NULL,
  `ISBN` varchar(13) NOT NULL,
  `FechaDevolucionP` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`IdPrestamo`,`ISBN`),
  KEY `ISBN` (`ISBN`),
  CONSTRAINT `prestamos_ibfk_1` FOREIGN KEY (`ISBN`) REFERENCES `libros` (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamos`
--

LOCK TABLES `prestamos` WRITE;
/*!40000 ALTER TABLE `prestamos` DISABLE KEYS */;
INSERT INTO `prestamos` VALUES ('PS100','9781473695986','16/05/2019'),('PS100','9789873832116','');
/*!40000 ALTER TABLE `prestamos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registros`
--

DROP TABLE IF EXISTS `registros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `registros` (
  `IdRegistro` varchar(5) NOT NULL,
  `IdActividad` varchar(5) NOT NULL,
  `IdUsuario` varchar(5) NOT NULL,
  `FechaR` varchar(10) DEFAULT NULL,
  `SumAbono` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdRegistro`),
  KEY `IdUsuario` (`IdUsuario`),
  KEY `IdActividad` (`IdActividad`),
  CONSTRAINT `registros_ibfk_1` FOREIGN KEY (`IdUsuario`) REFERENCES `usuarios` (`IdUsuario`),
  CONSTRAINT `registros_ibfk_2` FOREIGN KEY (`IdActividad`) REFERENCES `actividades` (`IdActividad`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registros`
--

LOCK TABLES `registros` WRITE;
/*!40000 ALTER TABLE `registros` DISABLE KEYS */;
INSERT INTO `registros` VALUES ('RF100','AT100','HF100','15/05/2019',10);
/*!40000 ALTER TABLE `registros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `reportes`
--

DROP TABLE IF EXISTS `reportes`;
/*!50001 DROP VIEW IF EXISTS `reportes`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `reportes` (
  `Escolaridad` tinyint NOT NULL,
  `Sexo` tinyint NOT NULL,
  `Discapacidad` tinyint NOT NULL,
  `FechaV` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `IdUsuario` varchar(5) NOT NULL,
  `NombreU` varchar(50) DEFAULT NULL,
  `Edad` int(11) DEFAULT NULL,
  `Sexo` char(1) DEFAULT NULL,
  `Escolaridad` varchar(30) DEFAULT NULL,
  `Discapacidad` varchar(40) DEFAULT NULL,
  `Telefono` varchar(10) DEFAULT NULL,
  `CP` varchar(5) DEFAULT NULL,
  `Colonia` varchar(30) DEFAULT NULL,
  `Calle` varchar(40) DEFAULT NULL,
  `Numero` int(11) DEFAULT NULL,
  PRIMARY KEY (`IdUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES ('HF100','Francisco Hernández',21,'M','Licenciatura','Ninguna','3411369418','49600','Centro','Rayón',66);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitas`
--

DROP TABLE IF EXISTS `visitas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `visitas` (
  `IdVisita` int(11) NOT NULL AUTO_INCREMENT,
  `IdUsuario` varchar(5) NOT NULL,
  `IdMaterial` varchar(5) NOT NULL,
  `FechaV` varchar(10) DEFAULT NULL,
  `HoraV` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`IdVisita`),
  KEY `IdUsuario` (`IdUsuario`),
  KEY `IdMaterial` (`IdMaterial`),
  CONSTRAINT `visitas_ibfk_1` FOREIGN KEY (`IdUsuario`) REFERENCES `usuarios` (`IdUsuario`),
  CONSTRAINT `visitas_ibfk_2` FOREIGN KEY (`IdMaterial`) REFERENCES `materiales` (`IdMaterial`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitas`
--

LOCK TABLES `visitas` WRITE;
/*!40000 ALTER TABLE `visitas` DISABLE KEYS */;
INSERT INTO `visitas` VALUES (33,'HF100','1','15/05/2019','13:13:44');
/*!40000 ALTER TABLE `visitas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `horario`
--

/*!50001 DROP TABLE IF EXISTS `horario`*/;
/*!50001 DROP VIEW IF EXISTS `horario`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `horario` AS select `c`.`IdActividad` AS `IdActividad`,`a`.`NombreA` AS `NombreA`,`c`.`DiaC` AS `DiaC`,`c`.`HoraC` AS `HoraC` from (`actividades` `a` join `clases` `c` on((`a`.`IdActividad` = `c`.`IdActividad`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `multasp`
--

/*!50001 DROP TABLE IF EXISTS `multasp`*/;
/*!50001 DROP VIEW IF EXISTS `multasp`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = cp850 */;
/*!50001 SET character_set_results     = cp850 */;
/*!50001 SET collation_connection      = cp850_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `multasp` AS select `pagos`.`IdPago` AS `IdPago`,`usuarios`.`IdUsuario` AS `IdUsuario`,`usuarios`.`NombreU` AS `NombreU`,`multas`.`Motivo` AS `Motivo`,`multas`.`CostoM` AS `CostoM`,`pagos`.`FechaMulta` AS `FechaMulta`,`pagos`.`FechaPago` AS `FechaPago`,`pagos`.`Estado` AS `Estado` from ((`pagos` join `multas` on((`multas`.`IdMulta` = `pagos`.`IdMulta`))) join `usuarios` on((`usuarios`.`IdUsuario` = `pagos`.`IdUsuario`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `reportes`
--

/*!50001 DROP TABLE IF EXISTS `reportes`*/;
/*!50001 DROP VIEW IF EXISTS `reportes`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `reportes` AS select `a`.`Escolaridad` AS `Escolaridad`,`a`.`Sexo` AS `Sexo`,`a`.`Discapacidad` AS `Discapacidad`,`v`.`FechaV` AS `FechaV` from (`usuarios` `a` join `visitas` `v`) where (`a`.`IdUsuario` = `v`.`IdUsuario`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-15 13:16:34
