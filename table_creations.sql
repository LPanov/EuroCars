use eurocars;

-- Relation between models and brands and between engines and models is Many to One
-- CREATE TABLE engines (
-- 	id INT PRIMARY KEY AUTO_INCREMENT,
--     type VARCHAR(50) NOT NULL UNIQUE,
--     prod_years varchar(20) NOT NULL,
--     power_kw int NOT NULL,
--     power_hp int AS (power_kw * 1.34102) VIRTUAL,
--     eng_capacity int,
--     car_image varchar(100),
--     model_id int NOT NULL,
--     CONSTRAINT engines_models
-- 		FOREIGN KEY(model_id) REFERENCES models(id)
--         ON DELETE CASCADE
-- );


-- Insert statements for vehicle types, brands and for the bridge table between vehicle_types and brands 
INSERT INTO vehicle_types (name) 
VALUES
	('Car'),
	('Van'),
	('Buses/Trucks'),
	('Motorcycle/ATV/UTV'),
	('VIN');
    
INSERT INTO brands(name)
VALUES
	('AUDI'),
	('BMW'),
	('CHEVROLET'),
	('CITROEN'),
	('OPEL'),
	('HYUNDAI'),
	('IVECO'),
	('FIAT'),
	('FORD'),
	('MERCEDES-BENZ'),
	('PEUGEOT'),
	('RENAULT'),
	('VW'),
    ('DAF'),
	('MAN'),
	('RENAULT TRUCKS'),
	('SCANIA'),
	('VOLVO'),
	('Aplillia'),
	('DUCATI'),
	('HONDA'),
	('KAWASAKI'),
	('KTM'),
	('SUZUKI'),
	('YAMAHA');
    
INSERT INTO vehicle_type_brands (type_id, brand_id) VALUES
-- Car brands (type_id = 1)
(1, 1),   -- AUDI
(1, 2),   -- BMW
(1, 3),   -- CHEVROLET
(1, 4),   -- CITROEN
(1, 5),   -- OPEL
(1, 6),   -- HYUNDAI
(1, 8),   -- FIAT
(1, 9),   -- FORD
(1, 10),  -- MERCEDES-BENZ
(1, 11),  -- PEUGEOT
(1, 12),  -- RENAULT
(1, 13),  -- VW
(1, 18),  -- VOLVO
(1, 21),  -- HONDA
(1, 24),  -- SUZUKI

-- Van brands (type_id = 2)
(2, 4),   -- CITROEN
(2, 6),   -- HYUNDAI
(2, 7),   -- IVECO
(2, 8),   -- FIAT
(2, 9),   -- FORD
(2, 10),  -- MERCEDES-BENZ
(2, 11),  -- PEUGEOT
(2, 12),  -- RENAULT
(2, 13),  -- VW

-- Buses/Trucks brands (type_id = 3)
(3, 6),   -- HYUNDAI
(3, 7),   -- IVECO
(3, 9),   -- FORD
(3, 10),  -- MERCEDES-BENZ
(3, 14),  -- DAF
(3, 15),  -- MAN
(3, 16),  -- RENAULT TRUCKS
(3, 17),  -- SCANIA
(3, 18),  -- VOLVO

-- Motorcycle/ATV/UTV brands (type_id = 4)
(4, 2),   -- BMW
(4, 19),  -- Aplillia
(4, 20),  -- DUCATI
(4, 21),  -- HONDA
(4, 22),  -- KAWASAKI
(4, 23),  -- KTM
(4, 24),  -- SUZUKI
(4, 25);  -- YAMAHA

-- Query to compare brand with vehicle type
SELECT 
    b.name AS Brand, vt.name AS Type
FROM
    brands b
        JOIN
    vehicle_types_brands vtb ON b.id = vtb.brand_id
        JOIN
    vehicle_types vt ON vtb.type_id = vt.id
ORDER BY vt.id;

