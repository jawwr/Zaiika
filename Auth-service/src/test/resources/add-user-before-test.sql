insert into users(id, login, name, password, patronymic, surname)
values (99999, 'TEST login', 'name', '$2a$12$DayyaPG1JUBknf3Eg7uOqeuGS8kl/zm69gJLtthLHBxq45eR4B2E.', 'patr',
        'surname');

insert into user_role(user_id, role_id)
values ((select id
         from users
         where login = 'TEST login'), 1);

insert into places(id, name, owner_id)
values (99999, 'TEST PLACE', (select id
                              from users
                              where login = 'TEST login'));

insert into place_roles(id, name, place_id)
values (99999, 'TEST PLACE ROLE', (select id
                                   from places
                                   where name = 'TEST PLACE'));

insert into workers(id, place_id, place_role_id, user_id)
values (99999, 99999, 99999, 99999);