DROP TABLE IF EXISTS Items;
CREATE TABLE Items(
   name     VARCHAR(22) NOT NULL
  ,desc     VARCHAR(41) NOT NULL
  ,type     INTEGER  NOT NULL
  ,imgIndex INTEGER  NOT NULL
  ,hp       INTEGER 
  ,exp      INTEGER 
  ,sell     INTEGER  NOT NULL
  ,mhp      INTEGER 
  ,dmg      INTEGER 
  ,acc      INTEGER 
  ,PRIMARY KEY(,type,imgIndex)
);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Small Health Potion','Restores a small amount of HP.',0,0,30,0,50,NULL,NULL,NULL);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Medium Health Potion','Restores a decent amount of HP.',0,1,100,0,75,NULL,NULL,NULL);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Slime Ball','A ball of green slime.',1,0,NULL,NULL,5,NULL,NULL,NULL);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Chocolate Cake','Not what you think it is.',1,1,NULL,NULL,5,NULL,NULL,NULL);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Baseball Cap','A regular red cap.',2,0,NULL,NULL,35,2,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Santa Hat','A special holiday hat.',2,1,NULL,NULL,225,10,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Magician''s Hat','A hat filled with magic tricks.',2,2,NULL,NULL,75,5,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Cowboy Hat','A hat worn in the wild west.',2,3,NULL,NULL,225,10,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Straw Hat','A hat said to worn
by a legendary pirate.',2,4,NULL,NULL,300,12,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Bronze Helmet','A low tier helmet.',2,5,NULL,NULL,75,8,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Iron Helmet','A middle tier helmet.',2,6,NULL,NULL,150,14,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Leather Armor','A beginner''s armor',3,1,NULL,NULL,85,10,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('White T-Shirt','A white shirt.',3,0,NULL,NULL,300,5,1,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Magician''s Robe','A robe imbued with magic.',3,2,NULL,NULL,55,5,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Ranger''s Leather Armor','Armor made for a high
skilled archer.',3,3,NULL,NULL,175,10,0,1);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Red Vest','Looks like a lifejacket.',3,4,NULL,NULL,55,5,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Bronze Chestplate','A low tier chestplate.',3,5,NULL,NULL,175,20,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Iron Chestplate','A middle tier chestplate.',3,6,NULL,NULL,250,35,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Wooden Stick','A beginner''s weapon.',4,0,NULL,NULL,15,0,2,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Wooden Sword','Not the most durable weapon.',4,1,NULL,NULL,40,0,4,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Fishing Rod','Reel the enemies in.',4,2,NULL,NULL,10,0,2,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Red Umbrella','Blocks more than just rain.',4,3,NULL,NULL,40,2,3,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Wooden Hammer','A hammer with a sharp edge.',4,4,NULL,NULL,30,0,3,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Bronze Sword','A low tier sword.',4,5,NULL,NULL,100,0,6,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Iron Sword','A middle tier sword.',4,6,NULL,NULL,125,0,8,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Leather Gloves','A beginner''s gloves.',5,0,NULL,NULL,15,5,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Bronze Gauntlet','A low tier gauntlet.',5,5,NULL,NULL,75,8,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Iron Gauntlet','A middle tier gauntlet.',5,6,NULL,NULL,150,14,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Leather Boots','A beginner''s boots.',6,0,NULL,NULL,15,5,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Winter Boots','Boots made for treading
in the snow.',6,1,NULL,NULL,15,5,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Ranger''s Leather Boots','Boots made for a high
skilled archer.',6,2,NULL,NULL,175,5,0,1);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Bronze Boots','Low tier boots.',6,4,NULL,NULL,75,7,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Iron Boots','Middle tier boots.',6,5,NULL,NULL,150,15,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Wooden Shield','Offers slight protection.',8,0,NULL,NULL,25,5,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Iron Round Shield','A round shield plated with iron.',8,1,NULL,NULL,150,12,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Bronze Shield','A low tier shield.',8,5,NULL,NULL,75,7,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Iron Shield','A middle tier shield.',8,6,NULL,NULL,150,14,0,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Silver Necklace','What does this even do?',7,1,NULL,NULL,300,0,0,1);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Golden Ring','A ring of precision.',9,0,NULL,NULL,300,0,0,1);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Silver Ring','What does this even do?',9,1,NULL,NULL,250,0,1,0);
INSERT INTO Items(name,desc,type,imgIndex,hp,exp,sell,mhp,dmg,acc) VALUES ('Wedding Ring','What''s the occasion?',9,2,NULL,NULL,750,1,1,1);
