package org.eclipse.rdf4j.sail.readonly;

import org.eclipse.rdf4j.model.Statement;

import java.util.Collection;

public interface ReadOnlyBackendFactoryInterface {

	ReadOnlyBackendInterface supplyBackend(Collection<Statement> statements);

}
