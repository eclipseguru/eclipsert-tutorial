/*******************************************************************************
 * Copyright (c) 2012 AGETO Service GmbH and others.
 * All rights reserved.
 *  
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Distribution License v1.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/org/documents/edl-v10.html.
 *
 * Contributors:
 *     Gunnar Wagenknecht - initial API and implementation
 *******************************************************************************/
/*******************************************************************************
 * This is sample code. Use at your own risk. It comes WITHOUT any warranty.
 * Released to public domain. Please copy & paste as you wish.
 *
 * Initial Contribution: Gunnar Wagenknecht
 *******************************************************************************/
package hello.preferences;

import hello.service.GreetingServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.gyrex.preferences.CloudScope;
import org.eclipse.gyrex.preferences.ModificationConflictException;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * Extension of {@link GreetingServiceImpl} which stores greetings into
 * OSGi/Eclipse Preferences using the {@link CloudScope} contributed by Gyrex.
 * This allows storing into ZooKeeper.
 */
public class PreferenceBasedGreetingServiceImpl extends GreetingServiceImpl {

	private static final String NODE_UNPROCESSED = "unprocessed";
	private static final String NODE_PROCESSED = "processed";
	private static final String KEY_PROCESSED_ON = "processedOn";
	private static final String KEY_PROCESSED_BY = "processedBy";
	private static final String KEY_CREATED = "created";
	private static final String KEY_SUBMITTED_BY = "submittedBy";
	private static final String KEY_TEXT = "text";

	public PreferenceBasedGreetingServiceImpl(final String myNodeId) {
		super(myNodeId);
	}

	private void add(final Greeting greeting, final String pathName) {
		//  create new node with greeting
		final Preferences greetingsNode = getGreetingsNode().node(pathName);
		final Preferences node = greetingsNode.node(DateFormatUtils.format(greeting.getCreated(), "yyyyMMddHHmmssSSS"));

		// populate
		node.putLong(KEY_CREATED, greeting.getCreated());
		node.put(KEY_TEXT, greeting.getText());
		node.put(KEY_SUBMITTED_BY, greeting.getSubmittedBy());
		if ((greeting.getProcessedBy() != null) && (greeting.getProcessedOn() > 0)) {
			node.putLong(KEY_PROCESSED_ON, greeting.getProcessedOn());
			node.put(KEY_PROCESSED_BY, greeting.getProcessedBy());
		}

		// flush
		try {
			greetingsNode.flush();
		} catch (final BackingStoreException e) {
			throw new RuntimeException("Error persisting greeting!", e);
		}
	}

	@Override
	protected void addProcessed(final Greeting greeting) {
		add(greeting, NODE_PROCESSED);
	}

	@Override
	protected void addUnprocessed(final Greeting greeting) {
		add(greeting, NODE_UNPROCESSED);
	}

	private Preferences getGreetingsNode() {
		return CloudScope.INSTANCE.getNode("hello.persistence").node("greetings");
	}

	@Override
	protected List<Greeting> loadAll() {
		try {
			final List<Greeting> greetings = new ArrayList<Greeting>();
			greetings.addAll(loadAll(NODE_PROCESSED));
			greetings.addAll(loadAll(NODE_UNPROCESSED));
			return greetings;
		} catch (final BackingStoreException e) {
			throw new RuntimeException("Error loading greetings!", e);
		}
	}

	private Collection<? extends Greeting> loadAll(final String path) throws BackingStoreException {
		if (!getGreetingsNode().nodeExists(path)) {
			return Collections.emptyList();
		}

		final Preferences greetingsNode = getGreetingsNode().node(path);
		final String[] childrenNames = greetingsNode.childrenNames();
		if (childrenNames.length == 0) {
			return Collections.emptyList();
		}

		// sort
		Arrays.sort(childrenNames);

		// generate greetings
		final List<Greeting> greetings = new ArrayList<Greeting>(childrenNames.length);
		for (final String childrenName : childrenNames) {
			final Preferences node = greetingsNode.node(childrenName);
			final Greeting greeting = loadGreeting(node);
			greetings.add(greeting);
		}
		return greetings;
	}

	private Greeting loadGreeting(final Preferences node) {
		final Greeting greeting = new Greeting(node.getLong(KEY_CREATED, 0), node.get(KEY_TEXT, ""), node.get(KEY_SUBMITTED_BY, ""));
		if ((node.get(KEY_PROCESSED_ON, null) != null) && (node.get(KEY_PROCESSED_BY, null) != null)) {
			greeting.process(node.getLong(KEY_PROCESSED_ON, 0), node.get(KEY_PROCESSED_BY, null));
		}
		return greeting;
	}

	@Override
	protected Greeting nextUnprocessed() {
		try {
			if (!getGreetingsNode().nodeExists(NODE_UNPROCESSED)) {
				return null;
			}

			final Preferences greetingsNode = getGreetingsNode().node(NODE_UNPROCESSED);

			// sync with backend
			greetingsNode.sync();

			// get first child
			final String[] childrenNames = greetingsNode.childrenNames();
			if (childrenNames.length == 0) {
				return null;
			}
			Arrays.sort(childrenNames);

			final Preferences node = greetingsNode.node(childrenNames[0]);
			final Greeting greeting = loadGreeting(node);
			node.removeNode();
			greetingsNode.flush();
			return greeting;
		} catch (final IllegalStateException e) {
			// someone else was faster (node already removed)
			return null;
		} catch (final ModificationConflictException e) {
			// someone else was faster
			return null;
		} catch (final BackingStoreException e) {
			throw new RuntimeException("Error retrieving next unprocessed greeting!", e);
		}
	}

}