INSERT INTO models (name, prod_years, brand_id) VALUES
-- Insert models for brand Audi with id 1
('80 B1 Saloon (80, 82)', '1972/05 - 1976/07', 1),
('80 B2 Saloon (811, 813, 814, 819, 853)', '1978/08 - 1987/07', 1),
('90 B2 (813, 814, 853)', '1984/08 - 1987/03', 1),
('90 B3 (893, 894, 8A2)', '1987/04 - 1991/09', 1),
('100 C1 Saloon (801, 803, 805, 807, 811, 813, F104)', '1968/11 - 1976/07', 1),
('100 C2 Avant (435, 436)', '1977/07 - 1983/02', 1),
('A1 (8X1, 8XK)', '2010/05 - 2018/10', 1),
('A2 (8Z0)', '2000/02 - 2005/08', 1),
('A3 (8L1)', '1996/09 - 2003/05', 1),
('A4 Allroad B8 (8KH)', '2009/04 - 2016/05', 1),
('A4 B5 (8D2)', '1994/11 - 2001/09', 1),
('A4 B7 (8EC)', '2004/11 - 2008/06', 1),
('A4 B8 (8K2)', '2007/11 - 2015/12', 1),
('A5 (8T3)', '2007/06 - 2017/01', 1),
('A6 C4 (4A2)', '1994/06 - 1997/10', 1),
('A6 C5 (4B2, 4B4)', '1997/01 - 2005/01', 1),
('A6 C6 (4F2)', '2004/05 - 2011/03', 1),
('A6 C7 (4G2, 4GC)', '2010/11 - 2018/09', 1),
('A6 C8 (4A2)', '2018/02 - n/a', 1),
('A7 Sportback (4KA)', '2017/10 - n/a', 1),
('A8 D2 (4D2, 4D8)', '1994/03 - 2002/09', 1),
('Q2 (GAB, GAQ)', '2016/06 - n/a', 1),
('Q3 (F8B)', '2018/07 - n/a', 1),
('Q5 (8UN)', '2025/01 - n/a', 1),
('Q6 (GFB)', '2024/04 - n/a', 1), 
('Q7 (4LB)', '2006/03 - 2015/08', 1),
('Q8 (4MN, 4MT)', '2018/02 - n/a', 1),
('R8 (422, 423)', '2007/04 - 2015/07', 1);

INSERT INTO models (name, prod_years, brand_id) VALUES
-- Model for brand_id 2(BMW)
('1 (E81)', '2006/09 - 2012/09', 2),
('2 Active Tourer (F45)', '2013/11 - 2021/10', 2),
('3 (E21)', '1975/06 - 1984/03', 2),
('3 (E30)', '1982/09 - 1992/01', 2),
('3 (E36)', '1990/09 - 1998/11', 2),
('3 (E46)', '1997/12 - 2005/05', 2),
('4 Convertible (F33, F83)', '2013/10 - 2020/07', 2),
('5 (E12)', '1972/03 - 1981/06', 2),
('5 (E28)', '1981/05 - 1987/12', 2),
('5 (E34)', '1987/06 - 1995/12', 2),
('5 (E39)', '1995/09 - 2003/06', 2),
('5 (E60)', '2001/12 - 2010/03', 2),
('6 (E24)', '1975/10 - 1989/04', 2),
('6 (E63)', '2003/09 - 2010/08', 2),
('7 (E23)', '1977/05 - 1986/09', 2),
('7 (E32)', '1985/03 - 1994/09', 2),
('7 (E38)', '1994/03 - 2001/11', 2),
('8 (E31)', '1990/01 - 1999/12', 2),
('315 Convertible', '1934/04 - 1937/06', 2),
('319 Convertible', '1934/12 - 1937/03', 2),
('320 Convertible', '1937/02 - 1938/12', 2),
('X1 (E84)', '2009/03 - 2015/06', 2),
('X2 (F39)', '2017/11 - 2023/10', 2),
('X3 (E83)', '2003/09 - 2011/12', 2),
('X4 (F26)', '2014/04 - 2018/03', 2),
('X5 (E53)', '2000/01 - 2006/12', 2),
('X6 (E71, E72)', '2007/06 - 2014/07', 2),
('X7 (G07)', '2019/03 - n/a', 2);

