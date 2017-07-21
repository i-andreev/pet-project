package lot;

import com.shareDiscount.domains.LotParam;
import com.shareDiscount.service.impl.LotService;
import com.shareDiscount.service.model.Lot;
import com.shareDiscount.service.persistence.LotRepo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LotServiceTest {

    @InjectMocks
    private LotService lotService = new LotService();

    @Mock
    private LotRepo lotRepositoryMock;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldReturn3Lots() {
        // arrange
        List<Lot> lots = Arrays.asList(new Lot(1L, "Lot1", 11L), new Lot(2L, "Lot2", 12L), new Lot(3L, "Lot3", 13L));

        when(lotRepositoryMock.findAll()).thenReturn(lots);
        // act
        assertThat(lotService.getAll(null)).isNotEmpty();
        assertEquals(lotService.getAll(null).size(), 3);

    }

    @Test
    public void shouldFind2LotsByUserId() {
        long userId = 1;
        // arrange
        List<Lot> lots = Arrays.asList(new Lot(userId, "Lot1", 11L), new Lot(userId, "Lot2", 12L), new Lot(2L, "Lot3", 13L));

        when(lotRepositoryMock.findByUserId(userId)).thenReturn(
                lots.stream()
                        .filter(lot -> userId==lot.getUserId())
                        .collect(Collectors.toList()));
        // act
        assertThat(lotService.getAll(userId)).isNotEmpty();
        assertEquals(lotService.getAll(userId).size(), 2);

    }

    @Test
    public void shouldReturnEmptyListOfLots() {
        // arrange
        List<Lot> lots = new ArrayList<>();

        when(lotRepositoryMock.findAll()).thenReturn(lots);
        // act
        assertThat(lotService.getAll(null)).isEmpty();
    }


    @Test
    public void shouldReturnLotDetails() {
        // arrange
        Optional<Lot> demoLot = Optional.of(new Lot(1L, "Nike Discount till 12 Feb", 1L));
        when(lotRepositoryMock.findByName("Nike Discount till 12 Feb")).thenReturn(demoLot);
        // act
        Optional<LotParam> lotDetails = lotService.findByName("Nike Discount till 12 Feb");
        // assert
        assertEquals(demoLot.get().getName(), lotDetails.get().getName());
        assertEquals(demoLot.get().getLifeTime(), lotDetails.get().getLifeTime());
    }

    @Test
    public void shouldBeEmptyOptionalWhenUserNameNotFound() {
        String lotName = "Adidas Discount till 20 Sep";
        // arrange

        when(lotRepositoryMock.findByName(lotName)).thenReturn(Optional.empty());
        // act
        assertTrue(lotService.findByName(lotName).equals(Optional.empty()));

    }

    @Test
    public void shouldEmptyOptionalWhenUserIdNotFound() {
        long userId = (long) 5;
        // arrange

        when(lotRepositoryMock.findOne(userId)).thenReturn(null);
        // act
        assertThat(lotService.findById(userId).equals(Optional.empty()));
    }
}
