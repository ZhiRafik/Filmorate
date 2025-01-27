![ER-diagram in the project Filmorate](https://github.com/ZhiRafik/Filmorate/blob/main/ER-Filmorate.png?raw=true)

ER-diagram:
- Thick arrow indicates one-to-one relationship, whereas the thin one means one-to-many. 
- User can have friends. The pairs of friends are contained in a special table, where every pair contains two ids (first user and second user) and friendship status (confirmed/pending).
- Table "likes" contains information about users' likes. Via pair "user-film" one can find out, who liked which film. PK consists of user_id and filmd_id
