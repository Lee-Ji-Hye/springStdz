INSERT INTO Task
       (TSK_ID, CMNTY_ID, FLD_ID, TSK_DES, TSK_REG_DT, TSK_MOD_DT, TSK_DUE_DT, TSK_COM_DT)
VALUES
       (1, 1, 1, 'lala', CURRENT_TIMESTAMP(), null, null, parsedatetime('2020-03-21 18:47:52.69', 'yyyy-MM-dd hh:mm:ss.SS'))
     , (2, 1, 1, 'lala', CURRENT_TIMESTAMP(), null, null, parsedatetime('2020-03-21 18:47:52.69', 'yyyy-MM-dd hh:mm:ss.SS'))
  	 , (3, 1, 2, 'lala', CURRENT_TIMESTAMP(), null, null, parsedatetime('2020-03-21 18:47:52.69', 'yyyy-MM-dd hh:mm:ss.SS'))
  	 , (4, 2, 2, 'lala', CURRENT_TIMESTAMP(), null, null, parsedatetime('2020-03-21 18:47:52.69', 'yyyy-MM-dd hh:mm:ss.SS'))
  	 , (5, 1, 2, 'lala', CURRENT_TIMESTAMP(), null, null, parsedatetime('2020-03-21 18:47:52.69', 'yyyy-MM-dd hh:mm:ss.SS'))
  	 , (6, 3, 2, 'lala', CURRENT_TIMESTAMP(), null, null, parsedatetime('2020-03-21 18:47:52.69', 'yyyy-MM-dd hh:mm:ss.SS'))
  	 , (7, 1, 2, 'lala', CURRENT_TIMESTAMP(), null, null, parsedatetime('2020-03-21 18:47:52.69', 'yyyy-MM-dd hh:mm:ss.SS'))
;

INSERT INTO Community
VALUES
       (1, '스프링 스터디', '3')
     , (2, '모각코','0')
     , (3, '회사','1,2,3,4,5')
;