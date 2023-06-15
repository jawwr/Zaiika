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
where id = 99999;

delete
from place_roles
where id = 99999;

delete
from places
where id = 99999;

delete
from tokens
where user_id = 99999;

delete
from user_role
where user_id = 99999;

delete
from users
where id = 99999;