plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    application

    `maven-publish`
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
    mavenCentral()
}

val floggerVersion = "0.4"
val jacksonVersion = "2.10.1"

dependencies {
    implementation("com.google.flogger:flogger:${floggerVersion}")
    implementation("com.google.flogger:flogger-system-backend:${floggerVersion}")

    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:${jacksonVersion}")

    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

application {
    // Define the main class for the application
    mainClassName = "journal.Journal"
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}

tasks.processResources {
    expand(project.properties)
}

val installZip by tasks.register<Copy>("installZip") {
    dependsOn(tasks.distZip)

    var installDir = ""
    if (project.hasProperty("installDir")) {
        installDir = project.property("installDir").toString()
    }
    if (installDir.isEmpty()) {
        println("Please provide the 'installDir' property like this: -PinstallDir=\"/path/to/installation/folder\"")
        return@register
    }

    val zipFile = tasks.distZip.get().archiveFile
    val outputDir = file(installDir)

    from(zipTree(zipFile))
    into(outputDir)
}

val installDesktopScript by tasks.register<Copy>("installDesktopScript") {
    dependsOn(tasks.processResources)

    var userHome = ""
    if (project.hasProperty("userHome")) {
        userHome = project.property("userHome").toString()
    }
    if (userHome.isEmpty()) {
        println("Please provide the 'userHome' property like this: -PuserHome=\"/path/to/users/home/folder\"")
        return@register
    }

    val desktopScript = file("${project.buildDir.absolutePath}/resources/main/Journal.desktop")
    from(desktopScript)
    into("${userHome}/.local/share/applications")
}

tasks.register("install") {
    if (!project.hasProperty("installDir")) {
        println("Please provide the 'installDir' property like this: -PinstallDir=\"/path/to/installation/folder\"")
        return@register
    }

    dependsOn(installZip, installDesktopScript)
}
