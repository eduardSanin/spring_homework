import dao.impl.DefaultUserDao;
import model.interfaces.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import service.impl.DefaultUserService;
import service.interfaces.UserService;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserServiceTest {
    private static final long USER_ID = 1;
    private static final String EMAIL = "email@mail.com";
    private static final String NAME = "name";
    private static final int PAGE_SIZE = 3;
    private static final int PAGE_NUM = 1;

    @Mock
    private DefaultUserDao userDao;
    @Mock
    private User user;
    @Mock
    private List<User> users;
    @InjectMocks
    private UserService userService = new DefaultUserService();

    @Test
    public void shouldReturnUserForGivenId_whenGetUserById() {
        when(userDao.getUserById(USER_ID)).thenReturn(user);
        User actual = userService.getUserById(USER_ID);
        assertEquals(user, actual);
    }

    @Test
    public void shouldReturnUserForGivenEmail_whenGetUserByEmail() {
        when(userDao.getUserByEmail(EMAIL)).thenReturn(user);
        User actual = userService.getUserByEmail(EMAIL);
        assertEquals(user, actual);
    }

    @Test
    public void shouldReturnUsersForGivenName_whenGetUserByName() {
        when(userDao.getUsersByName(NAME, PAGE_SIZE, PAGE_NUM)).thenReturn(users);
        List<User> actual = userService.getUsersByName(NAME, PAGE_SIZE, PAGE_NUM);
        assertEquals(users, actual);
    }

    @Test
    public void shouldDelegateCallToUserDao_whenCreateUser() {
        userService.create(user);
        verify(userDao).create(user);
    }

    @Test
    public void shouldDelegateCallToUserDao_whenUpdateUser() {
        userService.updateUser(user);
        verify(userDao).updateUser(user);
    }

    @Test
    public void shouldDelegateCallToUserDao_whenDeleteUser() {
        userService.remove(USER_ID);
        verify(userDao).remove(USER_ID);
    }
}