INSERT INTO models (name, prod_years, brand_id) VALUES
-- Models for Chevrolet(id 3)
('ASTRA Hatchback', '1998/09 - 2011/12', 3),
('ASTRA Saloon', '1999/03 - 2011/12', 3),
('AVEO / KALOS Hatchback (T200)', '2003/05 - 2008/05', 3),
('BOLT', '2016/08 - n/a', 3),
('CAMARO', '2010/01 - n/a', 3),
('CAPTIVA (C100, C140)', '2006/06 - n/a', 3),
('CAVALIER Convertible', '1989/10 - 1991/09', 3),
('CHEVY Saloon', '2002/08 - 2006/09', 3),
('CORSA Estate', '1997/08 - 2002/07', 3),
('CORVETTE', '1991/10 - 1997/04', 3),
('CRUZE (J300)', '2009/05 - n/a', 3),
('EL CAMINO Standard Cab Pickup', '1978/09 - 1982/12', 3);

INSERT INTO models (name, prod_years, brand_id) VALUES
-- Models for Citroen(4)
('BERLINGO (ER_, EC_)', '2018/06 - n/a', 4),
('C1 (PM_, PN_)', '2005/06 - 2014/09', 4),
('C15 Box Body/MPV (VD_)', '1984/10 - 2005/12', 4),
('C2 (JM_)', '2003/07 - 2012/09', 4),
('C3 I (FC_, FN_)', '2002/02 - 2012/07', 4),
('C3 II (SC_)', '2009/09 - 2016/09', 4),
('C3 III (SX)', '2016/07 - n/a', 4),
('C3 IV (CC_, CB_)', '2024/01 - n/a', 4),
('C4 I (LC_)', '2004/11 - 2012/11', 4),
('C4 II (NC_)', '2009/11 - n/a', 4),
('C4 III (BA_, BB_, BC_)', '2020/10 - n/a', 4),
('C5 I (DC_)', '2001/03 - 2004/08', 4),
('C5 II (RC_)', '2004/09 - 2008/09', 4),
('C5 III (RD_)', '2008/02 - 2017/05', 4),
('C6 (TD_)', '2005/09 - 2012/12', 4),
('C8 (EA_, EB_)', '2002/07 - n/a', 4);

-- Inserting models for Opel (brand_id = 5)
INSERT INTO models (name, prod_years, brand_id) VALUES
('ASTRA F Estate (T92)', '1991/09 - 1998/01', 5),
('ASTRA G CLASSIC (T98)', '2004/05 - 2009/07', 5),
('ASTRA G Estate (T98)', '1998/02 - 2004/07', 5),
('ASTRA H (A04)', '2004/01 - 2014/05', 5),
('ASTRA H Estate (A04)', '2004/08 - 2014/05', 5),
('ASTRA J (P10)', '2009/09 - 2015/10', 5),
('ASTRA K (B16)', '2015/06 - 2022/12', 5),
('ASTRA L (OV5)', '2021/10 - n/a', 5),
('CORSA A TR (S83)', '1982/09 - 1993/03', 5),
('CORSA B (S93)', '1993/03 - 2002/08', 5),
('CORSA C (X01)', '2000/09 - 2009/12', 5),
('CORSA D (S07)', '2006/07 - 2014/08', 5),
('CORSA E (X15)', '2014/09 - n/a', 5),
('CORSA F (P2JO)', '2019/07 - n/a', 5),
('FRONTERA (OV24)', '2024/07 - n/a', 5),
('INSIGNIA A (G09)', '2008/07 - 2017/03', 5),
('INSIGNIA B Grand Sport (Z18)', '2017/03 - n/a', 5),
('KADETT A', '1962/09 - 1965/08', 5),
('KADETT B Estate', '1971/08 - 1973/07', 5),
('KADETT C', '1973/08 - 1979/07', 5),
('KADETT D (31_-34_, 41_-44_)', '1979/08 - 1984/08', 5),
('KADETT E (T85)', '1984/09 - 1992/05', 5),
('OMEGA A Estate (V87)', '1986/09 - 1994/04', 5),
('OMEGA B (V94)', '1994/03 - 2003/07', 5),
('TIGRA (S93)', '1994/07 - 2000/12', 5),
('VECTRA A (J89)', '1988/09 - 1995/11', 5),
('VECTRA B (J96)', '1995/09 - 2002/07', 5),
('VECTRA C (Z02)', '2002/04 - 2009/01', 5);

