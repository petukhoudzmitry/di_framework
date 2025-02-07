plugins {
    id("java")
}

group = "com.diframework"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "${project.group}.Main")
    }

    from(configurations.runtimeClasspath.get().map{ zipTree(it) })
}


tasks.register("addictionRun", JavaExec::class) {
    dependsOn(tasks.build.name)
    mainClass = "${project.group}.Main"
    classpath = sourceSets.main.get().runtimeClasspath
}