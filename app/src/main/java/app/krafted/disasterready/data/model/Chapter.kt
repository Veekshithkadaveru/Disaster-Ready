package app.krafted.disasterready.data.model

data class Chapter(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: String,
    val background: String,
    val accentColor: String,
    val quickFact: String,
    val tips: List<Tip>
)
