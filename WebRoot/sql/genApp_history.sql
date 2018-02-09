SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for genApp_history
-- ----------------------------
DROP TABLE IF EXISTS `genApp_history`;
CREATE TABLE `genApp_history` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(50) default NULL,
  `appname` varchar(50) default NULL,
  `rulename` varchar(50) default NULL, 
  `searchkeyname` varchar(50) default NULL, 
  `createdate` datetime default NULL,  
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Test Records of genApp_history
-- ----------------------------
INSERT INTO `genApp_history` VALUES ('1', 'admin', 'nseApp_1','','',now());
INSERT INTO `genApp_history` VALUES ('2', 'admin', 'nseApp_2','','','2017-9-9 23:22:11');
INSERT INTO `genApp_history` VALUES ('3', 'xusen.li', 'nseApp_1','','','2016-9-9 23:22:11');