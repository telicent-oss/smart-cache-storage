package io.telicent.smart.cache.storage.rdf.iceberg;

import org.apache.iceberg.*;
import org.apache.iceberg.data.GenericAppenderFactory;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.FileAppenderFactory;
import org.apache.iceberg.io.OutputFileFactory;
import org.apache.iceberg.io.PartitionedFanoutWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a write transaction onto an Iceberg backed RDF dataset
 */
public class IcebergWriteTransaction {

    private final Table table;
    private final PartitionedFanoutWriter<Record> partitionedFanoutWriter;
    private final PartitionKey partitionKey;
    private final Record record;

    /**
     * Creates a new write transaction
     *
     * @param table          Iceberg table
     * @param targetFileSize Target file size for writes
     * @param partitionId    Partition ID
     * @param taskId         Task ID
     */
    public IcebergWriteTransaction(final Table table, final int targetFileSize, int partitionId, long taskId) {
        this.table = Objects.requireNonNull(table);

        FileAppenderFactory<org.apache.iceberg.data.Record> fileAppenderFactory =
                new GenericAppenderFactory(this.table.schema(), this.table.spec());
        OutputFileFactory outputFileFactory =
                OutputFileFactory.builderFor(this.table, partitionId, taskId).build();
        this.partitionKey = new PartitionKey(this.table.spec(), this.table.spec().schema());
        this.partitionedFanoutWriter =
                new PartitionedFanoutWriter<>(this.table.spec(), FileFormat.PARQUET, fileAppenderFactory,
                                              outputFileFactory, this.table.io(), targetFileSize) {
                    @Override
                    protected PartitionKey partition(Record record) {
                        partitionKey.partition(record);
                        return partitionKey;
                    }
                };
        record = GenericRecord.create(this.table.schema());
    }

    /**
     * Adds a quad to the current write transaction
     *
     * @param graph         Graph ID
     * @param subject       Subject ID
     * @param predicate     Predicate ID
     * @param object        Object ID
     * @param securityLabel Security Label ID
     * @throws IOException Thrown if write fails
     */
    public void add(long graph, long subject, long predicate, long object, long securityLabel) throws IOException {
        //@formatter:off
        Record record = this.record.copy(
                Map.of(IcebergFields.GRAPH, graph,
                       IcebergFields.SUBJECT, subject,
                       IcebergFields.PREDICATE, predicate,
                       IcebergFields.OBJECT, object,
                       IcebergFields.LABEL, securityLabel));
        //@formatter:on

        this.partitionedFanoutWriter.write(record);
    }

    /**
     * Commits the write transaction
     *
     * @throws IOException Thrown if the commit fails
     */
    public void commit() throws IOException {
        Transaction transaction = this.table.newTransaction();
        AppendFiles append = transaction.newAppend();
        Arrays.stream(this.partitionedFanoutWriter.dataFiles()).forEach(append::appendFile);
        append.commit();
        transaction.commitTransaction();
    }
}
