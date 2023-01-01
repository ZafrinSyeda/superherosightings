use superherosightings; 

select * from superherovillain; 

select * from superherovillain where ishero = true; 

select * from superherovillain where ishero = false; 

select * from location; 

select * from organisation; 

select sh.name, org.name
from superherovillain sh
join organisation org
on sh.organisationid = org.organisationid; 

select org.organisationId, org.name, org.description, org.address, org.city, org.country,
org.postcode, org.phoneNumber, org.email
from organisation org 
join superherovillain sh
on sh.organisationid = org.organisationid
where sh.superId = 2; 

update superherovillain
set organisationId = null 
where organisationId = 1; 

select * from superherovillain; 

select sh.name, l.name, sl.timesighted
from superherovillain sh
join superherovillain_location sl
on sh.superid = sl.superid 
join location l
on sl.locationid = l.locationid; 

select * from superherovillain_location;  

SELECT org.* 
FROM organisation org 
JOIN superherovillain svh 
ON svh.organisationId = org.organisationId 
WHERE svh.superid = 1; 

select sh.*
from superherovillain sh
join superherovillain_location sl
on sh.superid = sl.superid
where sl.sightingid = 3; 

select l.*
from location l
join superherovillain_location sl
on l.locationid = sl.locationid
where sl.sightingid = 6;

select * 
from superherovillain
where organisationid = 2; 

SELECT l.* 
FROM location l 
JOIN superherovillain_location sl 
ON sl.locationId = l.locationId 
WHERE sl.superId = 6; 

SELECT COUNT(sl.superId)
FROM location l 
JOIN superherovillain_location sl 
ON sl.locationId = l.locationId 
WHERE sl.locationid = 7; 

select * from superherovillain_location 
order by timesighted desc;  