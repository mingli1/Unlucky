DROP TABLE IF EXISTS Items;
CREATE TABLE Items(
   rarity   INTEGER  NOT NULL
  ,name     VARCHAR(25) NOT NULL
  ,description     VARCHAR(69) NOT NULL
  ,type     INTEGER  NOT NULL
  ,imgIndex INTEGER  NOT NULL
  ,hp       INTEGER 
  ,exp      INTEGER 
  ,sell     INTEGER  NOT NULL
  ,mhp      INTEGER 
  ,dmg      INTEGER 
  ,acc      INTEGER 
  ,eChance  INTEGER 
  ,PRIMARY KEY(type,imgIndex)
);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Small Health Potion','Restores a small amount of HP.',0,0,30,0,50,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Medium Health Potion','Restores a decent amount of HP.',0,1,100,0,75,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Slime Ball','A ball of green slime.',1,0,NULL,NULL,5,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Chocolate Cake','Not what you think it is.',1,1,NULL,NULL,5,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Baseball Cap','A regular red cap.',2,0,NULL,NULL,35,2,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Santa Hat','A special holiday hat.',2,1,NULL,NULL,225,10,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Magician''s Hat','A hat filled with magic tricks.',2,2,NULL,NULL,75,5,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Cowboy Hat','A hat worn in the wild west.',2,3,NULL,NULL,225,10,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Straw Hat','A hat said to worn
by a legendary pirate.',2,4,NULL,NULL,300,12,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Bronze Helmet','A low tier helmet.',2,5,NULL,NULL,75,8,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Iron Helmet','A middle tier helmet.',2,6,NULL,NULL,150,14,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Leather Armor','A beginner''s armor',3,1,NULL,NULL,85,10,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'White T-Shirt','A white shirt.',3,0,NULL,NULL,300,5,1,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Magician''s Robe','A robe imbued with magic.',3,2,NULL,NULL,55,5,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Ranger''s Leather Armor','Armor made for a high
skilled archer.',3,3,NULL,NULL,175,10,0,1,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Red Vest','Looks like a lifejacket.',3,4,NULL,NULL,55,5,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Bronze Chestplate','A low tier chestplate.',3,5,NULL,NULL,175,20,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Iron Chestplate','A middle tier chestplate.',3,6,NULL,NULL,250,35,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Wooden Stick','A beginner''s weapon.',4,0,NULL,NULL,15,0,2,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Wooden Sword','Not the most durable weapon.',4,1,NULL,NULL,40,0,4,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Fishing Rod','Reel the enemies in.',4,2,NULL,NULL,10,0,2,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Red Umbrella','Blocks more than just rain.',4,3,NULL,NULL,40,2,3,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Wooden Hammer','A hammer with a sharp edge.',4,4,NULL,NULL,30,0,3,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Bronze Sword','A low tier sword.',4,5,NULL,NULL,100,0,6,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Iron Sword','A middle tier sword.',4,6,NULL,NULL,125,0,8,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Leather Gloves','A beginner''s gloves.',5,0,NULL,NULL,15,5,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Bronze Gauntlet','A low tier gauntlet.',5,5,NULL,NULL,75,8,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Iron Gauntlet','A middle tier gauntlet.',5,6,NULL,NULL,150,14,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Leather Boots','A beginner''s boots.',6,0,NULL,NULL,15,5,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Winter Boots','Boots made for treading
in the snow.',6,1,NULL,NULL,15,5,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Ranger''s Leather Boots','Boots made for a high
skilled archer.',6,2,NULL,NULL,175,5,0,1,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Bronze Boots','Low tier boots.',6,4,NULL,NULL,75,7,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Iron Boots','Middle tier boots.',6,5,NULL,NULL,150,15,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Wooden Shield','Offers slight protection.',8,0,NULL,NULL,25,5,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Iron Round Shield','A round shield plated with iron.',8,1,NULL,NULL,150,12,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Bronze Shield','A low tier shield.',8,5,NULL,NULL,75,7,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Iron Shield','A middle tier shield.',8,6,NULL,NULL,150,14,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Silver Necklace','What does this even do?',7,1,NULL,NULL,300,0,0,1,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Golden Ring','A ring of precision.',9,0,NULL,NULL,300,0,0,1,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Silver Ring','What does this even do?',9,1,NULL,NULL,250,0,1,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (0,'Wedding Ring','What''s the occasion?',9,2,NULL,NULL,750,1,1,1,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Large Health Potion','Restores a large amount of HP.',0,2,175,0,150,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Large Health Potion','Restores a large amount of HP.',0,3,150,0,120,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Gold Helmet','A high tier helmet.',2,7,NULL,NULL,275,20,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Gold Chestplate','A high tier chestplate.',3,7,NULL,NULL,375,50,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Gold Sword','A high tier sword.',4,7,NULL,NULL,175,0,10,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Mittens','Keeps you warm
during the winter.',5,1,NULL,NULL,555,10,1,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Gloves of Accuracy','Gloves said to increase
your focus.',5,2,NULL,NULL,325,0,0,1,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Gloves of Domination','Gloves said to increase
your power.',5,3,NULL,NULL,325,0,5,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Gloves of Protection','Gloves said to increase
your defense.',5,4,NULL,NULL,325,15,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Gold Gauntlet','A high tier gauntlet.',5,7,NULL,NULL,275,20,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Snow Shoes','Very cold looking boots.',6,3,NULL,NULL,250,14,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Gold Boots','High tier boots.',6,6,NULL,NULL,275,20,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Boots of Accuracy','Boots said to increase
your focus.',6,8,NULL,NULL,325,0,0,1,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Boots of Domination','Boots said to increase
your power.',6,9,NULL,NULL,325,0,5,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Boots of Protection','Boots said to increase
your defense.',6,10,NULL,NULL,325,15,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Golden Necklace','A necklace of precision.',7,0,NULL,NULL,450,0,0,2,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Pendant of Domination','A pendant said to
increase your power.',7,2,NULL,NULL,575,0,7,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Pendant of Protection','A pendant said to
increase your defense.',7,4,NULL,NULL,575,20,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Golden Round Shield','A round shield trimmed with gold.',8,2,NULL,NULL,220,17,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Golden Shield','A high tier shield.',8,7,NULL,NULL,300,20,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (1,'Ring of Destruction','A ring imbued with power.',9,3,NULL,NULL,575,0,6,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Special Health Potion','Restores a massive amount of HP.',0,4,-30,0,400,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Experience Potion','Grants a small
amount of experience.',0,8,0,3,10000,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Enchanted Health Potion','Restores a massive amount of HP.',0,5,-40,0,500,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Runic Helmet','A helmet imbued
with runic energy.',2,8,NULL,NULL,680,30,2,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Runic Chestplate','A chestplate imbued
with runic energy.',3,8,NULL,NULL,1020,65,2,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Runic Sabre','A sword imbued
with runic energy.',4,8,NULL,NULL,900,0,14,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Runic Gauntlet','A gauntlet imbued
with runic energy.',5,8,NULL,NULL,700,30,2,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Runic Greaves','Boots imbued with
runic energy.',6,7,NULL,NULL,710,30,2,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Runic Shield','A shield imbued
with runic energy.',8,8,NULL,NULL,970,33,2,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Possessed Necklace','A powerful necklace said
to be cursed by a demon.',7,3,NULL,NULL,975,3,15,1,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Shield of Reflection','Works like a mirror.',8,4,NULL,NULL,789,35,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'On the defense!','An epic shield capable
of defending against the
strongest of attacks.',8,3,NULL,NULL,620,35,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Ring of Devastation','The user obtains
destructive power.',9,4,NULL,NULL,1052,0,19,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (2,'Ring of Greater Life','The ring guards the
user against danger.',9,5,NULL,NULL,1052,40,0,0,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'Elixir of Life','An epic health potion.',0,6,-60,0,820,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'Greater Elixir of Life','A legendary health potion.',0,7,-80,0,1200,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'Greater Experience Potion','Grants a massive amount
of experience.',0,9,0,15,30000,NULL,NULL,NULL,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'Death''s Fury','A necklace that gives the
user immense power.',7,5,NULL,NULL,2000,4,21,2,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'Nefaria','An ancient necklace forged
with the rarest materials.',7,6,NULL,NULL,3100,12,16,4,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'Soulstealer','An ancient ring forged
with the rarest materials.',9,6,NULL,NULL,3100,12,16,4,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'The Heartseeker','Allows the user to
target the enemy''s sweet spot.',9,7,NULL,NULL,2150,20,8,5,NULL);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'Enchant Scroll C','A scroll that enhances
enchanting chance.',10,0,NULL,NULL,1000,NULL,NULL,NULL,10);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'Enchant Scroll B','A scroll that enhances
enchanting chance.',10,1,NULL,NULL,5000,NULL,NULL,NULL,20);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'Enchant Scroll A','A scroll that enhances
enchanting chance.',10,2,NULL,NULL,10000,NULL,NULL,NULL,30);
INSERT INTO Items(rarity,name,description,type,imgIndex,hp,exp,sell,mhp,dmg,acc,eChance) VALUES (3,'Enchant Scroll S','A scroll that enhances
enchanting chance.',10,3,NULL,NULL,20000,NULL,NULL,NULL,40);
