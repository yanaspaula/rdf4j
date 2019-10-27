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
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteratorIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.base.SailDataset;
import org.eclipse.rdf4j.sail.base.SailSink;
import org.eclipse.rdf4j.sail.base.SailSource;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Håvard Mikkelsen Ottestad
 */
public class ReadOnlySailSource implements SailSource {

	private final ReadOnlyBackendInterface dataStructure;

	private final MemNamespaceStore namespaceStore;

	ReadOnlySailSource(ReadOnlyBackendInterface dataStructure, MemNamespaceStore MemNamespaceStore) {
		this.dataStructure = dataStructure;
		this.namespaceStore = MemNamespaceStore;
	}

	@Override
	public void close() throws SailException {
	}

	@Override
	public SailSource fork() {
		return new ReadOnlySailSource(this.dataStructure, namespaceStore);
	}

	@Override
	public SailSink sink(IsolationLevel level) throws SailException {
		return new SailSink() {
			@Override
			public void prepare() throws SailException {

			}

			@Override
			public void flush() throws SailException {

			}

			@Override
			public synchronized void setNamespace(String prefix, String name) throws SailException {
				namespaceStore.setNamespace(prefix, name);
			}

			@Override
			public synchronized void removeNamespace(String prefix) throws SailException {
				namespaceStore.removeNamespace(prefix);
			}

			@Override
			public synchronized void clearNamespaces() throws SailException {
				namespaceStore.clear();
			}

			@Override
			public void clear(Resource... contexts) throws SailException {

				throw new IllegalStateException("Attempted to clear a context in a read-only sail");

			}

			@Override
			public void observe(Resource subj, IRI pred, Value obj, Resource... contexts) throws SailException {
				throw new RuntimeException("Unsupported operation");
			}

			@Override
			public void approve(Resource subj, IRI pred, Value obj, Resource ctx) throws SailException {
				throw new IllegalStateException("Attempted to clear a context in a read-only sail");

			}

			@Override
			public void deprecate(Resource subj, IRI pred, Value obj, Resource ctx) throws SailException {
				throw new IllegalStateException("Attempted to clear a context in a read-only sail");

			}

			@Override
			public void close() throws SailException {

			}
		};
	}

	@Override
	public SailDataset dataset(IsolationLevel level) throws SailException {
		return new SailDataset() {
			@Override
			public void close() throws SailException {

			}

			@Override
			public String getNamespace(String prefix) throws SailException {
				return namespaceStore.getNamespace(prefix);
			}

			@Override
			public CloseableIteration<? extends Namespace, SailException> getNamespaces() {
				return new CloseableIteratorIteration<Namespace, SailException>(namespaceStore.iterator());
			}

			@Override
			public CloseableIteration<? extends Resource, SailException> getContextIDs() throws SailException {
				return new CloseableIteration<Resource, SailException>() {
					CloseableIteration<? extends Statement, SailException> statements = getStatements(null, null, null);

					Set<Resource> contexts = new HashSet<>();

					Resource next = internalNext();

					private Resource internalNext() {

						while (statements.hasNext()) {
							Statement next = statements.next();
							if (!contexts.contains(next.getContext())) {
								contexts.add(next.getContext());
								return next.getContext();
							}
						}

						return null;

					}

					@Override
					public boolean hasNext() {
						if (next == null) {
							next = internalNext();
						}
						return next != null;
					}

					@Override
					public Resource next() {

						if (next == null) {
							next = internalNext();
						}

						Resource temp = next;
						next = null;

						return temp;
					}

					@Override
					public void remove() {

					}

					@Override
					public void close() {
						statements.close();
					}
				};
			}

			@Override
			public CloseableIteration<? extends Statement, SailException> getStatements(Resource subj, IRI pred,
																						Value obj, Resource... contexts) throws SailException {
				return dataStructure.getStatements(subj, pred, obj, contexts);
			}

		};
	}

	@Override
	public void prepare() throws SailException {
	}

	@Override
	public void flush() throws SailException {
	}



}
