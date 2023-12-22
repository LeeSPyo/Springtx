package hello.springtx.propagation;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final LogRepository logRepository;

	public void joinV1(String username) {
		Member member = new Member(username);
		Log logMessage = new Log(username);
		log.info("== memberRepository ȣ�� ���� ==");
		memberRepository.save(member);
		log.info("== memberRepository ȣ�� ���� ==");
		log.info("== logRepository ȣ�� ���� ==");
		logRepository.save(logMessage);
		log.info("== logRepository ȣ�� ���� ==");
	}

	public void joinV2(String username) {
		Member member = new Member(username);
		Log logMessage = new Log(username);
		log.info("== memberRepository ȣ�� ���� ==");
		memberRepository.save(member);
		log.info("== memberRepository ȣ�� ���� ==");
		log.info("== logRepository ȣ�� ���� ==");
		try {
			logRepository.save(logMessage);
		} catch (RuntimeException e) {
			log.info("log ���忡 �����߽��ϴ�. logMessage={}", logMessage.getMessage());
			log.info("���� �帧 ��ȯ");
		}
		log.info("== logRepository ȣ�� ���� ==");
	}
}