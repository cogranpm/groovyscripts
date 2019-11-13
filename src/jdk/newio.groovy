package jdk
import java.nio.file.Path
import java.nio.file.Paths

println "hello"
Path homeDir = Paths.get(System.getProperty("user.home"))
println homeDir.getFileName()
println homeDir.getNameCount()
println homeDir.parent
println homeDir.root
println homeDir.toAbsolutePath()
println Paths.get(".").toRealPath()