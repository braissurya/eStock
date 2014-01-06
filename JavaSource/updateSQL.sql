CREATE TABLE `lst_closing_period` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE` int(1) NOT NULL COMMENT 'Type Closing : 1=stock; 2=accounting',
  `DESC` varchar(45) DEFAULT NULL COMMENT 'deskripsi closing',
  `PERIODE` datetime DEFAULT NULL COMMENT 'periode terakhir closing',
  `CABANG_ID` int(11) DEFAULT NULL COMMENT 'Cabang Periode closing',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='table periode closing';

ALTER TABLE `stock`.`mst_rep_tb` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`periode`, `coa_id`) ;

INSERT INTO `stock`.`lst_closing_period` (`ID`,`TYPE`,`DESC`, `PERIODE`,`CABANG_ID`) VALUES (1,1,'CLOSING STOCK', '2013-10-01',1);

INSERT INTO `stock`.`lst_closing_period` (`ID`,`TYPE`,`DESC`, `PERIODE`) VALUES (2,2,'CLOSING ACCOUNTING', '2013-10-01',1);

UPDATE `stock`.`lst_menu` SET `link` = 'report/uang/in1' WHERE `id` = 64;

UPDATE `stock`.`lst_menu` SET `link` = 'report/uang/out' WHERE `id` = 65;

UPDATE `stock`.`lst_menu` SET `link` = 'report/jurnal' WHERE `id` = 93;

INSERT INTO `stock`.`lst_config` (`id`, `jenis`, `keterangan`, `active`, `createby`) VALUES (5, -1, 'ORDER  0', 1, 1);

INSERT INTO `stock`.`lst_config` (`id`, `jenis`, `keterangan`, `active`, `createby`) VALUES (11, -1, 'ORDER 0', 1, 1);

-- setelah jalanin script INSERT lst_bank dibawah, untuk lst_bank yg NON BANK, tolong diupdate manual ID nya menjadi 0
INSERT INTO `stock`.`lst_bank` (`id`, `nama`, `kode_bi`, `active`, `createby`) VALUES (0, 'NON BANK', null, 1, 1);

-- setelah jalanin script INSERT lst_account dibawah, untuk lst_account yg NON BANK, tolong diupdate manual ID nya menjadi 0
INSERT INTO `stock`.`lst_account` (`id`, `id_bank`, `coa_id`, `cabang`, `no_rek`, `kurs`, `atas_nama`, `saldo`, `active`, `createby`) VALUES (0, 0, '1.01.00', '-', '-', 1, '-', 0.00, 1, 1);

