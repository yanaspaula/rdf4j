package org.eclipse.rdf4j.sail.readonly.backend.bplus.indexes;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.BplusTree;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.ListIterable;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.PartialStatement;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.comparators.PSOCComparator;

import java.util.ArrayList;
import java.util.List;

public class PSOIndex {
	BplusTree<Statement> index;

	public PSOIndex(List<Statement> collect) {

		collect = new ArrayList<>(collect);

		collect.sort(new PSOCComparator());

		index = new BplusTree<>(collect, new PSOCComparator());

	}

	public ListIterable getStatements(Resource subject, IRI predicate, Value object, Resource[] context) {

		BplusTree<Statement>.DataNode firstNode = index
				.getFirstNode(new PartialStatement(subject, predicate, object, null));
		BplusTree<Statement>.DataNode lastNode = index
				.getFirstNode(new PartialStatement(subject, predicate, object, null));

		return null;
	}
}
