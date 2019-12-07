package org.eclipse.rdf4j.sail.readonly.backend.bplus.indexes;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.comparators.SPOCComparator;

import java.util.Collection;
import java.util.Comparator;

public class SPOIndex extends Index {

	public SPOIndex(Collection<Statement> collect) {
		super(collect);
	}

	public boolean isEmpty() {
		return false;
	}

	@Override
	Comparator<Statement> getComparator() {
		return new SPOCComparator();
	}

}
