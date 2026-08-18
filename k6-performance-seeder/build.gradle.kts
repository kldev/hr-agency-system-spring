plugins {
    kotlin("jvm") version "2.4.10"
    application
}

group = "com.pl.hragency"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("org.slf4j:slf4j-simple:2.0.17")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

application {
    mainClass.set("com.pl.hragency.seeder.MainKt")
}

tasks.test {
    useJUnitPlatform()
}