select * from brands order by id;    
select * from models;
    
-- insert into engines (type, prod_years, power_kw, eng_capacity, model_id)
-- VALUES 
-- 	('1.6', '1996/09 - 2003/-5', 74, 1596, 1);

INSERT INTO engines (car_img, eng_capacity, kW, prod_years, type, model_id) 
VALUES
	('https://i.ibb.co/CsmzSmn8/image.png', 1596, 74, '1996/09 - 2003/-5', '1.6', 9),
	('https://i.ibb.co/CsmzSmn8/image.png', 1781, 110, '1996/12 - 2003/05', '1.8 T', 9),
	('https://i.ibb.co/CsmzSmn8/image.png', 1896, 66, '1996/09 - 2001/07', '1.9 TDI', 9);
    
select * from engines;



-- create tables for part categories
CREATE TABLE part_categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    parent_category_id INT,
    image_url VARCHAR(100),
    CONSTRAINT part_categories_part_categories_parent_category_id FOREIGN KEY (parent_category_id)
        REFERENCES part_categories (id)
);

INSERT INTO part_categories (name, image_url)
-- Main categories
VALUES
	('Filters', 'https://i.ibb.co/5gry7FB6/filters.png'),
	('Engine', 'https://i.ibb.co/YFJ6803P/engine.png'),
	('Wheel suspension', 'https://i.ibb.co/6JVKPLYX/suspension.png'),
	('Vehicle shock absorption', 'https://i.ibb.co/60qDKyg1/absorber.png'),
	('Ignition/glow system', 'https://i.ibb.co/5W2ncxw2/ignition.png'),
	('Drive', 'https://i.ibb.co/zVYV6L8d/drive.png'),
	('Brake system', 'https://i.ibb.co/QvGpt6XS/brakes.png'),
	('Steering system', 'https://i.ibb.co/rGjV18Cb/steering.png'),
	('Cooling system', 'https://i.ibb.co/6JZNsddX/cooling.png'),
	('Heating/ventilation/air-conditioning', 'https://i.ibb.co/Xx2brkW9/heating.png'),
	('Vehicle equipment/Accessories', 'https://i.ibb.co/7dLm0c8n/accessories.png'),
	('Ignition/Glow system', 'https://i.ibb.co/5W2ncxw2/ignition.png'),
	('Intake/exhaust system', 'https://i.ibb.co/0jLVqzC0/intake.png'),
	('Fuel feed system', 'https://i.ibb.co/B2LWPhct/fuel.png'),
	('Body parts', 'https://i.ibb.co/d4B234Kb/body.png'),
	('Glass/ window', 'https://i.ibb.co/yFhb4Prx/glass.png'),
	('Tunning', 'https://i.ibb.co/TDCPcL68/tunning.png'),
	('Tyres', 'https://i.ibb.co/spHdnHkx/tyres.png');

INSERT INTO part_categories (name, parent_category_id, image_url) VALUES
-- Subcategories under Filters category
	('Oil filter/casing/gasket', 1, NULL),
	('Fuel filter/casing/gasket', 1, NULL),
	('Air filter/casing/gasket', 1, NULL),
	('Cabin filter/casing/gasket', 1, NULL),
	('Hydraulic Filter', 1, NULL),
	('Filter Set', 1, NULL);

