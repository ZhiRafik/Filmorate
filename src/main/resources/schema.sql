CREATE TABLE IF NOT EXISTS genres
(
    id   BIGINT       NOT NULL,
    name varchar(255) NOT NULL,
    CONSTRAINT pk_genre_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS mpa
(
    id     BIGINT       NOT NULL,
    name   varchar(255) NOT NULL,
    CONSTRAINT pk_mpa_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS directors
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name varchar(255) NOT NULL,
    CONSTRAINT pk_director_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS films
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    name         varchar(255)          NOT NULL,
    description  varchar(200)          NOT NULL,
    release_date DATE                  NOT NULL,
    duration     BIGINT                NOT NULL,
    mpa_id       BIGINT,
    director_id  BIGINT,
    CONSTRAINT pk_film_id PRIMARY KEY (id),
    FOREIGN KEY (mpa_id) REFERENCES mpa (id) ON DELETE SET NULL,
    FOREIGN KEY (director_id) REFERENCES directors (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS films_genres
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    film_id  BIGINT                NOT NULL,
    genre_id BIGINT                NOT NULL,
    CONSTRAINT pk_films_genres_id PRIMARY KEY (id),
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    email    varchar(255)          NOT NULL,
    login    varchar(255) UNIQUE   NOT NULL,
    name     varchar(255),
    birthday DATE                  NOT NULL,
    CONSTRAINT pk_user_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS film_likes (
     film_id BIGINT,
     user_id BIGINT,
     PRIMARY KEY (film_id, user_id),
     FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friend_links
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    user_id         BIGINT                NOT NULL,
    friend_id       BIGINT                NOT NULL,
    is_approved     BOOL DEFAULT FALSE,
    CONSTRAINT pk_friend_links_id PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    content     varchar(255)          NOT NULL,
    is_positive boolean               NOT NULL,
    user_id     BIGINT                NOT NULL,
    film_id     BIGINT                NOT NULL,
    CONSTRAINT pk_reviews PRIMARY KEY (id),
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews_marks
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    review_id BIGINT                NOT NULL,
    user_id   BIGINT                NOT NULL,
    mark_type INT                   NOT NULL,
    CONSTRAINT pk_reviews_marks PRIMARY KEY (id),
    FOREIGN KEY (review_id) REFERENCES reviews (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS EVENTS
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    timestamp BIGINT                NOT NULL,
    user_id   BIGINT                NOT NULL,
    event_type varchar(6)            NOT NULL,
    operation varchar(6)            NOT NULL,
    entity_id BIGINT                NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);