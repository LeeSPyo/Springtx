package hello.springtx.propagetion;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hello.springtx.propagation.LogRepository;
import hello.springtx.propagation.MemberRepository;
import hello.springtx.propagation.MemberService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class MemberServiceTest {
	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	LogRepository logRepository;

	/**
	 * MemberService @Transactional:OFF MemberRepository @Transactional:ON
	 * LogRepository @Transactional:ON
	 */
	@Test
	void outerTxOff_success() {
		// given
		String username = "outerTxOff_success";
		// when
		memberService.joinV1(username);
		// then: ��� �����Ͱ� ���� ����ȴ�.
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isPresent());
	}

	/**
	 * MemberService @Transactional:OFF 
	 * MemberRepository @Transactional:ON
	 * LogRepository @Transactional:ON Exception
	 */
	@Test
	void outerTxOff_fail() {
		// given
		String username = "�α׿���_outerTxOff_fail";
		// when
		assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);
		// then: ������ �ѹ���� �ʰ�, member �����Ͱ� ���Ƽ� ����ȴ�.
		assertTrue(memberRepository.find(username).isPresent());
		assertTrue(logRepository.find(username).isEmpty());
	}
}