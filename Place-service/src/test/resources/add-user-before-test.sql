insert into users(id, login, name, password, patronymic, surname)
values (99999, 'TEST login', 'name', '$2a$12$DayyaPG1JUBknf3Eg7uOqeuGS8kl/zm69gJLtthLHBxq45eR4B2E.', 'patr',
        'surname');

insert into user_role(user_id, role_id)
values (99999, 1);

insert into places(id, name, owner_id)
values (99999, 'TEST PLACE', 99999);

insert into sites(id, name, place_id)
values (99999, 'TEST SITE NAME 1', 99999);

insert into sites(id, name, place_id)
values (99998, 'TEST SITE NAME 2', 99999);

insert into menus(id, title, site_id)
values (99999, 'TEST MENU TITLE', 99999);

insert into products(id, title, menu_id, price)
values (99999, 'TEST PRODUCT TITLE', 99999, 200.5);

insert into place_roles(id, name, place_id)
values (99999, 'TEST PLACE ROLE', 99999);

insert into place_roles(id, name, place_id)
VALUES (99998, 'test role for deleting', 17);

insert into workers(id, place_id, place_role_id, user_id)
values (99999, 99999, 99999, 99999);

insert into place_role_permission(place_role_id, permission_id)
values (99999, (select id
                from permissions
                where name = 'VIEW_PLACE'));

insert into place_role_permission(place_role_id, permission_id)
values (99999, (select id
                from permissions
                where name = 'MANAGE_PLACE'));