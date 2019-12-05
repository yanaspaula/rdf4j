package org.eclipse.rdf4j.sail.readonly.backend;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.extensiblestore.DataStructureInterface;

public abstract class ReadOnlyBackend implements DataStructureInterface {
	@Override
	public void addStatement(Statement statement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeStatement(Statement statement) {
		throw new UnsupportedOperationException();
	}



	@Override
	public void flushForReading() {

	}

	@Override
	public void init() {

	}

	@Override
	public void flushForCommit() {

	}
}
