package hello.springtx.propagetion;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class BasicTxTest {
	@Autowired
	PlatformTransactionManager txManager;

	@TestConfiguration
	static class Config {
		@Bean
		public PlatformTransactionManager transactionManager(DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}
	}

	@Test
	void commit() {
		log.info("Ʈ����� ����");
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("Ʈ����� Ŀ�� ����");
		txManager.commit(status);
		log.info("Ʈ����� Ŀ�� �Ϸ�");
	}

	@Test
	void rollback() {
		log.info("Ʈ����� ����");
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("Ʈ����� �ѹ� ����");
		txManager.rollback(status);
		log.info("Ʈ����� �ѹ� �Ϸ�");
	}
}
