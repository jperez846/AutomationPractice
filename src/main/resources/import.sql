-- import.sql
-- This file is automatically executed by Hibernate after schema creation
-- ONLY INSERT statements, no CREATE TABLE

-- Clear any existing data (optional)
DELETE FROM PRODUCT;

-- Insert test data
INSERT INTO PRODUCT (id, name, description, price) VALUES (100, 'Widget A', 'Premium widget for testing', 19.99);
INSERT INTO PRODUCT (id, name, description, price) VALUES (102, 'Gadget B', 'Advanced gadget for testing', 45.50);
INSERT INTO PRODUCT (id, name, description, price) VALUES (103, 'Tool C', 'Professional tool for testing', 99.99);