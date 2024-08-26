insert into tasklist.users (name, username, password)
values ('Dmitry Kupzov', 'kupzovd@gmail.com', '$2a$10$UJbYeof9F6xP3cCt5SeV3urceVbdsRntr..n1XdKAefmTu7NX8dPi'),
       ('Oleg Orlov', 'oreol@gmail.com', '$2a$10$UJbYeof9F6xP3cCt5SeV3urceVbdsRntr..n1XdKAefmTu7NX8dPi');

insert into tasks (title, description, status, expiration_date)
values ('Do homework', 'Write lecture about spring-boot', 'IN_PROGRESS', '2024-08-29 12:00:00'),
       ('Read book', 'read 10 pages about concurrency in Java', 'TODO', '2024-08-30 11:00:00'),
       ('Call Joy', 'discuss deploy Product-delivery app', 'DONE', '2024-08-28 10:00:00'),
       ('Clean flat', null, 'TODO', null);

insert into tasklist.users_tasks (task_id, user_id)
values (1, 2),
       (2, 2),
       (3, 2),
       (4, 1);

insert into tasklist.users_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER')