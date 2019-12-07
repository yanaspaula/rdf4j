/*******************************************************************************
 * Copyright (c) 2019 Eclipse RDF4J contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.sail.readonly;

import org.apache.commons.io.IOUtils;
import org.eclipse.rdf4j.IsolationLevels;
import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sail.SailRepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.readonly.backend.arraylist.CollectionReadOnlyBackendFactory;
import org.eclipse.rdf4j.sail.readonly.backend.bplus.BPlusTreeReadOnlyBackendFactory;
import org.eclipse.rdf4j.sail.readonly.backend.linkedhashmodel.LinkedHashModelReadOnlyBackendFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Håvard Ottestad
 */
@State(Scope.Benchmark)
@Warmup(iterations = 0)
@BenchmarkMode({ Mode.AverageTime })
//@Fork(value = 1, jvmArgs = { "-Xms8G", "-Xmx8G", "-Xmn4G", "-XX:+UseSerialGC" })
@Fork(value = 1, jvmArgs = {"-Xms8G", "-Xmx8G",/*"-XX:+UnlockExperimentalVMOptions", "-XX:+EnableJVMCI", "-XX:+UseJVMCICompiler",*/ "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=10", "-XX:StartFlightRecording=delay=60s,duration=120s,filename=recording.jfr,settings=profile,", "-XX:FlightRecorderOptions=samplethreads=true,stackdepth=1024", "-XX:+UnlockDiagnosticVMOptions", "-XX:+DebugNonSafepoints"})
@Measurement(iterations = 100)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class QueryBenchmark {

	SailRepository collectionBackedRespository;
	SailRepository lhmBackedRespository;
	SailRepository bptreeBackedRespository;
	SailRepository memoryStore;

	private static final String query1;
	private static final String query2;
	private static final String query3;
	private static final String query4;

	static {
		try {
			query1 = IOUtils.toString(getResourceAsStream("benchmarkFiles/query1.qr"), StandardCharsets.UTF_8);
			query2 = IOUtils.toString(getResourceAsStream("benchmarkFiles/query2.qr"), StandardCharsets.UTF_8);
			query3 = IOUtils.toString(getResourceAsStream("benchmarkFiles/query3.qr"), StandardCharsets.UTF_8);
			query4 = IOUtils.toString(getResourceAsStream("benchmarkFiles/query4.qr"), StandardCharsets.UTF_8);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Setup(Level.Trial)
	public void beforeClass() throws IOException, InterruptedException {

		Model parse = Rio.parse(getResourceAsStream("benchmarkFiles/bsbm-100.ttl"), "", RDFFormat.TURTLE);

		collectionBackedRespository = new SailRepository(
				new ReadOnlyStore(new CollectionReadOnlyBackendFactory(), parse));
		collectionBackedRespository.init();

		lhmBackedRespository = new SailRepository(
				new ReadOnlyStore(new LinkedHashModelReadOnlyBackendFactory(), parse));
		lhmBackedRespository.init();

		bptreeBackedRespository = new SailRepository(
			new ReadOnlyStore(new BPlusTreeReadOnlyBackendFactory(), parse));
		bptreeBackedRespository.init();

		memoryStore = new SailRepository(new MemoryStore());
		memoryStore.init();
		try (SailRepositoryConnection connection = memoryStore.getConnection()) {
			connection.add(parse);
		}

		System.gc();
		Thread.sleep(10);

	}

	private static InputStream getResourceAsStream(String name) {
		return QueryBenchmark.class.getClassLoader().getResourceAsStream(name);
	}

	@TearDown(Level.Trial)
	public void afterClass() {

	}

	@Benchmark
	public List<BindingSet> groupByQueryCollection() {

		try (SailRepositoryConnection connection = collectionBackedRespository.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query1)
					.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> groupByQueryLinkedHashModel() {

		try (SailRepositoryConnection connection = lhmBackedRespository.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query1)
					.evaluate());
		}
	}


	@Benchmark
	public List<BindingSet> groupByQueryBtree() {

		try (SailRepositoryConnection connection = bptreeBackedRespository.getConnection()) {
			return Iterations.asList(connection
				.prepareTupleQuery(query1)
				.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> groupByQueryMemoryStore() {

		try (SailRepositoryConnection connection = memoryStore.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query1)
					.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedQueryBtree() {

		try (SailRepositoryConnection connection = bptreeBackedRespository.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query2)
					.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedQueryLinkedHashModel() {

		try (SailRepositoryConnection connection = lhmBackedRespository.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query2)
					.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedQueryCollection() {

		try (SailRepositoryConnection connection = collectionBackedRespository.getConnection()) {
			return Iterations.asList(connection
				.prepareTupleQuery(query2)
				.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedQueryMemoryStore() {

		try (SailRepositoryConnection connection = memoryStore.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query2)
					.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedNoResultsQueryCollection() {

		try (SailRepositoryConnection connection = collectionBackedRespository.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query3)
					.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedNoResultsQueryBtree() {

		try (SailRepositoryConnection connection = bptreeBackedRespository.getConnection()) {
			return Iterations.asList(connection
				.prepareTupleQuery(query3)
				.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedNoResultsQueryLinkedHashModel() {

		try (SailRepositoryConnection connection = lhmBackedRespository.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query3)
					.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedNoResultsQueryMemoryStore() {

		try (SailRepositoryConnection connection = memoryStore.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query3)
					.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedNoResults2QueryCollection() {

		try (SailRepositoryConnection connection = collectionBackedRespository.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query4)
					.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedNoResults2QueryLinkedHashModel() {

		try (SailRepositoryConnection connection = lhmBackedRespository.getConnection()) {
			return Iterations.asList(connection
					.prepareTupleQuery(query4)
					.evaluate());
		}
	}

	@Benchmark
	public List<BindingSet> linkedNoResults2QueryMemoryStore() {

		throw new UnsupportedOperationException("This benchmark is too slow!");

//		try (SailRepositoryConnection connection = memoryStore.getConnection()) {
//			return Iterations.asList(connection
//				.prepareTupleQuery(query4)
//				.evaluate());
//		}
	}

}
