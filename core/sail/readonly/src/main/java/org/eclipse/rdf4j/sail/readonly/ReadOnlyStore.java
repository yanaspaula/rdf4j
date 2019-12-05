/* @formatter:off */
/*******************************************************************************
 * Copyright (c) 2019 Eclipse RDF4J contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.sail.readonly;

import org.eclipse.rdf4j.IsolationLevel;
import org.eclipse.rdf4j.IsolationLevels;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.sail.NotifyingSailConnection;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.extensiblestore.ExtensibleStore;
import org.eclipse.rdf4j.sail.extensiblestore.SimpleMemoryNamespaceStore;
import org.eclipse.rdf4j.sail.readonly.backend.ReadOnlyBackend;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Håvard Mikkelsen Ottestad
 */
public class ReadOnlyStore extends ExtensibleStore<ReadOnlyBackend, SimpleMemoryNamespaceStore> {


	public ReadOnlyStore(ReadOnlyBackendFactoryInterface backend, Collection<Statement> statements, Collection<Statement> statementsInferred) {
		dataStructureInferred = backend.supplyBackend(statementsInferred);
		dataStructure = backend.supplyBackend(statements);
		namespaceStore = new SimpleMemoryNamespaceStore();

	}

	public ReadOnlyStore(ReadOnlyBackendFactoryInterface backend, Collection<Statement> statements) {
		dataStructureInferred = backend.supplyBackend(Collections.EMPTY_LIST);
		dataStructure = backend.supplyBackend(statements);
		namespaceStore = new SimpleMemoryNamespaceStore();
	}


	@Override
	protected NotifyingSailConnection getConnectionInternal() throws SailException {
		return new ReadOnlyConnection(this);
	}

	@Override
	public boolean isWritable() throws SailException {
		return false;
	}

	@Override
	public IsolationLevel getDefaultIsolationLevel() {
		return IsolationLevels.NONE;
	}
}
