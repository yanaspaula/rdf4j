package org.eclipse.rdf4j.sail.readonly.backend.bplus.indexes;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.BplusTree;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.IterableThatCouldNeedFurtherFiltering;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.PartialStatement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public abstract class Index {

	abstract Comparator<Statement> getComparator();

	BplusTree<Statement> index;

	final Comparator<Statement> comparator = getComparator();


	public Index(Collection<Statement> collect) {
		ArrayList<Statement> list = new ArrayList<>(collect);

		list.sort(comparator);

		index = new BplusTree<>(list, comparator);

	}


	public IterableThatCouldNeedFurtherFiltering getStatements(Resource subject, IRI predicate, Value object, Resource[] context) {






		PartialStatement find = new PartialStatement(subject, predicate, object, null);
		BplusTree<Statement>.DataNode firstNode = index
			.getFirstNode(find);


		int startIndex;
		boolean keepGoing = true;
		do {

			for (startIndex = 0; startIndex < firstNode.datanodes.length; startIndex++) {
				int compare = comparator.compare(find, (Statement) firstNode.datanodes[startIndex]);
				if (compare <= 0) {
					keepGoing = false;
					break;
				}
			}

			if (startIndex == firstNode.datanodes.length) {
				firstNode = firstNode.next;
			}
		} while (keepGoing);

		BplusTree<Statement>.DataNode finalFirstNode = firstNode;
		int finalStartIndex = startIndex;


		return new IterableThatCouldNeedFurtherFiltering() {
			@Override
			public Iterator<Statement> iterator() {

				return new Iterator<Statement>() {

					BplusTree<Statement>.DataNode lastNode = index
						.getLastNode(new PartialStatement(subject, predicate, object, null));


					BplusTree<Statement>.DataNode currentNode = finalFirstNode;
					int currentIndex = finalStartIndex;


					Statement next;

					void calculateNext() {
						if (next != null) {
							return;
						}


						while (next == null) {


							// inside last node
							if (currentNode == lastNode) {

								if(currentIndex >= currentNode.datanodes.length) break;

								int compare = comparator.compare(find, (Statement) currentNode.datanodes[currentIndex]);

								if(compare == 0){
									next = (Statement) currentNode.datanodes[currentIndex];
									currentIndex++;
								}else{
									break;
									// exhausted!
								}
							}


							// inside the currentNode
							else if (currentIndex < currentNode.datanodes.length) {
								next = (Statement) currentNode.datanodes[currentIndex];
								currentIndex++;

							}else{

								currentIndex = 0;
								currentNode = currentNode.next;
							}



						}


					}

					@Override
					public boolean hasNext() {
						calculateNext();


						return next != null;
					}

					@Override
					public Statement next() {

						calculateNext();

						Statement temp = next;
						next = null;
						return temp;
					}
				};
			}

			@Override
			public boolean isNeedsFurtherFiltering() {
				return false;
			}
		};


	}

}
