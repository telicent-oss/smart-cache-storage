# Smart Cache - Storage Libraries

This repository provides helper libraries around common storage layers for Smart Caches, this aims to allow developers
to focus effort on the actual details of a particular Smart Cache implementation rather than on the boilerplate involved
in using a particular kind of storage backend.

These libraries are primarily built around Telicent storage usage patterns so reflect our way of thinking, in particular
they emphasises:

- Atomic transactional operations (where supported by the underlying storage)
- Jackson as a serialization/deserialization framework for translating JSON and YAML to and from storage
- Following our common [Design
  Ethos][DesignEthos]

# Requirements

This is a Java based project that requires Maven to build.

Some tests **MAY** require Docker, you can disable this via the `docker` profile, i.e. `-P-docker`, if you don't have a
working Docker environment.  However, the Docker tests provide proper integration testing of the various APIs against
real storage backends running in containers so are recommended.

# Usage

Please refer to the [Usage Documentation](docs/index.md) for how to use these libraries.

## License

Copyright 2024-2026 Telicent Ltd, licensed under the [Apache 2.0](LICENSE) and [NOTICE](NOTICE).

[DesignEthos]: https://github.com/telicent-oss/smart-caches-core/blob/main/docs/design.md#design-ethos

