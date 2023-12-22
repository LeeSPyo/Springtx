package hello.springtx.propagetion;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
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

	@Test
	void double_commit() {
		log.info("Ʈ�����1 ����");
		TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("Ʈ�����1 Ŀ��");
		txManager.commit(tx1);
		log.info("Ʈ�����2 ����");
		TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("Ʈ�����2 Ŀ��");
		txManager.commit(tx2);
	}

	@Test
	void inner_commit() {
		log.info("�ܺ� Ʈ����� ����");
		TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("outer.isNewTransaction()={}", outer.isNewTransaction());
		log.info("���� Ʈ����� ����");
		TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("inner.isNewTransaction()={}", inner.isNewTransaction());
		log.info("���� Ʈ����� Ŀ��");
		txManager.commit(inner);
		log.info("�ܺ� Ʈ����� Ŀ��");
		txManager.commit(outer);
	}

	@Test
	void outer_rollback() {
		log.info("�ܺ� Ʈ����� ����");
		TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("���� Ʈ����� ����");
		TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("���� Ʈ����� Ŀ��");
		txManager.commit(inner);
		log.info("�ܺ� Ʈ����� �ѹ�");
		txManager.rollback(outer);
	}

	@Test
	void inner_rollback() {
		log.info("�ܺ� Ʈ����� ����");
		TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("���� Ʈ����� ����");
		TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("���� Ʈ����� �ѹ�");
		txManager.rollback(inner);
		log.info("�ܺ� Ʈ����� Ŀ��");
		assertThatThrownBy(() -> txManager.commit(outer)).isInstanceOf(UnexpectedRollbackException.class);
	}

	@Test
	void inner_rollback_requires_new() {
		log.info("�ܺ� Ʈ����� ����");
		TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
		log.info("outer.isNewTransaction()={}", outer.isNewTransaction());
		log.info("���� Ʈ����� ����");
		DefaultTransactionAttribute definition = new DefaultTransactionAttribute();
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus inner = txManager.getTransaction(definition);
		log.info("inner.isNewTransaction()={}", inner.isNewTransaction());
		log.info("���� Ʈ����� �ѹ�");
		txManager.rollback(inner); // �ѹ�
		log.info("�ܺ� Ʈ����� Ŀ��");
		txManager.commit(outer); // Ŀ��
	}

}
