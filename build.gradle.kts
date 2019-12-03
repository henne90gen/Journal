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

publishing {
    repositories {
        maven {
            name = "Github"
            url = uri("https://maven.pkg.github.com/henne90gen/Journal")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
//                println(username)
//                if (this.password != null) {
//                    println(this.password!![0])
//                } else {
//                    println("No password set")
//                }
            }
        }
    }
    afterEvaluate {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                pom {
                    description.set("Journal Application")
                }
            }
        }
    }
}
