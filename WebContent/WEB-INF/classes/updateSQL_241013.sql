USE `stock`;

DELIMITER $$

DROP TRIGGER IF EXISTS stock.mst_opname_BINS$$
USE `stock`$$


CREATE DEFINER=`root`@`localhost` TRIGGER `mst_opname_BINS` BEFORE INSERT ON mst_opname FOR EACH ROW
-- Edit trigger body code below this line. Do not edit lines above this one
SET NEW.no_trans = CONCAT('020'
,date_format(sysdate(),'%m')
,date_format(sysdate(),'%y')
,(SELECT kode FROM lst_cabang c, lst_user u where c.id = u.cabang_id and u.id=New.createby)
,lpad( (SELECT mod(`auto_increment`, 99999) FROM `INFORMATION_SCHEMA`.`TABLES` 
WHERE table_name = 'mst_opname' and table_schema = 'stock' limit 0,1),5,'0')
)$$
DELIMITER ;