INSERT INTO part_categories (name, parent_category_id, image_url) VALUES
-- Sub-subcategories under 'Oil filter/casing/gasket' (parent_category_id = 19)
('Oil filter', 19, NULL),
('Cap, oil filter housing', 19, NULL),
('Gasket, oil filter housing', 19, NULL),
('Seal Ring, oil drain plug', 19, NULL),
('Screw Plug, oil sump', 19, NULL),

-- Sub-subcategories under 'Fuel filter/casing/gasket' (parent_category_id = 20)
('Fuel filter', 20, NULL),

-- Sub-subcategories under 'Air filter/casing/gasket' (parent_category_id = 21)
('Air filter', 21, NULL),
('Intake Hose, air filter', 21, NULL),
('Air Filter Housing Cover', 21, NULL),
('Seal, air filter housing', 21, NULL),
('Sports Air Filter System', 21, NULL),

-- Sub-subcategories under 'Cabin filter/casing/gasket' (parent_category_id = 22)
('Filter, cabin air', 22, NULL),

-- Sub-subcategories under 'Hydraulic Filter' (parent_category_id = 23)
('Hydraulic Filter, automatic transmission', 23, NULL),
('Hydraulic Filter, steering', 23, NULL),
('Parts Kit, automatic transmission oil change', 23, NULL),
('Hydraulic Filter Kit, automatic transmission', 23, NULL),

-- Sub-subcategories under 'Filter Set' (parent_category_id = 24)
('Filter Set', 24, NULL),
('Parts Set, maintenance service', 24, NULL);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Subcategories under Engine (id = 2)
('Engine block', 2),
('Cylinder head', 2),
('Engine lubrication', 2),
('Timing mechanism', 2);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Sub-subcategories under 'Engine block' (parent_category_id = 115)
('Crankshaft/Bearing shells', 115),
('Pistons/Rings', 115),
('Connecting rod', 115),
('PTO shaft', 115),
('Fly wheel', 115),
('Crankcase', 115),
('Frost plug', 115),

-- Sub-subcategories under 'Cylinder head' (parent_category_id = 116)
('Cylinder head/elements', 116),
('Cylinder head gasket', 116),
('Cylinder head-inlet/exhaust side', 116),
('Rocker cover', 116),

-- Sub-subcategories under 'Engine lubrication' (parent_category_id = 117)
('Oil pump/gasket/drive', 117),
('Oil sump/gasket', 117),
('Oil pressure switch', 117),
('Oilmeter', 117),
('Oil filler cap', 117),
('Oil pipes', 117),
('Engine Oil', 117),

-- Sub-subcategories under 'Timing mechanism' (parent_category_id = 118)
('Timing belts', 118),
('Timing belt tensioner/guide', 118),
('Valves/Guides', 118),
('Valve control', 118),
('Valve leak stopper/gasket', 118),
('Timing gear chain/tightening/guiding', 118),
('Distribution shaft', 118),
('Gear wheels', 118),
('Timing cover/case', 118);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Subcategories under Wheel suspension (id = 3)
('Track control arm', 3),
('Knuckle', 3),
('Stabilizer', 3),
('Bar links', 3),
('Wheel bearing/Wheel hub', 3),
('Repair Kit, wheel suspension', 3),
('Suspension beam / elements', 3),
('Article Search via Graphic', 3);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Subcategories under Vehicle shock absorption (id = 4)
('Shock absorbers', 4),
('Springs', 4);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Sub-subcategories under 'Shock absorbers' (parent_category_id = 154)
('Sports shock absorber', 154),
('Suspension Kit, shock absorber', 154),
('Suspension Kit, springs/shock absorbers', 154),
('Shock absorber', 154),

-- Sub-subcategories under 'Springs' (parent_category_id = 155)
('Coil spring', 155),
('Spring Seat', 155),
('Suspension springs set', 155),
('Suspension Kit, shock absorber', 155),
('Suspension Kit, springs/shock absorbers', 155),
('Spring washer', 155);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Subcategories under Ignition system (id = 5)
('Control unit', 5),
('Ignition coil', 5),
('Spark plugs/Glow plugs', 5);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Sub-subcategories under 'Control unit' (parent_category_id = 166)
('Relay, glow plug system', 166),
('Control Unit, ignition system', 166),

