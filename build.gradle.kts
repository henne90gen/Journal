import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    java
    application
    `maven-publish`
}

repositories {
    jcenter()
    mavenCentral()
}

val floggerVersion = "0.4"
val jacksonVersion = "2.10.1"
val googleVersion = "1.23.0"

dependencies {
    implementation("com.google.flogger:flogger:${floggerVersion}")
    implementation("com.google.flogger:flogger-system-backend:${floggerVersion}")

    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:${jacksonVersion}")

    implementation("com.google.api-client:google-api-client:${googleVersion}")
    implementation("com.google.oauth-client:google-oauth-client-jetty:${googleVersion}")
    implementation("com.google.apis:google-api-services-drive:v3-rev110-${googleVersion}")

    // Use JUnit Jupiter API and Engine for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

application {
    mainClassName = "journal.Journal"
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}

tasks.processResources {
    filesMatching("**/Journal.desktop") {
        expand(project.properties)
    }
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

    if (Os.isFamily(Os.FAMILY_MAC) or Os.isFamily(Os.FAMILY_WINDOWS)) {
        return@register
    }

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
