
SRC = src/main/resources/test.proto
PB = src/main/resources/test.pb

all: pb

clean:
	rm $(PB)

pb: $(SRC)
	mkdir -p src/main/resources
	protoc --experimental_allow_proto3_optional --descriptor_set_out=$(PB) $(SRC)
