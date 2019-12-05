package org.eclipse.rdf4j.sail.readonly.backend.arraylist;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.sail.extensiblestore.DataStructureInterface;
import org.eclipse.rdf4j.sail.readonly.ReadOnlyBackendFactoryInterface;
import org.eclipse.rdf4j.sail.readonly.backend.ReadOnlyBackend;

import java.util.Collection;

public class CollectionReadOnlyBackendFactory implements ReadOnlyBackendFactoryInterface {
	@Override
	public ReadOnlyBackend supplyBackend(Collection<Statement> statements) {
		return new CollectionReadOnlyBackend(statements);
	}
}
