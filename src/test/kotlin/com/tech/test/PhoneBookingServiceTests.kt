package com.tech.test

import com.tech.test.model.User
import com.tech.test.respository.BookingTxnRepository
import com.tech.test.respository.PhoneDeviceRepository
import com.tech.test.respository.UserRepository
import com.tech.test.service.BookingService
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.web.server.ResponseStatusException


//@ExtendWith(SpringExtension::class)
@DataJpaTest
class PhoneBookingServiceTests {
	@Autowired
	private lateinit var userRepository: UserRepository
	@Autowired
	private lateinit var phoneDeviceRepository: PhoneDeviceRepository
	@Autowired
	private lateinit var bookingService: BookingService
	@Autowired
	private lateinit var bookingTxnRepository: BookingTxnRepository

//	@InjectMocks
//	private lateinit var jmsTemplate: StringTemplate
//
//	@Before
//	fun prepare() {
//		MockitoAnnotations.initMocks(this)
//	}

	@Test
	fun userFindAllDevices() {
		val devices = bookingService.findCirculationDevices()
		assertTrue(devices.toList().sumOf { it.quantity } == 10, "Total devices must be 10")
	}

	@Test
	fun userAbleToBorrowADevice() {
		val tester = userRepository.findById(1).get()
		val iPhoneX = phoneDeviceRepository.findByModel("iPhone X")
		bookingService.borrowDevice(tester, iPhoneX)
		val devices = bookingService.findCirculationDevices()
		assert(devices.toList().sumOf { it.quantity } == 9)
		var availableTypeDevices = 0
		var unavailableTypeDevice = 0
		devices.toList().stream().forEach {
			if(it.isAvailable)
				availableTypeDevices++
			else
				unavailableTypeDevice++
		}
		assert(availableTypeDevices == 8)
		assert(unavailableTypeDevice == 1)
		val transaction = bookingTxnRepository.findUnreturnedTxnsByBorrowerAndDevice(tester, iPhoneX)
		assertNotNull(transaction.device)
		assertNotNull(transaction.borrower)
		assertNotNull(transaction.fromDate)
		assertNull(transaction.returnedDate)
	}

	@Test
	fun userAbleToSeeCorrectLatestCirculation(){
		userAbleToBorrowADevice()
		val devices = bookingService.findCirculationDevices()
		assert(devices.toList().sumOf { it.quantity } == 9)
		devices.forEach{
			if(it.model.equals("iPhone X")) {
				assertTrue((it.transactions?.size ?: 0) == 1, "Iphone X should have 1 transaction recorded")
				assertTrue(it.quantity == 0, "Iphone X availability should be zero")
				assertTrue(it.transactions?.single()?.borrower?.email.equals("jhon@email.com"))
			} else {
				assertTrue((it.transactions?.size ?: 0) == 0, "Phone model ${it.model} has ${(it.transactions?.size ?: 0)} transaction(s)")
				Assertions.assertThat(it.quantity).isBetween(1, 2)
			}
		}
	}

	@Test
	fun userAbleToReturnTheDevice() {
		userAbleToBorrowADevice()
		val tester = userRepository.findById(1).get()
		val iPhoneX = phoneDeviceRepository.findByModel("iPhone X")

		bookingService.returnDevice(tester, iPhoneX)

		val devices = bookingService.findCirculationDevices()
		assert(devices.toList().sumOf { it.quantity } == 10)
		val transaction = bookingTxnRepository.findUnreturnedTxnsByBorrowerAndDevice(tester, iPhoneX)
		assertNotNull(transaction.device)
		assertNotNull(transaction.borrower)
		assertNotNull(transaction.fromDate)
		assertNotNull(transaction.returnedDate)
	}

	@Test
	fun borrowOnMultipleDevicesInSingleModel(){
		val tester = userRepository.findById(1).get()
		val admin = userRepository.findById(2).get()
		val galaxyS8 = phoneDeviceRepository.findByModel("Samsung Galaxy S8")
		bookingService.borrowDevice(tester, galaxyS8)
		bookingService.borrowDevice(admin, galaxyS8)
		val devices = bookingService.findCirculationDevices()
		assert(devices.toList().sumOf { it.quantity } == 8)
		devices.forEach{
			if(it.model.equals("Samsung Galaxy S8")) {
				assertTrue(it.isAvailable == false, "Samsung Galaxy S8 fully booked")
				it.transactions?.forEachIndexed{ index, it ->
					Assertions.assertThat(it.borrower.role).isIn(User.Roles.TESTER, User.Roles.ADMIN)
					assertNotNull(it.fromDate, "Samsung Galaxy S8 $index borrowed at ${it.fromDate}")
				}
			}
		}
	}

	@Test
	fun userBorrowUnavailableDevice() {
		userAbleToBorrowADevice()
		val tester = userRepository.findById(1).get()
		val iPhoneX = phoneDeviceRepository.findByModel("iPhone X")
		assertThrows(ResponseStatusException::class.java, { bookingService.borrowDevice(tester, iPhoneX) })
	}

	@Test
	fun userReturnUnrecordedDevice() {
		val tester = userRepository.findById(1).get()
		val iPhoneX = phoneDeviceRepository.findByModel("iPhone X")
		assertThrows(ResponseStatusException::class.java, { bookingService.returnDevice(tester, iPhoneX) })
	}

}
