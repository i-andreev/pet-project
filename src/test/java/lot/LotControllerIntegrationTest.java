package lot;

import com.shareDiscount.config.WebAppConfigurationAware;
import com.shareDiscount.domains.LotParam;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LotControllerIntegrationTest extends WebAppConfigurationAware {

    @Resource
    private LotRepo lotRepo;
    @Resource
    private UserRepo userRepo;

    private User user;
    private User user2;

    private final String LOT_NAME_1 = "test-lot1";
    private final String LOT_NAME_2 = "test-lot2";
    private final String LOT_NAME_3 = "test-lot3";
    private final String ROLE_USER = "USER";
    private final Long LOT_LIFE_TIME_1 = 123L;
    private Long LOT_LIFE_TIME_2 = 321L;

    private List<Lot> lotList = new ArrayList<>();

    @Before
    public void beforeLotControllerTest() {

        this.lotRepo.deleteAllInBatch();
        this.userRepo.deleteAllInBatch();

        user = userRepo.save(new User("test-user1", "qaz", ROLE_USER, null));
        user2 = userRepo.save(new User("test-user2", "wsx", ROLE_USER, null));

        lotList.add(lotRepo.save(new Lot(user.getId(), LOT_NAME_1, LOT_LIFE_TIME_1)));
        lotList.add(lotRepo.save(new Lot(user.getId(), LOT_NAME_2, LOT_LIFE_TIME_2)));
        lotList.add(lotRepo.save(new Lot(user2.getId(), LOT_NAME_3, LOT_LIFE_TIME_2)));
    }

    @Test
    public void lotNotFound() throws Exception {
        mockMvc.perform(get("/api/lot/20")
                .content(IntegrationTestUtil.convertObjectToJsonString(new LotParam()))
                .contentType(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void lotGetOne() throws Exception {

        mockMvc.perform(get("/api/lot/" + lotList.get(0).getId())
                .accept(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(lotList.get(0).getId().intValue())))
                .andExpect(jsonPath("name", equalTo(LOT_NAME_1)))
                .andExpect(jsonPath("lifeTime", equalTo(LOT_LIFE_TIME_1.intValue())))
                .andExpect(jsonPath("userId", is(user.getId().intValue())));
    }

    @Test
    public void lotGetAll() throws Exception {

        mockMvc.perform(get("/api/lot")
                .accept(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(lotList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(LOT_NAME_1)))
                .andExpect(jsonPath("$[0].lifeTime", is(LOT_LIFE_TIME_1.intValue())))
                .andExpect(jsonPath("$[0].userId", is(user.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(lotList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(LOT_NAME_2)))
                .andExpect(jsonPath("$[1].lifeTime", is(LOT_LIFE_TIME_2.intValue())))
                .andExpect(jsonPath("$[1].userId", is(user.getId().intValue())))
                .andExpect(jsonPath("$[2].id", is(lotList.get(2).getId().intValue())))
                .andExpect(jsonPath("$[2].name", is(LOT_NAME_3)))
                .andExpect(jsonPath("$[2].lifeTime", is(LOT_LIFE_TIME_2.intValue())))
                .andExpect(jsonPath("$[2].userId", is(user2.getId().intValue())));
    }

    @Test
    public void lotGetAllByUserId() throws Exception {

        mockMvc.perform(get("/api/lot?userId=" + user.getId())
                .accept(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(lotList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(LOT_NAME_1)))
                .andExpect(jsonPath("$[0].lifeTime", is(LOT_LIFE_TIME_1.intValue())))
                .andExpect(jsonPath("$[0].userId", is(user.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(lotList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(LOT_NAME_2)))
                .andExpect(jsonPath("$[1].lifeTime", is(LOT_LIFE_TIME_2.intValue())))
                .andExpect(jsonPath("$[1].userId", is(user.getId().intValue())));

    }

    @Test
    public void lotCreate() throws Exception {
        final String lotName = "test-lot";
        Lot lot = new Lot(user2.getId(), lotName, 1L);
        mockMvc.perform(post("/api/lot")
                .contentType(IntegrationTestUtil.APPLICATION_JSON_UTF8)
                .content(IntegrationTestUtil.convertObjectToJsonString(lot)))
                .andExpect(status().isCreated());

    }

    @Test
    public void updateLot() throws Exception {
        String updatedName = "updated-lot-name";

        LotParam lotParam = new LotParam();
        lotParam.setName(updatedName);
        lotParam.setUserId(2);
        lotParam.setLifeTime(432);

        mockMvc.perform(put("/api/lot/" + lotList.get(0).getId())
                .contentType(IntegrationTestUtil.APPLICATION_JSON_UTF8)
                .content(IntegrationTestUtil.convertObjectToJsonString(lotParam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(lotList.get(0).getId().intValue())))
                .andExpect(jsonPath("name", is(updatedName)))
                .andExpect(jsonPath("lifeTime", is(432)))
                .andExpect(jsonPath("userId", is(2)));

    }

    @Test
    public void lotDelete() throws Exception {

        mockMvc.perform(delete("/api/lot/" + lotList.get(0).getId())
                .contentType(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        // check that after the removal left 2 Lots
        mockMvc.perform(get("/api/lot")
                .accept(IntegrationTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(lotList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(LOT_NAME_2)))
                .andExpect(jsonPath("$[0].lifeTime", is(LOT_LIFE_TIME_2.intValue())))
                .andExpect(jsonPath("$[0].userId", is(user.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(lotList.get(2).getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(LOT_NAME_3)))
                .andExpect(jsonPath("$[1].lifeTime", is(LOT_LIFE_TIME_2.intValue())))
                .andExpect(jsonPath("$[1].userId", is(user2.getId().intValue())));

    }
}
