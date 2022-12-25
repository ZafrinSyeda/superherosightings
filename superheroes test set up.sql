DROP DATABASE IF EXISTS superherosightingsTest;
CREATE DATABASE superherosightingsTest; 

USE superherosightingsTest;

CREATE TABLE organisation ( 
organisationId int primary key auto_increment,
name varchar(70) not null, 
description varchar(300),
address varchar(300),
city varchar(100),
country varchar(100),
postcode varchar(15),
phoneNumber varchar(12),
email varchar(30)
); 

CREATE TABLE superherovillain (
superId int primary key auto_increment,  
name varchar(60) not null, 
description varchar(300),
superpower varchar(100),
isHero boolean not null,
organisationId int,
foreign key (organisationId) references organisation(organisationId)
); 

CREATE TABLE location (
locationId int primary key auto_increment,
name varchar(60) not null, 
description varchar(300),
address varchar(300),
city varchar(100),
country varchar(100),
postcode varchar(15), 
longitude decimal(10, 6),
latitude decimal(10, 6)
);     
    
CREATE TABLE superherovillain_Location(
sightingId int primary key auto_increment, 
superId int not null, 
locationId int not null, 
timeSighted datetime, 
foreign key (superId) references superherovillain(superId), 
foreign key (locationId) references location(locationId)
); 

USE superherosightingsTest;
insert into organisation(name, description, address, city, country, postcode, phoneNumber, email)
values ("Guardians of the Globe", "affiliated with the Global Defense Agency ", "Guardians of the Globe HQ ", "Barcelona", "Spain", "XX3 8YY", 11111111111, "gg@gmail.com"),
("The Justice League", "team of DC superheroes", "The Hall", "Newport", "USA", "12345", 1231231231, "justiceleague123@yahoo.com"),
("The Avengers", "team of Marvel superheroes", "Avengers Tower ", "Manhattan", "USA", "98765", 98798798798, "avengerzz@outlook.com"),
("Brotherhood of Mutants", "mutant terrorist organisation ", "Avengers Tower ", null, null, null, 43543858342, "mutansrulexox@aol.com"),
("Legion of Doom", "group of DC supervillains ", "Hall of Doom", "Newport", "USA", "24680", 24681012146, "doom2468@icloud.com ");

insert into superherovillain(name, description, superpower, ishero, organisationId)
values("Invincible", "Mark Grayson, from the Invincible comics", "Superhuman", true,1 ),
("Batman", "Bruce Wayne from the Batman comics", "Being Rich", true, 2),
("Spider-Man", "Peter Parker, from the Spiderman comics ", "Spider Powers", true, 3),
("Wonder Woman", "Diana Prince, from the Wonder Woman comics", "Superhuman", true, 2),
("Magneto", "Max Eisenhardt from the X-men comics ", "Magnetism Manipulation", false, 4 ),
("The Riddler", "Edward Nashton from the Batman comics ", "Intellect", false, 5),
("Poison Ivy", "Dr Pamela Lillian Isley, from the Batman comics", "Plant Manipulation",false, null);


insert into location(name, address, city, country, postcode, longitude, latitude) 
values ("The Colleseum", 'Piazza del Colosseo 1', 'Rome', 'Italy', '00184', 41.8902, 12.4922),
("Taj Mahal", 'Dharmapuri, Forest Colony, Tajganj', 'Agra', 'India', '282001', 27.1751, 78.0421),
("Christ the Redeemer", "Rua Cosme Velho, 513", "Rio de Janeiro", "Brazil", null, 22.9519, 43.2105);

insert into location(name, city, country, longitude, latitude)
values ("The Great Wall of China", "Beijing", "China", 40.4319, 116.5704),
("Petra", "Ma'an", "Jordan", 30.328611, 35.441944),
("The Pyramids of Giza", "Giza", "Egypt", 29.9792, 31.1342),
("Machu Pichu", "Urubamba Province", "Peru", 13.1631, 72.5450);

insert into superherovillain_location(superid, locationid, timesighted)
values(1,3, '2022-1-01 13:00:00'),
(2,7, '2022-2-28 18:00:00'),
(6,7, '2022-3-1 05:30:00'),
(3,4, '2022-4-07 06:00:00'),
(4,1, '2022-6-22 12:00:00'),
(5,6, '2022-7-25 20:00:00'),
(6,5, '2022-10-11 17:00:00'),
(7,2, '2022-12-15 10:00:00'),
(5,4, '2022-12-16 10:00:00'),
(3,3, '2022-12-17 10:00:00'),
(1,1, '2022-12-18 10:00:00');



