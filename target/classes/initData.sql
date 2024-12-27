DELETE FROM appointments WHERE appointment_id < 10000;
DELETE FROM customers WHERE customer_id < 10000;
DELETE FROM first_level_divisions where division_id < 10000;
DELETE FROM countries where country_id < 10000;
DELETE FROM users WHERE user_id < 10000;
DELETE FROM contacts where contact_id < 10000;

-- users

INSERT INTO users VALUES(1, 'test', 'test', NOW(), 'script', NOW(), 'script');
INSERT INTO users VALUES(2, 'admin', 'admin', NOW(), 'script', NOW(), 'script');

-- contacts

INSERT INTO contacts VALUES(1,	'Anika Costa', 'acoasta@company.com');
INSERT INTO contacts VALUES(2,	'Daniel Garcia',	'dgarcia@company.com');
INSERT INTO contacts VALUES(3,	'Li Lee',	'llee@company.com');

-- Counties

INSERT INTO countries VALUES(1,	'U.S',	NOW(), 'script', NOW(), 'script');
INSERT INTO countries VALUES(2,	'UK',	NOW(), 'script', NOW(), 'script');
INSERT INTO countries VALUES(3,	'Canada',	NOW(), 'script', NOW(), 'script');

-- First Level divisions

INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Alabama', 1, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Alaska', 54, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Arizona', 02, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Arkansas', 03, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('California', 04, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Colorado', 05, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Connecticut', 06, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Delaware', 07, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('District of Columbia', 08, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Florida', 09, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Georgia', 10, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Hawaii', 52, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Idaho', 11, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Illinois', 12, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Indiana', 13, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Iowa', 14, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Kansas', 15, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Kentucky', 16, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Louisiana', 17, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Maine', 18, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Maryland', 19, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Massachusetts', 20, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Michigan', 21, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Minnesota', 22, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Mississippi', 23, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Missouri', 24, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Montana', 25, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Nebraska', 26, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Nevada', 27, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('New Hampshire', 28, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('New Jersey', 29, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('New Mexico', 30, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('New York', 31, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('North Carolina', 32, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('North Dakota', 33, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Ohio', 34, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Oklahoma', 35, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Oregon', 36, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Pennsylvania', 37, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Rhode Island', 38, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('South Carolina', 39, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('South Dakota', 40, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Tennessee', 41, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Texas', 42, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Utah', 43, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Vermont', 44, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Virginia', 45, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Washington', 46, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('West Virginia', 47, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Wisconsin', 48, NOW(), 'script', NOW(), 'script', 1 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Wyoming', 49, NOW(), 'script', NOW(), 'script', 1 );



INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Alberta', 61, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('British Columbia', 62, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Manitoba', 63, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('New Brunswick', 64, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Newfoundland and Labrador', 72, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Northwest Territories', 60, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Nova Scotia', 65, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Nunavut', 70, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Ontario', 67, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Prince Edward Island', 66, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Québec', 68, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Saskatchewan', 69, NOW(), 'script', NOW(), 'script', 3 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Yukon', 71, NOW(), 'script', NOW(), 'script', 3 );

INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('England', 101, NOW(), 'script', NOW(), 'script', 2 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Wales', 102, NOW(), 'script', NOW(), 'script', 2 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Scotland',103, NOW(), 'script', NOW(), 'script', 2 );
INSERT INTO first_level_divisions(division, division_id, create_date, created_by, last_update, last_updated_by, country_id) VALUES('Northern Ireland', 104, NOW(), 'script', NOW(), 'script', 2 );

-- Customers

INSERT INTO customers VALUES(1, 'Daddy Warbucks', '1919 Boardwalk', '01291', '869-908-1875', NOW(), 'script', NOW(), 'script', 29);
INSERT INTO customers VALUES(2, 'Lady McAnderson', '2 Wonder Way', 'AF19B', '11-445-910-2135', NOW(), 'script', NOW(), 'script', 103);
INSERT INTO customers VALUES(3, 'Dudley Do-Right', '48 Horse Manor ', '28198', '874-916-2671', NOW(), 'script', NOW(), 'script', 60);

-- Appointments

INSERT INTO appointments VALUES(1, 'title', 'description', 'location', 'Planning Session', '2020-05-28 12:00:00', '2024-12-30 13:00:00', NOW(), 'script', NOW(), 'script', 1, 1, 3);
INSERT INTO appointments VALUES(2, 'title', 'description', 'location', 'De-Briefing', '2020-05-29 12:00:00', '2024-12-31 13:00:00', NOW(), 'script', NOW(), 'script', 2, 2, 2);
INSERT INTO appointments VALUES(3, 'Budget Review', 'Quarterly budget evaluation', 'Conference Room A', 'Review', '2024-12-30 09:00:00', '2024-12-30 10:00:00', NOW(), 'script', NOW(), 'script', 1, 1, 1);
INSERT INTO appointments VALUES(4, 'Project Kickoff', 'Kickoff for new project', 'Conference Room B', 'Kickoff Meeting', '2024-12-30 11:00:00', '2024-12-30 12:30:00', NOW(), 'script', NOW(), 'script', 2, 2, 2);
INSERT INTO appointments VALUES(5, 'Team Retrospective', 'Sprint retrospective', 'Zoom', 'Retrospective', '2024-12-30 15:00:00', '2024-12-30 16:00:00', NOW(), 'script', NOW(), 'script', 3, 1, 3);
INSERT INTO appointments VALUES(6, 'Strategy Meeting', 'Discussing strategy for Q1', 'Boardroom', 'Strategy Session', '2024-12-31 09:00:00', '2024-12-31 10:30:00', NOW(), 'script', NOW(), 'script', 1, 2, 3);
INSERT INTO appointments VALUES(7, 'One-on-One', 'Monthly check-in with manager', 'Office', 'Check-in', '2024-12-31 11:00:00', '2024-12-31 11:30:00', NOW(), 'script', NOW(), 'script', 2, 1, 2);
INSERT INTO appointments VALUES(8, 'New Year Goals', 'Set goals for the new year', 'Conference Room C', 'Planning Session', '2024-01-02 10:00:00', '2024-01-02 11:30:00', NOW(), 'script', NOW(), 'script', 3, 2, 1);
INSERT INTO appointments VALUES(9, 'Vendor Meeting', 'Discussing terms with vendors', 'Conference Room D', 'Negotiation', '2024-01-02 14:00:00', '2024-01-02 15:00:00', NOW(), 'script', NOW(), 'script', 1, 1, 2);
INSERT INTO appointments VALUES(10, 'Technical Training', 'Java training session', 'Training Room', 'Training', '2024-01-03 09:00:00', '2024-01-03 11:00:00', NOW(), 'script', NOW(), 'script', 2, 2, 3);
INSERT INTO appointments VALUES(11, 'Customer Feedback', 'Review customer survey results', 'Zoom', 'Feedback Review', '2024-01-03 13:00:00', '2024-01-03 14:30:00', NOW(), 'script', NOW(), 'script', 3, 1, 1);
