package org.eclipse.rdf4j.sail.readonly;

import org.eclipse.rdf4j.common.io.IOUtil;
import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.SailRepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.NotifyingSailConnection;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.readonly.backend.arraylist.CollectionReadOnlyBackendFactory;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.BPlusTreeReadOnlyBackendFactory;
import org.eclipse.rdf4j.sail.readonly.backend.linkedhashmodel.LinkedHashModelReadOnlyBackendFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class ReadOnlySailTest {

	@Test
	public void hasStatementTest() {
		ReadOnlyStore readOnlyStore = new ReadOnlyStore(new CollectionReadOnlyBackendFactory(),
				Collections.emptyList());
		readOnlyStore.init();
		try (NotifyingSailConnection connection = readOnlyStore.getConnection()) {
			connection.hasStatement(null, null, null, true);
		}
	}

	@Test
	public void hasStatementWrappedTest() {
		SailRepository readOnlyStore = new SailRepository(
				new ReadOnlyStore(new CollectionReadOnlyBackendFactory(), Collections.emptyList()));
		readOnlyStore.init();
		try (SailRepositoryConnection connection = readOnlyStore.getConnection()) {
			connection.hasStatement(null, null, null, true);
		}
	}

	@Test
	public void hasStatementWrappedTestBplus() {
		SailRepository readOnlyStore = new SailRepository(
				new ReadOnlyStore(new BPlusTreeReadOnlyBackendFactory(), Collections.emptyList()));
		readOnlyStore.init();
		try (SailRepositoryConnection connection = readOnlyStore.getConnection()) {
			connection.hasStatement(null, null, null, true);
		}
	}

	@Test
	public void sparqlBPlus() throws IOException {
		Model parse = Rio.parse(getResourceAsStream("benchmarkFiles/bsbm-100.ttl"), "", RDFFormat.TURTLE);

		SailRepository repository = new SailRepository(new ReadOnlyStore(new BPlusTreeReadOnlyBackendFactory(), parse));
		repository.init();

		try (SailRepositoryConnection connection = repository.getConnection()) {
			List<BindingSet> bindingSets = Iterations
					.asList(connection.prepareTupleQuery("select * where {?a a ?type}").evaluate());
			bindingSets.forEach(System.out::println);
		}
	}

	@Test
	public void sparqlCollection() throws IOException {
		Model parse = Rio.parse(getResourceAsStream("benchmarkFiles/bsbm-100.ttl"), "", RDFFormat.TURTLE);

		SailRepository repository = new SailRepository(
				new ReadOnlyStore(new CollectionReadOnlyBackendFactory(), parse));
		repository.init();

		try (SailRepositoryConnection connection = repository.getConnection()) {
			List<BindingSet> bindingSets = Iterations.asList(
					connection.prepareTupleQuery(IOUtil.readString(getResourceAsStream("benchmarkFiles/query2.qr")))
							.evaluate());
			bindingSets.forEach(System.out::println);
		}
	}

	@Test
	public void sparqlLinkedHashModel() throws IOException {
		Model parse = Rio.parse(getResourceAsStream("benchmarkFiles/bsbm-100.ttl"), "", RDFFormat.TURTLE);

		SailRepository repository = new SailRepository(
				new ReadOnlyStore(new LinkedHashModelReadOnlyBackendFactory(), parse));
		repository.init();

		try (SailRepositoryConnection connection = repository.getConnection()) {
			TupleQuery tupleQuery = connection
					.prepareTupleQuery(IOUtil.readString(getResourceAsStream("benchmarkFiles/query2.qr")));
			TupleQueryResult evaluate = tupleQuery.evaluate();
			List<BindingSet> bindingSets = Iterations.asList(
					evaluate);
			System.out.println(bindingSets.size());
		}
	}

	@Test
	public void sparqlBtree() throws IOException {
		Model parse = Rio.parse(getResourceAsStream("benchmarkFiles/bsbm-100.ttl"), "", RDFFormat.TURTLE);

		SailRepository repository = new SailRepository(
				new ReadOnlyStore(new BPlusTreeReadOnlyBackendFactory(), parse));
		repository.init();

		try (SailRepositoryConnection connection = repository.getConnection()) {
			TupleQuery tupleQuery = connection
					.prepareTupleQuery(IOUtil.readString(getResourceAsStream("benchmarkFiles/query2.qr")));
			TupleQueryResult evaluate = tupleQuery.evaluate();
			List<BindingSet> bindingSets = Iterations.asList(
					evaluate);
			System.out.println(bindingSets.size());
		}
	}

	@Test
	public void sparqlMemoryStore() throws IOException {
		Model parse = Rio.parse(getResourceAsStream("benchmarkFiles/bsbm-100.ttl"), "", RDFFormat.TURTLE);

		SailRepository repository = new SailRepository(new MemoryStore());
		repository.init();

		try (SailRepositoryConnection connection = repository.getConnection()) {
			connection.add(parse);
		}

		try (SailRepositoryConnection connection = repository.getConnection()) {
			TupleQuery tupleQuery = connection
					.prepareTupleQuery(IOUtil.readString(getResourceAsStream("benchmarkFiles/query2.qr")));
			TupleQueryResult evaluate = tupleQuery.evaluate();
			List<BindingSet> bindingSets = Iterations.asList(
					evaluate);
			System.out.println(bindingSets.size());
		}
	}

	@Test
	public void sparqlBplus2() throws IOException {
		Model parse = Rio.parse(getResourceAsStream("benchmarkFiles/bsbm-100.ttl"), "", RDFFormat.TURTLE);

		SailRepository repository = new SailRepository(
				new ReadOnlyStore(new BPlusTreeReadOnlyBackendFactory(), parse));
		repository.init();

		for (int i = 0; i < 1000; i++) {

			try (SailRepositoryConnection connection = repository.getConnection()) {
				TupleQuery tupleQuery = connection
						.prepareTupleQuery(IOUtil.readString(getResourceAsStream("benchmarkFiles/query2.qr")));
				TupleQueryResult evaluate = tupleQuery.evaluate();
				List<BindingSet> bindingSets = Iterations.asList(
						evaluate);
				if (bindingSets.size() > 2940) {
					System.out.println(bindingSets.size());
				}
			}
		}
	}

	private static InputStream getResourceAsStream(String name) {
		return QueryBenchmark.class.getClassLoader().getResourceAsStream(name);
	}

}
