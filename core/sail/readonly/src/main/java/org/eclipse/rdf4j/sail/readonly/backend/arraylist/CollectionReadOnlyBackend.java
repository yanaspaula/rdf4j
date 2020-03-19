package org.eclipse.rdf4j.sail.readonly.backend.arraylist;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.extensiblestore.valuefactory.ExtensibleStatement;
import org.eclipse.rdf4j.sail.readonly.backend.ComparingIterator;
import org.eclipse.rdf4j.sail.readonly.backend.ReadOnlyBackend;

import java.util.Collection;

public class CollectionReadOnlyBackend extends ReadOnlyBackend {

	private final Collection<Statement> list;

	public CollectionReadOnlyBackend(Collection<Statement> list) {
		this.list = list;
	}

	@Override
	public CloseableIteration<? extends ExtensibleStatement, SailException> getStatements(Resource subject, IRI predicate,
																						  Value object, boolean inferred, Resource... context) {

		return new ComparingIterator(list.iterator(), subject, predicate, object, context);

	}

}
