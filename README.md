![ER-diagram in the project Filmorate](https://raw.githubusercontent.com/ZhiRafik/Filmorate/f292001b6e86c3f393a00c3e214c9616c033bd19/ER-Filmorate.jpg "ER-diagram")

ER-diagram:
- Thick arrow indicates one-to-one relationship, whereas the thin one means one-to-many. 
- User has a list of friends. The list contains of a list of pairs, where every pair contains friend's id (user_id) and friendship status (confirmed/pending).
- Film contains a list of users' ids who liked the film
