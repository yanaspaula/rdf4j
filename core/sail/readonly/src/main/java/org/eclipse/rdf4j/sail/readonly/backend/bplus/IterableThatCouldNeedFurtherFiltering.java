package org.eclipse.rdf4j.sail.readonly.backend.bplus;

import org.eclipse.rdf4j.model.Statement;

public interface IterableThatCouldNeedFurtherFiltering extends Iterable<Statement> {

	public boolean isNeedsFurtherFiltering();

}
