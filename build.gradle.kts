import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "com.ytdark"
version = "2.0.0"

repositories {
    mavenCentral()
}

val okHttpVersion = "4.12.0"
val kotlinxSerializationVersion = "1.6.3"
val cliktVersion = "4.3.0"
val lanternaVersion = "3.1.2"
val exposedVersion = "0.50.1"
val sqliteJdbcVersion = "3.45.3.0"
val flywayVersion = "10.12.0"
val commonsCsvVersion = "1.10.0"
val apachePoiVersion = "5.2.5"
val odfToolkitVersion = "0.9.0"
val slf4jVersion = "2.0.13"
val logbackVersion = "1.5.6"
val koinVersion = "3.5.6"
val junitVersion = "5.10.2"
val mockKVersion = "1.13.11"
val kotestVersion = "5.9.1"

dependencies {
    // HTTP
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    // JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    // CLI
    implementation("com.github.ajalt.clikt:clikt:$cliktVersion")

    // TUI
    implementation("com.googlecode.lanterna:lanterna:$lanternaVersion")

    // ORM + DB
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.xerial:sqlite-jdbc:$sqliteJdbcVersion")

    // Migrations
    implementation("org.flywaydb:flyway-core:$flywayVersion")

    // CSV
    implementation("org.apache.commons:commons-csv:$commonsCsvVersion")

    // XLSX
    implementation("org.apache.poi:poi:$apachePoiVersion")
    implementation("org.apache.poi:poi-ooxml:$apachePoiVersion")

    // ODS
    implementation("org.odftoolkit:simple-odf:$odfToolkitVersion")

    // Logging
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // DI
    implementation("io.insert-koin:koin-core:$koinVersion")

    // YAML config parsing (SnakeYAML)
    implementation("org.yaml:snakeyaml:2.2")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("io.mockk:mockk:$mockKVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.insert-koin:koin-test:$koinVersion")
}

application {
    mainClass.set("com.ytdark.MainKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("ytdark")
    archiveClassifier.set("all")
    archiveVersion.set("")
    mergeServiceFiles()
    manifest {
        attributes["Main-Class"] = "com.ytdark.MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
