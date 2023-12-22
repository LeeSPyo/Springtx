package hello.springtx.propagation;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LogRepository {
	private final EntityManager em;

	//@Transactional
	public void save(Log logMessage) {
		log.info("log ����");
		em.persist(logMessage);
		if (logMessage.getMessage().contains("�α׿���")) {
			log.info("log ����� ���� �߻�");
			throw new RuntimeException("���� �߻�");
		}
	}

	public Optional<Log> find(String message) {
		return em.createQuery("select l from Log l where l.message = :message", Log.class)
				.setParameter("message", message).getResultList().stream().findAny();
	}
}