plugins {
  kotlin("jvm") version "2.0.20"
}

group = "codes.jrave"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
  implementation("io.arrow-kt:arrow-core:1.2.4")
  implementation("io.arrow-kt:arrow-fx-coroutines:1.2.4")
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(21)
}