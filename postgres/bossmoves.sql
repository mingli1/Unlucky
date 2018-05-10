DROP TABLE IF EXISTS BossMoves;
CREATE TABLE BossMoves(
   bossIndex    INTEGER  NOT NULL
  ,type         INTEGER  NOT NULL
  ,name         VARCHAR(12) NOT NULL PRIMARY KEY
  ,minDamage    INTEGER 
  ,maxDamage    INTEGER 
  ,minHeal      INTEGER 
  ,maxHeal      INTEGER 
  ,dmgReduction INTEGER 
);
INSERT INTO BossMoves(bossIndex,type,name,minDamage,maxDamage,minHeal,maxHeal,dmgReduction) VALUES (0,1,'Bounce',1,8,NULL,NULL,NULL);
INSERT INTO BossMoves(bossIndex,type,name,minDamage,maxDamage,minHeal,maxHeal,dmgReduction) VALUES (0,1,'Slime Ball',1,11,NULL,NULL,NULL);
INSERT INTO BossMoves(bossIndex,type,name,minDamage,maxDamage,minHeal,maxHeal,dmgReduction) VALUES (0,1,'Poison Slime',1,10,NULL,NULL,NULL);
INSERT INTO BossMoves(bossIndex,type,name,minDamage,maxDamage,minHeal,maxHeal,dmgReduction) VALUES (0,1,'Slime Snare',1,9,NULL,NULL,NULL);
INSERT INTO BossMoves(bossIndex,type,name,minDamage,maxDamage,minHeal,maxHeal,dmgReduction) VALUES (0,1,'Goo Gun',1,14,NULL,NULL,NULL);
INSERT INTO BossMoves(bossIndex,type,name,minDamage,maxDamage,minHeal,maxHeal,dmgReduction) VALUES (0,1,'Slime Fire',1,12,NULL,NULL,NULL);
INSERT INTO BossMoves(bossIndex,type,name,minDamage,maxDamage,minHeal,maxHeal,dmgReduction) VALUES (0,1,'Blast Slime',1,15,NULL,NULL,NULL);
INSERT INTO BossMoves(bossIndex,type,name,minDamage,maxDamage,minHeal,maxHeal,dmgReduction) VALUES (0,3,'Regenerate',NULL,NULL,3,6,40);
