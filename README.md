![ER-diagram in the project Filmorate](https://github.com/ZhiRafik/Filmorate/blob/main/ER_Filmorate.png?raw=true)

ER-diagram:
- Thick arrow indicates one-to-one relationship, whereas the thin one means one-to-many. 
- User can have friends. The pairs of friends are contained in a special table, where every pair contains two ids (first user and second user) and friendship status (confirmed/pending).
- Film contains a list of users' ids who liked the film
