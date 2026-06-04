/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 5.7.9 : Database - student_faculty
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`student_faculty` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `student_faculty`;

/*Table structure for table `attendance` */

DROP TABLE IF EXISTS `attendance`;

CREATE TABLE `attendance` (
  `att_id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `att_date` varchar(111) DEFAULT NULL,
  `att_hour` varchar(111) DEFAULT NULL,
  `att_status` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`att_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `attendance` */

insert  into `attendance`(`att_id`,`student_id`,`att_date`,`att_hour`,`att_status`) values 
(1,1,'2007-02-22','4th hour','present'),
(2,1,'2007-02-22','4th hour','present'),
(3,1,'2010-06-06','4th hour','present');

/*Table structure for table `batches` */

DROP TABLE IF EXISTS `batches`;

CREATE TABLE `batches` (
  `batch_id` int(11) NOT NULL AUTO_INCREMENT,
  `department_id` int(11) DEFAULT NULL,
  `batch_name` varchar(111) DEFAULT NULL,
  `batch_description` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`batch_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `batches` */

insert  into `batches`(`batch_id`,`department_id`,`batch_name`,`batch_description`) values 
(4,3,'2017-20','....');

/*Table structure for table `department` */

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `department_id` int(11) NOT NULL AUTO_INCREMENT,
  `department_name` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `department` */

insert  into `department`(`department_id`,`department_name`) values 
(3,'BCA.');

/*Table structure for table `exams` */

DROP TABLE IF EXISTS `exams`;

CREATE TABLE `exams` (
  `exam_id` int(11) NOT NULL AUTO_INCREMENT,
  `course_name` varchar(111) DEFAULT NULL,
  `subject_name` varchar(111) DEFAULT NULL,
  `exam_type` varchar(111) DEFAULT NULL,
  `exam_date` varchar(111) DEFAULT NULL,
  `exam_time` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`exam_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `exams` */

/*Table structure for table `fees` */

DROP TABLE IF EXISTS `fees`;

CREATE TABLE `fees` (
  `fee_id` int(11) NOT NULL AUTO_INCREMENT,
  `fee_amount` varchar(111) DEFAULT NULL,
  `course_name` varchar(111) DEFAULT NULL,
  `due_date` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`fee_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `fees` */

/*Table structure for table `leave_request` */

DROP TABLE IF EXISTS `leave_request`;

CREATE TABLE `leave_request` (
  `leave_id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `reason` varchar(222) DEFAULT NULL,
  `leave_date` varchar(222) DEFAULT NULL,
  `no_of_days` varchar(222) DEFAULT NULL,
  `date_time` varchar(222) DEFAULT NULL,
  `status` varchar(222) DEFAULT NULL,
  PRIMARY KEY (`leave_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `leave_request` */

insert  into `leave_request`(`leave_id`,`student_id`,`reason`,`leave_date`,`no_of_days`,`date_time`,`status`) values 
(1,1,'fever','12-12-12','5','32-23-23','rejected');

/*Table structure for table `login` */

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `login_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(111) DEFAULT NULL,
  `password` varchar(111) DEFAULT NULL,
  `usertype` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`login_id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Data for the table `login` */

insert  into `login`(`login_id`,`username`,`password`,`usertype`) values 
(1,'admin','admin','admin'),
(2,'Damon Lynn','Pa$$w0rd!','staff'),
(7,'staff','staff','staff'),
(8,'staff1','staff1','staff'),
(9,'p1','p1','parent'),
(10,'p2','p2','parent'),
(11,'s1','s1','student'),
(12,'s2','s2','student');

/*Table structure for table `marklist` */

DROP TABLE IF EXISTS `marklist`;

CREATE TABLE `marklist` (
  `mark_id` int(11) NOT NULL AUTO_INCREMENT,
  `exam_id` int(11) DEFAULT NULL,
  `student_id` int(11) DEFAULT NULL,
  `internal_mark` varchar(111) DEFAULT NULL,
  `mark_awarded` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`mark_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `marklist` */

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `staff_id` int(11) DEFAULT NULL,
  `message` varchar(111) DEFAULT NULL,
  `reply` varchar(111) DEFAULT NULL,
  `message_date` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `message` */

/*Table structure for table `notification` */

DROP TABLE IF EXISTS `notification`;

CREATE TABLE `notification` (
  `notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(222) DEFAULT NULL,
  `description` varchar(222) DEFAULT NULL,
  `date_time` varchar(222) DEFAULT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `notification` */

insert  into `notification`(`notification_id`,`title`,`description`,`date_time`) values 
(2,'1','1','21323');

/*Table structure for table `parent` */

DROP TABLE IF EXISTS `parent`;

CREATE TABLE `parent` (
  `parent_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` int(11) DEFAULT NULL,
  `relation_with_student` varchar(111) DEFAULT NULL,
  `first_name` varchar(111) DEFAULT NULL,
  `last_name` varchar(111) DEFAULT NULL,
  `house_name` varchar(111) DEFAULT NULL,
  `place` varchar(111) DEFAULT NULL,
  `pincode` varchar(111) DEFAULT NULL,
  `phone` varchar(111) DEFAULT NULL,
  `email` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`parent_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `parent` */

insert  into `parent`(`parent_id`,`login_id`,`relation_with_student`,`first_name`,`last_name`,`house_name`,`place`,`pincode`,`phone`,`email`) values 
(1,9,'Guardian','p1','p1','Exercitationem quasi','Exercitationem quasi','Voluptatem voluptas ','+1 (375) 427-9142','nebagesu@mailinator.com'),
(2,10,'Brother','p2','p2','Sint esse esse enim','Sint esse esse enim','Nulla ut repellendus','+1 (739) 335-6692','nucecatawa@mailinator.com');

/*Table structure for table `payments` */

DROP TABLE IF EXISTS `payments`;

CREATE TABLE `payments` (
  `payment_id` int(11) NOT NULL AUTO_INCREMENT,
  `fee_id` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `payment_date` varchar(111) DEFAULT NULL,
  `amount_paid` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`payment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `payments` */

/*Table structure for table `staffs` */

DROP TABLE IF EXISTS `staffs`;

CREATE TABLE `staffs` (
  `staff_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` int(11) DEFAULT NULL,
  `batch_id` int(11) DEFAULT NULL,
  `first_name` varchar(111) DEFAULT NULL,
  `last_name` varchar(111) DEFAULT NULL,
  `qualification` varchar(111) DEFAULT NULL,
  `phone` varchar(111) DEFAULT NULL,
  `email` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`staff_id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Data for the table `staffs` */

insert  into `staffs`(`staff_id`,`login_id`,`batch_id`,`first_name`,`last_name`,`qualification`,`phone`,`email`) values 
(1,2,NULL,'aaa','Boris Bird','Necessitatibus tempo','+1 (741) 954-3711','pirise@mailinator.com'),
(6,7,NULL,'Maya Fisher','Jocelyn Cervantes','Eligendi sequi sit ','+1 (927) 546-5107','fywatu@mailinator.com'),
(7,8,4,'Amir Blake','Jermaine Guzman','Autem placeat ea ad','+1 (258) 574-6752','kewomagy@mailinator.com');

/*Table structure for table `students` */

DROP TABLE IF EXISTS `students`;

CREATE TABLE `students` (
  `student_id` int(11) NOT NULL AUTO_INCREMENT,
  `login_id` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `batch_id` int(11) DEFAULT NULL,
  `first_name` varchar(111) DEFAULT NULL,
  `last_name` varchar(111) DEFAULT NULL,
  `gender` varchar(111) DEFAULT NULL,
  `dob` varchar(111) DEFAULT NULL,
  `phone` varchar(111) DEFAULT NULL,
  `email` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `students` */

insert  into `students`(`student_id`,`login_id`,`parent_id`,`batch_id`,`first_name`,`last_name`,`gender`,`dob`,`phone`,`email`) values 
(1,11,1,4,'s1','s1','female','2002-11-02','+1 (513) 299-4953','wemesiwis@mailinator.com'),
(2,12,2,4,'s2','s2','male','1993-09-21','+1 (686) 337-2817','wopi@mailinator.com');

/*Table structure for table `study_material` */

DROP TABLE IF EXISTS `study_material`;

CREATE TABLE `study_material` (
  `material_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(111) DEFAULT NULL,
  `material_path` varchar(111) DEFAULT NULL,
  `staff_id` varchar(111) DEFAULT NULL,
  PRIMARY KEY (`material_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `study_material` */

/*Table structure for table `subjects` */

DROP TABLE IF EXISTS `subjects`;

CREATE TABLE `subjects` (
  `subject_id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_id` int(11) DEFAULT NULL,
  `subject_name` varchar(222) DEFAULT NULL,
  PRIMARY KEY (`subject_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `subjects` */

insert  into `subjects`(`subject_id`,`batch_id`,`subject_name`) values 
(2,4,'science'),
(3,4,'maths');

/*Table structure for table `time_table` */

DROP TABLE IF EXISTS `time_table`;

CREATE TABLE `time_table` (
  `table_id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_id` int(11) DEFAULT NULL,
  `day` varchar(222) DEFAULT NULL,
  `session` varchar(222) DEFAULT NULL,
  PRIMARY KEY (`table_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `time_table` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
