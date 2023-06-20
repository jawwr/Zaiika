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
where place_id = 99999
   or id = 99998;

delete
from ingredients
where title = 'TEST INGREDIENT TITLE 1'
   or title = 'TEST INGREDIENT TITLE 2';

delete
from product_modification
where title = 'TEST PRODUCT MODIFICATION';

delete
from modification_category
where title = 'TEST MODIFICATION CATEGORY TITLE';

delete
from products
where menu_id = 99999;

delete
from menus
where site_id = 99999;

delete
from sites
where place_id = 99999;

delete
from places
where id = 99999
   or owner_id = 99999;

delete
from tokens
where user_id = 99999;

delete
from user_role
where user_id = 99999;

delete
from users
where id = 99999;