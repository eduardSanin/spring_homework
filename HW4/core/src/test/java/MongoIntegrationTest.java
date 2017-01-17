import dao.interfaces.UserAccountDao;
import dao.interfaces.UserDao;
import model.impl.mongo.UserAccount;
import model.impl.mongo.UserEntry;
import model.interfaces.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@ActiveProfiles("mongo")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-context.xml"})
public class MongoIntegrationTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserAccountDao userAccountDao;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() {
        mongoTemplate.dropCollection(UserEntry.class);
    }

    @Test
    public void shouldCreateUser() {
        UserEntry user = new UserEntry();
        user.setName("ed");
        user.setEmail("email@mail.ru");
        userDao.create(user);
        assertThat(user).isEqualTo(userDao.getUserById(user.getUserId()));
    }

    @Test
    public void shouldRemoveUser() {
        UserEntry user = new UserEntry();
        user.setName("ed");
        userDao.create(user);
        assertThat(user).isEqualTo(userDao.getUserById(user.getUserId()));
        userDao.remove(user.getUserId());
        assertThat(userDao.getUserById(user.getUserId())).isNull();
    }

    @Test
    public void shouldReturnUserByEmail() {
        UserEntry user = new UserEntry();
        user.setName("ed");
        user.setEmail("email@mail.ru");
        userDao.create(user);
        assertThat(userDao.getUserByEmail(user.getEmail())).isEqualTo(user);
    }

    @Test
    public void shouldReturnTwoUsersByName() {
        UserEntry user = new UserEntry();
        user.setName("ed");
        userDao.create(user);
        User user2 = new UserEntry();
        user2.setEmail("mail@mail.ru");
        user2.setName("ed");
        userDao.create(user2);
        List<User> returnedUsers = userDao.getUsersByName(user.getName(), 3, 0);
        assertThat(returnedUsers).hasSize(2);
        assertThat(returnedUsers).containsExactly(user, user2);
    }

    @Test
    public void shouldReturnOneUserByName() {
        UserEntry user = new UserEntry();
        user.setName("ed");
        userDao.create(user);
        User user2 = new UserEntry();
        user2.setEmail("mail@mail.ru");
        user2.setName("ed");
        userDao.create(user2);
        List<User> returnedUsers = userDao.getUsersByName(user.getName(), 3, 1);
        assertThat(returnedUsers).hasSize(1);
    }

    @Test
    public void shouldCreateUserAccount() {
        UserEntry user = new UserEntry();
        user.setName("ed");
        user.setEmail("email@mail.ru");
        userDao.create(user);
        UserAccount userAccount = new UserAccount();
        userAccount.setUser(user);
        userAccount.setPrePaidAmount(BigDecimal.TEN);
        userAccountDao.createUserAccount(userAccount);
        assertThat(userAccountDao.getUserAccountByUserId(userAccount.getUser().getUserId())).isEqualTo(userAccount);
    }

    @Test
    public void shouldUpdateUserAccount() {
        UserEntry user = new UserEntry();
        user.setName("ed");
        user.setEmail("email@mail.ru");
        userDao.create(user);
        UserAccount userAccount = new UserAccount();
        userAccount.setUser(user);
        userAccount.setPrePaidAmount(BigDecimal.TEN);
        userAccountDao.createUserAccount(userAccount);
        assertThat(userAccountDao.getUserAccountByUserId(userAccount.getUser().getUserId())).isEqualTo(userAccount);
        userAccount.setPrePaidAmount(BigDecimal.ONE);
        userAccountDao.updateUserAccount(userAccount);
        assertThat(userAccountDao.getUserAccountByUserId(userAccount.getUser().getUserId())).isEqualTo(userAccount);
    }

}
