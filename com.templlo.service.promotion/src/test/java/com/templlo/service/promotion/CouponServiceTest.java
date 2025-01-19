// package com.templlo.service.promotion;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;
// import java.util.concurrent.TimeUnit;
//
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.redisson.api.RLock;
// import org.redisson.api.RedissonClient;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.core.ValueOperations;
// import org.springframework.data.redis.core.script.RedisScript;
//
// import com.templlo.service.common.client.ProgramFeignClient;
// import com.templlo.service.common.client.UserFeignClient;
// import com.templlo.service.common.dto.DetailProgramResponse;
// import com.templlo.service.common.response.ApiResponse;
// import com.templlo.service.coupon.dto.CouponDeleteResponseDto;
// import com.templlo.service.coupon.dto.CouponIssueResponseDto;
// import com.templlo.service.coupon.dto.CouponTransferResponseDto;
// import com.templlo.service.coupon.dto.CouponUseResponseDto;
// import com.templlo.service.coupon.entity.Coupon;
// import com.templlo.service.coupon.repository.CouponRepository;
// import com.templlo.service.coupon.service.CouponService;
// import com.templlo.service.kafka.KafkaProducerService;
// import com.templlo.service.promotion.entity.Promotion;
// import com.templlo.service.coupon.helper.RedisPromotionHelper;
// import com.templlo.service.promotion.helper.MockRedisPromotionHelper;
// import com.templlo.service.promotion.repository.PromotionRepository;
// import com.templlo.service.user_coupon.entity.UserCoupon;
// import com.templlo.service.user_coupon.repository.UserCouponRepository;
//
// class CouponServiceTest {
//
// 	@InjectMocks
// 	private CouponService couponService;
//
// 	@Mock
// 	private CouponRepository couponRepository;
// 	@Mock
// 	private UserCouponRepository userCouponRepository;
// 	@Mock
// 	private PromotionRepository promotionRepository;
// 	@Mock
// 	private UserFeignClient userFeignClient;
// 	@Mock
// 	private ProgramFeignClient programFeignClient;
// 	@Mock
// 	private KafkaProducerService kafkaProducerService;
//
// 	@Mock
// 	private RedisTemplate<String, Object> redisTemplate;
// 	@Mock
// 	private ValueOperations<String, Object> valueOperations;
// 	@Mock
// 	private RedissonClient redissonClient;
// 	@Mock
// 	private RLock rLock;
//
// 	private RedisPromotionHelper redisPromotionHelper;
//
// 	@BeforeEach
// 	void setUp() {
// 		MockitoAnnotations.openMocks(this);
//
// 		// Mock RedisPromotionHelper 인스턴스 생성
// 		redisPromotionHelper = spy(new MockRedisPromotionHelper());
//
// 		// CouponService에 Mock RedisPromotionHelper 주입
// 		couponService = new CouponService(
// 			couponRepository,
// 			userCouponRepository,
// 			promotionRepository,
// 			redissonClient,
// 			kafkaProducerService,
// 			redisTemplate, // RedisTemplate 전달
// 			programFeignClient,
// 			redisPromotionHelper // MockRedisPromotionHelper 사용
// 		);
//
// 		// RedisTemplate Mock 설정
// 		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
// 		when(valueOperations.get("promotion:remaining")).thenReturn(100L);
// 		when(valueOperations.get("promotion:issued")).thenReturn(0L);
//
// 		// RedissonClient Mock 설정
// 		when(redissonClient.getLock(anyString())).thenReturn(rLock);
// 		try {
// 			when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
// 		} catch (InterruptedException e) {
// 			throw new RuntimeException("Mock setup error: InterruptedException 발생", e);
// 		}
// 	}
//
// 	@AfterEach
// 	void tearDown() {
// 		redisTemplate.delete("promotion:remaining");
// 		redisTemplate.delete("promotion:issued");
// 	}
//
// 	@Test
// 	void testIssueCoupon_Success() {
// 		// Given
// 		UUID promotionId = UUID.randomUUID();
// 		UUID userId = UUID.randomUUID();
// 		String userLoginId = "testUser";
//
// 		Promotion promotion = Promotion.builder()
// 			.promotionId(promotionId)
// 			.totalCoupons(100)
// 			.build();
//
// 		Coupon coupon = Coupon.builder()
// 			.couponId(UUID.randomUUID())
// 			.build();
//
// 		// Mock 설정
// 		when(promotionRepository.findById(promotionId)).thenReturn(Optional.of(promotion));
// 		when(userCouponRepository.existsByUserIdAndCoupon_Promotion_PromotionId(userId, promotionId)).thenReturn(false);
// 		when(couponRepository.findFirstByPromotionAndStatus(promotion, "AVAILABLE")).thenReturn(Optional.of(coupon));
//
// 		// When
// 		CouponIssueResponseDto response = couponService.issueCoupon(promotionId, null, userId, userLoginId);
//
// 		// Then
// 		assertNotNull(response, "응답이 null이어서는 안 됩니다.");
// 		assertEquals("SUCCESS", response.status(), "응답 상태가 SUCCESS이어야 합니다.");
// 		assertEquals(coupon.getCouponId(), response.couponId(), "발급된 쿠폰 ID가 일치해야 합니다.");
// 	}
//
// 	@Test
// 	void testUseCoupon_Success() {
// 		// Given
// 		UUID couponId = UUID.randomUUID();
// 		UUID programId = UUID.randomUUID();
// 		LocalDate programDate = LocalDate.now();
//
// 		Coupon coupon = Coupon.builder()
// 			.couponId(couponId)
// 			.status("ISSUED")
// 			.discountType("PERCENTAGE")
// 			.value(BigDecimal.TEN)
// 			.build();
//
// 		DetailProgramResponse programResponse = new DetailProgramResponse(
// 			programId, "NORMAL", BigDecimal.valueOf(100), programDate
// 		);
//
// 		when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
// 		when(programFeignClient.getProgram(programId, programDate)).thenReturn(
// 			ApiResponse.ofSuccess(programResponse));
//
// 		// When
// 		CouponUseResponseDto response = couponService.useCoupon(couponId, programId, programDate);
//
// 		// Then
// 		assertNotNull(response);
// 		assertEquals("SUCCESS", response.status());
// 		assertEquals(0, BigDecimal.valueOf(90).compareTo(response.finalPrice()));
// 	}
//
// 	@Test
// 	void testTransferCoupon_Success() {
// 		// Given
// 		UUID couponId = UUID.randomUUID();
// 		UUID toUserId = UUID.randomUUID();
// 		UUID currentUserId = UUID.randomUUID();
//
// 		UserCoupon userCoupon = UserCoupon.builder()
// 			.userId(currentUserId)
// 			.status("ISSUED")
// 			.build();
//
// 		when(userCouponRepository.findByCouponId(couponId)).thenReturn(Optional.of(userCoupon));
// 		when(userCouponRepository.save(any(UserCoupon.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
// 		// When
// 		CouponTransferResponseDto response = couponService.transferCoupon(couponId, toUserId);
//
// 		// Then
// 		assertNotNull(response);
// 		assertEquals("SUCCESS", response.status());
// 		assertEquals("쿠폰이 성공적으로 양도되었습니다.", response.message());
// 	}
//
// 	@Test
// 	void testDeleteUserCoupon_Success() {
// 		// Given
// 		UUID userId = UUID.randomUUID();
// 		UUID couponId = UUID.randomUUID();
// 		UserCoupon userCoupon = UserCoupon.builder().build();
//
// 		when(userCouponRepository.findByUserIdAndCouponId(userId, couponId)).thenReturn(Optional.of(userCoupon));
//
// 		// When
// 		CouponDeleteResponseDto response = couponService.deleteUserCoupon(userId, couponId);
//
// 		// Then
// 		assertNotNull(response);
// 		assertEquals("SUCCESS", response.status());
// 		assertEquals("쿠폰이 성공적으로 삭제되었습니다.", response.message());
// 	}
//
// 	@Test
// 	void testLuaScriptExecution() {
// 		// Given
// 		String key = "promotion:remaining";
// 		RedisScript<Long> mockScript = mock(RedisScript.class);
//
// 		when(redisTemplate.execute(eq(mockScript), anyList(), any())).thenReturn(1L);
//
// 		// When
// 		Long result = redisTemplate.execute(mockScript, List.of(key), new Object[0]);
//
// 		// Then
// 		assertNotNull(result, "Lua 스크립트 실행 결과가 null이어서는 안 됩니다.");
// 		assertEquals(1L, result, "Lua 스크립트 실행 결과는 1이어야 합니다.");
// 	}
// }
