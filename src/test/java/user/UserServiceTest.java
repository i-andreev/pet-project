package user;

import com.shareDiscount.domains.UserParam;
import com.shareDiscount.service.impl.UserService;
import com.shareDiscount.service.model.User;
import com.shareDiscount.service.persistence.UserRepo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService = new UserService();

    @Mock
    private UserRepo userRepositoryMock;

    @Mock

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldReturn3Users() {
        // arrange
        List<User> users = Arrays.asList(new User("Tom1", "qwerty", "USER", null), new User("Tom2", "1234", "USER", null), new User("Tom3", "qwerty1", "USER", null));

        when(userRepositoryMock.findAll()).thenReturn(users);
        // act
        assertThat(userService.getAll()).isNotEmpty();
        assertEquals(userService.getAll().size(), 3);

    }

    @Test
    public void shouldReturnEmptyListOfUsers() {
        // arrange
        List<User> users = new ArrayList<>();

        when(userRepositoryMock.findAll()).thenReturn(users);
        // act
        assertThat(userService.getAll()).isEmpty();
        assertThat(userService.getAll().containsAll(users));

    }


    @Test
    public void shouldReturnUserDetails() {
        // arrange
        Optional<User> demoUser = Optional.of(new User("exampleUserNAme", "qwerty", "USER", null));
        when(userRepositoryMock.findByUserName("exampleUserNAme")).thenReturn(demoUser);
        // act
        Optional<UserParam> userDetails = userService.findByName("exampleUserNAme");
        // assert
        assertEquals(demoUser.get().getUserName(), userDetails.get().getUserName());
    }

    @Test
    public void shouldBeEmptyOptionalWhenUserNameNotFound() {
        String userName = "Vasily";
        // arrange

        when(userRepositoryMock.findByUserName(userName)).thenReturn(Optional.empty());
        // act
        assertTrue(userService.findByName(userName).equals(Optional.empty()));

    }

    @Test
    public void shouldEmptyOptionalWhenUserIdNotFound() {
        long userId = (long) 5;
        // arrange

        when(userRepositoryMock.findOne(userId)).thenReturn(null);
        // act
        assertThat(userService.findById(userId).equals(Optional.empty()));
    }



}
