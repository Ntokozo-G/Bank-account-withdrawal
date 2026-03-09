-- Seed data, loaded automatically on startup into the H2 database
-- 5 test accounts that we can use when calling the API, unique accountIds, i could have used UUID but this should do still unique

INSERT INTO accounts (id, name, balance) VALUES (1, 'Ntokozo',  1000.00);
INSERT INTO accounts (id, name, balance) VALUES (2, 'Sanlam',     250.00);
INSERT INTO accounts (id, name, balance) VALUES (3, 'FinTech',   0.00);
INSERT INTO accounts (id, name, balance) VALUES (4, 'Mpilo',   15.00);
INSERT INTO accounts (id, name, balance) VALUES (5, 'Nokwazi',   2.00);
