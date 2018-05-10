DROP TABLE IF EXISTS ShopItems;
CREATE TABLE ShopItems(
   rarity   INTEGER  NOT NULL
  ,name     VARCHAR(25) NOT NULL
  ,description     VARCHAR(56) NOT NULL
  ,type     INTEGER  NOT NULL
  ,imgIndex INTEGER  NOT NULL
  ,hp       INTEGER 
  ,exp      INTEGER 
  ,sell     INTEGER  NOT NULL
  ,price    INTEGER  NOT NULL
  ,mhp      INTEGER 
  ,dmg      INTEGER 
  ,acc      INTEGER 
  ,eChance  INTEGER 
  ,PRIMARY KEY(,type,imgIndex)
);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Small Health Potion','Restores a small amount of HP.',0,0,30,0,50,155,NULL,NULL,NULL,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Medium Health Potion','Restores a decent amount of HP.',0,1,100,0,75,270,NULL,NULL,NULL,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Magician''s Hat','A hat filled with magic tricks.',2,0,NULL,NULL,25,100,4,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Blue Beret','A hat typically
wore by a Frenchman.',2,1,NULL,NULL,30,160,6,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Frog Hat','The head of a
funny frog costume.',2,2,NULL,NULL,40,200,7,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Party Hat','The wearer of this
hat is fun at parties.',2,3,NULL,NULL,40,200,7,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Bronze Helmet','A low tier helmet.',2,4,NULL,NULL,275,730,14,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Iron Helmet','A middle tier helmet.',2,5,NULL,NULL,450,1800,24,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Magician''s Robe','A robe imbued with magic.',3,0,NULL,NULL,45,125,7,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Blue Vest and Shirt','A fancy outfit
for a special occasion.',3,1,NULL,NULL,50,170,8,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Frog Suit','The overall of a
funny frog costume.',3,2,NULL,NULL,80,220,10,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Ranger''s Leather Armor','Armor made for a high
skilled archer.',3,3,NULL,NULL,175,600,14,0,1,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Bronze Chestplate','A low tier chestplate.',3,4,NULL,NULL,300,900,22,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Iron Chestplate','A middle tier chestplate.',3,5,NULL,NULL,550,2050,40,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Magician''s Wand','A magical stick.',4,0,NULL,NULL,40,170,0,4,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Wooden Bow','A sharp shooter''s
wooden bow.',4,1,NULL,NULL,70,500,0,6,1,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Fishing Rod','Reel the enemies in.',4,2,NULL,NULL,45,200,0,6,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Wooden Sword','Not the most durable weapon.',4,3,NULL,NULL,60,250,0,9,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Bronze Sword','A low tier sword.',4,4,NULL,NULL,280,800,0,18,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Iron Sword','A middle tier sword.',4,5,NULL,NULL,560,2200,0,27,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Magician''s Gloves','A magician''s
magical gloves.',5,0,NULL,NULL,25,100,4,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Leather Gloves','A beginner''s gloves.',5,1,NULL,NULL,30,120,5,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Bronze Gauntlet','A low tier gauntlet.',5,2,NULL,NULL,275,680,14,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Iron Gauntlet','A middle tier gauntlet.',5,3,NULL,NULL,430,1790,22,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Magician''s Shoes','A magician''s
magical shoes',6,0,NULL,NULL,25,100,4,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Leather Boots','A beginner''s boots.',6,1,NULL,NULL,30,120,5,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Bronze Boots','Low tier boots.',6,2,NULL,NULL,275,680,14,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Iron Boots','Middle tier boots.',6,3,NULL,NULL,430,1790,22,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Wooden Shield','Offers slight protection.',8,0,NULL,NULL,30,180,5,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Bronze Shield','A low tier shield.',8,1,NULL,NULL,325,700,18,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (0,'Iron Shield','A middle tier shield.',8,2,NULL,NULL,500,2000,30,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Large Health Potion','Restores a large amount of HP.',0,2,175,0,150,520,NULL,NULL,NULL,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Gold Helmet','A high tier helmet.',2,6,NULL,NULL,800,3600,40,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Gold Chestplate','A high tier chestplate.',3,6,NULL,NULL,900,3850,66,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Gold Sword','A high tier sword.',4,6,NULL,NULL,1000,4000,0,48,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Gold Gauntlet','A high tier gauntlet.',5,4,NULL,NULL,680,3500,38,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Gold Boots','High tier boots.',6,4,NULL,NULL,680,3500,38,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Golden Shield','A high tier shield.',8,3,NULL,NULL,800,3700,50,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Silver Necklace','What does this even do?',7,1,NULL,NULL,900,1800,0,0,1,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Pendant of Domination','A pendant said to
increase your power.',7,2,NULL,NULL,3600,20000,0,30,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Pendant of Protection','A pendant said to
increase your defense.',7,4,NULL,NULL,2000,12500,30,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Golden Ring','A ring of precision.',9,0,NULL,NULL,1000,2050,0,0,1,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Silver Ring','What does this even do?',9,1,NULL,NULL,250,700,0,1,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (1,'Ring of Destruction','A ring imbued with power.',9,2,NULL,NULL,2000,12500,0,20,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Special Health Potion','Restores a massive amount of HP.',0,3,-30,0,400,950,NULL,NULL,NULL,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Enchanted Health Potion','Restores a massive amount of HP.',0,4,-40,0,500,1100,NULL,NULL,NULL,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Experience Potion','Grants a small
amount of experience.',0,7,0,3,10000,120000,NULL,NULL,NULL,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Runic Helmet','A helmet imbued
with runic energy.',2,7,NULL,NULL,2200,12000,68,5,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Runic Chestplate','A chestplate imbued
with runic energy.',3,7,NULL,NULL,2900,13500,96,10,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Runic Sabre','A sword imbued
with runic energy.',4,7,NULL,NULL,3000,15000,0,78,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Runic Gauntlet','A gauntlet imbued
with runic energy.',5,5,NULL,NULL,2600,11000,64,3,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Runic Greaves','Boots imbued with
runic energy.',6,5,NULL,NULL,2600,11000,64,3,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Runic Shield','A shield imbued
with runic energy.',8,4,NULL,NULL,3000,13000,80,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Skull Shield','A shield made from
exhumed remains.',8,6,NULL,NULL,4000,18000,100,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Golden Necklace','A necklace of precision.',7,0,NULL,NULL,3000,11400,0,0,2,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Possessed Necklace','A powerful necklace said
to be cursed by a demon.',7,3,NULL,NULL,4500,25000,30,15,1,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Opposite But Equal','This power necklace
has a sister necklace.',7,5,NULL,NULL,12000,85000,70,30,2,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Equal But Opposite','This power necklace
has a sister necklace.',7,6,NULL,NULL,12000,85000,70,30,2,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Ring of Devastation','The user obtains
destructive power.',9,3,NULL,NULL,6900,38000,0,40,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (2,'Ring of Greater Life','The ring guards the
user against danger.',9,4,NULL,NULL,8000,55000,100,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Elixir of Life','An epic health potion.',0,5,-60,0,820,1900,NULL,NULL,NULL,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Greater Elixir of Life','A legendary health potion.',0,6,-80,0,1200,3000,NULL,NULL,NULL,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Greater Experience Potion','Grants a massive amount
of experience.',0,8,0,15,30000,550000,NULL,NULL,NULL,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Inferno Helmet','A helmet forged
from the inferno.',2,8,NULL,NULL,6000,38000,150,10,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Inferno Chestplate','A chestplate forged
from the inferno.',3,8,NULL,NULL,7900,56000,215,20,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Inferno Scimitar','A sword forged
from the inferno.',4,8,NULL,NULL,8000,65000,0,142,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Inferno Gauntlet','A gauntlet forged
from the inferno.',5,6,NULL,NULL,5800,35000,132,8,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Inferno Greaves','Boots forged in
the inferno.',6,6,NULL,NULL,5800,35000,132,8,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Inferno Shield','A shield forged
from the inferno.',8,5,NULL,NULL,7000,50000,180,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Bloodrazor','A necklace powered by
a blood red crystal.',7,7,NULL,NULL,9999,99999,40,70,2,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'The Cornerstone','A necklace that massively
increases focus and precision.',7,8,NULL,NULL,30000,200000,100,60,10,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Shield of Absorption','Absorbs a majority of
damage from an attack.',8,7,NULL,NULL,8800,60000,200,0,0,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Death''s Rage','A ring that gives the
user immense power.',9,5,NULL,NULL,16000,120000,14,90,2,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'The Morning Sun','This ring shines
brighter than the others.',9,6,NULL,NULL,25000,170000,140,55,4,NULL);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Enchant Scroll C','A scroll that enhances
enchanting chance.',10,0,NULL,NULL,1000,10000,NULL,NULL,NULL,10);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Enchant Scroll B','A scroll that enhances
enchanting chance.',10,1,NULL,NULL,5000,60000,NULL,NULL,NULL,20);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Enchant Scroll A','A scroll that enhances
enchanting chance.',10,2,NULL,NULL,10000,130000,NULL,NULL,NULL,30);
INSERT INTO ShopItems(rarity,name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc,eChance) VALUES (3,'Enchant Scroll S','A scroll that enhances
enchanting chance.',10,3,NULL,NULL,20000,700000,NULL,NULL,NULL,40);
