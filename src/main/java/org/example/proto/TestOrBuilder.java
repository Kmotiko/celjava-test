// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/resources/test.proto

package org.example.proto;

public interface TestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:test.Test)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>map&lt;string, string&gt; map_field_a = 1;</code>
   */
  int getMapFieldACount();
  /**
   * <code>map&lt;string, string&gt; map_field_a = 1;</code>
   */
  boolean containsMapFieldA(
      java.lang.String key);
  /**
   * Use {@link #getMapFieldAMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.String>
  getMapFieldA();
  /**
   * <code>map&lt;string, string&gt; map_field_a = 1;</code>
   */
  java.util.Map<java.lang.String, java.lang.String>
  getMapFieldAMap();
  /**
   * <code>map&lt;string, string&gt; map_field_a = 1;</code>
   */

  java.lang.String getMapFieldAOrDefault(
      java.lang.String key,
      java.lang.String defaultValue);
  /**
   * <code>map&lt;string, string&gt; map_field_a = 1;</code>
   */

  java.lang.String getMapFieldAOrThrow(
      java.lang.String key);

  /**
   * <code>.test.Test.Contents contents = 2;</code>
   * @return Whether the contents field is set.
   */
  boolean hasContents();
  /**
   * <code>.test.Test.Contents contents = 2;</code>
   * @return The contents.
   */
  org.example.proto.Test.Contents getContents();
  /**
   * <code>.test.Test.Contents contents = 2;</code>
   */
  org.example.proto.Test.ContentsOrBuilder getContentsOrBuilder();
}