# Smart Cache Storage - RDF

From `0.15.0` onwards these libraries provide a `rdf` module with common RDF storage related code:

- [`DatasetGraphFilteredUnionView`](#datasetgraphfilteredunionview)

## APIs

### `DatasetGraphFilteredUnionView`

[Apache Jena][Jena] already provides a `DatasetGraphFilteredView` wrapper which is used to apply a `QuadFilter` to quads
as they come off the underlying storage.  This is how [RDF-ABAC][RdfAbac] functions, when a user performs operations it
generates an instance of this class with a `QuadFilter` tailored to their permissions.

However as the platform has evolved and we've started to use named graphs more we've been exploring Jena's union default
graph functionality works.  For some of our use cases the standard `DatasetGraphFilteredView` works fine, however for
others it interacts poorl with the union graph feature.  Due to how our system is designed to fail closed this generally
results in users not seeing/operating over data when they expect to be able to do so.

The `DatasetGraphFilteredUnionView` class extends the base Jena implementation and overrides relevant methods to ensure
it properly honours union graph mode.  This allows all our use cases to work cleanly.

To use this class you construct it exactly the same as you would the standard Jena implementation, the only difference
is that union graph mode works cleanly regardless of any additional wrapping datasets that may be applied around it e.g.

```java
DatasetGraphFilteredUnionView view
  = new DatsetGraphFilteredUnionView(baseDataset, quadFilter, visibleGraphs);
```

[Jena]: https://jena.apache.org
[RdfAbac]: https://github.com/telicent-oss/rdf-abac

