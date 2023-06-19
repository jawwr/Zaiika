delete
from place_role_permission
where place_role_id = 99999;

delete
from tokens
where user_id = (select id
                 from users
                 where login = 'test LOGIN');

delete
from user_role
where user_id = (select id
                 from users
                 where login = 'test LOGIN');

delete
from users
where login = 'test LOGIN';

delete
from workers
where id between 20 and 100000;

delete
from place_roles
where place_id = 99999 or id = 99998;

delete
from places
where id = 99999 or owner_id = 99999;

delete
from tokens
where user_id = 99999;

delete
from user_role
where user_id = 99999;

delete
from users
where id = 99999;