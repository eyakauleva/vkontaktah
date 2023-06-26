CREATE CONSTRAINT user_login_unique IF NOT EXISTS FOR (user:User) REQUIRE user.login IS UNIQUE;

CREATE CONSTRAINT user_id_unique IF NOT EXISTS FOR (user:User) REQUIRE user.id IS UNIQUE;

CREATE CONSTRAINT post_id_unique IF NOT EXISTS FOR (post:Post) REQUIRE post.id IS UNIQUE;