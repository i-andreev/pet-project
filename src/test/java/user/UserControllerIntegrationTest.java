package user;

import com.shareDiscount.config.WebAppConfigurationAware;
import com.shareDiscount.domains.UserParam;
import com.shareDiscount.service.model.Lot;
import com.shareDiscount.service.model.User;
import com.shareDiscount.service.persistence.LotRepo;
import com.shareDiscount.service.persistence.UserRepo;
import org.junit.Before;
import org.junit.Test;
import utils.IntegrationTestUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerIntegrationTest extends WebAppConfigurationAware {


    @Resource
    protected UserRepo userRepo;
    @Resource
    private LotRepo lotRepo;

    private final String USER_NAME_1 = "test-user1";
    private final String USER_NAME_2 = "test-user2";
    private final String ROLE_USER = "USER";
    private List<User> userList = new ArrayList<>();

    @Before
    public void beforeUserControllerTest() {

        this.lotRepo.deleteAllInBatch();
        this.userRepo.deleteAllInBatch();

        userList.add(userRepo.save(new User(USER_NAME_1, "qaz321", ROLE_USER, null)));
        lotRepo.save(new Lot(userList.get(0).getId(), "test-lot", 123L));
        userList.add(userRepo.save(new User(USER_NAME_2, "qaz123", ROLE_USER, null)));
    }

    @Test
    public void userNotFound() throws Exception {
        mockMvc.perform(get("/api/user/5")
                .content(IntegrationTestUtil.convertObjectToJsonString(new UserParam()))
                .contentType(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void userGetOne() throws Exception {

        mockMvc.perform(get("/api/user/" + userList.get(0).getId())
                .accept(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(userList.get(0).getId().intValue())))
                .andExpect(jsonPath("userName", is(USER_NAME_1)));

    }

    @Test
    public void userGetOneWithLots() throws Exception {

        mockMvc.perform(get("/api/user/" + userList.get(0).getId())
                .accept(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(userList.get(0).getId().intValue())))
                .andExpect(jsonPath("userName", is(USER_NAME_1)))
                .andExpect(jsonPath("lots", hasSize(1)))
                .andExpect(jsonPath("lots[0].name", is("test-lot")))
                .andExpect(jsonPath("lots[0].lifeTime", is(123)));


    }

    @Test
    public void userGetAll() throws Exception {

        mockMvc.perform(get("/api/user")
                .accept(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(userList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].userName", is(USER_NAME_1)))
                .andExpect(jsonPath("$[0].role", is(ROLE_USER)))
                .andExpect(jsonPath("$[1].id", is(userList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].userName", is(USER_NAME_2)))
                .andExpect(jsonPath("$[1].role", is(ROLE_USER)));

    }

    @Test
    public void userCreate() throws Exception {
        final String userName = "test-user";
        User user = new User(userName, "456", ROLE_USER, null);
        mockMvc.perform(post("/api/user")
                .contentType(IntegrationTestUtil.APPLICATION_JSON_UTF8)
                .content(IntegrationTestUtil.convertObjectToJsonString(user)))
                .andExpect(status().isCreated());

    }

    @Test
    public void updateUser() throws Exception {
        String updatedName = "updated-user-userName";

        UserParam userParam = new UserParam();
        userParam.setUserName(updatedName);

        mockMvc.perform(put("/api/user/" + userList.get(0).getId())
                .contentType(IntegrationTestUtil.APPLICATION_JSON_UTF8)
                .content(IntegrationTestUtil.convertObjectToJsonString(userParam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(userList.get(0).getId().intValue())))
                .andExpect(jsonPath("userName", is(updatedName)))
                .andExpect(jsonPath("role", is(ROLE_USER)));
    }

    @Test
    public void userDelete() throws Exception {

        mockMvc.perform(delete("/api/user/" + userList.get(0).getId())
                .contentType(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        // check that after the removal left only one User
        mockMvc.perform(get("/api/user")
                .accept(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(userList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[0].userName", is(USER_NAME_2)))
                .andExpect(jsonPath("$[0].role", is(ROLE_USER)));
    }
}
