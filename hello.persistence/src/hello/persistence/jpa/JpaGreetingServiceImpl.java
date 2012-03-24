/**
 * Copyright (c) 2012 AGETO Service GmbH and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 */
package hello.persistence.jpa;

import hello.service.GreetingServiceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 * Extension of {@link GreetingServiceImpl} which stores greetings into a
 * persistent data store.
 */
public class JpaGreetingServiceImpl extends GreetingServiceImpl {

	private final EntityManagerFactory entityManagerFactory;

	/**
	 * Creates a new instance.
	 * 
	 * @param myNodeId
	 * @param entityManagerFactory
	 */
	public JpaGreetingServiceImpl(final String myNodeId, final EntityManagerFactory entityManagerFactory) {
		super(myNodeId);
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	protected void addProcessed(final Greeting greeting) {
		final EntityManager em = getEntityManagerFactory().createEntityManager();
		final GreetingEntity entity = new GreetingEntity();
		entity.setCreated(greeting.getCreated());
		entity.setSubmittedBy(greeting.getSubmittedBy());
		entity.setText(greeting.getText());
		entity.setProcessedOn(greeting.getProcessedOn());
		entity.setProcessedBy(greeting.getProcessedBy());
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
		em.close();
	}

	@Override
	protected void addUnprocessed(final Greeting greeting) {
		final EntityManager em = getEntityManagerFactory().createEntityManager();
		final GreetingEntity entity = new GreetingEntity();
		entity.setCreated(greeting.getCreated());
		entity.setSubmittedBy(greeting.getSubmittedBy());
		entity.setText(greeting.getText());
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
		em.close();
	}

	public void close() {
		getEntityManagerFactory().close();
	}

	/**
	 * Returns the entityManagerFactory.
	 * 
	 * @return the entityManagerFactory
	 */
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	@Override
	protected List<Greeting> loadAll() {
		final EntityManager em = getEntityManagerFactory().createEntityManager();
		final TypedQuery<GreetingEntity> q = em.createQuery("SELECT g FROM GreetingEntity g ORDER BY g.created desc", GreetingEntity.class);
		final List<GreetingEntity> results = q.getResultList();
		final List<Greeting> greetings = new ArrayList<GreetingServiceImpl.Greeting>(results.size());
		for (final GreetingEntity entity : results) {
			final Greeting greeting = new Greeting(entity.getCreated(), entity.getText(), entity.getSubmittedBy());
			if (entity.getProcessedOn() > 0) {
				greeting.process(entity.getProcessedOn(), entity.getProcessedBy());
			}
			greetings.add(greeting);
		}
		em.close();
		return greetings;
	}

	@Override
	protected Greeting nextUnprocessed() {
		final EntityManager em = getEntityManagerFactory().createEntityManager();
		final TypedQuery<GreetingEntity> q = em.createQuery("SELECT g FROM GreetingEntity g WHERE g.processedOn == 0 ORDER BY g.created desc", GreetingEntity.class);
		q.setMaxResults(1);
		final List<GreetingEntity> results = q.getResultList();
		if (results.isEmpty()) {
			return null;
		}

		final GreetingEntity entity = results.get(0);
		em.getTransaction().begin();
		final Greeting greeting = new Greeting(entity.getCreated(), entity.getText(), entity.getSubmittedBy());
		if (entity.getProcessedOn() > 0) {
			greeting.process(entity.getProcessedOn(), entity.getProcessedBy());
		}
		em.remove(entity);
		em.getTransaction().commit();
		em.close();
		return greeting;
	}

}
