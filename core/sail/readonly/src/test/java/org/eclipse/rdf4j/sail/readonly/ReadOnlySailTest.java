package org.eclipse.rdf4j.sail.readonly;

import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.SailRepositoryConnection;
import org.eclipse.rdf4j.sail.NotifyingSailConnection;
import org.eclipse.rdf4j.sail.readonly.backend.arraylist.CollectionReadOnlyBackendFactory;
import org.junit.Test;

import java.util.Collections;

public class ReadOnlySailTest {

	@Test
	public void hasStatementTest(){
		ReadOnlyStore readOnlyStore = new ReadOnlyStore(new CollectionReadOnlyBackendFactory(), Collections.emptyList());
		readOnlyStore.init();
		try (NotifyingSailConnection connection = readOnlyStore.getConnection()) {
			connection.hasStatement(null,null,null, true);
		}
	}


	@Test
	public void hasStatementWrappedTest(){
		SailRepository readOnlyStore = new SailRepository(new ReadOnlyStore(new CollectionReadOnlyBackendFactory(), Collections.emptyList()));
		readOnlyStore.init();
		try (SailRepositoryConnection connection = readOnlyStore.getConnection()) {
			connection.hasStatement(null,null,null, true);
		}
	}

}
