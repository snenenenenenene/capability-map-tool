package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.StatusException;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.StatusDAL;

/**
*
* @author Bavo Van Meel
*
*/
@SpringBootTest
public class StatusServiceTest {
	
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
	void should_throwInvalidInputException_whenSavedInputIsInvalid() {
		LocalDate falseStatusDate = null;
		String expected = "Invalid input.";

		Exception exception = assertThrows(InvalidInputException.class,
				() -> statusService.save(falseStatusDate));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwDuplicateValueException_whenSavedStatusNameExists() {
		LocalDate falseValidityPeriod = LocalDate.of(2021, 8, 10);
		String expected = "Validity period already exists.";
		BDDMockito.doReturn(false).when(spyStatusService).existsByValidityPeriod(falseValidityPeriod);

		Exception exception = assertThrows(DuplicateValueException.class,
				() -> statusService.save(falseValidityPeriod));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_saveEnvironment_whenEnvironmentIsSaved() {
		LocalDate falseValidityPeriod = status.getValidityPeriod();

		Mockito.doReturn(true).when(spyStatusService).existsByValidityPeriod(falseValidityPeriod);
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
		LocalDate validityPeriod = status.getValidityPeriod();
		String expected = "Status ID is not valid.";
		statusService.save(validityPeriod);

		Exception exception = assertThrows(InvalidInputException.class,
				() -> statusService.get(invalidId));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenGetStatusIdDoesNotExist() {
		Integer id = status.getStatusId();
		String expected = "Status ID does not exists.";
		
		Exception exception = assertThrows(IndexDoesNotExistException.class,
				() -> statusService.get(id));
		
		assertEquals(exception.getMessage(), expected);
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
		
		String expected = "Invalid input.";

		Exception exception = assertThrows(InvalidInputException.class,
				() -> statusService.update(id, falseStatusDate));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenUpdateStatusIdDoesNotExist() {
		LocalDate falseStatusDate = status.getValidityPeriod();
		Integer id = status.getStatusId();
		String expected = "Can not update status if it does not exist.";
		BDDMockito.doReturn(false).when(spyStatusService).existsById(id);
		
		Exception exception = assertThrows(IndexDoesNotExistException.class,
				() -> statusService.update(id, falseStatusDate));
		
		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwStatusException_whenUpdateValidityPeriodExists() {
		LocalDate falseStatusDate = status.getValidityPeriod();
		Integer id = status.getStatusId();
		String expected = "Validity period already exists.";
		BDDMockito.doReturn(true).when(spyStatusService).existsById(id);
		BDDMockito.doReturn(true).when(spyStatusService).existsByValidityPeriod(falseStatusDate);
		
		Exception exception = assertThrows(StatusException.class,
				() -> statusService.update(id, falseStatusDate));
		
		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_retrieveValidStatus_whenStatusIsUpdated() {
		LocalDate falseStatusDate = LocalDate.of(2021, 9, 10);
		Integer id = status.getStatusId();
		statusService.save(falseStatusDate);
		
		BDDMockito.doReturn(true).when(spyStatusService).existsById(id);
		BDDMockito.doReturn(false).when(spyStatusService).existsByValidityPeriod(falseStatusDate);
		
		BDDMockito.given(statusDAL.save(BDDMockito.any(Status.class))).willReturn(status);
		Status fetchedStatus = statusService.update(id, falseStatusDate);
		
		assertNotNull(fetchedStatus);
		assertTrue(fetchedStatus instanceof Status);
		assertEquals(status.getStatusId(), fetchedStatus.getStatusId());
		assertEquals(status.getValidityPeriod(), fetchedStatus.getValidityPeriod());
	}
	
	@Test
	void should_throwInvalidInputException_whenDeleteInputIsInvalid() {
		Integer id = 0;
		String expected = "Status ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> statusService.delete(id));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenDeleteStatusIdDoesNotExist() {
		Integer id = status.getStatusId();
		String expected = "Status ID does not exists.";
		BDDMockito.doReturn(false).when(spyStatusService).existsById(id);
		
		Exception exception = assertThrows(IndexDoesNotExistException.class,
				() -> statusService.delete(id));
		
		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenDeleteInputIsValid() {
		LocalDate falseValidityPeriod = status.getValidityPeriod();
		Integer id = status.getStatusId();
		String expected = "Status ID does not exists.";
		
		Mockito.doReturn(true).when(spyStatusService).existsByValidityPeriod(falseValidityPeriod);
		BDDMockito.given(statusDAL.save(BDDMockito.any(Status.class))).willReturn(status);
		Status savedStatus = statusService.save(falseValidityPeriod);
		assertNotNull(savedStatus);
		
		statusDAL.deleteById(id);
		
		Exception exception = assertThrows(IndexDoesNotExistException.class,
				() -> statusService.get(id));
		
		assertEquals(exception.getMessage(), expected);		
	}
	
	@Test
	void should_throwInvalidInputException_whenExistsByIdInputIsInvalid() {
		Integer id = 0;
		String expected = "Status ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> statusService.existsById(id));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_ReturnFalse_whenEnvironmentDoesNotExistById() {
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
	void should_throwInvalidInputException_whenExistsByValidityPeriodInputIsInvalid() {
		LocalDate falseValidityPeriod = null;
		String expected = "Validity Period is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> statusService.existsByValidityPeriod(falseValidityPeriod));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_ReturnFalse_whenStatusDoesExistByValidityPeriod() {
		LocalDate falseValidityPeriod = status.getValidityPeriod();
		BDDMockito.given(statusDAL.findByValidityPeriod(BDDMockito.any(LocalDate.class)))
			.willReturn(optionalStatus);
		
		boolean result = statusService.existsByValidityPeriod(falseValidityPeriod);
		
		assertFalse(result);
	}
	
	@Test
	void should_ReturnFalse_whenStatusDoesNotExistByValidityPeriod() {
		LocalDate falseValidityPeriod = status.getValidityPeriod();
		BDDMockito.given(statusDAL.findByValidityPeriod(BDDMockito.any(LocalDate.class)))
			.willReturn(Optional.empty());
		
		boolean result = statusService.existsByValidityPeriod(falseValidityPeriod);
		
		assertTrue(result);
	}

}
