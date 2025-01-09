package com.templlo.service.promotion;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import com.templlo.service.coupon.dto.CouponIssueResponseDto;
import com.templlo.service.coupon.entity.Coupon;
import com.templlo.service.coupon.repository.CouponRepository;
import com.templlo.service.coupon.service.CouponService;
import com.templlo.service.promotion.entity.Promotion;
import com.templlo.service.promotion.repository.PromotionRepository;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"coupon-issued-topic"})
class CouponServiceIntegrationTest {

	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private PromotionRepository promotionRepository;

	private Promotion testPromotion;

	@BeforeEach
	void setup() {
		// 프로모션 생성
		testPromotion = promotionRepository.save(Promotion.builder()
			.name("Test Promotion")
			.type("HOTDEAL")
			.startDate(LocalDate.now())
			.endDate(LocalDate.now().plusDays(10))
			.couponType("DISCOUNT")
			.maleCoupons(10)
			.femaleCoupons(10)
			.totalCoupons(20)
			.remainingCoupons(20)
			.build());

		// 쿠폰 생성
		for (int i = 0; i < 10; i++) {
			couponRepository.save(Coupon.builder()
				.promotion(testPromotion)
				.gender("MALE")
				.status("AVAILABLE")
				.build());
		}
	}

	@Test
	void testCouponIssueConcurrency() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(10);

		for (int i = 0; i < 10; i++) {
			executorService.submit(() -> {
				try {
					CouponIssueResponseDto response = couponService.issueCoupon(testPromotion.getPromotionId(), "MALE");
					assertThat(response.status()).isEqualTo("SUCCESS");
				} catch (Exception e) {
					// 발급 실패 허용
					System.err.println(e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		// 남은 쿠폰 확인
		long remainingCoupons = couponRepository.countByPromotionAndGenderAndStatus(testPromotion, "MALE", "AVAILABLE");
		assertThat(remainingCoupons).isEqualTo(0);
	}

	@Test
	void testCouponIssueWithoutAvailableCoupons() {
		// 쿠폰 모두 발급
		for (int i = 0; i < 10; i++) {
			couponService.issueCoupon(testPromotion.getPromotionId(), "MALE");
		}

		// 추가 발급 시도
		assertThrows(IllegalStateException.class, () -> {
			couponService.issueCoupon(testPromotion.getPromotionId(), "MALE");
		});
	}
}
