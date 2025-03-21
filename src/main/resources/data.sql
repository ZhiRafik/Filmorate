INSERT INTO friendship_status (friendship_status_id, name)
KEY (name)
VALUES (DEFAULT, 'CONFIRMED'),
       (DEFAULT, 'PENDING');

INSERT INTO genres (genre_id, name)
KEY (name)
VALUES (DEFAULT, 'COMEDY'),
       (DEFAULT, 'HORROR'),
       (DEFAULT, 'ACTION'),
       (DEFAULT, 'ADVENTURE'),
       (DEFAULT, 'ROMANCE');

INSERT INTO mpa (mpa_id, name)
KEY (name)
VALUES (DEFAULT, 'G'),
       (DEFAULT, 'PG'),
       (DEFAULT, 'PG-13'),
       (DEFAULT, 'R'),
       (DEFAULT, 'NC-17');
