exmaple code to confirm cel-java behavior.


## How to execute

serialize proto to FileDescriptorSet format and generate java code from proto

```
make all
```

do test with following command.

```
bazelisk run //src/test/java/org/example/cel/dynamicmessage:dynamicmessage_test
```
