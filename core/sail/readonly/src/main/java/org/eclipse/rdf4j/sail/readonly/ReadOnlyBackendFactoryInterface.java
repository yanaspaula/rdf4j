package org.eclipse.rdf4j.sail.readonly;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.sail.extensiblestore.DataStructureInterface;
import org.eclipse.rdf4j.sail.readonly.backend.ReadOnlyBackend;

import java.util.Collection;

public interface ReadOnlyBackendFactoryInterface {

	ReadOnlyBackend supplyBackend(Collection<Statement> statements);

}
