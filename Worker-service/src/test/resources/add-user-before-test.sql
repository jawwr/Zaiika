insert into users(id, login, name, password, patronymic, surname)
values (99999, 'TEST login', 'name', '$2a$12$DayyaPG1JUBknf3Eg7uOqeuGS8kl/zm69gJLtthLHBxq45eR4B2E.', 'patr',
        'surname');

insert into user_role(user_id, role_id)
values (99999, 1);

insert into places(id, name, owner_id)
values (99999, 'TEST PLACE', 99999);

insert into place_roles(id, name, place_id)
values (99999, 'TEST PLACE ROLE', 99999);

insert into place_roles(id, name, place_id)
VALUES (99998, 'test role for deleting', 17);

insert into workers(id, place_id, place_role_id, user_id)
values (99999, 99999, 99999, 99999);

insert into place_role_permission(place_role_id, permission_id)
values (99999, (select id
                from permissions
                where name = 'MANAGE_WORKER'));

insert into place_role_permission(place_role_id, permission_id)
values (99999, (select id
                from permissions
                where name = 'MANAGE_PLACE_ROLE'));

insert into place_role_permission(place_role_id, permission_id)
values (99999, (select id
                from permissions
                where name = 'VIEW_PERMISSIONS'));