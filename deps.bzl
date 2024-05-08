load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_jvm_external//:specs.bzl", "maven")

def celjava_test_maven_install():
  maven_install(
    artifacts = [
      "com.google.protobuf:protobuf-java:3.25.3",
      "com.google.protobuf:protobuf-java-util:3.25.3",
      "dev.cel:cel:0.5.1",
      "org.junit.jupiter:junit-jupiter:5.10.2",
      "org.junit.jupiter:junit-jupiter-api:5.10.2",
      "org.junit.jupiter:junit-jupiter-engine:5.10.2",
      "org.junit.platform:junit-platform-console:1.9.3",
      "org.junit.platform:junit-platform-launcher:1.9.3",
      "org.junit.platform:junit-platform-reporting:1.9.3",
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
    version_conflict_policy = "pinned",
    strict_visibility = True,
    generate_compat_repositories = True,
    fail_if_repin_required = True,
    maven_install_json = "//:maven_install.json",
  )
