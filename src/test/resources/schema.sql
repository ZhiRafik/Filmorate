CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255),
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS mpa (
    mpa_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    genre_id INTEGER,
    mpa_id INTEGER,
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id) ON DELETE SET NULL,
    FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS likes (
    user_id BIGINT NOT NULL,
    film_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id)
);

CREATE TABLE IF NOT EXISTS friendship_status (
    friendship_status_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS friends_relationship (
    first_user BIGINT NOT NULL,
    second_user BIGINT NOT NULL,
    friendship_status_id INTEGER,
    PRIMARY KEY (first_user, second_user),
    FOREIGN KEY (friendship_status_id) REFERENCES friendship_status(friendship_status_id)
);


