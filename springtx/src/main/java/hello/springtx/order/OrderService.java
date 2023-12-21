package hello.springtx.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;

	//JPA�� Ʈ����� Ŀ�� ������ Order �����͸� DB�� �ݿ��Ѵ�.
	@Transactional
	public void order(Order order) throws NotEnoughMoneyException {
		log.info("order ȣ��");
		orderRepository.save(order);
		log.info("���� ���μ��� ����");
		if (order.getUsername().equals("����")) {
			log.info("�ý��� ���� �߻�");
			throw new RuntimeException("�ý��� ����");
		} else if (order.getUsername().equals("�ܰ����")) {
			log.info("�ܰ� ���� ����Ͻ� ���� �߻�");
			order.setPayStatus("���");
			throw new NotEnoughMoneyException("�ܰ� �����մϴ�");
		} else {
			//���� ����
			log.info("���� ����");
			order.setPayStatus("�Ϸ�");
		}
		log.info("���� ���μ��� �Ϸ�");
	}
}