/* @formatter:off */
/*******************************************************************************
 * Copyright (c) 2019 Eclipse RDF4J contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.sail.readonly.backend;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteratorIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.extensiblestore.valuefactory.ExtensibleStatement;
import org.eclipse.rdf4j.sail.extensiblestore.valuefactory.ExtensibleStatementHelper;

import java.util.Iterator;

public class ComparingIterator implements CloseableIteration<ExtensibleStatement, SailException> {

	private Statement next;

	private final CloseableIteration<? extends Statement, SailException> iterator;
	private final Resource subject;
	private final IRI predicate;
	private final Value object;
	private final Resource[] context;

	public ComparingIterator(CloseableIteration<? extends Statement, SailException> iterator, Resource subject, IRI predicate, Value object, Resource[] context) {
		this.iterator = iterator;
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		this.context = context;
	}

	public ComparingIterator(Iterator<Statement> iterator, Resource subject, IRI predicate, Value object, Resource[] context) {
		this(new CloseableIteratorIteration(iterator), subject, predicate, object, context);
	}

	@Override
	public void close() throws SailException {
	}

	@Override
	public boolean hasNext() throws SailException {
		internalNext();

		return next != null;
	}

	private void internalNext() {
		if (next == null) {
			while (iterator.hasNext()) {

				Statement temp = (Statement) iterator.next();

				if (subject != null && !temp.getSubject().equals(subject)) {
					continue;
				}
				if (predicate != null && !temp.getPredicate().equals(predicate)) {
					continue;
				}
				if (object != null && !temp.getObject().equals(object)) {
					continue;
				}
				if (context.length > 0 && !containsContext(context, temp.getContext())) {
					continue;
				}

				next = temp;
				break;
			}
		}
	}

	@Override
	public ExtensibleStatement next() throws SailException {
		internalNext();
		Statement temp = next;
		next = null;

		return ExtensibleStatementHelper.getDefaultImpl().fromStatement(temp, false);
	}

	@Override
	public void remove() throws SailException {

	}

	private boolean containsContext(Resource[] context, Resource context1) {
		for (Resource resource : context) {
			if (resource == null && context1 == null) {
				return true;
			}
			if (resource != null && resource.equals(context1)) {
				return true;
			}
		}
		return false;
	}

}
