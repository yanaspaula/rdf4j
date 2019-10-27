/* @formatter:off */
/*******************************************************************************
 * Copyright (c) 2019 Eclipse RDF4J contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.sail.readonly;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.algebra.evaluation.impl.EvaluationStatistics;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.base.SailSource;
import org.eclipse.rdf4j.sail.base.SailStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Håvard Mikkelsen Ottestad
 */
public class ReadOnlySailStore implements SailStore {

	private MemNamespaceStore mns = new MemNamespaceStore();

	private ReadOnlySailSource sailSource;
	private ReadOnlySailSource sailSourceInferred;

	public ReadOnlySailStore(ReadOnlyBackendFactoryInterface backendFactory, Collection<Statement> statements, Collection<Statement> inferredStatements) {
		sailSource = new ReadOnlySailSource(backendFactory.supplyBackend(statements), mns);
		sailSourceInferred = new ReadOnlySailSource(backendFactory.supplyBackend(inferredStatements), mns);
	}

	public ReadOnlySailStore(ReadOnlyBackendFactoryInterface backendFactory, Collection<Statement> statements) {
		sailSource = new ReadOnlySailSource(backendFactory.supplyBackend(statements), mns);
		sailSourceInferred = new ReadOnlySailSource(backendFactory.supplyBackend(Collections.emptyList()), mns);
	}

	@Override
	public void close() throws SailException {

	}

	@Override
	public ValueFactory getValueFactory() {
		return SimpleValueFactory.getInstance();
	}

	@Override
	public EvaluationStatistics getEvaluationStatistics() {
		return new EvaluationStatistics() {
		};
	}

	@Override
	public SailSource getExplicitSailSource() {
		return sailSource;
	}

	@Override
	public SailSource getInferredSailSource() {
		return sailSourceInferred;
	}


}
