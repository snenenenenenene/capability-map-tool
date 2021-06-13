package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.StatusDAL;

/**
*
* @author Bavo Van Meel
*
*/
@AutoConfigureMockMvc
@SpringBootTest
public class StatusServiceTest {
	
	@SuppressWarnings("unused")
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private StatusService statusService;
	
	@MockBean
	private StatusDAL statusDAL;
	
	@SpyBean
	private StatusService spyStatusService;
	
	private Status status;
	private List<Status> statusList;
	private Optional<Status> optionalStatus;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@BeforeEach
	public void init() {
		status = new Status(1, LocalDate.of(2021, 8, 10));
		statusList = List.of(new Status(1, LocalDate.of(2021, 8, 10)),
				new Status(2, LocalDate.of(2021, 8, 11)));
		optionalStatus = Optional.of(status);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusService);
		assertNotNull(statusDAL);
		assertNotNull(status);
		assertNotNull(statusList);
		assertNotNull(optionalStatus);
	}
	
	@Test
	void should_throwNoSuchElementException_whenSavedInputIsNull() {
		LocalDate falseStatusDate = null;
		String expected = "No value present";
		
		BDDMockito.when(statusService.save(falseStatusDate))
			.thenThrow(new NoSuchElementException("No value present"));

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> statusService.save(falseStatusDate));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwDuplicateValueException_whenSavedStatusNameExists() {
		LocalDate falseValidityPeriod = status.getValidityPeriod();
		String expected = "No value present";
		
		BDDMockito.when(statusService.save(falseValidityPeriod))
			.thenThrow(new NoSuchElementException("No value present"));

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> statusService.save(falseValidityPeriod));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_saveStatus_whenStatusIsSaved() {
		LocalDate falseValidityPeriod = status.getValidityPeriod();

		BDDMockito.given(statusDAL.save(BDDMockito.any(Status.class))).willReturn(status);
		Status result = statusService.save(falseValidityPeriod);

		assertNotNull(result);
		assertTrue(result instanceof Status);
		assertEquals(status.getStatusId(), result.getStatusId());
		assertEquals(status.getValidityPeriod(), result.getValidityPeriod());
	}
	
	@Test
	void should_throwInvalidInputException_whenFindByIdInputIsInvalid() {
		Integer invalidId = 0;
		String expected = "No value present";		

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> statusService.get(invalidId));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenGetStatusIdDoesNotExist() {
		Integer id = status.getStatusId();
		String expected = "No value present";
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> statusService.get(id));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_retrieveValidStatus_whenIdIsValidAndIdExists() {
		Integer id = status.getStatusId();
		
		Mockito.doReturn(true).when(spyStatusService).existsById(id);
		BDDMockito.given(statusDAL.findById(id)).willReturn(Optional.of(status));
		Status fetchedStatus = statusService.get(id);
		
		assertNotNull(fetchedStatus);
		assertTrue(fetchedStatus instanceof Status);
		assertEquals(status.getStatusId(), fetchedStatus.getStatusId());
		assertEquals(status.getValidityPeriod(), fetchedStatus.getValidityPeriod());
	}
	
	@Test
	void should_retrieveStatusList_whenGetAllIsCalled() {
		BDDMockito.given(statusDAL.findAll()).willReturn(statusList);
		List<Status> fetchedStatusList = statusService.getAll();
		
		assertNotNull(fetchedStatusList);
		assertEquals(statusList.size(), fetchedStatusList.size());
	}
	
	@Test
	void should_throwInvalidInputException_whenUpdatedInputIsInvalid() {
		LocalDate falseStatusDate = null;
		Integer id = status.getStatusId();		
		String expected = "No value present";
		
		BDDMockito.when(statusService.update(id, falseStatusDate))
			.thenThrow(new NoSuchElementException("No value present"));

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> statusService.update(id, falseStatusDate));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenUpdateStatusIdDoesNotExist() {
		LocalDate falseStatusDate = status.getValidityPeriod();
		Integer id = status.getStatusId();
		String expected = "No value present";
		
		BDDMockito.when(statusService.update(id, falseStatusDate))
			.thenThrow(new NoSuchElementException("No value present"));
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> statusService.update(id, falseStatusDate));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwStatusException_whenUpdateValidityPeriodExists() {
		LocalDate falseStatusDate = LocalDate.of(2021, 8, 1);
		Integer id = status.getStatusId();
		String expected = "No value present";
		
		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatus);
		
		BDDMockito.when(statusService.update(id, falseStatusDate))
			.thenThrow(new NoSuchElementException("No value present"));
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> statusService.update(id, falseStatusDate));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_retrieveValidStatus_whenStatusIsUpdated() {
		LocalDate falseStatusDate = LocalDate.of(2021, 9, 10);
		Integer id = status.getStatusId();
		statusService.save(falseStatusDate);
		
		BDDMockito.doReturn(true).when(spyStatusService).existsById(id);
		BDDMockito.doReturn(false).when(spyStatusService).existsByValidityPeriod(falseStatusDate);
		
		BDDMockito.given(statusDAL.save(BDDMockito.any(Status.class))).willReturn(status);
		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatus);
		Status fetchedStatus = statusService.update(id, falseStatusDate);
		
		assertNotNull(fetchedStatus);
		assertTrue(fetchedStatus instanceof Status);
		assertEquals(status.getStatusId(), fetchedStatus.getStatusId());
		assertEquals(status.getValidityPeriod(), fetchedStatus.getValidityPeriod());
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenDeleteInputIsValid() {
		Integer id = status.getStatusId();
		
		statusService.delete(id);
		
		Mockito.verify(statusDAL, Mockito.times(1)).deleteById(Mockito.eq(id));
	}
	
	@Test
	void should_ReturnFalse_whenStatusDoesNotExistById() {
		Integer id = status.getStatusId();
		BDDMockito.given(statusDAL.existsById(BDDMockito.anyInt())).willReturn(false);
		
		boolean result = statusService.existsById(id);
		
		assertFalse(result);
	}
	
	@Test
	void should_ReturnTrue_whenEnvironmentDoesExistById() {
		Integer id = status.getStatusId();
		BDDMockito.given(statusDAL.existsById(BDDMockito.anyInt())).willReturn(true);
		
		boolean result = statusService.existsById(id);
		
		assertTrue(result);
	}
	
	@Test
	void should_ReturnTrue_whenStatusDoesExistByValidityPeriod() {
		LocalDate falseValidityPeriod = status.getValidityPeriod();
		BDDMockito.given(statusDAL.findByValidityPeriod(BDDMockito.any(LocalDate.class)))
			.willReturn(optionalStatus);
		
		boolean result = statusService.existsByValidityPeriod(falseValidityPeriod);
		
		assertTrue(result);
	}
	
	@Test
	void should_ReturnFalse_whenStatusDoesNotExistByValidityPeriod() {
		LocalDate falseValidityPeriod = status.getValidityPeriod();
		BDDMockito.given(statusDAL.findByValidityPeriod(BDDMockito.any(LocalDate.class)))
			.willReturn(Optional.empty());
		
		boolean result = statusService.existsByValidityPeriod(falseValidityPeriod);
		
		assertFalse(result);
	}

}