-- Sub-subcategories under 'Ignition coil' (parent_category_id = 167)
('Ignition Coil', 167),

-- Sub-subcategories under 'Spark plugs/Glow plugs' (parent_category_id = 168)
('Spark plug', 168);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Subcategories under Drive system (id = 6)
('Clutch', 6),
('Differential', 6),
('Gearbox', 6);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Sub-subcategories under 'Clutch' (parent_category_id = 173)
('Clutch set', 173),
('Clutch elements', 173),
('Clutch steering', 173),

-- Sub-subcategories under 'Differential' (parent_category_id = 174)
('Differential body', 174),
('Differential/axle-shaft gaskets', 174),

-- Sub-subcategories under 'Gearbox' (parent_category_id = 175)
('Manual gearbox', 175),
('Automatic gearbox', 175),
('Engine/gearbox suspension', 175);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Subcategories under Brakes (id = 7)
('Disc brakes', 7),
('Drum brakes', 7);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Sub-subcategories under 'Disc brakes' (parent_category_id = 184)
('Brake pad', 184),
('Brake discs', 184),
('Brake disc caliper', 184),
('Brake disc spare parts', 184),
('Brakes kit', 184),

-- Sub-subcategories under 'Drum brakes' (parent_category_id = 185)
('Brake drum cylinder', 185),
('Brake drum spare parts', 185),
('Brakes kit', 185);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Subcategories under Steering system (id = 8)
('Transmission devices', 8),
('Steering rack', 8);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Sub-subcategories under 'Transmission devices' (parent_category_id = 194)
('Steering rods', 194),
('Article Search via Graphic', 194),

-- Sub-subcategories under 'Steering rack' (parent_category_id = 195)
('Steering rack', 195),
('Steering gear shields', 195),
('Steering gear gaskets', 195),
('Steering gear fastening', 195);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Subcategories under Cooling system (id = 9, assuming)
('Water pump', 9),
('Engine radiator', 9),
('Fan', 9),
('Thermostat', 9);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Sub-subcategories under 'Water pump' (parent_category_id = 202)
('Water pump', 202),
('Water pump gasket', 202),
('Water Pump & Timing Belt Kit', 202),

-- Sub-subcategories under 'Engine radiator' (parent_category_id = 203)
('Engine radiator', 203),
('Cap, radiator', 203),
('Seal Ring, radiator cap bolt', 203),
('Mounting, radiator', 203),

-- Sub-subcategories under 'Fan' (parent_category_id = 204)
('Fan, engine cooling', 204),
('Electric Motor, radiator fan', 204),
('Support, radiator fan', 204),
('Control Unit, electric fan (engine cooling)', 204),
('Cable Repair Kit, radiator fan control unit', 204),

-- Sub-subcategories under 'Thermostat' (parent_category_id = 205)
('Thermostat, coolant', 205),
('Thermostat Housing', 205),
('Thermostat gasket', 205),
('Seal, thermostat', 205);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Subcategories under Heating/Ventilation/Air-conditioning (id = 10)
('Air-conditioning', 10),
('Heating/Ventilation', 10);

INSERT INTO part_categories (name, parent_category_id) VALUES
-- Sub-subcategories under 'Air-conditioning' (parent_category_id = 222)
('Air conditioning condenser', 222),
('Control unit, air conditioner', 222),
('Air-conditioning compressor/Elements', 222),
('Dryer, air conditioner', 222),
('Fan, air conditioner', 222),
('Hoses, air conditioner', 222),
('Evaporator, air conditioning', 222),
('Valve, air conditioner', 222),

-- Sub-subcategories under 'Heating/Ventilation' (parent_category_id = 223)
('Filter, interior air', 223),
('Ventilator/Fan', 223),
('Heater/elements', 223),
('Control/Electricity', 223),
('Pits/Ducts', 223);

select * from part_categories;

