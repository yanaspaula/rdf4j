package org.eclipse.rdf4j.sail.readonly.backend.bplus.indexes;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.comparators.PSOCComparator;

import java.util.Collection;
import java.util.Comparator;

public class PSOIndex extends Index {

	public PSOIndex(Collection<Statement> collect) {
		super(collect);
	}

	@Override
	Comparator<Statement> getComparator() {
		return new PSOCComparator();
	}
}
