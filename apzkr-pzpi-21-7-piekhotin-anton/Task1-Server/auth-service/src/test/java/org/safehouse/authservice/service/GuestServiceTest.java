package org.safehouse.authservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.safehouse.authservice.model.dto.AuthRequestDto;
import org.safehouse.authservice.model.dto.GuestInfoDto;
import org.safehouse.authservice.model.entity.Guest;
import org.safehouse.authservice.model.entity.GuestRole;
import org.safehouse.authservice.model.exception.AuthException;
import org.safehouse.authservice.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {GuestService.class, PasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class GuestServiceTest {
	@MockBean
	private GuestRepository guestRepository;

	@Autowired
	private GuestService guestService;

	@MockBean
	private PasswordEncoder passwordEncoder;

	/**
	 * Method under test: {@link GuestService#createGuest(AuthRequestDto)}
	 */
	@Test
	void testCreateGuest() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.existsByEmail(Mockito.<String>any()))
				.thenThrow(new AuthException("0123456789ABCDEF", "An error occurred"));
		GuestService guestService = new GuestService(guestRepository, new BCryptPasswordEncoder());

		// Act and Assert
		assertThrows(AuthException.class,
				() -> guestService.createGuest(new AuthRequestDto("jane.doe@example.org", "iloveyou")));
		verify(guestRepository).existsByEmail(eq("jane.doe@example.org"));
	}


	/**
	 * Method under test: {@link GuestService#isGuestWithSameEmailExists(String)}
	 */
	@Test
	void testIsGuestWithSameEmailExists() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);

		// Act
		boolean actualIsGuestWithSameEmailExistsResult = (new GuestService(guestRepository, new BCryptPasswordEncoder()))
				.isGuestWithSameEmailExists("jane.doe@example.org");

		// Assert
		verify(guestRepository).existsByEmail(eq("jane.doe@example.org"));
		assertTrue(actualIsGuestWithSameEmailExistsResult);
	}

	/**
	 * Method under test: {@link GuestService#isGuestWithSameEmailExists(String)}
	 */
	@Test
	void testIsGuestWithSameEmailExists2() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);

		// Act
		boolean actualIsGuestWithSameEmailExistsResult = (new GuestService(guestRepository, new BCryptPasswordEncoder()))
				.isGuestWithSameEmailExists("jane.doe@example.org");

		// Assert
		verify(guestRepository).existsByEmail(eq("jane.doe@example.org"));
		assertFalse(actualIsGuestWithSameEmailExistsResult);
	}

	/**
	 * Method under test: {@link GuestService#isGuestWithSameEmailExists(String)}
	 */
	@Test
	void testIsGuestWithSameEmailExists3() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.existsByEmail(Mockito.<String>any()))
				.thenThrow(new AuthException("0123456789ABCDEF", "An error occurred"));

		// Act and Assert
		assertThrows(AuthException.class, () -> (new GuestService(guestRepository, new BCryptPasswordEncoder()))
				.isGuestWithSameEmailExists("jane.doe@example.org"));
		verify(guestRepository).existsByEmail(eq("jane.doe@example.org"));
	}

	/**
	 * Method under test: {@link GuestService#findByEmail(String)}
	 */
	@Test
	void testFindByEmail() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		Guest guest = new Guest();
		guest.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
		guest.setEmail("jane.doe@example.org");
		guest.setId(1L);
		guest.setIsEnabled(true);
		guest.setName("Name");
		guest.setPassword("iloveyou");
		guest.setRole(GuestRole.ADMIN);
		Optional<Guest> ofResult = Optional.of(guest);
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);

		// Act
		Guest actualFindByEmailResult = (new GuestService(guestRepository, new BCryptPasswordEncoder()))
				.findByEmail("jane.doe@example.org");

		// Assert
		verify(guestRepository).findByEmail(eq("jane.doe@example.org"));
		assertSame(guest, actualFindByEmailResult);
	}

	/**
	 * Method under test: {@link GuestService#findByEmail(String)}
	 */
	@Test
	void testFindByEmail2() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.findByEmail(Mockito.<String>any()))
				.thenThrow(new AuthException("0123456789ABCDEF", "An error occurred"));

		// Act and Assert
		assertThrows(AuthException.class,
				() -> (new GuestService(guestRepository, new BCryptPasswordEncoder())).findByEmail("jane.doe@example.org"));
		verify(guestRepository).findByEmail(eq("jane.doe@example.org"));
	}

	/**
	 * Method under test: {@link GuestService#isClientAuthenticated()}
	 */
	@Test
	void testIsClientAuthenticated() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);

		// Act and Assert
		assertFalse((new GuestService(guestRepository, new BCryptPasswordEncoder())).isClientAuthenticated());
	}

	/**
	 * Method under test: {@link GuestService#existsById(Long)}
	 */
	@Test
	void testExistsById() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.existsById(Mockito.<Long>any())).thenReturn(true);

		// Act
		boolean actualExistsByIdResult = (new GuestService(guestRepository, new BCryptPasswordEncoder())).existsById(1L);

		// Assert
		verify(guestRepository).existsById(eq(1L));
		assertTrue(actualExistsByIdResult);
	}

	/**
	 * Method under test: {@link GuestService#existsById(Long)}
	 */
	@Test
	void testExistsById2() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.existsById(Mockito.<Long>any())).thenReturn(false);

		// Act
		boolean actualExistsByIdResult = (new GuestService(guestRepository, new BCryptPasswordEncoder())).existsById(1L);

		// Assert
		verify(guestRepository).existsById(eq(1L));
		assertFalse(actualExistsByIdResult);
	}

	/**
	 * Method under test: {@link GuestService#existsById(Long)}
	 */
	@Test
	void testExistsById3() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.existsById(Mockito.<Long>any()))
				.thenThrow(new AuthException("0123456789ABCDEF", "An error occurred"));

		// Act and Assert
		assertThrows(AuthException.class,
				() -> (new GuestService(guestRepository, new BCryptPasswordEncoder())).existsById(1L));
		verify(guestRepository).existsById(eq(1L));
	}

	/**
	 * Method under test: {@link GuestService#editUser(Long, GuestInfoDto)}
	 */
	@Test
	void testEditUser() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
		GuestService guestService = new GuestService(guestRepository, new BCryptPasswordEncoder());
		GuestInfoDto guestInfoDto = GuestInfoDto.builder()
				.email("jane.doe@example.org")
				.name("Name")
				.role(GuestRole.ADMIN)
				.build();

		// Act
		boolean actualEditUserResult = guestService.editUser(1L, guestInfoDto);

		// Assert
		verify(guestRepository).existsByEmail(eq("jane.doe@example.org"));
		assertFalse(actualEditUserResult);
	}

	/**
	 * Method under test: {@link GuestService#editUser(Long, GuestInfoDto)}
	 */
	@Test
	void testEditUser2() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		Guest guest = new Guest();
		guest.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
		guest.setEmail("jane.doe@example.org");
		guest.setId(1L);
		guest.setIsEnabled(true);
		guest.setName("Name");
		guest.setPassword("iloveyou");
		guest.setRole(GuestRole.ADMIN);
		Optional<Guest> ofResult = Optional.of(guest);

		Guest guest2 = new Guest();
		guest2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
		guest2.setEmail("jane.doe@example.org");
		guest2.setId(1L);
		guest2.setIsEnabled(true);
		guest2.setName("Name");
		guest2.setPassword("iloveyou");
		guest2.setRole(GuestRole.ADMIN);
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.save(Mockito.<Guest>any())).thenReturn(guest2);
		when(guestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		when(guestRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
		when(guestRepository.existsById(Mockito.<Long>any())).thenReturn(true);
		GuestService guestService = new GuestService(guestRepository, new BCryptPasswordEncoder());
		GuestInfoDto guestInfoDto = GuestInfoDto.builder()
				.email("jane.doe@example.org")
				.name("Name")
				.role(GuestRole.ADMIN)
				.build();

		// Act
		boolean actualEditUserResult = guestService.editUser(1L, guestInfoDto);

		// Assert
		verify(guestRepository).existsByEmail(eq("jane.doe@example.org"));
		verify(guestRepository).existsById(eq(1L));
		verify(guestRepository).findById(eq(1L));
		verify(guestRepository).save(isA(Guest.class));
		assertTrue(actualEditUserResult);
	}

	/**
	 * Method under test: {@link GuestService#editUser(Long, GuestInfoDto)}
	 */
	@Test
	void testEditUser3() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		Guest guest = new Guest();
		guest.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
		guest.setEmail("jane.doe@example.org");
		guest.setId(1L);
		guest.setIsEnabled(true);
		guest.setName("Name");
		guest.setPassword("iloveyou");
		guest.setRole(GuestRole.ADMIN);
		Optional<Guest> ofResult = Optional.of(guest);
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.save(Mockito.<Guest>any()))
				.thenThrow(new AuthException("0123456789ABCDEF", "An error occurred"));
		when(guestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		when(guestRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
		when(guestRepository.existsById(Mockito.<Long>any())).thenReturn(true);
		GuestService guestService = new GuestService(guestRepository, new BCryptPasswordEncoder());
		GuestInfoDto guestInfoDto = GuestInfoDto.builder()
				.email("jane.doe@example.org")
				.name("Name")
				.role(GuestRole.ADMIN)
				.build();

		// Act and Assert
		assertThrows(AuthException.class, () -> guestService.editUser(1L, guestInfoDto));
		verify(guestRepository).existsByEmail(eq("jane.doe@example.org"));
		verify(guestRepository).existsById(eq(1L));
		verify(guestRepository).findById(eq(1L));
		verify(guestRepository).save(isA(Guest.class));
	}

	/**
	 * Method under test: {@link GuestService#editUser(Long, GuestInfoDto)}
	 */
	@Test
	void testEditUser4() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		Guest guest = mock(Guest.class);
		doNothing().when(guest).setCreated(Mockito.<LocalDateTime>any());
		doNothing().when(guest).setEmail(Mockito.<String>any());
		doNothing().when(guest).setId(Mockito.<Long>any());
		doNothing().when(guest).setIsEnabled(Mockito.<Boolean>any());
		doNothing().when(guest).setName(Mockito.<String>any());
		doNothing().when(guest).setPassword(Mockito.<String>any());
		doNothing().when(guest).setRole(Mockito.<GuestRole>any());
		guest.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
		guest.setEmail("jane.doe@example.org");
		guest.setId(1L);
		guest.setIsEnabled(true);
		guest.setName("Name");
		guest.setPassword("iloveyou");
		guest.setRole(GuestRole.ADMIN);
		Optional<Guest> ofResult = Optional.of(guest);

		Guest guest2 = new Guest();
		guest2.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
		guest2.setEmail("jane.doe@example.org");
		guest2.setId(1L);
		guest2.setIsEnabled(true);
		guest2.setName("Name");
		guest2.setPassword("iloveyou");
		guest2.setRole(GuestRole.ADMIN);
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.save(Mockito.<Guest>any())).thenReturn(guest2);
		when(guestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		when(guestRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
		when(guestRepository.existsById(Mockito.<Long>any())).thenReturn(true);
		GuestService guestService = new GuestService(guestRepository, new BCryptPasswordEncoder());
		GuestInfoDto guestInfoDto = GuestInfoDto.builder()
				.email("jane.doe@example.org")
				.name("Name")
				.role(GuestRole.ADMIN)
				.build();

		// Act
		boolean actualEditUserResult = guestService.editUser(1L, guestInfoDto);

		// Assert
		verify(guest).setCreated(isA(LocalDateTime.class));
		verify(guest, atLeast(1)).setEmail(eq("jane.doe@example.org"));
		verify(guest).setId(eq(1L));
		verify(guest).setIsEnabled(eq(true));
		verify(guest, atLeast(1)).setName(eq("Name"));
		verify(guest, atLeast(1)).setPassword(Mockito.<String>any());
		verify(guest, atLeast(1)).setRole(eq(GuestRole.ADMIN));
		verify(guestRepository).existsByEmail(eq("jane.doe@example.org"));
		verify(guestRepository).existsById(eq(1L));
		verify(guestRepository).findById(eq(1L));
		verify(guestRepository).save(isA(Guest.class));
		assertTrue(actualEditUserResult);
	}

	/**
	 * Method under test: {@link GuestService#editUser(Long, GuestInfoDto)}
	 */
	@Test
	void testEditUser5() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		Optional<Guest> emptyResult = Optional.empty();
		when(guestRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
		when(guestRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
		when(guestRepository.existsById(Mockito.<Long>any())).thenReturn(true);
		GuestService guestService = new GuestService(guestRepository, new BCryptPasswordEncoder());
		GuestInfoDto guestInfoDto = GuestInfoDto.builder()
				.email("jane.doe@example.org")
				.name("Name")
				.role(GuestRole.ADMIN)
				.build();

		// Act
		boolean actualEditUserResult = guestService.editUser(1L, guestInfoDto);

		// Assert
		verify(guestRepository).existsByEmail(eq("jane.doe@example.org"));
		verify(guestRepository).existsById(eq(1L));
		verify(guestRepository).findById(eq(1L));
		assertFalse(actualEditUserResult);
	}

	/**
	 * Method under test: {@link GuestService#editUser(Long, GuestInfoDto)}
	 */
	@Test
	void testEditUser6() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		Guest guest = mock(Guest.class);
		doNothing().when(guest).setCreated(Mockito.<LocalDateTime>any());
		doNothing().when(guest).setEmail(Mockito.<String>any());
		doNothing().when(guest).setId(Mockito.<Long>any());
		doNothing().when(guest).setIsEnabled(Mockito.<Boolean>any());
		doNothing().when(guest).setName(Mockito.<String>any());
		doNothing().when(guest).setPassword(Mockito.<String>any());
		doNothing().when(guest).setRole(Mockito.<GuestRole>any());
		guest.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
		guest.setEmail("jane.doe@example.org");
		guest.setId(1L);
		guest.setIsEnabled(true);
		guest.setName("Name");
		guest.setPassword("iloveyou");
		guest.setRole(GuestRole.ADMIN);
		Optional.of(guest);
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
		when(guestRepository.existsById(Mockito.<Long>any())).thenReturn(false);
		GuestService guestService = new GuestService(guestRepository, new BCryptPasswordEncoder());
		GuestInfoDto guestInfoDto = GuestInfoDto.builder()
				.email("jane.doe@example.org")
				.name("Name")
				.role(GuestRole.ADMIN)
				.build();

		// Act
		boolean actualEditUserResult = guestService.editUser(1L, guestInfoDto);

		// Assert
		verify(guest).setCreated(isA(LocalDateTime.class));
		verify(guest).setEmail(eq("jane.doe@example.org"));
		verify(guest).setId(eq(1L));
		verify(guest).setIsEnabled(eq(true));
		verify(guest).setName(eq("Name"));
		verify(guest).setPassword(eq("iloveyou"));
		verify(guest).setRole(eq(GuestRole.ADMIN));
		verify(guestRepository).existsByEmail(eq("jane.doe@example.org"));
		verify(guestRepository).existsById(eq(1L));
		assertFalse(actualEditUserResult);
	}

	/**
	 * Method under test: {@link GuestService#getUserInfoById(Long)}
	 */
	@Test
	void testGetUserInfoById() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		Guest guest = new Guest();
		guest.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
		guest.setEmail("jane.doe@example.org");
		guest.setId(1L);
		guest.setIsEnabled(true);
		guest.setName("Name");
		guest.setPassword("iloveyou");
		guest.setRole(GuestRole.ADMIN);
		Optional<Guest> ofResult = Optional.of(guest);
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

		// Act
		GuestInfoDto actualUserInfoById = (new GuestService(guestRepository, new BCryptPasswordEncoder()))
				.getUserInfoById(1L);

		// Assert
		verify(guestRepository).findById(eq(1L));
		assertEquals("Name", actualUserInfoById.getName());
		assertEquals("jane.doe@example.org", actualUserInfoById.getEmail());
		assertEquals(GuestRole.ADMIN, actualUserInfoById.getRole());
	}

	/**
	 * Method under test: {@link GuestService#getUserInfoById(Long)}
	 */
	@Test
	void testGetUserInfoById2() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		Guest guest = mock(Guest.class);
		when(guest.getEmail()).thenReturn("jane.doe@example.org");
		when(guest.getName()).thenReturn("Name");
		when(guest.getPassword()).thenReturn("iloveyou");
		when(guest.getRole()).thenReturn(GuestRole.ADMIN);
		doNothing().when(guest).setCreated(Mockito.<LocalDateTime>any());
		doNothing().when(guest).setEmail(Mockito.<String>any());
		doNothing().when(guest).setId(Mockito.<Long>any());
		doNothing().when(guest).setIsEnabled(Mockito.<Boolean>any());
		doNothing().when(guest).setName(Mockito.<String>any());
		doNothing().when(guest).setPassword(Mockito.<String>any());
		doNothing().when(guest).setRole(Mockito.<GuestRole>any());
		guest.setCreated(LocalDate.of(1970, 1, 1).atStartOfDay());
		guest.setEmail("jane.doe@example.org");
		guest.setId(1L);
		guest.setIsEnabled(true);
		guest.setName("Name");
		guest.setPassword("iloveyou");
		guest.setRole(GuestRole.ADMIN);
		Optional<Guest> ofResult = Optional.of(guest);
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

		// Act
		GuestInfoDto actualUserInfoById = (new GuestService(guestRepository, new BCryptPasswordEncoder()))
				.getUserInfoById(1L);

		// Assert
		verify(guest).getEmail();
		verify(guest).getName();
		verify(guest).getPassword();
		verify(guest).getRole();
		verify(guest).setCreated(isA(LocalDateTime.class));
		verify(guest).setEmail(eq("jane.doe@example.org"));
		verify(guest).setId(eq(1L));
		verify(guest).setIsEnabled(eq(true));
		verify(guest).setName(eq("Name"));
		verify(guest).setPassword(eq("iloveyou"));
		verify(guest).setRole(eq(GuestRole.ADMIN));
		verify(guestRepository).findById(eq(1L));
		assertEquals("Name", actualUserInfoById.getName());
		assertEquals("jane.doe@example.org", actualUserInfoById.getEmail());
		assertEquals(GuestRole.ADMIN, actualUserInfoById.getRole());
	}

	/**
	 * Method under test: {@link GuestService#getUserInfoById(Long)}
	 */
	@Test
	void testGetUserInfoById3() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		Optional<Guest> emptyResult = Optional.empty();
		when(guestRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

		// Act
		GuestInfoDto actualUserInfoById = (new GuestService(guestRepository, new BCryptPasswordEncoder()))
				.getUserInfoById(1L);

		// Assert
		verify(guestRepository).findById(eq(1L));
		assertNull(actualUserInfoById);
	}

	/**
	 * Method under test: {@link GuestService#getAllUsers()}
	 */
	@Test
	void testGetAllUsers() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		ArrayList<Guest> guestList = new ArrayList<>();
		when(guestRepository.findAll()).thenReturn(guestList);

		// Act
		List<Guest> actualAllUsers = (new GuestService(guestRepository, new BCryptPasswordEncoder())).getAllUsers();

		// Assert
		verify(guestRepository).findAll();
		assertTrue(actualAllUsers.isEmpty());
		assertSame(guestList, actualAllUsers);
	}

	/**
	 * Method under test: {@link GuestService#getAllUsers()}
	 */
	@Test
	void testGetAllUsers2() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.findAll()).thenThrow(new AuthException("0123456789ABCDEF", "An error occurred"));

		// Act and Assert
		assertThrows(AuthException.class,
				() -> (new GuestService(guestRepository, new BCryptPasswordEncoder())).getAllUsers());
		verify(guestRepository).findAll();
	}


	/**
	 * Method under test: {@link GuestService#deleteUserById(Long)}
	 */
	@Test
	void testDeleteUserById() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		doNothing().when(guestRepository).deleteById(Mockito.<Long>any());
		when(guestRepository.existsById(Mockito.<Long>any())).thenReturn(true);

		// Act
		boolean actualDeleteUserByIdResult = (new GuestService(guestRepository, new BCryptPasswordEncoder()))
				.deleteUserById(1L);

		// Assert
		verify(guestRepository).deleteById(eq(1L));
		verify(guestRepository).existsById(eq(1L));
		assertTrue(actualDeleteUserByIdResult);
	}

	/**
	 * Method under test: {@link GuestService#deleteUserById(Long)}
	 */
	@Test
	void testDeleteUserById2() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		doThrow(new AuthException("0123456789ABCDEF", "An error occurred")).when(guestRepository)
				.deleteById(Mockito.<Long>any());
		when(guestRepository.existsById(Mockito.<Long>any())).thenReturn(true);

		// Act and Assert
		assertThrows(AuthException.class,
				() -> (new GuestService(guestRepository, new BCryptPasswordEncoder())).deleteUserById(1L));
		verify(guestRepository).deleteById(eq(1L));
		verify(guestRepository).existsById(eq(1L));
	}

	/**
	 * Method under test: {@link GuestService#deleteUserById(Long)}
	 */
	@Test
	void testDeleteUserById3() {
		//   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

		// Arrange
		GuestRepository guestRepository = mock(GuestRepository.class);
		when(guestRepository.existsById(Mockito.<Long>any())).thenReturn(false);

		// Act
		boolean actualDeleteUserByIdResult = (new GuestService(guestRepository, new BCryptPasswordEncoder()))
				.deleteUserById(1L);

		// Assert
		verify(guestRepository).existsById(eq(1L));
		assertFalse(actualDeleteUserByIdResult);
	}

}
