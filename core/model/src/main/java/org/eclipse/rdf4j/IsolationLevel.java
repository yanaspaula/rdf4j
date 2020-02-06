/*******************************************************************************
 * Copyright (c) 2015 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j;

import java.util.Arrays;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.SESAME;

/**
 * A Transaction Isolation Level. Default levels supported by RDF4J are provided as constants, third-party triplestore
 * implementors may choose to add additional IsolationLevel implementations if their triplestore's isolation contract is
 * different from what is provided by default.
 * 
 * @author Jeen Broekstra
 */
public interface IsolationLevel {

	/**
	 * Verifies if this transaction isolation level is compatible with the supplied other isolation level - that is, if
	 * this transaction isolation level offers at least the same guarantees as the other level. By definition, every
	 * transaction isolation level is compatible with itself.
	 * 
	 * @param otherLevel an other isolation level to check compatibility against.
	 * @return true iff this isolation level is compatible with the supplied other isolation level, false otherwise.
	 */
	boolean isCompatibleWith(IsolationLevel otherLevel);

	/**
	 * Get a URI uniquely representing this isolation level.
	 * 
	 * @return a URI that uniquely represents this isolation level.
	 */
	IRI getURI();

	/**
	 * None: the lowest isolation level; transactions can see their own changes, but may not be able to roll them back
	 * and no support for isolation among transactions is guaranteed
	 */
	final IsolationLevel NONE = new IsolationLevelImpl("NONE", IsolationLevels.NONE);

	/**
	 * Read Uncommitted: transactions can be rolled back, but not necessarily isolated: concurrent transactions might
	 * see each other's uncommitted data (so-called 'dirty reads')
	 */
	final IsolationLevel READ_UNCOMMITTED = new IsolationLevelImpl("READ_UNCOMMITTED", NONE,
			IsolationLevels.READ_UNCOMMITTED);

	/**
	 * Read Committed: in this isolation level only statements from other transactions that have been committed (at some
	 * point) can be seen by this transaction.
	 */
	final IsolationLevel READ_COMMITTED = new IsolationLevelImpl("READ_COMMITTED", READ_UNCOMMITTED, NONE,
			IsolationLevels.READ_COMMITTED);

	/**
	 * Snapshot Read: in addition to {@link #READ_COMMITTED}, query results in this isolation level that are observed
	 * within a successful transaction will observe a consistent snapshot. Changes to the data occurring while a query
	 * is evaluated will not affect that query result.
	 */
	final IsolationLevel SNAPSHOT_READ = new IsolationLevelImpl("SNAPSHOT_READ", READ_COMMITTED, READ_UNCOMMITTED, NONE,
			IsolationLevels.SNAPSHOT_READ);

	/**
	 * Snapshot: in addition to {@link #SNAPSHOT_READ}, successful transactions in this isolation level will operate
	 * against a particular dataset snapshot. Transactions in this isolation level will see either the complete effects
	 * of other transactions (consistently throughout) or not at all.
	 */
	final IsolationLevel SNAPSHOT = new IsolationLevelImpl("SNAPSHOT", SNAPSHOT_READ, READ_COMMITTED, READ_UNCOMMITTED, NONE,
			IsolationLevels.SNAPSHOT);

	/**
	 * Serializable: in addition to {@link #SNAPSHOT}, this isolation level requires that all other successful
	 * transactions must appear to occur either completely before or completely after a successful serializable
	 * transaction.
	 */
	final IsolationLevel SERIALIZABLE = new IsolationLevelImpl("SERIALIZABLE", SNAPSHOT, SNAPSHOT_READ, READ_COMMITTED,
			READ_UNCOMMITTED, NONE, IsolationLevels.SERIALIZABLE);

	class IsolationLevelImpl implements IsolationLevel {

		private final List<? extends IsolationLevel> compatibleLevels;

		private final String name;

		private IsolationLevelImpl(String name, IsolationLevel... compatibleLevels) {
			this.name = name;
			this.compatibleLevels = Arrays.asList(compatibleLevels);
		}

		@Override
		public boolean isCompatibleWith(IsolationLevel otherLevel) {
			return this.equals(otherLevel) || compatibleLevels.contains(otherLevel);
		}

		@Override
		public IRI getURI() {
			final ValueFactory f = SimpleValueFactory.getInstance();
			return f.createIRI(SESAME.NAMESPACE, this.name);
		}
	}

}
