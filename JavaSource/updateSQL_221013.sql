ALTER TABLE `stock`.`mst_supplier` 
ADD COLUMN `pkp` TINYINT(1) NULL DEFAULT 0 COMMENT '0 : kena pajak (PKP)\n1 : tidak kena pajak (PTKP)' AFTER `last_order`;

ALTER TABLE `stock`.`mst_customer` 
ADD COLUMN `flag_ecer` TINYINT(1) NULL DEFAULT 1 COMMENT '1 = ecer\n0 = grosir' AFTER `pkp`,
ADD COLUMN `pay_mode` INT(2) NULL DEFAULT NULL COMMENT 'lst_config id 8\n\nkalau cash maka posisi langsung di payment baru gudang\nkalau credit maka posisi langsung di gudang' AFTER `flag_ecer`;

ALTER TABLE `stock`.`mst_karyawan` 
CHANGE COLUMN `gaji` `gaji` DECIMAL(14,2) NULL DEFAULT 0.00 COMMENT 'gaji pokok' ,
ADD COLUMN `makan` DECIMAL(14,2) NULL DEFAULT 0.00 COMMENT 'uang makan' AFTER `gaji`,
ADD COLUMN `transport` DECIMAL(14,2) NULL DEFAULT 0.00 COMMENT 'uang transport' AFTER `makan`;


