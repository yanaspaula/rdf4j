package org.eclipse.rdf4j.sail.readonly.backend.arraylist;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.sail.readonly.ReadOnlyBackendFactoryInterface;
import org.eclipse.rdf4j.sail.readonly.ReadOnlyBackendInterface;

import java.util.Collection;

public class CollectionReadOnlyBackendFactory implements ReadOnlyBackendFactoryInterface {
	@Override
	public ReadOnlyBackendInterface supplyBackend(Collection<Statement> statements) {
		return new CollectionReadOnlyBackend(statements);
	}
}
