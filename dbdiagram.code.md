```
// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs
Table genres {
  id bigint [primary key]
  name varchar [not null]
}

Table mpa {
  id bigint [primary key]
  name varchar [not null]
}

Table directors {
  id bigint [primary key]
  name varchar [not null]
}

Table films {
  id bigint [primary key]
  name varchar [not null]
  description varchar [not null]
  release_date date [not null]
  duration bigint [not null]
  mpa_id bigint
  director_id bigint
}

Table films_genres {
  id bigint [primary key]
  film_id bigint [not null]
  genre_id bigint [not null]
}

Table film_likes {
  film_id bigint [primary key]
  user_id bigint [primary key]
}

Table users {
  id bigint [primary key]
  email varchar [not null]
  login varchar [not null]
  name varchar
  birthday datetime [not null]
}

Table friend_links {
  id bigint [primary key]
  user_id bigint [not null]
  friend_id bigint [not null]
  is_approved bool
}

Table reviews {
  id bigint [primary key]
  content varchar(255) [not null]
  is_positive boolean [not null]
  user_id bigint [not null]
  film_id bigint [not null]
}

Table reviews_marks {
  id bigint [primary key]
  review_id bigint [not null]
  user_id bigint [not null]
  mark_type int [not null]
}

Table events {
  id bigint [primary key]
  timestamp timestamp [not null]
  user_id bigint [not null]
  event_type varchar [not null]
  operation varchar [not null]
  entity_id bigint [not null]
}

Ref: films.mpa_id > mpa.id
Ref: films.director_id > directors.id
Ref: films_genres.film_id > films.id
Ref: films_genres.genre_id > genres.id
Ref: film_likes.film_id > films.id
Ref: film_likes.user_id > users.id
Ref: friend_links.user_id > users.id
Ref: friend_links.friend_id > users.id
Ref: reviews.film_id > films.id
Ref: reviews.user_id > users.id
Ref: reviews_marks.review_id > reviews.id
Ref: reviews_marks.user_id > users.id
Ref: events.user_id > users.id
```
