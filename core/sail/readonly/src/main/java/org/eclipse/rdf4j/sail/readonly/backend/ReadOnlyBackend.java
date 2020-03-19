package org.eclipse.rdf4j.sail.readonly.backend;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.sail.extensiblestore.DataStructureInterface;
import org.eclipse.rdf4j.sail.extensiblestore.valuefactory.ExtensibleStatement;

public abstract class ReadOnlyBackend implements DataStructureInterface {

	@Override
	public void addStatement(ExtensibleStatement statement) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeStatement(ExtensibleStatement statement) {
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
