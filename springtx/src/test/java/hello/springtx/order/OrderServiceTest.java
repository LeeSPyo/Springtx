package hello.springtx.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class OrderServiceTest {
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderRepository;

	@Test
	void complete() throws NotEnoughMoneyException {
		//given
		Order order = new Order();
		order.setUsername("����");
		//when
		orderService.order(order);
		//then
		Order findOrder = orderRepository.findById(order.getId()).get();
		assertThat(findOrder.getPayStatus()).isEqualTo("�Ϸ�");
	}

	@Test
	void runtimeException() {
		//given
		Order order = new Order();
		order.setUsername("����");
		//when, then
		assertThatThrownBy(() -> orderService.order(order)).isInstanceOf(RuntimeException.class);
		//then: �ѹ�Ǿ����Ƿ� �����Ͱ� ����� �Ѵ�.
		Optional<Order> orderOptional = orderRepository.findById(order.getId());
		assertThat(orderOptional.isEmpty()).isTrue();
	}

	@Test
	void bizException() {
		//given
		Order order = new Order();
		order.setUsername("�ܰ����");
		//when
		try {
			orderService.order(order);
			fail("�ܰ� ���� ���ܰ� �߻��ؾ� �մϴ�.");
		} catch (NotEnoughMoneyException e) {
			log.info("������ �ܰ� ������ �˸��� ������ ���·� �Ա��ϵ��� �ȳ�");
		}
		//then
		Order findOrder = orderRepository.findById(order.getId()).get();
		assertThat(findOrder.getPayStatus()).isEqualTo("���");
	}
}