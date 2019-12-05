package org.eclipse.rdf4j.sail.readonly.backend.linkedhashmodel;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.sail.readonly.ReadOnlyBackendFactoryInterface;
import org.eclipse.rdf4j.sail.readonly.backend.ReadOnlyBackend;

import java.util.Collection;

public class LinkedHashModelReadOnlyBackendFactory implements ReadOnlyBackendFactoryInterface {
	@Override
	public ReadOnlyBackend supplyBackend(Collection<Statement> statements) {
		return new LinkedHashModelReadOnlyBackend(statements);
	}
}
