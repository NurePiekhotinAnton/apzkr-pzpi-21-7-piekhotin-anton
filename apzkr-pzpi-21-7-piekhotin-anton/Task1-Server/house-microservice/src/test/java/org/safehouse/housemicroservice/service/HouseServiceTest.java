package org.safehouse.housemicroservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.safehouse.housemicroservice.model.entity.House;
import org.safehouse.housemicroservice.repository.HouseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HouseServiceTest {
	@Mock
	private HouseRepository houseRepository;

	@InjectMocks
	private HouseService houseService;

	/**
	 * Method under test: {@link HouseService#getHouseById(Long)}
	 */
	@Test
	void testGetHouseById() {
		// Arrange
		House house = new House();
		house.setAddress("42 Main St");
		house.setId(1L);
		house.setName("Name");
		house.setUserId(1L);
		Optional<House> ofResult = Optional.of(house);
		when(houseRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

		// Act
		House actualHouseById = houseService.getHouseById(1L);

		// Assert
		verify(houseRepository).findById(eq(1L));
		assertSame(house, actualHouseById);
	}

	/**
	 * Method under test: {@link HouseService#getHousesByUserId(Long)}
	 */
	@Test
	void testGetHousesByUserId() {
		// Arrange
		ArrayList<House> houseList = new ArrayList<>();
		when(houseRepository.findByUserId(Mockito.<Long>any())).thenReturn(houseList);

		// Act
		List<House> actualHousesByUserId = houseService.getHousesByUserId(1L);

		// Assert
		verify(houseRepository).findByUserId(eq(1L));
		assertTrue(actualHousesByUserId.isEmpty());
		assertSame(houseList, actualHousesByUserId);
	}

	/**
	 * Method under test: {@link HouseService#getHousesByUserId(Long)}
	 */
	@Test
	void testGetHousesByUserId2() {
		// Arrange, Act and Assert
		assertNull(houseService.getHousesByUserId(0L));
	}

	/**
	 * Method under test: {@link HouseService#getHousesByUserId(Long)}
	 */
	@Test
	void testGetHousesByUserId3() {
		// Arrange, Act and Assert
		assertNull(houseService.getHousesByUserId(null));
	}

	/**
	 * Method under test: {@link HouseService#createHouse(House)}
	 */
	@Test
	void testCreateHouse() {
		// Arrange
		when(houseRepository.existsById(Mockito.<Long>any())).thenReturn(true);

		House house = new House();
		house.setAddress("42 Main St");
		house.setId(1L);
		house.setName("Name");
		house.setUserId(1L);

		// Act
		House actualCreateHouseResult = houseService.createHouse(house);

		// Assert
		verify(houseRepository).existsById(eq(1L));
		assertNull(actualCreateHouseResult);
	}

	/**
	 * Method under test: {@link HouseService#createHouse(House)}
	 */
	@Test
	void testCreateHouse2() {
		// Arrange
		House house = new House();
		house.setAddress("42 Main St");
		house.setId(1L);
		house.setName("Name");
		house.setUserId(1L);
		when(houseRepository.existsById(Mockito.<Long>any())).thenReturn(false);
		when(houseRepository.save(Mockito.<House>any())).thenReturn(house);

		House house2 = new House();
		house2.setAddress("42 Main St");
		house2.setId(1L);
		house2.setName("Name");
		house2.setUserId(1L);

		// Act
		House actualCreateHouseResult = houseService.createHouse(house2);

		// Assert
		verify(houseRepository).existsById(eq(1L));
		verify(houseRepository).save(isA(House.class));
		assertSame(house, actualCreateHouseResult);
	}

	/**
	 * Method under test: {@link HouseService#createHouse(House)}
	 */
	@Test
	void testCreateHouse3() {
		// Arrange
		House house = new House();
		house.setAddress("42 Main St");
		house.setId(1L);
		house.setName("Name");
		house.setUserId(1L);
		when(houseRepository.save(Mockito.<House>any())).thenReturn(house);
		House house2 = mock(House.class);
		when(house2.getId()).thenReturn(-1L);
		doNothing().when(house2).setAddress(Mockito.<String>any());
		doNothing().when(house2).setId(Mockito.<Long>any());
		doNothing().when(house2).setName(Mockito.<String>any());
		doNothing().when(house2).setUserId(Mockito.<Long>any());
		house2.setAddress("42 Main St");
		house2.setId(1L);
		house2.setName("Name");
		house2.setUserId(1L);

		// Act
		House actualCreateHouseResult = houseService.createHouse(house2);

		// Assert
		verify(house2).getId();
		verify(house2).setAddress(eq("42 Main St"));
		verify(house2).setId(eq(1L));
		verify(house2).setName(eq("Name"));
		verify(house2).setUserId(eq(1L));
		verify(houseRepository).save(isA(House.class));
		assertSame(house, actualCreateHouseResult);
	}

	/**
	 * Method under test: {@link HouseService#deleteHouseById(Long)}
	 */
	@Test
	void testDeleteHouseById() {
		// Arrange
		doNothing().when(houseRepository).deleteById(Mockito.<Long>any());
		when(houseRepository.existsById(Mockito.<Long>any())).thenReturn(true);

		// Act
		boolean actualDeleteHouseByIdResult = houseService.deleteHouseById(1L);

		// Assert
		verify(houseRepository).deleteById(eq(1L));
		verify(houseRepository).existsById(eq(1L));
		assertTrue(actualDeleteHouseByIdResult);
	}

	/**
	 * Method under test: {@link HouseService#deleteHouseById(Long)}
	 */
	@Test
	void testDeleteHouseById2() {
		// Arrange
		when(houseRepository.existsById(Mockito.<Long>any())).thenReturn(false);

		// Act
		boolean actualDeleteHouseByIdResult = houseService.deleteHouseById(1L);

		// Assert
		verify(houseRepository).existsById(eq(1L));
		assertFalse(actualDeleteHouseByIdResult);
	}

	/**
	 * Method under test: {@link HouseService#existById(Long)}
	 */
	@Test
	void testExistById() {
		// Arrange
		when(houseRepository.existsById(Mockito.<Long>any())).thenReturn(true);

		// Act
		boolean actualExistByIdResult = houseService.existById(1L);

		// Assert
		verify(houseRepository).existsById(eq(1L));
		assertTrue(actualExistByIdResult);
	}

	/**
	 * Method under test: {@link HouseService#existById(Long)}
	 */
	@Test
	void testExistById2() {
		// Arrange
		when(houseRepository.existsById(Mockito.<Long>any())).thenReturn(false);

		// Act
		boolean actualExistByIdResult = houseService.existById(1L);

		// Assert
		verify(houseRepository).existsById(eq(1L));
		assertFalse(actualExistByIdResult);
	}

	/**
	 * Method under test: {@link HouseService#existById(Long)}
	 */
	@Test
	void testExistById3() {
		// Arrange, Act and Assert
		assertFalse(houseService.existById(0L));
	}

	/**
	 * Method under test: {@link HouseService#existById(Long)}
	 */
	@Test
	void testExistById4() {
		// Arrange, Act and Assert
		assertFalse(houseService.existById(null));
	}

	/**
	 * Method under test: {@link HouseService#getAllHouses()}
	 */
	@Test
	void testGetAllHouses() {
		// Arrange
		when(houseRepository.findAll()).thenReturn(new ArrayList<>());

		// Act
		List<House> actualAllHouses = houseService.getAllHouses();

		// Assert
		verify(houseRepository).findAll();
		assertNull(actualAllHouses);
	}

	/**
	 * Method under test: {@link HouseService#getAllHouses()}
	 */
	@Test
	void testGetAllHouses2() {
		// Arrange
		House house = new House();
		house.setAddress("42 Main St");
		house.setId(1L);
		house.setName("Name");
		house.setUserId(1L);

		ArrayList<House> houseList = new ArrayList<>();
		houseList.add(house);
		when(houseRepository.findAll()).thenReturn(houseList);

		// Act
		List<House> actualAllHouses = houseService.getAllHouses();

		// Assert
		verify(houseRepository, atLeast(1)).findAll();
		assertEquals(1, actualAllHouses.size());
		assertSame(house, actualAllHouses.get(0));
	}
}
