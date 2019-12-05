package org.eclipse.rdf4j.sail.readonly.backend.linkedhashmodel;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.LookAheadIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.readonly.backend.ReadOnlyBackend;

import java.util.Collection;
import java.util.Iterator;

public class LinkedHashModelReadOnlyBackend extends ReadOnlyBackend {

	private final LinkedHashModel model;

	public LinkedHashModelReadOnlyBackend(Collection<Statement> list) {
		this.model = new LinkedHashModel(list);
	}

	@Override
	public CloseableIteration<? extends Statement, SailException> getStatements(Resource subject, IRI predicate,
			Value object, Resource... context) {

		return new LookAheadIteration<Statement, SailException>() {

			Iterator<Statement> iterator = model.filter(subject, predicate, object, context).iterator();

			@Override
			protected Statement getNextElement() throws SailException {
				if (iterator.hasNext()) {
					return iterator.next();
				}
				return null;
			}
		};

	}

}
