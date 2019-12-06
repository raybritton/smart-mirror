package app.raybritton.smartmirror.data.templates

data class FirewallTemplate(
    val inbound_rules: List<Rule> = listOf()
)

data class Rule(
    val protocol: String = "tcp",
    val ports: String,
    val sources: Sources
)

data class Sources(
    val addresses: List<String> = listOf( "0.0.0.0/0", "::/0")
)