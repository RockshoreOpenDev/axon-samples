package net.rockshore.axon.chat.query;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class MessageDao {
	@PersistenceContext
	private EntityManager em;

	public void add(MessageView view) {
		em.persist(view);
	}

	public List<MessageView> findAll() {
		return em.createQuery("from MessageView order by name",
				MessageView.class).getResultList();
	}
}
