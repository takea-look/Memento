package my.takealook.memento

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform