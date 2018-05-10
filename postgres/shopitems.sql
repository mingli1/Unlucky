DROP TABLE IF EXISTS ShopItems;
CREATE TABLE ShopItems(
   name     VARCHAR(22) NOT NULL
  ,description     VARCHAR(41) NOT NULL
  ,type     INTEGER  NOT NULL
  ,imgIndex INTEGER  NOT NULL
  ,hp       INTEGER 
  ,exp      INTEGER 
  ,sell     INTEGER  NOT NULL
  ,price    INTEGER  NOT NULL
  ,mhp      INTEGER 
  ,dmg      INTEGER 
  ,acc      INTEGER 
  ,PRIMARY KEY(type,imgIndex)
);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Small Health Potion','Restores a small amount of HP.',0,0,30,0,50,155,NULL,NULL,NULL);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Medium Health Potion','Restores a decent amount of HP.',0,1,100,0,75,270,NULL,NULL,NULL);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Magician''s Hat','A hat filled with magic tricks.',2,0,NULL,NULL,25,100,4,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Blue Beret','A hat typically
wore by a Frenchman.',2,1,NULL,NULL,30,160,6,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Frog Hat','The head of a
funny frog costume.',2,2,NULL,NULL,40,200,7,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Party Hat','The wearer of this
hat is fun at parties.',2,3,NULL,NULL,40,200,7,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Bronze Helmet','A low tier helmet.',2,4,NULL,NULL,275,730,14,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Iron Helmet','A middle tier helmet.',2,5,NULL,NULL,450,1800,24,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Magician''s Robe','A robe imbued with magic.',3,0,NULL,NULL,45,125,7,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Blue Vest and Shirt','A fancy outfit
for a special occasion.',3,1,NULL,NULL,50,170,8,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Frog Suit','The overall of a
funny frog costume.',3,2,NULL,NULL,80,220,10,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Ranger''s Leather Armor','Armor made for a high
skilled archer.',3,3,NULL,NULL,175,600,14,0,1);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Bronze Chestplate','A low tier chestplate.',3,4,NULL,NULL,300,900,22,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Iron Chestplate','A middle tier chestplate.',3,5,NULL,NULL,550,2050,40,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Magician''s Wand','A magical stick.',4,0,NULL,NULL,40,170,0,4,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Wooden Bow','A sharp shooter''s
wooden bow.',4,1,NULL,NULL,70,500,0,6,1);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Fishing Rod','Reel the enemies in.',4,2,NULL,NULL,45,200,0,6,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Wooden Sword','Not the most durable weapon.',4,3,NULL,NULL,60,250,0,9,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Bronze Sword','A low tier sword.',4,4,NULL,NULL,280,800,0,18,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Iron Sword','A middle tier sword.',4,5,NULL,NULL,560,2200,0,27,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Magician''s Gloves','A magician''s
magical gloves.',5,0,NULL,NULL,25,100,4,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Leather Gloves','A beginner''s gloves.',5,1,NULL,NULL,30,120,5,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Bronze Gauntlet','A low tier gauntlet.',5,2,NULL,NULL,275,680,14,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Iron Gauntlet','A middle tier gauntlet.',5,3,NULL,NULL,430,1790,22,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Magician''s Shoes','A magician''s
magical shoes',6,0,NULL,NULL,25,100,4,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Leather Boots','A beginner''s boots.',6,1,NULL,NULL,30,120,5,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Bronze Boots','Low tier boots.',6,2,NULL,NULL,275,680,14,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Iron Boots','Middle tier boots.',6,3,NULL,NULL,430,1790,22,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Wooden Shield','Offers slight protection.',8,0,NULL,NULL,30,180,5,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Bronze Shield','A low tier shield.',8,1,NULL,NULL,325,700,18,0,0);
INSERT INTO ShopItems(name,description,type,imgIndex,hp,exp,sell,price,mhp,dmg,acc) VALUES ('Iron Shield','A middle tier shield.',8,2,NULL,NULL,500,2000,30,0,0);
