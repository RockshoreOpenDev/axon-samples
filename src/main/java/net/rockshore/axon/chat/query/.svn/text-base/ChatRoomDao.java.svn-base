package net.rockshore.axon.chat.query;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class ChatRoomDao {
	@PersistenceContext
	private EntityManager em;

	public void add(ChatRoomView view) {
		em.persist(view);
	}

	public List<ChatRoomView> findAll() {
		return em.createQuery("from ChatRoomView order by name",
				ChatRoomView.class).getResultList();
	}
}
