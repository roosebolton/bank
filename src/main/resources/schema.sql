DROP SCHEMA IF EXISTS piratebanktest;
CREATE SCHEMA IF NOT EXISTS piratebanktest ;
USE piratebanktest;

CREATE TABLE `user`
(
    `user_id`  int          NOT NULL AUTO_INCREMENT,
    `username` varchar(320) NOT NULL,
    `password` varchar(320)  NOT NULL
);

CREATE TABLE `customer`
(
    `user_id`               int          NOT NULL,
    `first_name`            varchar(45)  NOT NULL,
    `infix`                 varchar(10),
    `last_name`             varchar(45)  NOT NULL,
    `date_of_birth`         date         NOT NULL,
    `bsn_number`            int          NOT NULL,
    `postal_code`           varchar(7)   NOT NULL,
    `house_number`          varchar(45)  NOT NULL,
    `house_number_addition` varchar(10),
    `street`                varchar(100) NOT NULL,
    `city`                  varchar(30)  NOT NULL,
    `iban_number`           varchar(18)  NOT NULL
);

CREATE TABLE `Asset`
(
    `name`         varchar(45) NOT NULL,
    `abbreviation` varchar(3)  NOT NULL
);

CREATE TABLE `asset_rates`
(
    `asset_name`        varchar(45) NOT NULL,
    `timestamp`         timestamp NOT NULL,
    `value`             decimal(19,1) NOT NULL,
    PRIMARY KEY (`asset_name`, `timestamp`)
);

CREATE TABLE `configdata`
(
  `idconfigdata` INT NOT NULL,
  `transaction_fee` DECIMAL(19,8) NOT NULL,
  `bank_starting_capital` DECIMAL(19,8) NOT NULL,
  `bank_id` INT NOT NULL,
   PRIMARY KEY (`idconfigdata`)

);


