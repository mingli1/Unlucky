DROP TABLE IF EXISTS Moves;
CREATE TABLE Moves(
   type         INTEGER  NOT NULL
  ,name         VARCHAR(16) NOT NULL PRIMARY KEY
  ,minDamage    INTEGER 
  ,maxDamage    INTEGER 
  ,damage       INTEGER 
  ,crit         INTEGER 
  ,minHeal      INTEGER 
  ,maxHeal      INTEGER 
  ,dmgReduction INTEGER 
);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Shock',1,2,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Poke',1,2,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Jab',1,2,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Quick Shot',2,3,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Silence',2,3,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Headbutt',2,3,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Slash Storm',2,3,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Flash Attack',2,3,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Electro Punch',3,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Magma Punch',3,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Ice Punch',3,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Comet Punch',3,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Acid Rain',3,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Poison Vapor',3,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Electrocute',3,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Incinerate',3,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Freeze',3,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Ignite',3,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Apply Poison',3,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Source Strike',4,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Cyclone',4,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Vortex Punch',4,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Mind Control',4,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Open the Void',5,6,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Shade Twist',5,6,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'Death Seal',6,7,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (0,'End of Time',7,8,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Swipe',1,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Swindle',1,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Flimflam',1,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Boom',1,4,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Rain of Arrows',1,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Trick Shot',1,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Brandish',1,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Shadow Attack',1,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Mortal Spear',1,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Mirage Stab',1,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Spiral Strike',1,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Ace of Spades',1,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Hurricane Beam',1,5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Charm',1,6,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Ring of Fire',1,6,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Necrotic Zap',1,6,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Chronostrike',1,6,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Aura Aurora',1,6,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Reflection Ray',1,7,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Death and Decay',1,7,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Gravity Punch',1,7,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Phantom Slice',1,8,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Quantum Strike',1,9,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Infinity Stab',1,9,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Summon Meteor',1,10,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Shake the Earth',1,11,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (1,'Press the Luck',1,15,NULL,NULL,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Shift Stab',NULL,NULL,2,60,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Whirlpool',NULL,NULL,2,70,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Lucky Strike',NULL,NULL,2,67,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Conjure Wave',NULL,NULL,2,80,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Execute',NULL,NULL,2,50,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Bee Sting',NULL,NULL,2,60,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Flip the Coin',NULL,NULL,2,50,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Melting Fire',NULL,NULL,2,55,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Warp Strike',NULL,NULL,2,40,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Pyro Pulse',NULL,NULL,2,40,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Expose Weakness',NULL,NULL,3,30,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Entropy Release',NULL,NULL,3,40,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Plague',NULL,NULL,3,50,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Illusion',NULL,NULL,3,55,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Vital Stab',NULL,NULL,3,25,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Radiant Punch',NULL,NULL,3,33,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Shockwave',NULL,NULL,3,35,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Zeus''s Lightning',NULL,NULL,3,45,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Tempest Shot',NULL,NULL,3,30,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Memory Mash',NULL,NULL,4,15,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Headshot',NULL,NULL,4,10,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Soul Stealer',NULL,NULL,4,20,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Toxic Dart',NULL,NULL,4,25,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Moonstrike',NULL,NULL,4,15,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Earthquake',NULL,NULL,5,10,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Disintegrate',NULL,NULL,5,15,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (2,'Nuclear Bullet',NULL,NULL,5,65,NULL,NULL,NULL);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Bandage',NULL,NULL,NULL,NULL,1,2,30);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Undying Resolve',NULL,NULL,NULL,NULL,1,2,80);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Light Heal',NULL,NULL,NULL,NULL,1,3,20);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Cure',NULL,NULL,NULL,NULL,2,3,30);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Strengthen',NULL,NULL,NULL,NULL,2,3,50);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Regeneration',NULL,NULL,NULL,NULL,2,4,20);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Repair Self',NULL,NULL,NULL,NULL,2,4,10);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Recovery',NULL,NULL,NULL,NULL,3,5,20);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Healing Wave',NULL,NULL,NULL,NULL,4,5,40);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Heal of Life',NULL,NULL,NULL,NULL,4,6,50);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Blessing of Gods',NULL,NULL,NULL,NULL,6,8,60);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Genesis',NULL,NULL,NULL,NULL,7,10,40);
INSERT INTO Moves(type,name,minDamage,maxDamage,damage,crit,minHeal,maxHeal,dmgReduction) VALUES (3,'Life or Death',NULL,NULL,NULL,NULL,1,10,30);
