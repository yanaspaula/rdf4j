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
import org.eclipse.rdf4j.model.Statement;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Håvard Ottestad
 */
@State(Scope.Benchmark)
@Warmup(iterations = 20)
@BenchmarkMode({ Mode.AverageTime })
@Fork(value = 1, jvmArgs = { "-Xms8G", "-Xmx8G", "-Xmn4G", "-XX:+UseSerialGC" })
//@Fork(value = 1, jvmArgs = {"-Xms8G", "-Xmx8G", "-Xmn4G", "-XX:+UseSerialGC", "-XX:+UnlockCommercialFeatures", "-XX:StartFlightRecording=delay=60s,duration=120s,filename=recording.jfr,settings=profile", "-XX:FlightRecorderOptions=samplethreads=true,stackdepth=1024", "-XX:+UnlockDiagnosticVMOptions", "-XX:+DebugNonSafepoints"})
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class IndexingBenchmark {

	Collection<Statement> model;

	@Setup(Level.Invocation)
	public void beforeClass() throws IOException, InterruptedException {

		model = new ArrayList<>(Rio.parse(getResourceAsStream("benchmarkFiles/bsbm-100.ttl"), "", RDFFormat.TURTLE));

		System.gc();

	}

	private static InputStream getResourceAsStream(String name) {
		return IndexingBenchmark.class.getClassLoader().getResourceAsStream(name);
	}

	@TearDown(Level.Trial)
	public void afterClass() {

	}

	@Benchmark
	public SailRepository initMemoryStore() {
		SailRepository memoryStore;

		memoryStore = new SailRepository(new MemoryStore());
		memoryStore.init();
		try (SailRepositoryConnection connection = memoryStore.getConnection()) {
			connection.begin(IsolationLevels.NONE);
			connection.add(model);
			connection.commit();
		}

		return memoryStore;
	}

	@Benchmark
	public SailRepository initCollection() {
		SailRepository repo = new SailRepository(new ReadOnlyStore(new CollectionReadOnlyBackendFactory(), model));
		repo.init();

		return repo;

	}

	@Benchmark
	public SailRepository initBplus() {
		SailRepository repo = new SailRepository(new ReadOnlyStore(new BPlusTreeReadOnlyBackendFactory(), model));
		repo.init();

		return repo;

	}

	@Benchmark
	public SailRepository initLinkedHashModel() {
		SailRepository repo = new SailRepository(new ReadOnlyStore(new LinkedHashModelReadOnlyBackendFactory(), model));
		repo.init();

		return repo;

	}

}
