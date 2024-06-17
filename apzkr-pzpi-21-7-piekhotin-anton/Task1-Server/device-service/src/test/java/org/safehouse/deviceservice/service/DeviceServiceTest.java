package org.safehouse.deviceservice.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.safehouse.deviceservice.model.entity.Device;
import org.safehouse.deviceservice.model.entity.DeviceType;
import org.safehouse.deviceservice.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ContextConfiguration(classes = {DeviceService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class DeviceServiceTest {
	@MockBean
	private DeviceRepository deviceRepository;

	@Autowired
	private DeviceService deviceService;

	/**
	 * Method under test: {@link DeviceService#getDeviceById(Long)}
	 */
	@Test
	void testGetDeviceById() {
		// Arrange
		Device device = new Device();
		device.setHouseId(1L);
		device.setId(1L);
		device.setType(DeviceType.LIGHT);
		Optional<Device> ofResult = Optional.of(device);
		when(deviceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

		// Act
		Device actualDeviceById = deviceService.getDeviceById(1L);

		// Assert
		verify(deviceRepository).findById(eq(1L));
		assertSame(device, actualDeviceById);
	}

	/**
	 * Method under test: {@link DeviceService#existsById(Long)}
	 */
	@Test
	void testExistsById() {
		// Arrange
		when(deviceRepository.existsById(Mockito.<Long>any())).thenReturn(true);

		// Act
		boolean actualExistsByIdResult = deviceService.existsById(1L);

		// Assert
		verify(deviceRepository).existsById(eq(1L));
		assertTrue(actualExistsByIdResult);
	}

	/**
	 * Method under test: {@link DeviceService#existsById(Long)}
	 */
	@Test
	void testExistsById2() {
		// Arrange
		when(deviceRepository.existsById(Mockito.<Long>any())).thenReturn(false);

		// Act
		boolean actualExistsByIdResult = deviceService.existsById(1L);

		// Assert
		verify(deviceRepository).existsById(eq(1L));
		assertFalse(actualExistsByIdResult);
	}

	/**
	 * Method under test: {@link DeviceService#existsById(Long)}
	 */
	@Test
	void testExistsById3() {
		// Arrange, Act and Assert
		assertFalse(deviceService.existsById(0L));
	}

	/**
	 * Method under test: {@link DeviceService#existsById(Long)}
	 */
	@Test
	void testExistsById4() {
		// Arrange, Act and Assert
		assertFalse(deviceService.existsById(null));
	}

	/**
	 * Method under test: {@link DeviceService#getDevicesByHouseId(Long)}
	 */
	@Test
	void testGetDevicesByHouseId() {
		// Arrange
		ArrayList<Device> deviceList = new ArrayList<>();
		when(deviceRepository.findAllByHouseId(Mockito.<Long>any())).thenReturn(deviceList);

		// Act
		List<Device> actualDevicesByHouseId = deviceService.getDevicesByHouseId(1L);

		// Assert
		verify(deviceRepository).findAllByHouseId(eq(1L));
		assertTrue(actualDevicesByHouseId.isEmpty());
		assertSame(deviceList, actualDevicesByHouseId);
	}

	/**
	 * Method under test: {@link DeviceService#getDevicesByHouseId(Long)}
	 */
	@Test
	void testGetDevicesByHouseId2() {
		// Arrange, Act and Assert
		assertNull(deviceService.getDevicesByHouseId(0L));
	}

	/**
	 * Method under test: {@link DeviceService#getDevicesByHouseId(Long)}
	 */
	@Test
	void testGetDevicesByHouseId3() {
		// Arrange, Act and Assert
		assertNull(deviceService.getDevicesByHouseId(null));
	}

	/**
	 * Method under test: {@link DeviceService#createDevice(Device)}
	 */
	@Test
	void testCreateDevice() {
		// Arrange
		when(deviceRepository.existsById(Mockito.<Long>any())).thenReturn(true);

		Device device = new Device();
		device.setHouseId(1L);
		device.setId(1L);
		device.setType(DeviceType.LIGHT);

		// Act
		Device actualCreateDeviceResult = deviceService.createDevice(device);

		// Assert
		verify(deviceRepository).existsById(eq(1L));
		assertNull(actualCreateDeviceResult);
	}

	/**
	 * Method under test: {@link DeviceService#createDevice(Device)}
	 */
	@Test
	void testCreateDevice2() {
		// Arrange
		Device device = new Device();
		device.setHouseId(1L);
		device.setId(1L);
		device.setType(DeviceType.LIGHT);
		when(deviceRepository.existsById(Mockito.<Long>any())).thenReturn(false);
		when(deviceRepository.save(Mockito.<Device>any())).thenReturn(device);

		Device device2 = new Device();
		device2.setHouseId(1L);
		device2.setId(1L);
		device2.setType(DeviceType.LIGHT);

		// Act
		Device actualCreateDeviceResult = deviceService.createDevice(device2);

		// Assert
		verify(deviceRepository).existsById(eq(1L));
		verify(deviceRepository).save(isA(Device.class));
		assertSame(device, actualCreateDeviceResult);
	}

	/**
	 * Method under test: {@link DeviceService#createDevice(Device)}
	 */
	@Test
	void testCreateDevice3() {
		// Arrange
		Device device = new Device();
		device.setHouseId(1L);
		device.setId(1L);
		device.setType(DeviceType.LIGHT);
		when(deviceRepository.save(Mockito.<Device>any())).thenReturn(device);
		Device device2 = mock(Device.class);
		when(device2.getId()).thenReturn(-1L);
		doNothing().when(device2).setHouseId(Mockito.<Long>any());
		doNothing().when(device2).setId(Mockito.<Long>any());
		doNothing().when(device2).setType(Mockito.<DeviceType>any());
		device2.setHouseId(1L);
		device2.setId(1L);
		device2.setType(DeviceType.LIGHT);

		// Act
		Device actualCreateDeviceResult = deviceService.createDevice(device2);

		// Assert
		verify(device2).getId();
		verify(device2).setHouseId(eq(1L));
		verify(device2).setId(eq(1L));
		verify(device2).setType(eq(DeviceType.LIGHT));
		verify(deviceRepository).save(isA(Device.class));
		assertSame(device, actualCreateDeviceResult);
	}

	/**
	 * Method under test: {@link DeviceService#deleteDeviceById(Long)}
	 */
	@Test
	void testDeleteDeviceById() {
		// Arrange
		doNothing().when(deviceRepository).deleteById(Mockito.<Long>any());
		when(deviceRepository.existsById(Mockito.<Long>any())).thenReturn(true);

		// Act
		boolean actualDeleteDeviceByIdResult = deviceService.deleteDeviceById(1L);

		// Assert
		verify(deviceRepository).deleteById(eq(1L));
		verify(deviceRepository).existsById(eq(1L));
		assertTrue(actualDeleteDeviceByIdResult);
	}

	/**
	 * Method under test: {@link DeviceService#deleteDeviceById(Long)}
	 */
	@Test
	void testDeleteDeviceById2() {
		// Arrange
		when(deviceRepository.existsById(Mockito.<Long>any())).thenReturn(false);

		// Act
		boolean actualDeleteDeviceByIdResult = deviceService.deleteDeviceById(1L);

		// Assert
		verify(deviceRepository).existsById(eq(1L));
		assertFalse(actualDeleteDeviceByIdResult);
	}

	/**
	 * Method under test: {@link DeviceService#getAllDevices()}
	 */
	@Test
	void testGetAllDevices() {
		// Arrange
		when(deviceRepository.findAll()).thenReturn(new ArrayList<>());

		// Act
		List<Device> actualAllDevices = deviceService.getAllDevices();

		// Assert
		verify(deviceRepository).findAll();
		assertNull(actualAllDevices);
	}

	/**
	 * Method under test: {@link DeviceService#getAllDevices()}
	 */
	@Test
	void testGetAllDevices2() {
		// Arrange
		Device device = new Device();
		device.setHouseId(1L);
		device.setId(1L);
		device.setType(DeviceType.LIGHT);

		ArrayList<Device> deviceList = new ArrayList<>();
		deviceList.add(device);
		when(deviceRepository.findAll()).thenReturn(deviceList);

		// Act
		List<Device> actualAllDevices = deviceService.getAllDevices();

		// Assert
		verify(deviceRepository, atLeast(1)).findAll();
		assertEquals(1, actualAllDevices.size());
		assertSame(device, actualAllDevices.get(0));
	}
}
