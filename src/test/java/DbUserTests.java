import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.Main;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = Main.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, Film.class, User.class})
class DbUserTests {
    private final UserDbStorage userStorage;
    private User user1 = new User();
    private User user2 = new User();


    @BeforeEach
    void setUp() {
        user1.setBirthday(LocalDate.of(2005, 11, 10));
        user1.setName("Aleks");
        user1.setLogin("Masterpanda");
        user1.setEmail("myfavouritemail@gmail.com");
        user2.setBirthday(LocalDate.of(2006, 2, 12));
        user2.setName("Dasha");
        user2.setLogin("Masterhorse");
        user2.setEmail("anotheremail@gmail.com");
        userStorage.addUser(user1);
        userStorage.addUser(user2);
    }

    @Test
    public void testFindUserById() {
        String name = userStorage.getUser(1).getName();
        assertEquals("Aleks", name);
    }

    @Test
    public void updateUserById() {
        String emailBefore = user1.getEmail();
        user1.setEmail("mynewemail@yandex.ru");
        String emailAfter = user1.getEmail();
        assertEquals("myfavouritemail@gmail.com->mynewemail@yandex.ru",
                            emailBefore + "->" + emailAfter);
    }

    @Test
    public void getAll(){
        ArrayList<User> users = new ArrayList<>(userStorage.getAll());
        String name1 = users.get(0).getName();
        String name2 = users.get(1).getName();
        assertEquals("Aleks Dasha", name1 + " " + name2);
    }

    @Test
    public void containsUser(){
        boolean flag = userStorage.containsUser(user1);
        assertTrue(flag);
    }

    @Test
    public void addFriend(){
        userStorage.addFriend(user1, user2);
        ArrayList<Integer> friendsIds1 = new ArrayList<>(userStorage.getFriendsIds(user1));
        ArrayList<Integer> friendsIds2 = new ArrayList<>(userStorage.getFriendsIds(user2));
        String ids = friendsIds1.get(0) + " " + friendsIds2.get(0);
        assertEquals("2 1", ids);
    }

    @Test
    public void removeFriend(){
        userStorage.addFriend(user1, user2);
        userStorage.removeFriend(user1, user2);
        ArrayList<Integer> friendsIds1 = new ArrayList<>(userStorage.getFriendsIds(user1));
        ArrayList<Integer> friendsIds2 = new ArrayList<>(userStorage.getFriendsIds(user2));
        boolean flag1 = friendsIds1.isEmpty();
        boolean flag2 = friendsIds2.isEmpty();
        boolean flag = flag1 && flag2;
        assertTrue(flag);
    }

    @Test
    public void getFriendsIds(){
        User user3 = new User();
        user3.setBirthday(LocalDate.of(2005, 12, 28));
        user3.setName("Oleg");
        user3.setLogin("Aezakmi");
        user3.setEmail("arbuz@gmail.com");
        userStorage.addUser(user3);
        userStorage.addFriend(user1, user2);
        userStorage.addFriend(user1, user3);
        ArrayList<Integer> friendsIds = new ArrayList<>(userStorage.getFriendsIds(user1));
        String ids = friendsIds.get(0) + " " + friendsIds.get(1);
        assertEquals("2 3", ids);
    }

    @Test
    public void getCommonFriendsIds(){
        User user3 = new User();
        user3.setBirthday(LocalDate.of(2005, 12, 28));
        user3.setName("Oleg");
        user3.setLogin("Aezakmi");
        user3.setEmail("arbuz@gmail.com");
        userStorage.addUser(user3);
        userStorage.addFriend(user1, user2);
        userStorage.addFriend(user3, user2);
        ArrayList<Integer> friendsIds = new ArrayList<>(userStorage.getCommonFriendsIds(user1, user3));
        Integer id = friendsIds.get(0);
        assertEquals(2, id);
    }
}