package secret.santa.application.interfaces

interface Listener<T> {
    fun DownloadComplete(arg: T)
}