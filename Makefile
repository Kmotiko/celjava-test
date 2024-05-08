
SRC = src/main/resources/test.proto
PB = src/main/resources/test.pb
JAVA = src/main/java/org/example/proto/Test.java src/main/java/org/example/proto/TestOrBuilder.java src/main/java/org/example/proto/TestOuterClass.java

all: pb java

clean:
	rm $(PB)
	rm $(JAVA)

pb: $(SRC)
	mkdir -p src/main/resources
	protoc --experimental_allow_proto3_optional --descriptor_set_out=$(PB) $(SRC)

java: $(SRC)
	 protoc --experimental_allow_proto3_optional --java_out=./src/main/java/ $(SRC)
