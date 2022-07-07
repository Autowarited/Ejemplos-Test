
INSERT INTO status (name_status)
VALUES ('confirmado'),
       ('borrador');

INSERT INTO sale_order (number, customer, date, status, category, total)
VALUES ('V-2022-7-1', 1, current_date, 1, 2, 500),
       ('V-2022-7-2', 2, current_date, 1, 2, 500),
       ('V-2022-7-3', 3, current_date, 1, 2, 500),
       ('V-2022-7-4', 4, current_date, 1, 2, 600),
       ('V-2022-7-5', 5, current_date, 1, 2, 800),
       ('V-2022-7-6', 1, current_date, 2, 2, 900),
       ('V-2022-7-7', 3, current_date, 1, 2, 100),
       ('V-2022-7-8', 1, current_date, 2, 2, 1000),
       ('V-2022-7-9', 6, current_date, 1, 2, 100);


